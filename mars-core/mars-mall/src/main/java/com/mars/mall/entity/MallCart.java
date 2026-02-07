package com.mars.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车表
 *
 * @author Mars
 * @date 2026-02-03
 */
@Data
@TableName("mall_cart")
public class MallCart implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 购物车ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 会员ID */
    private Long memberId;

    /** 商品ID */
    private Long productId;

    /** SKU ID */
    private Long skuId;

    /** 数量 */
    private Integer quantity;

    /** 是否选中(0-否 1-是) */
    private Integer selected;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // ========== 非数据库字段 ==========

    /** 商品名称 */
    @TableField(exist = false)
    private String productName;

    /** 商品图片 */
    @TableField(exist = false)
    private String productImage;

    /** 商品价格 */
    @TableField(exist = false)
    private BigDecimal productPrice;

    /** SKU名称 */
    @TableField(exist = false)
    private String skuName;

    /** SKU价格 */
    @TableField(exist = false)
    private BigDecimal skuPrice;

    /** 库存 */
    @TableField(exist = false)
    private Integer stock;
}
