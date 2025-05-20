package com.mars.admin.modules.system.domain.bo;

import com.mars.admin.modules.system.domain.SysLogininfor;
import io.github.linpeilie.AutoMapperConfig__14;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__14.class,
    uses = {},
    imports = {}
)
public interface SysLogininforBoToSysLogininforMapper extends BaseMapper<SysLogininforBo, SysLogininfor> {
}
