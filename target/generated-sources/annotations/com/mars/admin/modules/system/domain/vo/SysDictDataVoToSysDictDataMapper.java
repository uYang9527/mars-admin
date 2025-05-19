package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysDictData;
import com.mars.admin.modules.system.domain.SysDictDataToSysDictDataVoMapper;
import io.github.linpeilie.AutoMapperConfig__0;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__0.class,
    uses = {SysDictDataToSysDictDataVoMapper.class},
    imports = {}
)
public interface SysDictDataVoToSysDictDataMapper extends BaseMapper<SysDictDataVo, SysDictData> {
}
