package com.mars.easy.admin.modules.system.pojo.vo.sysclient;

import com.mars.easy.admin.modules.auth.entity.ClientVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * SysClient查询返回
 * </p>
 *
 * @Author 程序员Mars
 * @since 2024-01-22
 */
@Data
@Schema(description = "SysClient返回vo")
public class SysClientVO extends ClientVO {

    @Schema(description = "是否锁定")
    private String isLock;

}
