-- =============================================
-- Mars Admin RBAC权限系统设计 DDL
-- 基于角色的访问控制 (Role-Based Access Control)
-- 表名：sys开头，字段：create_by/update_by
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `mars_admin` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `mars_admin`;

-- =============================================
-- 1. 系统用户表 (sys_user)
-- =============================================
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码(MD5加密)',
    `nickname` VARCHAR(50) NULL COMMENT '昵称',
    `real_name` VARCHAR(50) NULL COMMENT '真实姓名',
    `email` VARCHAR(100) NULL COMMENT '邮箱',
    `phone` VARCHAR(20) NULL COMMENT '手机号',
    `avatar` VARCHAR(500) NULL COMMENT '头像URL',
    `gender` TINYINT(1) DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    `birthday` DATE NULL COMMENT '生日',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `user_type` TINYINT(1) DEFAULT 1 COMMENT '用户类型：1-系统用户，2-普通用户',
    `login_count` INT DEFAULT 0 COMMENT '登录次数',
    `last_login_time` DATETIME NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) NULL COMMENT '最后登录IP',
    `password_update_time` DATETIME NULL COMMENT '密码修改时间',
    `remark` VARCHAR(500) NULL COMMENT '备注',
    
    -- 审计字段
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL COMMENT '创建人',
    `update_by` BIGINT NULL COMMENT '更新人',
    `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    `delete_time` DATETIME NULL COMMENT '删除时间',
    
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    UNIQUE KEY `uk_phone` (`phone`),
    KEY `idx_status` (`status`),
    KEY `idx_user_type` (`user_type`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- =============================================
-- 2. 系统角色表 (sys_role)
-- =============================================
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
    `role_key` VARCHAR(100) NOT NULL COMMENT '角色权限字符串',
    `role_sort` INT DEFAULT 0 COMMENT '显示顺序',
    `data_scope` TINYINT(1) DEFAULT 1 COMMENT '数据范围：1-全部，2-自定义，3-本部门，4-本部门及以下，5-仅本人',
    `menu_check_strictly` TINYINT(1) DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
    `dept_check_strictly` TINYINT(1) DEFAULT 1 COMMENT '部门树选择项是否关联显示',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `is_system` TINYINT(1) DEFAULT 0 COMMENT '是否系统角色：0-否，1-是(不可删除)',
    `description` VARCHAR(200) NULL COMMENT '角色描述',
    `remark` VARCHAR(500) NULL COMMENT '备注',
    
    -- 审计字段
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL COMMENT '创建人',
    `update_by` BIGINT NULL COMMENT '更新人',
    `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    `delete_time` DATETIME NULL COMMENT '删除时间',
    
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`),
    UNIQUE KEY `uk_role_key` (`role_key`),
    KEY `idx_status` (`status`),
    KEY `idx_role_sort` (`role_sort`),
    KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色表';

-- =============================================
-- 3. 系统菜单表 (sys_menu)
-- =============================================
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父菜单ID，0为根菜单',
    `menu_name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
    `menu_code` VARCHAR(100) NOT NULL COMMENT '菜单编码',
    `menu_type` CHAR(1) NOT NULL COMMENT '菜单类型：M-目录，C-菜单，F-按钮',
    `route_name` VARCHAR(100) NULL COMMENT '路由名称',
    `route_path` VARCHAR(200) NULL COMMENT '路由地址',
    `component` VARCHAR(200) NULL COMMENT '组件路径',
    `redirect` VARCHAR(200) NULL COMMENT '重定向地址',
    `query_param` VARCHAR(255) NULL COMMENT '路由参数',
    `is_frame` TINYINT(1) DEFAULT 1 COMMENT '是否为外链：0-是，1-否',
    `is_cache` TINYINT(1) DEFAULT 0 COMMENT '是否缓存：0-缓存，1-不缓存',
    `menu_type_flag` CHAR(1) DEFAULT '' COMMENT '菜单类型标识',
    `visible` TINYINT(1) DEFAULT 1 COMMENT '菜单状态：0-隐藏，1-显示',
    `status` TINYINT(1) DEFAULT 1 COMMENT '菜单状态：0-停用，1-正常',
    `perms` VARCHAR(100) NULL COMMENT '权限标识',
    `icon` VARCHAR(100) NULL COMMENT '菜单图标',
    `order_num` INT DEFAULT 0 COMMENT '显示顺序',
    `is_system` TINYINT(1) DEFAULT 0 COMMENT '是否系统菜单：0-否，1-是(不可删除)',
    `remark` VARCHAR(500) NULL COMMENT '备注',
    
    -- 审计字段
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL COMMENT '创建人',
    `update_by` BIGINT NULL COMMENT '更新人',
    `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    `delete_time` DATETIME NULL COMMENT '删除时间',
    
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_menu_code` (`menu_code`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_menu_type` (`menu_type`),
    KEY `idx_status` (`status`),
    KEY `idx_visible` (`visible`),
    KEY `idx_order_num` (`order_num`),
    KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统菜单权限表';

-- =============================================
-- 4. 系统部门表 (sys_dept)
-- =============================================
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '部门ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父部门ID，0为根部门',
    `ancestors` VARCHAR(500) DEFAULT '' COMMENT '祖级列表',
    `dept_name` VARCHAR(50) NOT NULL COMMENT '部门名称',
    `dept_code` VARCHAR(50) NOT NULL COMMENT '部门编码',
    `order_num` INT DEFAULT 0 COMMENT '显示顺序',
    `leader` VARCHAR(50) NULL COMMENT '负责人',
    `phone` VARCHAR(20) NULL COMMENT '联系电话',
    `email` VARCHAR(100) NULL COMMENT '邮箱',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-停用，1-正常',
    `is_system` TINYINT(1) DEFAULT 0 COMMENT '是否系统部门：0-否，1-是(不可删除)',
    `remark` VARCHAR(500) NULL COMMENT '备注',
    
    -- 审计字段
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL COMMENT '创建人',
    `update_by` BIGINT NULL COMMENT '更新人',
    `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    `delete_time` DATETIME NULL COMMENT '删除时间',
    
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dept_code` (`dept_code`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_status` (`status`),
    KEY `idx_order_num` (`order_num`),
    KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统部门表';

-- =============================================
-- 5. 系统岗位表 (sys_post)
-- =============================================
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
    `post_code` VARCHAR(50) NOT NULL COMMENT '岗位编码',
    `post_name` VARCHAR(50) NOT NULL COMMENT '岗位名称',
    `post_sort` INT DEFAULT 0 COMMENT '显示顺序',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-停用，1-正常',
    `is_system` TINYINT(1) DEFAULT 0 COMMENT '是否系统岗位：0-否，1-是(不可删除)',
    `remark` VARCHAR(500) NULL COMMENT '备注',
    
    -- 审计字段
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL COMMENT '创建人',
    `update_by` BIGINT NULL COMMENT '更新人',
    `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    `delete_time` DATETIME NULL COMMENT '删除时间',
    
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_post_code` (`post_code`),
    KEY `idx_status` (`status`),
    KEY `idx_post_sort` (`post_sort`),
    KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统岗位表';

-- =============================================
-- 6. 用户角色关联表 (sys_user_role)
-- =============================================
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    
    -- 审计字段
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` BIGINT NULL COMMENT '创建人',
    
    PRIMARY KEY (`user_id`, `role_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`),
    CONSTRAINT `fk_sys_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_sys_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- =============================================
-- 7. 角色菜单关联表 (sys_role_menu)
-- =============================================
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
    
    -- 审计字段
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` BIGINT NULL COMMENT '创建人',
    
    PRIMARY KEY (`role_id`, `menu_id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_menu_id` (`menu_id`),
    CONSTRAINT `fk_sys_role_menu_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_sys_role_menu_menu` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- =============================================
-- 8. 角色部门关联表 (sys_role_dept)
-- =============================================
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept` (
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `dept_id` BIGINT NOT NULL COMMENT '部门ID',
    
    -- 审计字段
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` BIGINT NULL COMMENT '创建人',
    
    PRIMARY KEY (`role_id`, `dept_id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_dept_id` (`dept_id`),
    CONSTRAINT `fk_sys_role_dept_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_sys_role_dept_dept` FOREIGN KEY (`dept_id`) REFERENCES `sys_dept` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色和部门关联表';

-- =============================================
-- 9. 用户部门关联表 (sys_user_dept)
-- =============================================
DROP TABLE IF EXISTS `sys_user_dept`;
CREATE TABLE `sys_user_dept` (
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `dept_id` BIGINT NOT NULL COMMENT '部门ID',
    `is_main` TINYINT(1) DEFAULT 0 COMMENT '是否主部门：0-否，1-是',
    
    -- 审计字段
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` BIGINT NULL COMMENT '创建人',
    
    PRIMARY KEY (`user_id`, `dept_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_dept_id` (`dept_id`),
    KEY `idx_is_main` (`is_main`),
    CONSTRAINT `fk_sys_user_dept_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_sys_user_dept_dept` FOREIGN KEY (`dept_id`) REFERENCES `sys_dept` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户部门关联表';

-- =============================================
-- 10. 用户岗位关联表 (sys_user_post)
-- =============================================
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post` (
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `post_id` BIGINT NOT NULL COMMENT '岗位ID',
    
    -- 审计字段
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` BIGINT NULL COMMENT '创建人',
    
    PRIMARY KEY (`user_id`, `post_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_post_id` (`post_id`),
    CONSTRAINT `fk_sys_user_post_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_sys_user_post_post` FOREIGN KEY (`post_id`) REFERENCES `sys_post` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户岗位关联表';

-- =============================================
-- 11. 系统配置表 (sys_config)
-- =============================================
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '参数主键',
    `config_name` VARCHAR(100) NOT NULL COMMENT '参数名称',
    `config_key` VARCHAR(100) NOT NULL COMMENT '参数键名',
    `config_value` VARCHAR(500) NOT NULL COMMENT '参数键值',
    `config_type` CHAR(1) DEFAULT 'N' COMMENT '系统内置：Y-是，N-否',
    `remark` VARCHAR(500) NULL COMMENT '备注',
    
    -- 审计字段
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL COMMENT '创建人',
    `update_by` BIGINT NULL COMMENT '更新人',
    `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    `delete_time` DATETIME NULL COMMENT '删除时间',
    
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`),
    KEY `idx_config_type` (`config_type`),
    KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='参数配置表';

-- =============================================
-- 12. 系统字典类型表 (sys_dict_type)
-- =============================================
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典主键',
    `dict_name` VARCHAR(100) NOT NULL COMMENT '字典名称',
    `dict_type` VARCHAR(100) NOT NULL COMMENT '字典类型',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-停用，1-正常',
    `remark` VARCHAR(500) NULL COMMENT '备注',
    
    -- 审计字段
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL COMMENT '创建人',
    `update_by` BIGINT NULL COMMENT '更新人',
    `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    `delete_time` DATETIME NULL COMMENT '删除时间',
    
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dict_type` (`dict_type`),
    KEY `idx_status` (`status`),
    KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典类型表';

-- =============================================
-- 13. 系统字典数据表 (sys_dict_data)
-- =============================================
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典编码',
    `dict_sort` INT DEFAULT 0 COMMENT '字典排序',
    `dict_label` VARCHAR(100) NOT NULL COMMENT '字典标签',
    `dict_value` VARCHAR(100) NOT NULL COMMENT '字典键值',
    `dict_type` VARCHAR(100) NOT NULL COMMENT '字典类型',
    `css_class` VARCHAR(100) NULL COMMENT '样式属性',
    `list_class` VARCHAR(100) NULL COMMENT '表格回显样式',
    `is_default` CHAR(1) DEFAULT 'N' COMMENT '是否默认：Y-是，N-否',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-停用，1-正常',
    `remark` VARCHAR(500) NULL COMMENT '备注',
    
    -- 审计字段
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL COMMENT '创建人',
    `update_by` BIGINT NULL COMMENT '更新人',
    `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    `delete_time` DATETIME NULL COMMENT '删除时间',
    
    PRIMARY KEY (`id`),
    KEY `idx_dict_type` (`dict_type`),
    KEY `idx_dict_sort` (`dict_sort`),
    KEY `idx_status` (`status`),
    KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典数据表';

-- =============================================
-- 14. 系统操作日志表 (sys_oper_log)
-- =============================================
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志主键',
    `title` VARCHAR(50) NULL COMMENT '模块标题',
    `business_type` INT DEFAULT 0 COMMENT '业务类型：0-其它，1-新增，2-修改，3-删除',
    `method` VARCHAR(100) NULL COMMENT '方法名称',
    `request_method` VARCHAR(10) NULL COMMENT '请求方式',
    `operator_type` INT DEFAULT 0 COMMENT '操作类别：0-其它，1-后台用户，2-手机端用户',
    `oper_name` VARCHAR(50) NULL COMMENT '操作人员',
    `dept_name` VARCHAR(50) NULL COMMENT '部门名称',
    `oper_url` VARCHAR(255) NULL COMMENT '请求URL',
    `oper_ip` VARCHAR(128) NULL COMMENT '主机地址',
    `oper_location` VARCHAR(255) NULL COMMENT '操作地点',
    `oper_param` VARCHAR(2000) NULL COMMENT '请求参数',
    `json_result` VARCHAR(2000) NULL COMMENT '返回参数',
    `status` INT DEFAULT 0 COMMENT '操作状态：0-正常，1-异常',
    `error_msg` VARCHAR(2000) NULL COMMENT '错误消息',
    `oper_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `cost_time` BIGINT DEFAULT 0 COMMENT '消耗时间',
    
    PRIMARY KEY (`id`),
    KEY `idx_business_type` (`business_type`),
    KEY `idx_status` (`status`),
    KEY `idx_oper_time` (`oper_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志记录';

-- =============================================
-- 15. 系统登录日志表 (sys_logininfor)
-- =============================================
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '访问ID',
    `user_name` VARCHAR(50) NULL COMMENT '用户账号',
    `ipaddr` VARCHAR(128) NULL COMMENT '登录IP地址',
    `login_location` VARCHAR(255) NULL COMMENT '登录地点',
    `browser` VARCHAR(50) NULL COMMENT '浏览器类型',
    `os` VARCHAR(50) NULL COMMENT '操作系统',
    `status` CHAR(1) DEFAULT '0' COMMENT '登录状态：0-成功，1-失败',
    `msg` VARCHAR(255) NULL COMMENT '提示消息',
    `login_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
    
    PRIMARY KEY (`id`),
    KEY `idx_user_name` (`user_name`),
    KEY `idx_status` (`status`),
    KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统访问记录';

-- =============================================
-- 初始化基础数据
-- =============================================

-- 插入超级管理员用户
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `real_name`, `email`, `status`, `user_type`, `remark`, `create_by`)
VALUES (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', '超级管理员', '系统管理员', 'admin@mars.com', 1, 1,  '系统默认超级管理员', 1);

-- 插入默认角色
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `role_key`, `role_sort`, `data_scope`, `status`, `is_system`, `description`, `create_by`) 
VALUES 
(1, '超级管理员', 'SUPER_ADMIN', 'admin', 1, 1, 1, 1, '超级管理员', 1),
(2, '系统管理员', 'SYSTEM_ADMIN', 'system', 2, 2, 1, 1, '系统管理员', 1),
(3, '普通用户', 'COMMON_USER', 'common', 3, 5, 1, 0, '普通用户', 1);

-- 插入默认部门
INSERT INTO `sys_dept` (`id`, `parent_id`, `ancestors`, `dept_name`, `dept_code`, `order_num`, `leader`, `status`, `is_system`, `create_by`) 
VALUES 
(100, 0, '0', '总公司', 'HEAD_OFFICE', 0, 'admin', 1, 1, 1),
(101, 100, '0,100', '技术部', 'TECH_DEPT', 1, 'admin', 1, 1, 1),
(102, 100, '0,100', '运营部', 'OPERATION_DEPT', 2, 'admin', 1, 1, 1),
(103, 100, '0,100', '财务部', 'FINANCE_DEPT', 3, 'admin', 1, 1, 1);

-- 插入默认岗位
INSERT INTO `sys_post` (`id`, `post_code`, `post_name`, `post_sort`, `status`, `is_system`, `create_by`) 
VALUES 
(1, 'CEO', '董事长', 1, 1, 1, 1),
(2, 'CTO', '技术总监', 2, 1, 1, 1),
(3, 'DEV', '开发工程师', 3, 1, 1, 1),
(4, 'TEST', '测试工程师', 4, 1, 1, 1);

-- 插入系统菜单
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_code`, `menu_type`, `route_name`, `route_path`, `component`, `perms`, `icon`, `order_num`, `visible`, `status`, `is_system`, `create_by`) 
VALUES 
-- 一级菜单
(1, 0, '系统管理', 'SYSTEM', 'M', 'System', '/system', 'Layout', '', 'system', 1, 1, 1, 1, 1),
(2, 0, '系统监控', 'MONITOR', 'M', 'Monitor', '/monitor', 'Layout', '', 'monitor', 2, 1, 1, 1, 1),
(3, 0, '系统工具', 'TOOL', 'M', 'Tool', '/tool', 'Layout', '', 'tool', 3, 1, 1, 1, 1),

-- 系统管理子菜单
(100, 1, '用户管理', 'USER_MANAGE', 'C', 'User', 'user', 'system/user/index', 'system:user:list', 'user', 1, 1, 1, 1, 1),
(101, 1, '角色管理', 'ROLE_MANAGE', 'C', 'Role', 'role', 'system/role/index', 'system:role:list', 'peoples', 2, 1, 1, 1, 1),
(102, 1, '菜单管理', 'MENU_MANAGE', 'C', 'Menu', 'menu', 'system/menu/index', 'system:menu:list', 'tree-table', 3, 1, 1, 1, 1),
(103, 1, '部门管理', 'DEPT_MANAGE', 'C', 'Dept', 'dept', 'system/dept/index', 'system:dept:list', 'tree', 4, 1, 1, 1, 1),
(104, 1, '岗位管理', 'POST_MANAGE', 'C', 'Post', 'post', 'system/post/index', 'system:post:list', 'post', 5, 1, 1, 1, 1),
(105, 1, '字典管理', 'DICT_MANAGE', 'C', 'Dict', 'dict', 'system/dict/index', 'system:dict:list', 'dict', 6, 1, 1, 1, 1),
(106, 1, '参数设置', 'CONFIG_MANAGE', 'C', 'Config', 'config', 'system/config/index', 'system:config:list', 'edit', 7, 1, 1, 1, 1),

-- 用户管理按钮
(1000, 100, '用户查询', 'USER_QUERY', 'F', '', '', '', 'system:user:query', '', 1, 1, 1, 1, 1),
(1001, 100, '用户新增', 'USER_ADD', 'F', '', '', '', 'system:user:add', '', 2, 1, 1, 1, 1),
(1002, 100, '用户修改', 'USER_EDIT', 'F', '', '', '', 'system:user:edit', '', 3, 1, 1, 1, 1),
(1003, 100, '用户删除', 'USER_DELETE', 'F', '', '', '', 'system:user:remove', '', 4, 1, 1, 1, 1),
(1004, 100, '用户导出', 'USER_EXPORT', 'F', '', '', '', 'system:user:export', '', 5, 1, 1, 1, 1),
(1005, 100, '用户导入', 'USER_IMPORT', 'F', '', '', '', 'system:user:import', '', 6, 1, 1, 1, 1),
(1006, 100, '重置密码', 'USER_RESET_PWD', 'F', '', '', '', 'system:user:resetPwd', '', 7, 1, 1, 1, 1),

-- 角色管理按钮
(1007, 101, '角色查询', 'ROLE_QUERY', 'F', '', '', '', 'system:role:query', '', 1, 1, 1, 1, 1),
(1008, 101, '角色新增', 'ROLE_ADD', 'F', '', '', '', 'system:role:add', '', 2, 1, 1, 1, 1),
(1009, 101, '角色修改', 'ROLE_EDIT', 'F', '', '', '', 'system:role:edit', '', 3, 1, 1, 1, 1),
(1010, 101, '角色删除', 'ROLE_DELETE', 'F', '', '', '', 'system:role:remove', '', 4, 1, 1, 1, 1),
(1011, 101, '角色导出', 'ROLE_EXPORT', 'F', '', '', '', 'system:role:export', '', 5, 1, 1, 1, 1),

-- 菜单管理按钮
(1012, 102, '菜单查询', 'MENU_QUERY', 'F', '', '', '', 'system:menu:query', '', 1, 1, 1, 1, 1),
(1013, 102, '菜单新增', 'MENU_ADD', 'F', '', '', '', 'system:menu:add', '', 2, 1, 1, 1, 1),
(1014, 102, '菜单修改', 'MENU_EDIT', 'F', '', '', '', 'system:menu:edit', '', 3, 1, 1, 1, 1),
(1015, 102, '菜单删除', 'MENU_DELETE', 'F', '', '', '', 'system:menu:remove', '', 4, 1, 1, 1, 1);

-- 分配用户角色
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `create_by`) VALUES (1, 1, 1);

-- 分配用户部门
INSERT INTO `sys_user_dept` (`user_id`, `dept_id`, `is_main`, `create_by`) VALUES (1, 100, 1, 1);

-- 分配用户岗位
INSERT INTO `sys_user_post` (`user_id`, `post_id`, `create_by`) VALUES (1, 1, 1);

-- 给超级管理员角色分配所有菜单权限
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_by`) 
SELECT 1, id, 1 FROM `sys_menu` WHERE `is_deleted` = 0;

-- 插入系统配置
INSERT INTO `sys_config` (`config_name`, `config_key`, `config_value`, `config_type`, `remark`, `create_by`) 
VALUES 
('主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow', 1),
('用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', '初始化密码 123456', 1),
('主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', '深色主题theme-dark，浅色主题theme-light', 1);

-- 插入字典类型
INSERT INTO `sys_dict_type` (`dict_name`, `dict_type`, `status`, `remark`, `create_by`) 
VALUES 
('用户性别', 'sys_user_sex', 1, '用户性别列表', 1),
('菜单状态', 'sys_show_hide', 1, '菜单状态列表', 1),
('系统开关', 'sys_normal_disable', 1, '系统开关列表', 1),
('任务状态', 'sys_job_status', 1, '任务状态列表', 1),
('任务分组', 'sys_job_group', 1, '任务分组列表', 1),
('系统是否', 'sys_yes_no', 1, '系统是否列表', 1);

-- 插入字典数据
INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `remark`, `create_by`) 
VALUES 
(1, '男', '0', 'sys_user_sex', '', '', 'Y', 1, '性别男', 1),
(2, '女', '1', 'sys_user_sex', '', '', 'N', 1, '性别女', 1),
(3, '未知', '2', 'sys_user_sex', '', '', 'N', 1, '性别未知', 1),
(1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', 1, '显示菜单', 1),
(2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', 1, '隐藏菜单', 1),
(1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', 1, '正常状态', 1),
(2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', 1, '停用状态', 1),
(1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', 1, '系统默认是', 1),
(2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', 1, '系统默认否', 1);

-- =============================================
-- 创建视图
-- =============================================

-- 用户详情视图
CREATE OR REPLACE VIEW `v_sys_user_detail` AS
SELECT 
    u.id,
    u.username,
    u.nickname,
    u.real_name,
    u.email,
    u.phone,
    u.avatar,
    u.gender,
    u.birthday,
    u.status,
    u.user_type,
    u.login_count,
    u.last_login_time,
    u.last_login_ip,
    u.create_time,
    u.update_time,
    GROUP_CONCAT(DISTINCT r.role_name) AS role_names,
    GROUP_CONCAT(DISTINCT d.dept_name) AS dept_names,
    GROUP_CONCAT(DISTINCT p.post_name) AS post_names
FROM sys_user u
LEFT JOIN sys_user_role ur ON u.id = ur.user_id
LEFT JOIN sys_role r ON ur.role_id = r.id AND r.is_deleted = 0
LEFT JOIN sys_user_dept ud ON u.id = ud.user_id
LEFT JOIN sys_dept d ON ud.dept_id = d.id AND d.is_deleted = 0
LEFT JOIN sys_user_post up ON u.id = up.user_id
LEFT JOIN sys_post p ON up.post_id = p.id AND p.is_deleted = 0
WHERE u.is_deleted = 0
GROUP BY u.id;

-- 菜单树视图
CREATE OR REPLACE VIEW `v_sys_menu_tree` AS
SELECT 
    m.id,
    m.parent_id,
    m.menu_name,
    m.menu_code,
    m.menu_type,
    m.route_name,
    m.route_path,
    m.component,
    m.perms,
    m.icon,
    m.order_num,
    m.visible,
    m.status,
    pm.menu_name AS parent_name
FROM sys_menu m
LEFT JOIN sys_menu pm ON m.parent_id = pm.id
WHERE m.is_deleted = 0
ORDER BY m.parent_id, m.order_num;

-- =============================================
-- 完成
-- ============================================= 