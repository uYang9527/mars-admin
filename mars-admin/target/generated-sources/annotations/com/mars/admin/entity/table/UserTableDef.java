package com.mars.admin.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class UserTableDef extends TableDef {

    public static final UserTableDef USER = new UserTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 邮箱
     */
    public final QueryColumn EMAIL = new QueryColumn(this, "email");

    /**
     * 手机
     */
    public final QueryColumn PHONE = new QueryColumn(this, "phone");

    /**
     * 状态
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    /**
     * 密码
     */
    public final QueryColumn PASSWORD = new QueryColumn(this, "password");

    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    /**
     * 脱敏规则
     */
    public final QueryColumn USERNAME = new QueryColumn(this, "username");

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
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, EMAIL, PHONE, STATUS, CREATE_BY, PASSWORD, UPDATE_BY, USERNAME, CREATE_TIME, DELETE_TIME, UPDATE_TIME};

    public UserTableDef() {
        super("", "tb_user");
    }

    private UserTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public UserTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new UserTableDef("", "tb_user", alias));
    }

}
