package com.mars.easy.admin.modules.tool.pojo.po.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class GeneratorTableColumnTableDef extends TableDef {

    /**
     * <p>
 代码生成业务表字段
 </p>

 @Author 程序员Mars
 @since 2023-11-27
     */
    public static final GeneratorTableColumnTableDef GENERATOR_TABLE_COLUMN = new GeneratorTableColumnTableDef();

    /**
     * 是否主键（1是）
     */
    public final QueryColumn IS_PK = new QueryColumn(this, "is_pk");

    /**
     * 排序
     */
    public final QueryColumn SORT = new QueryColumn(this, "sort");

    /**
     * 是否编辑字段（1是）
     */
    public final QueryColumn IS_EDIT = new QueryColumn(this, "is_edit");

    /**
     * 是否列表字段（1是）
     */
    public final QueryColumn IS_LIST = new QueryColumn(this, "is_list");

    /**
     * ts类型 (string、number、string[])
     */
    public final QueryColumn TS_TYPE = new QueryColumn(this, "ts_type");

    /**
     * 是否查询字段（1是）
     */
    public final QueryColumn IS_QUERY = new QueryColumn(this, "is_query");

    /**
     * 其他设置
     */
    public final QueryColumn OPTIONS = new QueryColumn(this, "options");

    /**
     * 归属表编号
     */
    public final QueryColumn TABLE_ID = new QueryColumn(this, "table_id");

    /**
     * 编号
     */
    public final QueryColumn COLUMN_ID = new QueryColumn(this, "column_id");

    public final QueryColumn CREATE_ID = new QueryColumn(this, "create_id");

    /**
     * 字典类型
     */
    public final QueryColumn DICT_TYPE = new QueryColumn(this, "dict_type");

    /**
     * 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    public final QueryColumn HTML_TYPE = new QueryColumn(this, "html_type");

    /**
     * 是否导出字段(1 是)
     */
    public final QueryColumn IS_EXPORT = new QueryColumn(this, "is_export");

    /**
     * 是否导入字段(1 是)
     */
    public final QueryColumn IS_IMPORT = new QueryColumn(this, "is_import");

    /**
     * 是否为插入字段（1是）
     */
    public final QueryColumn IS_INSERT = new QueryColumn(this, "is_insert");

    /**
     * JAVA类型 （String、Integer、Long、Double、BigDecimal、）
     */
    public final QueryColumn JAVA_TYPE = new QueryColumn(this, "java_type");

    public final QueryColumn UPDATE_ID = new QueryColumn(this, "update_id");

    /**
     * JAVA字段名
     */
    public final QueryColumn JAVA_FIELD = new QueryColumn(this, "java_field");

    /**
     * 查询方式（等于、不等于、大于、小于、范围）
     */
    public final QueryColumn QUERY_TYPE = new QueryColumn(this, "query_type");

    /**
     * 列名称
     */
    public final QueryColumn COLUMN_NAME = new QueryColumn(this, "column_name");

    /**
     * 列类型
     */
    public final QueryColumn COLUMN_TYPE = new QueryColumn(this, "column_type");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    /**
     * 是否自动填充(1 是)，通用属性的自动填充，需代码配合（create_id、create_time、update_id、update_time）
     */
    public final QueryColumn IS_AUTOFILL = new QueryColumn(this, "is_autofill");

    /**
     * 是否逻辑删除(1 是)
     */
    public final QueryColumn IS_LOGIC_DEL = new QueryColumn(this, "is_logic_del");

    /**
     * 是否必填（1是）
     */
    public final QueryColumn IS_REQUIRED = new QueryColumn(this, "is_required");

    /**
     * 搜索类型(input、input-number、select、select-v2、tree-select、cascader、date-picker、time-picker、time-select、switch、slider)
     */
    public final QueryColumn SEARCH_TYPE = new QueryColumn(this, "search_type");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 是否自增（1是）
     */
    public final QueryColumn IS_INCREMENT = new QueryColumn(this, "is_increment");

    /**
     * 自动填充类型(INSERT/UPDATE/INSERT_UPDATE)
     */
    public final QueryColumn AUTOFILL_TYPE = new QueryColumn(this, "autofill_type");

    /**
     * get开头的驼峰字段名
     */
    public final QueryColumn UP_CAMEL_FIELD = new QueryColumn(this, "up_camel_field");

    /**
     * 列描述
     */
    public final QueryColumn COLUMN_COMMENT = new QueryColumn(this, "column_comment");

    /**
     * 是否进行唯一校验(1 是)，字段唯一性校验
     */
    public final QueryColumn IS_UNIQUE_VALID = new QueryColumn(this, "is_unique_valid");

    /**
     * java类型包名(java)
     */
    public final QueryColumn JAVA_TYPE_PACKAGE = new QueryColumn(this, "java_type_package");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{IS_PK, SORT, IS_EDIT, IS_LIST, TS_TYPE, IS_QUERY, OPTIONS, TABLE_ID, COLUMN_ID, CREATE_ID, DICT_TYPE, HTML_TYPE, IS_EXPORT, IS_IMPORT, IS_INSERT, JAVA_TYPE, UPDATE_ID, JAVA_FIELD, QUERY_TYPE, COLUMN_NAME, COLUMN_TYPE, CREATE_TIME, IS_AUTOFILL, IS_LOGIC_DEL, IS_REQUIRED, SEARCH_TYPE, UPDATE_TIME, IS_INCREMENT, AUTOFILL_TYPE, UP_CAMEL_FIELD, COLUMN_COMMENT, IS_UNIQUE_VALID, JAVA_TYPE_PACKAGE};

    public GeneratorTableColumnTableDef() {
        super("", "generator_table_column");
    }

    private GeneratorTableColumnTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public GeneratorTableColumnTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new GeneratorTableColumnTableDef("", "generator_table_column", alias));
    }

}
