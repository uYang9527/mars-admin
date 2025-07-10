package com.mars.admin.framework.oss.strategy.impl;

import com.mars.admin.common.response.FilePreviewInfo;
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
import java.io.FileInputStream;
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
            // 初始化上传路径
            initUploadPath();
            
            String originalName = file.getOriginalFilename();
            String fileName = generateFileName(originalName);
            String filePath = buildFilePath(fileName, config);
            String fullPath = getNormalizedPath(uploadPath, filePath);
            
            log.info("保存文件到: {}", fullPath);
            
            // 创建目录
            createDirectories(fullPath);
            
            // 保存文件
            File targetFile = new File(fullPath);
            try {
                file.transferTo(targetFile);
            } catch (IOException e) {
                log.error("保存文件失败: {}", e.getMessage());
                // 尝试使用替代方法保存
                try (FileOutputStream out = new FileOutputStream(targetFile)) {
                    out.write(file.getBytes());
                }
            }
            
            // 确认文件已保存成功
            if (!targetFile.exists()) {
                throw new IOException("文件保存失败，无法在目标路径找到文件: " + targetFile.getAbsolutePath());
            }
            
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
            log.error("本地文件上传失败: {}", e.getMessage(), e);
            throw new RuntimeException("本地文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public FileUploadResult upload(InputStream inputStream, String fileName, String contentType, SysOssConfig config) {
        try {
            // 初始化上传路径
            initUploadPath();
            
            String filePath = buildFilePath(fileName, config);
            String fullPath = getNormalizedPath(uploadPath, filePath);
            
            log.info("保存文件流到: {}", fullPath);
            
            // 创建目录
            createDirectories(fullPath);
            
            // 保存文件
            File targetFile = new File(fullPath);
            try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            
            // 获取文件大小
            long size = targetFile.length();
            
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
            log.error("本地文件流上传失败: {}", e.getMessage(), e);
            throw new RuntimeException("本地文件流上传失败: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(String fileName, SysOssConfig config) {
        try {
            String filePath = config.getPrefix() + File.separator + fileName;
            String fullPath = getNormalizedPath(uploadPath, filePath);
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

    @Override
    public InputStream getFileInputStream(String fileName, SysOssConfig config) throws IOException {
        String fullPath = getNormalizedPath(uploadPath, fileName);
        log.debug("获取文件流: {}", fullPath);
        
        File file = new File(fullPath);
        if (!file.exists() || !file.isFile()) {
            log.error("文件不存在: {}", fullPath);
            throw new IOException("文件不存在: " + fileName);
        }
        
        return new FileInputStream(file);
    }
    
    @Override
    public FilePreviewInfo getPreviewInfo(String fileName, String originalName, String contentType, long size, SysOssConfig config) throws IOException {
        String fullPath = getNormalizedPath(uploadPath, fileName);
        File file = new File(fullPath);
        if (!file.exists() || !file.isFile()) {
            log.error("文件不存在: {}", fullPath);
            throw new IOException("文件不存在: " + fileName);
        }
        
        // 构建URL
        String url = buildAccessUrl(fileName);
        
        // 判断文件类型，确定预览类型
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
    }
    
    private String generateFileName(String originalName) {
        String extension = FileUtils.getFileExtension(originalName);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid + (StringUtils.hasText(extension) ? "." + extension : "");
    }

    private String buildFilePath(String fileName, SysOssConfig config) {
        // 直接使用日期路径，不添加额外的前缀，避免重复的upload目录
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return datePath + File.separator + fileName;
    }
    
    /**
     * 初始化上传根目录
     */
    private void initUploadPath() throws IOException {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            log.info("创建上传根目录: {}", uploadPath);
            if (!uploadDir.mkdirs()) {
                throw new IOException("无法创建上传根目录: " + uploadPath);
            }
        }
    }

    /**
     * 获取规范化的完整路径
     */
    private String getNormalizedPath(String basePath, String relativePath) {
        // 确保基础路径不以分隔符结尾
        if (basePath.endsWith(File.separator)) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }
        
        // 确保相对路径不以分隔符开头
        if (relativePath.startsWith(File.separator)) {
            relativePath = relativePath.substring(1);
        }
        
        // 组合并规范化路径
        Path path = Paths.get(basePath, relativePath).normalize();
        return path.toString();
    }

    private void createDirectories(String fullPath) throws IOException {
        try {
            Path path = Paths.get(fullPath).getParent();
            if (path != null && !Files.exists(path)) {
                log.info("创建目录: {}", path);
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            log.error("创建目录失败: {}", e.getMessage(), e);
            
            // 尝试使用File API创建目录
            File directory = new File(Paths.get(fullPath).getParent().toString());
            if (!directory.exists() && !directory.mkdirs()) {
                throw new IOException("无法创建目录: " + directory.getAbsolutePath());
            }
        }
    }

    private String buildAccessUrl(String filePath) {
        // 构建完整的访问URL
        String baseUrl = "";
        // 获取当前主机名和端口
        baseUrl = "http://localhost:" + serverPort;
        if (StringUtils.hasText(contextPath) && !contextPath.equals("/")) {
            baseUrl += contextPath;
        }
        // 将Windows风格的路径分隔符替换为URL路径分隔符
        String normalizedPath = filePath.replace(File.separator, "/");
        return baseUrl + "/file/" + normalizedPath;
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
} 