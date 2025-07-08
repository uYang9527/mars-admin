package com.mars.admin;

import com.mars.admin.framework.anno.MarsAdminStarter;
import org.springframework.boot.SpringApplication;

/**
 * 启动类
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-07-05
 */
@MarsAdminStarter
public class MarsAdminApplication {


    public static void main(String[] args) {
        SpringApplication.run(MarsAdminApplication.class, args);
    }
}
