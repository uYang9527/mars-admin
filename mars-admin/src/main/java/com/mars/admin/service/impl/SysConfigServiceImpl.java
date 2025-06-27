package com.mars.admin.service.impl;

import com.mars.admin.entity.SysConfig;
import com.mars.admin.mapper.SysConfigMapper;
import com.mars.admin.service.ISysConfigService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static com.mars.admin.entity.table.SysConfigTableDef.SYS_CONFIG;

/**
 * 系统配置Service实现类
 * 继承 BaseServiceImpl 获得更多便捷方法
 *
 * @author Mars
 */
@Service
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfig> implements ISysConfigService {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Override
    public SysConfig selectByConfigKey(String configKey) {
        return sysConfigMapper.selectByConfigKey(configKey);
    }

    @Override
    public Page<SysConfig> selectConfigPage(Page<SysConfig> page, SysConfig config) {
        QueryWrapper query = QueryWrapper.create()
                .select()
                .from(SYS_CONFIG)
                .where(SYS_CONFIG.IS_DELETED.eq(0));

        if (StringUtils.hasText(config.getConfigName())) {
            query.and(SYS_CONFIG.CONFIG_NAME.like(config.getConfigName()));
        }
        if (StringUtils.hasText(config.getConfigKey())) {
            query.and(SYS_CONFIG.CONFIG_KEY.like(config.getConfigKey()));
        }
        if (StringUtils.hasText(config.getConfigType())) {
            query.and(SYS_CONFIG.CONFIG_TYPE.eq(config.getConfigType()));
        }

        query.orderBy(SYS_CONFIG.CREATE_TIME.desc());

        return this.page(page, query);
    }

    @Override
    public List<SysConfig> selectAllConfigs() {
        return sysConfigMapper.selectAllConfigs();
    }

    @Override
    public List<SysConfig> selectByConfigType(String configType) {
        return sysConfigMapper.selectByConfigType(configType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertConfig(SysConfig config) {
        return this.save(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateConfig(SysConfig config) {
        return this.updateById(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteConfigs(Long[] configIds) {
        return this.removeByIds(Arrays.asList(configIds));
    }

    @Override
    public boolean checkConfigKeyUnique(String configKey, Long configId) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_CONFIG.CONFIG_KEY.eq(configKey))
                .and(SYS_CONFIG.IS_DELETED.eq(0));

        if (configId != null) {
            query.and(SYS_CONFIG.ID.ne(configId));
        }

        return this.count(query) == 0;
    }

    @Override
    public String getConfigValueByKey(String configKey) {
        SysConfig config = selectByConfigKey(configKey);
        return config != null ? config.getConfigValue() : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateConfigValueByKey(String configKey, String configValue) {
        SysConfig config = selectByConfigKey(configKey);
        if (config != null) {
            config.setConfigValue(configValue);
            return updateConfig(config);
        }
        return false;
    }
} 