package com.mars.easy.admin.modules.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.mars.easy.admin.framework.entity.ApiResult;
import com.mars.easy.admin.modules.auth.entity.LoginInfo;
import com.mars.easy.admin.modules.auth.entity.LoginVO;
import com.mars.easy.admin.modules.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用controller
 *
 * @ClassName CommonController
 * @Author 程序员Mars
 * @Date 2023/12/25 10:07
 * @Version 1.0
 */
@Tag(name = "认证")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "登录")
    @PostMapping("login")
    public ApiResult<LoginVO> login(@RequestBody LoginInfo loginInfo) {
        return ApiResult.success(authService.loginClient(loginInfo));
    }

    @Operation(summary = "登出")
    @PostMapping("logout")
    public ApiResult<Void> logout() {
        StpUtil.getTokenSession().logout();
        StpUtil.logout();
        return ApiResult.success();
    }


}
