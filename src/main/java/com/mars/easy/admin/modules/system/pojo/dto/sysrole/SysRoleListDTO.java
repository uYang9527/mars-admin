package com.mars.easy.admin.modules.system.pojo.dto.sysrole;

import com.mars.easy.admin.framework.entity.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author 程序员Mars
 * @date 2023/8/24 15:28
 */
@Data
@Schema(description = "角色查询")
public class SysRoleListDTO extends PageQuery {

    @Schema(description =  "角色名称")
    private String roleName;

}
