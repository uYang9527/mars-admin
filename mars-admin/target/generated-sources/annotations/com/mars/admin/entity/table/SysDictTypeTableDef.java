package com.mars.admin.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysDictTypeTableDef extends TableDef {

    /**
     * 系统字典类型实体类

 @author Mars
     */
    public static final SysDictTypeTableDef SYS_DICT_TYPE = new SysDictTypeTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn REMARK = new QueryColumn(this, "remark");

    public final QueryColumn STATUS = new QueryColumn(this, "status");

    /**
     * 创建人
     */
    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    public final QueryColumn DICT_NAME = new QueryColumn(this, "dict_name");

    public final QueryColumn DICT_TYPE = new QueryColumn(this, "dict_type");

    /**
     * 更新人
     */
    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    /**
     * 是否删除：0-否，1-是
     */
    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    /**
     * 创建时间
     */
    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    /**
     * 删除时间
     */
    public final QueryColumn DELETE_TIME = new QueryColumn(this, "delete_time");

    /**
     * 更新时间
     */
    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, REMARK, STATUS, CREATE_BY, DICT_NAME, DICT_TYPE, UPDATE_BY, CREATE_TIME, DELETE_TIME, UPDATE_TIME};

    public SysDictTypeTableDef() {
        super("", "sys_dict_type");
    }

    private SysDictTypeTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysDictTypeTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysDictTypeTableDef("", "sys_dict_type", alias));
    }

}
