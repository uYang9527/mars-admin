package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysTenantBoToSysTenantMapper;
import com.mars.admin.modules.system.domain.vo.SysTenantVo;
import com.mars.admin.modules.system.domain.vo.SysTenantVoToSysTenantMapper;
import io.github.linpeilie.AutoMapperConfig__0;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__0.class,
    uses = {SysTenantVoToSysTenantMapper.class,SysTenantBoToSysTenantMapper.class},
    imports = {}
)
public interface SysTenantToSysTenantVoMapper extends BaseMapper<SysTenant, SysTenantVo> {
}
