package com.mars.easy.admin.modules.tool.pojo.po.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class GeneratorTableTableDef extends TableDef {

    /**
     * <p>
 代码生成业务表
 </p>

 @Author 程序员Mars
 @since 2023-11-27
     */
    public static final GeneratorTableTableDef GENERATOR_TABLE = new GeneratorTableTableDef();

    /**
     * 生成路径
     */
    public final QueryColumn PATH = new QueryColumn(this, "path");

    /**
     * 生成方式(0 zip压缩包；1 自定义路径)
     */
    public final QueryColumn TYPE = new QueryColumn(this, "type");

    /**
     * 其他参数
     */
    public final QueryColumn OPTIONS = new QueryColumn(this, "options");

    /**
     * api生成路径
     */
    public final QueryColumn PATH_API = new QueryColumn(this, "path_api");

    /**
     * web生成路径
     */
    public final QueryColumn PATH_WEB = new QueryColumn(this, "path_web");

    public final QueryColumn TABLE_ID = new QueryColumn(this, "table_id");

    public final QueryColumn CREATE_ID = new QueryColumn(this, "create_id");

    public final QueryColumn UPDATE_ID = new QueryColumn(this, "update_id");

    /**
     * 实体类名称
     */
    public final QueryColumn CLASS_NAME = new QueryColumn(this, "class_name");

    /**
     * 是否支持导出(1 是)
     */
    public final QueryColumn HAS_EXPORT = new QueryColumn(this, "has_export");

    /**
     * 是否支持导入(1 是)
     */
    public final QueryColumn HAS_IMPORT = new QueryColumn(this, "has_import");

    /**
     * 表名称
 <p>
 private String tableName;
 <p>
 /**
 表描述
     */
    public final QueryColumn TABLE_NAME = new QueryColumn(this, "table_name");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    public final QueryColumn IS_AUTOFILL = new QueryColumn(this, "is_autofill");

    /**
     * 生成模块名
     */
    public final QueryColumn MODULE_NAME = new QueryColumn(this, "module_name");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 生成包路径
     */
    public final QueryColumn PACKAGE_NAME = new QueryColumn(this, "package_name");

    /**
     * 使用的模版
     */
    public final QueryColumn TPL_CATEGORY = new QueryColumn(this, "tpl_category");

    /**
     * 生成业务名
     */
    public final QueryColumn BUSINESS_NAME = new QueryColumn(this, "business_name");

    /**
     * 生成功能名
     */
    public final QueryColumn FUNCTION_NAME = new QueryColumn(this, "function_name");

    /**
     * 生成类型
 <p>
 all      全部
 server   后端
 service  接口
     */
    public final QueryColumn GENERATE_TYPE = new QueryColumn(this, "generate_type");

    /**
     * 是否自动创建菜单路由（1 是）
     */
    public final QueryColumn MENU_INIT_TYPE = new QueryColumn(this, "menu_init_type");

    /**
     * 上级菜单id
     */
    public final QueryColumn PARENT_MENU_ID = new QueryColumn(this, "parent_menu_id");

    public final QueryColumn TABLE_COMMENT = new QueryColumn(this, "table_comment");

    /**
     * camel实体类名称
     */
    public final QueryColumn CAMEL_CLASS_NAME = new QueryColumn(this, "camel_class_name");

    /**
     * 生成作者名
     */
    public final QueryColumn FUNCTION_AUTHOR = new QueryColumn(this, "function_author");

    /**
     * 是否自动创建按钮权限 (1 是)
     */
    public final QueryColumn BTN_PERMISSION_TYPE = new QueryColumn(this, "btn_permission_type");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{PATH, TYPE, OPTIONS, PATH_API, PATH_WEB, TABLE_ID, CREATE_ID, UPDATE_ID, CLASS_NAME, HAS_EXPORT, HAS_IMPORT, TABLE_NAME, CREATE_TIME, IS_AUTOFILL, MODULE_NAME, UPDATE_TIME, PACKAGE_NAME, TPL_CATEGORY, BUSINESS_NAME, FUNCTION_NAME, GENERATE_TYPE, MENU_INIT_TYPE, PARENT_MENU_ID, TABLE_COMMENT, CAMEL_CLASS_NAME, FUNCTION_AUTHOR, BTN_PERMISSION_TYPE};

    public GeneratorTableTableDef() {
        super("", "generator_table");
    }

    private GeneratorTableTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public GeneratorTableTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new GeneratorTableTableDef("", "generator_table", alias));
    }

}
