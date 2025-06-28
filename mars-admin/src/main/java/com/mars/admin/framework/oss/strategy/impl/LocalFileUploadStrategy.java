package com.mars.admin.framework.oss.strategy.impl;

import com.mars.admin.common.response.FileUploadResult;
import com.mars.admin.entity.SysOssConfig;
import com.mars.admin.framework.oss.strategy.FileUploadStrategy;
import com.mars.admin.framework.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 本地文件上传策略
 *
 * @author Mars.wq
 */
@Slf4j
@Component
public class LocalFileUploadStrategy implements FileUploadStrategy {

    @Value("${file.upload.path:./upload}")
    private String uploadPath;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Value("${server.port:8080}")
    private String serverPort;

    @Override
    public String getType() {
        return "local";
    }

    @Override
    public FileUploadResult upload(MultipartFile file, SysOssConfig config) {
        try {
            String originalName = file.getOriginalFilename();
            String fileName = generateFileName(originalName);
            String filePath = buildFilePath(fileName, config);
            String fullPath = uploadPath + File.separator + filePath;
            
            // 创建目录
            createDirectories(fullPath);
            
            // 保存文件
            File targetFile = new File(fullPath);
            file.transferTo(targetFile);
            
            // 构建访问URL
            String url = buildAccessUrl(filePath);
            
            return new FileUploadResult()
                    .setFileName(fileName)
                    .setOriginalName(originalName)
                    .setFileSuffix(FileUtils.getFileExtension(originalName))
                    .setUrl(url)
                    .setSize(file.getSize())
                    .setFilePath(filePath)
                    .setContentType(file.getContentType())
                    .setConfigKey(config.getConfigKey())
                    .setUploadStatus(1);
                    
        } catch (IOException e) {
            log.error("本地文件上传失败", e);
            throw new RuntimeException("本地文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public FileUploadResult upload(InputStream inputStream, String fileName, String contentType, SysOssConfig config) {
        try {
            String filePath = buildFilePath(fileName, config);
            String fullPath = uploadPath + File.separator + filePath;
            
            // 创建目录
            createDirectories(fullPath);
            
            // 保存文件
            try (FileOutputStream outputStream = new FileOutputStream(fullPath)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            
            // 获取文件大小
            long size = new File(fullPath).length();
            
            // 构建访问URL
            String url = buildAccessUrl(filePath);
            
            return new FileUploadResult()
                    .setFileName(fileName)
                    .setOriginalName(fileName)
                    .setFileSuffix(FileUtils.getFileExtension(fileName))
                    .setUrl(url)
                    .setSize(size)
                    .setFilePath(filePath)
                    .setContentType(contentType)
                    .setConfigKey(config.getConfigKey())
                    .setUploadStatus(1);
                    
        } catch (IOException e) {
            log.error("本地文件流上传失败", e);
            throw new RuntimeException("本地文件流上传失败: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(String fileName, SysOssConfig config) {
        try {
            String filePath = config.getPrefix() + File.separator + fileName;
            String fullPath = uploadPath + File.separator + filePath;
            File file = new File(fullPath);
            if (file.exists()) {
                return file.delete();
            }
            return true;
        } catch (Exception e) {
            log.error("删除本地文件失败: {}", fileName, e);
            return false;
        }
    }

    @Override
    public String getPresignedUrl(String fileName, SysOssConfig config, int expireTime) {
        // 本地存储不需要预签名URL，直接返回访问URL
        String filePath = config.getPrefix() + File.separator + fileName;
        return buildAccessUrl(filePath);
    }

    private String generateFileName(String originalName) {
        String extension = FileUtils.getFileExtension(originalName);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid + (StringUtils.hasText(extension) ? "." + extension : "");
    }

    private String buildFilePath(String fileName, SysOssConfig config) {
        String prefix = StringUtils.hasText(config.getPrefix()) ? config.getPrefix() : "upload";
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return prefix + File.separator + datePath + File.separator + fileName;
    }

    private void createDirectories(String fullPath) throws IOException {
        Path path = Paths.get(fullPath).getParent();
        if (path != null && !Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    private String buildAccessUrl(String filePath) {
        String baseUrl = "http://localhost:" + serverPort;
        if (StringUtils.hasText(contextPath)) {
            baseUrl += contextPath;
        }
        return baseUrl + "/file/" + filePath.replace(File.separator, "/");
    }
} 