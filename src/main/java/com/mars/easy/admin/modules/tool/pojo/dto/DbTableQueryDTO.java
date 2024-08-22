package com.mars.easy.admin.modules.tool.pojo.dto;

import com.mars.easy.admin.framework.entity.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName DbTableQueryDTO
 * @Author 程序员Mars
 * @Date 2023/11/29 14:17
 * @Version 1.0
 */
@Schema(description = "导入表查询")
@Data
public class DbTableQueryDTO extends PageQuery {

    @Schema(description ="表名称")
    private String tableName;

    @Schema(description ="表描述")
    private String tableComment;

}
