package com.mars.admin.modules.system.domain.bo;

import com.mars.admin.modules.system.domain.SysDept;
import io.github.linpeilie.AutoMapperConfig__18;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__18.class,
    uses = {},
    imports = {}
)
public interface SysDeptBoToSysDeptMapper extends BaseMapper<SysDeptBo, SysDept> {
}
