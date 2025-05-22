package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.vo.SysTenantVo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-22T20:33:13+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Microsoft)"
)
@Component
public class SysTenantToSysTenantVoMapperImpl implements SysTenantToSysTenantVoMapper {

    @Override
    public SysTenantVo convert(SysTenant arg0) {
        if ( arg0 == null ) {
            return null;
        }

        SysTenantVo sysTenantVo = new SysTenantVo();

        sysTenantVo.setId( arg0.getId() );
        sysTenantVo.setTenantId( arg0.getTenantId() );
        sysTenantVo.setContactUserName( arg0.getContactUserName() );
        sysTenantVo.setContactPhone( arg0.getContactPhone() );
        sysTenantVo.setCompanyName( arg0.getCompanyName() );
        sysTenantVo.setLicenseNumber( arg0.getLicenseNumber() );
        sysTenantVo.setAddress( arg0.getAddress() );
        sysTenantVo.setDomain( arg0.getDomain() );
        sysTenantVo.setIntro( arg0.getIntro() );
        sysTenantVo.setRemark( arg0.getRemark() );
        sysTenantVo.setPackageId( arg0.getPackageId() );
        sysTenantVo.setExpireTime( arg0.getExpireTime() );
        sysTenantVo.setAccountCount( arg0.getAccountCount() );
        sysTenantVo.setStatus( arg0.getStatus() );

        return sysTenantVo;
    }

    @Override
    public SysTenantVo convert(SysTenant arg0, SysTenantVo arg1) {
        if ( arg0 == null ) {
            return arg1;
        }

        arg1.setId( arg0.getId() );
        arg1.setTenantId( arg0.getTenantId() );
        arg1.setContactUserName( arg0.getContactUserName() );
        arg1.setContactPhone( arg0.getContactPhone() );
        arg1.setCompanyName( arg0.getCompanyName() );
        arg1.setLicenseNumber( arg0.getLicenseNumber() );
        arg1.setAddress( arg0.getAddress() );
        arg1.setDomain( arg0.getDomain() );
        arg1.setIntro( arg0.getIntro() );
        arg1.setRemark( arg0.getRemark() );
        arg1.setPackageId( arg0.getPackageId() );
        arg1.setExpireTime( arg0.getExpireTime() );
        arg1.setAccountCount( arg0.getAccountCount() );
        arg1.setStatus( arg0.getStatus() );

        return arg1;
    }
}
