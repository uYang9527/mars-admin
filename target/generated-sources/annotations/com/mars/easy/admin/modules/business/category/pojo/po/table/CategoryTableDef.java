package com.mars.easy.admin.modules.business.category.pojo.po.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class CategoryTableDef extends TableDef {

    /**
     * 菜品及套餐分类

 @author Mars
 @since 2024-08-22
     */
    public static final CategoryTableDef CATEGORY = new CategoryTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn NAME = new QueryColumn(this, "name");

    public final QueryColumn SORT = new QueryColumn(this, "sort");

    public final QueryColumn TYPE = new QueryColumn(this, "type");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    public final QueryColumn CREATE_USER = new QueryColumn(this, "create_user");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    public final QueryColumn UPDATE_USER = new QueryColumn(this, "update_user");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, NAME, SORT, TYPE, CREATE_TIME, CREATE_USER, UPDATE_TIME, UPDATE_USER};

    public CategoryTableDef() {
        super("", "category");
    }

    private CategoryTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public CategoryTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new CategoryTableDef("", "category", alias));
    }

}
