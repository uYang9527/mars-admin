package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.vo.SysPostVo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-19T19:52:38+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Microsoft)"
)
@Component
public class SysPostToSysPostVoMapperImpl implements SysPostToSysPostVoMapper {

    @Override
    public SysPostVo convert(SysPost arg0) {
        if ( arg0 == null ) {
            return null;
        }

        SysPostVo sysPostVo = new SysPostVo();

        sysPostVo.setPostId( arg0.getPostId() );
        sysPostVo.setDeptId( arg0.getDeptId() );
        sysPostVo.setPostCode( arg0.getPostCode() );
        sysPostVo.setPostName( arg0.getPostName() );
        sysPostVo.setPostCategory( arg0.getPostCategory() );
        sysPostVo.setPostSort( arg0.getPostSort() );
        sysPostVo.setStatus( arg0.getStatus() );
        sysPostVo.setRemark( arg0.getRemark() );
        sysPostVo.setCreateTime( arg0.getCreateTime() );

        return sysPostVo;
    }

    @Override
    public SysPostVo convert(SysPost arg0, SysPostVo arg1) {
        if ( arg0 == null ) {
            return arg1;
        }

        arg1.setPostId( arg0.getPostId() );
        arg1.setDeptId( arg0.getDeptId() );
        arg1.setPostCode( arg0.getPostCode() );
        arg1.setPostName( arg0.getPostName() );
        arg1.setPostCategory( arg0.getPostCategory() );
        arg1.setPostSort( arg0.getPostSort() );
        arg1.setStatus( arg0.getStatus() );
        arg1.setRemark( arg0.getRemark() );
        arg1.setCreateTime( arg0.getCreateTime() );

        return arg1;
    }
}
