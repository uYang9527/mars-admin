package com.mars.admin.interceptor;

import com.mars.admin.framework.interceptor.ProductionReadOnlyInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 生产环境只读拦截器测试
 *
 * @author Mars
 */
class ProductionReadOnlyInterceptorTest {

    private ProductionReadOnlyInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        interceptor = new ProductionReadOnlyInterceptor();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void testDevEnvironmentShouldAllow() throws Exception {
        // 设置开发环境
        ReflectionTestUtils.setField(interceptor, "activeProfile", "dev");
        ReflectionTestUtils.setField(interceptor, "readOnlyEnabled", true);

        request.setMethod("POST");
        request.setRequestURI("/system/user");

        boolean result = interceptor.preHandle(request, response, null);
        assertTrue(result, "开发环境应该允许所有操作");
    }

    @Test
    void testProdEnvironmentWithReadOnlyDisabledShouldAllow() throws Exception {
        // 设置生产环境但未启用只读模式
        ReflectionTestUtils.setField(interceptor, "activeProfile", "prod");
        ReflectionTestUtils.setField(interceptor, "readOnlyEnabled", false);

        request.setMethod("POST");
        request.setRequestURI("/system/user");

        boolean result = interceptor.preHandle(request, response, null);
        assertTrue(result, "生产环境未启用只读模式应该允许操作");
    }

    @Test
    void testProdEnvironmentWithGetMethodShouldAllow() throws Exception {
        // 设置生产环境并启用只读模式
        ReflectionTestUtils.setField(interceptor, "activeProfile", "prod");
        ReflectionTestUtils.setField(interceptor, "readOnlyEnabled", true);

        request.setMethod("GET");
        request.setRequestURI("/system/user/list");

        boolean result = interceptor.preHandle(request, response, null);
        assertTrue(result, "生产环境应该允许GET请求");
    }

    @Test
    void testProdEnvironmentWithPostMethodShouldBlock() throws Exception {
        // 设置生产环境并启用只读模式
        ReflectionTestUtils.setField(interceptor, "activeProfile", "prod");
        ReflectionTestUtils.setField(interceptor, "readOnlyEnabled", true);
        ReflectionTestUtils.setField(interceptor, "readOnlyMessage", "演示环境，不允许进行增删改操作");

        request.setMethod("POST");
        request.setRequestURI("/system/user");

        boolean result = interceptor.preHandle(request, response, null);
        assertFalse(result, "生产环境应该拦截POST请求");
        assertEquals(403, response.getStatus(), "应该返回403状态码");
    }

    @Test
    void testProdEnvironmentWithPutMethodShouldBlock() throws Exception {
        // 设置生产环境并启用只读模式
        ReflectionTestUtils.setField(interceptor, "activeProfile", "prod");
        ReflectionTestUtils.setField(interceptor, "readOnlyEnabled", true);

        request.setMethod("PUT");
        request.setRequestURI("/system/user");

        boolean result = interceptor.preHandle(request, response, null);
        assertFalse(result, "生产环境应该拦截PUT请求");
    }

    @Test
    void testProdEnvironmentWithDeleteMethodShouldBlock() throws Exception {
        // 设置生产环境并启用只读模式
        ReflectionTestUtils.setField(interceptor, "activeProfile", "prod");
        ReflectionTestUtils.setField(interceptor, "readOnlyEnabled", true);

        request.setMethod("DELETE");
        request.setRequestURI("/system/user/1");

        boolean result = interceptor.preHandle(request, response, null);
        assertFalse(result, "生产环境应该拦截DELETE请求");
    }

    @Test
    void testWhitelistPathsShouldAllow() throws Exception {
        // 设置生产环境并启用只读模式
        ReflectionTestUtils.setField(interceptor, "activeProfile", "prod");
        ReflectionTestUtils.setField(interceptor, "readOnlyEnabled", true);

        // 测试登录接口
        request.setMethod("POST");
        request.setRequestURI("/auth/login");

        boolean result = interceptor.preHandle(request, response, null);
        assertTrue(result, "白名单路径应该被允许");

        // 测试导出接口
        request.setRequestURI("/system/user/export");
        result = interceptor.preHandle(request, response, null);
        assertTrue(result, "导出接口应该被允许");
    }
} 