package com.mars.admin.modules.system.domain.bo;

import com.mars.admin.modules.system.domain.SysOss;
import io.github.linpeilie.AutoMapperConfig__25;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__25.class,
    uses = {},
    imports = {}
)
public interface SysOssBoToSysOssMapper extends BaseMapper<SysOssBo, SysOss> {
}
