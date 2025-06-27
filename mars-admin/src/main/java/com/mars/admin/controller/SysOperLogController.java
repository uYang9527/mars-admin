package com.mars.admin.controller;

import com.mars.admin.controller.base.BaseController;
import com.mars.admin.entity.SysOperLog;
import com.mars.admin.framework.common.Result;
import com.mars.admin.service.ISysOperLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统操作日志Controller
 * 继承BaseController获得基础的增删改查功能
 *
 * @author Mars
 */
@RestController
@RequestMapping("/system/operlog")
@Tag(name = "系统操作日志管理", description = "系统操作日志管理相关接口")
public class SysOperLogController extends BaseController<SysOperLog, Long> {

    @Autowired
    private ISysOperLogService sysOperLogService;

    // 继承BaseController后自动拥有基础的增删改查功能：
    // GET    /system/operlog/list           - 获取所有操作日志
    // GET    /system/operlog/{id}           - 根据ID获取操作日志
    // GET    /system/operlog/page           - 分页查询操作日志
    // POST   /system/operlog                - 新增操作日志
    // PUT    /system/operlog                - 更新操作日志
    // DELETE /system/operlog/{id}           - 删除操作日志
    // DELETE /system/operlog/batch          - 批量删除操作日志

    /**
     * 根据用户名查询操作日志
     */
    @GetMapping("/user/{userName}")
    @Operation(summary = "根据用户名查询操作日志", description = "根据用户名查询操作日志")
    public Result<List<SysOperLog>> getByUserName(@Parameter(description = "用户名") @PathVariable String userName) {
        // 使用Service层的方法，这些方法在Service实现类中定义
        List<SysOperLog> logs = sysOperLogService.selectByOperName(userName);
        return Result.success(logs);
    }

    /**
     * 根据操作类型查询操作日志
     */
    @GetMapping("/type/{operType}")
    @Operation(summary = "根据操作类型查询操作日志", description = "根据操作类型查询操作日志")
    public Result<List<SysOperLog>> getByOperType(@Parameter(description = "操作类型") @PathVariable Integer operType) {
        List<SysOperLog> logs = sysOperLogService.selectByBusinessType(operType);
        return Result.success(logs);
    }

    /**
     * 根据状态查询操作日志
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态查询操作日志", description = "根据状态查询操作日志")
    public Result<List<SysOperLog>> getByStatus(@Parameter(description = "状态") @PathVariable Integer status) {
        List<SysOperLog> logs = sysOperLogService.selectByStatus(status);
        return Result.success(logs);
    }

    /**
     * 清空操作日志
     */
    @DeleteMapping("/clean")
    @Operation(summary = "清空操作日志", description = "清空操作日志")
    public Result<Void> clean() {
        boolean result = sysOperLogService.cleanOperLog();
        return result ? Result.success() : Result.error("清空失败");
    }

    /**
     * 导出操作日志
     */
    @PostMapping("/export")
    @Operation(summary = "导出操作日志", description = "导出操作日志")
    public Result<List<SysOperLog>> export(@RequestBody SysOperLog operLog) {
        List<SysOperLog> list = sysOperLogService.exportOperLog(operLog);
        return Result.success(list);
    }
} 