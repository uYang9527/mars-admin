package com.mars.easy.admin.modules.system.service;

import com.mybatisflex.core.service.IService;
import com.mars.easy.admin.modules.system.pojo.po.SysDeptLeader;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <p>
 * 部门领导人表 Service
 * </p>
 *
 * @Author 程序员Mars
 * @since 2024-03-26
 */
public interface SysDeptLeaderService extends IService<SysDeptLeader> {


    @Transactional
    void syncLeader(Long deptId, List<Long> leaderIds);
}
