package com.mars.admin.modules.system.controller;

import com.mars.admin.framework.common.Result;
import com.mars.admin.framework.common.annotation.OperationLog;
import com.mars.admin.framework.enums.BusinessType;
import com.mars.admin.modules.system.entity.SysDictData;
import com.mars.admin.modules.system.entity.SysDictType;
import com.mars.admin.modules.system.service.ISysDictDataService;
import com.mars.admin.modules.system.service.ISysDictTypeService;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典管理控制器（整合版）
 * 统一管理字典类型和字典数据的所有操作
 *
 * @author Mars
 */
@Tag(name = "字典管理", description = "字典管理相关接口")
@RestController
@RequestMapping("/system/dict")
public class SysDictController {

    @Autowired
    private ISysDictTypeService sysDictTypeService;

    @Autowired
    private ISysDictDataService sysDictDataService;

    // ==================== 字典树形数据 ====================

    /**
     * 获取字典树形数据
     */
    @GetMapping("/tree")
    @Operation(summary = "获取字典树形数据", description = "获取字典类型和字典数据的树形结构")
    public Result<List<Map<String, Object>>> getDictTree() {
        List<Map<String, Object>> treeData = new ArrayList<>();

        // 获取所有字典类型
        List<SysDictType> dictTypes = sysDictTypeService.selectNormalDictTypes();

        for (SysDictType dictType : dictTypes) {
            Map<String, Object> typeNode = new HashMap<>();
            typeNode.put("key", "type-" + dictType.getId());
            typeNode.put("title", dictType.getDictName());
            typeNode.put("nodeType", "dictType");
            typeNode.put("isLeaf", false);

            // 添加字典类型的完整数据
            typeNode.put("data", dictType);

            // 获取该类型下的字典数据
            List<SysDictData> dictDataList = sysDictDataService.selectNormalDictDataByType(dictType.getDictType());

            List<Map<String, Object>> children = new ArrayList<>();
            for (SysDictData dictData : dictDataList) {
                Map<String, Object> dataNode = new HashMap<>();
                dataNode.put("key", "data-" + dictData.getId());
                dataNode.put("title", dictData.getDictLabel());
                dataNode.put("nodeType", "dictData");
                dataNode.put("isLeaf", true);

                // 添加字典数据的完整数据
                dataNode.put("data", dictData);

                children.add(dataNode);
            }

            typeNode.put("children", children);
            treeData.add(typeNode);
        }

        return Result.success(treeData);
    }

    // ==================== 字典类型管理 ====================

    /**
     * 分页查询字典类型列表
     */
    @GetMapping("/type/page")
    @Operation(summary = "分页查询字典类型列表", description = "分页查询字典类型列表，支持多条件搜索")
    public Result<Page<SysDictType>> getDictTypePage(
            @Parameter(description = "当前页", example = "1") @RequestParam(value = "current", defaultValue = "1") Integer current,
            @Parameter(description = "每页大小", example = "10") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @Parameter(description = "字典名称") @RequestParam(value = "dictName", required = false) String dictName,
            @Parameter(description = "字典类型") @RequestParam(value = "dictType", required = false) String dictType,
            @Parameter(description = "状态") @RequestParam(value = "status", required = false) Integer status) {

        SysDictType queryCondition = new SysDictType();
        queryCondition.setDictName(dictName);
        queryCondition.setDictType(dictType);
        queryCondition.setStatus(status);

        Page<SysDictType> page = sysDictTypeService.selectDictTypePage(Page.of(current, size), queryCondition);
        return Result.success(page);
    }

    /**
     * 获取所有正常状态的字典类型
     */
    @GetMapping("/type/normal")
    @Operation(summary = "获取所有正常状态的字典类型", description = "获取所有正常状态的字典类型")
    public Result<List<SysDictType>> getNormalDictTypes() {
        List<SysDictType> dictTypes = sysDictTypeService.selectNormalDictTypes();
        return Result.success(dictTypes);
    }

    /**
     * 根据ID获取字典类型
     */
    @GetMapping("/type/{id}")
    @Operation(summary = "根据ID获取字典类型", description = "根据ID获取字典类型详情")
    public Result<SysDictType> getDictTypeById(@Parameter(description = "字典类型ID") @PathVariable Long id) {
        SysDictType dictType = sysDictTypeService.getDictTypeById(id);
        if (dictType != null) {
            return Result.success(dictType);
        } else {
            return Result.error("数据不存在");
        }
    }

