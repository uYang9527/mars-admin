package com.mars.easy.admin.framework.listener;

import com.mars.easy.admin.framework.redis.RedisTemplateClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * @author: 程序员Mars
 * @date: 2022/9/9 16:21
 * @description:
 */
@Slf4j
@Component
@Order(value = 1)
@RequiredArgsConstructor
public class AppStartListener implements CommandLineRunner, RedisTemplateClient {

    private final RedisTemplate redisTemplate;

    private final Environment env;

    @Override
    public void run(String... args) throws Exception {

        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        log.info("\n----------------------------------------------------------\n\t" +
                "Server is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                "Knife4j-ui: http://" + ip + ":" + port + path + "/doc.html\n\t" +
                "----------------------------------------------------------");
    }

    @Override
    public RedisTemplate getTemplate() {
        return redisTemplate;
    }
}
