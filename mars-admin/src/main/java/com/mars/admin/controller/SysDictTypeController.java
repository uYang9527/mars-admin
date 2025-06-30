package com.mars.admin.controller;

import com.mars.admin.controller.base.BaseController;
import com.mars.admin.entity.SysDictType;
import com.mars.admin.framework.common.Result;
import com.mars.admin.service.ISysDictTypeService;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统字典类型Controller
 * 继承BaseController获得基础的增删改查功能
 *
 * @author Mars
 */
@RestController
@RequestMapping("/system/dict/type")
@Tag(name = "系统字典类型管理", description = "系统字典类型管理相关接口")
public class SysDictTypeController extends BaseController<SysDictType, Long> {

    @Autowired
    private ISysDictTypeService sysDictTypeService;

    // 继承BaseController后自动拥有基础的增删改查功能：
    // GET    /system/dict/type/list           - 获取所有字典类型
    // GET    /system/dict/type/{id}           - 根据ID获取字典类型
    // GET    /system/dict/type/page           - 分页查询字典类型
    // POST   /system/dict/type                - 新增字典类型
    // PUT    /system/dict/type                - 更新字典类型
    // DELETE /system/dict/type/{id}           - 删除字典类型
    // DELETE /system/dict/type/batch          - 批量删除字典类型

    /**
     * 分页查询字典类型列表（重写BaseController方法以支持搜索条件）
     */
    @GetMapping("/pageList")
    @Operation(summary = "分页查询字典类型列表", description = "分页查询字典类型列表，支持多条件搜索")
    public Result<Page<SysDictType>> pageList(
            @Parameter(description = "当前页", example = "1") @RequestParam(value = "current", defaultValue = "1") Integer current,
            @Parameter(description = "每页大小", example = "10") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @Parameter(description = "字典名称") @RequestParam(value = "dictName", required = false) String dictName,
            @Parameter(description = "字典类型") @RequestParam(value = "dictType", required = false) String dictType,
            @Parameter(description = "状态") @RequestParam(value = "status", required = false) Integer status) {
        
        // 构建查询条件
        SysDictType queryCondition = new SysDictType();
        queryCondition.setDictName(dictName);
        queryCondition.setDictType(dictType);
        queryCondition.setStatus(status);
        
        // 分页查询
        Page<SysDictType> page = sysDictTypeService.selectDictTypePage(Page.of(current, size), queryCondition);
        return Result.success(page);
    }

    /**
     * 查询所有正常状态的字典类型
     */
    @GetMapping("/optionselect")
    @Operation(summary = "查询所有正常状态的字典类型", description = "查询所有正常状态的字典类型")
    public Result<List<SysDictType>> optionselect() {
        List<SysDictType> dictTypes = sysDictTypeService.selectNormalDictTypes();
        return Result.success(dictTypes);
    }

    /**
     * 根据字典类型查询字典类型信息
     */
    @GetMapping("/type/{dictType}")
    @Operation(summary = "根据字典类型查询字典类型信息", description = "根据字典类型查询字典类型信息")
    public Result<SysDictType> getByDictType(@Parameter(description = "字典类型") @PathVariable String dictType) {
        SysDictType result = sysDictTypeService.selectByDictType(dictType);
        return Result.success(result);
    }

    /**
     * 查询字典类型详情（包含字典数据）
     */
    @GetMapping("/detail/{dictTypeId}")
    @Operation(summary = "查询字典类型详情", description = "查询字典类型详情（包含字典数据）")
    public Result<SysDictType> getDetail(@Parameter(description = "字典类型ID") @PathVariable Long dictTypeId) {
        SysDictType result = sysDictTypeService.selectDictTypeDetailById(dictTypeId);
        return Result.success(result);
    }

    /**
     * 校验字典类型是否唯一
     */
    @GetMapping("/checkDictTypeUnique")
    @Operation(summary = "校验字典类型是否唯一", description = "校验字典类型是否唯一")
    public Result<Boolean> checkDictTypeUnique(
            @Parameter(description = "字典类型") @RequestParam String dictType,
            @Parameter(description = "字典类型ID") @RequestParam(required = false) Long dictTypeId) {
        boolean unique = sysDictTypeService.checkDictTypeUnique(dictType, dictTypeId);
        return Result.success(unique);
    }

    /**
     * 校验字典名称是否唯一
     */
    @GetMapping("/checkDictNameUnique")
    @Operation(summary = "校验字典名称是否唯一", description = "校验字典名称是否唯一")
    public Result<Boolean> checkDictNameUnique(
            @Parameter(description = "字典名称") @RequestParam String dictName,
            @Parameter(description = "字典类型ID") @RequestParam(required = false) Long dictTypeId) {
        boolean unique = sysDictTypeService.checkDictNameUnique(dictName, dictTypeId);
        return Result.success(unique);
    }

    /**
     * 刷新字典缓存
     */
    @DeleteMapping("/refreshCache")
    @Operation(summary = "刷新字典缓存", description = "刷新字典缓存")
    public Result<Void> refreshCache() {
        // TODO: 实现字典缓存刷新逻辑
        return Result.success();
    }
} 