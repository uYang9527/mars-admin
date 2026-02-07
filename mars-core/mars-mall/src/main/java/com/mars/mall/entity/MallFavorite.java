package com.mars.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品收藏表
 *
 * @author Mars
 * @date 2026-02-03
 */
@Data
@TableName("mall_favorite")
public class MallFavorite implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 收藏ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 会员ID */
    private Long memberId;

    /** 商品ID */
    private Long productId;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // ========== 非数据库字段 ==========

    /** 商品信息 */
    @TableField(exist = false)
    private MallProduct product;
}
