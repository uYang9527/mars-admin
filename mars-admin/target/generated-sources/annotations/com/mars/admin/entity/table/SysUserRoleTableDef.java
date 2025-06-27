package com.mars.admin.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysUserRoleTableDef extends TableDef {

    /**
     * 用户角色关联实体类

 @author Mars
     */
    public static final SysUserRoleTableDef SYS_USER_ROLE = new SysUserRoleTableDef();

    public final QueryColumn ROLE_ID = new QueryColumn(this, "role_id");

    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ROLE_ID, USER_ID, CREATE_BY, CREATE_TIME};

    public SysUserRoleTableDef() {
        super("", "sys_user_role");
    }

    private SysUserRoleTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysUserRoleTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysUserRoleTableDef("", "sys_user_role", alias));
    }

}
