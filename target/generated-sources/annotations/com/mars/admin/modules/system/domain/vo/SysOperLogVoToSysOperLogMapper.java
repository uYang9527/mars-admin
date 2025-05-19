package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysOperLog;
import com.mars.admin.modules.system.domain.SysOperLogToSysOperLogVoMapper;
import io.github.linpeilie.AutoMapperConfig__0;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__0.class,
    uses = {SysOperLogToSysOperLogVoMapper.class},
    imports = {}
)
public interface SysOperLogVoToSysOperLogMapper extends BaseMapper<SysOperLogVo, SysOperLog> {
}
