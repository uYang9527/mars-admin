package com.mars.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单表
 *
 * @author Mars
 * @date 2026-02-03
 */
@Data
@TableName("mall_order")
public class MallOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 订单号 */
    private String orderNo;

    /** 会员ID */
    private Long memberId;

    /** 订单总金额 */
    private BigDecimal totalAmount;

    /** 实付金额 */
    private BigDecimal payAmount;

    /** 运费 */
    private BigDecimal freightAmount;

    /** 优惠金额 */
    private BigDecimal discountAmount;

    /** 优惠券ID */
    private Long couponId;

    /** 支付方式(1-微信 2-支付宝) */
    private Integer payType;

    /** 支付时间 */
    private LocalDateTime payTime;

    /** 支付流水号 */
    private String payNo;

    /** 订单状态(0-待付款 1-待发货 2-待收货 3-已完成 4-已取消 5-已退款) */
    private Integer status;

    /** 收货人姓名 */
    private String receiverName;

    /** 收货人电话 */
    private String receiverPhone;

    /** 省份 */
    private String receiverProvince;

    /** 城市 */
    private String receiverCity;

    /** 区县 */
    private String receiverDistrict;

    /** 详细地址 */
    private String receiverDetail;

    /** 物流公司 */
    private String deliveryCompany;

    /** 物流单号 */
    private String deliveryNo;

    /** 发货时间 */
    private LocalDateTime deliveryTime;

    /** 收货时间 */
    private LocalDateTime receiveTime;

    /** 订单备注 */
    private String remark;

    /** 删除标记 */
    @TableLogic
    private Integer deleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // ========== 非数据库字段 ==========

    /** 订单商品列表 */
    @TableField(exist = false)
    private List<MallOrderItem> orderItems;

    /** 会员昵称 */
    @TableField(exist = false)
    private String memberNickname;

    /** 会员手机号 */
    @TableField(exist = false)
    private String memberPhone;

    /**
     * 获取完整收货地址
     */
    public String getFullAddress() {
        return receiverProvince + " " + receiverCity + " " + receiverDistrict + " " + receiverDetail;
    }

    // 订单状态常量
    public static final int STATUS_PENDING_PAY = 0;     // 待付款
    public static final int STATUS_PENDING_SHIP = 1;    // 待发货
    public static final int STATUS_PENDING_RECEIVE = 2; // 待收货
    public static final int STATUS_COMPLETED = 3;       // 已完成
    public static final int STATUS_CANCELLED = 4;       // 已取消
    public static final int STATUS_REFUNDED = 5;        // 已退款

    // 支付方式常量
    public static final int PAY_TYPE_WECHAT = 1;  // 微信支付
    public static final int PAY_TYPE_ALIPAY = 2;  // 支付宝
}
