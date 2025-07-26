package com.mars.admin.modules.system.service;

import com.mars.admin.modules.system.entity.SysOperLog;
import com.mars.admin.modules.base.service.BaseService;
import com.mybatisflex.core.paginate.Page;

import java.util.List;

/**
 * 系统操作日志Service接口
 * 继承 BaseService 获得更多便捷方法
 *
 * @author Mars
 */
public interface ISysOperLogService extends BaseService<SysOperLog> {

    /**
     * 分页查询操作日志列表
     *
     * @param page 分页参数
     * @param operLog 查询条件
     * @return 操作日志分页列表
     */
    Page<SysOperLog> selectOperLogPage(Page<SysOperLog> page, SysOperLog operLog);

    /**
     * 根据操作人员查询操作日志
     *
     * @param operName 操作人员
     * @return 操作日志列表
     */
    List<SysOperLog> selectByOperName(String operName);

    /**
     * 根据业务类型查询操作日志
     *
     * @param businessType 业务类型
     * @return 操作日志列表
     */
    List<SysOperLog> selectByBusinessType(Integer businessType);

    /**
     * 根据状态查询操作日志
     *
     * @param status 状态
     * @return 操作日志列表
     */
    List<SysOperLog> selectByStatus(Integer status);

    /**
     * 根据操作IP查询操作日志
     *
     * @param operIp 操作IP
     * @return 操作日志列表
     */
    List<SysOperLog> selectByOperIp(String operIp);

    /**
     * 新增操作日志
     *
     * @param operLog 操作日志信息
     * @return 新增结果
     */
    boolean insertOperLog(SysOperLog operLog);

    /**
     * 删除操作日志
     *
     * @param operLogIds 操作日志ID数组
     * @return 删除结果
     */
    boolean deleteOperLogs(Long[] operLogIds);

    /**
     * 清空操作日志
     *
     * @return 清空结果
     */
    boolean cleanOperLog();

    /**
     * 导出操作日志
     *
     * @param operLog 查询条件
     * @return 操作日志列表
     */
    List<SysOperLog> exportOperLog(SysOperLog operLog);
}
