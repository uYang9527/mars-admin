package com.mars.admin.service.impl;

import com.mars.admin.entity.SysOssConfig;
import com.mars.admin.framework.oss.factory.FileUploadStrategyFactory;
import com.mars.admin.framework.oss.strategy.FileUploadStrategy;
import com.mars.admin.service.ISysOssConfigService;
import com.mars.admin.service.impl.BaseServiceImpl;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mars.admin.entity.table.SysOssConfigTableDef.SYS_OSS_CONFIG;

/**
 * OSS配置 服务实现类
 * 继承 BaseServiceImpl 获得更多便捷方法
 *
 * @author Mars.wq
 */
@Slf4j
@Service
public class SysOssConfigServiceImpl extends BaseServiceImpl<SysOssConfig> implements ISysOssConfigService {

    @Autowired
    private FileUploadStrategyFactory strategyFactory;

    @Override
    public Page<SysOssConfig> selectConfigPage(Page<SysOssConfig> page, QueryWrapper queryWrapper) {
        return this.page(page, queryWrapper);
    }

    @Override
    public SysOssConfig selectConfigByKey(String configKey) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(SYS_OSS_CONFIG.CONFIG_KEY.eq(configKey))
                .and(SYS_OSS_CONFIG.IS_DELETED.eq(0));
        return this.getOne(queryWrapper);
    }

    @Override
    public List<SysOssConfig> selectEnabledConfigs() {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(SYS_OSS_CONFIG.STATUS.eq(1))
                .and(SYS_OSS_CONFIG.IS_DELETED.eq(0))
                .orderBy(SYS_OSS_CONFIG.CREATE_TIME.desc());
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveConfig(SysOssConfig config) {
        // 检查配置key是否重复
        SysOssConfig existing = selectConfigByKey(config.getConfigKey());
        if (existing != null) {
            throw new RuntimeException("配置key已存在: " + config.getConfigKey());
        }
        return this.save(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateConfig(SysOssConfig config) {
        // 检查配置key是否重复（排除自己）
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(SYS_OSS_CONFIG.CONFIG_KEY.eq(config.getConfigKey()))
                .and(SYS_OSS_CONFIG.ID.ne(config.getId()))
                .and(SYS_OSS_CONFIG.IS_DELETED.eq(0));
        
        SysOssConfig existing = this.getOne(queryWrapper);
        if (existing != null) {
            throw new RuntimeException("配置key已存在: " + config.getConfigKey());
        }
        
        return this.updateById(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteConfig(Long id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteConfigs(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long id, Integer status) {
        SysOssConfig config = new SysOssConfig();
        config.setId(id);
        config.setStatus(status);
        return this.updateById(config);
    }

    @Override
    public boolean testConnection(SysOssConfig config) {
        try {
            // 根据配置类型获取对应的策略
            FileUploadStrategy strategy = strategyFactory.getStrategy(config.getConfigKey());
            
            // 这里可以实现具体的连接测试逻辑
            // 比如创建一个测试文件上传，然后删除
            log.info("测试OSS配置连接: {}", config.getConfigKey());
            
            // 简单的测试，实际项目中可以上传一个小的测试文件
            return true;
        } catch (Exception e) {
            log.error("测试OSS配置连接失败: {}", config.getConfigKey(), e);
            return false;
        }
    }
} 