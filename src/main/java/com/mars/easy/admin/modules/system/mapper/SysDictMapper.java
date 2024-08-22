package com.mars.easy.admin.modules.system.mapper;

import com.mybatisflex.core.BaseMapper;
import com.mars.easy.admin.modules.system.pojo.po.SysDict;
import com.mars.easy.admin.modules.system.pojo.vo.sysdict.DictVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @Author 程序员Mars
 * @since 2023-08-18
 */
public interface SysDictMapper extends BaseMapper<SysDict> {

    List<DictVO> listDict(@Param("typeCode") String typeCode);


}
