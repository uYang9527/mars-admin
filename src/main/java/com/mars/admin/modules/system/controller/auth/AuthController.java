package com.mars.admin.modules.system.controller.auth;

import cn.dev33.satoken.annotation.SaIgnore;
import com.mars.admin.framework.core.domain.R;
import com.mars.admin.framework.core.domain.model.LoginBody;
import com.mars.admin.modules.system.domain.bo.LoginBo;
import com.mars.admin.modules.system.domain.vo.LoginVo;
import com.mars.admin.modules.system.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@SaIgnore
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    /**
     * 登录
     *
     * @param loginBo 登录请求参数
     * @return 登录结果
     */
    @PostMapping("/login")
    public R<LoginVo> login(@RequestBody LoginBo loginBo) {
        return R.ok(authService.login(loginBo));
    }


}
