package com.mars.admin.modules.system.domain.bo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SysMenuCreateBo {


    private String id;

    @NotNull(message = "菜单名称不能为空")
    private String title;

    /**
     * 父级id
     */
    private String pid;

    /**
     * 路径
     */
    private String path;

    private String name;

    /**
     * icon图标
     */
    private String icon;

    /**
     * 组件路径
     */
    private String component;

    /**
     * redirect地址
     */
    private String redirect;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 层级
     */
    private Integer deep;

    /**
     * 菜单类型 （字典表menu_type）
     */
    private String menuTypeCd;

    /**
     * 按钮权限
     */
    private String permissions;

    /**
     * 是否隐藏
     */
    private String isHidden;

    /**
     * 是否有子级
     */
    private String hasChildren;

    /**
     * 路由外链时填写的访问地址
     */
    private String isLink;

    /**
     * 菜单是否全屏
     */
    private String isFull;

    /**
     * 菜单是否固定在标签页
     */
    private String isAffix;

    /**
     * 当前路由是否缓存
     */
    private String isKeepAlive;
}
