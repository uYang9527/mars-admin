package com.mars.admin.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 系统菜单实体类
 *
 * @author Mars
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table("sys_menu")
@Schema(description = "系统菜单")
public class SysMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description = "菜单ID")
    private Long id;

    @Schema(description = "父菜单ID，0为根菜单")
    @Column("parent_id")
    private Long parentId;

    @Schema(description = "菜单名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("menu_name")
    private String menuName;

    @Schema(description = "菜单编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("menu_code")
    private String menuCode;

    @Schema(description = "菜单类型：M-目录，C-菜单，F-按钮", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("menu_type")
    private String menuType;

    @Schema(description = "路由名称")
    @Column("route_name")
    private String routeName;

    @Schema(description = "路由地址")
    @Column("route_path")
    private String routePath;

    @Schema(description = "组件路径")
    @Column("component")
    private String component;

    @Schema(description = "重定向地址")
    @Column("redirect")
    private String redirect;

    @Schema(description = "路由参数")
    @Column("query_param")
    private String queryParam;

    @Schema(description = "是否为外链：0-是，1-否")
    @Column("is_frame")
    private Integer isFrame;

    @Schema(description = "是否缓存：0-缓存，1-不缓存")
    @Column("is_cache")
    private Integer isCache;

    @Schema(description = "菜单类型标识")
    @Column("menu_type_flag")
    private String menuTypeFlag;

    @Schema(description = "菜单状态：0-隐藏，1-显示")
    @Column("visible")
    private Integer visible;

    @Schema(description = "菜单状态：0-停用，1-正常")
    @Column("status")
    private Integer status;

    @Schema(description = "权限标识")
    @Column("perms")
    private String perms;

    @Schema(description = "菜单图标")
    @Column("icon")
    private String icon;

    @Schema(description = "显示顺序")
    @Column("order_num")
    private Integer orderNum;

    @Schema(description = "是否系统菜单：0-否，1-是(不可删除)")
    @Column("is_system")
    private Integer isSystem;

    @Schema(description = "备注")
    @Column("remark")
    private String remark;

    // 非数据库字段
    @Schema(description = "子菜单列表")
    @Column(ignore = true)
    private List<SysMenu> children;

    @Schema(description = "父菜单名称")
    @Column(ignore = true)
    private String parentName;
} 