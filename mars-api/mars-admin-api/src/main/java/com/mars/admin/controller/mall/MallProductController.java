package com.mars.admin.controller.mall;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mars.common.result.PageResult;
import com.mars.common.result.Result;
import com.mars.system.annotation.Log;
import com.mars.system.annotation.RepeatSubmit;
import com.mars.system.annotation.Log.BusinessType;
import com.mars.mall.entity.MallProduct;
import com.mars.mall.service.MallProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 商品管理
 *
 * @author Mars
 * @date 2026-02-03
 */
@RestController
@RequestMapping("/mall/product")
@RequiredArgsConstructor
public class MallProductController {

    private final MallProductService productService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @SaCheckPermission("mall:product:list")
    public Result<PageResult<MallProduct>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status) {
        var result = productService.page(page, pageSize, name, categoryId, status);
        return Result.ok(PageResult.of(result));
    }

    /**
     * 获取详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("mall:product:query")
    public Result<MallProduct> getInfo(@PathVariable Long id) {
        return Result.ok(productService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping
    @SaCheckPermission("mall:product:add")
    @RepeatSubmit
    @Log(title = "商品", businessType = BusinessType.INSERT)
    public Result<Void> add(@RequestBody MallProduct product) {
        productService.create(product);
        return Result.ok();
    }

    /**
     * 修改
     */
    @PutMapping
    @SaCheckPermission("mall:product:edit")
    @Log(title = "商品", businessType = BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody MallProduct product) {
        productService.update(product);
        return Result.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    @SaCheckPermission("mall:product:remove")
    @Log(title = "商品", businessType = BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long[] ids) {
        productService.delete(ids);
        return Result.ok();
    }

    /**
     * 上下架
     */
    @PutMapping("/{id}/status/{status}")
    @SaCheckPermission("mall:product:edit")
    @Log(title = "商品上下架", businessType = BusinessType.UPDATE)
    public Result<Void> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        productService.updateStatus(id, status);
        return Result.ok();
    }
}
