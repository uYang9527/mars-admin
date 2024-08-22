package com.mars.easy.admin.modules.system.pojo.dto.sysdept;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.mars.easy.admin.framework.entity.PageQuery;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * SysDept添加DTO
 * </p>
 *
 * @Author 程序员Mars
 * @since 2024-03-20
 */
@Data
@Schema(description = "SysDept查询DTO")
public class SysDeptListDTO extends PageQuery {

    @Schema(description =  "部门名称")
    private String name;


}
