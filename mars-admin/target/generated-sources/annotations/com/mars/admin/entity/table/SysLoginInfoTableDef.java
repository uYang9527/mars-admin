package com.mars.admin.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysLoginInfoTableDef extends TableDef {

    /**
     * 系统登录日志实体类

 @author Mars
     */
    public static final SysLoginInfoTableDef SYS_LOGIN_INFO = new SysLoginInfoTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn OS = new QueryColumn(this, "os");

    public final QueryColumn MSG = new QueryColumn(this, "msg");

    public final QueryColumn IPADDR = new QueryColumn(this, "ipaddr");

    public final QueryColumn STATUS = new QueryColumn(this, "status");

    public final QueryColumn BROWSER = new QueryColumn(this, "browser");

    public final QueryColumn USER_NAME = new QueryColumn(this, "user_name");

    public final QueryColumn LOGIN_TIME = new QueryColumn(this, "login_time");

    public final QueryColumn LOGIN_LOCATION = new QueryColumn(this, "login_location");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, OS, MSG, IPADDR, STATUS, BROWSER, USER_NAME, LOGIN_TIME, LOGIN_LOCATION};

    public SysLoginInfoTableDef() {
        super("", "sys_logininfor");
    }

    private SysLoginInfoTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysLoginInfoTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysLoginInfoTableDef("", "sys_logininfor", alias));
    }

}
