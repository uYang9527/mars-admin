package com.mars.easy.admin.framework.util;


import com.mars.easy.admin.framework.entity.SocketBean;
import com.mars.easy.admin.framework.entity.SocketResult;
import com.mars.easy.admin.framework.entity.TransferMessage;

import java.util.List;

/**
 * @Author 程序员Mars
 * @date 2023/9/6 17:27
 */
public class SocketUtil {

    public static SocketBean formatSocketMessage(String message) {
        return JsonUtils.parseObject(message, SocketResult.class);
    }

    public static String transferMessage(SocketBean bean) {
        return JsonUtils.toJsonString(bean);
    }

    /**
     * 发送消息转换
     *
     * @param sb
     * @param usernames
     * @return
     */
    public static TransferMessage pubTransferMessage(SocketBean sb, List<String> usernames) {
        TransferMessage transferMessage = new TransferMessage();
        transferMessage.setMessage(sb);
        transferMessage.setToUsers(usernames);
        return transferMessage;
    }

}
