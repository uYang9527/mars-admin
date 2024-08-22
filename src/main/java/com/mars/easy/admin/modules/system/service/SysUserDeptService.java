package com.mars.easy.admin.modules.system.service;

import com.mybatisflex.core.service.IService;
import com.mars.easy.admin.modules.system.pojo.dto.sysuser.UserDeptDTO;
import com.mars.easy.admin.modules.system.pojo.po.SysUserDept;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
* <p>
    * 用户-部门关系表 Service
    * </p>
*
* @Author 程序员Mars
* @since 2024-04-02
*/
public interface SysUserDeptService extends IService<SysUserDept> {


    @Transactional
    void bind(UserDeptDTO dto);

    void unbind(List<Long> userIds);
}
