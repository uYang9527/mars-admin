package com.mars.admin.service.impl;

import com.mars.admin.entity.SysLoginInfo;
import com.mars.admin.framework.util.UserAgentUtils;
import com.mars.admin.mapper.SysLoginInfoMapper;
import com.mars.admin.service.ISysLoginInfoService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
        // 使用原生SQL来执行清空操作，避免MyBatis-Flex的全表操作限制
        return sysLoginInfoMapper.cleanLoginInfo() > 0;
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
        if (StringUtils.hasText(loginInfo.getIpaddr())) {
            query.and(SYS_LOGIN_INFO.IPADDR.like(loginInfo.getIpaddr()));
        }
        if (StringUtils.hasText(loginInfo.getLoginLocation())) {
            query.and(SYS_LOGIN_INFO.LOGIN_LOCATION.like(loginInfo.getLoginLocation()));
        }

        query.orderBy(SYS_LOGIN_INFO.LOGIN_TIME.desc());

        return this.list(query);
    }

    @Override
    public void recordLoginInfo(String username, String status, String message, String ipaddr) {
        try {
            // 尝试从当前请求中获取设备信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                jakarta.servlet.http.HttpServletRequest request = attributes.getRequest();
                String browser = UserAgentUtils.getBrowser(request);
                String os = UserAgentUtils.getOperatingSystem(request);
                String location = UserAgentUtils.getLocationByIp(ipaddr);
                
                recordLoginInfo(username, status, message, ipaddr, browser, os, location);
            } else {
                // 如果无法获取请求信息，使用简化版本
                recordLoginInfo(username, status, message, ipaddr, "Unknown", "Unknown", "未知位置");
            }
        } catch (Exception e) {
            // 记录日志失败不应该影响主业务，只记录错误日志
            System.err.println("记录登录日志失败: " + e.getMessage());
        }
    }

    @Override
    public void recordLoginInfo(String username, String status, String message, String ipaddr, 
                               String browser, String os, String location) {
        try {
            SysLoginInfo loginInfo = new SysLoginInfo();
            loginInfo.setUserName(username);
            loginInfo.setStatus(status);
            loginInfo.setMsg(message);
            loginInfo.setIpaddr(ipaddr);
            loginInfo.setBrowser(browser);
            loginInfo.setOs(os);
            loginInfo.setLoginLocation(location);
            
            this.save(loginInfo);
        } catch (Exception e) {
            // 记录日志失败不应该影响主业务，只记录错误日志
            System.err.println("记录登录日志失败: " + e.getMessage());
        }
    }
} 