package com.mars.admin.framework.oss.strategy.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.mars.admin.common.response.FileUploadResult;
import com.mars.admin.common.response.FilePreviewInfo;
import com.mars.admin.modules.system.entity.SysOssConfig;
import com.mars.admin.framework.oss.strategy.FileUploadStrategy;
import com.mars.admin.framework.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

/**
 * 阿里云OSS文件上传策略
 *
 * @author Mars.wq
 */
@Slf4j
@Component
public class AliyunOssFileUploadStrategy implements FileUploadStrategy {

    @Override
    public String getType() {
        return "aliyun";
    }

    @Override
    public FileUploadResult upload(MultipartFile file, SysOssConfig config) {
        OSS ossClient = null;
        try {
            ossClient = createOssClient(config);

            String originalName = file.getOriginalFilename();
            String fileName = generateFileName(originalName);
            String objectName = buildObjectName(fileName, config);

            // 创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            metadata.setCacheControl("no-cache");
            metadata.setHeader("Pragma", "no-cache");
            metadata.setContentDisposition("inline;filename=" + originalName);

            // 上传文件
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                config.getBucketName(), objectName, file.getInputStream(), metadata);
            ossClient.putObject(putObjectRequest);

            // 构建访问URL
            String url = buildAccessUrl(config, objectName);

            return new FileUploadResult()
                    .setFileName(fileName)
                    .setOriginalName(originalName)
                    .setFileSuffix(FileUtils.getFileExtension(originalName))
                    .setUrl(url)
                    .setSize(file.getSize())
                    .setFilePath(objectName)
                    .setContentType(file.getContentType())
                    .setConfigKey(config.getConfigKey())
                    .setUploadStatus(1);

        } catch (Exception e) {
            log.error("阿里云OSS文件上传失败", e);
            throw new RuntimeException("阿里云OSS文件上传失败: " + e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Override
    public FileUploadResult upload(InputStream inputStream, String fileName, String contentType, SysOssConfig config) {
        OSS ossClient = null;
        try {
            ossClient = createOssClient(config);

            String objectName = buildObjectName(fileName, config);

            // 创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setCacheControl("no-cache");
            metadata.setHeader("Pragma", "no-cache");
            metadata.setContentDisposition("inline;filename=" + fileName);

            // 上传文件
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                config.getBucketName(), objectName, inputStream, metadata);
            ossClient.putObject(putObjectRequest);

            // 获取文件信息
            com.aliyun.oss.model.ObjectMetadata objMetadata = ossClient.getObjectMetadata(config.getBucketName(), objectName);
            long size = objMetadata.getContentLength();

            // 构建访问URL
            String url = buildAccessUrl(config, objectName);

            return new FileUploadResult()
                    .setFileName(fileName)
                    .setOriginalName(fileName)
                    .setFileSuffix(FileUtils.getFileExtension(fileName))
                    .setUrl(url)
                    .setSize(size)
                    .setFilePath(objectName)
                    .setContentType(contentType)
                    .setConfigKey(config.getConfigKey())
                    .setUploadStatus(1);

        } catch (Exception e) {
            log.error("阿里云OSS文件流上传失败", e);
            throw new RuntimeException("阿里云OSS文件流上传失败: " + e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Override
    public boolean delete(String fileName, SysOssConfig config) {
        OSS ossClient = null;
        try {
            ossClient = createOssClient(config);
            String objectName = config.getPrefix() + "/" + fileName;
            ossClient.deleteObject(config.getBucketName(), objectName);
            return true;
        } catch (Exception e) {
            log.error("删除阿里云OSS文件失败: {}", fileName, e);
            return false;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Override
    public String getPresignedUrl(String fileName, SysOssConfig config, int expireTime) {
        OSS ossClient = null;
        try {
            ossClient = createOssClient(config);
            String objectName = config.getPrefix() + "/" + fileName;

            // 设置过期时间
            Date expiration = new Date(System.currentTimeMillis() + expireTime * 1000L);

            return ossClient.generatePresignedUrl(config.getBucketName(), objectName, expiration).toString();
        } catch (Exception e) {
            log.error("获取阿里云OSS预签名URL失败: {}", fileName, e);
            return null;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Override
    public InputStream getFileInputStream(String fileName, SysOssConfig config) throws IOException {
        OSS ossClient = null;
        try {
            ossClient = createOssClient(config);

            // 获取文件流
            OSSObject ossObject = ossClient.getObject(config.getBucketName(), fileName);
            return ossObject.getObjectContent();
        } catch (Exception e) {
            log.error("获取阿里云OSS文件流失败: {}", e.getMessage(), e);
            throw new IOException("获取阿里云OSS文件流失败: " + e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Override
    public FilePreviewInfo getPreviewInfo(String fileName, String originalName, String contentType, long size, SysOssConfig config) throws IOException {
        OSS ossClient = null;
        try {
            ossClient = createOssClient(config);

            // 检查文件是否存在
            boolean exists = ossClient.doesObjectExist(config.getBucketName(), fileName);
            if (!exists) {
                log.error("阿里云OSS文件不存在: {}", fileName);
                throw new IOException("文件不存在: " + fileName);
            }

            // 获取签名URL，默认10分钟有效期
            Date expiration = new Date(System.currentTimeMillis() + 10 * 60 * 1000);
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(config.getBucketName(), fileName);
            request.setExpiration(expiration);
            URL signedUrl = ossClient.generatePresignedUrl(request);
            String url = signedUrl.toString();

            // 构建预览信息
            FilePreviewInfo previewInfo = new FilePreviewInfo()
                    .setPreviewUrl(url)
                    .setContentType(contentType)
                    .setOriginalName(originalName)
                    .setSize(size);

            // 设置预览类型
            if (isImageFile(contentType)) {
                // 图片直接预览
                previewInfo.setPreviewType(1);
            } else if (isPdfFile(contentType)) {
                // PDF直接预览
                previewInfo.setPreviewType(3);
            } else if (isOfficeFile(contentType)) {
                // Office文档需要预览服务
                previewInfo.setPreviewType(2);
                previewInfo.setNeedConvert(true);
            } else if (isVideoFile(contentType)) {
                // 视频文件
                previewInfo.setPreviewType(4);
            } else if (isAudioFile(contentType)) {
                // 音频文件
                previewInfo.setPreviewType(5);
            } else if (isTextFile(contentType, originalName)) {
                // 文本文件
                previewInfo.setPreviewType(6);
            } else {
                // 不支持预览
                previewInfo.setPreviewType(0);
            }

            return previewInfo;
        } catch (Exception e) {
            log.error("获取阿里云OSS预览信息失败: {}", e.getMessage(), e);
            throw new IOException("获取阿里云OSS预览信息失败: " + e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 判断是否为文本文件
     */
    private boolean isTextFile(String contentType, String fileName) {
        // 根据内容类型判断
        if (contentType != null && (
                contentType.equals("text/plain") ||
                contentType.equals("text/html") ||
                contentType.equals("text/css") ||
                contentType.equals("text/javascript") ||
                contentType.equals("application/json") ||
                contentType.equals("application/xml"))) {
            return true;
        }

        // 根据文件扩展名判断
        if (fileName != null) {
            String lowerName = fileName.toLowerCase();
            return lowerName.endsWith(".txt") ||
                   lowerName.endsWith(".log") ||
                   lowerName.endsWith(".ini") ||
                   lowerName.endsWith(".json") ||
                   lowerName.endsWith(".xml") ||
                   lowerName.endsWith(".md") ||
                   lowerName.endsWith(".csv");
        }

        return false;
    }

    private OSS createOssClient(SysOssConfig config) {
        String endpoint = config.getEndpoint();
        if (config.getIsHttps() != null && config.getIsHttps() == 1) {
            if (!endpoint.startsWith("https://")) {
                endpoint = "https://" + endpoint.replace("http://", "");
            }
        } else {
            if (!endpoint.startsWith("http://")) {
                endpoint = "http://" + endpoint.replace("https://", "");
            }
        }

        return new OSSClientBuilder().build(endpoint, config.getAccessKey(), config.getSecretKey());
    }

    private String generateFileName(String originalName) {
        String extension = FileUtils.getFileExtension(originalName);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid + (StringUtils.hasText(extension) ? "." + extension : "");
    }

    private String buildObjectName(String fileName, SysOssConfig config) {
        String prefix = StringUtils.hasText(config.getPrefix()) ? config.getPrefix() : "upload";
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return prefix + "/" + datePath + "/" + fileName;
    }

    private String buildAccessUrl(SysOssConfig config, String objectName) {
        String baseUrl = config.getDomain();
        if (!StringUtils.hasText(baseUrl)) {
            String endpoint = config.getEndpoint();
            if (config.getIsHttps() != null && config.getIsHttps() == 1) {
                baseUrl = "https://" + config.getBucketName() + "." + endpoint.replace("https://", "").replace("http://", "");
            } else {
                baseUrl = "http://" + config.getBucketName() + "." + endpoint.replace("https://", "").replace("http://", "");
            }
        }

        // 确保URL以/结尾
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }

        return baseUrl + objectName;
    }

    /**
     * 判断是否为图片文件
     */
    private boolean isImageFile(String contentType) {
        return contentType != null && contentType.startsWith("image/");
    }

    /**
     * 判断是否为PDF文件
     */
    private boolean isPdfFile(String contentType) {
        return contentType != null && contentType.equals("application/pdf");
    }

    /**
     * 判断是否为Office文件
     */
    private boolean isOfficeFile(String contentType) {
        if (contentType == null) return false;
        return contentType.contains("word") ||
               contentType.contains("excel") ||
               contentType.contains("powerpoint") ||
               contentType.contains("ms-excel") ||
               contentType.contains("ms-word") ||
               contentType.contains("ms-powerpoint") ||
               contentType.contains("openxmlformats");
    }

    /**
     * 判断是否为视频文件
     */
    private boolean isVideoFile(String contentType) {
        return contentType != null && contentType.startsWith("video/");
    }

    /**
     * 判断是否为音频文件
     */
    private boolean isAudioFile(String contentType) {
        return contentType != null && contentType.startsWith("audio/");
    }
}
