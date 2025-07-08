package com.mars.admin;

import com.mars.admin.framework.anno.MarsAdminStarter;
import org.springframework.boot.SpringApplication;


@MarsAdminStarter
public class MarsAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarsAdminApplication.class, args);
    }
}
