package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysTenantToSysTenantVoMapper;
import com.mars.admin.modules.web.domain.vo.TenantListVo;
import com.mars.admin.modules.web.domain.vo.TenantListVoToSysTenantVoMapper;
import io.github.linpeilie.AutoMapperConfig__18;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__18.class,
    uses = {TenantListVoToSysTenantVoMapper.class,SysTenantVoToSysTenantMapper.class,SysTenantToSysTenantVoMapper.class},
    imports = {}
)
public interface SysTenantVoToTenantListVoMapper extends BaseMapper<SysTenantVo, TenantListVo> {
}
