package com.mars.admin.service;

import com.mars.admin.entity.SysLoginInfo;
import com.mybatisflex.core.paginate.Page;

import java.util.List;

/**
 * 系统登录日志Service接口
 * 继承 BaseService 获得更多便捷方法
 *
 * @author Mars
 */
public interface ISysLoginInfoService extends BaseService<SysLoginInfo> {

    /**
     * 分页查询登录日志列表
     *
     * @param page 分页参数
     * @param loginInfo 查询条件
     * @return 登录日志分页列表
     */
    Page<SysLoginInfo> selectLoginInfoPage(Page<SysLoginInfo> page, SysLoginInfo loginInfo);

    /**
     * 根据用户名查询登录日志
     *
     * @param userName 用户名
     * @return 登录日志列表
     */
    List<SysLoginInfo> selectByUserName(String userName);

    /**
     * 根据登录状态查询登录日志
     *
     * @param status 登录状态
     * @return 登录日志列表
     */
    List<SysLoginInfo> selectByStatus(String status);

    /**
     * 根据登录IP查询登录日志
     *
     * @param ipaddr 登录IP
     * @return 登录日志列表
     */
    List<SysLoginInfo> selectByIpaddr(String ipaddr);

    /**
     * 根据登录地点查询登录日志
     *
     * @param loginLocation 登录地点
     * @return 登录日志列表
     */
    List<SysLoginInfo> selectByLoginLocation(String loginLocation);

    /**
     * 新增登录日志
     *
     * @param loginInfo 登录日志信息
     * @return 新增结果
     */
    boolean insertLoginInfo(SysLoginInfo loginInfo);

    /**
     * 删除登录日志
     *
     * @param loginInfoIds 登录日志ID数组
     * @return 删除结果
     */
    boolean deleteLoginInfos(Long[] loginInfoIds);

    /**
     * 清空登录日志
     *
     * @return 清空结果
     */
    boolean cleanLoginInfo();

    /**
     * 导出登录日志
     *
     * @param loginInfo 查询条件
     * @return 登录日志列表
     */
    List<SysLoginInfo> exportLoginInfo(SysLoginInfo loginInfo);

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status 登录状态：0-失败，1-成功，2-退出
     * @param message 登录信息
     * @param ipaddr IP地址
     */
    void recordLoginInfo(String username, String status, String message, String ipaddr);

    /**
     * 记录登录信息（包含详细设备信息）
     *
     * @param username 用户名
     * @param status 登录状态：0-失败，1-成功，2-退出
     * @param message 登录信息
     * @param ipaddr IP地址
     * @param browser 浏览器信息
     * @param os 操作系统信息
     * @param location 登录地点
     */
    void recordLoginInfo(String username, String status, String message, String ipaddr, 
                        String browser, String os, String location);
} 