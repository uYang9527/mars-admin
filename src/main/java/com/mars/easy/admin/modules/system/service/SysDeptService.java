package com.mars.easy.admin.modules.system.service;

import com.mybatisflex.core.service.IService;
import com.mars.easy.admin.modules.system.pojo.dto.sysdept.SysDeptCreateDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysdept.SysDeptListDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysdept.SysDeptUpdateDTO;
import com.mars.easy.admin.modules.system.pojo.po.SysDept;
import com.mars.easy.admin.modules.system.pojo.vo.sysdept.DeptTreeVO;
import com.mars.easy.admin.modules.system.pojo.vo.sysdept.SysDeptLeaderVO;
import com.mars.easy.admin.modules.system.pojo.vo.sysdept.SysDeptVO;
import com.mars.easy.admin.framework.entity.PageResult;
import com.mars.easy.admin.framework.entity.SelectIdsDTO;

import java.util.List;


/**
 * <p>
 * 部门表 Service
 * </p>
 *
 * @Author 程序员Mars
 * @since 2024-03-20
 */
public interface SysDeptService extends IService<SysDept> {

    void create(SysDeptCreateDTO dto);

    void update(SysDeptUpdateDTO dto);

    PageResult<SysDeptVO> page(SysDeptListDTO dto);

    List<SysDeptVO> list(SysDeptListDTO dto);

    void remove(SelectIdsDTO dto);

    SysDeptVO detail(Object id);


    List<DeptTreeVO> getDepartmentTreeWithAdditionalNodes();

    List<DeptTreeVO> getDeptTree(Integer excludeNodeId, Boolean appendRoot, Boolean needSetTotal);

    SysDeptLeaderVO findSysUserDeptLeader();
}
