package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.framework.core.service.Treeable;
import lombok.Data;

import java.util.List;

/**
 * SysMenuVO
 *
 * @Author 程序员Mars
 * @since 2023/8/21
 */
@Data
public class MenuTreeVo implements Treeable<MenuTreeVo> {

    private Object id;

    private Object pid;

    private Long deep;

    private Long sort;

    private String title;

    private String menuTypeCd;

    private String permissions;

    private List<MenuTreeVo> children;
}
