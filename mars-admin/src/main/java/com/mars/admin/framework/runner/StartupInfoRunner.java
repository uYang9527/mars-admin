package com.mars.admin.framework.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 应用启动完成后打印系统信息
 *
 * @author 程序员Mars
 * @version 1.0
 * @date 2025-01-16
 */
@Slf4j
@Component
@Order(1) // 优先执行，显示启动信息
public class StartupInfoRunner implements ApplicationRunner {

    @Value("${server.port:8080}")
    private String port;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Value("${startup.info.mode:full}")
    private String infoMode;

    @Value("${startup.info.show-banner:true}")
    private boolean showBanner;

    private final Environment environment;

    public StartupInfoRunner(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 根据配置决定是否显示启动信息
        if (!"none".equals(infoMode)) {
            printStartupInfo();
        }
    }

    private void printStartupInfo() {
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            String applicationName = environment.getProperty("spring.application.name", "Mars-Admin");
            
            // 处理context-path
            String contextPathStr = contextPath == null || contextPath.isEmpty() ? "" : contextPath;
            if (!contextPathStr.isEmpty() && !contextPathStr.startsWith("/")) {
                contextPathStr = "/" + contextPathStr;
            }


            
            System.out.println("🚀 " + applicationName + " 启动成功！");
            System.out.printf("📋 当前环境:     %s%n", activeProfile);
            System.out.printf("🌐 本地访问地址: http://localhost:%s%s%n", port, contextPathStr);
            System.out.printf("🌐 外部访问地址: http://%s:%s%s%n", hostAddress, port, contextPathStr);
            
            // 根据模式显示详细信息
            if ("full".equals(infoMode)) {
                System.out.println();
                System.out.println("📚 接口文档地址:");
                System.out.printf("   ├─ Knife4j文档:  http://localhost:%s%sdoc.html%n", port, contextPathStr);
                System.out.printf("   ├─ Swagger文档:  http://localhost:%s%sswagger-ui.html%n", port, contextPathStr);
                System.out.printf("   └─ OpenAPI规范:  http://localhost:%s%sv3/api-docs%n", port, contextPathStr);
                
                System.out.println();
                System.out.println("🔧 系统监控地址:");
                System.out.printf("   ├─ Druid监控:    http://localhost:%s%sdruid%n", port, contextPathStr);
                System.out.printf("   └─ SQL日志:      http://localhost:%s%ssqlLog.html%n", port, contextPathStr);


                System.out.println();
                System.out.println("==================================================================================");
            } else if ("simple".equals(infoMode)) {
                System.out.println();
                System.out.printf("📚 接口文档:     http://localhost:%s%sdoc.html%n", port, contextPathStr);
                System.out.printf("🔧 监控面板:     http://localhost:%s%sdruid%n", port, contextPathStr);
                System.out.println("==================================================================================");
            }
            
        } catch (UnknownHostException e) {
            log.error("获取主机地址失败", e);
        }
    }
} 