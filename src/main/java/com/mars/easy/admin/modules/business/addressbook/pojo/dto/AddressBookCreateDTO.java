package com.mars.easy.admin.modules.business.addressbook.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * AddressBook添加DTO
 *
 * @author Mars
 * @since 2024-08-22
 */
@Data
@Schema(description = "AddressBook添加DTO")
public class AddressBookCreateDTO {

   @Schema(description =  "用户id")
   private Integer userId;

   @Schema(description =  "收货人")
   private String consignee;

   @Schema(description =  "性别 0 女 1 男")
   private Integer sex;

   @Schema(description =  "手机号")
   private String phone;

   @Schema(description =  "省级区划编号")
   private String provinceCode;

   @Schema(description =  "省级名称")
   private String provinceName;

   @Schema(description =  "市级区划编号")
   private String cityCode;

   @Schema(description =  "市级名称")
   private String cityName;

   @Schema(description =  "区级区划编号")
   private String districtCode;

   @Schema(description =  "区级名称")
   private String districtName;

   @Schema(description =  "详细地址")
   private String detail;

   @Schema(description =  "标签")
   private String label;

   @Schema(description =  "默认 0 否 1是")
   private Integer isDefault;

   @Schema(description =  "创建人")
   private Integer createUser;

   @Schema(description =  "修改人")
   private Integer updateUser;

   @Schema(description =  "是否删除")
   private Integer isDeleted;

}
