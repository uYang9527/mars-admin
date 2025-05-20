package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysDictData;
import com.mars.admin.modules.system.domain.SysDictDataToSysDictDataVoMapper;
import io.github.linpeilie.AutoMapperConfig__14;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__14.class,
    uses = {SysDictDataToSysDictDataVoMapper.class},
    imports = {}
)
public interface SysDictDataVoToSysDictDataMapper extends BaseMapper<SysDictDataVo, SysDictData> {
}
