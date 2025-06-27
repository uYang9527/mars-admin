package com.mars.admin.entity;

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
 * 系统配置实体类
 *
 * @author Mars
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(value = "sys_config", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "系统配置")
public class SysConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description = "配置ID")
    private Long id;

    @Schema(description = "参数名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("config_name")
    private String configName;

    @Schema(description = "参数键名", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("config_key")
    private String configKey;

    @Schema(description = "参数键值", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("config_value")
    private String configValue;

    @Schema(description = "系统内置：Y-是，N-否")
    @Column("config_type")
    private String configType;

    @Schema(description = "备注")
    @Column("remark")
    private String remark;
} 