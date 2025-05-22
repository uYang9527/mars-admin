package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysTenantBoToSysTenantMapper;
import com.mars.admin.modules.system.domain.vo.SysTenantVo;
import com.mars.admin.modules.system.domain.vo.SysTenantVoToSysTenantMapper;
import com.mars.admin.modules.system.domain.vo.SysTenantVoToTenantListVoMapper;
import com.mars.admin.modules.web.domain.vo.TenantListVoToSysTenantVoMapper;
import io.github.linpeilie.AutoMapperConfig__21;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__21.class,
    uses = {TenantListVoToSysTenantVoMapper.class,SysTenantVoToTenantListVoMapper.class,SysTenantVoToSysTenantMapper.class,SysTenantBoToSysTenantMapper.class},
    imports = {}
)
public interface SysTenantToSysTenantVoMapper extends BaseMapper<SysTenant, SysTenantVo> {
}
