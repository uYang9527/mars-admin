package com.mars.admin.modules.system.domain.bo;

import com.mars.admin.modules.system.domain.SysClient;
import io.github.linpeilie.AutoMapperConfig__14;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__14.class,
    uses = {},
    imports = {}
)
public interface SysClientBoToSysClientMapper extends BaseMapper<SysClientBo, SysClient> {
}
