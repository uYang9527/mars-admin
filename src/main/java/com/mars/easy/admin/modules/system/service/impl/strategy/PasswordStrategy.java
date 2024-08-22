package com.mars.easy.admin.modules.system.service.impl.strategy;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.mars.easy.admin.framework.util.LoginUtils;
import com.mars.easy.admin.modules.auth.entity.ClientVO;
import com.mars.easy.admin.modules.auth.entity.LoginInfo;
import com.mars.easy.admin.modules.auth.entity.LoginVO;
import com.mars.easy.admin.modules.auth.service.IAuthStrategy;
import com.mars.easy.admin.modules.system.service.SysUserService;
import com.mars.easy.admin.framework.entity.BaseUserInfo;
import com.mars.easy.admin.framework.entity.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 密码认证策略
 *
 * @ClassName PasswordStrategy
 * @Author 程序员Mars
 * @Date 2024/1/23 10:29
 * @Version 1.0
 */
@Slf4j
@Service("password" + IAuthStrategy.BASE_NAME)
@RequiredArgsConstructor
public class PasswordStrategy implements IAuthStrategy {

    private final SysUserService sysUserService;

    @Override
    public LoginVO login(LoginInfo info, ClientVO client) {
        String clientId = client.getClientId();
        String password = info.getPassword();
        String username = info.getUsername();
        SaLoginModel model = createLoginModel(client);
        LoginUser loginUser = sysUserService.buildLoginUser(username, password);
        Long userId = loginUser.getUserInfo().getId();
        Map<String, Object> extraData = createExtraData(clientId, userId);
        LoginUtils.performLogin(loginUser, model, extraData);
        return createLoginVO(loginUser.getUserInfo());
    }

    private SaLoginModel createLoginModel(ClientVO client) {
        SaLoginModel model = new SaLoginModel();
        model.setDevice(client.getDeviceTypeCd());
        model.setTimeout(client.getTimeout());
        model.setActiveTimeout(client.getActiveTimeout());
        return model;
    }

    private Map<String, Object> createExtraData(String clientId, Long userId) {
        Map<String, Object> extraData = new HashMap<>();
        extraData.put("clientId", clientId);
        extraData.put("userId", userId);
        return extraData;
    }

    private LoginVO createLoginVO(BaseUserInfo userInfo) {
        LoginVO loginVo = new LoginVO();
        loginVo.setAccessToken(StpUtil.getTokenValue());
        loginVo.setExpireIn(StpUtil.getTokenTimeout());
        loginVo.setUserInfo(userInfo);
        return loginVo;
    }

}
