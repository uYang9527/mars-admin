package com.mars.easy.admin.modules.business.category.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import com.mars.easy.admin.framework.entity.ApiPageResult;
import com.mars.easy.admin.framework.entity.ApiResult;
import com.mars.easy.admin.framework.constant.GlobalConstant;
import com.mars.easy.admin.framework.entity.PageResult;
import com.mars.easy.admin.framework.entity.SelectIdsDTO;
import com.mars.easy.admin.modules.business.category.service.CategoryService;
import com.mars.easy.admin.modules.business.category.pojo.dto.CategoryCreateDTO;
import com.mars.easy.admin.modules.business.category.pojo.dto.CategoryUpdateDTO;
import com.mars.easy.admin.modules.business.category.pojo.dto.CategoryListDTO;
import com.mars.easy.admin.modules.business.category.pojo.vo.CategoryVO;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * 菜品及套餐分类 Controller
 *
 * @author Mars
 * @since 2024-08-22
 */
@Tag(name =  "菜品及套餐分类")
@RestController
@RequestMapping("category")
@RequiredArgsConstructor
public class CategoryController  {

    private final CategoryService categoryService;

    @Operation(summary = "新增")
    @SaCheckPermission(value = "category.create", orRole = GlobalConstant.SUPER_ROLE)
    @PostMapping
    public ApiResult create(@RequestBody CategoryCreateDTO dto) {
        categoryService.create(dto);
        return ApiResult.success();
    }

    @Operation(summary = "修改")
    @SaCheckPermission(value = "category.update", orRole = GlobalConstant.SUPER_ROLE)
    @PutMapping
    public ApiResult update(@RequestBody CategoryUpdateDTO dto) {
        categoryService.update(dto);
        return ApiResult.success();
    }

    @Operation(summary = "删除")
    @SaCheckPermission(value = "category.remove", orRole = GlobalConstant.SUPER_ROLE)
    @DeleteMapping
    public ApiResult remove(@RequestBody SelectIdsDTO dto) {
        categoryService.remove(dto);
        return ApiResult.success();
    }

    @Operation(summary = "列表查询")
    @SaCheckPermission(value = "category.query_table", orRole = GlobalConstant.SUPER_ROLE)
    @GetMapping
    public ApiResult<PageResult<CategoryVO>> list(CategoryListDTO dto) {
        return ApiPageResult.success(categoryService.page(dto));
    }

    @Operation(summary = "详情")
    @SaCheckPermission(value = "category.query_table", orRole = GlobalConstant.SUPER_ROLE)
    @GetMapping("/{id}")
    public ApiResult<CategoryVO> detail(@PathVariable Object id) {
        return ApiResult.success(categoryService.detail(id));
    }

    @Operation(summary = "导入")
    @Parameters({
      @Parameter(name = "file", description = "上传文件", schema = @Schema(type = "string", format = "binary"), required = true),
    })
    @SaCheckPermission(value = "category.import", orRole = GlobalConstant.SUPER_ROLE)
    @PostMapping("/import")
    public void importExcel(MultipartFile file) {
        categoryService.importExcel(file);
    }

    @Operation(summary = "导出")
    @SaCheckPermission(value = "category.export", orRole = GlobalConstant.SUPER_ROLE)
    @PostMapping("/export")
    public void exportExcel(@RequestBody CategoryListDTO dto, HttpServletResponse response) {
        categoryService.exportExcel(dto, response);
    }
}