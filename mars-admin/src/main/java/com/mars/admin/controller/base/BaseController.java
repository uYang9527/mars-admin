package com.mars.admin.controller.base;

import com.mars.admin.framework.common.Result;
import com.mars.admin.service.BaseService;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * 通用基础控制器
 * 提供标准的增删改查功能
 *
 * @param <T> 实体类型
 * @param <ID> 主键类型
 * @author Mars.wq [wqexpore@163.com]
 * @version 1.0
 * @date 2025-06-23 17:35:08
 */
public abstract class BaseController<T, ID extends Serializable> {

    @Autowired
    protected BaseService<T> service;

    /**
     * 获取所有记录
     *
     * @return 所有记录列表
     */
    @Operation(summary = "获取所有记录", description = "获取所有记录列表")
    @GetMapping("/list")
    public Result<List<T>> list() {
        List<T> list = service.list();
        return Result.success(list);
    }

    /**
     * 根据ID获取单条记录
     *
     * @param id 主键ID
     * @return 单条记录
     */
    @Operation(summary = "根据ID获取单条记录", description = "根据主键ID获取单条记录")
    @GetMapping("/{id}")
    public Result<T> getById(@Parameter(description = "主键ID", required = true) @PathVariable ID id) {
        T entity = service.getById(id);
        if (entity != null) {
            return Result.success(entity);
        } else {
            return Result.error("数据不存在");
        }
    }

    /**
     * 分页查询
     *
     * @param pageNum  页码，默认1
     * @param pageSize 每页大小，默认10
     * @return 分页结果
     */
    @Operation(summary = "分页查询", description = "分页查询记录")
    @GetMapping("/page")
    public Result<Page<T>> page(@Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
                        @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<T> page = service.page(Page.of(pageNum, pageSize));
        return Result.success(page);
    }

    /**
     * 新增记录
     *
     * @param entity 实体对象
     * @return 是否成功
     */
    @Operation(summary = "新增记录", description = "新增一条记录")
    @PostMapping
    public Result<Boolean> save(@Parameter(description = "实体对象", required = true) @RequestBody T entity) {
        boolean success = service.save(entity);
        return Result.of(success, "新增成功", "新增失败");
    }

    /**
     * 批量新增
     *
     * @param entities 实体列表
     * @return 是否成功
     */
    @Operation(summary = "批量新增", description = "批量新增记录")
    @PostMapping("/batch")
    public Result<Boolean> saveBatch(@Parameter(description = "实体列表", required = true) @RequestBody List<T> entities) {
        boolean success = service.saveBatch(entities);
        return Result.of(success, "批量新增成功", "批量新增失败");
    }

    /**
     * 更新记录
     *
     * @param entity 实体对象
     * @return 是否成功
     */
    @Operation(summary = "更新记录", description = "更新一条记录")
    @PutMapping
    public Result<Boolean> update(@Parameter(description = "实体对象", required = true) @RequestBody T entity) {
        boolean success = service.updateById(entity);
        return Result.of(success, "更新成功", "更新失败");
    }

    /**
     * 保存或更新（有ID则更新，无ID则新增）
     *
     * @param entity 实体对象
     * @return 是否成功
     */
    @Operation(summary = "保存或更新", description = "保存或更新记录，有ID则更新，无ID则新增")
    @PostMapping("/saveOrUpdate")
    public Result<Boolean> saveOrUpdate(@Parameter(description = "实体对象", required = true) @RequestBody T entity) {
        boolean success = service.saveOrUpdate(entity);
        return Result.of(success, "保存成功", "保存失败");
    }

    /**
     * 根据ID删除
     *
     * @param id 主键ID
     * @return 是否成功
     */
    @Operation(summary = "根据ID删除", description = "根据主键ID删除记录")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteById(@Parameter(description = "主键ID", required = true) @PathVariable ID id) {
        boolean success = service.removeById(id);
        return Result.of(success, "删除成功", "删除失败");
    }

    /**
     * 批量删除
     *
     * @param ids ID列表
     * @return 是否成功
     */
    @Operation(summary = "批量删除", description = "根据ID列表批量删除记录")
    @DeleteMapping("/batch")
    public Result<Boolean> deleteBatch(@Parameter(description = "ID列表", required = true) @RequestBody List<ID> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("ID列表不能为空");
        }
        boolean success = service.removeByIds(ids);
        return Result.of(success, "批量删除成功", "批量删除失败");
    }

    /**
     * 获取总记录数
     *
     * @return 总记录数
     */
    @Operation(summary = "获取总记录数", description = "获取数据表总记录数")
    @GetMapping("/count")
    public Result<Long> count() {
        long count = service.count();
        return Result.success(count);
    }
} 