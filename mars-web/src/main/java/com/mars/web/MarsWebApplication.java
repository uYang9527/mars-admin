package com.mars.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Mars管理系统启动类
 */
@SpringBootApplication(scanBasePackages = "com.mars")
@MapperScan("com.mars.**.mapper")
public class MarsWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarsWebApplication.class, args);
    }
}
