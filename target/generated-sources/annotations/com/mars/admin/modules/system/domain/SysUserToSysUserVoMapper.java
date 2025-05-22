package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysUserBoToSysUserMapper;
import com.mars.admin.modules.system.domain.vo.SysRoleVoToSysRoleMapper;
import com.mars.admin.modules.system.domain.vo.SysUserVo;
import com.mars.admin.modules.system.domain.vo.SysUserVoToSysUserMapper;
import io.github.linpeilie.AutoMapperConfig__21;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__21.class,
    uses = {SysRoleVoToSysRoleMapper.class,SysRoleToSysRoleVoMapper.class,SysUserVoToSysUserMapper.class,SysUserBoToSysUserMapper.class},
    imports = {}
)
public interface SysUserToSysUserVoMapper extends BaseMapper<SysUser, SysUserVo> {
}
