package com.mars.admin.modules.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户岗位关联实体类
 *
 * @author Mars
 */
@Data
@Accessors(chain = true)
@Table("sys_user_post")
@Schema(description = "用户岗位关联")
public class SysUserPost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("user_id")
    private Long userId;

    @Schema(description = "岗位ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("post_id")
    private Long postId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(value = "create_time", onInsertValue = "now()")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    @Column("create_by")
    private Long createBy;
}
