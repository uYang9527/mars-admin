package com.mars.easy.admin.modules.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;

import com.mars.easy.admin.framework.constant.GlobalConstant;
import com.mars.easy.admin.framework.entity.ApiPageResult;
import com.mars.easy.admin.framework.entity.ApiResult;
import com.mars.easy.admin.framework.entity.PageResult;
import com.mars.easy.admin.framework.entity.SelectIdsDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysconfig.SysConfigCreateDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysconfig.SysConfigListDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysconfig.SysConfigUpdateDTO;
import com.mars.easy.admin.modules.system.pojo.po.SysConfig;
import com.mars.easy.admin.modules.system.service.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 参数配置表 前端控制器
 * </p>
 *
 * @Author 程序员Mars
 * @since 2023-11-23
 */
@Tag(name =  "参数配置")
@RestController
@RequestMapping("/sys-config")
@RequiredArgsConstructor
public class SysConfigController {

    private final SysConfigService sysConfigService;

    @Operation(summary ="新增")
    @SaCheckPermission(value = "sys.config.add_btn", orRole = GlobalConstant.SUPER_ROLE)
    @PostMapping
    public ApiResult create(@RequestBody SysConfigCreateDTO dto) {
        sysConfigService.create(dto);
        return ApiResult.success();
    }

    @Operation(summary ="修改")
    @SaCheckPermission(value = "sys.config.update_btn", orRole = GlobalConstant.SUPER_ROLE)
    @PutMapping
    public ApiResult update(@RequestBody SysConfigUpdateDTO dto) {
        sysConfigService.update(dto);
        return ApiResult.success();
    }

    @Operation(summary ="删除")
    @SaCheckPermission(value = "sys.config.delete_btn", orRole = GlobalConstant.SUPER_ROLE)
    @DeleteMapping
    public ApiResult remove(@RequestBody SelectIdsDTO dto) {
        sysConfigService.remove(dto);
        return ApiResult.success();
    }

    @Operation(summary ="列表查询")
    @SaCheckPermission(value = "sys.config.query_table", orRole = GlobalConstant.SUPER_ROLE)
    @GetMapping
    public ApiResult<PageResult<SysConfig>> list(SysConfigListDTO dto) {
        return ApiPageResult.success(sysConfigService.list(dto));
    }

}
