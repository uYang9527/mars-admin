package com.mars.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品SKU表
 *
 * @author Mars
 * @date 2026-02-03
 */
@Data
@TableName("mall_product_sku")
public class MallProductSku implements Serializable {

    private static final long serialVersionUID = 1L;

    /** SKU ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 商品ID */
    private Long productId;

    /** SKU名称 */
    private String skuName;

    /** SKU图片 */
    private String skuImage;

    /** 价格 */
    private BigDecimal price;

    /** 原价 */
    private BigDecimal originalPrice;

    /** 库存 */
    private Integer stock;

    /** 销量 */
    private Integer sales;

    /** 规格属性(JSON) */
    private String specs;

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
}
