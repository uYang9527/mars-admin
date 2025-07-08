package com.mars.admin.framework.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * IP工具类
 * 获取客户端真实IP地址
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-07-05
 */
public class IpUtils {

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IPV4 = "127.0.0.1";
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    /**
     * 获取客户端IP地址
     * 
     * @param request 请求对象
     * @return IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }

        String ip = null;
        try {
            // 1. 优先获取X-Forwarded-For头
            ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                // 2. 获取Proxy-Client-IP头
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                // 3. 获取WL-Proxy-Client-IP头
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                // 4. 获取HTTP_CLIENT_IP头
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                // 5. 获取HTTP_X_FORWARDED_FOR头
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                // 6. 获取X-Real-IP头
                ip = request.getHeader("X-Real-IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                // 7. 最后获取RemoteAddr
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ip = UNKNOWN;
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP，多个IP按照','分割
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(0, ip.indexOf(","));
        }

        // 如果是IPv6的本地回环地址，转换为IPv4
        if (LOCALHOST_IPV6.equals(ip)) {
            ip = LOCALHOST_IPV4;
        }

        return ip;
    }

    /**
     * 判断IP地址是否为内网地址
     * 
     * @param ip IP地址
     * @return 是否为内网地址
     */
    public static boolean isInternalIp(String ip) {
        if (ip == null || ip.length() == 0) {
            return false;
        }

        try {
            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }

            int a = Integer.parseInt(parts[0]);
            int b = Integer.parseInt(parts[1]);
            
            // 10.0.0.0 ~ 10.255.255.255
            if (a == 10) {
                return true;
            }
            
            // 172.16.0.0 ~ 172.31.255.255
            if (a == 172 && b >= 16 && b <= 31) {
                return true;
            }
            
            // 192.168.0.0 ~ 192.168.255.255
            if (a == 192 && b == 168) {
                return true;
            }
            
            // 127.0.0.0 ~ 127.255.255.255
            if (a == 127) {
                return true;
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取本机IP地址
     * 
     * @return 本机IP地址
     */
    public static String getLocalIp() {
        try {
            java.net.InetAddress addr = java.net.InetAddress.getLocalHost();
            return addr.getHostAddress();
        } catch (Exception e) {
            return LOCALHOST_IPV4;
        }
    }
} 