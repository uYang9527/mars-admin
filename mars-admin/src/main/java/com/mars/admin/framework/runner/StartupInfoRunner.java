package com.mars.admin.framework.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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
public class StartupInfoRunner implements ApplicationRunner {

    @Value("${server.port:8080}")
    private String port;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    private final Environment environment;

    public StartupInfoRunner(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        printStartupInfo();
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

            System.out.println();
            System.out.println("==================================================================================");
            System.out.println("                                                                                  ");
            System.out.println("    ███╗   ███╗ █████╗ ██████╗ ███████╗      █████╗ ██████╗ ███╗   ███╗██╗███╗   ██╗");
            System.out.println("    ████╗ ████║██╔══██╗██╔══██╗██╔════╝     ██╔══██╗██╔══██╗████╗ ████║██║████╗  ██║");
            System.out.println("    ██╔████╔██║███████║██████╔╝███████╗     ███████║██║  ██║██╔████╔██║██║██╔██╗ ██║");
            System.out.println("    ██║╚██╔╝██║██╔══██║██╔══██╗╚════██║     ██╔══██║██║  ██║██║╚██╔╝██║██║██║╚██╗██║");
            System.out.println("    ██║ ╚═╝ ██║██║  ██║██║  ██║███████║     ██║  ██║██████╔╝██║ ╚═╝ ██║██║██║ ╚████║");
            System.out.println("    ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝     ╚═╝  ╚═╝╚═════╝ ╚═╝     ╚═╝╚═╝╚═╝  ╚═══╝");
            System.out.println("                                                                                  ");
            System.out.println("==================================================================================");
            System.out.println();
            
            log.info("🚀 {} 启动成功！", applicationName);
            log.info("📋 当前环境: {}", activeProfile);
            log.info("🌐 本地访问地址: http://localhost:{}{}", port, contextPathStr);
            log.info("🌐 外部访问地址: http://{}:{}{}", hostAddress, port, contextPathStr);
            
            System.out.println();
            System.out.println("📚 接口文档地址:");
            System.out.printf("   ├─ Knife4j文档:  http://localhost:%s%s/doc.html%n", port, contextPathStr);
            System.out.printf("   ├─ Swagger文档:  http://localhost:%s%s/swagger-ui.html%n", port, contextPathStr);
            System.out.printf("   └─ OpenAPI规范:  http://localhost:%s%s/v3/api-docs%n", port, contextPathStr);
            
            System.out.println();
            System.out.println("🔧 系统监控地址:");
            System.out.printf("   ├─ Druid监控:    http://localhost:%s%s/druid%n", port, contextPathStr);
            System.out.printf("   ├─ SQL日志:      http://localhost:%s%s/sqlLog.html%n", port, contextPathStr);
            System.out.printf("   └─ 健康检查:     http://localhost:%s%s/actuator/health%n", port, contextPathStr);
            
            System.out.println();
            System.out.println("🔐 多端登录支持:");
            System.out.println("   ├─ PC端登录:     POST /auth/login (loginType: pc)");
            System.out.println("   ├─ 小程序登录:   POST /auth/login (loginType: miniprogram)");
            System.out.println("   ├─ APP登录:      POST /auth/login (loginType: app)");
            System.out.println("   └─ 发送验证码:   POST /auth/sendCaptcha?mobile=手机号");
            
            System.out.println();
            System.out.println("💡 快速测试:");
            System.out.printf("   curl -X POST http://localhost:%s%s/auth/login \\%n", port, contextPathStr);
            System.out.println("     -H \"Content-Type: application/json\" \\");
            System.out.println("     -d '{\"loginType\":\"pc\",\"username\":\"admin\",\"password\":\"123456\"}'");
            
            System.out.println();
            System.out.println("==================================================================================");
            
        } catch (UnknownHostException e) {
            log.error("获取主机地址失败", e);
        }
    }
} 