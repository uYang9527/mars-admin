package com.mars.admin.modules.web.domain.vo;

import com.mars.admin.modules.system.domain.vo.SysTenantVo;
import com.mars.admin.modules.system.domain.vo.SysTenantVoToTenantListVoMapper;
import io.github.linpeilie.AutoMapperConfig__25;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__25.class,
    uses = {SysTenantVoToTenantListVoMapper.class},
    imports = {}
)
public interface TenantListVoToSysTenantVoMapper extends BaseMapper<TenantListVo, SysTenantVo> {
}
