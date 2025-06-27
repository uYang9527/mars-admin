package com.mars.admin.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysRoleMenuTableDef extends TableDef {

    /**
     * 角色菜单关联实体类

 @author Mars
     */
    public static final SysRoleMenuTableDef SYS_ROLE_MENU = new SysRoleMenuTableDef();

    public final QueryColumn MENU_ID = new QueryColumn(this, "menu_id");

    public final QueryColumn ROLE_ID = new QueryColumn(this, "role_id");

    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{MENU_ID, ROLE_ID, CREATE_BY, CREATE_TIME};

    public SysRoleMenuTableDef() {
        super("", "sys_role_menu");
    }

    private SysRoleMenuTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysRoleMenuTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysRoleMenuTableDef("", "sys_role_menu", alias));
    }

}
