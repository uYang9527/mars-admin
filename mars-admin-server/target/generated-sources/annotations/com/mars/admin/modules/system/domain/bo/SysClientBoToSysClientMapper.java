package com.mars.admin.modules.system.domain.bo;

import com.mars.admin.modules.system.domain.SysClient;
import io.github.linpeilie.AutoMapperConfig__20;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__20.class,
    uses = {},
    imports = {}
)
public interface SysClientBoToSysClientMapper extends BaseMapper<SysClientBo, SysClient> {
}
