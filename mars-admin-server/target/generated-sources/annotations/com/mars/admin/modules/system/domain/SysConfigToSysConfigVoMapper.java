package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysConfigBoToSysConfigMapper;
import com.mars.admin.modules.system.domain.vo.SysConfigVo;
import com.mars.admin.modules.system.domain.vo.SysConfigVoToSysConfigMapper;
import io.github.linpeilie.AutoMapperConfig__25;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__25.class,
    uses = {SysConfigBoToSysConfigMapper.class,SysConfigVoToSysConfigMapper.class},
    imports = {}
)
public interface SysConfigToSysConfigVoMapper extends BaseMapper<SysConfig, SysConfigVo> {
}
