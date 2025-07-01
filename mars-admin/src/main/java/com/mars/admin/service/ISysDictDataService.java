package com.mars.admin.service;

import com.mars.admin.entity.SysDictData;
import com.mybatisflex.core.paginate.Page;

import java.util.List;

/**
 * 系统字典数据Service接口
 * 继承 BaseService 获得更多便捷方法
 *
 * @author Mars
 */
public interface ISysDictDataService extends BaseService<SysDictData> {

    /**
     * 根据字典类型查询字典数据列表
     *
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    List<SysDictData> selectByDictType(String dictType);

    /**
     * 根据字典类型和字典值查询字典数据
     *
     * @param dictType 字典类型
     * @param dictValue 字典值
     * @return 字典数据
     */
    SysDictData selectByDictTypeAndValue(String dictType, String dictValue);

    /**
     * 分页查询字典数据列表
     *
     * @param page 分页参数
     * @param dictData 查询条件
     * @return 字典数据分页列表
     */
    Page<SysDictData> selectDictDataPage(Page<SysDictData> page, SysDictData dictData);

    /**
     * 查询所有正常状态的字典数据
     *
     * @return 字典数据列表
     */
    List<SysDictData> selectNormalDictData();

    /**
     * 根据字典类型查询正常状态的字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    List<SysDictData> selectNormalDictDataByType(String dictType);

    /**
     * 根据ID查询字典数据（使用缓存）
     *
     * @param id 字典数据ID
     * @return 字典数据信息
     */
    SysDictData getDictDataById(Long id);

    /**
     * 新增字典数据
     *
     * @param dictData 字典数据信息
     * @return 新增结果
     */
    boolean insertDictData(SysDictData dictData);

    /**
     * 修改字典数据
     *
     * @param dictData 字典数据信息
     * @return 修改结果
     */
    boolean updateDictData(SysDictData dictData);

    /**
     * 删除字典数据
     *
     * @param dictDataIds 字典数据ID数组
     * @return 删除结果
     */
    boolean deleteDictData(Long[] dictDataIds);

    /**
     * 根据字典类型删除字典数据
     *
     * @param dictType 字典类型
     * @return 删除结果
     */
    boolean deleteDictDataByType(String dictType);

    /**
     * 校验字典值是否唯一
     *
     * @param dictType 字典类型
     * @param dictValue 字典值
     * @param dictDataId 字典数据ID（修改时传入）
     * @return 是否唯一
     */
    boolean checkDictValueUnique(String dictType, String dictValue, Long dictDataId);

    /**
     * 刷新字典数据缓存
     */
    void refreshCache();
} 