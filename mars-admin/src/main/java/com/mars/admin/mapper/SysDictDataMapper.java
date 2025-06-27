package com.mars.admin.mapper;

import com.mars.admin.entity.SysDictData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统字典数据Mapper接口
 * 继承 BasePlusMapper 获得更多便捷方法
 *
 * @author Mars
 */
@Mapper
public interface SysDictDataMapper extends BasePlusMapper<SysDictData> {

    /**
     * 根据字典类型查询字典数据列表
     *
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    @Select("SELECT * FROM sys_dict_data WHERE dict_type = #{dictType} AND is_deleted = 0 ORDER BY dict_sort")
    List<SysDictData> selectByDictType(@Param("dictType") String dictType);

    /**
     * 根据字典类型和字典值查询字典数据
     *
     * @param dictType 字典类型
     * @param dictValue 字典值
     * @return 字典数据
     */
    @Select("SELECT * FROM sys_dict_data WHERE dict_type = #{dictType} AND dict_value = #{dictValue} AND is_deleted = 0")
    SysDictData selectByDictTypeAndValue(@Param("dictType") String dictType, @Param("dictValue") String dictValue);

    /**
     * 分页查询字典数据列表
     *
     * @param dictData 查询条件
     * @return 字典数据列表
     */
    List<SysDictData> selectDictDataList(SysDictData dictData);

    /**
     * 查询所有正常状态的字典数据
     *
     * @return 字典数据列表
     */
    @Select("SELECT * FROM sys_dict_data WHERE status = 1 AND is_deleted = 0 ORDER BY dict_type, dict_sort")
    List<SysDictData> selectNormalDictData();

    /**
     * 根据字典类型查询正常状态的字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    @Select("SELECT * FROM sys_dict_data WHERE dict_type = #{dictType} AND status = 1 AND is_deleted = 0 ORDER BY dict_sort")
    List<SysDictData> selectNormalDictDataByType(@Param("dictType") String dictType);

    /**
     * 根据字典类型查询字典数据列表（便捷方法）
     *
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    default List<SysDictData> selectListByDictType(String dictType) {
        return selectListByField("dict_type", dictType);
    }

    /**
     * 根据字典值查询字典数据
     *
     * @param dictValue 字典值
     * @return 字典数据
     */
    default SysDictData selectByDictValue(String dictValue) {
        return selectOneByField("dict_value", dictValue);
    }

    /**
     * 根据状态查询字典数据列表
     *
     * @param status 状态
     * @return 字典数据列表
     */
    default List<SysDictData> selectByStatus(Integer status) {
        return selectListByField("status", status);
    }

    /**
     * 检查字典值是否存在
     *
     * @param dictValue 字典值
     * @return 是否存在
     */
    default boolean existsByDictValue(String dictValue) {
        return existsByField("dict_value", dictValue);
    }
} 