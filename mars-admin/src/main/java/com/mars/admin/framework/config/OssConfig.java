package com.mars.admin.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * OSS配置类
 *
 * @author Mars.wq
 */
@Slf4j
@Configuration
public class OssConfig {

    @Value("${file.upload.path:./upload}")
    private String uploadPath;

    @Value("${file.upload.default-config:local}")
    private String defaultConfigKey;

    @Value("${file.upload.max-size:10485760}")
    private long maxFileSize;

    @Value("${file.upload.allowed-types:jpg,jpeg,png,gif,bmp,doc,docx,xls,xlsx,ppt,pptx,pdf,txt,zip,rar}")
    private String allowedTypes;

    public String getUploadPath() {
        return uploadPath;
    }

    public String getDefaultConfigKey() {
        return defaultConfigKey;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public String getAllowedTypes() {
        return allowedTypes;
    }

    public String[] getAllowedTypesArray() {
        if (allowedTypes == null || allowedTypes.trim().isEmpty()) {
            return new String[0];
        }
        return allowedTypes.split(",");
    }
} 