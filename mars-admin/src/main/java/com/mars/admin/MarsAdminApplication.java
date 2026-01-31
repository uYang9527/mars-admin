package com.mars.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Mars管理系统启动类
 */
@SpringBootApplication(scanBasePackages = "com.mars")
@MapperScan("com.mars.**.mapper")
public class MarsAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarsAdminApplication.class, args);
        System.out.println("=================================");
        System.out.println("    Mars Admin 启动成功!");
        System.out.println("    http://localhost:8080");
        System.out.println("=================================");
    }
}
