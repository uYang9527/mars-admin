package com.mars.admin.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysOperLogTableDef extends TableDef {

    /**
     * 系统操作日志实体类

 @author Mars
     */
    public static final SysOperLogTableDef SYS_OPER_LOG = new SysOperLogTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn TITLE = new QueryColumn(this, "title");

    public final QueryColumn METHOD = new QueryColumn(this, "method");

    public final QueryColumn OPER_IP = new QueryColumn(this, "oper_ip");

    public final QueryColumn STATUS = new QueryColumn(this, "status");

    public final QueryColumn OPER_URL = new QueryColumn(this, "oper_url");

    public final QueryColumn COST_TIME = new QueryColumn(this, "cost_time");

    public final QueryColumn DEPT_NAME = new QueryColumn(this, "dept_name");

    public final QueryColumn ERROR_MSG = new QueryColumn(this, "error_msg");

    public final QueryColumn OPER_NAME = new QueryColumn(this, "oper_name");

    public final QueryColumn OPER_TIME = new QueryColumn(this, "oper_time");

    public final QueryColumn OPER_PARAM = new QueryColumn(this, "oper_param");

    public final QueryColumn JSON_RESULT = new QueryColumn(this, "json_result");

    public final QueryColumn BUSINESS_TYPE = new QueryColumn(this, "business_type");

    public final QueryColumn OPER_LOCATION = new QueryColumn(this, "oper_location");

    public final QueryColumn OPERATOR_TYPE = new QueryColumn(this, "operator_type");

    public final QueryColumn REQUEST_METHOD = new QueryColumn(this, "request_method");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, TITLE, METHOD, OPER_IP, STATUS, OPER_URL, COST_TIME, DEPT_NAME, ERROR_MSG, OPER_NAME, OPER_TIME, OPER_PARAM, JSON_RESULT, BUSINESS_TYPE, OPER_LOCATION, OPERATOR_TYPE, REQUEST_METHOD};

    public SysOperLogTableDef() {
        super("", "sys_oper_log");
    }

    private SysOperLogTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public SysOperLogTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new SysOperLogTableDef("", "sys_oper_log", alias));
    }

}
