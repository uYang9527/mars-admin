package com.mars.admin.controller;

import com.mars.admin.common.response.FilePreviewInfo;
import com.mars.admin.entity.SysOss;
import com.mars.admin.framework.common.Result;
import com.mars.admin.framework.common.annotation.OperationLog;
import com.mars.admin.framework.enums.BusinessType;
import com.mars.admin.service.IFileDownloadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 文件下载控制器
 *
 * @author Mars.wq
 */
@Slf4j
@RestController
@RequestMapping("/file")
@Tag(name = "文件下载管理", description = "文件下载和预览相关接口")
public class FileDownloadController {

    @Autowired
    private IFileDownloadService fileDownloadService;

    @Operation(summary = "下载文件")
    @Parameters({@Parameter(name = "fileId", description = "文件ID", required = true)})
    @GetMapping("/download/{fileId}")
    @OperationLog(title = "文件管理", businessType = BusinessType.DOWNLOAD, 
                  description = "下载文件：#{#fileId}", isSaveRequestData = false)
    public ResponseEntity<Resource> download(@PathVariable Long fileId) {
        try {
            SysOss fileInfo = fileDownloadService.getFileInfo(fileId);
            if (fileInfo == null) {
                return ResponseEntity.notFound().build();
            }

            // 获取文件流
            var downloadResult = fileDownloadService.downloadFile(fileId);
            if (downloadResult == null || downloadResult.getInputStream() == null) {
                return ResponseEntity.notFound().build();
            }

            // 读取文件流到字节数组
            byte[] fileBytes = readAllBytes(downloadResult.getInputStream());

            // 设置文件名编码
            String encodedFileName = URLEncoder.encode(fileInfo.getOriginalName(), StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            // 构建响应
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileInfo.getContentType()))
                    .contentLength(fileBytes.length)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                    .body(new ByteArrayResource(fileBytes));
        } catch (Exception e) {
            log.error("下载文件失败, ID: {}, 原因: {}", fileId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "获取文件预览信息")
    @Parameters({@Parameter(name = "fileId", description = "文件ID", required = true)})
    @GetMapping("/preview/info/{fileId}")
    public Result<FilePreviewInfo> getPreviewInfo(@PathVariable Long fileId) {
        try {
            FilePreviewInfo previewInfo = fileDownloadService.getPreviewInfo(fileId);
            return Result.success("获取预览信息成功", previewInfo);
        } catch (IOException e) {
            log.error("获取文件预览信息失败, ID: {}, 原因: {}", fileId, e.getMessage(), e);
            return Result.error("获取预览信息失败: " + e.getMessage());
        }
    }

    @Operation(summary = "在线预览文件")
    @Parameters({@Parameter(name = "fileId", description = "文件ID", required = true)})
    @GetMapping("/preview/{fileId}")
    @OperationLog(title = "文件管理", businessType = BusinessType.PREVIEW, 
                  description = "预览文件：#{#fileId}", isSaveRequestData = false)
    public ResponseEntity<Resource> preview(@PathVariable Long fileId) {
        try {
            SysOss fileInfo = fileDownloadService.getFileInfo(fileId);
            if (fileInfo == null) {
                return ResponseEntity.notFound().build();
            }

            // 获取文件流
            var downloadResult = fileDownloadService.downloadFile(fileId);
            if (downloadResult == null || downloadResult.getInputStream() == null) {
                return ResponseEntity.notFound().build();
            }

            // 读取文件流到字节数组
            byte[] fileBytes = readAllBytes(downloadResult.getInputStream());

            // 设置文件名编码
            String encodedFileName = URLEncoder.encode(fileInfo.getOriginalName(), StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            // 构建响应
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileInfo.getContentType()))
                    .contentLength(fileBytes.length)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename*=UTF-8''" + encodedFileName)
                    .body(new ByteArrayResource(fileBytes));
        } catch (Exception e) {
            log.error("预览文件失败, ID: {}, 原因: {}", fileId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 读取输入流中的所有字节
     */
    private byte[] readAllBytes(InputStream inputStream) throws IOException {
        // 对于已知大小的文件，可以预分配缓冲区大小
        byte[] buffer = new byte[8192];
        int bytesRead;
        byte[] result = new byte[0];
        int position = 0;
        
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            // 如果result缓冲区不够，则扩容
            if (position + bytesRead > result.length) {
                byte[] newResult = Arrays.copyOf(result, Math.max(result.length * 2, position + bytesRead));
                System.arraycopy(result, 0, newResult, 0, position);
                result = newResult;
            }
            
            // 将新读取的数据复制到result中
            System.arraycopy(buffer, 0, result, position, bytesRead);
            position += bytesRead;
        }
        
        // 如果最终的数组大小和已读取的大小不一致，裁剪数组
        return (position == result.length) ? result : Arrays.copyOf(result, position);
    }
} 