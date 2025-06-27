package com.mars.admin.controller;

import com.mars.admin.controller.base.BaseController;
import com.mars.admin.entity.SysLoginInfo;
import com.mars.admin.framework.common.Result;
import com.mars.admin.service.ISysLoginInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统登录日志Controller
 * 继承BaseController获得基础的增删改查功能
 *
 * @author Mars
 */
@RestController
@RequestMapping("/system/logininfo")
@Tag(name = "系统登录日志管理", description = "系统登录日志管理相关接口")
public class SysLoginInfoController extends BaseController<SysLoginInfo, Long> {

    @Autowired
    private ISysLoginInfoService sysLoginInfoService;

    // 继承BaseController后自动拥有基础的增删改查功能：
    // GET    /system/logininfo/list           - 获取所有登录日志
    // GET    /system/logininfo/{id}           - 根据ID获取登录日志
    // GET    /system/logininfo/page           - 分页查询登录日志
    // POST   /system/logininfo                - 新增登录日志
    // PUT    /system/logininfo                - 更新登录日志
    // DELETE /system/logininfo/{id}           - 删除登录日志
    // DELETE /system/logininfo/batch          - 批量删除登录日志

    /**
     * 根据用户名查询登录日志
     */
    @GetMapping("/user/{userName}")
    @Operation(summary = "根据用户名查询登录日志", description = "根据用户名查询登录日志")
    public Result<List<SysLoginInfo>> getByUserName(@Parameter(description = "用户名") @PathVariable String userName) {
        List<SysLoginInfo> logs = sysLoginInfoService.selectByUserName(userName);
        return Result.success(logs);
    }

    /**
     * 根据状态查询登录日志
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态查询登录日志", description = "根据状态查询登录日志")
    public Result<List<SysLoginInfo>> getByStatus(@Parameter(description = "状态") @PathVariable String status) {
        List<SysLoginInfo> logs = sysLoginInfoService.selectByStatus(status);
        return Result.success(logs);
    }

    /**
     * 根据IP地址查询登录日志
     */
    @GetMapping("/ip/{ipaddr}")
    @Operation(summary = "根据IP地址查询登录日志", description = "根据IP地址查询登录日志")
    public Result<List<SysLoginInfo>> getByIpaddr(@Parameter(description = "IP地址") @PathVariable String ipaddr) {
        List<SysLoginInfo> logs = sysLoginInfoService.selectByIpaddr(ipaddr);
        return Result.success(logs);
    }

    /**
     * 清空登录日志
     */
    @DeleteMapping("/clean")
    @Operation(summary = "清空登录日志", description = "清空登录日志")
    public Result<Void> clean() {
        boolean result = sysLoginInfoService.cleanLoginInfo();
        return result ? Result.success() : Result.error("清空失败");
    }

    /**
     * 导出登录日志
     */
    @PostMapping("/export")
    @Operation(summary = "导出登录日志", description = "导出登录日志")
    public Result<List<SysLoginInfo>> export(@RequestBody SysLoginInfo loginInfo) {
        List<SysLoginInfo> list = sysLoginInfoService.exportLoginInfo(loginInfo);
        return Result.success(list);
    }
} 