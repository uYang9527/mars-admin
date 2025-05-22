package com.mars.admin.modules.system.domain.bo;

import lombok.Data;

/**
 * SysMenuQueryDTO
 *
 * @Author 程序员Mars
 * @since 2023/8/21
 */
@Data
public class SysMenuListBo {

   private boolean isShowButton;

    {
        isShowButton = true;
    }
}
