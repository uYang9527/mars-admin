package com.mars.admin.framework.config;

import com.mars.admin.modules.system.entity.SysOssConfig;
import com.mars.admin.modules.system.service.ISysOssConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * OSS配置加载器
 * 在应用启动时加载OSS配置信息
 *
 * @author Mars.wq
 */
@Slf4j
@Component
public class OssConfigLoader implements ApplicationRunner {

    @Autowired
    private ISysOssConfigService ossConfigService;

    @Override
    @Async
    public void run(ApplicationArguments args) throws Exception {

        try {
            // 获取所有启用的OSS配置
            List<SysOssConfig> configs = ossConfigService.selectEnabledConfigs();

            log.info("成功加载 {} 个OSS配置:", configs.size());
            for (SysOssConfig config : configs) {
                log.info("- 配置类型: {}, 状态: {}",
                    config.getConfigKey(),
                    config.getStatus() == 1 ? "启用" : "停用");
            }

        } catch (Exception e) {
            log.error("加载OSS配置失败", e);
            log.warn("将使用默认的本地存储配置");
        }

        log.info("OSS配置加载完成");
    }
}
