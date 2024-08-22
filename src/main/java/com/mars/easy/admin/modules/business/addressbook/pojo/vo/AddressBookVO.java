package com.mars.easy.admin.modules.business.addressbook.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * AddressBook返回vo
 *
 * @author Mars
 * @since 2024-08-22
 */
@Data
@Schema(description = "AddressBook返回vo")
public class AddressBookVO {

    @ExcelIgnore
    @Schema(description =  "主键")
    private Long id;

    @ExcelProperty(value = "用户id")
    @Schema(description =  "用户id")
    private Integer userId;

    @ExcelProperty(value = "收货人")
    @Schema(description =  "收货人")
    private String consignee;

    @ExcelProperty(value = "性别 0 女 1 男")
    @Schema(description =  "性别 0 女 1 男")
    private Integer sex;

    @ExcelProperty(value = "手机号")
    @Schema(description =  "手机号")
    private String phone;

    @ExcelProperty(value = "省级区划编号")
    @Schema(description =  "省级区划编号")
    private String provinceCode;

    @ExcelProperty(value = "省级名称")
    @Schema(description =  "省级名称")
    private String provinceName;

    @ExcelProperty(value = "市级区划编号")
    @Schema(description =  "市级区划编号")
    private String cityCode;

    @ExcelProperty(value = "市级名称")
    @Schema(description =  "市级名称")
    private String cityName;

    @ExcelProperty(value = "区级区划编号")
    @Schema(description =  "区级区划编号")
    private String districtCode;

    @ExcelProperty(value = "区级名称")
    @Schema(description =  "区级名称")
    private String districtName;

    @ExcelProperty(value = "详细地址")
    @Schema(description =  "详细地址")
    private String detail;

    @ExcelProperty(value = "标签")
    @Schema(description =  "标签")
    private String label;

    @ExcelProperty(value = "默认 0 否 1是")
    @Schema(description =  "默认 0 否 1是")
    private Integer isDefault;

    @ExcelProperty(value = "创建人")
    @Schema(description =  "创建人")
    private Integer createUser;

    @ExcelProperty(value = "修改人")
    @Schema(description =  "修改人")
    private Integer updateUser;

    @ExcelProperty(value = "是否删除")
    @Schema(description =  "是否删除")
    private Integer isDeleted;

}
