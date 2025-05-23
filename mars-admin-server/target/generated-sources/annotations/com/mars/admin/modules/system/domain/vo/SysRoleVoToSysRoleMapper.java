package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysRole;
import com.mars.admin.modules.system.domain.SysRoleToSysRoleVoMapper;
import io.github.linpeilie.AutoMapperConfig__20;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__20.class,
    uses = {SysRoleToSysRoleVoMapper.class},
    imports = {}
)
public interface SysRoleVoToSysRoleMapper extends BaseMapper<SysRoleVo, SysRole> {
}
