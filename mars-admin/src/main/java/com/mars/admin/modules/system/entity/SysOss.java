package com.mars.admin.modules.system.entity;

import com.mars.admin.modules.base.entity.BaseEntity;
import com.mars.admin.framework.listener.EntityChangeListener;
import com.mybatisflex.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 对象存储实体类
 *
 * @author Mars.wq
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(value = "sys_oss", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "对象存储")
public class SysOss extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description = "对象存储主键")
    private Long id;

    @Schema(description = "文件名", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("file_name")
    private String fileName;

    @Schema(description = "原名", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("original_name")
    private String originalName;

    @Schema(description = "文件后缀名", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("file_suffix")
    private String fileSuffix;

    @Schema(description = "文件地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("url")
    private String url;

    @Schema(description = "文件大小")
    @Column("size")
    private Long size;

    @Schema(description = "配置key", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("config_key")
    private String configKey;

    @Schema(description = "文件路径")
    @Column("file_path")
    private String filePath;

    @Schema(description = "文件类型")
    @Column("content_type")
    private String contentType;

    @Schema(description = "上传ID(用于分片上传)")
    @Column("upload_id")
    private String uploadId;

    @Schema(description = "上传状态：0-失败，1-成功")
    @Column("upload_status")
    private Integer uploadStatus;

    @Schema(description = "备注")
    @Column("remark")
    private String remark;
}
