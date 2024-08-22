package com.mars.easy.admin.framework.entity;

import com.mars.easy.admin.framework.enums.SocketChannelEnum;
import com.mars.easy.admin.framework.util.PageUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 程序员Mars
 * @date 2023/9/6 10:17
 */
@Data
public class SocketPageResult<T> extends SocketResult<T> {

    public static <T> SocketPageResult<PageResult<T>> success(List<T> data) {
        SocketPageResult<PageResult<T>> apiResult = new SocketPageResult<>();
        apiResult.data = (data != null) ? PageUtils.getPageResult(data) : PageUtils.getPageResult(new ArrayList<>());
        return apiResult;
    }

    public static <T> SocketPageResult<PageResult<T>> success(SocketChannelEnum channel, PageResult<T> data) {
        SocketPageResult<PageResult<T>> apiResult = new SocketPageResult<>();
        apiResult.data = (data != null) ? data : PageUtils.getPageResult(new ArrayList<>());
        apiResult.channel = channel;
        return apiResult;
    }

    public static <T> SocketPageResult<PageResult<T>> success(SocketChannelEnum channel, List<T> data, Object param) {
        SocketPageResult<PageResult<T>> apiResult = new SocketPageResult<>();
        apiResult.channel = channel;
        apiResult.data = (data != null) ? PageUtils.getPageResult(data) : PageUtils.getPageResult(new ArrayList<>(), param);
        return apiResult;
    }

}
