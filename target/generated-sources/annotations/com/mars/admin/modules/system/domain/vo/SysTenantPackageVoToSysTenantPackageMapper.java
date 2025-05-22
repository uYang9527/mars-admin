package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysTenantPackage;
import com.mars.admin.modules.system.domain.SysTenantPackageToSysTenantPackageVoMapper;
import io.github.linpeilie.AutoMapperConfig__19;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__19.class,
    uses = {SysTenantPackageToSysTenantPackageVoMapper.class},
    imports = {}
)
public interface SysTenantPackageVoToSysTenantPackageMapper extends BaseMapper<SysTenantPackageVo, SysTenantPackage> {
}
