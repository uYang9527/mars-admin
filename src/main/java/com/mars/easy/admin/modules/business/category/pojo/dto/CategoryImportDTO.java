package com.mars.easy.admin.modules.business.category.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * Category导入DTO
 *
 * @author Mars
 * @since 2024-08-22
 */
@Data
@Schema(description = "Category导入DTO")
public class CategoryImportDTO {

    @ExcelProperty(value = "类型   1 菜品分类 2 套餐分类")
    @Schema(description =  "类型   1 菜品分类 2 套餐分类")
    private Integer type;

    @ExcelProperty(value = "分类名称")
    @Schema(description =  "分类名称")
    private String name;

    @ExcelProperty(value = "顺序")
    @Schema(description =  "顺序")
    private Integer sort;

    @ExcelProperty(value = "创建人")
    @Schema(description =  "创建人")
    private Integer createUser;

    @ExcelProperty(value = "修改人")
    @Schema(description =  "修改人")
    private Integer updateUser;

}
