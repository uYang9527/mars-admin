package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysDictTypeBoToSysDictTypeMapper;
import com.mars.admin.modules.system.domain.vo.SysDictTypeVo;
import com.mars.admin.modules.system.domain.vo.SysDictTypeVoToSysDictTypeMapper;
import io.github.linpeilie.AutoMapperConfig__18;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__18.class,
    uses = {SysDictTypeVoToSysDictTypeMapper.class,SysDictTypeBoToSysDictTypeMapper.class},
    imports = {}
)
public interface SysDictTypeToSysDictTypeVoMapper extends BaseMapper<SysDictType, SysDictTypeVo> {
}
