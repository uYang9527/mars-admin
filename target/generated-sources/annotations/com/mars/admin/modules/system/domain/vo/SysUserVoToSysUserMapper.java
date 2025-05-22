package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysRoleToSysRoleVoMapper;
import com.mars.admin.modules.system.domain.SysUser;
import com.mars.admin.modules.system.domain.SysUserToSysUserVoMapper;
import io.github.linpeilie.AutoMapperConfig__19;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__19.class,
    uses = {SysRoleVoToSysRoleMapper.class,SysRoleToSysRoleVoMapper.class,SysUserToSysUserVoMapper.class},
    imports = {}
)
public interface SysUserVoToSysUserMapper extends BaseMapper<SysUserVo, SysUser> {
}
