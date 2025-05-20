package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysClient;
import com.mars.admin.modules.system.domain.SysClientToSysClientVoMapper;
import io.github.linpeilie.AutoMapperConfig__14;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__14.class,
    uses = {SysClientToSysClientVoMapper.class},
    imports = {}
)
public interface SysClientVoToSysClientMapper extends BaseMapper<SysClientVo, SysClient> {
}
