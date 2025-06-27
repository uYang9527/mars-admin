package com.mars.admin.controller;

import com.mars.admin.controller.base.BaseController;
import com.mars.admin.entity.SysConfig;
import com.mars.admin.framework.common.Result;
import com.mars.admin.service.ISysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统配置Controller
 * 继承BaseController获得基础的增删改查功能
 *
 * @author Mars
 */
@RestController
@RequestMapping("/system/config")
@Tag(name = "系统配置管理", description = "系统配置管理相关接口")
public class SysConfigController extends BaseController<SysConfig, Long> {

    @Autowired
    private ISysConfigService sysConfigService;

    // 继承BaseController后自动拥有基础的增删改查功能：
    // GET    /system/config/list           - 获取所有配置
    // GET    /system/config/{id}           - 根据ID获取配置
    // GET    /system/config/page           - 分页查询配置
    // POST   /system/config                - 新增配置
    // PUT    /system/config                - 更新配置
    // DELETE /system/config/{id}           - 删除配置
    // DELETE /system/config/batch          - 批量删除配置

    /**
     * 根据配置键查询配置值
     */
    @GetMapping("/configKey/{configKey}")
    @Operation(summary = "根据配置键查询配置值", description = "根据配置键查询配置值")
    public Result<String> getConfigByKey(@Parameter(description = "配置键") @PathVariable String configKey) {
        String configValue = sysConfigService.getConfigValueByKey(configKey);
        return Result.success(configValue);
    }

    /**
     * 校验配置键是否唯一
     */
    @GetMapping("/checkConfigKeyUnique")
    @Operation(summary = "校验配置键是否唯一", description = "校验配置键是否唯一")
    public Result<Boolean> checkConfigKeyUnique(
            @Parameter(description = "配置键") @RequestParam String configKey,
            @Parameter(description = "配置ID") @RequestParam(required = false) Long configId) {
        boolean unique = sysConfigService.checkConfigKeyUnique(configKey, configId);
        return Result.success(unique);
    }

    /**
     * 刷新配置缓存
     */
    @DeleteMapping("/refreshCache")
    @Operation(summary = "刷新配置缓存", description = "刷新配置缓存")
    public Result<Void> refreshCache() {
        // TODO: 实现配置缓存刷新逻辑
        return Result.success();
    }
} 