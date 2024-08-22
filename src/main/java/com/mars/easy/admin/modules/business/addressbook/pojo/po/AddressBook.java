package com.mars.easy.admin.modules.business.addressbook.pojo.po;

import com.mybatisflex.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.io.Serial;
import com.mars.easy.admin.framework.datascope.EntityChangeListener;
import java.time.LocalDateTime;

/**
* 地址管理
*
* @author Mars
* @since 2024-08-22
*/
@Data
@Table(value = "address_book")
@Schema(description = "地址管理")
public class AddressBook implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Schema(description ="主键")
    private Long id;

    @Schema(description ="用户id")
    private Integer userId;

    @Schema(description ="收货人")
    private String consignee;

    @Schema(description ="性别 0 女 1 男")
    private Integer sex;

    @Schema(description ="手机号")
    private String phone;

    @Schema(description ="省级区划编号")
    private String provinceCode;

    @Schema(description ="省级名称")
    private String provinceName;

    @Schema(description ="市级区划编号")
    private String cityCode;

    @Schema(description ="市级名称")
    private String cityName;

    @Schema(description ="区级区划编号")
    private String districtCode;

    @Schema(description ="区级名称")
    private String districtName;

    @Schema(description ="详细地址")
    private String detail;

    @Schema(description ="标签")
    private String label;

    @Schema(description ="默认 0 否 1是")
    private Integer isDefault;

    @Schema(description ="创建时间")
    private LocalDateTime createTime;

    @Schema(description ="更新时间")
    private LocalDateTime updateTime;

    @Schema(description ="创建人")
    private Integer createUser;

    @Schema(description ="修改人")
    private Integer updateUser;

    @Schema(description ="是否删除")
    private Integer isDeleted;

}
