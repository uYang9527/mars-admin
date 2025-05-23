package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysTenantPackageBoToSysTenantPackageMapper;
import com.mars.admin.modules.system.domain.vo.SysTenantPackageVo;
import com.mars.admin.modules.system.domain.vo.SysTenantPackageVoToSysTenantPackageMapper;
import io.github.linpeilie.AutoMapperConfig__20;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__20.class,
    uses = {SysTenantPackageBoToSysTenantPackageMapper.class,SysTenantPackageVoToSysTenantPackageMapper.class},
    imports = {}
)
public interface SysTenantPackageToSysTenantPackageVoMapper extends BaseMapper<SysTenantPackage, SysTenantPackageVo> {
}
