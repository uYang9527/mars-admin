package com.mars.admin.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class RoleTableDef extends TableDef {

    /**
     * 角色实体类
 继承BaseEntity，自动拥有公共字段和自动填充功能

 @author Mars.wq [wqexpore@163.com]
 @version 1.0
 @date 2025-01-27
     */
    public static final RoleTableDef ROLE = new RoleTableDef();

    /**
     * 角色ID
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 角色状态
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    /**
     * 是否删除
     */
    public final QueryColumn IS_DELETE = new QueryColumn(this, "is_delete");

    /**
     * 角色代码
     */
    public final QueryColumn ROLE_CODE = new QueryColumn(this, "role_code");

    /**
     * 角色名称
     */
    public final QueryColumn ROLE_NAME = new QueryColumn(this, "role_name");

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
     * 角色描述
     */
    public final QueryColumn DESCRIPTION = new QueryColumn(this, "description");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, STATUS, ROLE_CODE, ROLE_NAME, CREATE_TIME, CREATE_USER, DELETE_TIME, UPDATE_TIME, UPDATE_USER, DESCRIPTION};

    public RoleTableDef() {
        super("", "tb_role");
    }

    private RoleTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public RoleTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new RoleTableDef("", "tb_role", alias));
    }

}
