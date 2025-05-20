package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysLogininfor;
import com.mars.admin.modules.system.domain.SysLogininforToSysLogininforVoMapper;
import io.github.linpeilie.AutoMapperConfig__18;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__18.class,
    uses = {SysLogininforToSysLogininforVoMapper.class},
    imports = {}
)
public interface SysLogininforVoToSysLogininforMapper extends BaseMapper<SysLogininforVo, SysLogininfor> {
}
