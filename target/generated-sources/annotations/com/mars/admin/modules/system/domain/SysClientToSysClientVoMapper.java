package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysClientBoToSysClientMapper;
import com.mars.admin.modules.system.domain.vo.SysClientVo;
import com.mars.admin.modules.system.domain.vo.SysClientVoToSysClientMapper;
import io.github.linpeilie.AutoMapperConfig__21;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__21.class,
    uses = {SysClientBoToSysClientMapper.class,SysClientVoToSysClientMapper.class},
    imports = {}
)
public interface SysClientToSysClientVoMapper extends BaseMapper<SysClient, SysClientVo> {
}
