-- 更新菜单数据脚本
-- 修正路由名称、路径和组件配置以符合前端ElegantRoute格式

-- 更新系统管理一级菜单
UPDATE sys_menu SET 
    route_name = 'manage',
    route_path = '/manage',
    component = 'layout.base'
WHERE menu_name = '系统管理' AND parent_id = 0;

-- 更新用户管理菜单
UPDATE sys_menu SET 
    route_name = 'manage_user',
    route_path = '/manage/user',
    component = 'view.manage_user'
WHERE menu_name = '用户管理' AND menu_code = 'USER_MANAGE';

-- 更新角色管理菜单
UPDATE sys_menu SET 
    route_name = 'manage_role',
    route_path = '/manage/role',
    component = 'view.manage_role'
WHERE menu_name = '角色管理' AND menu_code = 'ROLE_MANAGE';

-- 更新菜单管理菜单
UPDATE sys_menu SET 
    route_name = 'manage_menu',
    route_path = '/manage/menu',
    component = 'view.manage_menu'
WHERE menu_name = '菜单管理' AND menu_code = 'MENU_MANAGE';

-- 更新部门管理菜单
UPDATE sys_menu SET 
    route_name = 'manage_dept',
    route_path = '/manage/dept',
    component = 'view.manage_dept'
WHERE menu_name = '部门管理' AND menu_code = 'DEPT_MANAGE';

-- 更新岗位管理菜单
UPDATE sys_menu SET 
    route_name = 'manage_post',
    route_path = '/manage/post',
    component = 'view.manage_post'
WHERE menu_name = '岗位管理' AND menu_code = 'POST_MANAGE';

-- 更新字典管理菜单
UPDATE sys_menu SET 
    route_name = 'manage_dict',
    route_path = '/manage/dict',
    component = 'view.manage_dict'
WHERE menu_name = '字典管理' AND menu_code = 'DICT_MANAGE';

-- 更新参数设置菜单
UPDATE sys_menu SET 
    route_name = 'manage_config',
    route_path = '/manage/config',
    component = 'view.manage_config'
WHERE menu_name = '参数设置' AND menu_code = 'CONFIG_MANAGE';

-- 更新系统监控菜单
UPDATE sys_menu SET 
    route_name = 'monitor',
    route_path = '/monitor',
    component = 'layout.base'
WHERE menu_name = '系统监控' AND parent_id = 0;

-- 更新系统工具菜单
UPDATE sys_menu SET 
    route_name = 'tool',
    route_path = '/tool',
    component = 'layout.base'
WHERE menu_name = '系统工具' AND parent_id = 0;

-- 查看更新后的菜单数据
SELECT id, parent_id, menu_name, menu_type, route_name, route_path, component, order_num 
FROM sys_menu 
WHERE is_deleted = 0 
ORDER BY parent_id, order_num; 