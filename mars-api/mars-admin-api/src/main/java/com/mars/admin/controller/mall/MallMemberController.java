package com.mars.admin.controller.mall;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mars.common.result.PageResult;
import com.mars.common.result.Result;
import com.mars.system.annotation.Log;
import com.mars.system.annotation.Log.BusinessType;
import com.mars.mall.entity.MallMember;
import com.mars.mall.service.MallMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 商城会员管理
 *
 * @author Mars
 * @date 2026-02-03
 */
@RestController
@RequestMapping("/mall/member")
@RequiredArgsConstructor
public class MallMemberController {

    private final MallMemberService memberService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @SaCheckPermission("mall:member:list")
    public Result<PageResult<MallMember>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String nickname) {
        var result = memberService.page(page, pageSize, phone, nickname);
        return Result.ok(PageResult.of(result));
    }

    /**
     * 获取详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("mall:member:query")
    public Result<MallMember> getInfo(@PathVariable Long id) {
        return Result.ok(memberService.getById(id));
    }

    /**
     * 修改会员信息
     */
    @PutMapping
    @SaCheckPermission("mall:member:edit")
    @Log(title = "商城会员", businessType = BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody MallMember member) {
        memberService.update(member);
        return Result.ok();
    }
}
