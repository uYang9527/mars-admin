package com.mars.admin.mapper;

import com.mars.admin.entity.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统部门Mapper接口
 * 继承 BasePlusMapper 获得更多便捷方法
 *
 * @author Mars
 */
@Mapper
public interface SysDeptMapper extends BasePlusMapper<SysDept> {

    /**
     * 根据部门编码查询部门
     *
     * @param deptCode 部门编码
     * @return 部门信息
     */
    default SysDept selectByDeptCode(String deptCode) {
        return selectOneByField("dept_code", deptCode);
    }

    /**
     * 分页查询部门列表
     *
     * @param dept 查询条件
     * @return 部门列表
     */
    List<SysDept> selectDeptList(SysDept dept);

    /**
     * 根据用户ID查询部门列表
     *
     * @param userId 用户ID
     * @return 部门列表
     */
    List<SysDept> selectDeptsByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查询部门列表
     *
     * @param roleId 角色ID
     * @return 部门列表
     */
    List<SysDept> selectDeptsByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询所有正常状态的部门
     *
     * @return 部门列表
     */
    @Select("SELECT * FROM sys_dept WHERE status = 1 AND is_deleted = 0 ORDER BY parent_id, order_num")
    List<SysDept> selectNormalDepts();

    /**
     * 根据父部门ID查询子部门列表
     *
     * @param parentId 父部门ID
     * @return 子部门列表
     */
    @Select("SELECT * FROM sys_dept WHERE parent_id = #{parentId} AND is_deleted = 0 ORDER BY order_num")
    List<SysDept> selectChildrenByParentId(@Param("parentId") Long parentId);

    /**
     * 查询部门树
     *
     * @return 部门树列表
     */
    List<SysDept> selectDeptTree();

    /**
     * 根据祖级列表查询部门列表
     *
     * @param ancestors 祖级列表
     * @return 部门列表
     */
    @Select("SELECT * FROM sys_dept WHERE ancestors LIKE CONCAT('%', #{ancestors}, '%') AND is_deleted = 0")
    List<SysDept> selectDeptsByAncestors(@Param("ancestors") String ancestors);

    /**
     * 根据父部门ID查询子部门列表（便捷方法）
     *
     * @param parentId 父部门ID
     * @return 子部门列表
     */
    default List<SysDept> selectByParentId(Long parentId) {
        return selectListByField("parent_id", parentId);
    }

    /**
     * 根据状态查询部门列表
     *
     * @param status 状态
     * @return 部门列表
     */
    default List<SysDept> selectByStatus(Integer status) {
        return selectListByField("status", status);
    }

    /**
     * 根据部门名称查询部门
     *
     * @param deptName 部门名称
     * @return 部门信息
     */
    default SysDept selectByDeptName(String deptName) {
        return selectOneByField("dept_name", deptName);
    }

    /**
     * 检查部门编码是否存在
     *
     * @param deptCode 部门编码
     * @return 是否存在
     */
    default boolean existsByDeptCode(String deptCode) {
        return existsByField("dept_code", deptCode);
    }

    /**
     * 检查部门名称是否存在
     *
     * @param deptName 部门名称
     * @return 是否存在
     */
    default boolean existsByDeptName(String deptName) {
        return existsByField("dept_name", deptName);
    }
} 