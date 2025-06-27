package com.mars.admin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mars.admin.framework.listener.EntityChangeListener;
import com.mybatisflex.annotation.*;
import com.mybatisflex.core.mask.Masks;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 系统用户实体类
 *
 * @author Mars
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(value = "sys_user", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "系统用户")
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("username")
    @JsonProperty("userName")
    private String username;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column("password")
    private String password;

    @Schema(description = "昵称")
    @Column("nickname")
    @JsonProperty("nickName")
    private String nickname;

    @Schema(description = "真实姓名")
    @Column("real_name")
    private String realName;

    @Schema(description = "邮箱")
    @Column("email")
    private String email;

    @Schema(description = "手机号")
    @ColumnMask(Masks.MOBILE)
    private String phone;

    @Schema(description = "头像URL")
    @Column("avatar")
    private String avatar;

    @Schema(description = "性别：0-未知，1-男，2-女")
    @Column("gender")
    @JsonProperty("userGender")
    private Integer gender;

    @Schema(description = "生日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column("birthday")
    private LocalDate birthday;

    @Schema(description = "状态：0-禁用，1-启用")
    @Column("status")
    private Integer status;

    @Schema(description = "用户类型：1-系统用户，2-普通用户")
    @Column("user_type")
    private Integer userType;

    @Schema(description = "登录次数")
    @Column("login_count")
    private Integer loginCount;

    @Schema(description = "最后登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column("last_login_time")
    private LocalDateTime lastLoginTime;

    @Schema(description = "最后登录IP")
    @Column("last_login_ip")
    private String lastLoginIp;

    @Schema(description = "密码修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column("password_update_time")
    private LocalDateTime passwordUpdateTime;

    @Schema(description = "备注")
    @Column("remark")
    private String remark;

    // 非数据库字段
    @Schema(description = "角色列表")
    @Column(ignore = true)
    private List<SysRole> roles;

    @Schema(description = "部门列表")
    @Column(ignore = true)
    private List<SysDept> depts;

    @Schema(description = "岗位列表")
    @Column(ignore = true)
    private List<SysPost> posts;

    @Schema(description = "角色ID列表")
    @Column(ignore = true)
    private List<Long> roleIds;

    @Schema(description = "部门ID列表")
    @Column(ignore = true)
    private List<Long> deptIds;

    @Schema(description = "岗位ID列表")
    @Column(ignore = true)
    private List<Long> postIds;

    @Schema(description = "用户路由信息")
    @Column(ignore = true)
    private List<Map<String, Object>> routes;
} 