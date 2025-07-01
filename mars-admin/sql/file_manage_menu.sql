-- 文件管理菜单数据
INSERT INTO `sys_menu` (
    `id`, `parent_id`, `menu_name`, `menu_code`, `menu_type`, `route_name`, `route_path`, 
    `component`, `redirect`, `query_param`, `is_frame`, `is_cache`, `menu_type_flag`, 
    `visible`, `status`, `perms`, `icon`, `order_num`, `is_system`, `remark`,
    `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`
) VALUES 
-- 文件管理主菜单
(
    301, 0, '文件管理', 'file_manage', 'M', 'file-manage', '/file-manage', 
    '', NULL, NULL, 1, 0, '', 
    1, 1, '', 'ic:round-folder', 301, 0, '文件管理主菜单',
    NOW(), NOW(), 1, 1, 0
),
-- 文件列表
(
    302, 301, '文件列表', 'file_list', 'C', 'manage_file-manage', '/manage/file-manage', 
    'view.manage_file-manage', NULL, NULL, 1, 0, '', 
    1, 1, 'system:file:list', 'ic:round-insert-drive-file', 1, 0, '文件列表管理',
    NOW(), NOW(), 1, 1, 0
),
-- OSS配置管理
(
    303, 301, 'OSS配置', 'oss_config', 'C', 'manage_oss-config', '/manage/oss-config', 
    'view.manage_oss-config', NULL, NULL, 1, 0, '', 
    1, 1, 'system:ossConfig:list', 'ic:round-storage', 2, 0, 'OSS配置管理',
    NOW(), NOW(), 1, 1, 0
);

-- 文件管理权限按钮
INSERT INTO `sys_menu` (
    `id`, `parent_id`, `menu_name`, `menu_code`, `menu_type`, `route_name`, `route_path`, 
    `component`, `redirect`, `query_param`, `is_frame`, `is_cache`, `menu_type_flag`, 
    `visible`, `status`, `perms`, `icon`, `order_num`, `is_system`, `remark`,
    `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`
) VALUES 
-- 文件管理按钮权限
(30201, 302, '文件上传', 'file_upload', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:file:upload', '', 1, 0, '文件上传权限', NOW(), NOW(), 1, 1, 0),
(30202, 302, '文件下载', 'file_download', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:file:download', '', 2, 0, '文件下载权限', NOW(), NOW(), 1, 1, 0),
(30203, 302, '文件删除', 'file_delete', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:file:delete', '', 3, 0, '文件删除权限', NOW(), NOW(), 1, 1, 0),
(30204, 302, '文件预览', 'file_preview', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:file:preview', '', 4, 0, '文件预览权限', NOW(), NOW(), 1, 1, 0),

-- OSS配置管理按钮权限  
(30301, 303, '配置新增', 'oss_config_add', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:ossConfig:add', '', 1, 0, 'OSS配置新增权限', NOW(), NOW(), 1, 1, 0),
(30302, 303, '配置编辑', 'oss_config_edit', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:ossConfig:edit', '', 2, 0, 'OSS配置编辑权限', NOW(), NOW(), 1, 1, 0),
(30303, 303, '配置删除', 'oss_config_remove', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:ossConfig:remove', '', 3, 0, 'OSS配置删除权限', NOW(), NOW(), 1, 1, 0),
(30304, 303, '配置查询', 'oss_config_query', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:ossConfig:query', '', 4, 0, 'OSS配置查询权限', NOW(), NOW(), 1, 1, 0),
(30305, 303, '配置测试', 'oss_config_test', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:ossConfig:test', '', 5, 0, 'OSS配置测试权限', NOW(), NOW(), 1, 1, 0); 