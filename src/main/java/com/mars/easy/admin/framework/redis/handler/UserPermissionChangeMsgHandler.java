package com.mars.easy.admin.framework.redis.handler;


import com.mars.easy.admin.framework.entity.UserPermissionChangeMessage;

public interface UserPermissionChangeMsgHandler {
    void handlerMsg(UserPermissionChangeMessage message);
}
