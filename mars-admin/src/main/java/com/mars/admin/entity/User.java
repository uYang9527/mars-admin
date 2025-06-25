package com.mars.admin.entity;

import com.mars.admin.framework.listener.EntityChangeListener;
import com.mybatisflex.annotation.*;
import com.mybatisflex.core.mask.Masks;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Schema(name = "用户实体", description = "用户信息")
@Accessors(chain = true)
@Table(value = "tb_user", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    @Schema(description = "用户ID", example = "1")
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 脱敏规则
     */
    @Schema(description = "用户名", example = "张三", requiredMode = Schema.RequiredMode.REQUIRED)
    @ColumnMask(Masks.CHINESE_NAME)
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column
    private String password;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱", example = "zhangsan@example.com")
    @Column
    private String email;

    /**
     * 手机
     */
    @Schema(description = "手机号", example = "13800138000")
    @Column
    private String phone;


}