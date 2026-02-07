package com.mars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Mars Admin 启动类
 */
@SpringBootApplication
public class MarsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarsApplication.class, args);
        System.out.println("========================================");
        System.out.println("    Mars Admin 启动成功！");
        System.out.println("========================================");
    }
}
