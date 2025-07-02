package com.mars.admin.controller;

import com.mars.admin.controller.base.BaseController;
import com.mars.admin.entity.SysLoginInfo;
import com.mars.admin.framework.common.Result;
import com.mars.admin.framework.util.ExcelUtils;
import com.mars.admin.service.ISysLoginInfoService;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 系统登录日志Controller
 * 继承BaseController获得基础的增删改查功能
 *
 * @author Mars
 */
@RestController
@RequestMapping("/system/logininfor")
@Tag(name = "系统登录日志管理", description = "系统登录日志管理相关接口")
public class SysLoginInfoController extends BaseController<SysLoginInfo, Long> {

    @Autowired
    private ISysLoginInfoService sysLoginInfoService;

    // 继承BaseController后自动拥有基础的增删改查功能：
    // GET    /system/logininfor/list           - 获取所有登录日志
    // GET    /system/logininfor/{id}           - 根据ID获取登录日志
    // GET    /system/logininfor/page           - 分页查询登录日志（基础功能，无条件查询）
    // GET    /system/logininfor/pageList       - 分页查询登录日志（支持条件搜索）
    // POST   /system/logininfor                - 新增登录日志
    // PUT    /system/logininfor                - 更新登录日志
    // DELETE /system/logininfor/{id}           - 删除登录日志
    // DELETE /system/logininfor/batch          - 批量删除登录日志

    /**
     * 分页查询登录日志列表（支持搜索条件）
     */
    @GetMapping("/pageList")
    @Operation(summary = "分页查询登录日志列表", description = "分页查询登录日志列表，支持多条件搜索")
    public Result<Page<SysLoginInfo>> pageList(
            @Parameter(description = "当前页", example = "1") @RequestParam(value = "current", defaultValue = "1") Integer current,
            @Parameter(description = "每页大小", example = "10") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @Parameter(description = "用户账号") @RequestParam(value = "userName", required = false) String userName,
            @Parameter(description = "登录IP") @RequestParam(value = "ipaddr", required = false) String ipaddr,
            @Parameter(description = "登录状态") @RequestParam(value = "status", required = false) String status,
            @Parameter(description = "登录地点") @RequestParam(value = "loginLocation", required = false) String loginLocation) {
        
        // 构建查询条件
        SysLoginInfo queryCondition = new SysLoginInfo();
        queryCondition.setUserName(userName);
        queryCondition.setIpaddr(ipaddr);
        queryCondition.setStatus(status);
        queryCondition.setLoginLocation(loginLocation);
        
        // 分页查询
        Page<SysLoginInfo> page = sysLoginInfoService.selectLoginInfoPage(Page.of(current, size), queryCondition);
        return Result.success(page);
    }

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
    @Operation(summary = "导出登录日志", description = "根据条件导出登录日志")
    public Result<List<SysLoginInfo>> exportByPost(@RequestBody(required = false) SysLoginInfo loginInfo) {
        if (loginInfo == null) {
            loginInfo = new SysLoginInfo();
        }
        List<SysLoginInfo> list = sysLoginInfoService.exportLoginInfo(loginInfo);
        return Result.success(list);
    }

    /**
     * 导出登录日志为Excel文件
     */
    @PostMapping("/exportExcel")
    @Operation(summary = "导出登录日志为Excel文件", description = "根据条件导出登录日志为Excel文件")
    public void exportExcel(@RequestBody(required = false) SysLoginInfo loginInfo, HttpServletResponse response) {
        try {
            if (loginInfo == null) {
                loginInfo = new SysLoginInfo();
            }
            List<SysLoginInfo> list = sysLoginInfoService.exportLoginInfo(loginInfo);
            ExcelUtils.exportLoginLogToExcel(response, list);
        } catch (Exception e) {
            throw new RuntimeException("导出Excel失败: " + e.getMessage());
        }
    }
} 