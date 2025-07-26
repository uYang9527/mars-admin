package com.mars.admin.modules.base.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 本地文件访问控制器
 *
 * @author Mars.wq
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class LocalFileController {

    @Value("${file.upload.path:./upload}")
    private String uploadPath;

    @GetMapping("/**")
    public ResponseEntity<Resource> getFile(HttpServletRequest request) {
        try {
            // 获取文件路径
            String requestURI = request.getRequestURI();
            String filePath = requestURI.substring("/file/".length());

            // 处理可能的编码问题
            filePath = java.net.URLDecoder.decode(filePath, "UTF-8");

            // 构建完整的文件路径
            Path fullPath = Paths.get(uploadPath, filePath).normalize();
            File file = fullPath.toFile();

            if (!file.exists() || !file.isFile()) {
                log.warn("请求的文件不存在: {}", fullPath);
                return ResponseEntity.notFound().build();
            }

            // 安全检查：确保文件在上传目录内
            Path uploadDir = Paths.get(uploadPath).normalize();
            if (!fullPath.startsWith(uploadDir)) {
                log.warn("尝试访问上传目录外的文件: {}", fullPath);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Resource resource = new FileSystemResource(file);

            // 获取文件的MIME类型
            String contentType = Files.probeContentType(fullPath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                .body(resource);

        } catch (Exception e) {
            log.error("访问本地文件失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
