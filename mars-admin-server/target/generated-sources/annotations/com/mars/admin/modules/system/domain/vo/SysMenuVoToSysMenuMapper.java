package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysMenu;
import com.mars.admin.modules.system.domain.SysMenuToSysMenuVoMapper;
import io.github.linpeilie.AutoMapperConfig__20;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__20.class,
    uses = {SysMenuToSysMenuVoMapper.class,SysMenuToSysMenuVoMapper.class},
    imports = {}
)
public interface SysMenuVoToSysMenuMapper extends BaseMapper<SysMenuVo, SysMenu> {
}
