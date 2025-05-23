package com.mars.admin.modules.system.domain.bo;

import com.mars.admin.modules.system.domain.SysMenu;
import io.github.linpeilie.AutoMapperConfig__20;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__20.class,
    uses = {},
    imports = {}
)
public interface SysMenuBoToSysMenuMapper extends BaseMapper<SysMenuBo, SysMenu> {
}
