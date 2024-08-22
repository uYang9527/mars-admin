package com.mars.easy.admin.modules.system.pojo.dto.sysfile;

import com.mars.easy.admin.framework.entity.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SysFileQueryDTO
 *
 * @Author 程序员Mars
 * @since 2023/8/31 0031
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "公共文件搜索")
public class SysFileListDTO extends PageQuery {

    @Schema(description =  "文件名")
    private String filename;
}
