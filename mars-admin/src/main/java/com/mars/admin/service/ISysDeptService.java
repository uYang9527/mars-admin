package com.mars.admin.service;

import com.mars.admin.entity.SysDept;
import com.mybatisflex.core.paginate.Page;

import java.util.List;

/**
 * 系统部门Service接口
 * 继承 BaseService 获得更多便捷方法
 *
 * @author Mars
 */
public interface ISysDeptService extends BaseService<SysDept> {

    /**
     * 根据部门编码查询部门
     *
     * @param deptCode 部门编码
     * @return 部门信息
     */
    SysDept selectByDeptCode(String deptCode);

    /**
     * 根据部门名称查询部门
     *
     * @param deptName 部门名称
     * @return 部门信息
     */
    SysDept selectByDeptName(String deptName);

    /**
     * 分页查询部门列表
     *
     * @param page 分页参数
     * @param dept 查询条件
     * @return 部门分页列表
     */
    Page<SysDept> selectDeptPage(Page<SysDept> page, SysDept dept);

    /**
     * 根据用户ID查询部门列表
     *
     * @param userId 用户ID
     * @return 部门列表
     */
    List<SysDept> selectDeptsByUserId(Long userId);

    /**
     * 根据角色ID查询部门列表
     *
     * @param roleId 角色ID
     * @return 部门列表
     */
    List<SysDept> selectDeptsByRoleId(Long roleId);

    /**
     * 查询所有正常状态的部门
     *
     * @return 部门列表
     */
    List<SysDept> selectNormalDepts();

    /**
     * 根据父部门ID查询子部门列表
     *
     * @param parentId 父部门ID
     * @return 子部门列表
     */
    List<SysDept> selectChildrenByParentId(Long parentId);

    /**
     * 查询部门列表
     *
     * @param dept 查询条件
     * @return 部门列表
     */
    List<SysDept> selectDeptList(SysDept dept);

    /**
     * 查询部门树
     *
     * @return 部门树列表
     */
    List<SysDept> selectDeptTree();

    /**
     * 新增部门
     *
     * @param dept 部门信息
     * @return 新增结果
     */
    boolean insertDept(SysDept dept);

    /**
     * 修改部门
     *
     * @param dept 部门信息
     * @return 修改结果
     */
    boolean updateDept(SysDept dept);

    /**
     * 删除单个部门
     *
     * @param deptId 部门ID
     * @return 删除结果
     */
    boolean deleteDeptById(Long deptId);

    /**
     * 删除部门
     *
     * @param deptIds 部门ID数组
     * @return 删除结果
     */
    boolean deleteDepts(Long[] deptIds);

    /**
     * 校验部门编码是否唯一
     *
     * @param deptCode 部门编码
     * @param deptId 部门ID（修改时传入）
     * @return 是否唯一
     */
    boolean checkDeptCodeUnique(String deptCode, Long deptId);

    /**
     * 校验部门名称是否唯一
     *
     * @param deptName 部门名称
     * @param deptId 部门ID（修改时传入）
     * @return 是否唯一
     */
    boolean checkDeptNameUnique(String deptName, Long deptId);

    /**
     * 检查部门是否有子部门
     *
     * @param deptId 部门ID
     * @return 是否有子部门
     */
    boolean hasChildDept(Long deptId);

    /**
     * 检查部门是否有关联用户
     *
     * @param deptId 部门ID
     * @return 是否有关联用户
     */
    boolean hasAssociatedUser(Long deptId);
} 