package com.mars.easy.admin.modules.auth.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import com.mars.easy.admin.framework.entity.LoginUser;
import com.mars.easy.admin.framework.util.LoginUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName StpInterfaceImpl
 * @Author 程序员Mars
 * @Date 2024/1/26 16:40
 * @Version 1.0
 */
@Order(Integer.MIN_VALUE)
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        LoginUser loginUser = LoginUtils.getLoginUser();
        List<String> list = new ArrayList<>();
        list.addAll(loginUser.getPermissions());
        return list;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        LoginUser loginUser = LoginUtils.getLoginUser();
        List<String> list = new ArrayList<String>();
        list.addAll(loginUser.getRoles());
        return list;
    }
}
