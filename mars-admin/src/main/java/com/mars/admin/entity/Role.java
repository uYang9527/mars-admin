package com.mars.admin.entity;

import com.mars.admin.framework.listener.EntityChangeListener;
import com.mybatisflex.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 角色实体类
 * 继承BaseEntity，自动拥有公共字段和自动填充功能
 *
 * @author Mars.wq [wqexpore@163.com]
 * @version 1.0
 * @date 2025-01-27
 */
@Data
@Schema(name = "角色实体", description = "角色信息")
@Accessors(chain = true)
@Table(value = "tb_role", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {

    /**
     * 角色ID
     */
    @Schema(description = "角色ID", example = "1")
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称", example = "管理员", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column
    private String roleName;

    /**
     * 角色代码
     */
    @Schema(description = "角色代码", example = "ADMIN", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column
    private String roleCode;

    /**
     * 角色描述
     */
    @Schema(description = "角色描述", example = "系统管理员角色")
    @Column
    private String description;

    /**
     * 角色状态
     */
    @Schema(description = "角色状态", example = "1", allowableValues = {"0", "1"})
    @Column
    private Integer status;
} 