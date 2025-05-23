package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysNotice;
import com.mars.admin.modules.system.domain.SysNoticeToSysNoticeVoMapper;
import io.github.linpeilie.AutoMapperConfig__20;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__20.class,
    uses = {SysNoticeToSysNoticeVoMapper.class},
    imports = {}
)
public interface SysNoticeVoToSysNoticeMapper extends BaseMapper<SysNoticeVo, SysNotice> {
}
