-- 添加 API 访问统计菜单（系统监控下）
-- 执行: mysql -u root -p mars < sql/upgrade/api-access-menu.sql

INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `permission`, `icon`, `sort`, `visible`, `status`, `is_frame`, `create_time`, `update_time`, `create_by`, `update_by`, `deleted`)
SELECT 283, 36, 'API访问统计', 2, '/monitor/api-access', '/monitor/api-access/index', 'monitor:apiAccess:list', 'StatsChartOutline', 7, 1, 1, 0, NOW(), NOW(), NULL, NULL, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE id = 283);

-- 为超级管理员(role_id=1)、普通管理员(role_id=2)分配菜单
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) SELECT 1, 283 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id = 1 AND menu_id = 283);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) SELECT 2, 283 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id = 2 AND menu_id = 283);
