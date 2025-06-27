package com.mars.admin.service.impl;

import com.mars.admin.entity.SysLoginInfo;
import com.mars.admin.mapper.SysLoginInfoMapper;
import com.mars.admin.service.ISysLoginInfoService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static com.mars.admin.entity.table.SysLoginInfoTableDef.SYS_LOGIN_INFO;

/**
 * 系统登录日志Service实现类
 * 继承 BaseServiceImpl 获得更多便捷方法
 *
 * @author Mars
 */
@Service
public class SysLoginInfoServiceImpl extends BaseServiceImpl<SysLoginInfo> implements ISysLoginInfoService {

    @Autowired
    private SysLoginInfoMapper sysLoginInfoMapper;

    @Override
    public Page<SysLoginInfo> selectLoginInfoPage(Page<SysLoginInfo> page, SysLoginInfo loginInfo) {
        QueryWrapper query = QueryWrapper.create()
                .select()
                .from(SYS_LOGIN_INFO);

        if (StringUtils.hasText(loginInfo.getUserName())) {
            query.and(SYS_LOGIN_INFO.USER_NAME.like(loginInfo.getUserName()));
        }
        if (StringUtils.hasText(loginInfo.getStatus())) {
            query.and(SYS_LOGIN_INFO.STATUS.eq(loginInfo.getStatus()));
        }
        if (StringUtils.hasText(loginInfo.getIpaddr())) {
            query.and(SYS_LOGIN_INFO.IPADDR.like(loginInfo.getIpaddr()));
        }
        if (StringUtils.hasText(loginInfo.getLoginLocation())) {
            query.and(SYS_LOGIN_INFO.LOGIN_LOCATION.like(loginInfo.getLoginLocation()));
        }

        query.orderBy(SYS_LOGIN_INFO.LOGIN_TIME.desc());

        return this.page(page, query);
    }

    @Override
    public List<SysLoginInfo> selectByUserName(String userName) {
        return sysLoginInfoMapper.selectByUserName(userName);
    }

    @Override
    public List<SysLoginInfo> selectByStatus(String status) {
        return sysLoginInfoMapper.selectByStatus(status);
    }

    @Override
    public List<SysLoginInfo> selectByIpaddr(String ipaddr) {
        return sysLoginInfoMapper.selectByIpaddr(ipaddr);
    }

    @Override
    public List<SysLoginInfo> selectByLoginLocation(String loginLocation) {
        return sysLoginInfoMapper.selectByLoginLocation(loginLocation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertLoginInfo(SysLoginInfo loginInfo) {
        return this.save(loginInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLoginInfos(Long[] loginInfoIds) {
        return this.removeByIds(Arrays.asList(loginInfoIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cleanLoginInfo() {
        return this.remove(QueryWrapper.create());
    }

    @Override
    public List<SysLoginInfo> exportLoginInfo(SysLoginInfo loginInfo) {
        QueryWrapper query = QueryWrapper.create()
                .select()
                .from(SYS_LOGIN_INFO);

        if (StringUtils.hasText(loginInfo.getUserName())) {
            query.and(SYS_LOGIN_INFO.USER_NAME.like(loginInfo.getUserName()));
        }
        if (StringUtils.hasText(loginInfo.getStatus())) {
            query.and(SYS_LOGIN_INFO.STATUS.eq(loginInfo.getStatus()));
        }

        query.orderBy(SYS_LOGIN_INFO.LOGIN_TIME.desc());

        return this.list(query);
    }

    @Override
    public void recordLoginInfo(String username, String status, String message, String ipaddr) {
        try {
            SysLoginInfo loginInfo = new SysLoginInfo();
            loginInfo.setUserName(username);
            loginInfo.setStatus(status);
            loginInfo.setMsg(message);
            loginInfo.setIpaddr(ipaddr);
            // 可以根据需要添加更多信息，如浏览器、操作系统、登录地点等
            // loginInfo.setBrowser(getBrowser());
            // loginInfo.setOs(getOs());
            // loginInfo.setLoginLocation(getLoginLocation(ipaddr));
            
            this.save(loginInfo);
        } catch (Exception e) {
            // 记录日志失败不应该影响主业务，只记录错误日志
            // 这里可以使用日志框架记录错误
            System.err.println("记录登录日志失败: " + e.getMessage());
        }
    }
} 