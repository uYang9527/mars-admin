package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysDept;
import com.mars.admin.modules.system.domain.SysDeptToSysDeptVoMapper;
import com.mars.admin.modules.system.domain.bo.SysDeptBoToSysDeptMapper;
import io.github.linpeilie.AutoMapperConfig__20;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__20.class,
    uses = {SysDeptBoToSysDeptMapper.class,SysDeptToSysDeptVoMapper.class,SysDeptToSysDeptVoMapper.class},
    imports = {}
)
public interface SysDeptVoToSysDeptMapper extends BaseMapper<SysDeptVo, SysDept> {
}
