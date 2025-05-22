package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysDictType;
import com.mars.admin.modules.system.domain.SysDictTypeToSysDictTypeVoMapper;
import io.github.linpeilie.AutoMapperConfig__21;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__21.class,
    uses = {SysDictTypeToSysDictTypeVoMapper.class},
    imports = {}
)
public interface SysDictTypeVoToSysDictTypeMapper extends BaseMapper<SysDictTypeVo, SysDictType> {
}
