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
 * 系统部门实体类
 *
 * @author Mars
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table("sys_dept")
@Schema(description = "系统部门")
public class SysDept extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description = "部门ID")
    private Long id;

    @Schema(description = "父部门ID，0为根部门")
    @Column("parent_id")
    private Long parentId;

    @Schema(description = "祖级列表")
    @Column("ancestors")
    private String ancestors;

    @Schema(description = "部门名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("dept_name")
    private String deptName;

    @Schema(description = "部门编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("dept_code")
    private String deptCode;

    @Schema(description = "显示顺序")
    @Column("order_num")
    private Integer orderNum;

    @Schema(description = "负责人")
    @Column("leader")
    private String leader;

    @Schema(description = "联系电话")
    @Column("phone")
    private String phone;

    @Schema(description = "邮箱")
    @Column("email")
    private String email;

    @Schema(description = "状态：0-停用，1-正常")
    @Column("status")
    private Integer status;

    @Schema(description = "是否系统部门：0-否，1-是(不可删除)")
    @Column("is_system")
    private Integer isSystem;

    @Schema(description = "备注")
    @Column("remark")
    private String remark;

    // 非数据库字段
    @Schema(description = "子部门列表")
    @Column(ignore = true)
    private List<SysDept> children;

    @Schema(description = "父部门名称")
    @Column(ignore = true)
    private String parentName;
} 