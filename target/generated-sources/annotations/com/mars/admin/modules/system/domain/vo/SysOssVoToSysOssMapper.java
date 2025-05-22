package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysOss;
import com.mars.admin.modules.system.domain.SysOssToSysOssVoMapper;
import io.github.linpeilie.AutoMapperConfig__19;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__19.class,
    uses = {SysOssToSysOssVoMapper.class},
    imports = {}
)
public interface SysOssVoToSysOssMapper extends BaseMapper<SysOssVo, SysOss> {
}
