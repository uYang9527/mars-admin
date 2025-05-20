package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysOssBoToSysOssMapper;
import com.mars.admin.modules.system.domain.vo.SysOssVo;
import com.mars.admin.modules.system.domain.vo.SysOssVoToSysOssMapper;
import io.github.linpeilie.AutoMapperConfig__14;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__14.class,
    uses = {SysOssBoToSysOssMapper.class,SysOssVoToSysOssMapper.class},
    imports = {}
)
public interface SysOssToSysOssVoMapper extends BaseMapper<SysOss, SysOssVo> {
}
