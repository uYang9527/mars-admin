package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysOperLogBoToSysOperLogMapper;
import com.mars.admin.modules.system.domain.vo.SysOperLogVo;
import com.mars.admin.modules.system.domain.vo.SysOperLogVoToSysOperLogMapper;
import io.github.linpeilie.AutoMapperConfig__19;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__19.class,
    uses = {SysOperLogVoToSysOperLogMapper.class,SysOperLogBoToSysOperLogMapper.class},
    imports = {}
)
public interface SysOperLogToSysOperLogVoMapper extends BaseMapper<SysOperLog, SysOperLogVo> {
}
