package com.mars.easy.admin.modules.auth.service;


import com.mars.easy.admin.framework.enums.CommonResponseEnum;
import com.mars.easy.admin.framework.util.SpringApplicationContextUtils;
import com.mars.easy.admin.modules.auth.entity.ClientVO;
import com.mars.easy.admin.modules.auth.entity.LoginInfo;
import com.mars.easy.admin.modules.auth.entity.LoginVO;

/**
 * 策略接口
 *
 * @ClassName IAuthStrategy
 * @Author 程序员Mars
 * @Date 2024/1/23 9:38
 * @Version 1.0
 */
public interface IAuthStrategy {

    String BASE_NAME = "AuthStrategy";

    static LoginVO login(LoginInfo info, ClientVO client, String grantType) {
        // 授权类型和客户端ID
        String beanName = grantType + BASE_NAME;
        CommonResponseEnum.INVALID.message("无效的授权类型").assertFalse(SpringApplicationContextUtils.containsBean(beanName));
        IAuthStrategy instance = SpringApplicationContextUtils.getBean(beanName);
        return instance.login(info, client);
    }

    LoginVO login(LoginInfo info, ClientVO client);

}
