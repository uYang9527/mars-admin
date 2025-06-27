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
 * 用户角色关联实体类
 *
 * @author Mars
 */
@Data
@Accessors(chain = true)
@Table("sys_user_role")
@Schema(description = "用户角色关联")
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("user_id")
    private Long userId;

    @Schema(description = "角色ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("role_id")
    private Long roleId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(value = "create_time", onInsertValue = "now()")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    @Column("create_by")
    private Long createBy;
} 