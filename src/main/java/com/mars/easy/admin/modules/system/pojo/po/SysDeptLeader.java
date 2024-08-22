package com.mars.easy.admin.modules.system.pojo.po;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
/**
* <p>
* 部门领导人表
* </p>
*
* @Author 程序员Mars
* @since 2024-03-26
*/
@Data
@Table(value = "sys_dept_leader")
@Schema(description = "部门领导人表")
public class SysDeptLeader implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description ="")
    private Long id;

    @Schema(description ="dept_id")
    private Long deptId;

    @Schema(description ="sys_user_id")
    private Long leaderId;

}
