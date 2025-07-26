package com.mars.admin.modules.system.service.impl;

import com.mars.admin.modules.system.entity.SysOperLog;
import com.mars.admin.modules.system.mapper.SysOperLogMapper;
import com.mars.admin.modules.system.service.ISysOperLogService;
import com.mars.admin.modules.base.service.impl.BaseServiceImpl;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static com.mars.admin.modules.system.entity.table.SysOperLogTableDef.SYS_OPER_LOG;


/**
 * 系统操作日志Service实现类
 * 继承 BaseServiceImpl 获得更多便捷方法
 *
 * @author Mars
 */
@Service
public class SysOperLogServiceImpl extends BaseServiceImpl<SysOperLog> implements ISysOperLogService {

    @Autowired
    private SysOperLogMapper sysOperLogMapper;

    @Override
    public Page<SysOperLog> selectOperLogPage(Page<SysOperLog> page, SysOperLog operLog) {
        QueryWrapper query = QueryWrapper.create()
                .select()
                .from(SYS_OPER_LOG);

        if (StringUtils.hasText(operLog.getOperName())) {
            query.and(SYS_OPER_LOG.OPER_NAME.like(operLog.getOperName()));
        }
        if (StringUtils.hasText(operLog.getTitle())) {
            query.and(SYS_OPER_LOG.TITLE.like(operLog.getTitle()));
        }
        if (operLog.getBusinessType() != null) {
            query.and(SYS_OPER_LOG.BUSINESS_TYPE.eq(operLog.getBusinessType()));
        }
        if (operLog.getStatus() != null) {
            query.and(SYS_OPER_LOG.STATUS.eq(operLog.getStatus()));
        }
        if (StringUtils.hasText(operLog.getOperIp())) {
            query.and(SYS_OPER_LOG.OPER_IP.like(operLog.getOperIp()));
        }

        query.orderBy(SYS_OPER_LOG.OPER_TIME.desc());

        return this.page(page, query);
    }

    @Override
    public List<SysOperLog> selectByOperName(String operName) {
        return sysOperLogMapper.selectByOperName(operName);
    }

    @Override
    public List<SysOperLog> selectByBusinessType(Integer businessType) {
        return sysOperLogMapper.selectByBusinessType(businessType);
    }

    @Override
    public List<SysOperLog> selectByStatus(Integer status) {
        return sysOperLogMapper.selectByStatus(status);
    }

    @Override
    public List<SysOperLog> selectByOperIp(String operIp) {
        return sysOperLogMapper.selectByOperIp(operIp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertOperLog(SysOperLog operLog) {
        return this.save(operLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOperLogs(Long[] operLogIds) {
        return this.removeByIds(Arrays.asList(operLogIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cleanOperLog() {
        // 使用原生SQL来执行清空操作，避免MyBatis-Flex的全表操作限制
        return sysOperLogMapper.cleanOperLog() > 0;
    }

    @Override
    public List<SysOperLog> exportOperLog(SysOperLog operLog) {
        QueryWrapper query = QueryWrapper.create()
                .select()
                .from(SYS_OPER_LOG);

        if (StringUtils.hasText(operLog.getTitle())) {
            query.and(SYS_OPER_LOG.TITLE.like(operLog.getTitle()));
        }
        if (StringUtils.hasText(operLog.getOperName())) {
            query.and(SYS_OPER_LOG.OPER_NAME.like(operLog.getOperName()));
        }
        if (operLog.getBusinessType() != null) {
            query.and(SYS_OPER_LOG.BUSINESS_TYPE.eq(operLog.getBusinessType()));
        }
        if (operLog.getStatus() != null) {
            query.and(SYS_OPER_LOG.STATUS.eq(operLog.getStatus()));
        }
        if (StringUtils.hasText(operLog.getOperIp())) {
            query.and(SYS_OPER_LOG.OPER_IP.like(operLog.getOperIp()));
        }

        query.orderBy(SYS_OPER_LOG.OPER_TIME.desc());

        return this.list(query);
    }
}
