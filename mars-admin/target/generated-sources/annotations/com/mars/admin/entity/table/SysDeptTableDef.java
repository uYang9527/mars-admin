package com.mars.admin.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysDeptTableDef extends TableDef {

    /**
     * 系统部门实体类

 @author Mars
     */
    public static final SysDeptTableDef SYS_DEPT = new SysDeptTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn EMAIL = new QueryColumn(this, "email");

    public final QueryColumn PHONE = new QueryColumn(this, "phone");

    public final QueryColumn LEADER = new QueryColumn(this, "leader");

    public final QueryColumn REMARK = new QueryColumn(this, "remark");

    public final QueryColumn STATUS = new QueryColumn(this, "status");

    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    public final QueryColumn DEPT_CODE = new QueryColumn(this, "dept_code");

    public final QueryColumn DEPT_NAME = new QueryColumn(this, "dept_name");

    public final QueryColumn IS_SYSTEM = new QueryColumn(this, "is_system");

    public final QueryColumn ORDER_NUM = new QueryColumn(this, "order_num");

    public final QueryColumn PARENT_ID = new QueryColumn(this, "parent_id");

    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    public final QueryColumn ANCESTORS = new QueryColumn(this, "ancestors");

    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    public final QueryColumn DELETE_TIME = new QueryColumn(this, "delete_time");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, EMAIL, PHONE, LEADER, REMARK, STATUS, CREATE_BY, DEPT_CODE, DEPT_NAME, IS_SYSTEM, ORDER_NUM, PARENT_ID, UPDATE_BY, ANCESTORS, CREATE_TIME, DELETE_TIME, UPDATE_TIME};

    public SysDeptTableDef() {
        super("", "sys_dept");
    }

    private SysDeptTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysDeptTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysDeptTableDef("", "sys_dept", alias));
    }

}
