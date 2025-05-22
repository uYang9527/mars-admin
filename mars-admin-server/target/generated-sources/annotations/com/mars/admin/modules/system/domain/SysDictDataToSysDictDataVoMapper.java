package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysDictDataBoToSysDictDataMapper;
import com.mars.admin.modules.system.domain.vo.SysDictDataVo;
import com.mars.admin.modules.system.domain.vo.SysDictDataVoToSysDictDataMapper;
import io.github.linpeilie.AutoMapperConfig__25;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__25.class,
    uses = {SysDictDataVoToSysDictDataMapper.class,SysDictDataBoToSysDictDataMapper.class},
    imports = {}
)
public interface SysDictDataToSysDictDataVoMapper extends BaseMapper<SysDictData, SysDictDataVo> {
}
