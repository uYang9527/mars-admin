package com.mars.easy.admin.modules.tool.service;


import com.mars.easy.admin.framework.entity.PageResult;
import com.mars.easy.admin.modules.tool.pojo.dto.DbTableQueryDTO;
import com.mars.easy.admin.modules.tool.pojo.dto.ImportTableDTO;
import com.mars.easy.admin.modules.tool.pojo.dto.SelectTablesDTO;
import com.mars.easy.admin.modules.tool.pojo.po.GeneratorTable;
import com.mars.easy.admin.modules.tool.pojo.vo.GenCheckedInfoVO;
import com.mars.easy.admin.modules.tool.pojo.vo.GeneratorDetailVO;
import com.mars.easy.admin.modules.tool.pojo.vo.GeneratorPreviewVO;
import com.mybatisflex.core.service.IService;
import freemarker.template.Template;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 代码生成业务表 服务类
 * </p>
 *
 * @Author 程序员Mars
 * @since 2023-11-27
 */
public interface GeneratorTableService extends IService<GeneratorTable> {

    /**
     * 导入表格
     *
     * @param dto
     */
    void importTable(ImportTableDTO dto);

    /**
     * 查询未导入的表
     *
     * @param dto
     * @return
     */
    PageResult<GeneratorTable> selectDbTableNotInImport(DbTableQueryDTO dto);

    /**
     * 查询已经导入的表
     *
     * @param dto
     * @return
     */
    PageResult<GeneratorTable> selectDbTableByImport(DbTableQueryDTO dto);

    /**
     * 代码生成配置详情
     *
     * @param tableName
     * @return
     */
    GeneratorDetailVO detail(String tableName);

    /**
     * 更新代码生成配置
     * @param generatorDetailVO
     */
    void updateGeneratorSetting(GeneratorDetailVO generatorDetailVO);

    /**
     * 生成代码
     */
    List<String> generator(String tableName) throws IOException;

    GenCheckedInfoVO checkDist(String tableName);

    byte[] downloadZip(SelectTablesDTO dto) throws IOException;

    List<GeneratorPreviewVO> preview(String tableName) throws IOException;

    Template getMenuSqlTemplate() throws IOException;

    @Transactional
    void remove(SelectTablesDTO dto);

    Template getDictSqlTemplate() throws IOException;
}
