package com.mars.admin.mapper;

import com.mars.admin.entity.SysLoginInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统登录日志Mapper接口
 * 继承 BasePlusMapper 获得更多便捷方法
 *
 * @author Mars
 */
@Mapper
public interface SysLoginInfoMapper extends BasePlusMapper<SysLoginInfo> {

    /**
     * 分页查询登录日志列表
     *
     * @param loginInfo 查询条件
     * @return 登录日志列表
     */
    List<SysLoginInfo> selectLoginInfoList(SysLoginInfo loginInfo);

    /**
     * 根据用户名查询登录日志
     *
     * @param userName 用户名
     * @return 登录日志列表
     */
    default List<SysLoginInfo> selectByUserName(String userName) {
        return selectListByField("user_name", userName);
    }

    /**
     * 根据登录状态查询登录日志
     *
     * @param status 登录状态
     * @return 登录日志列表
     */
    default List<SysLoginInfo> selectByStatus(String status) {
        return selectListByField("status", status);
    }

    /**
     * 根据登录IP查询登录日志
     *
     * @param ipaddr 登录IP
     * @return 登录日志列表
     */
    default List<SysLoginInfo> selectByIpaddr(String ipaddr) {
        return selectListByField("ipaddr", ipaddr);
    }

    /**
     * 清空登录日志
     * 
     * @return 删除的记录数
     */
    int cleanLoginInfo();

    /**
     * 根据登录地点查询登录日志
     *
     * @param loginLocation 登录地点
     * @return 登录日志列表
     */
    default List<SysLoginInfo> selectByLoginLocation(String loginLocation) {
        return selectListByFieldLike("login_location", loginLocation);
    }
} 