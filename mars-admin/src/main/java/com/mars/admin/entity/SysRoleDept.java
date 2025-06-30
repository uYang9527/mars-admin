package com.mars.admin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色部门关联实体类
 *
 * @author Mars
 */
@Data
@Accessors(chain = true)
@Table("sys_role_dept")
@Schema(description = "角色部门关联")
public class SysRoleDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("role_id")
    private Long roleId;

    @Schema(description = "部门ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("dept_id")
    private Long deptId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(value = "create_time", onInsertValue = "now()")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    @Column("create_by")
    private Long createBy;
} 