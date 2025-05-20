package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysMenuBoToSysMenuMapper;
import com.mars.admin.modules.system.domain.vo.SysMenuVo;
import com.mars.admin.modules.system.domain.vo.SysMenuVoToSysMenuMapper;
import io.github.linpeilie.AutoMapperConfig__18;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__18.class,
    uses = {SysMenuBoToSysMenuMapper.class,SysMenuVoToSysMenuMapper.class},
    imports = {}
)
public interface SysMenuToSysMenuVoMapper extends BaseMapper<SysMenu, SysMenuVo> {
}
