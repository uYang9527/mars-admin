package com.mars.db.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.mars.common.annotation.DataScope;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据权限拦截器 (重构后支持跨模块动态加载)
 */
@Component
public class DataPermissionInterceptor implements InnerInterceptor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        MetaObject metaObject = SystemMetaObject.forObject(sh);
        MappedStatement ms = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

        if (SqlCommandType.SELECT != ms.getSqlCommandType()) {
            return;
        }

        DataScope dataScope = getDataScope(ms);
        if (dataScope == null) {
            return;
        }

        if (!StpUtil.isLogin()) {
            return;
        }

        Long userId = StpUtil.getLoginIdAsLong();
        
        // 动态获取 Mapper 解决循环依赖
        Object roleMapper = applicationContext.getBean("sysRoleMapper");
        Object userMapper = applicationContext.getBean("sysUserMapper");

        if (isAdmin(userId, roleMapper)) {
            return;
        }

        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        String originalSql = boundSql.getSql();

        String filterSql = buildDataScopeFilter(userId, dataScope, roleMapper, userMapper);
        if (StringUtils.hasText(filterSql)) {
            metaObject.setValue("delegate.boundSql.sql", "SELECT * FROM (" + originalSql + ") temp_data_scope WHERE " + filterSql);
        }
    }

    private DataScope getDataScope(MappedStatement ms) {
        try {
            String id = ms.getId();
            String className = id.substring(0, id.lastIndexOf("."));
            String methodName = id.substring(id.lastIndexOf(".") + 1);
            Class<?> clazz = Class.forName(className);
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName) && method.isAnnotationPresent(DataScope.class)) {
                    return method.getAnnotation(DataScope.class);
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    @SuppressWarnings("unchecked")
    private boolean isAdmin(Long userId, Object roleMapper) {
        try {
            Method method = roleMapper.getClass().getMethod("selectRolesByUserId", Long.class);
            List<?> roles = (List<?>) method.invoke(roleMapper, userId);
            for (Object role : roles) {
                Method getCode = role.getClass().getMethod("getCode");
                if ("admin".equals(getCode.invoke(role))) return true;
            }
        } catch (Exception ignored) {}
        return false;
    }

    @SuppressWarnings("unchecked")
    private String buildDataScopeFilter(Long userId, DataScope dataScope, Object roleMapper, Object userMapper) {
        try {
            Method selectRoles = roleMapper.getClass().getMethod("selectRolesByUserId", Long.class);
            List<?> roles = (List<?>) selectRoles.invoke(roleMapper, userId);
            if (CollectionUtils.isEmpty(roles)) return "1=0";

            List<String> conditions = new ArrayList<>();
            Long userDeptId = null;
            
            // 获取用户部门ID
            Method selectUser = userMapper.getClass().getMethod("selectById", java.io.Serializable.class);
            Object user = selectUser.invoke(userMapper, userId);
            if (user != null) {
                userDeptId = (Long) user.getClass().getMethod("getDeptId").invoke(user);
            }

            for (Object role : roles) {
                Integer scope = (Integer) role.getClass().getMethod("getDataScope").invoke(role);
                Long roleId = (Long) role.getClass().getMethod("getId").invoke(role);
                
                if (scope == null || scope == 1) return "";
                
                String deptAlias = StringUtils.hasText(dataScope.deptAlias()) ? dataScope.deptAlias() : "dept_id";
                if (scope == 2) {
                    conditions.add(deptAlias + " IN (SELECT dept_id FROM sys_role_dept WHERE role_id = " + roleId + ")");
                } else if (scope == 3 && userDeptId != null) {
                    conditions.add(deptAlias + " = " + userDeptId);
                } else if (scope == 4 && userDeptId != null) {
                    conditions.add(deptAlias + " IN (SELECT id FROM sys_dept WHERE id = " + userDeptId + " OR FIND_IN_SET(" + userDeptId + ", ancestors))");
                } else if (scope == 5) {
                    String userAlias = StringUtils.hasText(dataScope.userAlias()) ? dataScope.userAlias() : "create_by";
                    conditions.add(userAlias + " = " + userId);
                }
            }
            return conditions.isEmpty() ? "" : "(" + String.join(" OR ", conditions) + ")";
        } catch (Exception e) {
            return "";
        }
    }
}

