package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysOssConfigBoToSysOssConfigMapper;
import com.mars.admin.modules.system.domain.vo.SysOssConfigVo;
import com.mars.admin.modules.system.domain.vo.SysOssConfigVoToSysOssConfigMapper;
import io.github.linpeilie.AutoMapperConfig__14;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__14.class,
    uses = {SysOssConfigBoToSysOssConfigMapper.class,SysOssConfigVoToSysOssConfigMapper.class},
    imports = {}
)
public interface SysOssConfigToSysOssConfigVoMapper extends BaseMapper<SysOssConfig, SysOssConfigVo> {
}
