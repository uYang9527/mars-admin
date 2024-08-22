package com.mars.easy.admin.modules.tool.mapper;

import com.mars.easy.admin.modules.tool.pojo.po.GeneratorTableColumn;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 代码生成业务表字段 Mapper 接口
 * </p>
 *
 * @Author 程序员Mars
 * @since 2023-11-27
 */
public interface GeneratorTableColumnMapper extends BaseMapper<GeneratorTableColumn> {

    List<GeneratorTableColumn> queryAllByTableName(@Param("tableNames") List<String> tableNames);

}
