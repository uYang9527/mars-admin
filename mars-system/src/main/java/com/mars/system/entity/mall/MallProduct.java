package com.mars.system.entity.mall;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品表
 *
 * @author Mars
 * @date 2026-02-03
 */
@Data
@TableName("mall_product")
public class MallProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 商品ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 分类ID */
    private Long categoryId;

    /** 商品名称 */
    private String name;

    /** 副标题 */
    private String subtitle;

    /** 主图 */
    private String mainImage;

    /** 图片列表(JSON数组) */
    private String images;

    /** 商品详情(富文本) */
    private String detail;

    /** 价格 */
    private BigDecimal price;

    /** 原价 */
    private BigDecimal originalPrice;

    /** 库存 */
    private Integer stock;

    /** 销量 */
    private Integer sales;

    /** 单位 */
    private String unit;

    /** 重量(kg) */
    private BigDecimal weight;

    /** 是否热门(0-否 1-是) */
    private Integer isHot;

    /** 是否新品(0-否 1-是) */
    private Integer isNew;

    /** 是否推荐(0-否 1-是) */
    private Integer isRecommend;

    /** 排序 */
    private Integer sort;

    /** 状态(0-下架 1-上架) */
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

    /** 分类名称 */
    @TableField(exist = false)
    private String categoryName;

    /** SKU列表 */
    @TableField(exist = false)
    private List<MallProductSku> skuList;

    /** 是否收藏 */
    @TableField(exist = false)
    private Boolean isFavorite;
}
