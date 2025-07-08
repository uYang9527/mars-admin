package com.mars.admin.controller;

import com.mars.admin.controller.base.BaseController;
import com.mars.admin.entity.SysPost;
import com.mars.admin.framework.common.Result;
import com.mars.admin.framework.common.annotation.OperationLog;
import com.mars.admin.framework.enums.BusinessType;
import com.mars.admin.service.ISysPostService;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class SysPostController extends BaseController<SysPost, Long> {

    private final ISysPostService sysPostService;

    // 继承BaseController后自动拥有基础的增删改查功能：
    // GET    /system/post/list           - 获取所有岗位
    // GET    /system/post/{id}           - 根据ID获取岗位
    // GET    /system/post/page           - 分页查询岗位
    // POST   /system/post                - 新增岗位
    // PUT    /system/post                - 更新岗位
    // DELETE /system/post/{id}           - 删除岗位
    // DELETE /system/post/batch          - 批量删除岗位

    /**
     * 分页条件查询岗位
     *
     * @param pageNum  当前页码
     * @param pageSize 每页大小
     * @param postName 岗位名称
     * @param postCode 岗位编码
     * @param status   状态
     * @return 分页结果
     */
    @GetMapping("/pageList")
    @Operation(summary = "分页条件查询岗位", description = "根据条件分页查询岗位列表")
    public Result<Page<SysPost>> pageList(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "岗位名称") @RequestParam(required = false) String postName,
            @Parameter(description = "岗位编码") @RequestParam(required = false) String postCode,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        // 创建查询条件
        SysPost post = new SysPost();
        post.setPostName(postName);
        post.setPostCode(postCode);
        post.setStatus(status);
        
        // 创建分页对象
        Page<SysPost> page = new Page<>(pageNum, pageSize);
        
        // 调用服务层方法进行查询
        Page<SysPost> postPage = sysPostService.selectPostPage(page, post);
        
        return Result.success(postPage);
    }

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
    @OperationLog(title = "岗位管理", businessType = BusinessType.EXPORT, description = "导出岗位数据")
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
    
    /**
     * 根据ID获取岗位（使用缓存）
     */
    @GetMapping("/cache/{id}")
    @Operation(summary = "根据ID获取岗位（使用缓存）", description = "根据ID获取岗位信息，优先从缓存获取")
    public Result<SysPost> getPostByIdWithCache(@Parameter(description = "岗位ID") @PathVariable Long id) {
        SysPost post = sysPostService.getPostById(id);
        return Result.success(post);
    }
    
    /**
     * 新增岗位（重写BaseController的方法）
     *
     * @param entity 岗位信息
     * @return 是否成功
     */
    @Operation(summary = "新增岗位", description = "新增岗位信息")
    @PostMapping
    @OperationLog(title = "岗位管理", businessType = BusinessType.INSERT, 
                  description = "新增岗位：#{#entity.postName}")
    @Override
    public Result<Boolean> save(@RequestBody SysPost entity) {
        boolean success = sysPostService.save(entity);
        return Result.of(success, "新增成功", "新增失败");
    }

    /**
     * 修改岗位（重写BaseController的方法）
     *
     * @param entity 岗位信息
     * @return 是否成功
     */
    @Operation(summary = "修改岗位", description = "修改岗位信息")
    @PutMapping
    @OperationLog(title = "岗位管理", businessType = BusinessType.UPDATE, 
                  description = "修改岗位：#{#entity.postName}")
    @Override
    public Result<Boolean> update(@RequestBody SysPost entity) {
        boolean success = sysPostService.updateById(entity);
        return Result.of(success, "修改成功", "修改失败");
    }
    
    /**
     * 修改岗位状态
     */
    @PutMapping("/changeStatus")
    @Operation(summary = "修改岗位状态", description = "修改岗位状态")
    @OperationLog(title = "岗位管理", businessType = BusinessType.UPDATE, 
                  description = "修改岗位状态，ID：#{#post.id}，状态：#{#post.status}")
    public Result<Void> changeStatus(@RequestBody SysPost post) {
        sysPostService.updateById(post);
        return Result.success();
    }
    
    /**
     * 批量删除岗位（重写BaseController的方法）
     *
     * @param ids ID列表
     * @return 删除结果
     */
    @Operation(summary = "批量删除岗位", description = "根据ID列表批量删除岗位及关联数据")
    @DeleteMapping("/batch")
    @OperationLog(title = "岗位管理", businessType = BusinessType.DELETE, 
                  description = "批量删除岗位，ID列表：#{#ids}")
    public Result<Boolean> deleteBatch(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("ID列表不能为空");
        }
        Long[] postIds = ids.toArray(new Long[0]);
        boolean success = sysPostService.deletePosts(postIds);
        return Result.of(success, "批量删除成功", "批量删除失败");
    }
    
    /**
     * 根据ID删除岗位（重写BaseController的方法）
     *
     * @param id 主键ID
     * @return 是否成功
     */
    @Operation(summary = "根据ID删除岗位", description = "根据主键ID删除岗位及关联数据")
    @DeleteMapping("/{id}")
    @OperationLog(title = "岗位管理", businessType = BusinessType.DELETE, 
                  description = "删除岗位，ID：#{#id}")
    @Override
    public Result<Boolean> deleteById(@PathVariable Long id) {
        boolean success = sysPostService.deletePostById(id);
        return Result.of(success, "删除成功", "删除失败");
    }
} 