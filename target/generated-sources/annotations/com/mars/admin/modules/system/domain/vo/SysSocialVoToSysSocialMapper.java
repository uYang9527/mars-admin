package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysSocial;
import com.mars.admin.modules.system.domain.SysSocialToSysSocialVoMapper;
import io.github.linpeilie.AutoMapperConfig__0;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__0.class,
    uses = {SysSocialToSysSocialVoMapper.class},
    imports = {}
)
public interface SysSocialVoToSysSocialMapper extends BaseMapper<SysSocialVo, SysSocial> {
}
