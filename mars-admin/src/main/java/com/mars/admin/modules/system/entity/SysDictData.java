package com.mars.admin.modules.system.entity;

import com.mars.admin.modules.base.entity.BaseEntity;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 系统字典数据实体类
 *
 * @author Mars
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table("sys_dict_data")
@Schema(description = "系统字典数据")
public class SysDictData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description = "字典编码")
    private Long id;

    @Schema(description = "字典排序")
    @Column("dict_sort")
    private Integer dictSort;

    @Schema(description = "字典标签", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("dict_label")
    private String dictLabel;

    @Schema(description = "字典键值", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("dict_value")
    private String dictValue;

    @Schema(description = "字典类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("dict_type")
    private String dictType;

    @Schema(description = "样式属性")
    @Column("css_class")
    private String cssClass;

    @Schema(description = "表格回显样式")
    @Column("list_class")
    private String listClass;

    @Schema(description = "是否默认：Y-是，N-否")
    @Column("is_default")
    private String isDefault;

    @Schema(description = "状态：0-停用，1-正常")
    @Column("status")
    private Integer status;

    @Schema(description = "备注")
    @Column("remark")
    private String remark;
}
