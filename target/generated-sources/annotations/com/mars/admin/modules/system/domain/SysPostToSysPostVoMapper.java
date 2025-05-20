package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysPostBoToSysPostMapper;
import com.mars.admin.modules.system.domain.vo.SysPostVo;
import com.mars.admin.modules.system.domain.vo.SysPostVoToSysPostMapper;
import io.github.linpeilie.AutoMapperConfig__14;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__14.class,
    uses = {SysPostVoToSysPostMapper.class,SysPostBoToSysPostMapper.class},
    imports = {}
)
public interface SysPostToSysPostVoMapper extends BaseMapper<SysPost, SysPostVo> {
}
