package com.mars.admin.controller;

import com.mars.admin.controller.base.BaseController;
import com.mars.admin.entity.SysPost;
import com.mars.admin.framework.common.Result;
import com.mars.admin.service.ISysPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统岗位Controller
 * 继承BaseController获得基础的增删改查功能
 *
 * @author Mars
 */
@RestController
@RequestMapping("/system/post")
@Tag(name = "系统岗位管理", description = "系统岗位管理相关接口")
public class SysPostController extends BaseController<SysPost, Long> {

    @Autowired
    private ISysPostService sysPostService;

    // 继承BaseController后自动拥有基础的增删改查功能：
    // GET    /system/post/list           - 获取所有岗位
    // GET    /system/post/{id}           - 根据ID获取岗位
    // GET    /system/post/page           - 分页查询岗位
    // POST   /system/post                - 新增岗位
    // PUT    /system/post                - 更新岗位
    // DELETE /system/post/{id}           - 删除岗位
    // DELETE /system/post/batch          - 批量删除岗位

    /**
     * 查询所有正常状态的岗位
     */
    @GetMapping("/optionselect")
    @Operation(summary = "查询所有正常状态的岗位", description = "查询所有正常状态的岗位")
    public Result<List<SysPost>> optionselect() {
        List<SysPost> posts = sysPostService.selectNormalPosts();
        return Result.success(posts);
    }

    /**
     * 根据用户ID查询岗位
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "根据用户ID查询岗位", description = "根据用户ID查询岗位")
    public Result<List<SysPost>> getPostsByUserId(@Parameter(description = "用户ID") @PathVariable Long userId) {
        List<SysPost> posts = sysPostService.selectPostsByUserId(userId);
        return Result.success(posts);
    }

    /**
     * 导出岗位数据
     */
    @PostMapping("/export")
    @Operation(summary = "导出岗位数据", description = "导出岗位数据")
    public Result<List<SysPost>> export(@RequestBody SysPost post) {
        List<SysPost> list = sysPostService.exportPost(post);
        return Result.success(list);
    }

    /**
     * 校验岗位编码是否唯一
     */
    @GetMapping("/checkPostCodeUnique")
    @Operation(summary = "校验岗位编码是否唯一", description = "校验岗位编码是否唯一")
    public Result<Boolean> checkPostCodeUnique(
            @Parameter(description = "岗位编码") @RequestParam String postCode,
            @Parameter(description = "岗位ID") @RequestParam(required = false) Long postId) {
        boolean unique = sysPostService.checkPostCodeUnique(postCode, postId);
        return Result.success(unique);
    }

    /**
     * 校验岗位名称是否唯一
     */
    @GetMapping("/checkPostNameUnique")
    @Operation(summary = "校验岗位名称是否唯一", description = "校验岗位名称是否唯一")
    public Result<Boolean> checkPostNameUnique(
            @Parameter(description = "岗位名称") @RequestParam String postName,
            @Parameter(description = "岗位ID") @RequestParam(required = false) Long postId) {
        boolean unique = sysPostService.checkPostNameUnique(postName, postId);
        return Result.success(unique);
    }
} 