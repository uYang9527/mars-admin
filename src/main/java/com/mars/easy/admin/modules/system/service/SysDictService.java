package com.mars.easy.admin.modules.system.service;

import com.mybatisflex.core.service.IService;
import com.mars.easy.admin.modules.system.pojo.dto.sysdict.SysDictCreateDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysdict.SysDictListDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysdict.SysDictUpdateDTO;
import com.mars.easy.admin.modules.system.pojo.po.SysDict;
import com.mars.easy.admin.framework.entity.DictCustomVO;
import com.mars.easy.admin.framework.entity.PageResult;
import com.mars.easy.admin.framework.entity.SelectIdsDTO;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @Author 程序员Mars
 * @since 2023-08-18
 */
public interface SysDictService extends IService<SysDict> {

    void create(SysDictCreateDTO dto);

    void update(SysDictUpdateDTO dto);

    void remove(SelectIdsDTO dto);

    PageResult<SysDict> list(SysDictListDTO dto);

    Map<String, List<DictCustomVO>> dictList(String typeCode);

    Map<String, List<DictCustomVO>> dictAll();

    List<DictCustomVO> getDictByType(String typeCode);

    @SneakyThrows
    String exportDictSql(SelectIdsDTO dto);
}
