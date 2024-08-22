package com.mars.easy.admin.modules.business.addressbook.pojo.po.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class AddressBookTableDef extends TableDef {

    /**
     * 地址管理

 @author Mars
 @since 2024-08-22
     */
    public static final AddressBookTableDef ADDRESS_BOOK = new AddressBookTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn SEX = new QueryColumn(this, "sex");

    public final QueryColumn LABEL = new QueryColumn(this, "label");

    public final QueryColumn PHONE = new QueryColumn(this, "phone");

    public final QueryColumn DETAIL = new QueryColumn(this, "detail");

    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    public final QueryColumn CITY_CODE = new QueryColumn(this, "city_code");

    public final QueryColumn CITY_NAME = new QueryColumn(this, "city_name");

    public final QueryColumn CONSIGNEE = new QueryColumn(this, "consignee");

    public final QueryColumn IS_DEFAULT = new QueryColumn(this, "is_default");

    public final QueryColumn IS_DELETED = new QueryColumn(this, "is_deleted");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    public final QueryColumn CREATE_USER = new QueryColumn(this, "create_user");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    public final QueryColumn UPDATE_USER = new QueryColumn(this, "update_user");

    public final QueryColumn DISTRICT_CODE = new QueryColumn(this, "district_code");

    public final QueryColumn DISTRICT_NAME = new QueryColumn(this, "district_name");

    public final QueryColumn PROVINCE_CODE = new QueryColumn(this, "province_code");

    public final QueryColumn PROVINCE_NAME = new QueryColumn(this, "province_name");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, SEX, LABEL, PHONE, DETAIL, USER_ID, CITY_CODE, CITY_NAME, CONSIGNEE, IS_DEFAULT, IS_DELETED, CREATE_TIME, CREATE_USER, UPDATE_TIME, UPDATE_USER, DISTRICT_CODE, DISTRICT_NAME, PROVINCE_CODE, PROVINCE_NAME};

    public AddressBookTableDef() {
        super("", "address_book");
    }

    private AddressBookTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public AddressBookTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new AddressBookTableDef("", "address_book", alias));
    }

}
