package com.mars.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mars.common.result.PageResult;
import com.mars.common.result.Result;
import com.mars.system.annotation.Log;
import com.mars.system.annotation.Log.BusinessType;
import com.mars.system.entity.Customer;
import com.mars.system.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 客户表
 *
 * @author Mars
 * @date 2026-02-02
 */
@RestController
@RequestMapping("/system/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @SaCheckPermission("system:customer:list")
    public Result<PageResult<Customer>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name) {
        var result = customerService.page(page, pageSize, id, name);
        return Result.ok(PageResult.of(result));
    }

    /**
     * 获取详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("system:customer:query")
    public Result<Customer> getInfo(@PathVariable Long id) {
        return Result.ok(customerService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping
    @SaCheckPermission("system:customer:add")
    @Log(title = "客户表", businessType = BusinessType.INSERT)
    public Result<Void> add(@RequestBody Customer customer) {
        customerService.create(customer);
        return Result.ok();
    }

    /**
     * 修改
     */
    @PutMapping
    @SaCheckPermission("system:customer:edit")
    @Log(title = "客户表", businessType = BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody Customer customer) {
        customerService.update(customer);
        return Result.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    @SaCheckPermission("system:customer:remove")
    @Log(title = "客户表", businessType = BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long[] ids) {
        customerService.delete(ids);
        return Result.ok();
    }
}
