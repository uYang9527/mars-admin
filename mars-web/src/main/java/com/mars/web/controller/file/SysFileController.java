package com.mars.web.controller.file;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mars.common.result.PageResult;
import com.mars.common.result.Result;
import com.mars.system.annotation.Log;
import com.mars.system.annotation.Log.BusinessType;
import com.mars.system.entity.SysFile;
import com.mars.system.service.SysFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 文件管理
 */
@RestController
@RequestMapping("/sys/file")
@RequiredArgsConstructor
public class SysFileController {

    private final SysFileService fileService;

    /**
     * 分页查询文件列表
     */
    @GetMapping("/page")
    @SaCheckPermission("sys:file:list")
    public Result<PageResult<SysFile>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String originalName,
            @RequestParam(required = false) String fileType) {
        var result = fileService.page(page, pageSize, originalName, fileType);
        return Result.ok(PageResult.of(result));
    }

    /**
     * 获取文件详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("sys:file:list")
    public Result<SysFile> detail(@PathVariable Long id) {
        return Result.ok(fileService.getById(id));
    }

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @SaCheckPermission("sys:file:upload")
    @Log(title = "上传文件", businessType = BusinessType.INSERT)
    public Result<SysFile> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String path) {
        return Result.ok(fileService.upload(file, path));
    }

    /**
     * 上传图片
     */
    @PostMapping("/upload/image")
    @SaCheckPermission("sys:file:upload")
    @Log(title = "上传图片", businessType = BusinessType.INSERT)
    public Result<SysFile> uploadImage(@RequestParam("file") MultipartFile file) {
        return Result.ok(fileService.uploadImage(file));
    }

    /**
     * 下载文件
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {
        SysFile sysFile = fileService.getById(id);
        if (sysFile == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] bytes = fileService.getFileBytes(id);

        String encodedName = URLEncoder.encode(sysFile.getOriginalName(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedName)
                .header(HttpHeaders.CONTENT_TYPE, sysFile.getFileType())
                .body(bytes);
    }

    /**
     * 预览文件
     */
    @GetMapping("/preview/{id}")
    public ResponseEntity<byte[]> preview(@PathVariable Long id) {
        SysFile sysFile = fileService.getById(id);
        if (sysFile == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] bytes = fileService.getFileBytes(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, sysFile.getFileType())
                .body(bytes);
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/{id}")
    @SaCheckPermission("sys:file:delete")
    @Log(title = "删除文件", businessType = BusinessType.DELETE)
    public Result<Void> delete(@PathVariable Long id) {
        fileService.delete(id);
        return Result.ok();
    }

    /**
     * 批量删除文件
     */
    @DeleteMapping("/batch")
    @SaCheckPermission("sys:file:delete")
    @Log(title = "批量删除文件", businessType = BusinessType.DELETE)
    public Result<Void> deleteBatch(@RequestBody Long[] ids) {
        fileService.deleteBatch(ids);
        return Result.ok();
    }
}
