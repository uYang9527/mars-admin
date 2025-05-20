package com.mars.admin.modules.web.domain.vo;

import com.mars.admin.modules.system.domain.vo.SysTenantVo;
import com.mars.admin.modules.system.domain.vo.SysTenantVoToTenantListVoMapper;
import io.github.linpeilie.AutoMapperConfig__18;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__18.class,
    uses = {SysTenantVoToTenantListVoMapper.class},
    imports = {}
)
public interface TenantListVoToSysTenantVoMapper extends BaseMapper<TenantListVo, SysTenantVo> {
}
