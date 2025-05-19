package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.vo.SysTenantPackageVo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-19T19:52:38+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Microsoft)"
)
@Component
public class SysTenantPackageToSysTenantPackageVoMapperImpl implements SysTenantPackageToSysTenantPackageVoMapper {

    @Override
    public SysTenantPackageVo convert(SysTenantPackage arg0) {
        if ( arg0 == null ) {
            return null;
        }

        SysTenantPackageVo sysTenantPackageVo = new SysTenantPackageVo();

        sysTenantPackageVo.setPackageId( arg0.getPackageId() );
        sysTenantPackageVo.setPackageName( arg0.getPackageName() );
        sysTenantPackageVo.setMenuIds( arg0.getMenuIds() );
        sysTenantPackageVo.setRemark( arg0.getRemark() );
        sysTenantPackageVo.setMenuCheckStrictly( arg0.getMenuCheckStrictly() );
        sysTenantPackageVo.setStatus( arg0.getStatus() );

        return sysTenantPackageVo;
    }

    @Override
    public SysTenantPackageVo convert(SysTenantPackage arg0, SysTenantPackageVo arg1) {
        if ( arg0 == null ) {
            return arg1;
        }

        arg1.setPackageId( arg0.getPackageId() );
        arg1.setPackageName( arg0.getPackageName() );
        arg1.setMenuIds( arg0.getMenuIds() );
        arg1.setRemark( arg0.getRemark() );
        arg1.setMenuCheckStrictly( arg0.getMenuCheckStrictly() );
        arg1.setStatus( arg0.getStatus() );

        return arg1;
    }
}
