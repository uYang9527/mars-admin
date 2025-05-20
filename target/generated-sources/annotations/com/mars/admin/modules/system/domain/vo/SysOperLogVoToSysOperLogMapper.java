package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysOperLog;
import com.mars.admin.modules.system.domain.SysOperLogToSysOperLogVoMapper;
import io.github.linpeilie.AutoMapperConfig__14;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__14.class,
    uses = {SysOperLogToSysOperLogVoMapper.class},
    imports = {}
)
public interface SysOperLogVoToSysOperLogMapper extends BaseMapper<SysOperLogVo, SysOperLog> {
}
