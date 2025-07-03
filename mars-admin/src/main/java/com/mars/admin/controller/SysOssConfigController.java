package com.mars.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mars.admin.entity.SysOssConfig;
import com.mars.admin.framework.common.Result;
import com.mars.admin.service.ISysOssConfigService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mars.admin.entity.table.SysOssConfigTableDef.SYS_OSS_CONFIG;

/**
 * OSS配置控制器
 *
 * @author Mars.wq
 */
@Slf4j
@RestController
@RequestMapping("/system/oss-config")
@Tag(name = "OSS配置管理", description = "OSS配置相关接口")
public class SysOssConfigController {

    @Autowired
    private ISysOssConfigService ossConfigService;

    @Operation(summary = "分页查询OSS配置列表")
    @Parameters({@Parameter(name = "current", description = "当前页", required = true), @Parameter(name = "size", description = "页大小", required = true), @Parameter(name = "configKey", description = "配置key"), @Parameter(name = "status", description = "状态")})
    @SaCheckPermission("system:ossConfig:list")
    @GetMapping("/page")
    public Result<Page<SysOssConfig>> page(@RequestParam(value = "current", defaultValue = "1") long current, @RequestParam(value = "size", defaultValue = "10") long size, @RequestParam(value = "configKey", required = false) String configKey, @RequestParam(value = "status", required = false) Integer status) {

        Page<SysOssConfig> page = new Page<>(current, size);
        QueryWrapper queryWrapper = QueryWrapper.create().where(SYS_OSS_CONFIG.IS_DELETED.eq(0));

        if (StringUtils.hasText(configKey)) {
            queryWrapper.and(SYS_OSS_CONFIG.CONFIG_KEY.like(configKey));
        }
        if (status != null) {
            queryWrapper.and(SYS_OSS_CONFIG.STATUS.eq(status));
        }

        queryWrapper.orderBy(SYS_OSS_CONFIG.CREATE_TIME.desc());

        Page<SysOssConfig> result = ossConfigService.selectConfigPage(page, queryWrapper);
        return Result.success(result);
    }

    @Operation(summary = "根据ID获取OSS配置")
    @Parameter(name = "id", description = "配置ID", required = true)
    @SaCheckPermission("system:ossConfig:query")
    @GetMapping("/{id}")
    public Result<SysOssConfig> getById(@PathVariable Long id) {
        SysOssConfig config = ossConfigService.getById(id);
        if (config != null) {
            return Result.success("获取成功", config);
        } else {
            return Result.error("配置不存在");
        }
    }

    @Operation(summary = "根据配置key获取OSS配置")
    @Parameter(name = "configKey", description = "配置key", required = true)
    @SaCheckPermission("system:ossConfig:query")
    @GetMapping("/by-key/{configKey}")
    public Result<SysOssConfig> getByKey(@PathVariable String configKey) {
        SysOssConfig config = ossConfigService.selectConfigByKey(configKey);
        if (config != null) {
            return Result.success("获取成功", config);
        } else {
            return Result.error("配置不存在");
        }
    }

    @Operation(summary = "获取启用的OSS配置列表")
    @SaCheckPermission("system:ossConfig:list")
    @GetMapping("/enabled")
    public Result<List<SysOssConfig>> getEnabledConfigs() {
        List<SysOssConfig> configs = ossConfigService.selectEnabledConfigs();
        return Result.success("获取成功", configs);
    }

    @Operation(summary = "新增OSS配置")
    @SaCheckPermission("system:ossConfig:add")
    @PostMapping
    public Result<Void> add(@RequestBody @Validated SysOssConfig config) {
        try {
            boolean success = ossConfigService.saveConfig(config);
            if (success) {
                return Result.success();
            } else {
                return Result.error("新增失败");
            }
        } catch (Exception e) {
            log.error("新增OSS配置失败", e);
            return Result.error("新增失败: " + e.getMessage());
        }
    }

    @Operation(summary = "修改OSS配置")
    @SaCheckPermission("system:ossConfig:edit")
    @PutMapping
    public Result<Void> update(@RequestBody @Validated SysOssConfig config) {
        try {
            boolean success = ossConfigService.updateConfig(config);
            if (success) {
                return Result.success();
            } else {
                return Result.error("修改失败");
            }
        } catch (Exception e) {
            log.error("修改OSS配置失败", e);
            return Result.error("修改失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除OSS配置")
    @Parameter(name = "id", description = "配置ID", required = true)
    @SaCheckPermission("system:ossConfig:remove")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            boolean success = ossConfigService.deleteConfig(id);
            if (success) {
                return Result.success();
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除OSS配置失败", e);
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量删除OSS配置")
    @Parameter(name = "ids", description = "配置ID列表", required = true)
    @SaCheckPermission("system:ossConfig:remove")
    @DeleteMapping("/batch")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        try {
            boolean success = ossConfigService.deleteConfigs(ids);
            if (success) {
                return Result.success();
            } else {
                return Result.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除OSS配置失败", e);
            return Result.error("批量删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "更新OSS配置状态")
    @Parameters({@Parameter(name = "id", description = "配置ID", required = true), @Parameter(name = "status", description = "状态：0-停用，1-启用", required = true)})
    @SaCheckPermission("system:ossConfig:edit")
    @PutMapping("/{id}/status/{status}")
    public Result<Void> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        try {
            boolean success = ossConfigService.updateStatus(id, status);
            if (success) {
                return Result.success();
            } else {
                return Result.error("状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新OSS配置状态失败", e);
            return Result.error("状态更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "测试OSS配置连接")
    @SaCheckPermission("system:ossConfig:test")
    @PostMapping("/test")
    public Result<Void> testConnection(@RequestBody SysOssConfig config) {
        try {
            boolean success = ossConfigService.testConnection(config);
            if (success) {
                return Result.success();
            } else {
                return Result.error("连接测试失败");
            }
        } catch (Exception e) {
            log.error("测试OSS配置连接失败", e);
            return Result.error("连接测试失败: " + e.getMessage());
        }
    }

    @Operation(summary = "刷新OSS配置缓存")
    @SaCheckPermission("system:ossConfig:edit")
    @DeleteMapping("/refreshCache")
    public Result<Void> refreshCache() {
        try {
            ossConfigService.refreshCache();
            return Result.success();
        } catch (Exception e) {
            log.error("刷新OSS配置缓存失败", e);
            return Result.error("刷新缓存失败: " + e.getMessage());
        }
    }
}
