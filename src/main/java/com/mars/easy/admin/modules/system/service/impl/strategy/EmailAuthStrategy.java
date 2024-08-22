package com.mars.easy.admin.modules.system.service.impl.strategy;

import com.mars.easy.admin.modules.auth.entity.ClientVO;
import com.mars.easy.admin.modules.auth.entity.LoginInfo;
import com.mars.easy.admin.modules.auth.entity.LoginVO;
import com.mars.easy.admin.modules.auth.service.IAuthStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName EmailAuthStrategy
 * @Author 程序员Mars
 * @Date 2024/1/23 10:29
 * @Version 1.0
 */
@Slf4j
@Service("email" + IAuthStrategy.BASE_NAME)
public class EmailAuthStrategy implements IAuthStrategy {
    @Override
    public LoginVO login(LoginInfo body, ClientVO client) {
        return null;
    }
}
