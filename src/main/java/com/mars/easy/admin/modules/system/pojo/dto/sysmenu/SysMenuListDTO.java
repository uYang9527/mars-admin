package com.mars.easy.admin.modules.system.pojo.dto.sysmenu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * SysMenuQueryDTO
 *
 * @Author 程序员Mars
 * @since 2023/8/21
 */
@Data
public class SysMenuListDTO {

    @Schema(description = "是否查询按钮")
   private boolean isShowButton;

    {
        isShowButton = true;
    }
}
