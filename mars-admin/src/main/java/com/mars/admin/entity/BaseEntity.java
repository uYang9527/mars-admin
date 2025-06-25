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
 * 基础实体类
 * 包含公共字段：创建时间、更新时间、创建人、更新人、删除标记、删除时间
 *
 * @author Mars.wq [wqexpore@163.com]
 * @version 1.0
 * @date 2025-01-27
 */
@Data
@Accessors(chain = true)
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2024-01-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(onInsertValue = "now()")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", example = "2024-01-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(onInsertValue = "now()", onUpdateValue = "now()")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @Schema(description = "创建人", example = "1")
    @Column
    private Long createUser;

    /**
     * 更新人
     */
    @Schema(description = "更新人", example = "1")
    @Column
    private Long updateUser;

    /**
     * 是否删除
     */
    @Schema(description = "是否删除", example = "false", hidden = true)
    @Column(isLogicDelete = true)
    private Boolean isDelete;

    /**
     * 删除时间
     */
    @Schema(description = "删除时间", example = "2024-01-01 12:00:00", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deleteTime;
} 