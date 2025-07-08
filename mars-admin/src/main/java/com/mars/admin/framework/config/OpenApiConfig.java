package com.mars.admin.framework.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 3 配置类
 *
 * @author 程序员Mars
 * @version 1.0
 * @date 2025-07-05
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mars Admin API")
                        .description("中小企业快速开发脚手架 API 文档")
                        .version("2.0.0")
                        .contact(new Contact()
                                .name("Mars.wq")
                                .email("wqexpore@163.com")
                                .url("https://github.com/mars-wq"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
} 