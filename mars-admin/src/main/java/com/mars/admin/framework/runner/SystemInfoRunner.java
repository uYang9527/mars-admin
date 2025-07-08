package com.mars.admin.framework.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 系统信息打印器（在StartupInfoRunner之后执行）
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-01-16
 */
@Slf4j
@Component
@Order(2) // 在StartupInfoRunner之后执行
public class SystemInfoRunner implements ApplicationRunner {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Value("${startup.info.show-system-info:true}")
    private boolean showSystemInfo;

    private final Environment environment;
    private final DataSource dataSource;

    public SystemInfoRunner(Environment environment, DataSource dataSource) {
        this.environment = environment;
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 根据配置决定是否显示系统信息
        if (showSystemInfo) {
            printSystemInfo();
        }
    }

    private void printSystemInfo() {
        System.out.println();
        System.out.println("📊 系统运行信息:");
        
        // 启动时间
        String startupTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.printf("   ├─ 启动时间:     %s%n", startupTime);
        
        // JVM信息
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;
        
        System.out.printf("   ├─ JVM版本:      %s%n", System.getProperty("java.version"));
        System.out.printf("   ├─ 最大内存:     %d MB%n", maxMemory);
        System.out.printf("   ├─ 已用内存:     %d MB%n", usedMemory);
        System.out.printf("   ├─ 空闲内存:     %d MB%n", freeMemory);
        
        // 操作系统信息
        System.out.printf("   ├─ 操作系统:     %s %s%n", 
            System.getProperty("os.name"), 
            System.getProperty("os.version"));
        
        // 数据库连接信息
        try (Connection connection = dataSource.getConnection()) {
            String dbUrl = connection.getMetaData().getURL();
            String dbProductName = connection.getMetaData().getDatabaseProductName();
            String dbVersion = connection.getMetaData().getDatabaseProductVersion();
            System.out.printf("   ├─ 数据库:       %s %s%n", dbProductName, dbVersion);
            System.out.printf("   ├─ 连接地址:     %s%n", dbUrl);
        } catch (Exception e) {
            System.out.printf("   ├─ 数据库:       连接失败 (%s)%n", e.getMessage());
        }
        
        // Redis信息
        String redisHost = environment.getProperty("spring.data.redis.host", "localhost");
        String redisPort = environment.getProperty("spring.data.redis.port", "6379");
        String redisDatabase = environment.getProperty("spring.data.redis.database", "0");
        System.out.printf("   ├─ Redis:        %s:%s (DB:%s)%n", redisHost, redisPort, redisDatabase);
        
        // 配置文件信息
        System.out.printf("   └─ 配置环境:     %s%n", activeProfile);
        
        System.out.println();
        System.out.println("⚠️  重要提示:");
        System.out.println("   ├─ 默认管理员账号: admin / 123456");
        System.out.println("   ├─ Druid监控账号:  admin / marsadmin");
        System.out.println("   ├─ 生产环境请及时修改默认密码");
        System.out.println("   └─ 详细文档请查看: docs/multi-login-strategy.md");
        
        System.out.println();
        System.out.println("==================================================================================");
        System.out.println();
    }
} 