package com.mars.admin.modules.system.domain.bo;

import com.mars.admin.modules.system.domain.SysSocial;
import io.github.linpeilie.AutoMapperConfig__21;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__21.class,
    uses = {},
    imports = {}
)
public interface SysSocialBoToSysSocialMapper extends BaseMapper<SysSocialBo, SysSocial> {
}
