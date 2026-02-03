package com.mars.web.controller.mall;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mars.common.result.PageResult;
import com.mars.common.result.Result;
import com.mars.system.annotation.Log;
import com.mars.system.annotation.Log.BusinessType;
import com.mars.system.entity.mall.MallBanner;
import com.mars.system.service.mall.MallBannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 轮播图管理
 *
 * @author Mars
 * @date 2026-02-03
 */
@RestController
@RequestMapping("/mall/banner")
@RequiredArgsConstructor
public class MallBannerController {

    private final MallBannerService bannerService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @SaCheckPermission("mall:banner:list")
    public Result<PageResult<MallBanner>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String position) {
        var result = bannerService.page(page, pageSize, position);
        return Result.ok(PageResult.of(result));
    }

    /**
     * 获取详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("mall:banner:query")
    public Result<MallBanner> getInfo(@PathVariable Long id) {
        return Result.ok(bannerService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping
    @SaCheckPermission("mall:banner:add")
    @Log(title = "轮播图", businessType = BusinessType.INSERT)
    public Result<Void> add(@RequestBody MallBanner banner) {
        bannerService.create(banner);
        return Result.ok();
    }

    /**
     * 修改
     */
    @PutMapping
    @SaCheckPermission("mall:banner:edit")
    @Log(title = "轮播图", businessType = BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody MallBanner banner) {
        bannerService.update(banner);
        return Result.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    @SaCheckPermission("mall:banner:remove")
    @Log(title = "轮播图", businessType = BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long[] ids) {
        bannerService.delete(ids);
        return Result.ok();
    }

    /**
     * 更新状态
     */
    @PutMapping("/{id}/status/{status}")
    @SaCheckPermission("mall:banner:edit")
    @Log(title = "轮播图状态", businessType = BusinessType.UPDATE)
    public Result<Void> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        bannerService.updateStatus(id, status);
        return Result.ok();
    }
}
