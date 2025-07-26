package com.mars.admin.modules.system.mapper;

import com.mars.admin.modules.base.mapper.BasePlusMapper;
import com.mars.admin.modules.system.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色关联Mapper接口
 *
 * @author Mars
 */
@Mapper
public interface SysUserRoleMapper extends BasePlusMapper<SysUserRole> {
}