    /**
     * 新增字典类型
     */
    @PostMapping("/type")
    @Operation(summary = "新增字典类型", description = "新增字典类型信息")
    @OperationLog(title = "字典管理", businessType = BusinessType.INSERT,
                  description = "新增字典类型：#{#dictType.dictName}")
    public Result<Boolean> addDictType(@Parameter(description = "字典类型信息", required = true) @RequestBody SysDictType dictType) {
        boolean success = sysDictTypeService.insertDictType(dictType);
        return Result.of(success, "新增成功", "新增失败");
    }

    /**
     * 更新字典类型
     */
    @PutMapping("/type")
    @Operation(summary = "更新字典类型", description = "更新字典类型信息")
    @OperationLog(title = "字典管理", businessType = BusinessType.UPDATE,
                  description = "更新字典类型：#{#dictType.dictName}")
    public Result<Boolean> updateDictType(@Parameter(description = "字典类型信息", required = true) @RequestBody SysDictType dictType) {
        boolean success = sysDictTypeService.updateDictType(dictType);
        return Result.of(success, "更新成功", "更新失败");
    }

    /**
     * 删除字典类型
     */
    @DeleteMapping("/type/{id}")
    @Operation(summary = "删除字典类型", description = "根据ID删除字典类型及关联数据")
    @OperationLog(title = "字典管理", businessType = BusinessType.DELETE,
                  description = "删除字典类型，ID：#{#id}")
    public Result<Boolean> deleteDictType(@Parameter(description = "字典类型ID") @PathVariable Long id) {
        boolean success = sysDictTypeService.deleteDictTypeById(id);
        return Result.of(success, "删除成功", "删除失败");
    }

