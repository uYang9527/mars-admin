package com.mars.admin.framework.oss.strategy.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.mars.admin.common.response.FileUploadResult;
import com.mars.admin.entity.SysOssConfig;
import com.mars.admin.framework.oss.strategy.FileUploadStrategy;
import com.mars.admin.framework.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
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
} 