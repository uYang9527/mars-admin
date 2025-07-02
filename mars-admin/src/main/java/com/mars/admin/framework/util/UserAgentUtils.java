package com.mars.admin.framework.util;

import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户代理工具类
 * 用于解析浏览器和操作系统信息
 *
 * @author Mars
 */
public class UserAgentUtils {

    /**
     * 获取浏览器信息
     *
     * @param request HTTP请求
     * @return 浏览器信息
     */
    public static String getBrowser(HttpServletRequest request) {
        if (request == null) {
            return "Unknown";
        }

        String userAgent = request.getHeader("User-Agent");
        if (!StringUtils.hasText(userAgent)) {
            return "Unknown";
        }

        userAgent = userAgent.toLowerCase();

        if (userAgent.contains("edge")) {
            return "Microsoft Edge";
        } else if (userAgent.contains("chrome")) {
            return "Google Chrome";
        } else if (userAgent.contains("firefox")) {
            return "Mozilla Firefox";
        } else if (userAgent.contains("safari") && !userAgent.contains("chrome")) {
            return "Safari";
        } else if (userAgent.contains("opera") || userAgent.contains("opr")) {
            return "Opera";
        } else if (userAgent.contains("msie") || userAgent.contains("trident")) {
            return "Internet Explorer";
        } else {
            return "Other";
        }
    }

    /**
     * 获取操作系统信息
     *
     * @param request HTTP请求
     * @return 操作系统信息
     */
    public static String getOperatingSystem(HttpServletRequest request) {
        if (request == null) {
            return "Unknown";
        }

        String userAgent = request.getHeader("User-Agent");
        if (!StringUtils.hasText(userAgent)) {
            return "Unknown";
        }

        userAgent = userAgent.toLowerCase();

        if (userAgent.contains("windows nt 10")) {
            return "Windows 10";
        } else if (userAgent.contains("windows nt 6.3")) {
            return "Windows 8.1";
        } else if (userAgent.contains("windows nt 6.2")) {
            return "Windows 8";
        } else if (userAgent.contains("windows nt 6.1")) {
            return "Windows 7";
        } else if (userAgent.contains("windows nt 6.0")) {
            return "Windows Vista";
        } else if (userAgent.contains("windows nt 5.1")) {
            return "Windows XP";
        } else if (userAgent.contains("windows")) {
            return "Windows";
        } else if (userAgent.contains("mac os x")) {
            return "Mac OS X";
        } else if (userAgent.contains("mac")) {
            return "Mac OS";
        } else if (userAgent.contains("linux")) {
            return "Linux";
        } else if (userAgent.contains("ubuntu")) {
            return "Ubuntu";
        } else if (userAgent.contains("android")) {
            return "Android";
        } else if (userAgent.contains("iphone") || userAgent.contains("ipad")) {
            return "iOS";
        } else {
            return "Other";
        }
    }

    /**
     * 获取设备类型
     *
     * @param request HTTP请求
     * @return 设备类型
     */
    public static String getDeviceType(HttpServletRequest request) {
        if (request == null) {
            return "Unknown";
        }

        String userAgent = request.getHeader("User-Agent");
        if (!StringUtils.hasText(userAgent)) {
            return "Unknown";
        }

        userAgent = userAgent.toLowerCase();

        if (userAgent.contains("mobile") || userAgent.contains("android") || 
            userAgent.contains("iphone") || userAgent.contains("blackberry") || 
            userAgent.contains("windows phone")) {
            return "Mobile";
        } else if (userAgent.contains("tablet") || userAgent.contains("ipad")) {
            return "Tablet";
        } else {
            return "Desktop";
        }
    }

    /**
     * 根据IP地址获取地理位置（简单实现）
     * 实际项目中可以接入第三方IP地理位置服务
     *
     * @param ipaddr IP地址
     * @return 地理位置
     */
    public static String getLocationByIp(String ipaddr) {
        if (!StringUtils.hasText(ipaddr) || "unknown".equals(ipaddr)) {
            return "未知位置";
        }

        // 本地IP地址
        if ("127.0.0.1".equals(ipaddr) || "0:0:0:0:0:0:0:1".equals(ipaddr) || 
            ipaddr.startsWith("192.168.") || ipaddr.startsWith("10.") || 
            ipaddr.startsWith("172.")) {
            return "内网IP";
        }

        // 这里可以接入第三方IP地理位置服务，如：
        // 1. 淘宝IP库：http://ip.taobao.com/
        // 2. 百度IP库：https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php
        // 3. IP138：http://www.ip138.com/
        // 
        // 为了演示，这里只返回一个固定值
        // 实际使用时应该调用真实的IP地理位置API
        
        return "未知位置";
    }

    /**
     * 获取完整的用户代理信息
     *
     * @param request HTTP请求
     * @return 用户代理信息字符串
     */
    public static String getUserAgentInfo(HttpServletRequest request) {
        if (request == null) {
            return "Unknown";
        }

        String browser = getBrowser(request);
        String os = getOperatingSystem(request);
        String device = getDeviceType(request);

        return String.format("%s / %s / %s", browser, os, device);
    }
} 