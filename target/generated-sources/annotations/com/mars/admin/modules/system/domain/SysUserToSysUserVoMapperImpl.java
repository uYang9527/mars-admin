package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.vo.SysUserVo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-19T19:52:38+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Microsoft)"
)
@Component
public class SysUserToSysUserVoMapperImpl implements SysUserToSysUserVoMapper {

    @Override
    public SysUserVo convert(SysUser arg0) {
        if ( arg0 == null ) {
            return null;
        }

        SysUserVo sysUserVo = new SysUserVo();

        sysUserVo.setUserId( arg0.getUserId() );
        sysUserVo.setTenantId( arg0.getTenantId() );
        sysUserVo.setDeptId( arg0.getDeptId() );
        sysUserVo.setUserName( arg0.getUserName() );
        sysUserVo.setNickName( arg0.getNickName() );
        sysUserVo.setUserType( arg0.getUserType() );
        sysUserVo.setEmail( arg0.getEmail() );
        sysUserVo.setPhonenumber( arg0.getPhonenumber() );
        sysUserVo.setSex( arg0.getSex() );
        sysUserVo.setAvatar( arg0.getAvatar() );
        sysUserVo.setPassword( arg0.getPassword() );
        sysUserVo.setStatus( arg0.getStatus() );
        sysUserVo.setLoginIp( arg0.getLoginIp() );
        sysUserVo.setLoginDate( arg0.getLoginDate() );
        sysUserVo.setRemark( arg0.getRemark() );
        sysUserVo.setCreateTime( arg0.getCreateTime() );

        return sysUserVo;
    }

    @Override
    public SysUserVo convert(SysUser arg0, SysUserVo arg1) {
        if ( arg0 == null ) {
            return arg1;
        }

        arg1.setUserId( arg0.getUserId() );
        arg1.setTenantId( arg0.getTenantId() );
        arg1.setDeptId( arg0.getDeptId() );
        arg1.setUserName( arg0.getUserName() );
        arg1.setNickName( arg0.getNickName() );
        arg1.setUserType( arg0.getUserType() );
        arg1.setEmail( arg0.getEmail() );
        arg1.setPhonenumber( arg0.getPhonenumber() );
        arg1.setSex( arg0.getSex() );
        arg1.setAvatar( arg0.getAvatar() );
        arg1.setPassword( arg0.getPassword() );
        arg1.setStatus( arg0.getStatus() );
        arg1.setLoginIp( arg0.getLoginIp() );
        arg1.setLoginDate( arg0.getLoginDate() );
        arg1.setRemark( arg0.getRemark() );
        arg1.setCreateTime( arg0.getCreateTime() );

        return arg1;
    }
}
