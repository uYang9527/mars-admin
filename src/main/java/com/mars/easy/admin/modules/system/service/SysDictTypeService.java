package com.mars.easy.admin.modules.system.service;

import com.mybatisflex.core.service.IService;
import com.mars.easy.admin.modules.system.pojo.dto.sysdict.SysDictTypeAddDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysdict.SysDictTypeListDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysdict.SysDictTypeUpDTO;
import com.mars.easy.admin.modules.system.pojo.po.SysDictType;
import com.mars.easy.admin.framework.entity.PageResult;
import com.mars.easy.admin.framework.entity.SelectIdsDTO;

import java.util.List;

/**
 * <p>
 * 字典类型 服务类
 * </p>
 *
 * @Author 程序员Mars
 * @since 2023-08-18
 */
public interface SysDictTypeService extends IService<SysDictType> {

    void create(SysDictTypeAddDTO dto);

    void update(SysDictTypeUpDTO dto);

    void remove(SelectIdsDTO dto);

    SysDictType detail(Long id);

    PageResult<SysDictType> list(SysDictTypeListDTO dto);

    List<SysDictType> selectOptionsType();
}
