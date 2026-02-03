package com.mars.system.entity.mall;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员优惠券表
 *
 * @author Mars
 * @date 2026-02-03
 */
@Data
@TableName("mall_member_coupon")
public class MallMemberCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 会员ID */
    private Long memberId;

    /** 优惠券ID */
    private Long couponId;

    /** 状态(0-未使用 1-已使用 2-已过期) */
    private Integer status;

    /** 使用时间 */
    private LocalDateTime useTime;

    /** 使用订单ID */
    private Long orderId;

    /** 领取时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // ========== 非数据库字段 ==========

    /** 优惠券信息 */
    @TableField(exist = false)
    private MallCoupon coupon;

    // 状态常量
    public static final int STATUS_UNUSED = 0;   // 未使用
    public static final int STATUS_USED = 1;     // 已使用
    public static final int STATUS_EXPIRED = 2;  // 已过期
}
