package com.mars.easy.admin.framework.entity;

import com.mars.easy.admin.framework.enums.MessageTransferScopeEnum;
import com.mars.easy.admin.framework.enums.SocketChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author 程序员Mars
 * @date 2023/9/6 17:20
 */
@Data
public class SocketBean<T> {

    {
        data = (T) "";
        channel = SocketChannelEnum.DEFAULTS;
        scope = MessageTransferScopeEnum.SOCKET_CLIENT;
    }

    @Schema(description = "data")
    protected T data;

    @Schema(description = "通道类型")
    protected SocketChannelEnum channel;

    @Schema(description = "消息通知作用域")
    protected MessageTransferScopeEnum scope;



}
