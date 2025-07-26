package com.mars.admin.modules.system.mapper;

import com.mars.admin.modules.base.mapper.BasePlusMapper;
import com.mars.admin.modules.system.entity.SysDictType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统字典类型Mapper接口
 * 继承 BasePlusMapper 获得更多便捷方法
 *
 * @author Mars
 */
@Mapper
public interface SysDictTypeMapper extends BasePlusMapper<SysDictType> {

    /**
     * 根据字典类型查询字典类型
     *
     * @param dictType 字典类型
     * @return 字典类型信息
     */
    default SysDictType selectByDictType(String dictType) {
        return selectOneByField("dict_type", dictType);
    }

    /**
     * 分页查询字典类型列表
     *
     * @param dictType 查询条件
     * @return 字典类型列表
     */
    List<SysDictType> selectDictTypeList(SysDictType dictType);

    /**
     * 查询字典类型详情（包含字典数据）
     *
     * @param dictTypeId 字典类型ID
     * @return 字典类型详情
     */
    SysDictType selectDictTypeDetailById(@Param("dictTypeId") Long dictTypeId);

    /**
     * 查询所有正常状态的字典类型
     *
     * @return 字典类型列表
     */
    @Select("SELECT * FROM sys_dict_type WHERE status = 1 AND is_deleted = 0 ORDER BY create_time DESC")
    List<SysDictType> selectNormalDictTypes();

    /**
     * 根据字典名称查询字典类型
     *
     * @param dictName 字典名称
     * @return 字典类型信息
     */
    default SysDictType selectByDictName(String dictName) {
        return selectOneByField("dict_name", dictName);
    }

    /**
     * 根据状态查询字典类型列表
     *
     * @param status 状态
     * @return 字典类型列表
     */
    default List<SysDictType> selectByStatus(Integer status) {
        return selectListByField("status", status);
    }

    /**
     * 检查字典类型是否存在
     *
     * @param dictType 字典类型
     * @return 是否存在
     */
    default boolean existsByDictType(String dictType) {
        return existsByField("dict_type", dictType);
    }

    /**
     * 检查字典名称是否存在
     *
     * @param dictName 字典名称
     * @return 是否存在
     */
    default boolean existsByDictName(String dictName) {
        return existsByField("dict_name", dictName);
    }
}
