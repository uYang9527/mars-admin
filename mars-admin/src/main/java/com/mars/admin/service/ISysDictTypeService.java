package com.mars.admin.service;

import com.mars.admin.entity.SysDictType;
import com.mybatisflex.core.paginate.Page;

import java.util.List;

/**
 * 系统字典类型Service接口
 * 继承 BaseService 获得更多便捷方法
 *
 * @author Mars
 */
public interface ISysDictTypeService extends BaseService<SysDictType> {

    /**
     * 根据字典类型查询字典类型
     *
     * @param dictType 字典类型
     * @return 字典类型信息
     */
    SysDictType selectByDictType(String dictType);

    /**
     * 根据字典名称查询字典类型
     *
     * @param dictName 字典名称
     * @return 字典类型信息
     */
    SysDictType selectByDictName(String dictName);

    /**
     * 分页查询字典类型列表
     *
     * @param page 分页参数
     * @param dictType 查询条件
     * @return 字典类型分页列表
     */
    Page<SysDictType> selectDictTypePage(Page<SysDictType> page, SysDictType dictType);

    /**
     * 查询字典类型详情（包含字典数据）
     *
     * @param dictTypeId 字典类型ID
     * @return 字典类型详情
     */
    SysDictType selectDictTypeDetailById(Long dictTypeId);

    /**
     * 查询所有正常状态的字典类型
     *
     * @return 字典类型列表
     */
    List<SysDictType> selectNormalDictTypes();

    /**
     * 新增字典类型
     *
     * @param dictType 字典类型信息
     * @return 新增结果
     */
    boolean insertDictType(SysDictType dictType);

    /**
     * 修改字典类型
     *
     * @param dictType 字典类型信息
     * @return 修改结果
     */
    boolean updateDictType(SysDictType dictType);

    /**
     * 删除字典类型
     *
     * @param dictTypeIds 字典类型ID数组
     * @return 删除结果
     */
    boolean deleteDictTypes(Long[] dictTypeIds);

    /**
     * 校验字典类型是否唯一
     *
     * @param dictType 字典类型
     * @param dictTypeId 字典类型ID（修改时传入）
     * @return 是否唯一
     */
    boolean checkDictTypeUnique(String dictType, Long dictTypeId);

    /**
     * 校验字典名称是否唯一
     *
     * @param dictName 字典名称
     * @param dictTypeId 字典类型ID（修改时传入）
     * @return 是否唯一
     */
    boolean checkDictNameUnique(String dictName, Long dictTypeId);
} 