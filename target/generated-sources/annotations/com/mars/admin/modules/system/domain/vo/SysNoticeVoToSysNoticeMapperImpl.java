package com.mars.admin.modules.system.domain.vo;

import com.mars.admin.modules.system.domain.SysNotice;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-19T19:52:38+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Microsoft)"
)
@Component
public class SysNoticeVoToSysNoticeMapperImpl implements SysNoticeVoToSysNoticeMapper {

    @Override
    public SysNotice convert(SysNoticeVo arg0) {
        if ( arg0 == null ) {
            return null;
        }

        SysNotice sysNotice = new SysNotice();

        sysNotice.setCreateBy( arg0.getCreateBy() );
        sysNotice.setCreateTime( arg0.getCreateTime() );
        sysNotice.setNoticeId( arg0.getNoticeId() );
        sysNotice.setNoticeTitle( arg0.getNoticeTitle() );
        sysNotice.setNoticeType( arg0.getNoticeType() );
        sysNotice.setNoticeContent( arg0.getNoticeContent() );
        sysNotice.setStatus( arg0.getStatus() );
        sysNotice.setRemark( arg0.getRemark() );

        return sysNotice;
    }

    @Override
    public SysNotice convert(SysNoticeVo arg0, SysNotice arg1) {
        if ( arg0 == null ) {
            return arg1;
        }

        arg1.setCreateBy( arg0.getCreateBy() );
        arg1.setCreateTime( arg0.getCreateTime() );
        arg1.setNoticeId( arg0.getNoticeId() );
        arg1.setNoticeTitle( arg0.getNoticeTitle() );
        arg1.setNoticeType( arg0.getNoticeType() );
        arg1.setNoticeContent( arg0.getNoticeContent() );
        arg1.setStatus( arg0.getStatus() );
        arg1.setRemark( arg0.getRemark() );

        return arg1;
    }
}
