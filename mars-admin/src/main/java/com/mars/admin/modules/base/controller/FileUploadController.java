package com.mars.admin.modules.base.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mars.admin.common.response.FileUploadResult;
import com.mars.admin.modules.system.entity.SysOss;
import com.mars.admin.framework.common.Result;
import com.mars.admin.framework.common.annotation.OperationLog;
import com.mars.admin.framework.enums.BusinessType;
import com.mars.admin.modules.system.service.IFileUploadService;
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

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static com.mars.admin.modules.system.entity.table.SysOssTableDef.SYS_OSS;


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
            log.warn("上传失败：文件为空");
            return Result.error("上传文件不能为空");
        }

        log.info("开始上传文件: {}, 大小: {}, 配置: {}",
                file.getOriginalFilename(),
                file.getSize(),
                StringUtils.hasText(configKey) ? configKey : "默认配置");

        try {
            FileUploadResult result;
            if (StringUtils.hasText(configKey)) {
                result = fileUploadService.upload(file, configKey);
            } else {
                result = fileUploadService.upload(file);
            }
            log.info("文件上传成功: {}, URL: {}", file.getOriginalFilename(), result.getUrl());
            return Result.success("上传成功", result);
        } catch (Exception e) {
            log.error("文件上传失败: {}, 原因: {}", file.getOriginalFilename(), e.getMessage(), e);

            // 根据异常类型提供更具体的错误信息
            String errorMessage = "上传失败";
            if (e instanceof IOException) {
                errorMessage += ": 文件读写错误";
            } else if (e.getMessage().contains("配置不存在")) {
                errorMessage += ": 存储配置不存在或无效";
            } else if (e.getMessage().contains("权限")) {
                errorMessage += ": 存储权限不足";
            } else {
                errorMessage += ": " + e.getMessage();
            }

            return Result.error(errorMessage);
        }
    }

    @Operation(summary = "删除文件")
    @Parameter(name = "id", description = "文件ID", required = true)
    @SaCheckPermission("system:file:delete")
    @DeleteMapping("/{id}")
    @OperationLog(title = "文件管理", businessType = BusinessType.DELETE,
            description = "删除文件，文件ID：#{#id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("开始删除文件, ID: {}", id);
        try {
            boolean success = fileUploadService.delete(id);
            if (success) {
                log.info("文件删除成功, ID: {}", id);
                return Result.success();
            } else {
                log.warn("文件删除失败, ID: {}, 可能文件不存在", id);
                return Result.error("删除失败，文件可能不存在");
            }
        } catch (Exception e) {
            log.error("删除文件失败, ID: {}, 原因: {}", id, e.getMessage(), e);
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
        log.info("开始批量删除文件, IDs: {}", ids);
        if (ids == null || ids.isEmpty()) {
            return Result.error("请选择要删除的文件");
        }

        try {
            boolean success = fileUploadService.batchDelete(ids);
            if (success) {
                log.info("批量删除文件成功, IDs: {}", ids);
                return Result.success();
            } else {
                log.warn("批量删除文件部分失败, IDs: {}", ids);
                return Result.error("部分文件删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除文件失败, IDs: {}, 原因: {}", ids, e.getMessage(), e);
            return Result.error("批量删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取文件预签名URL")
    @Parameters({@Parameter(name = "id", description = "文件ID", required = true), @Parameter(name = "expireTime", description = "过期时间(秒)", required = false)})
    @GetMapping("/{id}/presigned-url")
    public Result<String> getPresignedUrl(@PathVariable Long id, @RequestParam(value = "expireTime", defaultValue = "3600") int expireTime) {
        log.info("开始获取文件预签名URL, ID: {}, 过期时间: {}秒", id, expireTime);
        try {
            String url = fileUploadService.getPresignedUrl(id, expireTime);
            if (url != null) {
                log.info("获取文件预签名URL成功, ID: {}", id);
                return Result.success("获取成功", url);
            } else {
                log.warn("获取文件预签名URL失败, ID: {}, 可能文件不存在", id);
                return Result.error("获取预签名URL失败，文件可能不存在");
            }
        } catch (Exception e) {
            log.error("获取文件预签名URL失败, ID: {}, 原因: {}", id, e.getMessage(), e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    @Operation(summary = "分页查询文件列表")
    @Parameters({@Parameter(name = "current", description = "当前页", required = true), @Parameter(name = "size", description = "页大小", required = true), @Parameter(name = "fileName", description = "文件名"), @Parameter(name = "configKey", description = "配置key"), @Parameter(name = "contentType", description = "文件类型")})
    @SaCheckPermission("system:file:list")
    @GetMapping("/page")
    public Result<Page<SysOss>> page(@RequestParam(value = "current", defaultValue = "1") long current, @RequestParam(value = "size", defaultValue = "10") long size, @RequestParam(value = "fileName", required = false) String fileName, @RequestParam(value = "configKey", required = false) String configKey, @RequestParam(value = "contentType", required = false) String contentType) {

        log.debug("查询文件列表, 页码: {}, 每页大小: {}, 文件名: {}, 配置: {}, 类型: {}",
                current, size, fileName, configKey, contentType);

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

        try {
            Page<SysOss> result = fileUploadService.selectFilePage(page, queryWrapper);
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询文件列表失败, 原因: {}", e.getMessage(), e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据ID获取文件信息")
    @Parameter(name = "id", description = "文件ID", required = true)
    @GetMapping("/{id}")
    public Result<SysOss> getById(@PathVariable Long id) {
        log.debug("获取文件信息, ID: {}", id);
        try {
            SysOss file = fileUploadService.getFileById(id);
            if (file != null) {
                return Result.success("获取成功", file);
            } else {
                log.warn("文件不存在, ID: {}", id);
                return Result.error("文件不存在");
            }
        } catch (Exception e) {
            log.error("获取文件信息失败, ID: {}, 原因: {}", id, e.getMessage(), e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取可用的上传策略类型")
    @GetMapping("/types")
    public Result<Set<String>> getAvailableTypes() {
        log.debug("获取可用的上传策略类型");
        try {
            Set<String> types = fileUploadService.getAvailableTypes();
            return Result.success("获取成功", types);
        } catch (Exception e) {
            log.error("获取可用上传策略类型失败, 原因: {}", e.getMessage(), e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }
}
