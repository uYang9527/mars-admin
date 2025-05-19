package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysDeptBoToSysDeptMapper;
import com.mars.admin.modules.system.domain.vo.SysDeptVo;
import com.mars.admin.modules.system.domain.vo.SysDeptVoToSysDeptMapper;
import io.github.linpeilie.AutoMapperConfig__0;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__0.class,
    uses = {SysDeptBoToSysDeptMapper.class,SysDeptVoToSysDeptMapper.class,SysDeptBoToSysDeptMapper.class},
    imports = {}
)
public interface SysDeptToSysDeptVoMapper extends BaseMapper<SysDept, SysDeptVo> {
}
