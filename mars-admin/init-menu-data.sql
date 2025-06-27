-- 菜单初始化数据
-- 如果数据库中没有菜单数据，可以执行这个脚本

-- 删除现有菜单数据（可选）
-- DELETE FROM sys_menu WHERE id > 0;

-- 插入系统菜单
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_code`, `menu_type`, `route_name`, `route_path`, `component`, `perms`, `icon`, `order_num`, `visible`, `status`, `is_system`, `create_by`) 
VALUES 
-- 一级菜单
(1, 0, '系统管理', 'SYSTEM', 'M', 'manage', '/manage', 'layout.base', '', 'carbon:cloud-service-management', 1, 1, 1, 1, 1),

-- 系统管理子菜单
(100, 1, '用户管理', 'USER_MANAGE', 'C', 'manage_user', 'user', 'view.manage_user', 'system:user:list', 'ic:round-manage-accounts', 1, 1, 1, 1, 1),
(101, 1, '角色管理', 'ROLE_MANAGE', 'C', 'manage_role', 'role', 'view.manage_role', 'system:role:list', 'carbon:user-role', 2, 1, 1, 1, 1),
(102, 1, '菜单管理', 'MENU_MANAGE', 'C', 'manage_menu', 'menu', 'view.manage_menu', 'system:menu:list', 'material-symbols:route', 3, 1, 1, 1, 1);

-- 给超级管理员角色分配所有菜单权限
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`, `create_by`) 
SELECT 1, id, 1 FROM `sys_menu` WHERE `is_deleted` = 0; 