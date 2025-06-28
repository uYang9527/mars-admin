package com.mars.admin.entity;

import com.mars.admin.framework.listener.EntityChangeListener;
import com.mybatisflex.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * OSS配置实体类
 *
 * @author Mars.wq
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(value = "sys_oss_config", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "OSS配置")
public class SysOssConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description = "配置ID")
    private Long id;

    @Schema(description = "配置key", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("config_key")
    private String configKey;

    @Schema(description = "accessKey")
    @Column("access_key")
    private String accessKey;

    @Schema(description = "secretKey")
    @Column("secret_key")
    private String secretKey;

    @Schema(description = "桶名称")
    @Column("bucket_name")
    private String bucketName;

    @Schema(description = "前缀")
    @Column("prefix")
    private String prefix;

    @Schema(description = "访问站点")
    @Column("endpoint")
    private String endpoint;

    @Schema(description = "自定义域名")
    @Column("domain")
    private String domain;

    @Schema(description = "是否https：0-否，1-是")
    @Column("is_https")
    private Integer isHttps;

    @Schema(description = "区域")
    @Column("region")
    private String region;

    @Schema(description = "桶权限类型：0-private，1-public，2-custom")
    @Column("access_policy")
    private Integer accessPolicy;

    @Schema(description = "状态：0-停用，1-启用")
    @Column("status")
    private Integer status;

    @Schema(description = "扩展字段1")
    @Column("ext1")
    private String ext1;

    @Schema(description = "备注")
    @Column("remark")
    private String remark;
} 