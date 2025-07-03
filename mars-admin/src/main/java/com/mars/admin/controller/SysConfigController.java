package com.mars.admin.controller;

import com.mars.admin.controller.base.BaseController;
import com.mars.admin.entity.SysConfig;
import com.mars.admin.framework.common.Result;
import com.mars.admin.framework.common.annotation.OperationLog;
import com.mars.admin.framework.enums.BusinessType;
import com.mars.admin.service.ISysConfigService;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 分页查询系统配置列表（重写BaseController方法以支持搜索条件）
     */
    @GetMapping("/pageList")
    @Operation(summary = "分页查询系统配置列表", description = "分页查询系统配置列表，支持多条件搜索")
    public Result<Page<SysConfig>> pageList(
            @Parameter(description = "当前页", example = "1") @RequestParam(value = "current", defaultValue = "1") Integer current,
            @Parameter(description = "每页大小", example = "10") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @Parameter(description = "参数名称") @RequestParam(value = "configName", required = false) String configName,
            @Parameter(description = "参数键名") @RequestParam(value = "configKey", required = false) String configKey,
            @Parameter(description = "系统内置") @RequestParam(value = "configType", required = false) String configType) {
        
        // 构建查询条件
        SysConfig queryCondition = new SysConfig();
        queryCondition.setConfigName(configName);
        queryCondition.setConfigKey(configKey);
        queryCondition.setConfigType(configType);
        
        // 分页查询
        Page<SysConfig> page = sysConfigService.selectConfigPage(Page.of(current, size), queryCondition);
        return Result.success(page);
    }

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
    @OperationLog(title = "参数设置", businessType = BusinessType.CLEAN, description = "刷新参数缓存")
    public Result<Void> refreshCache() {
        sysConfigService.refreshCache();
        return Result.success();
    }

    /**
     * 新增配置（重写BaseController的方法）
     */
    @PostMapping
    @Operation(summary = "新增配置", description = "新增配置信息")
    @OperationLog(title = "参数设置", businessType = BusinessType.INSERT, 
                  description = "新增参数：#{#entity.configName}")
    @Override
    public Result<Boolean> save(@RequestBody SysConfig entity) {
        // 校验配置键是否唯一
        if (!sysConfigService.checkConfigKeyUnique(entity.getConfigKey(), null)) {
            return Result.error("配置键已存在，请重新输入");
        }
        boolean success = sysConfigService.insertConfig(entity);
        return Result.of(success, "新增成功", "新增失败");
    }

    /**
     * 修改配置（重写BaseController的方法）
     */
    @PutMapping
    @Operation(summary = "修改配置", description = "修改配置信息")
    @OperationLog(title = "参数设置", businessType = BusinessType.UPDATE, 
                  description = "修改参数：#{#entity.configName}")
    @Override
    public Result<Boolean> update(@RequestBody SysConfig entity) {
        // 校验配置键是否唯一
        if (!sysConfigService.checkConfigKeyUnique(entity.getConfigKey(), entity.getId())) {
            return Result.error("配置键已存在，请重新输入");
        }
        boolean success = sysConfigService.updateConfig(entity);
        return Result.of(success, "修改成功", "修改失败");
    }

    /**
     * 根据ID删除配置（重写BaseController的方法）
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除配置", description = "根据ID删除配置")
    @OperationLog(title = "参数设置", businessType = BusinessType.DELETE, 
                  description = "删除参数配置，ID：#{#id}")
    @Override
    public Result<Boolean> deleteById(@PathVariable Long id) {
        // 检查是否为系统内置配置
        SysConfig config = sysConfigService.getById(id);
        if (config != null && "Y".equals(config.getConfigType())) {
            return Result.error("系统内置配置不能删除");
        }
        boolean success = sysConfigService.deleteConfigs(new Long[]{id});
        return Result.of(success, "删除成功", "删除失败");
    }

    /**
     * 批量删除配置（重写BaseController的方法）
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除配置", description = "批量删除配置")
    @OperationLog(title = "参数设置", businessType = BusinessType.DELETE, 
                  description = "批量删除参数配置，ID列表：#{#ids}")
    @Override
    public Result<Boolean> deleteBatch(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("ID列表不能为空");
        }
        
        // 检查是否包含系统内置配置
        for (Long id : ids) {
            SysConfig config = sysConfigService.getById(id);
            if (config != null && "Y".equals(config.getConfigType())) {
                return Result.error("包含系统内置配置，不能删除");
            }
        }
        
        boolean success = sysConfigService.deleteConfigs(ids.toArray(new Long[0]));
        return Result.of(success, "批量删除成功", "批量删除失败");
    }
} 