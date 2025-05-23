package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysDictTypeBoToSysDictTypeMapper;
import com.mars.admin.modules.system.domain.vo.SysDictTypeVo;
import com.mars.admin.modules.system.domain.vo.SysDictTypeVoToSysDictTypeMapper;
import io.github.linpeilie.AutoMapperConfig__20;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__20.class,
    uses = {SysDictTypeVoToSysDictTypeMapper.class,SysDictTypeBoToSysDictTypeMapper.class},
    imports = {}
)
public interface SysDictTypeToSysDictTypeVoMapper extends BaseMapper<SysDictType, SysDictTypeVo> {
}
