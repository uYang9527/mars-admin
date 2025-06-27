package com.mars.admin.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysMenuTableDef extends TableDef {

    /**
     * 系统菜单实体类

 @author Mars
     */
    public static final SysMenuTableDef SYS_MENU = new SysMenuTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn ICON = new QueryColumn(this, "icon");

    public final QueryColumn PERMS = new QueryColumn(this, "perms");

    public final QueryColumn REMARK = new QueryColumn(this, "remark");

    public final QueryColumn STATUS = new QueryColumn(this, "status");

    public final QueryColumn IS_CACHE = new QueryColumn(this, "is_cache");

    public final QueryColumn IS_FRAME = new QueryColumn(this, "is_frame");

    public final QueryColumn VISIBLE = new QueryColumn(this, "visible");

    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    public final QueryColumn IS_SYSTEM = new QueryColumn(this, "is_system");

    public final QueryColumn MENU_CODE = new QueryColumn(this, "menu_code");

    public final QueryColumn MENU_NAME = new QueryColumn(this, "menu_name");

    public final QueryColumn MENU_TYPE = new QueryColumn(this, "menu_type");

    public final QueryColumn ORDER_NUM = new QueryColumn(this, "order_num");

    public final QueryColumn PARENT_ID = new QueryColumn(this, "parent_id");

    public final QueryColumn REDIRECT = new QueryColumn(this, "redirect");

    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    public final QueryColumn COMPONENT = new QueryColumn(this, "component");

    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    public final QueryColumn ROUTE_NAME = new QueryColumn(this, "route_name");

    public final QueryColumn ROUTE_PATH = new QueryColumn(this, "route_path");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    public final QueryColumn DELETE_TIME = new QueryColumn(this, "delete_time");

    public final QueryColumn QUERY_PARAM = new QueryColumn(this, "query_param");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    public final QueryColumn MENU_TYPE_FLAG = new QueryColumn(this, "menu_type_flag");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, ICON, PERMS, REMARK, STATUS, IS_CACHE, IS_FRAME, VISIBLE, CREATE_BY, IS_SYSTEM, MENU_CODE, MENU_NAME, MENU_TYPE, ORDER_NUM, PARENT_ID, REDIRECT, UPDATE_BY, COMPONENT, ROUTE_NAME, ROUTE_PATH, CREATE_TIME, DELETE_TIME, QUERY_PARAM, UPDATE_TIME, MENU_TYPE_FLAG};

    public SysMenuTableDef() {
        super("", "sys_menu");
    }

    private SysMenuTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysMenuTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysMenuTableDef("", "sys_menu", alias));
    }

}
