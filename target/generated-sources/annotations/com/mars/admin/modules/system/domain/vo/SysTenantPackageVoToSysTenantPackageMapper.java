package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysTenantPackage;
import com.mars.admin.modules.system.domain.SysTenantPackageToSysTenantPackageVoMapper;
import io.github.linpeilie.AutoMapperConfig__14;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__14.class,
    uses = {SysTenantPackageToSysTenantPackageVoMapper.class},
    imports = {}
)
public interface SysTenantPackageVoToSysTenantPackageMapper extends BaseMapper<SysTenantPackageVo, SysTenantPackage> {
}
