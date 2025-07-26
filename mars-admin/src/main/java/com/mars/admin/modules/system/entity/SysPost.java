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

/**
 * 系统岗位实体类
 *
 * @author Mars
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(value = "sys_post", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "系统岗位")
public class SysPost extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description = "岗位ID")
    private Long id;

    @Schema(description = "岗位编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("post_code")
    private String postCode;

    @Schema(description = "岗位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("post_name")
    private String postName;

    @Schema(description = "显示顺序")
    @Column("post_sort")
    private Integer postSort;

    @Schema(description = "状态：0-停用，1-正常")
    @Column("status")
    private Integer status;

    @Schema(description = "是否系统岗位：0-否，1-是(不可删除)")
    @Column("is_system")
    private Integer isSystem;

    @Schema(description = "备注")
    @Column("remark")
    private String remark;
}
