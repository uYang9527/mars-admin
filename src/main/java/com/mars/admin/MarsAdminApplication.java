package com.mars.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@MapperScan("com.mars.admin.modules.*.mapper")
@SpringBootApplication
@ComponentScan("com.mars.admin")
public class MarsAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarsAdminApplication.class, args);
    }

}
