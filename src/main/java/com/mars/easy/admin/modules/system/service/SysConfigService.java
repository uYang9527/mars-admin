package com.mars.easy.admin.modules.system.service;

import com.mybatisflex.core.service.IService;
import com.mars.easy.admin.modules.system.pojo.dto.sysconfig.SysConfigCreateDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysconfig.SysConfigListDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysconfig.SysConfigUpdateDTO;
import com.mars.easy.admin.modules.system.pojo.po.SysConfig;
import com.mars.easy.admin.framework.entity.PageResult;
import com.mars.easy.admin.framework.entity.SelectIdsDTO;

/**
 * <p>
 * 参数配置表 服务类
 * </p>
 *
 * @Author 程序员Mars
 * @since 2023-11-23
 */
public interface SysConfigService extends IService<SysConfig> {

    void create(SysConfigCreateDTO dto);

    void update(SysConfigUpdateDTO dto);

    PageResult<SysConfig> list(SysConfigListDTO queryDTO);

    void remove(SelectIdsDTO dto);

    SysConfig detail(Object id);
}
