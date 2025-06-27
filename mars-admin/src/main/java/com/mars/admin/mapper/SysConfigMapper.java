package com.mars.admin.mapper;

import com.mars.admin.entity.SysConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统配置Mapper接口
 * 继承 BasePlusMapper 获得更多便捷方法
 *
 * @author Mars
 */
@Mapper
public interface SysConfigMapper extends BasePlusMapper<SysConfig> {

    /**
     * 根据配置键名查询配置
     *
     * @param configKey 配置键名
     * @return 配置信息
     */
    default SysConfig selectByConfigKey(String configKey) {
        return selectOneByField("config_key", configKey);
    }

    /**
     * 分页查询配置列表
     *
     * @param config 查询条件
     * @return 配置列表
     */
    List<SysConfig> selectConfigList(SysConfig config);

    /**
     * 查询所有配置
     *
     * @return 配置列表
     */
    @Select("SELECT * FROM sys_config WHERE is_deleted = 0 ORDER BY create_time DESC")
    List<SysConfig> selectAllConfigs();

    /**
     * 根据配置类型查询配置列表
     *
     * @param configType 配置类型
     * @return 配置列表
     */
    default List<SysConfig> selectByConfigType(String configType) {
        return selectListByField("config_type", configType);
    }

    /**
     * 检查配置键名是否存在
     *
     * @param configKey 配置键名
     * @return 是否存在
     */
    default boolean existsByConfigKey(String configKey) {
        return existsByField("config_key", configKey);
    }
} 