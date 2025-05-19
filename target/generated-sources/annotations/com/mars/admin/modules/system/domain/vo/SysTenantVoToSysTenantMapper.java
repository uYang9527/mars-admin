package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysTenant;
import com.mars.admin.modules.system.domain.SysTenantToSysTenantVoMapper;
import io.github.linpeilie.AutoMapperConfig__0;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__0.class,
    uses = {SysTenantToSysTenantVoMapper.class},
    imports = {}
)
public interface SysTenantVoToSysTenantMapper extends BaseMapper<SysTenantVo, SysTenant> {
}
