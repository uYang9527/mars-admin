package com.mars.admin.modules.system.controller;

import com.mars.admin.framework.common.Result;
import com.mars.admin.modules.system.service.ISysDictDataService;
import com.mars.admin.modules.system.service.ISysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典缓存管理Controller
 * 统一管理字典类型和字典数据的缓存
 *
 * @author Mars
 */
@RestController
@RequestMapping("/system/dict/cache")
@Tag(name = "字典缓存管理", description = "字典缓存管理相关接口")
public class SysDictCacheController {

    @Autowired
    private ISysDictTypeService sysDictTypeService;

    @Autowired
    private ISysDictDataService sysDictDataService;

    /**
     * 刷新所有字典缓存
     */
    @DeleteMapping("/refresh")
    @Operation(summary = "刷新所有字典缓存", description = "同时刷新字典类型和字典数据的所有缓存")
    public Result<String> refreshAllCache() {
        sysDictTypeService.refreshCache();
        sysDictDataService.refreshCache();
        return Result.success("所有字典缓存刷新成功");
    }

    /**
     * 刷新字典类型缓存
     */
    @DeleteMapping("/refreshType")
    @Operation(summary = "刷新字典类型缓存", description = "刷新字典类型缓存")
    public Result<String> refreshTypeCache() {
        sysDictTypeService.refreshCache();
        return Result.success("字典类型缓存刷新成功");
    }

    /**
     * 刷新字典数据缓存
     */
    @DeleteMapping("/refreshData")
    @Operation(summary = "刷新字典数据缓存", description = "刷新字典数据缓存")
    public Result<String> refreshDataCache() {
        sysDictDataService.refreshCache();
        return Result.success("字典数据缓存刷新成功");
    }
}
