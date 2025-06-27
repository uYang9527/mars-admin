package com.mars.admin.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysUserTableDef extends TableDef {

    /**
     * 系统用户实体类

 @author Mars
     */
    public static final SysUserTableDef SYS_USER = new SysUserTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn EMAIL = new QueryColumn(this, "email");

    public final QueryColumn PHONE = new QueryColumn(this, "phone");

    public final QueryColumn AVATAR = new QueryColumn(this, "avatar");

    public final QueryColumn GENDER = new QueryColumn(this, "gender");

    public final QueryColumn REMARK = new QueryColumn(this, "remark");

    public final QueryColumn STATUS = new QueryColumn(this, "status");

    public final QueryColumn BIRTHDAY = new QueryColumn(this, "birthday");

    /**
     * 创建人
     */
    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    public final QueryColumn NICKNAME = new QueryColumn(this, "nickname");

    public final QueryColumn PASSWORD = new QueryColumn(this, "password");

    public final QueryColumn REAL_NAME = new QueryColumn(this, "real_name");

    /**
     * 更新人
     */
    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    public final QueryColumn USER_TYPE = new QueryColumn(this, "user_type");

    public final QueryColumn USERNAME = new QueryColumn(this, "username");

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

    public final QueryColumn LOGIN_COUNT = new QueryColumn(this, "login_count");

    /**
     * 更新时间
     */
    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    public final QueryColumn LAST_LOGIN_IP = new QueryColumn(this, "last_login_ip");

    public final QueryColumn LAST_LOGIN_TIME = new QueryColumn(this, "last_login_time");

    public final QueryColumn PASSWORD_UPDATE_TIME = new QueryColumn(this, "password_update_time");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, EMAIL, PHONE, AVATAR, GENDER, REMARK, STATUS, BIRTHDAY, CREATE_BY, NICKNAME, PASSWORD, REAL_NAME, UPDATE_BY, USER_TYPE, USERNAME, CREATE_TIME, DELETE_TIME, LOGIN_COUNT, UPDATE_TIME, LAST_LOGIN_IP, LAST_LOGIN_TIME, PASSWORD_UPDATE_TIME};

    public SysUserTableDef() {
        super("", "sys_user");
    }

    private SysUserTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysUserTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysUserTableDef("", "sys_user", alias));
    }

}
