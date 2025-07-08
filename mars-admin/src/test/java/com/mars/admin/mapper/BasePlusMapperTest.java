package com.mars.admin.mapper;

import com.mars.admin.entity.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BasePlusMapper 测试类
 * 验证 Mapper 是否正确注册到 Spring 容器
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-01-16
 */
@SpringBootTest
public class BasePlusMapperTest {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Test
    public void testMapperInjection() {
        // 验证 Mapper 是否正确注入
        assertNotNull(sysUserMapper, "SysUserMapper should not be null");
        
        // 验证 BasePlusMapper 的方法是否可用
        long count = sysUserMapper.selectCountByQuery(null);
        assertTrue(count >= 0, "User count should be non-negative");
        
        System.out.println("✅ BasePlusMapper 测试通过，当前用户数量: " + count);
    }

    @Test
    public void testBasePlusMapperMethods() {
        // 测试 BasePlusMapper 扩展方法
        assertNotNull(sysUserMapper, "SysUserMapper should not be null");
        
        // 测试根据字段查询
        long adminCount = sysUserMapper.selectCountByField("username", "admin");
        System.out.println("✅ 根据用户名查询测试通过，admin用户数量: " + adminCount);
        
        // 测试分页查询
        var page = sysUserMapper.selectPage(1, 10);
        assertNotNull(page, "分页结果不应为空");
        System.out.println("✅ 分页查询测试通过，总记录数: " + page.getTotalRow());
    }
} 