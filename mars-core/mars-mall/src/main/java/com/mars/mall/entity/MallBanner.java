package com.mars.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 轮播图表
 *
 * @author Mars
 * @date 2026-02-03
 */
@Data
@TableName("mall_banner")
public class MallBanner implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 轮播图ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 标题 */
    private String title;

    /** 副标题 */
    private String subtitle;

    /** 图片地址 */
    private String image;

    /** 链接类型(0-无 1-商品 2-分类 3-外链) */
    private Integer linkType;

    /** 链接值 */
    private String linkValue;

    /** 位置(home-首页) */
    private String position;

    /** 排序 */
    private Integer sort;

    /** 状态(0-禁用 1-正常) */
    private Integer status;

    /** 开始时间 */
    private LocalDateTime startTime;

    /** 结束时间 */
    private LocalDateTime endTime;

    /** 删除标记 */
    @TableLogic
    private Integer deleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 链接类型常量
    public static final int LINK_TYPE_NONE = 0;     // 无链接
    public static final int LINK_TYPE_PRODUCT = 1;  // 商品
    public static final int LINK_TYPE_CATEGORY = 2; // 分类
    public static final int LINK_TYPE_URL = 3;      // 外链
}
