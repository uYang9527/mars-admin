package com.mars.easy.admin.modules.system.pojo.po;

import com.mars.easy.admin.framework.datascope.EntityChangeListener;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 用户-部门关系表
 * </p>
 *
 * @Author 程序员Mars
 * @since 2024-04-02
 */
@Data
@Table(value = "sys_user_dept", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "用户-部门关系表")
public class SysUserDept implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description = "")
    private Long id;

    @Schema(description = "sys_dept_id")
    private Long deptId;

    @Schema(description = "sys_user_id")
    private Long userId;

}
