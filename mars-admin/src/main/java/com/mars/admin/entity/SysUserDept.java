package com.mars.admin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mars.admin.framework.listener.EntityChangeListener;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 代码领取微信公众号【程序员Mars】
 *
 * @className: SysUserDept
 * @author: Mars
 * @date: 2025/6/28 23:13
 */
@Data
@Accessors(chain = true)
@Table(value = "sys_user_dept")
@Schema(description = "用户部门关联表")
public class SysUserDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("user_id")
    private Long userId;

    @Schema(description = "部门ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("dept_id")
    private Long deptId;

    @Schema(description = "是否主部门：0-否，1-是", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("is_main")
    private Integer isMain;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(value = "create_time", onInsertValue = "now()")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    @Column("create_by")
    private Long createBy;
}
