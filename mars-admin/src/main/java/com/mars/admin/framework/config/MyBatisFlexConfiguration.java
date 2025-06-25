package com.mars.admin.framework.config;

import com.mars.admin.framework.listener.EntityLogicDeleteListener;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.logicdelete.LogicDeleteProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 功能描述
 *
 * @author Mars.wq [wqexpore@163.com]
 * @version 1.0
 * @date 2025-06-02 14:39:44
 */
@Slf4j
@Configuration
public class MyBatisFlexConfiguration {


    public MyBatisFlexConfiguration() {
        // 开启审计功能
        AuditManager.setAuditEnable(true);

        // 设置 SQL 审计收集器
        AuditManager.setMessageCollector(auditMessage ->
                log.info("{},{}ms", auditMessage.getFullSql()
                        , auditMessage.getElapsedTime())
        );
    }



    @Bean
    public LogicDeleteProcessor logicDeleteProcessor() {
        return new EntityLogicDeleteListener();
    }



}
