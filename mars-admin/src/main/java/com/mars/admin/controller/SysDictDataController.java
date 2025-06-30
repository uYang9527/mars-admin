package com.mars.admin.controller;

import com.mars.admin.controller.base.BaseController;
import com.mars.admin.entity.SysDictData;
import com.mars.admin.framework.common.Result;
import com.mars.admin.service.ISysDictDataService;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统字典数据Controller
 * 继承BaseController获得基础的增删改查功能
 *
 * @author Mars
 */
@RestController
@RequestMapping("/system/dict/data")
@Tag(name = "系统字典数据管理", description = "系统字典数据管理相关接口")
public class SysDictDataController extends BaseController<SysDictData, Long> {

    @Autowired
    private ISysDictDataService sysDictDataService;

    // 继承BaseController后自动拥有基础的增删改查功能：
    // GET    /system/dict/data/list           - 获取所有字典数据
    // GET    /system/dict/data/{id}           - 根据ID获取字典数据
    // GET    /system/dict/data/page           - 分页查询字典数据（重写以支持搜索条件）
    // POST   /system/dict/data                - 新增字典数据
    // PUT    /system/dict/data                - 更新字典数据
    // DELETE /system/dict/data/{id}           - 删除字典数据
    // DELETE /system/dict/data/batch          - 批量删除字典数据

    /**
     * 分页查询字典数据列表（重写BaseController方法以支持搜索条件）
     */
    @GetMapping("/pageList")
    @Operation(summary = "分页查询字典数据列表", description = "分页查询字典数据列表，支持多条件搜索")
    public Result<Page<SysDictData>> pageList(
            @Parameter(description = "当前页", example = "1") @RequestParam(value = "current", defaultValue = "1") Integer current,
            @Parameter(description = "每页大小", example = "10") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @Parameter(description = "字典标签") @RequestParam(value = "dictLabel", required = false) String dictLabel,
            @Parameter(description = "字典键值") @RequestParam(value = "dictValue", required = false) String dictValue,
            @Parameter(description = "字典类型") @RequestParam(value = "dictType", required = false) String dictType,
            @Parameter(description = "状态") @RequestParam(value = "status", required = false) Integer status) {
        
        // 构建查询条件
        SysDictData queryCondition = new SysDictData();
        queryCondition.setDictLabel(dictLabel);
        queryCondition.setDictValue(dictValue);
        queryCondition.setDictType(dictType);
        queryCondition.setStatus(status);
        
        // 分页查询
        Page<SysDictData> page = sysDictDataService.selectDictDataPage(Page.of(current, size), queryCondition);
        return Result.success(page);
    }

    /**
     * 根据字典类型查询字典数据
     */
    @GetMapping("/type/{dictType}")
    @Operation(summary = "根据字典类型查询字典数据", description = "根据字典类型查询字典数据")
    public Result<List<SysDictData>> getByDictType(@Parameter(description = "字典类型") @PathVariable String dictType) {
        List<SysDictData> list = sysDictDataService.selectByDictType(dictType);
        return Result.success(list);
    }

    /**
     * 根据字典类型查询正常状态的字典数据
     */
    @GetMapping("/type/{dictType}/normal")
    @Operation(summary = "根据字典类型查询正常状态的字典数据", description = "根据字典类型查询正常状态的字典数据")
    public Result<List<SysDictData>> getNormalByDictType(@Parameter(description = "字典类型") @PathVariable String dictType) {
        List<SysDictData> list = sysDictDataService.selectNormalDictDataByType(dictType);
        return Result.success(list);
    }

    /**
     * 根据字典类型和字典值查询字典数据
     */
    @GetMapping("/type/{dictType}/value/{dictValue}")
    @Operation(summary = "根据字典类型和字典值查询字典数据", description = "根据字典类型和字典值查询字典数据")
    public Result<SysDictData> getByDictTypeAndValue(
            @Parameter(description = "字典类型") @PathVariable String dictType,
            @Parameter(description = "字典值") @PathVariable String dictValue) {
        SysDictData result = sysDictDataService.selectByDictTypeAndValue(dictType, dictValue);
        return Result.success(result);
    }

    /**
     * 校验字典值是否唯一
     */
    @GetMapping("/checkDictValueUnique")
    @Operation(summary = "校验字典值是否唯一", description = "校验字典值是否唯一")
    public Result<Boolean> checkDictValueUnique(
            @Parameter(description = "字典类型") @RequestParam String dictType,
            @Parameter(description = "字典值") @RequestParam String dictValue,
            @Parameter(description = "字典数据ID") @RequestParam(required = false) Long dictDataId) {
        boolean unique = sysDictDataService.checkDictValueUnique(dictType, dictValue, dictDataId);
        return Result.success(unique);
    }

    /**
     * 批量删除字典数据（重写BaseController的方法）
     *
     * @param ids ID列表
     * @return 删除结果
     */
    @Operation(summary = "批量删除字典数据", description = "根据ID列表批量删除字典数据")
    @DeleteMapping("/batch")
    public Result<Boolean> deleteBatch(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("ID列表不能为空");
        }
        Long[] dictDataIds = ids.toArray(new Long[0]);
        boolean success = sysDictDataService.deleteDictData(dictDataIds);
        return Result.of(success, "批量删除成功", "批量删除失败");
    }
    
    /**
     * 根据ID删除字典数据（重写BaseController的方法）
     *
     * @param id 主键ID
     * @return 是否成功
     */
    @Operation(summary = "根据ID删除字典数据", description = "根据主键ID删除字典数据")
    @DeleteMapping("/{id}")
    @Override
    public Result<Boolean> deleteById(@PathVariable Long id) {
        Long[] dictDataIds = {id};
        boolean success = sysDictDataService.deleteDictData(dictDataIds);
        return Result.of(success, "删除成功", "删除失败");
    }
} 