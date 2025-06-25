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
     * 是否删除
     */
    public final QueryColumn IS_DELETE = new QueryColumn(this, "is_delete");

    /**
     * 密码
     */
    public final QueryColumn PASSWORD = new QueryColumn(this, "password");

    /**
     * 脱敏规则
     */
    public final QueryColumn USERNAME = new QueryColumn(this, "username");

    /**
     * 创建时间
     */
    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    /**
     * 创建人
     */
    public final QueryColumn CREATE_USER = new QueryColumn(this, "create_user");

    /**
     * 删除时间
     */
    public final QueryColumn DELETE_TIME = new QueryColumn(this, "delete_time");

    /**
     * 更新时间
     */
    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 更新人
     */
    public final QueryColumn UPDATE_USER = new QueryColumn(this, "update_user");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, EMAIL, PHONE, PASSWORD, USERNAME, CREATE_TIME, CREATE_USER, DELETE_TIME, UPDATE_TIME, UPDATE_USER};

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
