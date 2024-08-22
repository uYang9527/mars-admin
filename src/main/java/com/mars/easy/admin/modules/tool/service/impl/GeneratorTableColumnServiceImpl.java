package com.mars.easy.admin.modules.tool.service.impl;

import com.mars.easy.admin.modules.tool.mapper.GeneratorTableColumnMapper;
import com.mars.easy.admin.modules.tool.pojo.po.GeneratorTableColumn;
import com.mars.easy.admin.modules.tool.pojo.po.table.GeneratorTableColumnTableDef;
import com.mars.easy.admin.modules.tool.pojo.po.table.GeneratorTableTableDef;
import com.mars.easy.admin.modules.tool.service.GeneratorTableColumnService;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 代码生成业务表字段 服务实现类
 * </p>
 *
 * @Author 程序员Mars
 * @since 2023-11-27
 */
@Service
public class GeneratorTableColumnServiceImpl extends ServiceImpl<GeneratorTableColumnMapper, GeneratorTableColumn> implements GeneratorTableColumnService {

    @Override
    public void batchInsert(List<GeneratorTableColumn> tableColumns) {
        saveBatch(tableColumns);
    }

    @Override
    public List<GeneratorTableColumn> getTableColumnsByTableId(Integer tableId) {
        QueryWrapper wrapper = QueryWrapper.create()
                .eq(GeneratorTableColumn::getTableId, tableId)
                .orderBy(GeneratorTableColumn::getSort).asc();
        List<GeneratorTableColumn> list = list(wrapper);
        return list;
    }

    @Override
    public List<GeneratorTableColumn> getTableColumnsByTableName(Integer tableId) {
        List<GeneratorTableColumn> list = QueryChain.of(mapper)
                .eq(GeneratorTableColumn::getTableId, tableId)
                .orderBy(GeneratorTableColumn::getSort)
                .asc()
                .list();
        return list;
    }

    @Override
    public void updateBatchTableColumns(List<GeneratorTableColumn> columns) {
        updateBatch(columns);
    }

    @Override
    public void remove(List tableNames){
        this.updateChain()
                .from(GeneratorTableTableDef.GENERATOR_TABLE, GeneratorTableColumnTableDef.GENERATOR_TABLE_COLUMN)
                .where(GeneratorTableColumnTableDef.GENERATOR_TABLE_COLUMN.TABLE_ID.eq(GeneratorTableTableDef.GENERATOR_TABLE.TABLE_ID))
                .where(GeneratorTableTableDef.GENERATOR_TABLE.TABLE_NAME.in(tableNames))
                .remove();
    }


}
