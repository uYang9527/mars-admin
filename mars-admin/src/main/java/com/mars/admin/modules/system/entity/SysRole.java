package com.mars.admin.modules.system.entity;

import com.mars.admin.modules.base.entity.BaseEntity;
import com.mars.admin.framework.listener.EntityChangeListener;
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
 * 系统角色实体类
 *
 * @author Mars
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(value = "sys_role", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "系统角色")
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "角色名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("role_name")
    private String roleName;

    @Schema(description = "角色编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("role_code")
    private String roleCode;

    @Schema(description = "角色权限字符串", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("role_key")
    private String roleKey;

    @Schema(description = "显示顺序")
    @Column("role_sort")
    private Integer roleSort;

    @Schema(description = "数据范围：1-全部，2-自定义，3-本部门，4-本部门及以下，5-仅本人")
    @Column("data_scope")
    private Integer dataScope;

    @Schema(description = "菜单树选择项是否关联显示")
    @Column("menu_check_strictly")
    private Integer menuCheckStrictly;

    @Schema(description = "部门树选择项是否关联显示")
    @Column("dept_check_strictly")
    private Integer deptCheckStrictly;

    @Schema(description = "状态：0-禁用，1-启用")
    @Column("status")
    private Integer status;

    @Schema(description = "是否系统角色：0-否，1-是(不可删除)")
    @Column("is_system")
    private Integer isSystem;

    @Schema(description = "角色描述")
    @Column("description")
    private String description;

    @Schema(description = "备注")
    @Column("remark")
    private String remark;

    // 非数据库字段
    @Schema(description = "菜单列表")
    @Column(ignore = true)
    private List<SysMenu> menus;

    @Schema(description = "部门列表")
    @Column(ignore = true)
    private List<SysDept> depts;

    @Schema(description = "菜单ID列表")
    @Column(ignore = true)
    private List<Long> menuIds;

    @Schema(description = "部门ID列表")
    @Column(ignore = true)
    private List<Long> deptIds;
}
