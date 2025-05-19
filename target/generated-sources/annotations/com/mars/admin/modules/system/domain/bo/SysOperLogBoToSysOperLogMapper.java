package com.mars.admin.modules.system.domain.bo;

import com.mars.admin.framework.log.event.OperLogEventToSysOperLogBoMapper;
import com.mars.admin.modules.system.domain.SysOperLog;
import io.github.linpeilie.AutoMapperConfig__0;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__0.class,
    uses = {SysOperLogBoToOperLogEventMapper.class,OperLogEventToSysOperLogBoMapper.class},
    imports = {}
)
public interface SysOperLogBoToSysOperLogMapper extends BaseMapper<SysOperLogBo, SysOperLog> {
}
