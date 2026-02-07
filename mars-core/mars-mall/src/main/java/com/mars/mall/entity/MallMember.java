package com.mars.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 商城会员表
 *
 * @author Mars
 * @date 2026-02-03
 */
@Data
@TableName("mall_member")
public class MallMember implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 会员ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 微信openId */
    private String openId;

    /** 微信unionId */
    private String unionId;

    /** 昵称 */
    private String nickname;

    /** 头像 */
    private String avatar;

    /** 手机号 */
    private String phone;

    /** 性别(0-未知 1-男 2-女) */
    private Integer gender;

    /** 生日 */
    private LocalDate birthday;

    /** 积分 */
    private Integer points;

    /** 余额 */
    private BigDecimal balance;

    /** 会员等级(1-普通 2-银卡 3-金卡 4-钻石) */
    private Integer level;

    /** 状态(0-禁用 1-正常) */
    private Integer status;

    /** 最后登录时间 */
    private LocalDateTime lastLoginTime;

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
