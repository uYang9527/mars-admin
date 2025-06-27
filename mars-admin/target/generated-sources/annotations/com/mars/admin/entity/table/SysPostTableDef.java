package com.mars.admin.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysPostTableDef extends TableDef {

    /**
     * 系统岗位实体类

 @author Mars
     */
    public static final SysPostTableDef SYS_POST = new SysPostTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn REMARK = new QueryColumn(this, "remark");

    public final QueryColumn STATUS = new QueryColumn(this, "status");

    /**
     * 创建人
     */
    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    public final QueryColumn IS_SYSTEM = new QueryColumn(this, "is_system");

    public final QueryColumn POST_CODE = new QueryColumn(this, "post_code");

    public final QueryColumn POST_NAME = new QueryColumn(this, "post_name");

    public final QueryColumn POST_SORT = new QueryColumn(this, "post_sort");

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
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, REMARK, STATUS, CREATE_BY, IS_SYSTEM, POST_CODE, POST_NAME, POST_SORT, UPDATE_BY, CREATE_TIME, DELETE_TIME, UPDATE_TIME};

    public SysPostTableDef() {
        super("", "sys_post");
    }

    private SysPostTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysPostTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysPostTableDef("", "sys_post", alias));
    }

}
