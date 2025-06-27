package com.mars.admin.entity;

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
 * 系统操作日志实体类
 *
 * @author Mars
 */
@Data
@Accessors(chain = true)
@Table("sys_oper_log")
@Schema(description = "系统操作日志")
public class SysOperLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    @Schema(description = "日志主键")
    private Long id;

    @Schema(description = "模块标题")
    @Column("title")
    private String title;

    @Schema(description = "业务类型：0-其它，1-新增，2-修改，3-删除")
    @Column("business_type")
    private Integer businessType;

    @Schema(description = "方法名称")
    @Column("method")
    private String method;

    @Schema(description = "请求方式")
    @Column("request_method")
    private String requestMethod;

    @Schema(description = "操作类别：0-其它，1-后台用户，2-手机端用户")
    @Column("operator_type")
    private Integer operatorType;

    @Schema(description = "操作人员")
    @Column("oper_name")
    private String operName;

    @Schema(description = "部门名称")
    @Column("dept_name")
    private String deptName;

    @Schema(description = "请求URL")
    @Column("oper_url")
    private String operUrl;

    @Schema(description = "主机地址")
    @Column("oper_ip")
    private String operIp;

    @Schema(description = "操作地点")
    @Column("oper_location")
    private String operLocation;

    @Schema(description = "请求参数")
    @Column("oper_param")
    private String operParam;

    @Schema(description = "返回参数")
    @Column("json_result")
    private String jsonResult;

    @Schema(description = "操作状态：0-正常，1-异常")
    @Column("status")
    private Integer status;

    @Schema(description = "错误消息")
    @Column("error_msg")
    private String errorMsg;

    @Schema(description = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(value = "oper_time", onInsertValue = "now()")
    private LocalDateTime operTime;

    @Schema(description = "消耗时间")
    @Column("cost_time")
    private Long costTime;
} 