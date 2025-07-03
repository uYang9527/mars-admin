package com.mars.admin.service;

import com.mars.admin.entity.SysConfig;
import com.mybatisflex.core.paginate.Page;

import java.util.List;

/**
 * 系统配置Service接口
 * 继承 BaseService 获得更多便捷方法
 *
 * @author Mars
 */
public interface ISysConfigService extends BaseService<SysConfig> {

    /**
     * 根据配置键名查询配置
     *
     * @param configKey 配置键名
     * @return 配置信息
     */
    SysConfig selectByConfigKey(String configKey);

    /**
     * 分页查询配置列表
     *
     * @param page 分页参数
     * @param config 查询条件
     * @return 配置分页列表
     */
    Page<SysConfig> selectConfigPage(Page<SysConfig> page, SysConfig config);

    /**
     * 查询所有配置
     *
     * @return 配置列表
     */
    List<SysConfig> selectAllConfigs();

    /**
     * 根据配置类型查询配置列表
     *
     * @param configType 配置类型
     * @return 配置列表
     */
    List<SysConfig> selectByConfigType(String configType);

    /**
     * 新增配置
     *
     * @param config 配置信息
     * @return 新增结果
     */
    boolean insertConfig(SysConfig config);

    /**
     * 修改配置
     *
     * @param config 配置信息
     * @return 修改结果
     */
    boolean updateConfig(SysConfig config);

    /**
     * 删除配置
     *
     * @param configIds 配置ID数组
     * @return 删除结果
     */
    boolean deleteConfigs(Long[] configIds);

    /**
     * 校验配置键名是否唯一
     *
     * @param configKey 配置键名
     * @param configId 配置ID（修改时传入）
     * @return 是否唯一
     */
    boolean checkConfigKeyUnique(String configKey, Long configId);

    /**
     * 根据配置键名获取配置值
     *
     * @param configKey 配置键名
     * @return 配置值
     */
    String getConfigValueByKey(String configKey);

    /**
     * 更新配置值
     *
     * @param configKey 配置键名
     * @param configValue 配置值
     * @return 更新结果
     */
    boolean updateConfigValueByKey(String configKey, String configValue);

    /**
     * 刷新参数缓存
     */
    void refreshCache();

    /**
     *  批量删除
     * @param ids 删除的ID列表
     * @return 删除结果
     */
    boolean removeBatchByIds(List<Long> ids);
}