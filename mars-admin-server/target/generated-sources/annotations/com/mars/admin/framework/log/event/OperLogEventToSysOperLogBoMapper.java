package com.mars.admin.framework.log.event;

import com.mars.admin.modules.system.domain.bo.SysOperLogBo;
import com.mars.admin.modules.system.domain.bo.SysOperLogBoToOperLogEventMapper;
import com.mars.admin.modules.system.domain.bo.SysOperLogBoToSysOperLogMapper;
import io.github.linpeilie.AutoMapperConfig__20;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__20.class,
    uses = {SysOperLogBoToSysOperLogMapper.class,SysOperLogBoToOperLogEventMapper.class},
    imports = {}
)
public interface OperLogEventToSysOperLogBoMapper extends BaseMapper<OperLogEvent, SysOperLogBo> {
}
