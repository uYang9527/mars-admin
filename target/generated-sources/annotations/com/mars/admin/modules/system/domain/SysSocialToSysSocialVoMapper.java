package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysSocialBoToSysSocialMapper;
import com.mars.admin.modules.system.domain.vo.SysSocialVo;
import com.mars.admin.modules.system.domain.vo.SysSocialVoToSysSocialMapper;
import io.github.linpeilie.AutoMapperConfig__14;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__14.class,
    uses = {SysSocialVoToSysSocialMapper.class,SysSocialBoToSysSocialMapper.class},
    imports = {}
)
public interface SysSocialToSysSocialVoMapper extends BaseMapper<SysSocial, SysSocialVo> {
}
