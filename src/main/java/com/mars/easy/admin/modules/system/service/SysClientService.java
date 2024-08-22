package com.mars.easy.admin.modules.system.service;

import com.mars.easy.admin.framework.entity.PageResult;
import com.mars.easy.admin.framework.entity.SelectIdsDTO;
import com.mars.easy.admin.modules.auth.entity.ClientVO;
import com.mars.easy.admin.modules.system.pojo.dto.sysclient.SysClientCreateDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysclient.SysClientListDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysclient.SysClientUpdateDTO;
import com.mars.easy.admin.modules.system.pojo.po.SysClient;
import com.mars.easy.admin.modules.system.pojo.vo.sysclient.SysClientVO;
import com.mybatisflex.core.service.IService;

import java.util.List;


/**
* <p>
    * 系统授权表 Service
    * </p>
*
* @Author 程序员Mars
* @since 2024-01-22
*/
public interface SysClientService  extends IService<SysClient>  {

    void create(SysClientCreateDTO dto);

    void update(SysClientUpdateDTO dto);

    PageResult<SysClientVO> page(SysClientListDTO dto);

    List<SysClientVO> list(SysClientListDTO dto);

    void remove(SelectIdsDTO dto);

    ClientVO detail(Object id);


}
