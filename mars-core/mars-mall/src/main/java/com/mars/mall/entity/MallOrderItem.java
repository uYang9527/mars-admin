package com.mars.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单商品表
 *
 * @author Mars
 * @date 2026-02-03
 */
@Data
@TableName("mall_order_item")
public class MallOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单商品ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 订单ID */
    private Long orderId;

    /** 订单号 */
    private String orderNo;

    /** 商品ID */
    private Long productId;

    /** SKU ID */
    private Long skuId;

    /** 商品名称 */
    private String productName;

    /** 商品图片 */
    private String productImage;

    /** SKU名称 */
    private String skuName;

    /** 商品单价 */
    private BigDecimal price;

    /** 购买数量 */
    private Integer quantity;

    /** 小计金额 */
    private BigDecimal totalAmount;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
