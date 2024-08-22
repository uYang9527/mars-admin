package com.mars.easy.admin.modules.auth.service.impl;


import com.mars.easy.admin.framework.enums.CommonResponseEnum;
import com.mars.easy.admin.framework.util.SpringApplicationContextUtils;
import com.mars.easy.admin.modules.auth.entity.ClientVO;
import com.mars.easy.admin.modules.auth.entity.LoginInfo;
import com.mars.easy.admin.modules.auth.entity.LoginVO;
import com.mars.easy.admin.modules.auth.service.AuthService;
import com.mars.easy.admin.modules.auth.service.ClientService;
import com.mars.easy.admin.modules.auth.service.IAuthStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author 程序员Mars
 * @since 2022-10-01
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Override
    public LoginVO loginClient(LoginInfo info) {
        String clientId = info.getClientId();
        ClientService clientService = SpringApplicationContextUtils.getBean(ClientService.class);
        ClientVO client = clientService.getClientByClientId(clientId);
        CommonResponseEnum.CLIENT_INVALID.assertNull(client);
        CommonResponseEnum.CLIENT_BLOCKED.assertTrue(!("1003001").equals(client.getClientStatusCd()));
        return IAuthStrategy.login(info, client, info.getGrantType());
    }

}
