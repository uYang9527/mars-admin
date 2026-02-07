package com.mars.web.controller.mall;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mars.common.result.PageResult;
import com.mars.common.result.Result;
import com.mars.system.annotation.Log;
import com.mars.system.annotation.RepeatSubmit;
import com.mars.system.annotation.Log.BusinessType;
import com.mars.system.entity.mall.MallCategory;
import com.mars.system.service.mall.MallCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品分类管理
 *
 * @author Mars
 * @date 2026-02-03
 */
@RestController
@RequestMapping("/mall/category")
@RequiredArgsConstructor
public class MallCategoryController {

    private final MallCategoryService categoryService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @SaCheckPermission("mall:category:list")
    public Result<PageResult<MallCategory>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {
        var result = categoryService.page(page, pageSize, name);
        return Result.ok(PageResult.of(result));
    }

    /**
     * 获取分类树
     */
    @GetMapping("/tree")
    @SaCheckPermission("mall:category:list")
    public Result<List<MallCategory>> tree() {
        return Result.ok(categoryService.listTree());
    }

    /**
     * 获取详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("mall:category:query")
    public Result<MallCategory> getInfo(@PathVariable Long id) {
        return Result.ok(categoryService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping
    @SaCheckPermission("mall:category:add")
    @RepeatSubmit
    @Log(title = "商品分类", businessType = BusinessType.INSERT)
    public Result<Void> add(@RequestBody MallCategory category) {
        categoryService.create(category);
        return Result.ok();
    }

    /**
     * 修改
     */
    @PutMapping
    @SaCheckPermission("mall:category:edit")
    @Log(title = "商品分类", businessType = BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody MallCategory category) {
        categoryService.update(category);
        return Result.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    @SaCheckPermission("mall:category:remove")
    @Log(title = "商品分类", businessType = BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long[] ids) {
        categoryService.delete(ids);
        return Result.ok();
    }
}
