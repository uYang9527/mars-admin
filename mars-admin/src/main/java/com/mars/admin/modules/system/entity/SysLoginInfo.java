package com.mars.admin.modules.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统登录日志实体类
 *
 * @author Mars
 */
@Data
@Accessors(chain = true)
@Table("sys_logininfor")
@Schema(description = "系统登录日志")
public class SysLoginInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description = "访问ID")
    private Long id;

    @Schema(description = "用户账号")
    @Column("user_name")
    private String userName;

    @Schema(description = "登录IP地址")
    @Column("ipaddr")
    private String ipaddr;

    @Schema(description = "登录地点")
    @Column("login_location")
    private String loginLocation;

    @Schema(description = "浏览器类型")
    @Column("browser")
    private String browser;

    @Schema(description = "操作系统")
    @Column("os")
    private String os;

    @Schema(description = "登录状态：0-成功，1-失败")
    @Column("status")
    private String status;

    @Schema(description = "提示消息")
    @Column("msg")
    private String msg;

    @Schema(description = "访问时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(value = "login_time", onInsertValue = "now()")
    private LocalDateTime loginTime;
}
