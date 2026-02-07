package com.mars.admin.controller.mall;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mars.common.result.PageResult;
import com.mars.common.result.Result;
import com.mars.system.annotation.Log;
import com.mars.system.annotation.Log.BusinessType;
import com.mars.mall.entity.MallOrder;
import com.mars.mall.service.MallOrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 订单管理
 *
 * @author Mars
 * @date 2026-02-03
 */
@RestController
@RequestMapping("/mall/order")
@RequiredArgsConstructor
public class MallOrderController {

    private final MallOrderService orderService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @SaCheckPermission("mall:order:list")
    public Result<PageResult<MallOrder>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long memberId) {
        var result = orderService.page(page, pageSize, orderNo, status, memberId);
        return Result.ok(PageResult.of(result));
    }

    /**
     * 获取详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("mall:order:query")
    public Result<MallOrder> getInfo(@PathVariable Long id) {
        return Result.ok(orderService.getById(id));
    }

    /**
     * 发货
     */
    @PostMapping("/{id}/ship")
    @SaCheckPermission("mall:order:ship")
    @Log(title = "订单发货", businessType = BusinessType.UPDATE)
    public Result<Void> ship(@PathVariable Long id, @RequestBody ShipRequest request) {
        orderService.ship(id, request.getDeliveryCompany(), request.getDeliveryNo());
        return Result.ok();
    }

    @Data
    public static class ShipRequest {
        private String deliveryCompany;
        private String deliveryNo;
    }
}
