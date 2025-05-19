package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysOssConfig;
import com.mars.admin.modules.system.domain.SysOssConfigToSysOssConfigVoMapper;
import io.github.linpeilie.AutoMapperConfig__0;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__0.class,
    uses = {SysOssConfigToSysOssConfigVoMapper.class},
    imports = {}
)
public interface SysOssConfigVoToSysOssConfigMapper extends BaseMapper<SysOssConfigVo, SysOssConfig> {
}
