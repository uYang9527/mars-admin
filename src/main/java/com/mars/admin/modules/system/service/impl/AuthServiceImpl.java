package com.mars.admin.modules.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.mars.admin.framework.core.constant.SystemConstants;
import com.mars.admin.framework.core.exception.ServiceException;
import com.mars.admin.framework.core.utils.MessageUtils;
import com.mars.admin.framework.core.utils.StringUtils;
import com.mars.admin.modules.system.domain.bo.LoginBo;
import com.mars.admin.modules.system.domain.vo.LoginVo;
import com.mars.admin.modules.system.domain.vo.SysClientVo;
import com.mars.admin.modules.system.service.AuthService;
import com.mars.admin.modules.system.service.IAuthStrategy;
import com.mars.admin.modules.system.service.ISysClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final ISysClientService clientService;


    @Override
    public LoginVo login(LoginBo loginBody) {
        // 授权类型和客户端id
        String clientId = loginBody.getClientId();
        String grantType = loginBody.getGrantType();
        SysClientVo client = clientService.queryByClientId(clientId);
        if (ObjectUtil.isNull(client) || !StringUtils.contains(client.getGrantType(), grantType)) {
            log.info("客户端id: {} 认证类型：{} 异常!.", clientId, grantType);
            throw new ServiceException(MessageUtils.message("auth.grant.type.error"));
        } else if (!SystemConstants.NORMAL.equals(client.getStatus())) {
            throw new ServiceException(MessageUtils.message("auth.grant.type.blocked"));
        }
        return IAuthStrategy.login(JSONObject.toJSONString(loginBody), client, grantType);
    }
}
