package com.mars.admin.modules.system.domain.bo;

import com.mars.admin.modules.system.domain.SysDictType;
import io.github.linpeilie.AutoMapperConfig__21;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__21.class,
    uses = {},
    imports = {}
)
public interface SysDictTypeBoToSysDictTypeMapper extends BaseMapper<SysDictTypeBo, SysDictType> {
}
