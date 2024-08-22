package com.mars.easy.admin.modules.tool.pojo.result;

import lombok.Data;

/**
 * @ClassName TableResult
 * @Author 程序员Mars
 * @Date 2023/11/27 13:55
 * @Version 1.0
 */
@Data
public class TableResult {

    private String tableName;

    private String tableComment;

    private String createTime;

    private String updateTime;

}
