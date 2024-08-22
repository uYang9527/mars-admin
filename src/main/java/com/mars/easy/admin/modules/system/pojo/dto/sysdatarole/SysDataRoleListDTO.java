package com.mars.easy.admin.modules.system.pojo.dto.sysdatarole;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.mars.easy.admin.framework.entity.PageQuery;

/**
 * <p>
 * SysDataRole查询DTO
 * </p>
 *
 * @Author 程序员Mars-admin
 * @since 2024-07-09
 */
@Data
@Schema(description = "SysDataRole查询DTO")
public class SysDataRoleListDTO extends PageQuery {

    @Schema(description =  "角色名称")
    private String roleName;

    @Schema(description =  "是否锁定")
    private String isLock;


}
