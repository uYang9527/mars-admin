package com.mars.admin.modules.system.domain.bo;

import com.mars.admin.framework.log.event.OperLogEvent;
import com.mars.admin.framework.log.event.OperLogEventToSysOperLogBoMapper;
import io.github.linpeilie.AutoMapperConfig__19;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__19.class,
    uses = {SysOperLogBoToSysOperLogMapper.class,OperLogEventToSysOperLogBoMapper.class},
    imports = {}
)
public interface SysOperLogBoToOperLogEventMapper extends BaseMapper<SysOperLogBo, OperLogEvent> {
}
