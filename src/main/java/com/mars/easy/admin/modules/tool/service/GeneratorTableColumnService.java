package com.mars.easy.admin.modules.tool.service;


import com.mars.easy.admin.modules.tool.pojo.po.GeneratorTableColumn;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * <p>
 * 代码生成业务表字段 服务类
 * </p>
 *
 * @Author 程序员Mars
 * @since 2023-11-27
 */
public interface GeneratorTableColumnService extends IService<GeneratorTableColumn> {

    void batchInsert(List<GeneratorTableColumn> tableColumns);

    List<GeneratorTableColumn> getTableColumnsByTableId(Integer tableId);

    List<GeneratorTableColumn> getTableColumnsByTableName(Integer tableId);

    void updateBatchTableColumns(List<GeneratorTableColumn> columns);

    void remove(List tableNames);
}
