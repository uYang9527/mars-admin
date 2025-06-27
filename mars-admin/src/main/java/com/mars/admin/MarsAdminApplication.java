package com.mars.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
@MapperScan("com.mars.admin.mapper")
@EnableConfigurationProperties
public class MarsAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarsAdminApplication.class, args);
    }
}
