package com.mars.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mars.admin.common.response.FileUploadResult;
import com.mars.admin.entity.SysOss;
import com.mars.admin.framework.common.Result;
import com.mars.admin.framework.common.annotation.OperationLog;
import com.mars.admin.framework.enums.BusinessType;
import com.mars.admin.service.IFileUploadService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

import static com.mars.admin.entity.table.SysOssTableDef.SYS_OSS;

/**
 * 文件上传控制器
 *
 * @author Mars.wq
 */
@Slf4j
@RestController
@RequestMapping("/file")
@Tag(name = "文件上传管理", description = "文件上传相关接口")
public class FileUploadController {

    @Autowired
    private IFileUploadService fileUploadService;

    @Operation(summary = "上传文件")
    @Parameters({@Parameter(name = "file", description = "上传的文件", required = true), @Parameter(name = "configKey", description = "OSS配置key，不传则使用默认配置")})
    @PostMapping("/upload")
    @OperationLog(title = "文件管理", businessType = BusinessType.UPLOAD, 
                  description = "上传文件：#{#file.originalFilename}", isSaveRequestData = false)
    public Result<FileUploadResult> upload(@RequestParam("file") MultipartFile file, @RequestParam(value = "configKey", required = false) String configKey) {

        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }
        try {
            FileUploadResult result;
            if (StringUtils.hasText(configKey)) {
                result = fileUploadService.upload(file, configKey);
            } else {
                result = fileUploadService.upload(file);
            }
            return Result.success("上传成功", result);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return Result.error("上传失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除文件")
    @Parameter(name = "id", description = "文件ID", required = true)
    @SaCheckPermission("system:file:delete")
    @DeleteMapping("/{id}")
    @OperationLog(title = "文件管理", businessType = BusinessType.DELETE, 
                  description = "删除文件，文件ID：#{#id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            boolean success = fileUploadService.delete(id);
            if (success) {
                return Result.success();
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除文件失败", e);
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量删除文件")
    @Parameter(name = "ids", description = "文件ID列表", required = true)
    @SaCheckPermission("system:file:delete")
    @DeleteMapping("/batch")
    @OperationLog(title = "文件管理", businessType = BusinessType.DELETE, 
                  description = "批量删除文件，ID列表：#{#ids}")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        try {
            boolean success = fileUploadService.batchDelete(ids);
            if (success) {
                return Result.success();
            } else {
                return Result.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除文件失败", e);
            return Result.error("批量删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取文件预签名URL")
    @Parameters({@Parameter(name = "id", description = "文件ID", required = true), @Parameter(name = "expireTime", description = "过期时间(秒)", required = false)})
    @GetMapping("/{id}/presigned-url")
    public Result<String> getPresignedUrl(@PathVariable Long id, @RequestParam(value = "expireTime", defaultValue = "3600") int expireTime) {

        try {
            String url = fileUploadService.getPresignedUrl(id, expireTime);
            if (url != null) {
                return Result.success("获取成功", url);
            } else {
                return Result.error("获取预签名URL失败");
            }
        } catch (Exception e) {
            log.error("获取预签名URL失败", e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    @Operation(summary = "分页查询文件列表")
    @Parameters({@Parameter(name = "current", description = "当前页", required = true), @Parameter(name = "size", description = "页大小", required = true), @Parameter(name = "fileName", description = "文件名"), @Parameter(name = "configKey", description = "配置key"), @Parameter(name = "contentType", description = "文件类型")})
    @SaCheckPermission("system:file:list")
    @GetMapping("/page")
    public Result<Page<SysOss>> page(@RequestParam(value = "current", defaultValue = "1") long current, @RequestParam(value = "size", defaultValue = "10") long size, @RequestParam(value = "fileName", required = false) String fileName, @RequestParam(value = "configKey", required = false) String configKey, @RequestParam(value = "contentType", required = false) String contentType) {

        Page<SysOss> page = new Page<>(current, size);
        QueryWrapper queryWrapper = QueryWrapper.create().where(SYS_OSS.IS_DELETED.eq(0));

        if (StringUtils.hasText(fileName)) {
            queryWrapper.and(SYS_OSS.FILE_NAME.like(fileName));
        }
        if (StringUtils.hasText(configKey)) {
            queryWrapper.and(SYS_OSS.CONFIG_KEY.eq(configKey));
        }
        if (StringUtils.hasText(contentType)) {
            queryWrapper.and(SYS_OSS.CONTENT_TYPE.like(contentType));
        }

        queryWrapper.orderBy(SYS_OSS.CREATE_TIME.desc());

        Page<SysOss> result = fileUploadService.selectFilePage(page, queryWrapper);
        return Result.success(result);
    }

    @Operation(summary = "根据ID获取文件信息")
    @Parameter(name = "id", description = "文件ID", required = true)
    @GetMapping("/{id}")
    public Result<SysOss> getById(@PathVariable Long id) {
        SysOss file = fileUploadService.getFileById(id);
        if (file != null) {
            return Result.success("获取成功", file);
        } else {
            return Result.error("文件不存在");
        }
    }

    @Operation(summary = "获取可用的上传策略类型")
    @GetMapping("/types")
    public Result<Set<String>> getAvailableTypes() {
        Set<String> types = fileUploadService.getAvailableTypes();
        return Result.success("获取成功", types);
    }
}
