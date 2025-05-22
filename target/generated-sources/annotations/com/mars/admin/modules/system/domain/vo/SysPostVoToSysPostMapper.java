package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysPost;
import com.mars.admin.modules.system.domain.SysPostToSysPostVoMapper;
import io.github.linpeilie.AutoMapperConfig__21;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__21.class,
    uses = {SysPostToSysPostVoMapper.class},
    imports = {}
)
public interface SysPostVoToSysPostMapper extends BaseMapper<SysPostVo, SysPost> {
}
