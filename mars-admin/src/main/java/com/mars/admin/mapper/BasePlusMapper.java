package com.mars.admin.mapper;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;

import java.io.Serializable;
import java.util.List;

/**
 * 增强版 BaseMapper
 * 在 MyBatis-Flex 的 BaseMapper 基础上提供更多便捷方法
 *
 * @param <T> 实体类型
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-06-23 17:35:08
 */
public interface BasePlusMapper<T> extends BaseMapper<T> {

    /**
     * 根据字段名和值查询单条记录
     *
     * @param fieldName 字段名
     * @param value     字段值
     * @return 单条记录
     */
    default T selectOneByField(String fieldName, Object value) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(fieldName + " = ?", value);
        return selectOneByQuery(queryWrapper);
    }

    /**
     * 根据字段名和值查询记录列表
     *
     * @param fieldName 字段名
     * @param value     字段值
     * @return 记录列表
     */
    default List<T> selectListByField(String fieldName, Object value) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(fieldName + " = ?", value);
        return selectListByQuery(queryWrapper);
    }

    /**
     * 根据字段名和值查询记录数量
     *
     * @param fieldName 字段名
     * @param value     字段值
     * @return 记录数量
     */
    default long selectCountByField(String fieldName, Object value) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(fieldName + " = ?", value);
        return selectCountByQuery(queryWrapper);
    }

    /**
     * 根据字段名和值判断记录是否存在
     *
     * @param fieldName 字段名
     * @param value     字段值
     * @return 是否存在
     */
    default boolean existsByField(String fieldName, Object value) {
        return selectCountByField(fieldName, value) > 0;
    }

    /**
     * 根据字段名和值删除记录
     *
     * @param fieldName 字段名
     * @param value     字段值
     * @return 删除的记录数
     */
    default int deleteByField(String fieldName, Object value) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(fieldName + " = ?", value);
        return deleteByQuery(queryWrapper);
    }

    /**
     * 根据字段名和值列表查询记录
     *
     * @param fieldName 字段名
     * @param values    字段值列表
     * @return 记录列表
     */
    default List<T> selectListByFieldIn(String fieldName, List<?> values) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(fieldName + " IN (?)", values);
        return selectListByQuery(queryWrapper);
    }

    /**
     * 根据字段名和值列表删除记录
     *
     * @param fieldName 字段名
     * @param values    字段值列表
     * @return 删除的记录数
     */
    default int deleteByFieldIn(String fieldName, List<?> values) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(fieldName + " IN (?)", values);
        return deleteByQuery(queryWrapper);
    }

    /**
     * 分页查询所有记录
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    default Page<T> selectPage(int pageNum, int pageSize) {
        return paginate(Page.of(pageNum, pageSize), QueryWrapper.create());
    }

    /**
     * 根据字段名和值分页查询
     *
     * @param fieldName 字段名
     * @param value     字段值
     * @param pageNum   页码
     * @param pageSize  每页大小
     * @return 分页结果
     */
    default Page<T> selectPageByField(String fieldName, Object value, int pageNum, int pageSize) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(fieldName + " = ?", value);
        return paginate(Page.of(pageNum, pageSize), queryWrapper);
    }

    /**
     * 模糊查询
     *
     * @param fieldName 字段名
     * @param value     字段值（会自动添加 % 符号）
     * @return 记录列表
     */
    default List<T> selectListByFieldLike(String fieldName, String value) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(fieldName + " LIKE ?", "%" + value + "%");
        return selectListByQuery(queryWrapper);
    }

    /**
     * 范围查询
     *
     * @param fieldName 字段名
     * @param minValue  最小值
     * @param maxValue  最大值
     * @return 记录列表
     */
    default List<T> selectListByFieldBetween(String fieldName, Object minValue, Object maxValue) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(fieldName + " BETWEEN ? AND ?", minValue, maxValue);
        return selectListByQuery(queryWrapper);
    }

    /**
     * 根据字段排序查询所有记录
     *
     * @param fieldName 排序字段名
     * @param isAsc     是否升序
     * @return 记录列表
     */
    default List<T> selectListOrderBy(String fieldName, boolean isAsc) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        if (isAsc) {
            queryWrapper.orderBy(fieldName + " ASC");
        } else {
            queryWrapper.orderBy(fieldName + " DESC");
        }
        return selectListByQuery(queryWrapper);
    }

    /**
     * 查询最新的 N 条记录（按 ID 倒序）
     *
     * @param limit 记录数量
     * @return 记录列表
     */
    default List<T> selectLatest(int limit) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .orderBy("id DESC")
                .limit(limit);
        return selectListByQuery(queryWrapper);
    }
} 