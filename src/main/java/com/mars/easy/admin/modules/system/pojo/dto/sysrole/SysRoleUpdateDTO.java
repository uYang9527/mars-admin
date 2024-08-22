package com.mars.easy.admin.modules.system.pojo.dto.sysrole;

import com.mars.easy.admin.framework.valid.annotation.NotZero;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author 程序员Mars
 * @date 2023/8/24 15:28
 */
@Data
@Schema(description = "角色添加")
public class SysRoleUpdateDTO {

    @NotZero
    @Schema(description =  "角色id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description =  "角色名称")
    private String roleName;

    @Schema(description =  "备注")
    private String remark;

    @Schema(description = "标识")
    private String permissions;

}
