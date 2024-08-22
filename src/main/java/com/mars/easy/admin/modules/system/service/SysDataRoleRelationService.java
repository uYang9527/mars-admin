package com.mars.easy.admin.modules.system.service;

import com.mybatisflex.core.service.IService;
import com.mars.easy.admin.modules.system.pojo.po.SysDataRoleRelation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统数据角色-关联表 Service
 * </p>
 *
 * @Author 程序员Mars-admin
 * @since 2024-07-11
 */
public interface SysDataRoleRelationService extends IService<SysDataRoleRelation> {


    @Transactional
    void batchSave(Long roleId, String relationTypeCd, List<Long> relationIds);

    List<Long> getSelectRelationId(Long roleId, String relationTypeCd);
}
