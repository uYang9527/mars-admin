package com.mars.admin.framework.context;

import cn.dev33.satoken.stp.StpUtil;

/**
 * 用户上下文工具类
 * 用于获取当前登录用户信息
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-07-05
 */
public class UserContext {

    /**
     * 获取当前登录用户ID
     *
     * @return 用户ID，未登录返回null
     */
    public static Long getCurrentUserId() {
        try {
            if (StpUtil.isLogin()) {
                Object loginId = StpUtil.getLoginId();
                if (loginId != null) {
                    return Long.valueOf(loginId.toString());
                }
            }
        } catch (Exception e) {
            // 忽略异常，返回null
        }
        return null;
    }

    /**
     * 获取当前登录用户ID，如果未登录返回默认值
     *
     * @param defaultUserId 默认用户ID
     * @return 用户ID
     */
    public static Long getCurrentUserId(Long defaultUserId) {
        Long userId = getCurrentUserId();
        return userId != null ? userId : defaultUserId;
    }

    /**
     * 检查是否已登录
     *
     * @return true已登录，false未登录
     */
    public static boolean isLogin() {
        try {
            return StpUtil.isLogin();
        } catch (Exception e) {
            return false;
        }
    }
} 