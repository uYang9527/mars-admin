package com.mars.admin.mapper;

import com.mars.admin.entity.SysOperLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统操作日志Mapper接口
 * 继承 BasePlusMapper 获得更多便捷方法
 *
 * @author Mars
 */
@Mapper
public interface SysOperLogMapper extends BasePlusMapper<SysOperLog> {

    /**
     * 分页查询操作日志列表
     *
     * @param operLog 查询条件
     * @return 操作日志列表
     */
    List<SysOperLog> selectOperLogList(SysOperLog operLog);

    /**
     * 根据操作人员查询操作日志
     *
     * @param operName 操作人员
     * @return 操作日志列表
     */
    default List<SysOperLog> selectByOperName(String operName) {
        return selectListByField("oper_name", operName);
    }

    /**
     * 根据业务类型查询操作日志
     *
     * @param businessType 业务类型
     * @return 操作日志列表
     */
    default List<SysOperLog> selectByBusinessType(Integer businessType) {
        return selectListByField("business_type", businessType);
    }

    /**
     * 根据状态查询操作日志
     *
     * @param status 状态
     * @return 操作日志列表
     */
    default List<SysOperLog> selectByStatus(Integer status) {
        return selectListByField("status", status);
    }

    /**
     * 清空操作日志
     */
    void cleanOperLog();

    /**
     * 根据操作IP查询操作日志
     *
     * @param operIp 操作IP
     * @return 操作日志列表
     */
    default List<SysOperLog> selectByOperIp(String operIp) {
        return selectListByField("oper_ip", operIp);
    }

    /**
     * 根据请求方式查询操作日志
     *
     * @param requestMethod 请求方式
     * @return 操作日志列表
     */
    default List<SysOperLog> selectByRequestMethod(String requestMethod) {
        return selectListByField("request_method", requestMethod);
    }
} 