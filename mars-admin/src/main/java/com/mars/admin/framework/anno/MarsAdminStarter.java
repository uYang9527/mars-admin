package com.mars.admin.framework.anno;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableAsync
@EnableCaching
@SpringBootApplication
@EnableConfigurationProperties
@MapperScan("com.mars.admin.mapper")
public @interface MarsAdminStarter {
}
