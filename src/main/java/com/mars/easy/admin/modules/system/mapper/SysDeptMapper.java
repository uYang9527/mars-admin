package com.mars.easy.admin.modules.system.mapper;

import com.mybatisflex.core.BaseMapper;
import com.mars.easy.admin.modules.system.pojo.po.SysDept;
import com.mars.easy.admin.modules.system.pojo.vo.sysdept.TotalDeptVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @Author 程序员Mars
 * @since 2024-03-20
 */
public interface SysDeptMapper extends BaseMapper<SysDept> {

    /**
     * （向上递归）查询指定层级的祖籍id
     *
     * @param deptId
     * @return
     */
    List<Integer> iterUpDeptAncestors(@Param("deptId") Integer deptId);

    /**
     * 统计分配部门的用户数量
     *
     * @return
     */
    List<TotalDeptVO> countUsersPerDept();




}
