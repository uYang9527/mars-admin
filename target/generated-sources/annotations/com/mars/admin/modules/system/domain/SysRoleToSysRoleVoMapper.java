package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysRoleBoToSysRoleMapper;
import com.mars.admin.modules.system.domain.vo.SysRoleVo;
import com.mars.admin.modules.system.domain.vo.SysRoleVoToSysRoleMapper;
import io.github.linpeilie.AutoMapperConfig__19;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__19.class,
    uses = {SysRoleBoToSysRoleMapper.class,SysRoleVoToSysRoleMapper.class},
    imports = {}
)
public interface SysRoleToSysRoleVoMapper extends BaseMapper<SysRole, SysRoleVo> {
}
