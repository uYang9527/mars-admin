package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysOssConfigBoToSysOssConfigMapper;
import com.mars.admin.modules.system.domain.vo.SysOssConfigVo;
import com.mars.admin.modules.system.domain.vo.SysOssConfigVoToSysOssConfigMapper;
import io.github.linpeilie.AutoMapperConfig__25;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__25.class,
    uses = {SysOssConfigVoToSysOssConfigMapper.class,SysOssConfigBoToSysOssConfigMapper.class},
    imports = {}
)
public interface SysOssConfigToSysOssConfigVoMapper extends BaseMapper<SysOssConfig, SysOssConfigVo> {
}
