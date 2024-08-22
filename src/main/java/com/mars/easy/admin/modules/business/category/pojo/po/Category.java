package com.mars.easy.admin.modules.business.category.pojo.po;

import com.mybatisflex.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.io.Serial;
import com.mars.easy.admin.framework.datascope.EntityChangeListener;
import java.time.LocalDateTime;

/**
* 菜品及套餐分类
*
* @author Mars
* @since 2024-08-22
*/
@Data
@Table(value = "category")
@Schema(description = "菜品及套餐分类")
public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Schema(description ="主键")
    private Long id;

    @Schema(description ="类型   1 菜品分类 2 套餐分类")
    private Integer type;

    @Schema(description ="分类名称")
    private String name;

    @Schema(description ="顺序")
    private Integer sort;

    @Schema(description ="创建时间")
    private LocalDateTime createTime;

    @Schema(description ="更新时间")
    private LocalDateTime updateTime;

    @Schema(description ="创建人")
    private Integer createUser;

    @Schema(description ="修改人")
    private Integer updateUser;

}
