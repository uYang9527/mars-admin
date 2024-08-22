package com.mars.easy.admin.modules.system.service;

import com.mybatisflex.core.service.IService;
import com.mars.easy.admin.modules.system.pojo.po.SysDeptClosure;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
* <p>
    * 部门祖籍关系表 Service
    * </p>
*
* @Author 程序员Mars
* @since 2024-03-28
*/
public interface SysDeptClosureService extends IService<SysDeptClosure> {

    List<SysDeptClosure> ancestorsPath(Long deptId);

    void create(Long deptId, Long parentDeptId);

    @Transactional
    void remove(Long nodeId);

    List<Long> descendants(List<Long> ancestorIds);

    void move(Long nodeId, Long newNodeId);
}