    /**
     * 批量删除字典类型
     */
    @DeleteMapping("/type/batch")
    @Operation(summary = "批量删除字典类型", description = "根据ID列表批量删除字典类型及关联数据")
    @OperationLog(title = "字典管理", businessType = BusinessType.DELETE,
                  description = "批量删除字典类型，ID列表：#{#ids}")
    public Result<Boolean> deleteDictTypes(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("ID列表不能为空");
        }
        Long[] dictTypeIds = ids.toArray(new Long[0]);
        boolean success = sysDictTypeService.deleteDictTypes(dictTypeIds);
        return Result.of(success, "批量删除成功", "批量删除失败");
    }

    /**
     * 校验字典类型是否唯一
     */
    @GetMapping("/type/check-unique")
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
    @GetMapping("/type/check-name-unique")
    @Operation(summary = "校验字典名称是否唯一", description = "校验字典名称是否唯一")
    public Result<Boolean> checkDictNameUnique(
            @Parameter(description = "字典名称") @RequestParam String dictName,
            @Parameter(description = "字典类型ID") @RequestParam(required = false) Long dictTypeId) {
        boolean unique = sysDictTypeService.checkDictNameUnique(dictName, dictTypeId);
        return Result.success(unique);
    }

    // ==================== 字典数据管理 ====================

    /**
     * 分页查询字典数据列表
     */
    @GetMapping("/data/page")
    @Operation(summary = "分页查询字典数据列表", description = "分页查询字典数据列表，支持多条件搜索")
    public Result<Page<SysDictData>> getDictDataPage(
            @Parameter(description = "当前页", example = "1") @RequestParam(value = "current", defaultValue = "1") Integer current,
            @Parameter(description = "每页大小", example = "10") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @Parameter(description = "字典标签") @RequestParam(value = "dictLabel", required = false) String dictLabel,
            @Parameter(description = "字典键值") @RequestParam(value = "dictValue", required = false) String dictValue,
            @Parameter(description = "字典类型") @RequestParam(value = "dictType", required = false) String dictType,
            @Parameter(description = "状态") @RequestParam(value = "status", required = false) Integer status) {

        SysDictData queryCondition = new SysDictData();
        queryCondition.setDictLabel(dictLabel);
        queryCondition.setDictValue(dictValue);
        queryCondition.setDictType(dictType);
        queryCondition.setStatus(status);

        Page<SysDictData> page = sysDictDataService.selectDictDataPage(Page.of(current, size), queryCondition);
        return Result.success(page);
    }

    /**
     * 根据字典类型查询字典数据
     */
    @GetMapping("/data/type/{dictType}")
    @Operation(summary = "根据字典类型查询字典数据", description = "根据字典类型查询字典数据")
    public Result<List<SysDictData>> getDictDataByType(@Parameter(description = "字典类型") @PathVariable String dictType) {
        List<SysDictData> list = sysDictDataService.selectByDictType(dictType);
        return Result.success(list);
    }

    /**
     * 根据字典类型查询正常状态的字典数据
     */
    @GetMapping("/data/type/{dictType}/normal")
    @Operation(summary = "根据字典类型查询正常状态的字典数据", description = "根据字典类型查询正常状态的字典数据")
    public Result<List<SysDictData>> getNormalDictDataByType(@Parameter(description = "字典类型") @PathVariable String dictType) {
        List<SysDictData> list = sysDictDataService.selectNormalDictDataByType(dictType);
        return Result.success(list);
    }

    /**
     * 根据ID获取字典数据
     */
    @GetMapping("/data/{id}")
    @Operation(summary = "根据ID获取字典数据", description = "根据ID获取字典数据详情")
    public Result<SysDictData> getDictDataById(@Parameter(description = "字典数据ID") @PathVariable Long id) {
        SysDictData dictData = sysDictDataService.getDictDataById(id);
        if (dictData != null) {
            return Result.success(dictData);
        } else {
            return Result.error("数据不存在");
        }
    }

    /**
     * 新增字典数据
     */
    @PostMapping("/data")
    @Operation(summary = "新增字典数据", description = "新增字典数据信息")
    @OperationLog(title = "字典管理", businessType = BusinessType.INSERT,
                  description = "新增字典数据：#{#dictData.dictLabel}")
    public Result<Boolean> addDictData(@Parameter(description = "字典数据信息", required = true) @RequestBody SysDictData dictData) {
        boolean success = sysDictDataService.insertDictData(dictData);
        return Result.of(success, "新增成功", "新增失败");
    }

    /**
     * 更新字典数据
     */
    @PutMapping("/data")
    @Operation(summary = "更新字典数据", description = "更新字典数据信息")
    @OperationLog(title = "字典管理", businessType = BusinessType.UPDATE,
                  description = "更新字典数据：#{#dictData.dictLabel}")
    public Result<Boolean> updateDictData(@Parameter(description = "字典数据信息", required = true) @RequestBody SysDictData dictData) {
        boolean success = sysDictDataService.updateDictData(dictData);
        return Result.of(success, "更新成功", "更新失败");
    }

    /**
     * 删除字典数据
     */
    @DeleteMapping("/data/{id}")
    @Operation(summary = "删除字典数据", description = "根据ID删除字典数据")
    @OperationLog(title = "字典管理", businessType = BusinessType.DELETE,
                  description = "删除字典数据，ID：#{#id}")
    public Result<Boolean> deleteDictData(@Parameter(description = "字典数据ID") @PathVariable Long id) {
        Long[] dictDataIds = {id};
        boolean success = sysDictDataService.deleteDictData(dictDataIds);
        return Result.of(success, "删除成功", "删除失败");
    }

    /**
     * 批量删除字典数据
     */
    @DeleteMapping("/data/batch")
    @Operation(summary = "批量删除字典数据", description = "根据ID列表批量删除字典数据")
    @OperationLog(title = "字典管理", businessType = BusinessType.DELETE,
                  description = "批量删除字典数据，ID列表：#{#ids}")
    public Result<Boolean> deleteDictDatas(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("ID列表不能为空");
        }
        Long[] dictDataIds = ids.toArray(new Long[0]);
        boolean success = sysDictDataService.deleteDictData(dictDataIds);
        return Result.of(success, "批量删除成功", "批量删除失败");
    }

    /**
     * 校验字典值是否唯一
     */
    @GetMapping("/data/check-value-unique")
    @Operation(summary = "校验字典值是否唯一", description = "校验字典值是否唯一")
    public Result<Boolean> checkDictValueUnique(
            @Parameter(description = "字典类型") @RequestParam String dictType,
            @Parameter(description = "字典值") @RequestParam String dictValue,
            @Parameter(description = "字典数据ID") @RequestParam(required = false) Long dictDataId) {
        boolean unique = sysDictDataService.checkDictValueUnique(dictType, dictValue, dictDataId);
        return Result.success(unique);
    }
}
