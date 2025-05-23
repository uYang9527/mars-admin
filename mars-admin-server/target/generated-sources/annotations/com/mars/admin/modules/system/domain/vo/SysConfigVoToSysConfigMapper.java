package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysConfig;
import com.mars.admin.modules.system.domain.SysConfigToSysConfigVoMapper;
import io.github.linpeilie.AutoMapperConfig__20;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__20.class,
    uses = {SysConfigToSysConfigVoMapper.class},
    imports = {}
)
public interface SysConfigVoToSysConfigMapper extends BaseMapper<SysConfigVo, SysConfig> {
}
