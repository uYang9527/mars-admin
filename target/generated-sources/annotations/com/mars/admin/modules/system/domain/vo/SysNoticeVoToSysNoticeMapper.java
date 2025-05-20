package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysNotice;
import com.mars.admin.modules.system.domain.SysNoticeToSysNoticeVoMapper;
import io.github.linpeilie.AutoMapperConfig__14;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__14.class,
    uses = {SysNoticeToSysNoticeVoMapper.class},
    imports = {}
)
public interface SysNoticeVoToSysNoticeMapper extends BaseMapper<SysNoticeVo, SysNotice> {
}
