package com.mars.easy.admin.modules.system.pojo.vo.sysmenu;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author 程序员Mars
 * @date 2023/9/1 15:32
 */
@Data
public class MenuPermissionVO {

    @Schema(description ="权限标识数量")
    private int permissionCount;

}
