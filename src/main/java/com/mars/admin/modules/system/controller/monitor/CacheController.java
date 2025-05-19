package com.mars.admin.modules.system.controller.monitor;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import com.mars.admin.framework.core.domain.R;
import com.mars.admin.framework.core.utils.StringUtils;
import com.mars.admin.modules.system.domain.vo.CacheListInfoVo;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 缓存监控
 *
 * @author Mars
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/monitor/cache")
public class CacheController {

    private final RedissonConnectionFactory connectionFactory;

    /**
     * 获取缓存监控列表
     */
    @SaCheckPermission("monitor:cache:list")
    @GetMapping()
    public R<CacheListInfoVo> getInfo() throws Exception {
        RedisConnection connection = connectionFactory.getConnection();
        Properties commandStats = connection.commands().info("commandstats");

        List<Map<String, String>> pieList = new ArrayList<>();
        if (commandStats != null) {
            commandStats.stringPropertyNames().forEach(key -> {
                Map<String, String> data = new HashMap<>(2);
                String property = commandStats.getProperty(key);
                data.put("name", StringUtils.removeStart(key, "cmdstat_"));
                data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
                pieList.add(data);
            });
        }

        CacheListInfoVo infoVo = new CacheListInfoVo();
        infoVo.setInfo(connection.commands().info());
        infoVo.setDbSize(connection.commands().dbSize());
        infoVo.setCommandStats(pieList);
        return R.ok(infoVo);
    }

}
