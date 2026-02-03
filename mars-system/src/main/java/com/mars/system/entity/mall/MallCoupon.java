package com.mars.system.entity.mall;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券表
 *
 * @author Mars
 * @date 2026-02-03
 */
@Data
@TableName("mall_coupon")
public class MallCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 优惠券ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 优惠券名称 */
    private String name;

    /** 类型(1-满减券 2-折扣券 3-无门槛券) */
    private Integer type;

    /** 优惠金额/折扣 */
    private BigDecimal amount;

    /** 最低消费金额 */
    private BigDecimal minAmount;

    /** 发放总量(0-不限) */
    private Integer totalCount;

    /** 已领取数量 */
    private Integer receiveCount;

    /** 已使用数量 */
    private Integer useCount;

    /** 每人限领数量 */
    private Integer perLimit;

    /** 开始时间 */
    private LocalDateTime startTime;

    /** 结束时间 */
    private LocalDateTime endTime;

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

    // 优惠券类型常量
    public static final int TYPE_FULL_REDUCE = 1;  // 满减券
    public static final int TYPE_DISCOUNT = 2;     // 折扣券
    public static final int TYPE_NO_THRESHOLD = 3; // 无门槛券
}
