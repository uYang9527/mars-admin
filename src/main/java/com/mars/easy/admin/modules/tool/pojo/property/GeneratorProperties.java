package com.mars.easy.admin.modules.tool.pojo.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName GeneratorProperty
 * @Author 程序员Mars
 * @Date 2024/1/15 8:38
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "generator")
public class GeneratorProperties {

    {
        moduleName = "sz-service";
        serviceName = "sz-service-admin";
    }

    private PathProperties path;

    private GlobalProperties global;

    private String moduleName;

    private String serviceName;


    @Data
    public static class GlobalProperties {

        {
            author = "Mars";
            packages = "com.mars.easy.admin";
        }

        private String author;

        private String packages;
    }


    @Data
    public static class PathProperties {

        private String web;

        private String api;
    }

}
