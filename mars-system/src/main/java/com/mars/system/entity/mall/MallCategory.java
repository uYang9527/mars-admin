package com.mars.system.entity.mall;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品分类表
 *
 * @author Mars
 * @date 2026-02-03
 */
@Data
@TableName("mall_category")
public class MallCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 分类ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 父分类ID */
    private Long parentId;

    /** 分类名称 */
    private String name;

    /** 分类图标 */
    private String icon;

    /** 分类图片 */
    private String image;

    /** 排序 */
    private Integer sort;

    /** 状态(0-禁用 1-正常) */
    private Integer status;

    /** 删除标记 */
    @TableLogic
    private Integer deleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 子分类列表 */
    @TableField(exist = false)
    private List<MallCategory> children;
}
