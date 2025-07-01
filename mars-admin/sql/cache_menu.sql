-- 缓存管理菜单配置
-- 添加缓存监控和缓存列表菜单到系统管理模块下

-- 缓存监控菜单
INSERT INTO `sys_menu` VALUES (
    107, -- 菜单ID
    1,   -- 父级菜单ID (系统管理)
    '缓存监控', -- 菜单名称
    'CACHE_MONITOR', -- 菜单编码
    'C', -- 菜单类型 (C=菜单)
    'manage_cache-monitor', -- 路由名称
    '/manage/cache-monitor', -- 路由路径
    'view.manage_cache-monitor', -- 组件路径
    NULL, -- 查询参数
    NULL, -- 是否外链
    1,    -- 状态 (1=正常)
    0,    -- 是否隐藏 (0=显示)
    '',   -- 权限标识
    1,    -- 是否显示
    1,    -- 是否外链
    'system:cache:monitor', -- 权限标识
    'mdi:monitor-dashboard', -- 图标
    8,    -- 排序
    1,    -- 显示状态
    NULL, -- 备注
    NOW(), -- 创建时间
    NOW(), -- 更新时间
    1,    -- 创建人
    NULL, -- 更新人
    0,    -- 删除标志
    NULL  -- 菜单类型标识
);

-- 缓存列表菜单
INSERT INTO `sys_menu` VALUES (
    108, -- 菜单ID
    1,   -- 父级菜单ID (系统管理)
    '缓存列表', -- 菜单名称
    'CACHE_LIST', -- 菜单编码
    'C', -- 菜单类型 (C=菜单)
    'manage_cache-list', -- 路由名称
    '/manage/cache-list', -- 路由路径
    'view.manage_cache-list', -- 组件路径
    NULL, -- 查询参数
    NULL, -- 是否外链
    1,    -- 状态 (1=正常)
    0,    -- 是否隐藏 (0=显示)
    '',   -- 权限标识
    1,    -- 是否显示
    1,    -- 是否外链
    'system:cache:list', -- 权限标识
    'mdi:database-search', -- 图标
    9,    -- 排序
    1,    -- 显示状态
    NULL, -- 备注
    NOW(), -- 创建时间
    NOW(), -- 更新时间
    1,    -- 创建人
    NULL, -- 更新人
    0,    -- 删除标志
    NULL  -- 菜单类型标识
);

-- 缓存监控相关权限按钮
INSERT INTO `sys_menu` VALUES (1070, 107, '缓存监控查询', 'CACHE_MONITOR_QUERY', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:monitor:query', '', 1, 1, NULL, NOW(), NOW(), 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1071, 107, '缓存监控刷新', 'CACHE_MONITOR_REFRESH', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:monitor:refresh', '', 2, 1, NULL, NOW(), NOW(), 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1072, 107, '缓存统计查询', 'CACHE_STATS_QUERY', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:stats:query', '', 3, 1, NULL, NOW(), NOW(), 1, NULL, 0, NULL);

-- 缓存列表相关权限按钮
INSERT INTO `sys_menu` VALUES (1080, 108, '缓存列表查询', 'CACHE_LIST_QUERY', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:list:query', '', 1, 1, NULL, NOW(), NOW(), 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1081, 108, '缓存详情查看', 'CACHE_DETAIL_VIEW', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:detail:view', '', 2, 1, NULL, NOW(), NOW(), 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1082, 108, '缓存删除', 'CACHE_DELETE', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:delete', '', 3, 1, NULL, NOW(), NOW(), 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1083, 108, '缓存批量删除', 'CACHE_BATCH_DELETE', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:batch:delete', '', 4, 1, NULL, NOW(), NOW(), 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1084, 108, '缓存空间清空', 'CACHE_SPACE_CLEAR', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:space:clear', '', 5, 1, NULL, NOW(), NOW(), 1, NULL, 0, NULL);

-- 为超级管理员角色分配缓存管理权限
INSERT INTO `sys_role_menu` VALUES (1, 107); -- 缓存监控
INSERT INTO `sys_role_menu` VALUES (1, 108); -- 缓存列表
INSERT INTO `sys_role_menu` VALUES (1, 1070); -- 缓存监控查询
INSERT INTO `sys_role_menu` VALUES (1, 1071); -- 缓存监控刷新
INSERT INTO `sys_role_menu` VALUES (1, 1072); -- 缓存统计查询
INSERT INTO `sys_role_menu` VALUES (1, 1080); -- 缓存列表查询
INSERT INTO `sys_role_menu` VALUES (1, 1081); -- 缓存详情查看
INSERT INTO `sys_role_menu` VALUES (1, 1082); -- 缓存删除
INSERT INTO `sys_role_menu` VALUES (1, 1083); -- 缓存批量删除
INSERT INTO `sys_role_menu` VALUES (1, 1084); -- 缓存空间清空

-- 查询验证菜单是否添加成功
SELECT 
    m.id,
    m.parent_id,
    m.menu_name,
    m.menu_code,
    m.route_name,
    m.route_path,
    m.component,
    m.perms,
    m.icon,
    m.order_num,
    pm.menu_name as parent_menu_name
FROM sys_menu m 
LEFT JOIN sys_menu pm ON m.parent_id = pm.id 
WHERE m.menu_code IN ('CACHE_MONITOR', 'CACHE_LIST')
   OR m.parent_id IN (107, 108)
ORDER BY m.parent_id, m.order_num; 