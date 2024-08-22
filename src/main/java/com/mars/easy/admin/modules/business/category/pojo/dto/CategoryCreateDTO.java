package com.mars.easy.admin.modules.business.category.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Category添加DTO
 *
 * @author Mars
 * @since 2024-08-22
 */
@Data
@Schema(description = "Category添加DTO")
public class CategoryCreateDTO {

   @Schema(description =  "类型   1 菜品分类 2 套餐分类")
   private Integer type;

   @Schema(description =  "分类名称")
   private String name;

   @Schema(description =  "顺序")
   private Integer sort;

   @Schema(description =  "创建人")
   private Integer createUser;

   @Schema(description =  "修改人")
   private Integer updateUser;

}
