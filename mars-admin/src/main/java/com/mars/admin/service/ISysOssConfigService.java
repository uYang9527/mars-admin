package com.mars.admin.service;

import com.mars.admin.entity.SysOssConfig;
import com.mars.admin.service.BaseService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;

import java.util.List;

/**
 * OSS配置 服务类
 *
 * @author Mars.wq
 */
public interface ISysOssConfigService extends BaseService<SysOssConfig> {

    /**
     * 分页查询OSS配置列表
     *
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return 分页结果
     */
    Page<SysOssConfig> selectConfigPage(Page<SysOssConfig> page, QueryWrapper queryWrapper);

    /**
     * 根据配置key获取配置
     *
     * @param configKey 配置key
     * @return OSS配置
     */
    SysOssConfig selectConfigByKey(String configKey);

    /**
     * 获取启用的配置列表
     *
     * @return 启用的配置列表
     */
    List<SysOssConfig> selectEnabledConfigs();

    /**
     * 保存OSS配置
     *
     * @param config OSS配置
     * @return 是否成功
     */
    boolean saveConfig(SysOssConfig config);

    /**
     * 更新OSS配置
     *
     * @param config OSS配置
     * @return 是否成功
     */
    boolean updateConfig(SysOssConfig config);

    /**
     * 删除OSS配置
     *
     * @param id 配置ID
     * @return 是否成功
     */
    boolean deleteConfig(Long id);

    /**
     * 批量删除OSS配置
     *
     * @param ids 配置ID列表
     * @return 是否成功
     */
    boolean deleteConfigs(List<Long> ids);

    /**
     * 更新配置状态
     *
     * @param id 配置ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 测试配置连接
     *
     * @param config OSS配置
     * @return 测试结果
     */
    boolean testConnection(SysOssConfig config);
} 