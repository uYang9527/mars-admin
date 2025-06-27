package com.mars.admin.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysConfigTableDef extends TableDef {

    /**
     * 系统配置实体类

 @author Mars
     */
    public static final SysConfigTableDef SYS_CONFIG = new SysConfigTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn REMARK = new QueryColumn(this, "remark");

    /**
     * 创建人
     */
    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    /**
     * 更新人
     */
    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    public final QueryColumn CONFIG_KEY = new QueryColumn(this, "config_key");

    /**
     * 是否删除：0-否，1-是
     */
    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    public final QueryColumn CONFIG_NAME = new QueryColumn(this, "config_name");

    public final QueryColumn CONFIG_TYPE = new QueryColumn(this, "config_type");

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

    public final QueryColumn CONFIG_VALUE = new QueryColumn(this, "config_value");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, REMARK, CREATE_BY, UPDATE_BY, CONFIG_KEY, CONFIG_NAME, CONFIG_TYPE, CREATE_TIME, DELETE_TIME, UPDATE_TIME, CONFIG_VALUE};

    public SysConfigTableDef() {
        super("", "sys_config");
    }

    private SysConfigTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysConfigTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysConfigTableDef("", "sys_config", alias));
    }

}
