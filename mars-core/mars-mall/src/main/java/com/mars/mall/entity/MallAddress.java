package com.mars.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 收货地址表
 *
 * @author Mars
 * @date 2026-02-03
 */
@Data
@TableName("mall_address")
public class MallAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 地址ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 会员ID */
    private Long memberId;

    /** 收货人姓名 */
    private String name;

    /** 手机号 */
    private String phone;

    /** 省份 */
    private String province;

    /** 城市 */
    private String city;

    /** 区县 */
    private String district;

    /** 详细地址 */
    private String detail;

    /** 邮政编码 */
    private String postalCode;

    /** 是否默认(0-否 1-是) */
    private Integer isDefault;

    /** 删除标记 */
    @TableLogic
    private Integer deleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 获取完整地址
     */
    public String getFullAddress() {
        return province + " " + city + " " + district + " " + detail;
    }
}
