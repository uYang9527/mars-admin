package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysLogininforBoToSysLogininforMapper;
import com.mars.admin.modules.system.domain.vo.SysLogininforVo;
import com.mars.admin.modules.system.domain.vo.SysLogininforVoToSysLogininforMapper;
import io.github.linpeilie.AutoMapperConfig__18;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__18.class,
    uses = {SysLogininforVoToSysLogininforMapper.class,SysLogininforBoToSysLogininforMapper.class},
    imports = {}
)
public interface SysLogininforToSysLogininforVoMapper extends BaseMapper<SysLogininfor, SysLogininforVo> {
}
