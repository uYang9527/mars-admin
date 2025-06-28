package com.mars.admin.framework.oss.strategy.impl;

import com.mars.admin.common.response.FileUploadResult;
import com.mars.admin.entity.SysOssConfig;
import com.mars.admin.framework.oss.strategy.FileUploadStrategy;
import com.mars.admin.framework.util.FileUtils;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * MinIO文件上传策略
 *
 * @author Mars.wq
 */
@Slf4j
@Component
public class MinioFileUploadStrategy implements FileUploadStrategy {

    @Override
    public String getType() {
        return "minio";
    }

    @Override
    public FileUploadResult upload(MultipartFile file, SysOssConfig config) {
        try {
            MinioClient minioClient = createMinioClient(config);
            
            String originalName = file.getOriginalFilename();
            String fileName = generateFileName(originalName);
            String objectName = buildObjectName(fileName, config);
            
            // 确保桶存在
            ensureBucketExists(minioClient, config.getBucketName());
            
            // 上传文件
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(config.getBucketName())
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
            
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
            log.error("MinIO文件上传失败", e);
            throw new RuntimeException("MinIO文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public FileUploadResult upload(InputStream inputStream, String fileName, String contentType, SysOssConfig config) {
        try {
            MinioClient minioClient = createMinioClient(config);
            
            String objectName = buildObjectName(fileName, config);
            
            // 确保桶存在
            ensureBucketExists(minioClient, config.getBucketName());
            
            // 由于无法直接获取InputStream的大小，使用-1让MinIO自动处理
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(config.getBucketName())
                    .object(objectName)
                    .stream(inputStream, -1, 10485760) // 10MB part size
                    .contentType(contentType)
                    .build()
            );
            
            // 获取文件信息
            StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(config.getBucketName())
                    .object(objectName)
                    .build()
            );
            
            // 构建访问URL
            String url = buildAccessUrl(config, objectName);
            
            return new FileUploadResult()
                    .setFileName(fileName)
                    .setOriginalName(fileName)
                    .setFileSuffix(FileUtils.getFileExtension(fileName))
                    .setUrl(url)
                    .setSize(stat.size())
                    .setFilePath(objectName)
                    .setContentType(contentType)
                    .setConfigKey(config.getConfigKey())
                    .setUploadStatus(1);
                    
        } catch (Exception e) {
            log.error("MinIO文件流上传失败", e);
            throw new RuntimeException("MinIO文件流上传失败: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(String fileName, SysOssConfig config) {
        try {
            MinioClient minioClient = createMinioClient(config);
            String objectName = config.getPrefix() + "/" + fileName;
            
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(config.getBucketName())
                    .object(objectName)
                    .build()
            );
            
            return true;
        } catch (Exception e) {
            log.error("删除MinIO文件失败: {}", fileName, e);
            return false;
        }
    }

    @Override
    public String getPresignedUrl(String fileName, SysOssConfig config, int expireTime) {
        try {
            MinioClient minioClient = createMinioClient(config);
            String objectName = config.getPrefix() + "/" + fileName;
            
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(config.getBucketName())
                    .object(objectName)
                    .expiry(expireTime, TimeUnit.SECONDS)
                    .build()
            );
        } catch (Exception e) {
            log.error("获取MinIO预签名URL失败: {}", fileName, e);
            return null;
        }
    }

    private MinioClient createMinioClient(SysOssConfig config) {
        return MinioClient.builder()
                .endpoint(config.getEndpoint())
                .credentials(config.getAccessKey(), config.getSecretKey())
                .region(config.getRegion())
                .build();
    }

    private void ensureBucketExists(MinioClient minioClient, String bucketName) throws Exception {
        boolean exists = minioClient.bucketExists(
            BucketExistsArgs.builder()
                .bucket(bucketName)
                .build()
        );
        
        if (!exists) {
            minioClient.makeBucket(
                MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build()
            );
            log.info("创建MinIO桶: {}", bucketName);
        }
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
            baseUrl = config.getEndpoint();
        }
        
        // 确保URL以/结尾
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        
        return baseUrl + config.getBucketName() + "/" + objectName;
    }
} 