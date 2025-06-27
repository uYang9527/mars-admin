package com.mars.admin.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 系统字典类型实体类
 *
 * @author Mars
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table("sys_dict_type")
@Schema(description = "系统字典类型")
public class SysDictType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description = "字典主键")
    private Long id;

    @Schema(description = "字典名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("dict_name")
    private String dictName;

    @Schema(description = "字典类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("dict_type")
    private String dictType;

    @Schema(description = "状态：0-停用，1-正常")
    @Column("status")
    private Integer status;

    @Schema(description = "备注")
    @Column("remark")
    private String remark;

    // 非数据库字段
    @Schema(description = "字典数据列表")
    @Column(ignore = true)
    private List<SysDictData> dictDataList;
} 