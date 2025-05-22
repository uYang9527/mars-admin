package com.mars.admin.modules.system.domain;

import com.mars.admin.modules.system.domain.bo.SysNoticeBoToSysNoticeMapper;
import com.mars.admin.modules.system.domain.vo.SysNoticeVo;
import com.mars.admin.modules.system.domain.vo.SysNoticeVoToSysNoticeMapper;
import io.github.linpeilie.AutoMapperConfig__19;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__19.class,
    uses = {SysNoticeVoToSysNoticeMapper.class,SysNoticeBoToSysNoticeMapper.class},
    imports = {}
)
public interface SysNoticeToSysNoticeVoMapper extends BaseMapper<SysNotice, SysNoticeVo> {
}
