/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : mars_admin

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 09/07/2025 16:35:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'N' COMMENT '系统内置：Y-是，N-否',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_config_key`(`config_key` ASC) USING BTREE,
  INDEX `idx_config_type`(`config_type` ASC) USING BTREE,
  INDEX `idx_is_deleted`(`is_deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', '初始化密码 123456', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', '深色主题theme-dark，浅色主题theme-light', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门ID，0为根部门',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '部门名称',
  `dept_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '部门编码',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-停用，1-正常',
  `is_system` tinyint(1) NULL DEFAULT 0 COMMENT '是否系统部门：0-否，1-是(不可删除)',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_dept_code`(`dept_code` ASC) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_order_num`(`order_num` ASC) USING BTREE,
  INDEX `idx_is_deleted`(`is_deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 109 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '总公司', 'HEAD_OFFICE', 0, 'admin', '18483678969', '123@qq.com', 1, 1, '1212111', '2025-06-25 16:34:16', '2025-06-28 23:29:14', 1, 1, 0, NULL);
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '技术部', 'TECH_DEPT', 1, 'admin', '18483678969', '123@qq.com', 1, 1, NULL, '2025-06-25 16:34:16', '2025-06-28 22:10:01', 1, NULL, 0, NULL);
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '运营部', 'OPERATION_DEPT', 2, 'admin', '18483678969', '123@qq.com', 1, 1, NULL, '2025-06-25 16:34:16', '2025-06-28 22:10:01', 1, NULL, 0, NULL);
INSERT INTO `sys_dept` VALUES (103, 100, '0,100', '财务部', 'FINANCE_DEPT', 3, 'admin', '18483678969', '123@qq.com', 1, 1, NULL, '2025-06-25 16:34:16', '2025-06-28 22:11:17', 1, NULL, 1, '2025-06-28 22:11:17');
INSERT INTO `sys_dept` VALUES (104, 100, '0,100', '11', '212', 0, '12', '18483678369', '123@qq.com', 1, NULL, '1212', '2025-06-28 22:16:30', '2025-06-28 23:27:43', 1, NULL, 1, '2025-06-28 23:27:43');
INSERT INTO `sys_dept` VALUES (105, 100, '0,100', '11', '11', 22, '11', '18483678369', '123@qq.com', 1, NULL, '', '2025-06-28 23:29:45', '2025-06-28 23:30:16', 1, NULL, 1, '2025-06-28 23:30:16');
INSERT INTO `sys_dept` VALUES (108, 100, '0,100', '11', '2121233', 0, '12', '18483678369', '123@qq.com', 1, NULL, '', '2025-06-28 23:33:59', '2025-06-28 23:34:17', 1, NULL, 1, '2025-06-28 23:34:17');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '样式属性',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'N' COMMENT '是否默认：Y-是，N-否',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-停用，1-正常',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_dict_type`(`dict_type` ASC) USING BTREE,
  INDEX `idx_dict_sort`(`dict_sort` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_is_deleted`(`is_deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', 1, '性别男', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', 1, '性别女', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', 1, '性别未知', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', 1, '显示菜单', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', 1, '隐藏菜单', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', 1, '正常状态', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', 1, '停用状态', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_dict_data` VALUES (8, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', 1, '系统默认是', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_dict_data` VALUES (9, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', 1, '系统默认否', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_dict_data` VALUES (10, 0, '1', '1', 'test', '', '', 'N', 1, '', '2025-07-08 13:54:37', '2025-07-08 13:54:41', NULL, NULL, 1, '2025-07-08 13:54:41');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典类型',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-停用，1-正常',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_dict_type`(`dict_type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_is_deleted`(`is_deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', 1, '用户性别列表', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', 1, '菜单状态列表', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', 1, '系统开关列表', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', 1, '任务状态列表', '2025-06-25 16:34:16', '2025-07-01 11:43:42', 1, NULL, 1, '2025-07-01 11:43:42');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', 1, '任务分组列表', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', 1, '系统是否列表', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_dict_type` VALUES (7, '测试', 'test', 1, '', '2025-07-08 13:54:29', '2025-07-08 13:54:44', 1, NULL, 1, '2025-07-08 13:54:44');

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '登录状态：0-成功，1-失败',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_name`(`user_name` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_login_time`(`login_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 144 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统访问记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------
INSERT INTO `sys_logininfor` VALUES (1, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '0', '密码错误', '2025-06-25 21:04:58');
INSERT INTO `sys_logininfor` VALUES (2, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '0', '密码错误', '2025-06-25 21:05:07');
INSERT INTO `sys_logininfor` VALUES (3, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '0', '密码错误', '2025-06-25 21:06:52');
INSERT INTO `sys_logininfor` VALUES (4, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-25 21:07:03');
INSERT INTO `sys_logininfor` VALUES (5, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-25 22:35:10');
INSERT INTO `sys_logininfor` VALUES (6, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-25 22:55:40');
INSERT INTO `sys_logininfor` VALUES (7, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-25 22:59:21');
INSERT INTO `sys_logininfor` VALUES (8, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-25 23:02:06');
INSERT INTO `sys_logininfor` VALUES (9, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '2', '退出成功', '2025-06-25 23:04:17');
INSERT INTO `sys_logininfor` VALUES (10, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-25 23:04:30');
INSERT INTO `sys_logininfor` VALUES (11, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-25 23:05:48');
INSERT INTO `sys_logininfor` VALUES (12, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-25 23:06:14');
INSERT INTO `sys_logininfor` VALUES (13, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 14:12:05');
INSERT INTO `sys_logininfor` VALUES (14, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 14:12:26');
INSERT INTO `sys_logininfor` VALUES (15, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 14:12:38');
INSERT INTO `sys_logininfor` VALUES (16, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 14:12:52');
INSERT INTO `sys_logininfor` VALUES (17, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 14:22:11');
INSERT INTO `sys_logininfor` VALUES (18, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 14:29:03');
INSERT INTO `sys_logininfor` VALUES (19, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 14:29:15');
INSERT INTO `sys_logininfor` VALUES (20, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 14:56:07');
INSERT INTO `sys_logininfor` VALUES (21, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 15:12:03');
INSERT INTO `sys_logininfor` VALUES (22, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 15:20:28');
INSERT INTO `sys_logininfor` VALUES (23, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 17:11:27');
INSERT INTO `sys_logininfor` VALUES (24, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 17:17:44');
INSERT INTO `sys_logininfor` VALUES (25, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 17:22:15');
INSERT INTO `sys_logininfor` VALUES (26, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 17:22:31');
INSERT INTO `sys_logininfor` VALUES (27, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 17:25:10');
INSERT INTO `sys_logininfor` VALUES (28, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 17:29:20');
INSERT INTO `sys_logininfor` VALUES (29, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 17:29:27');
INSERT INTO `sys_logininfor` VALUES (30, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 17:31:04');
INSERT INTO `sys_logininfor` VALUES (31, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 17:35:42');
INSERT INTO `sys_logininfor` VALUES (32, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 17:35:52');
INSERT INTO `sys_logininfor` VALUES (33, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 17:43:50');
INSERT INTO `sys_logininfor` VALUES (34, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 17:43:57');
INSERT INTO `sys_logininfor` VALUES (35, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 17:44:10');
INSERT INTO `sys_logininfor` VALUES (36, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 17:44:52');
INSERT INTO `sys_logininfor` VALUES (37, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 17:45:46');
INSERT INTO `sys_logininfor` VALUES (38, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-26 17:52:00');
INSERT INTO `sys_logininfor` VALUES (39, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-28 10:59:37');
INSERT INTO `sys_logininfor` VALUES (40, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-28 11:03:23');
INSERT INTO `sys_logininfor` VALUES (41, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-28 11:17:13');
INSERT INTO `sys_logininfor` VALUES (42, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-28 21:36:03');
INSERT INTO `sys_logininfor` VALUES (43, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-28 21:36:30');
INSERT INTO `sys_logininfor` VALUES (44, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-28 23:27:29');
INSERT INTO `sys_logininfor` VALUES (45, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-30 21:48:56');
INSERT INTO `sys_logininfor` VALUES (46, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-30 21:58:53');
INSERT INTO `sys_logininfor` VALUES (47, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-30 22:01:00');
INSERT INTO `sys_logininfor` VALUES (48, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-06-30 22:04:20');
INSERT INTO `sys_logininfor` VALUES (49, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 14:03:26');
INSERT INTO `sys_logininfor` VALUES (50, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 14:07:10');
INSERT INTO `sys_logininfor` VALUES (51, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 14:09:06');
INSERT INTO `sys_logininfor` VALUES (52, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 14:13:59');
INSERT INTO `sys_logininfor` VALUES (53, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 14:16:22');
INSERT INTO `sys_logininfor` VALUES (54, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 14:22:15');
INSERT INTO `sys_logininfor` VALUES (55, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 14:25:02');
INSERT INTO `sys_logininfor` VALUES (56, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 14:26:55');
INSERT INTO `sys_logininfor` VALUES (57, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 14:27:25');
INSERT INTO `sys_logininfor` VALUES (58, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 14:28:37');
INSERT INTO `sys_logininfor` VALUES (59, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 14:32:37');
INSERT INTO `sys_logininfor` VALUES (60, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 14:35:21');
INSERT INTO `sys_logininfor` VALUES (61, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 14:40:18');
INSERT INTO `sys_logininfor` VALUES (62, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 14:49:36');
INSERT INTO `sys_logininfor` VALUES (63, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 14:56:28');
INSERT INTO `sys_logininfor` VALUES (64, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 15:05:29');
INSERT INTO `sys_logininfor` VALUES (65, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 15:11:43');
INSERT INTO `sys_logininfor` VALUES (66, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 15:18:02');
INSERT INTO `sys_logininfor` VALUES (67, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 15:37:26');
INSERT INTO `sys_logininfor` VALUES (68, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 15:45:31');
INSERT INTO `sys_logininfor` VALUES (69, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 15:46:22');
INSERT INTO `sys_logininfor` VALUES (70, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 15:50:37');
INSERT INTO `sys_logininfor` VALUES (71, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 15:55:13');
INSERT INTO `sys_logininfor` VALUES (72, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 15:58:24');
INSERT INTO `sys_logininfor` VALUES (73, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 16:07:09');
INSERT INTO `sys_logininfor` VALUES (74, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 16:07:12');
INSERT INTO `sys_logininfor` VALUES (75, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 16:08:58');
INSERT INTO `sys_logininfor` VALUES (76, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 16:14:17');
INSERT INTO `sys_logininfor` VALUES (77, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 16:50:11');
INSERT INTO `sys_logininfor` VALUES (78, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 17:26:20');
INSERT INTO `sys_logininfor` VALUES (79, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 17:37:35');
INSERT INTO `sys_logininfor` VALUES (80, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 17:43:08');
INSERT INTO `sys_logininfor` VALUES (81, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 18:05:14');
INSERT INTO `sys_logininfor` VALUES (82, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 19:32:45');
INSERT INTO `sys_logininfor` VALUES (83, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 20:10:55');
INSERT INTO `sys_logininfor` VALUES (84, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 20:11:04');
INSERT INTO `sys_logininfor` VALUES (85, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 20:11:16');
INSERT INTO `sys_logininfor` VALUES (86, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 20:14:37');
INSERT INTO `sys_logininfor` VALUES (87, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 20:16:14');
INSERT INTO `sys_logininfor` VALUES (88, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 20:19:12');
INSERT INTO `sys_logininfor` VALUES (89, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 20:25:12');
INSERT INTO `sys_logininfor` VALUES (90, 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '1', '登录成功', '2025-07-01 22:17:41');
INSERT INTO `sys_logininfor` VALUES (91, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-03 13:55:36');
INSERT INTO `sys_logininfor` VALUES (92, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-03 14:24:48');
INSERT INTO `sys_logininfor` VALUES (93, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-03 14:42:17');
INSERT INTO `sys_logininfor` VALUES (94, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-03 14:47:27');
INSERT INTO `sys_logininfor` VALUES (95, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-03 14:48:07');
INSERT INTO `sys_logininfor` VALUES (96, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-03 14:48:28');
INSERT INTO `sys_logininfor` VALUES (97, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-03 14:48:52');
INSERT INTO `sys_logininfor` VALUES (98, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-03 14:50:08');
INSERT INTO `sys_logininfor` VALUES (99, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-03 14:51:01');
INSERT INTO `sys_logininfor` VALUES (100, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-03 14:52:15');
INSERT INTO `sys_logininfor` VALUES (101, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-03 14:53:41');
INSERT INTO `sys_logininfor` VALUES (102, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-03 14:54:25');
INSERT INTO `sys_logininfor` VALUES (103, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-03 14:58:41');
INSERT INTO `sys_logininfor` VALUES (104, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-03 15:48:20');
INSERT INTO `sys_logininfor` VALUES (105, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-03 16:01:37');
INSERT INTO `sys_logininfor` VALUES (106, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-03 16:48:44');
INSERT INTO `sys_logininfor` VALUES (107, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-03 16:49:58');
INSERT INTO `sys_logininfor` VALUES (108, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-03 17:06:47');
INSERT INTO `sys_logininfor` VALUES (109, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-03 17:08:45');
INSERT INTO `sys_logininfor` VALUES (110, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-04 09:56:06');
INSERT INTO `sys_logininfor` VALUES (111, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-04 10:36:19');
INSERT INTO `sys_logininfor` VALUES (112, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '密码错误', '2025-07-04 11:10:58');
INSERT INTO `sys_logininfor` VALUES (113, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-04 11:11:04');
INSERT INTO `sys_logininfor` VALUES (114, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-04 11:11:36');
INSERT INTO `sys_logininfor` VALUES (115, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-04 13:46:54');
INSERT INTO `sys_logininfor` VALUES (116, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-04 13:56:25');
INSERT INTO `sys_logininfor` VALUES (117, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-04 14:43:37');
INSERT INTO `sys_logininfor` VALUES (118, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-04 15:57:49');
INSERT INTO `sys_logininfor` VALUES (119, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-04 16:55:58');
INSERT INTO `sys_logininfor` VALUES (120, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-08 11:25:55');
INSERT INTO `sys_logininfor` VALUES (121, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-08 11:31:28');
INSERT INTO `sys_logininfor` VALUES (122, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-08 11:33:37');
INSERT INTO `sys_logininfor` VALUES (123, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-08 11:50:33');
INSERT INTO `sys_logininfor` VALUES (124, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-08 11:52:34');
INSERT INTO `sys_logininfor` VALUES (125, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-08 11:54:26');
INSERT INTO `sys_logininfor` VALUES (126, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-08 11:58:15');
INSERT INTO `sys_logininfor` VALUES (127, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-08 12:00:39');
INSERT INTO `sys_logininfor` VALUES (128, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-08 13:49:02');
INSERT INTO `sys_logininfor` VALUES (129, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-08 13:55:48');
INSERT INTO `sys_logininfor` VALUES (130, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-08 14:26:58');
INSERT INTO `sys_logininfor` VALUES (131, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-08 15:27:08');
INSERT INTO `sys_logininfor` VALUES (132, 'admin', '0:0:0:0:0:0:0:1', '内网IP', 'Google Chrome', 'Windows 10', '0', '登录成功', '2025-07-08 15:55:32');
INSERT INTO `sys_logininfor` VALUES (133, 'admin', '127.0.0.1', '内网IP', 'Google Chrome', 'Windows 10', '0', 'PC端登录成功', '2025-07-08 17:07:08');
INSERT INTO `sys_logininfor` VALUES (134, 'admin', '127.0.0.1', '内网IP', 'Microsoft Edge', 'Windows 10', '0', 'PC端登录成功', '2025-07-08 17:34:41');
INSERT INTO `sys_logininfor` VALUES (135, 'admin', '127.0.0.1', '内网IP', 'Microsoft Edge', 'Windows 10', '0', 'PC端登录成功', '2025-07-08 17:38:47');
INSERT INTO `sys_logininfor` VALUES (136, 'admin', '127.0.0.1', '内网IP', 'Google Chrome', 'Windows 10', '0', 'PC端登录成功', '2025-07-09 15:29:31');
INSERT INTO `sys_logininfor` VALUES (137, 'admin', '127.0.0.1', '内网IP', 'Google Chrome', 'Windows 10', '0', 'PC端登录成功', '2025-07-09 15:46:15');
INSERT INTO `sys_logininfor` VALUES (138, 'admin', '127.0.0.1', '内网IP', 'Google Chrome', 'Windows 10', '0', 'PC端登录成功', '2025-07-09 15:47:45');
INSERT INTO `sys_logininfor` VALUES (139, 'admin', '127.0.0.1', '内网IP', 'Google Chrome', 'Windows 10', '0', 'PC端登录成功', '2025-07-09 15:49:53');
INSERT INTO `sys_logininfor` VALUES (140, 'admin', '127.0.0.1', '内网IP', 'Google Chrome', 'Windows 10', '0', 'PC端登录成功', '2025-07-09 15:54:13');
INSERT INTO `sys_logininfor` VALUES (141, 'admin', '127.0.0.1', '内网IP', 'Microsoft Edge', 'Windows 10', '0', 'PC端登录成功', '2025-07-09 15:54:24');
INSERT INTO `sys_logininfor` VALUES (142, 'admin', '127.0.0.1', '内网IP', 'Google Chrome', 'Windows 10', '0', 'PC端登录成功', '2025-07-09 15:54:30');
INSERT INTO `sys_logininfor` VALUES (143, 'admin', '127.0.0.1', '内网IP', 'Google Chrome', 'Windows 10', '0', 'PC端登录成功', '2025-07-09 16:25:08');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID，0为根菜单',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `menu_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单编码',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单类型：M-目录，C-菜单，F-按钮',
  `route_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路由名称',
  `route_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路由地址',
  `component` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组件路径',
  `redirect` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '重定向地址',
  `query_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路由参数',
  `is_frame` tinyint(1) NULL DEFAULT 1 COMMENT '是否为外链：0-是，1-否',
  `is_cache` tinyint(1) NULL DEFAULT 0 COMMENT '是否缓存：0-缓存，1-不缓存',
  `menu_type_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '菜单类型标识',
  `visible` tinyint(1) NULL DEFAULT 1 COMMENT '菜单状态：0-隐藏，1-显示',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '菜单状态：0-停用，1-正常',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `is_system` tinyint(1) NULL DEFAULT 0 COMMENT '是否系统菜单：0-否，1-是(不可删除)',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_menu_code`(`menu_code` ASC) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_menu_type`(`menu_type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_visible`(`visible` ASC) USING BTREE,
  INDEX `idx_order_num`(`order_num` ASC) USING BTREE,
  INDEX `idx_is_deleted`(`is_deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30312 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', 'SYSTEM', 'M', 'manage', '/manage', 'layout.base', NULL, NULL, 1, 0, '', 1, 1, '', 'hugeicons:ai-setting', 1, 1, NULL, '2025-06-25 16:34:16', '2025-07-08 14:46:38', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (2, 0, '系统运维', 'MONITOR', 'M', 'monitor', '/monitor', 'layout.base', NULL, NULL, 1, 0, '', 1, 1, '', 'pepicons-pencil:monitor', 2, 1, NULL, '2025-06-25 16:34:16', '2025-07-08 14:29:08', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (3, 0, '系统工具', 'TOOL', 'M', 'tool', '/tool', 'layout.base', NULL, NULL, 1, 0, '', 1, 1, '', 'tool', 3, 1, NULL, '2025-06-25 16:34:16', '2025-07-08 13:59:51', 1, NULL, 1, '2025-07-08 13:59:51');
INSERT INTO `sys_menu` VALUES (100, 1018, '用户列表', 'USER_MANAGE', 'C', 'manage_user', '/manage/user', 'view.manage_user', NULL, NULL, 1, 0, '', 1, 1, 'system:user:list', 'tabler:users', 1, 1, NULL, '2025-06-25 16:34:16', '2025-07-08 14:51:22', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (101, 1017, '角色管理', 'ROLE_MANAGE', 'C', 'manage_role', '/manage/role', 'view.manage_role', NULL, NULL, 1, 0, '', 1, 1, 'system:role:list', 'peoples', 2, 1, NULL, '2025-06-25 16:34:16', '2025-06-30 21:54:17', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (102, 1017, '菜单管理', 'MENU_MANAGE', 'C', 'manage_menu', '/manage/menu', 'view.manage_menu', NULL, NULL, 1, 0, '', 1, 1, 'system:menu:list', 'bi:menu-button-wide', 3, 1, NULL, '2025-06-25 16:34:16', '2025-07-08 14:35:09', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (103, 30306, '部门管理', 'DEPT_MANAGE', 'C', 'manage_dept', '/manage/dept', 'view.manage_dept', NULL, NULL, 1, 0, '', 1, 1, 'system:dept:list', 'fluent:organization-32-regular', 4, 1, NULL, '2025-06-25 16:34:16', '2025-07-08 14:23:43', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (104, 30306, '岗位管理', 'POST_MANAGE', 'C', 'manage_post', '/manage/post', 'view.manage_post', NULL, NULL, 1, 0, '', 1, 1, 'system:post:list', 'post', 5, 1, NULL, '2025-06-25 16:34:16', '2025-07-03 15:46:58', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (105, 1, '字典管理', 'DICT_MANAGE', 'C', 'manage_dict', '/manage/dict', 'view.manage_dict', NULL, NULL, 1, 0, '', 1, 1, 'system:dict:list', 'dict', 6, 1, NULL, '2025-06-25 16:34:16', '2025-06-26 15:02:40', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (106, 1, '参数设置', 'CONFIG_MANAGE', 'C', 'manage_config', '/manage/config', 'view.manage_config', NULL, NULL, 1, 0, '', 1, 1, 'system:config:list', 'edit', 7, 1, NULL, '2025-06-25 16:34:16', '2025-06-26 15:02:49', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (107, 2, '缓存监控', 'CACHE_MONITOR', 'C', 'manage_cache-monitor', '/manage/cache-monitor', 'view.manage_cache-monitor', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:monitor', 'material-symbols:monitor-heart-outline', 8, 1, NULL, '2025-07-01 15:02:21', '2025-07-08 14:21:07', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (108, 2, '缓存列表', 'CACHE_LIST', 'C', 'manage_cache-list', '/manage/cache-list', 'view.manage_cache-list', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:list', 'mdi:database-search', 9, 1, NULL, '2025-07-01 15:02:21', '2025-07-04 15:07:53', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (109, 2, '系统监控', 'SYSTEM_MONITOR', 'C', 'manage_system-monitor', '/manage/system-monitor', 'view.manage_system-monitor', NULL, NULL, 1, 0, '', 1, 1, 'system:monitor:info', 'pepicons-pencil:monitor', 1, 0, NULL, '2025-07-04 14:47:37', '2025-07-08 14:17:20', NULL, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (301, 0, '文件管理', 'FILE_MANAGE', 'M', 'file-manage', '/file-manage', '', NULL, NULL, 1, 0, '', 1, 1, '', 'clarity:directory-line', 11, 0, '文件管理主菜单', '2025-07-01 20:04:43', '2025-07-08 15:04:14', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (302, 301, '文件列表', 'FILE_LIST', 'C', 'manage_file-manage', '/manage/file-manage', 'view.manage_file-manage', NULL, NULL, 1, 0, '', 1, 1, 'system:file:list', 'octicon:file-directory-open-fill-24', 1, 0, '文件列表管理', '2025-07-01 20:04:43', '2025-07-08 14:49:57', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (303, 301, 'OSS配置', 'OSS_CONFIG', 'C', 'manage_oss-config', '/manage/oss-config', 'view.manage_oss-config', NULL, NULL, 1, 0, '', 1, 1, 'system:ossConfig:list', 'ic:round-storage', 2, 0, 'OSS配置管理', '2025-07-01 20:04:43', '2025-07-01 20:24:49', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (1000, 100, '用户查询', 'USER_QUERY', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:user:query', '', 1, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1001, 100, '用户新增', 'USER_ADD', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:user:add', '', 2, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1002, 100, '用户修改', 'USER_EDIT', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:user:edit', '', 3, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1003, 100, '用户删除', 'USER_DELETE', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:user:remove', '', 4, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1004, 100, '用户导出', 'USER_EXPORT', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:user:export', '', 5, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1005, 100, '用户导入', 'USER_IMPORT', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:user:import', '', 6, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1006, 100, '重置密码', 'USER_RESET_PWD', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:user:resetPwd', '', 7, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1007, 101, '角色查询', 'ROLE_QUERY', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:role:query', '', 1, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1008, 101, '角色新增', 'ROLE_ADD', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:role:add', '', 2, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1009, 101, '角色修改', 'ROLE_EDIT', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:role:edit', '', 3, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1010, 101, '角色删除', 'ROLE_DELETE', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:role:remove', '', 4, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1011, 101, '角色导出', 'ROLE_EXPORT', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:role:export', '', 5, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1012, 102, '菜单查询', 'MENU_QUERY', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:menu:query', '', 1, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1013, 102, '菜单新增', 'MENU_ADD', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:menu:add', '', 2, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1014, 102, '菜单修改', 'MENU_EDIT', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:menu:edit', '', 3, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1015, 102, '菜单删除', 'MENU_DELETE', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:menu:remove', '', 4, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1016, 0, '首页', 'HOME', 'C', 'home', '/home', 'layout.base$view.home', NULL, NULL, 1, 0, '', 1, 1, NULL, 'ic:baseline-home', 0, 1, NULL, '2025-06-26 16:06:53', '2025-07-08 14:07:38', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1017, 0, '权限管理', 'POWER', 'M', 'power', '/power', '', '', NULL, 1, 0, '', 1, 1, '', 'icon-park-outline:permissions', 20, NULL, NULL, '2025-06-30 21:51:59', '2025-07-08 14:33:08', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1018, 0, '用户管理', 'USER', 'M', 'user', '/user', '', '', NULL, 1, 0, '', 1, 1, '', 'tabler:users', 30, NULL, NULL, '2025-06-30 21:56:53', '2025-07-08 14:51:11', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (1070, 107, '缓存监控查询', 'CACHE_MONITOR_QUERY', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:monitor:query', '', 1, 1, NULL, '2025-07-01 15:02:52', '2025-07-01 15:02:52', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1071, 107, '缓存监控刷新', 'CACHE_MONITOR_REFRESH', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:monitor:refresh', '', 2, 1, NULL, '2025-07-01 15:02:52', '2025-07-01 15:02:52', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1072, 107, '缓存统计查询', 'CACHE_STATS_QUERY', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:stats:query', '', 3, 1, NULL, '2025-07-01 15:02:52', '2025-07-01 15:02:52', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1080, 108, '缓存列表查询', 'CACHE_LIST_QUERY', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:list:query', '', 1, 1, NULL, '2025-07-01 15:02:58', '2025-07-01 15:02:58', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1081, 108, '缓存详情查看', 'CACHE_DETAIL_VIEW', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:detail:view', '', 2, 1, NULL, '2025-07-01 15:02:58', '2025-07-01 15:02:58', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1082, 108, '缓存删除', 'CACHE_DELETE', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:delete', '', 3, 1, NULL, '2025-07-01 15:02:58', '2025-07-01 15:02:58', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1083, 108, '缓存批量删除', 'CACHE_BATCH_DELETE', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:batch:delete', '', 4, 1, NULL, '2025-07-01 15:02:58', '2025-07-01 15:02:58', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1084, 108, '缓存空间清空', 'CACHE_SPACE_CLEAR', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:space:clear', '', 5, 1, NULL, '2025-07-01 15:02:58', '2025-07-01 15:02:58', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (2100, 0, '日志管理', 'LOG_MANAGE', 'M', 'log-manage', '/log-manage', '', NULL, NULL, 1, 0, '', 1, 1, '', 'ix:log', 10, 0, '日志管理目录', '2025-01-02 10:00:00', '2025-07-08 14:25:14', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (2101, 2100, '操作日志', 'OPERATION_LOG', 'C', 'manage_operation-log', '/manage/operation-log', 'view.manage_operation-log', NULL, NULL, 1, 0, '', 1, 1, 'system:operlog:list', 'ix:log', 1, 0, '操作日志菜单', '2025-01-02 10:00:00', '2025-07-08 14:35:46', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (2102, 2100, '登录日志', 'LOGIN_LOG', 'C', 'manage_login-log', '/manage/login-log', 'view.manage_login-log', NULL, NULL, 1, 0, '', 1, 1, 'system:logininfor:list', 'ix:log', 2, 0, '登录日志菜单', '2025-01-02 10:00:00', '2025-07-02 13:58:40', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (2103, 2101, '操作查询', 'OPERLOG_QUERY', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:operlog:query', '', 1, 0, '操作日志查询权限', '2025-01-02 10:00:00', '2025-01-02 10:00:00', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (2104, 2101, '操作删除', 'OPERLOG_REMOVE', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:operlog:remove', '', 2, 0, '操作日志删除权限', '2025-01-02 10:00:00', '2025-01-02 10:00:00', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (2105, 2101, '日志导出', 'OPERLOG_EXPORT', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:operlog:export', '', 3, 0, '操作日志导出权限', '2025-01-02 10:00:00', '2025-01-02 10:00:00', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (2106, 2101, '详情查看', 'OPERLOG_DETAIL', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:operlog:detail', '', 4, 0, '操作日志详情权限', '2025-01-02 10:00:00', '2025-01-02 10:00:00', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (2107, 2101, '日志清空', 'OPERLOG_CLEAR', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:operlog:clear', '', 5, 0, '操作日志清空权限', '2025-01-02 10:00:00', '2025-01-02 10:00:00', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (2108, 2102, '登录查询', 'LOGININFOR_QUERY', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:logininfor:query', '', 1, 0, '登录日志查询权限', '2025-01-02 10:00:00', '2025-01-02 10:00:00', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (2109, 2102, '登录删除', 'LOGININFOR_REMOVE', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:logininfor:remove', '', 2, 0, '登录日志删除权限', '2025-01-02 10:00:00', '2025-01-02 10:00:00', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (2110, 2102, '日志导出', 'LOGININFOR_EXPORT', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:logininfor:export', '', 3, 0, '登录日志导出权限', '2025-01-02 10:00:00', '2025-01-02 10:00:00', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (2111, 2102, '账户解锁', 'LOGININFOR_UNLOCK', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:logininfor:unlock', '', 4, 0, '登录日志解锁权限', '2025-01-02 10:00:00', '2025-01-02 10:00:00', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (2112, 2102, '详情查看', 'LOGININFOR_DETAIL', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:logininfor:detail', '', 5, 0, '登录日志详情权限', '2025-01-02 10:00:00', '2025-01-02 10:00:00', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (2113, 2102, '日志清空', 'LOGININFOR_CLEAR', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:logininfor:clear', '', 6, 0, '登录日志清空权限', '2025-01-02 10:00:00', '2025-01-02 10:00:00', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30201, 302, '文件上传', 'FILE_UPLOAD', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:file:upload', '', 1, 0, '文件上传权限', '2025-07-01 20:04:43', '2025-07-01 20:22:45', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30202, 302, '文件下载', 'FILE_DOWNLOAD', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:file:download', '', 2, 0, '文件下载权限', '2025-07-01 20:04:43', '2025-07-01 20:22:55', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30203, 302, '文件删除', 'FILE_DELETE', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:file:delete', '', 3, 0, '文件删除权限', '2025-07-01 20:04:43', '2025-07-01 20:23:06', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30204, 302, '文件预览', 'FILE_PREVIEW', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:file:preview', '', 4, 0, '文件预览权限', '2025-07-01 20:04:43', '2025-07-01 20:23:15', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30301, 303, '配置新增', 'OSS_COMFIG_ADD', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:ossConfig:add', '', 1, 0, 'OSS配置新增权限', '2025-07-01 20:04:43', '2025-07-01 20:23:29', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30302, 303, '配置编辑', 'OSS_COMFIG_EDIT', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:ossConfig:edit', '', 2, 0, 'OSS配置编辑权限', '2025-07-01 20:04:43', '2025-07-01 20:24:13', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30303, 303, '配置删除', 'OSS_COMFIG_REMOVE', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:ossConfig:remove', '', 3, 0, 'OSS配置删除权限', '2025-07-01 20:04:43', '2025-07-01 20:24:05', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30304, 303, '配置查询', 'OSS_COMFIG_QUERY', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:ossConfig:query', '', 4, 0, 'OSS配置查询权限', '2025-07-01 20:04:43', '2025-07-01 20:24:00', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30305, 303, '配置测试', 'OSS_COMFIG_TEST', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:ossConfig:test', '', 5, 0, 'OSS配置测试权限', '2025-07-01 20:04:43', '2025-07-01 20:23:54', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30306, 0, '组织管理', 'org', 'M', 'org', '/org', '', '', NULL, 1, 0, '', 1, 1, '', 'eos-icons:organization-outlined', 3, 0, NULL, '2025-07-03 15:46:07', '2025-07-08 14:22:46', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30307, 0, '系统运维', 'maintenance', 'M', 'maintenance', '/maintenance', '', '', NULL, 0, 0, '', 1, 1, '', 'pepicons-pencil:monitor', 0, 0, NULL, '2025-07-03 15:59:01', '2025-07-08 14:26:17', 1, NULL, 1, '2025-07-08 11:50:10');
INSERT INTO `sys_menu` VALUES (30308, 2, '连接监控', 'mysqlconn', 'C', 'mysqlconn', 'http://localhost:8080/druid/index.html', '', 'http://localhost:8080/druid/index.html', NULL, 0, 0, '', 1, 1, '', 'ic:outline-monitor-heart', 0, 0, NULL, '2025-07-03 16:01:20', '2025-07-08 14:13:50', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30309, 2, '接口文档', 'apidoc', 'C', 'apidoc', 'http://localhost:8080/doc.html', '', 'http://localhost:8080/doc.html', NULL, 0, 0, '', 1, 1, '', 'mingcute:doc-line', 0, 0, NULL, '2025-07-03 16:01:20', '2025-07-08 14:15:41', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30310, 0, '官网网站', 'website', 'C', 'website', 'https://www.marsadmin.cn/', '', 'https://www.marsadmin.cn/', NULL, 0, 0, '', 1, 1, '', 'hugeicons:office', 100, 0, NULL, '2025-07-03 16:01:20', '2025-07-08 15:06:40', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30311, 0, '官网文档', 'websitedoc', 'C', 'websitedoc', 'https://docs.marsadmin.cn/', '', 'https://docs.marsadmin.cn/', NULL, 0, 0, '', 1, 1, '', 'solar:route-linear', 200, 0, NULL, '2025-07-03 16:01:20', '2025-07-08 15:07:14', 1, 1, 0, NULL);

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模块标题',
  `business_type` int NULL DEFAULT 0 COMMENT '业务类型：0-其它，1-新增，2-修改，3-删除',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求方式',
  `operator_type` int NULL DEFAULT 0 COMMENT '操作类别：0-其它，1-后台用户，2-手机端用户',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '返回参数',
  `status` int NULL DEFAULT 0 COMMENT '操作状态：0-正常，1-异常',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `cost_time` bigint NULL DEFAULT 0 COMMENT '消耗时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_business_type`(`business_type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_oper_time`(`oper_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '操作日志记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO `sys_oper_log` VALUES (1, '文件管理', 10, 'com.mars.admin.modules.base.controller.FileUploadController.upload()', 'POST', 1, NULL, NULL, '/file/upload', '127.0.0.1', NULL, '描述: 上传文件：#{#file.originalFilename}', '{\"code\":200,\"message\":\"上传成功\",\"data\":{\"id\":2,\"fileName\":\"b5f0f4fbb41c489fa1799c50db6247de.jpg\",\"originalName\":\"img_v3_02mm_6eaf2ac0-71ba-43ed-a56f-3913e28ab79g.jpg\",\"fileSuffix\":\"jpg\",\"url\":\"http://150.158.49.140:9000/test/upload/2025/07/03/b5f0f4fbb41c489fa1799c50db6247de.jpg\",\"size\":100276,\"filePath\":\"upload/2025/07/03/b5f0f4fbb41c489fa1799c50db6247de.jpg\",\"contentType\":\"image/jpeg\",\"configKey\":\"minio\",\"uploadStatus\":1},\"timestamp\":1751521465056,\"error\":false,\"success\":true}', 0, NULL, '2025-07-03 13:44:25', 4);
INSERT INTO `sys_oper_log` VALUES (2, '菜单管理', 1, 'com.mars.admin.modules.system.controller.SysMenuController.save()', 'POST', 1, 'admin', '未知部门', '/system/menu', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":null,\"updateTime\":null,\"createBy\":null,\"updateBy\":null,\"isDeleted\":null,\"deleteTime\":null,\"id\":null,\"parentId\":0,\"menuName\":\"组织管理\",\"menuCode\":\"org\",\"menuType\":\"M\",\"routeName\":\"org\",\"routePath\":\"/org\",\"component\":\"\",\"redirect\":\"\",\"queryParam\":null,\"isFrame\":1,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"\",\"icon\":\"\",\"orderNum\":0,\"isSystem\":null,\"remark\":null,\"children\":null,\"parentName\":null}} | 描述: 新增菜单：#{#entity.menuName}', '{\"code\":500,\"message\":\"权限标识已存在，请重新输入\",\"data\":null,\"timestamp\":1751528390583,\"error\":true,\"success\":false}', 0, NULL, '2025-07-03 15:39:50', 8);
INSERT INTO `sys_oper_log` VALUES (3, '菜单管理', 1, 'com.mars.admin.modules.system.controller.SysMenuController.save()', 'POST', 1, 'admin', '未知部门', '/system/menu', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":null,\"updateTime\":null,\"createBy\":null,\"updateBy\":null,\"isDeleted\":null,\"deleteTime\":null,\"id\":null,\"parentId\":0,\"menuName\":\"组织管理\",\"menuCode\":\"org\",\"menuType\":\"M\",\"routeName\":\"org\",\"routePath\":\"/org\",\"component\":\"\",\"redirect\":\"\",\"queryParam\":null,\"isFrame\":1,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"\",\"icon\":\"\",\"orderNum\":0,\"isSystem\":null,\"remark\":null,\"children\":null,\"parentName\":null}} | 描述: 新增菜单：#{#entity.menuName}', '{\"code\":500,\"message\":\"权限标识已存在，请重新输入\",\"data\":null,\"timestamp\":1751528443748,\"error\":true,\"success\":false}', 0, NULL, '2025-07-03 15:40:43', 4);
INSERT INTO `sys_oper_log` VALUES (4, '菜单管理', 1, 'com.mars.admin.modules.system.controller.SysMenuController.save()', 'POST', 1, 'admin', '未知部门', '/system/menu', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":null,\"updateTime\":null,\"createBy\":null,\"updateBy\":null,\"isDeleted\":null,\"deleteTime\":null,\"id\":null,\"parentId\":0,\"menuName\":\"组织管理\",\"menuCode\":\"org\",\"menuType\":\"M\",\"routeName\":\"org\",\"routePath\":\"/org\",\"component\":\"\",\"redirect\":\"\",\"queryParam\":null,\"isFrame\":1,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"\",\"icon\":\"\",\"orderNum\":0,\"isSystem\":null,\"remark\":null,\"children\":null,\"parentName\":null}} | 描述: 新增菜单：#{#entity.menuName}', '{\"code\":500,\"message\":\"权限标识已存在，请重新输入\",\"data\":null,\"timestamp\":1751528463888,\"error\":true,\"success\":false}', 0, NULL, '2025-07-03 15:41:03', 7);
INSERT INTO `sys_oper_log` VALUES (5, '菜单管理', 1, 'com.mars.admin.modules.system.controller.SysMenuController.save()', 'POST', 1, 'admin', '未知部门', '/system/menu', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":null,\"updateTime\":null,\"createBy\":null,\"updateBy\":null,\"isDeleted\":null,\"deleteTime\":null,\"id\":null,\"parentId\":0,\"menuName\":\"组织管理\",\"menuCode\":\"org\",\"menuType\":\"M\",\"routeName\":\"org\",\"routePath\":\"/org\",\"component\":\"\",\"redirect\":\"\",\"queryParam\":null,\"isFrame\":1,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"\",\"icon\":\"\",\"orderNum\":0,\"isSystem\":null,\"remark\":null,\"children\":null,\"parentName\":null}} | 描述: 新增菜单', '{\"code\":500,\"message\":\"权限标识已存在，请重新输入\",\"data\":null,\"timestamp\":1751528536572,\"success\":false,\"error\":true}', 0, NULL, '2025-07-03 15:42:16', 25);
INSERT INTO `sys_oper_log` VALUES (6, '菜单管理', 1, 'com.mars.admin.modules.system.controller.SysMenuController.save()', 'POST', 1, 'admin', '未知部门', '/system/menu', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":null,\"updateTime\":null,\"createBy\":null,\"updateBy\":null,\"isDeleted\":null,\"deleteTime\":null,\"id\":null,\"parentId\":0,\"menuName\":\"组织管理\",\"menuCode\":\"org\",\"menuType\":\"M\",\"routeName\":\"org\",\"routePath\":\"/org\",\"component\":\"\",\"redirect\":\"\",\"queryParam\":null,\"isFrame\":1,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"\",\"icon\":\"\",\"orderNum\":0,\"isSystem\":null,\"remark\":null,\"children\":null,\"parentName\":null}} | 描述: 新增菜单', '{\"code\":500,\"message\":\"权限标识已存在，请重新输入\",\"data\":null,\"timestamp\":1751528571399,\"success\":false,\"error\":true}', 0, NULL, '2025-07-03 15:42:51', 6);
INSERT INTO `sys_oper_log` VALUES (7, '菜单管理', 1, 'com.mars.admin.modules.system.controller.SysMenuController.save()', 'POST', 1, 'admin', '未知部门', '/system/menu', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":null,\"updateTime\":null,\"createBy\":null,\"updateBy\":null,\"isDeleted\":null,\"deleteTime\":null,\"id\":null,\"parentId\":0,\"menuName\":\"组织管理\",\"menuCode\":\"org\",\"menuType\":\"M\",\"routeName\":\"org\",\"routePath\":\"/org\",\"component\":\"\",\"redirect\":\"\",\"queryParam\":null,\"isFrame\":1,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"\",\"icon\":\"\",\"orderNum\":0,\"isSystem\":null,\"remark\":null,\"children\":null,\"parentName\":null}} | 描述: 新增菜单', '{\"code\":500,\"message\":\"权限标识已存在，请重新输入\",\"data\":null,\"timestamp\":1751528584345,\"success\":false,\"error\":true}', 0, NULL, '2025-07-03 15:43:04', 4);
INSERT INTO `sys_oper_log` VALUES (8, '菜单管理', 1, 'com.mars.admin.modules.system.controller.SysMenuController.save()', 'POST', 1, 'admin', '未知部门', '/system/menu', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":null,\"updateTime\":null,\"createBy\":null,\"updateBy\":null,\"isDeleted\":null,\"deleteTime\":null,\"id\":null,\"parentId\":0,\"menuName\":\"组织管理\",\"menuCode\":\"org\",\"menuType\":\"M\",\"routeName\":\"org\",\"routePath\":\"/org\",\"component\":\"\",\"redirect\":\"\",\"queryParam\":null,\"isFrame\":1,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"\",\"icon\":\"\",\"orderNum\":0,\"isSystem\":null,\"remark\":null,\"children\":null,\"parentName\":null}} | 描述: 新增菜单', '{\"code\":500,\"message\":\"权限标识已存在，请重新输入\",\"data\":null,\"timestamp\":1751528716211,\"success\":false,\"error\":true}', 0, NULL, '2025-07-03 15:45:16', 5);
INSERT INTO `sys_oper_log` VALUES (9, '菜单管理', 1, 'com.mars.admin.modules.system.controller.SysMenuController.save()', 'POST', 1, 'admin', '未知部门', '/system/menu', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":\"2025-07-03 15:46:07\",\"updateTime\":null,\"createBy\":1,\"updateBy\":null,\"isDeleted\":0,\"deleteTime\":null,\"id\":30306,\"parentId\":0,\"menuName\":\"组织管理\",\"menuCode\":\"org\",\"menuType\":\"M\",\"routeName\":\"org\",\"routePath\":\"/org\",\"component\":\"\",\"redirect\":\"\",\"queryParam\":null,\"isFrame\":1,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"\",\"icon\":\"\",\"orderNum\":0,\"isSystem\":null,\"remark\":null,\"children\":null,\"parentName\":null}} | 描述: 新增菜单', '{\"code\":200,\"message\":\"新增成功\",\"data\":null,\"timestamp\":1751528767435,\"error\":false,\"success\":true}', 0, NULL, '2025-07-03 15:46:07', 26);
INSERT INTO `sys_oper_log` VALUES (10, '菜单管理', 2, 'com.mars.admin.modules.system.controller.SysMenuController.update()', 'PUT', 1, 'admin', '未知部门', '/system/menu', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":\"2025-06-25 16:34:16\",\"updateTime\":\"2025-07-03 15:46:42\",\"createBy\":1,\"updateBy\":1,\"isDeleted\":0,\"deleteTime\":null,\"id\":103,\"parentId\":30306,\"menuName\":\"部门管理\",\"menuCode\":\"DEPT_MANAGE\",\"menuType\":\"C\",\"routeName\":\"manage_dept\",\"routePath\":\"/manage/dept\",\"component\":\"view.manage_dept\",\"redirect\":null,\"queryParam\":null,\"isFrame\":1,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"system:dept:list\",\"icon\":\"tree\",\"orderNum\":4,\"isSystem\":1,\"remark\":null,\"children\":[],\"parentName\":null}} | 描述: 修改菜单', '{\"code\":200,\"message\":\"修改成功\",\"data\":null,\"timestamp\":1751528802228,\"error\":false,\"success\":true}', 0, NULL, '2025-07-03 15:46:42', 4);
INSERT INTO `sys_oper_log` VALUES (11, '菜单管理', 2, 'com.mars.admin.modules.system.controller.SysMenuController.update()', 'PUT', 1, 'admin', '未知部门', '/system/menu', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":\"2025-06-25 16:34:16\",\"updateTime\":\"2025-07-03 15:46:58\",\"createBy\":1,\"updateBy\":1,\"isDeleted\":0,\"deleteTime\":null,\"id\":104,\"parentId\":30306,\"menuName\":\"岗位管理\",\"menuCode\":\"POST_MANAGE\",\"menuType\":\"C\",\"routeName\":\"manage_post\",\"routePath\":\"/manage/post\",\"component\":\"view.manage_post\",\"redirect\":null,\"queryParam\":null,\"isFrame\":1,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"system:post:list\",\"icon\":\"post\",\"orderNum\":5,\"isSystem\":1,\"remark\":null,\"children\":[],\"parentName\":null}} | 描述: 修改菜单', '{\"code\":200,\"message\":\"修改成功\",\"data\":null,\"timestamp\":1751528818389,\"error\":false,\"success\":true}', 0, NULL, '2025-07-03 15:46:58', 3);
INSERT INTO `sys_oper_log` VALUES (12, '角色管理', 4, 'com.mars.admin.modules.system.controller.SysRoleController.authMenu()', 'PUT', 1, 'admin', '未知部门', '/system/role/authMenu', '127.0.0.1', NULL, '{\"role\":{\"createTime\":null,\"updateTime\":null,\"createBy\":null,\"updateBy\":null,\"isDeleted\":null,\"deleteTime\":null,\"id\":1,\"roleName\":null,\"roleCode\":null,\"roleKey\":null,\"roleSort\":null,\"dataScope\":null,\"menuCheckStrictly\":null,\"deptCheckStrictly\":null,\"status\":null,\"isSystem\":null,\"description\":null,\"remark\":null,\"menus\":null,\"depts\":null,\"menuIds\":[1016,2,3,105,106,1000,1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1011,1012,1013,1014,1015,1070,1071,1072,1080,1081,1082,1083,1084,30201,30202,30203,30204,30301,30302,30303,30304,30305,100,101,102,107,108,302,303,1018,1017,1,301],\"deptIds\":null}} | 描述: 分配角色菜单权限，角色ID：#{#role.id}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null,\"timestamp\":1751528860862,\"error\":false,\"success\":true}', 0, NULL, '2025-07-03 15:47:40', 4);
INSERT INTO `sys_oper_log` VALUES (13, '角色管理', 4, 'com.mars.admin.modules.system.controller.SysRoleController.authMenu()', 'PUT', 1, 'admin', '未知部门', '/system/role/authMenu', '127.0.0.1', NULL, '{\"role\":{\"createTime\":null,\"updateTime\":null,\"createBy\":null,\"updateBy\":null,\"isDeleted\":null,\"deleteTime\":null,\"id\":1,\"roleName\":null,\"roleCode\":null,\"roleKey\":null,\"roleSort\":null,\"dataScope\":null,\"menuCheckStrictly\":null,\"deptCheckStrictly\":null,\"status\":null,\"isSystem\":null,\"description\":null,\"remark\":null,\"menus\":null,\"depts\":null,\"menuIds\":[1016,2,3,105,106,1000,1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1011,1012,1013,1014,1015,1070,1071,1072,1080,1081,1082,1083,1084,30201,30202,30203,30204,30301,30302,30303,30304,30305,100,101,102,107,108,302,303,1018,1017,1,301,30306,103,104],\"deptIds\":null}} | 描述: 分配角色菜单权限，角色ID：#{#role.id}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null,\"timestamp\":1751528866704,\"error\":false,\"success\":true}', 0, NULL, '2025-07-03 15:47:46', 5);
INSERT INTO `sys_oper_log` VALUES (14, '文件管理', 10, 'com.mars.admin.modules.base.controller.FileUploadController.upload()', 'POST', 1, NULL, NULL, '/file/upload', '127.0.0.1', NULL, '描述: 上传文件：#{#file.originalFilename}', '{\"code\":500,\"message\":\"上传失败: 本地文件上传失败: java.io.FileNotFoundException: C:\\\\Users\\\\qiu_m\\\\AppData\\\\Local\\\\Temp\\\\tomcat.8080.5746850484025418959\\\\work\\\\Tomcat\\\\localhost\\\\ROOT\\\\.\\\\upload\\\\upload\\\\2025\\\\07\\\\04\\\\cbcb6a91fb7b43fb83df4f03c0a688e5.jpeg (系统找不到指定的路径。)\",\"data\":null,\"timestamp\":1751598147896,\"error\":true,\"success\":false}', 0, NULL, '2025-07-04 11:02:27', 5);
INSERT INTO `sys_oper_log` VALUES (15, '文件管理', 10, 'com.mars.admin.modules.base.controller.FileUploadController.upload()', 'POST', 1, NULL, NULL, '/file/upload', '127.0.0.1', NULL, '描述: 上传文件：#{#file.originalFilename}', '{\"code\":200,\"message\":\"上传成功\",\"data\":{\"id\":3,\"fileName\":\"55afdcc5602c458ea03a22a2bc55bf16.jpeg\",\"originalName\":\"头像.jpeg\",\"fileSuffix\":\"jpeg\",\"url\":\"http://150.158.49.140:9000/test/upload/2025/07/04/55afdcc5602c458ea03a22a2bc55bf16.jpeg\",\"size\":8838,\"filePath\":\"upload/2025/07/04/55afdcc5602c458ea03a22a2bc55bf16.jpeg\",\"contentType\":\"image/jpeg\",\"configKey\":\"minio\",\"uploadStatus\":1},\"timestamp\":1751598273145,\"error\":false,\"success\":true}', 0, NULL, '2025-07-04 11:04:33', 2);
INSERT INTO `sys_oper_log` VALUES (16, '文件管理', 10, 'com.mars.admin.modules.base.controller.FileUploadController.upload()', 'POST', 1, NULL, NULL, '/file/upload', '127.0.0.1', NULL, '描述: 上传文件：#{#file.originalFilename}', '{\"code\":200,\"message\":\"上传成功\",\"data\":{\"id\":4,\"fileName\":\"ce27368b3efa4c1d81c0dc8c49ea8f89.jpg\",\"originalName\":\"Bears.jpg\",\"fileSuffix\":\"jpg\",\"url\":\"http://150.158.49.140:9000/test/upload/2025/07/04/ce27368b3efa4c1d81c0dc8c49ea8f89.jpg\",\"size\":123500,\"filePath\":\"upload/2025/07/04/ce27368b3efa4c1d81c0dc8c49ea8f89.jpg\",\"contentType\":\"image/jpeg\",\"configKey\":\"minio\",\"uploadStatus\":1},\"timestamp\":1751599113397,\"error\":false,\"success\":true}', 0, NULL, '2025-07-04 11:18:33', 0);
INSERT INTO `sys_oper_log` VALUES (17, '文件管理', 10, 'com.mars.admin.modules.base.controller.FileUploadController.upload()', 'POST', 1, NULL, NULL, '/file/upload', '127.0.0.1', NULL, '描述: 上传文件：#{#file.originalFilename}', '{\"code\":200,\"message\":\"上传成功\",\"data\":{\"id\":5,\"fileName\":\"7af03b4667b14d4ba3f274a194365c19.jpeg\",\"originalName\":\"头像.jpeg\",\"fileSuffix\":\"jpeg\",\"url\":\"http://150.158.49.140:9000/test/upload/2025/07/04/7af03b4667b14d4ba3f274a194365c19.jpeg\",\"size\":8838,\"filePath\":\"upload/2025/07/04/7af03b4667b14d4ba3f274a194365c19.jpeg\",\"contentType\":\"image/jpeg\",\"configKey\":\"minio\",\"uploadStatus\":1},\"timestamp\":1751599120842,\"error\":false,\"success\":true}', 0, NULL, '2025-07-04 11:18:40', 1);
INSERT INTO `sys_oper_log` VALUES (18, '角色管理', 4, 'com.mars.admin.modules.system.controller.SysRoleController.authMenu()', 'PUT', 1, 'admin', '未知部门', '/system/role/authMenu', '127.0.0.1', NULL, '{\"role\":{\"createTime\":null,\"updateTime\":null,\"createBy\":null,\"updateBy\":null,\"isDeleted\":null,\"deleteTime\":null,\"id\":1,\"roleName\":null,\"roleCode\":null,\"roleKey\":null,\"roleSort\":null,\"dataScope\":null,\"menuCheckStrictly\":null,\"deptCheckStrictly\":null,\"status\":null,\"isSystem\":null,\"description\":null,\"remark\":null,\"menus\":null,\"depts\":null,\"menuIds\":[1016,2,3,105,106,1000,1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1011,1012,1013,1014,1015,1070,1071,1072,1080,1081,1082,1083,1084,30201,30202,30203,30204,30301,30302,30303,30304,30305,100,101,102,107,108,302,303,1018,1017,1,301],\"deptIds\":null}} | 描述: 分配角色菜单权限，角色ID：#{#role.id}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null,\"timestamp\":1751608646198,\"error\":false,\"success\":true}', 0, NULL, '2025-07-04 13:57:26', 11);
INSERT INTO `sys_oper_log` VALUES (19, '角色管理', 4, 'com.mars.admin.modules.system.controller.SysRoleController.authMenu()', 'PUT', 1, 'admin', '未知部门', '/system/role/authMenu', '127.0.0.1', NULL, '{\"role\":{\"createTime\":null,\"updateTime\":null,\"createBy\":null,\"updateBy\":null,\"isDeleted\":null,\"deleteTime\":null,\"id\":1,\"roleName\":null,\"roleCode\":null,\"roleKey\":null,\"roleSort\":null,\"dataScope\":null,\"menuCheckStrictly\":null,\"deptCheckStrictly\":null,\"status\":null,\"isSystem\":null,\"description\":null,\"remark\":null,\"menus\":null,\"depts\":null,\"menuIds\":[1016,2,3,105,106,1000,1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1011,1012,1013,1014,1015,1070,1071,1072,1080,1081,1082,1083,1084,30201,30202,30203,30204,30301,30302,30303,30304,30305,100,101,102,107,108,302,303,1018,1017,1,301,30306,103,104],\"deptIds\":null}} | 描述: 分配角色菜单权限，角色ID：#{#role.id}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null,\"timestamp\":1751608998327,\"error\":false,\"success\":true}', 0, NULL, '2025-07-04 14:03:18', 11);
INSERT INTO `sys_oper_log` VALUES (20, '角色管理', 4, 'com.mars.admin.modules.system.controller.SysRoleController.authMenu()', 'PUT', 1, 'admin', '未知部门', '/system/role/authMenu', '127.0.0.1', NULL, '{\"role\":{\"createTime\":null,\"updateTime\":null,\"createBy\":null,\"updateBy\":null,\"isDeleted\":null,\"deleteTime\":null,\"id\":1,\"roleName\":null,\"roleCode\":null,\"roleKey\":null,\"roleSort\":null,\"dataScope\":null,\"menuCheckStrictly\":null,\"deptCheckStrictly\":null,\"status\":null,\"isSystem\":null,\"description\":null,\"remark\":null,\"menus\":null,\"depts\":null,\"menuIds\":[1016,2,3,105,106,1000,1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1011,1012,1013,1014,1015,30201,30202,30203,30204,30301,30302,30303,30304,30305,103,104,100,101,102,302,303,1018,1017,30306,301,1],\"deptIds\":null}} | 描述: 分配角色菜单权限，角色ID：#{#role.id}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null,\"timestamp\":1751609016094,\"error\":false,\"success\":true}', 0, NULL, '2025-07-04 14:03:36', 4);
INSERT INTO `sys_oper_log` VALUES (21, '角色管理', 4, 'com.mars.admin.modules.system.controller.SysRoleController.authMenu()', 'PUT', 1, 'admin', '未知部门', '/system/role/authMenu', '127.0.0.1', NULL, '{\"role\":{\"createTime\":null,\"updateTime\":null,\"createBy\":null,\"updateBy\":null,\"isDeleted\":null,\"deleteTime\":null,\"id\":1,\"roleName\":null,\"roleCode\":null,\"roleKey\":null,\"roleSort\":null,\"dataScope\":null,\"menuCheckStrictly\":null,\"deptCheckStrictly\":null,\"status\":null,\"isSystem\":null,\"description\":null,\"remark\":null,\"menus\":null,\"depts\":null,\"menuIds\":[1016,2,3,105,106,1000,1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1011,1012,1013,1014,1015,30201,30202,30203,30204,30301,30302,30303,30304,30305,103,104,100,101,102,302,303,1018,1017,30306,301,107,1070,1071,1072,108,1080,1081,1082,1083,1084,1],\"deptIds\":null}} | 描述: 分配角色菜单权限，角色ID：#{#role.id}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null,\"timestamp\":1751609021265,\"error\":false,\"success\":true}', 0, NULL, '2025-07-04 14:03:41', 6);
INSERT INTO `sys_oper_log` VALUES (22, '角色管理', 4, 'com.mars.admin.modules.system.controller.SysRoleController.authMenu()', 'PUT', 1, 'admin', '未知部门', '/system/role/authMenu', '127.0.0.1', NULL, '{\"role\":{\"createTime\":null,\"updateTime\":null,\"createBy\":null,\"updateBy\":null,\"isDeleted\":null,\"deleteTime\":null,\"id\":1,\"roleName\":null,\"roleCode\":null,\"roleKey\":null,\"roleSort\":null,\"dataScope\":null,\"menuCheckStrictly\":null,\"deptCheckStrictly\":null,\"status\":null,\"isSystem\":null,\"description\":null,\"remark\":null,\"menus\":null,\"depts\":null,\"menuIds\":[1016,3,105,106,1000,1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1011,1012,1013,1014,1015,1070,1071,1072,1080,1081,1082,1083,1084,30201,30202,30203,30204,30301,30302,30303,30304,30305,103,104,100,101,102,107,108,302,303,1018,1017,30306,1,301,2,109],\"deptIds\":null}} | 描述: 分配角色菜单权限，角色ID：#{#role.id}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null,\"timestamp\":1751611721309,\"error\":false,\"success\":true}', 0, NULL, '2025-07-04 14:48:41', 6);
INSERT INTO `sys_oper_log` VALUES (23, '角色管理', 4, 'com.mars.admin.modules.system.controller.SysRoleController.authMenu()', 'PUT', 1, 'admin', '未知部门', '/system/role/authMenu', '127.0.0.1', NULL, '{\"role\":{\"createTime\":null,\"updateTime\":null,\"createBy\":null,\"updateBy\":null,\"isDeleted\":null,\"deleteTime\":null,\"id\":1,\"roleName\":null,\"roleCode\":null,\"roleKey\":null,\"roleSort\":null,\"dataScope\":null,\"menuCheckStrictly\":null,\"deptCheckStrictly\":null,\"status\":null,\"isSystem\":null,\"description\":null,\"remark\":null,\"menus\":null,\"depts\":null,\"menuIds\":[1016,3,105,106,109,1000,1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1011,1012,1013,1014,1015,1070,1071,1072,1080,1081,1082,1083,1084,30201,30202,30203,30204,30301,30302,30303,30304,30305,103,104,30308,100,101,102,107,108,302,303,1018,1017,30306,1,2,301,30307,2100,2101,2102,2103,2104,2105,2106,2107,2108,2109,2110,2111,2112,2113],\"deptIds\":null}} | 描述: 分配角色菜单权限，角色ID：#{#role.id}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null,\"timestamp\":1751945608256,\"error\":false,\"success\":true}', 0, NULL, '2025-07-08 11:33:28', 9);
INSERT INTO `sys_oper_log` VALUES (24, '角色管理', 2, 'com.mars.admin.modules.system.controller.SysRoleController.update()', 'PUT', 1, 'admin', '未知部门', '/system/role', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":null,\"updateTime\":\"2025-07-08 11:33:30\",\"createBy\":null,\"updateBy\":1,\"isDeleted\":null,\"deleteTime\":null,\"id\":1,\"roleName\":\"超级管理员\",\"roleCode\":\"SUPER_ADMIN\",\"roleKey\":\"admin\",\"roleSort\":1,\"dataScope\":1,\"menuCheckStrictly\":1,\"deptCheckStrictly\":1,\"status\":1,\"isSystem\":null,\"description\":\"超级管理员\",\"remark\":\"\",\"menus\":null,\"depts\":null,\"menuIds\":null,\"deptIds\":null}} | 描述: 修改角色：#{#entity.roleName}', '{\"code\":200,\"message\":\"修改成功\",\"data\":null,\"timestamp\":1751945610581,\"error\":false,\"success\":true}', 0, NULL, '2025-07-08 11:33:30', 5);
INSERT INTO `sys_oper_log` VALUES (25, '菜单管理', 2, 'com.mars.admin.modules.system.controller.SysMenuController.update()', 'PUT', 1, 'admin', '未知部门', '/system/menu', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":\"2025-07-03 16:01:20\",\"updateTime\":\"2025-07-04 16:53:55\",\"createBy\":1,\"updateBy\":null,\"isDeleted\":0,\"deleteTime\":null,\"id\":30308,\"parentId\":2,\"menuName\":\"连接监控\",\"menuCode\":\"mysqlconn\",\"menuType\":\"C\",\"routeName\":\"mysqlconn\",\"routePath\":\"/mysqlconn\",\"component\":\"\",\"redirect\":\"http://localhost:8080/druid/index.html\",\"queryParam\":null,\"isFrame\":0,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"\",\"icon\":\"\",\"orderNum\":0,\"isSystem\":0,\"remark\":null,\"children\":[],\"parentName\":null}} | 描述: 修改菜单：#{#entity.menuName}', '{\"code\":500,\"message\":\"权限标识已存在，请重新输入\",\"data\":null,\"timestamp\":1751945845824,\"error\":true,\"success\":false}', 0, NULL, '2025-07-08 11:37:25', 5);
INSERT INTO `sys_oper_log` VALUES (26, '菜单管理', 2, 'com.mars.admin.modules.system.controller.SysMenuController.update()', 'PUT', 1, 'admin', '未知部门', '/system/menu', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":\"2025-07-03 16:01:20\",\"updateTime\":\"2025-07-04 16:53:55\",\"createBy\":1,\"updateBy\":null,\"isDeleted\":0,\"deleteTime\":null,\"id\":30308,\"parentId\":2,\"menuName\":\"连接监控\",\"menuCode\":\"mysqlconn\",\"menuType\":\"C\",\"routeName\":\"mysqlconn\",\"routePath\":\"/mysqlconn\",\"component\":\"\",\"redirect\":\"http://localhost:8080/druid/index.html\",\"queryParam\":null,\"isFrame\":0,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"\",\"icon\":\"\",\"orderNum\":0,\"isSystem\":0,\"remark\":null,\"children\":[],\"parentName\":null}} | 描述: 修改菜单', '{\"code\":500,\"message\":\"权限标识已存在，请重新输入\",\"data\":null,\"timestamp\":1751945935683,\"error\":true,\"success\":false}', 0, NULL, '2025-07-08 11:38:55', 31);
INSERT INTO `sys_oper_log` VALUES (27, '菜单管理', 2, 'com.mars.admin.modules.system.controller.SysMenuController.update()', 'PUT', 1, 'admin', '未知部门', '/system/menu', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":\"2025-07-03 16:01:20\",\"updateTime\":\"2025-07-04 16:53:55\",\"createBy\":1,\"updateBy\":null,\"isDeleted\":0,\"deleteTime\":null,\"id\":30308,\"parentId\":2,\"menuName\":\"连接监控\",\"menuCode\":\"mysqlconn\",\"menuType\":\"C\",\"routeName\":\"mysqlconn\",\"routePath\":\"/mysqlconn\",\"component\":\"\",\"redirect\":\"http://localhost:8080/druid/index.html\",\"queryParam\":null,\"isFrame\":0,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"\",\"icon\":\"\",\"orderNum\":0,\"isSystem\":0,\"remark\":null,\"children\":[],\"parentName\":null}} | 描述: 修改菜单', '{\"code\":500,\"message\":\"权限标识已存在，请重新输入\",\"data\":null,\"timestamp\":1751946342949,\"error\":true,\"success\":false}', 0, NULL, '2025-07-08 11:45:43', 32);
INSERT INTO `sys_oper_log` VALUES (28, '菜单管理', 2, 'com.mars.admin.modules.system.controller.SysMenuController.update()', 'PUT', 1, 'admin', '未知部门', '/system/menu', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":\"2025-07-03 16:01:20\",\"updateTime\":\"2025-07-04 16:53:55\",\"createBy\":1,\"updateBy\":null,\"isDeleted\":0,\"deleteTime\":null,\"id\":30308,\"parentId\":2,\"menuName\":\"连接监控\",\"menuCode\":\"mysqlconn\",\"menuType\":\"C\",\"routeName\":\"mysqlconn\",\"routePath\":\"/mysqlconn\",\"component\":\"\",\"redirect\":\"http://localhost:8080/druid/index.html\",\"queryParam\":null,\"isFrame\":0,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"\",\"icon\":\"\",\"orderNum\":0,\"isSystem\":0,\"remark\":null,\"children\":[],\"parentName\":null}} | 描述: 修改菜单', '{\"code\":500,\"message\":\"权限标识已存在，请重新输入\",\"data\":null,\"timestamp\":1751946368712,\"error\":true,\"success\":false}', 0, NULL, '2025-07-08 11:46:08', 7);
INSERT INTO `sys_oper_log` VALUES (29, '菜单管理', 2, 'com.mars.admin.modules.system.controller.SysMenuController.update()', 'PUT', 1, 'admin', '未知部门', '/system/menu', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":\"2025-07-03 16:01:20\",\"updateTime\":\"2025-07-04 16:53:55\",\"createBy\":1,\"updateBy\":null,\"isDeleted\":0,\"deleteTime\":null,\"id\":30308,\"parentId\":2,\"menuName\":\"连接监控\",\"menuCode\":\"mysqlconn\",\"menuType\":\"C\",\"routeName\":\"mysqlconn\",\"routePath\":\"/mysqlconn\",\"component\":\"\",\"redirect\":\"http://localhost:8080/druid/index.html\",\"queryParam\":null,\"isFrame\":0,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"\",\"icon\":\"\",\"orderNum\":0,\"isSystem\":0,\"remark\":null,\"children\":[],\"parentName\":null}} | 描述: 修改菜单', '{\"code\":500,\"message\":\"权限标识已存在，请重新输入\",\"data\":null,\"timestamp\":1751946433654,\"error\":true,\"success\":false}', 0, NULL, '2025-07-08 11:47:13', 7);
INSERT INTO `sys_oper_log` VALUES (30, '菜单管理', 2, 'com.mars.admin.modules.system.controller.SysMenuController.update()', 'PUT', 1, 'admin', '未知部门', '/system/menu', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":\"2025-07-03 16:01:20\",\"updateTime\":\"2025-07-04 16:53:55\",\"createBy\":1,\"updateBy\":null,\"isDeleted\":0,\"deleteTime\":null,\"id\":30308,\"parentId\":2,\"menuName\":\"连接监控\",\"menuCode\":\"mysqlconn\",\"menuType\":\"C\",\"routeName\":\"mysqlconn\",\"routePath\":\"/mysqlconn\",\"component\":\"\",\"redirect\":\"http://localhost:8080/druid/index.html\",\"queryParam\":null,\"isFrame\":0,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"\",\"icon\":\"\",\"orderNum\":0,\"isSystem\":0,\"remark\":null,\"children\":[],\"parentName\":null}} | 描述: 修改菜单', '{\"code\":500,\"message\":\"权限标识已存在，请重新输入\",\"data\":null,\"timestamp\":1751946526468,\"error\":true,\"success\":false}', 0, NULL, '2025-07-08 11:48:46', 9);
INSERT INTO `sys_oper_log` VALUES (31, '菜单管理', 2, 'com.mars.admin.modules.system.controller.SysMenuController.update()', 'PUT', 1, 'admin', '未知部门', '/system/menu', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":\"2025-07-03 16:01:20\",\"updateTime\":\"2025-07-08 11:49:54\",\"createBy\":1,\"updateBy\":1,\"isDeleted\":0,\"deleteTime\":null,\"id\":30308,\"parentId\":2,\"menuName\":\"连接监控\",\"menuCode\":\"mysqlconn\",\"menuType\":\"C\",\"routeName\":\"mysqlconn\",\"routePath\":\"/mysqlconn\",\"component\":\"\",\"redirect\":\"http://localhost:8080/druid/index.html\",\"queryParam\":null,\"isFrame\":0,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"\",\"icon\":\"\",\"orderNum\":0,\"isSystem\":0,\"remark\":null,\"children\":[],\"parentName\":null}} | 描述: 修改菜单', '{\"code\":200,\"message\":\"修改成功\",\"data\":null,\"timestamp\":1751946594489,\"error\":false,\"success\":true}', 0, NULL, '2025-07-08 11:49:54', 47);
INSERT INTO `sys_oper_log` VALUES (32, '菜单管理', 3, 'com.mars.admin.modules.system.controller.SysMenuController.deleteById()', 'DELETE', 1, 'admin', '未知部门', '/system/menu/30307', '127.0.0.1', NULL, '{\"id\":30307} | 描述: 删除菜单', '{\"code\":200,\"message\":\"删除成功\",\"data\":null,\"timestamp\":1751946610859,\"error\":false,\"success\":true}', 0, NULL, '2025-07-08 11:50:10', 12);
INSERT INTO `sys_oper_log` VALUES (33, '菜单管理', 2, 'com.mars.admin.modules.system.controller.SysMenuController.update()', 'PUT', 1, 'admin', '未知部门', '/system/menu', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":\"2025-07-03 15:46:07\",\"updateTime\":\"2025-07-08 12:00:33\",\"createBy\":1,\"updateBy\":1,\"isDeleted\":0,\"deleteTime\":null,\"id\":30306,\"parentId\":0,\"menuName\":\"组织管理\",\"menuCode\":\"org\",\"menuType\":\"M\",\"routeName\":\"org\",\"routePath\":\"/org\",\"component\":\"\",\"redirect\":\"\",\"queryParam\":null,\"isFrame\":1,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"\",\"icon\":\"\",\"orderNum\":3,\"isSystem\":0,\"remark\":null,\"children\":[{\"createTime\":\"2025-06-25 16:34:16\",\"updateTime\":\"2025-07-03 15:46:42\",\"createBy\":1,\"updateBy\":1,\"isDeleted\":0,\"deleteTime\":null,\"id\":103,\"parentId\":30306,\"menuName\":\"部门管理\",\"menuCode\":\"DEPT_MANAGE\",\"menuType\":\"C\",\"routeName\":\"manage_dept\",\"routePath\":\"/manage/dept\",\"component\":\"view.manage_dept\",\"redirect\":null,\"queryParam\":null,\"isFrame\":1,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"system:dept:list\",\"icon\":\"tree\",\"orderNum\":4,\"isSystem\":1,\"remark\":null,\"children\":[],\"parentName\":null},{\"createTime\":\"2025-06-25 16:34:16\",\"updateTime\":\"2025-07-03 15:46:58\",\"createBy\":1,\"updateBy\":1,\"isDeleted\":0,\"deleteTime\":null,\"id\":104,\"parentId\":30306,\"menuName\":\"岗位管理\",\"menuCode\":\"POST_MANAGE\",\"menuType\":\"C\",\"routeName\":\"manage_post\",\"routePath\":\"/manage/post\",\"component\":\"view.manage_post\",\"redirect\":null,\"queryParam\":null,\"isFrame\":1,\"isCache\":0,\"menuTypeFlag\":\"\",\"visible\":1,\"status\":1,\"perms\":\"system:post:list\",\"icon\":\"post\",\"orderNum\":5,\"isSystem\":1,\"remark\":null,\"children\":[],\"parentName\":null}],\"parentName\":null}} | 描述: 修改菜单', '{\"code\":200,\"message\":\"修改成功\",\"data\":null,\"timestamp\":1751947233286,\"error\":false,\"success\":true}', 0, NULL, '2025-07-08 12:00:33', 8);
INSERT INTO `sys_oper_log` VALUES (34, '字典管理', 1, 'com.mars.admin.modules.system.controller.SysDictController.addDictType()', 'POST', 1, 'admin', '未知部门', '/system/dict/type', '127.0.0.1', NULL, '{\"dictType\":{\"createTime\":\"2025-06-25 16:34:16\",\"updateTime\":\"2025-06-25 16:34:16\",\"createBy\":1,\"updateBy\":null,\"isDeleted\":0,\"deleteTime\":null,\"id\":7,\"dictName\":\"测试\",\"dictType\":\"test\",\"status\":1,\"remark\":\"\",\"dictDataList\":null}} | 描述: 新增字典类型：#{#dictType.dictName}', '{\"code\":200,\"message\":\"新增成功\",\"data\":null,\"timestamp\":1751954069747,\"error\":false,\"success\":true}', 0, NULL, '2025-07-08 13:54:29', 8);
INSERT INTO `sys_oper_log` VALUES (35, '字典管理', 1, 'com.mars.admin.modules.system.controller.SysDictController.addDictData()', 'POST', 1, 'admin', '未知部门', '/system/dict/data', '127.0.0.1', NULL, '{\"dictData\":{\"createTime\":null,\"updateTime\":null,\"createBy\":null,\"updateBy\":null,\"isDeleted\":0,\"deleteTime\":null,\"id\":10,\"dictSort\":0,\"dictLabel\":\"1\",\"dictValue\":\"1\",\"dictType\":\"test\",\"cssClass\":\"\",\"listClass\":\"\",\"isDefault\":\"N\",\"status\":1,\"remark\":\"\"}} | 描述: 新增字典数据：#{#dictData.dictLabel}', '{\"code\":200,\"message\":\"新增成功\",\"data\":null,\"timestamp\":1751954077136,\"error\":false,\"success\":true}', 0, NULL, '2025-07-08 13:54:37', 4);
INSERT INTO `sys_oper_log` VALUES (36, '字典管理', 3, 'com.mars.admin.modules.system.controller.SysDictController.deleteDictData()', 'DELETE', 1, 'admin', '未知部门', '/system/dict/data/10', '127.0.0.1', NULL, '{\"id\":10} | 描述: 删除字典数据，ID：#{#id}', '{\"code\":200,\"message\":\"删除成功\",\"data\":null,\"timestamp\":1751954081137,\"error\":false,\"success\":true}', 0, NULL, '2025-07-08 13:54:41', 3);
INSERT INTO `sys_oper_log` VALUES (37, '字典管理', 3, 'com.mars.admin.modules.system.controller.SysDictController.deleteDictType()', 'DELETE', 1, 'admin', '未知部门', '/system/dict/type/7', '127.0.0.1', NULL, '{\"id\":7} | 描述: 删除字典类型，ID：#{#id}', '{\"code\":200,\"message\":\"删除成功\",\"data\":null,\"timestamp\":1751954084481,\"error\":false,\"success\":true}', 0, NULL, '2025-07-08 13:54:44', 2);
INSERT INTO `sys_oper_log` VALUES (38, '文件管理', 10, 'com.mars.admin.modules.base.controller.FileUploadController.upload()', 'POST', 1, NULL, NULL, '/file/upload', '127.0.0.1', NULL, '描述: 上传文件：#{#file.originalFilename}', '{\"code\":200,\"message\":\"上传成功\",\"data\":{\"id\":6,\"fileName\":\"fdebb9be36184ceaa0bebf3c49f63b00.jpg\",\"originalName\":\"sex.jpg\",\"fileSuffix\":\"jpg\",\"url\":\"http://150.158.49.140:9000/test/upload/2025/07/08/fdebb9be36184ceaa0bebf3c49f63b00.jpg\",\"size\":39854,\"filePath\":\"upload/2025/07/08/fdebb9be36184ceaa0bebf3c49f63b00.jpg\",\"contentType\":\"image/jpeg\",\"configKey\":\"minio\",\"uploadStatus\":1},\"timestamp\":1751954170967,\"error\":false,\"success\":true}', 0, NULL, '2025-07-08 13:56:10', 2);
INSERT INTO `sys_oper_log` VALUES (39, '文件管理', 10, 'com.mars.admin.modules.base.controller.FileUploadController.upload()', 'POST', 1, NULL, NULL, '/file/upload', '127.0.0.1', NULL, '描述: 上传文件：#{#file.originalFilename}', '{\"code\":500,\"message\":\"上传失败: 本地文件上传失败: java.io.FileNotFoundException: C:\\\\Users\\\\Administrator\\\\AppData\\\\Local\\\\Temp\\\\tomcat.8080.12348213868114401789\\\\work\\\\Tomcat\\\\localhost\\\\ROOT\\\\.\\\\upload\\\\upload\\\\2025\\\\07\\\\08\\\\88b29180328c444a82d466b2415091c5.jpg (系统找不到指定的路径。)\",\"data\":null,\"timestamp\":1751954328977,\"error\":true,\"success\":false}', 0, NULL, '2025-07-08 13:58:48', 1);
INSERT INTO `sys_oper_log` VALUES (40, '文件管理', 3, 'com.mars.admin.modules.base.controller.FileUploadController.delete()', 'DELETE', 1, 'admin', '未知部门', '/file/6', '127.0.0.1', NULL, '{\"id\":6} | 描述: 删除文件，文件ID：#{#id}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null,\"timestamp\":1751954334745,\"error\":false,\"success\":true}', 0, NULL, '2025-07-08 13:58:54', 4);
INSERT INTO `sys_oper_log` VALUES (41, '文件管理', 10, 'com.mars.admin.modules.base.controller.FileUploadController.upload()', 'POST', 1, NULL, NULL, '/file/upload', '127.0.0.1', NULL, '描述: 上传文件：#{#file.originalFilename}', '{\"code\":500,\"message\":\"上传失败: 本地文件上传失败: java.io.FileNotFoundException: C:\\\\Users\\\\Administrator\\\\AppData\\\\Local\\\\Temp\\\\tomcat.8080.12348213868114401789\\\\work\\\\Tomcat\\\\localhost\\\\ROOT\\\\.\\\\upload\\\\upload\\\\2025\\\\07\\\\08\\\\b3c108b13c524d07957c8b9c4fc8c301.jpg (系统找不到指定的路径。)\",\"data\":null,\"timestamp\":1751954340921,\"error\":true,\"success\":false}', 0, NULL, '2025-07-08 13:59:00', 0);
INSERT INTO `sys_oper_log` VALUES (42, '菜单管理', 3, 'com.mars.admin.modules.system.controller.SysMenuController.deleteById()', 'DELETE', 1, 'admin', '未知部门', '/system/menu/3', '127.0.0.1', NULL, '{\"id\":3} | 描述: 删除菜单', '{\"code\":200,\"message\":\"删除成功\",\"data\":null,\"timestamp\":1751954391504,\"error\":false,\"success\":true}', 0, NULL, '2025-07-08 13:59:51', 2);
INSERT INTO `sys_oper_log` VALUES (43, '文件管理', 10, 'com.mars.admin.modules.base.controller.FileUploadController.upload()', 'POST', 1, NULL, NULL, '/file/upload', '127.0.0.1', NULL, '描述: 上传文件：#{#file.originalFilename}', '{\"code\":200,\"message\":\"上传成功\",\"data\":{\"id\":7,\"fileName\":\"3098111a10b340d79926b877bedbb8ea.jpg\",\"originalName\":\"img_v3_02mm_6eaf2ac0-71ba-43ed-a56f-3913e28ab79g.jpg\",\"fileSuffix\":\"jpg\",\"url\":\"http://150.158.49.140:9000/test/upload/2025/07/08/3098111a10b340d79926b877bedbb8ea.jpg\",\"size\":100276,\"filePath\":\"upload/2025/07/08/3098111a10b340d79926b877bedbb8ea.jpg\",\"contentType\":\"image/jpeg\",\"configKey\":\"minio\",\"uploadStatus\":1},\"timestamp\":1751961344340,\"error\":false,\"success\":true}', 0, NULL, '2025-07-08 15:55:44', 6);
INSERT INTO `sys_oper_log` VALUES (44, '文件管理', 10, 'com.mars.admin.modules.base.controller.FileUploadController.upload()', 'POST', 1, NULL, NULL, '/file/upload', '127.0.0.1', NULL, '描述: 上传文件：#{#file.originalFilename}', '{\"code\":200,\"message\":\"上传成功\",\"data\":{\"id\":8,\"fileName\":\"05d75ad7b8aa400183ea2712293ca9c4.jpg\",\"originalName\":\"profile.jpg\",\"fileSuffix\":\"jpg\",\"url\":\"http://150.158.49.140:9000/test/upload/2025/07/08/05d75ad7b8aa400183ea2712293ca9c4.jpg\",\"size\":93672,\"filePath\":\"upload/2025/07/08/05d75ad7b8aa400183ea2712293ca9c4.jpg\",\"contentType\":\"image/jpeg\",\"configKey\":\"minio\",\"uploadStatus\":1},\"timestamp\":1751961386577,\"error\":false,\"success\":true}', 0, NULL, '2025-07-08 15:56:26', 0);
INSERT INTO `sys_oper_log` VALUES (45, '文件管理', 10, 'com.mars.admin.modules.base.controller.FileUploadController.upload()', 'POST', 1, NULL, NULL, '/file/upload', '127.0.0.1', NULL, '描述: 上传文件：#{#file.originalFilename}', '{\"code\":200,\"message\":\"上传成功\",\"data\":{\"id\":9,\"fileName\":\"6a6007c5eea44aca847344b0ea689c36.jpg\",\"originalName\":\"profile.jpg\",\"fileSuffix\":\"jpg\",\"url\":\"http://150.158.49.140:9000/test/upload/2025/07/08/6a6007c5eea44aca847344b0ea689c36.jpg\",\"size\":93672,\"filePath\":\"upload/2025/07/08/6a6007c5eea44aca847344b0ea689c36.jpg\",\"contentType\":\"image/jpeg\",\"configKey\":\"minio\",\"uploadStatus\":1},\"timestamp\":1751961420504,\"error\":false,\"success\":true}', 0, NULL, '2025-07-08 15:57:00', 0);
INSERT INTO `sys_oper_log` VALUES (46, '文件管理', 10, 'com.mars.admin.modules.base.controller.FileUploadController.upload()', 'POST', 1, NULL, NULL, '/file/upload', '127.0.0.1', NULL, '描述: 上传文件：#{#file.originalFilename}', '{\"code\":200,\"message\":\"上传成功\",\"data\":{\"id\":10,\"fileName\":\"2e6040c282ca4023b99154d64e256b1c.jpg\",\"originalName\":\"profile.jpg\",\"fileSuffix\":\"jpg\",\"url\":\"http://localhost:8080/file/2025/07/09/2e6040c282ca4023b99154d64e256b1c.jpg\",\"size\":93672,\"filePath\":\"2025/07/09\\\\2e6040c282ca4023b99154d64e256b1c.jpg\",\"contentType\":\"image/jpeg\",\"configKey\":\"local\",\"uploadStatus\":1},\"timestamp\":1752045805222,\"error\":false,\"success\":true}', 0, NULL, '2025-07-09 15:23:25', 5);
INSERT INTO `sys_oper_log` VALUES (47, '文件管理', 10, 'com.mars.admin.modules.base.controller.FileUploadController.upload()', 'POST', 1, NULL, NULL, '/file/upload', '127.0.0.1', NULL, '描述: 上传文件：#{#file.originalFilename}', '{\"code\":200,\"message\":\"上传成功\",\"data\":{\"id\":11,\"fileName\":\"738b8d79e4ae4354958001222ddd4004.jpg\",\"originalName\":\"profile.jpg\",\"fileSuffix\":\"jpg\",\"url\":\"http://150.158.49.140:9000/test/upload/2025/07/09/738b8d79e4ae4354958001222ddd4004.jpg\",\"size\":93672,\"filePath\":\"upload/2025/07/09/738b8d79e4ae4354958001222ddd4004.jpg\",\"contentType\":\"image/jpeg\",\"configKey\":\"minio\",\"uploadStatus\":1},\"timestamp\":1752046551316,\"error\":false,\"success\":true}', 0, NULL, '2025-07-09 15:36:10', 19562);
INSERT INTO `sys_oper_log` VALUES (48, '文件管理', 10, 'com.mars.admin.modules.base.controller.FileUploadController.upload()', 'POST', 1, NULL, NULL, '/file/upload', '127.0.0.1', NULL, '描述: 上传文件：#{#file.originalFilename}', '{\"code\":200,\"message\":\"上传成功\",\"data\":{\"id\":12,\"fileName\":\"0a6fadf4d7804fdf86a1b371f819c0cb.jpg\",\"originalName\":\"profile.jpg\",\"fileSuffix\":\"jpg\",\"url\":\"http://150.158.49.140:9000/test/upload/2025/07/09/0a6fadf4d7804fdf86a1b371f819c0cb.jpg\",\"size\":93672,\"filePath\":\"upload/2025/07/09/0a6fadf4d7804fdf86a1b371f819c0cb.jpg\",\"contentType\":\"image/jpeg\",\"configKey\":\"minio\",\"uploadStatus\":1},\"timestamp\":1752046913335,\"error\":false,\"success\":true}', 0, NULL, '2025-07-09 15:43:21', 27487);
INSERT INTO `sys_oper_log` VALUES (49, '文件管理', 10, 'com.mars.admin.modules.base.controller.FileUploadController.upload()', 'POST', 1, NULL, NULL, '/file/upload', '127.0.0.1', NULL, '描述: 上传文件：#{#file.originalFilename}', '{\"code\":200,\"message\":\"上传成功\",\"data\":{\"id\":13,\"fileName\":\"541f2175a8d94b85bbaa2e89f3c82643.jpg\",\"originalName\":\"profile.jpg\",\"fileSuffix\":\"jpg\",\"url\":\"http://150.158.49.140:9000/test/upload/2025/07/09/541f2175a8d94b85bbaa2e89f3c82643.jpg\",\"size\":93672,\"filePath\":\"upload/2025/07/09/541f2175a8d94b85bbaa2e89f3c82643.jpg\",\"contentType\":\"image/jpeg\",\"configKey\":\"minio\",\"uploadStatus\":1},\"timestamp\":1752047023383,\"error\":false,\"success\":true}', 0, NULL, '2025-07-09 15:44:19', 36373);
INSERT INTO `sys_oper_log` VALUES (50, '岗位管理', 1, 'com.mars.admin.modules.system.controller.SysPostController.save()', 'POST', 1, 'admin', '未知部门', '/system/post', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":\"2025-07-09 16:10:42\",\"updateTime\":null,\"createBy\":1,\"updateBy\":null,\"isDeleted\":0,\"deleteTime\":null,\"id\":5,\"postCode\":\"test111\",\"postName\":\"test\",\"postSort\":0,\"status\":1,\"isSystem\":null,\"remark\":\"\"}} | 描述: 新增岗位：#{#entity.postName}', '{\"code\":200,\"message\":\"新增成功\",\"data\":null,\"timestamp\":1752048642406,\"error\":false,\"success\":true}', 0, NULL, '2025-07-09 16:10:42', 7);
INSERT INTO `sys_oper_log` VALUES (51, '文件管理', 10, 'com.mars.admin.modules.base.controller.FileUploadController.upload()', 'POST', 1, NULL, NULL, '/file/upload', '127.0.0.1', NULL, '描述: 上传文件：#{#file.originalFilename}', '{\"code\":200,\"message\":\"上传成功\",\"data\":{\"id\":14,\"fileName\":\"7c035439baca475e9434b9e2daec2c00.jpg\",\"originalName\":\"profile.jpg\",\"fileSuffix\":\"jpg\",\"url\":\"http://150.158.49.140:9000/test/upload/2025/07/09/7c035439baca475e9434b9e2daec2c00.jpg\",\"size\":93672,\"filePath\":\"upload/2025/07/09/7c035439baca475e9434b9e2daec2c00.jpg\",\"contentType\":\"image/jpeg\",\"configKey\":\"minio\",\"uploadStatus\":1},\"timestamp\":1752049750887,\"error\":false,\"success\":true}', 0, NULL, '2025-07-09 16:29:10', 26);
INSERT INTO `sys_oper_log` VALUES (52, '岗位管理', 1, 'com.mars.admin.modules.system.controller.SysPostController.save()', 'POST', 1, 'admin', '未知部门', '/system/post', '127.0.0.1', NULL, '{\"entity\":{\"createTime\":\"2025-07-09 16:30:04\",\"updateTime\":null,\"createBy\":1,\"updateBy\":null,\"isDeleted\":0,\"deleteTime\":null,\"id\":6,\"postCode\":\"2323\",\"postName\":\"test1213\",\"postSort\":0,\"status\":1,\"isSystem\":null,\"remark\":\"\"}} | 描述: 新增岗位：#{#entity.postName}', '{\"code\":200,\"message\":\"新增成功\",\"data\":null,\"timestamp\":1752049804698,\"error\":false,\"success\":true}', 0, NULL, '2025-07-09 16:30:04', 23);
INSERT INTO `sys_oper_log` VALUES (53, '岗位管理', 2, 'com.mars.admin.modules.system.controller.SysPostController.changeStatus()', 'PUT', 1, 'admin', '未知部门', '/system/post/changeStatus', '127.0.0.1', NULL, '{\"post\":{\"createTime\":null,\"updateTime\":\"2025-07-09 16:31:29\",\"createBy\":null,\"updateBy\":1,\"isDeleted\":null,\"deleteTime\":null,\"id\":6,\"postCode\":null,\"postName\":null,\"postSort\":null,\"status\":0,\"isSystem\":null,\"remark\":null}} | 描述: 修改岗位状态，ID：#{#post.id}，状态：#{#post.status}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null,\"timestamp\":1752049889266,\"error\":false,\"success\":true}', 0, NULL, '2025-07-09 16:31:29', 10);
INSERT INTO `sys_oper_log` VALUES (54, '岗位管理', 2, 'com.mars.admin.modules.system.controller.SysPostController.changeStatus()', 'PUT', 1, 'admin', '未知部门', '/system/post/changeStatus', '127.0.0.1', NULL, '{\"post\":{\"createTime\":null,\"updateTime\":\"2025-07-09 16:31:37\",\"createBy\":null,\"updateBy\":1,\"isDeleted\":null,\"deleteTime\":null,\"id\":6,\"postCode\":null,\"postName\":null,\"postSort\":null,\"status\":1,\"isSystem\":null,\"remark\":null}} | 描述: 修改岗位状态，ID：#{#post.id}，状态：#{#post.status}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null,\"timestamp\":1752049897212,\"error\":false,\"success\":true}', 0, NULL, '2025-07-09 16:31:37', 4);
INSERT INTO `sys_oper_log` VALUES (55, '岗位管理', 3, 'com.mars.admin.modules.system.controller.SysPostController.deleteById()', 'DELETE', 1, 'admin', '未知部门', '/system/post/6', '127.0.0.1', NULL, '{\"id\":6} | 描述: 删除岗位，ID：#{#id}', '{\"code\":200,\"message\":\"删除成功\",\"data\":null,\"timestamp\":1752049900015,\"error\":false,\"success\":true}', 0, NULL, '2025-07-09 16:31:40', 5);
INSERT INTO `sys_oper_log` VALUES (56, '岗位管理', 3, 'com.mars.admin.modules.system.controller.SysPostController.deleteById()', 'DELETE', 1, 'admin', '未知部门', '/system/post/5', '127.0.0.1', NULL, '{\"id\":5} | 描述: 删除岗位，ID：#{#id}', '{\"code\":200,\"message\":\"删除成功\",\"data\":null,\"timestamp\":1752049902826,\"error\":false,\"success\":true}', 0, NULL, '2025-07-09 16:31:42', 4);

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '对象存储主键',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件名',
  `original_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '原名',
  `file_suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件后缀名',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件地址',
  `size` bigint NULL DEFAULT 0 COMMENT '文件大小',
  `config_key` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置key',
  `file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件路径',
  `content_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件类型',
  `upload_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '上传ID(用于分片上传)',
  `upload_status` tinyint(1) NULL DEFAULT 1 COMMENT '上传状态：0-失败，1-成功',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_config_key`(`config_key` ASC) USING BTREE,
  INDEX `idx_upload_status`(`upload_status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_is_deleted`(`is_deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '对象存储表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oss
-- ----------------------------
INSERT INTO `sys_oss` VALUES (1, '59e354e153c64b9886e4f5c7a83f214d.jpg', 'logo.jpg', 'jpg', 'http://150.158.49.140:9000/test/upload/2025/06/28/59e354e153c64b9886e4f5c7a83f214d.jpg', 60453, 'minio', 'upload/2025/06/28/59e354e153c64b9886e4f5c7a83f214d.jpg', 'image/jpeg', NULL, 1, NULL, '2025-06-28 11:55:40', '2025-06-28 11:55:40', 1, NULL, 0, NULL);
INSERT INTO `sys_oss` VALUES (2, 'b5f0f4fbb41c489fa1799c50db6247de.jpg', 'img_v3_02mm_6eaf2ac0-71ba-43ed-a56f-3913e28ab79g.jpg', 'jpg', 'http://150.158.49.140:9000/test/upload/2025/07/03/b5f0f4fbb41c489fa1799c50db6247de.jpg', 100276, 'minio', 'upload/2025/07/03/b5f0f4fbb41c489fa1799c50db6247de.jpg', 'image/jpeg', NULL, 1, NULL, '2025-07-03 13:44:25', '2025-07-03 13:44:25', NULL, NULL, 0, NULL);
INSERT INTO `sys_oss` VALUES (3, '55afdcc5602c458ea03a22a2bc55bf16.jpeg', '头像.jpeg', 'jpeg', 'http://150.158.49.140:9000/test/upload/2025/07/04/55afdcc5602c458ea03a22a2bc55bf16.jpeg', 8838, 'minio', 'upload/2025/07/04/55afdcc5602c458ea03a22a2bc55bf16.jpeg', 'image/jpeg', NULL, 1, NULL, '2025-07-04 11:04:33', '2025-07-04 11:04:33', NULL, NULL, 0, NULL);
INSERT INTO `sys_oss` VALUES (4, 'ce27368b3efa4c1d81c0dc8c49ea8f89.jpg', 'Bears.jpg', 'jpg', 'http://150.158.49.140:9000/test/upload/2025/07/04/ce27368b3efa4c1d81c0dc8c49ea8f89.jpg', 123500, 'minio', 'upload/2025/07/04/ce27368b3efa4c1d81c0dc8c49ea8f89.jpg', 'image/jpeg', NULL, 1, NULL, '2025-07-04 11:18:33', '2025-07-04 11:18:33', NULL, NULL, 0, NULL);
INSERT INTO `sys_oss` VALUES (5, '7af03b4667b14d4ba3f274a194365c19.jpeg', '头像.jpeg', 'jpeg', 'http://150.158.49.140:9000/test/upload/2025/07/04/7af03b4667b14d4ba3f274a194365c19.jpeg', 8838, 'minio', 'upload/2025/07/04/7af03b4667b14d4ba3f274a194365c19.jpeg', 'image/jpeg', NULL, 1, NULL, '2025-07-04 11:18:40', '2025-07-04 11:18:40', NULL, NULL, 0, NULL);
INSERT INTO `sys_oss` VALUES (6, 'fdebb9be36184ceaa0bebf3c49f63b00.jpg', 'sex.jpg', 'jpg', 'http://150.158.49.140:9000/test/upload/2025/07/08/fdebb9be36184ceaa0bebf3c49f63b00.jpg', 39854, 'minio', 'upload/2025/07/08/fdebb9be36184ceaa0bebf3c49f63b00.jpg', 'image/jpeg', NULL, 1, NULL, '2025-07-08 13:56:10', '2025-07-08 13:58:54', NULL, NULL, 1, '2025-07-08 13:58:54');
INSERT INTO `sys_oss` VALUES (7, '3098111a10b340d79926b877bedbb8ea.jpg', 'img_v3_02mm_6eaf2ac0-71ba-43ed-a56f-3913e28ab79g.jpg', 'jpg', 'http://150.158.49.140:9000/test/upload/2025/07/08/3098111a10b340d79926b877bedbb8ea.jpg', 100276, 'minio', 'upload/2025/07/08/3098111a10b340d79926b877bedbb8ea.jpg', 'image/jpeg', NULL, 1, NULL, '2025-07-08 15:55:44', '2025-07-08 15:55:44', NULL, NULL, 0, NULL);
INSERT INTO `sys_oss` VALUES (8, '05d75ad7b8aa400183ea2712293ca9c4.jpg', 'profile.jpg', 'jpg', 'http://150.158.49.140:9000/test/upload/2025/07/08/05d75ad7b8aa400183ea2712293ca9c4.jpg', 93672, 'minio', 'upload/2025/07/08/05d75ad7b8aa400183ea2712293ca9c4.jpg', 'image/jpeg', NULL, 1, NULL, '2025-07-08 15:56:26', '2025-07-08 15:56:26', NULL, NULL, 0, NULL);
INSERT INTO `sys_oss` VALUES (9, '6a6007c5eea44aca847344b0ea689c36.jpg', 'profile.jpg', 'jpg', 'http://150.158.49.140:9000/test/upload/2025/07/08/6a6007c5eea44aca847344b0ea689c36.jpg', 93672, 'minio', 'upload/2025/07/08/6a6007c5eea44aca847344b0ea689c36.jpg', 'image/jpeg', NULL, 1, NULL, '2025-07-08 15:57:00', '2025-07-08 15:57:00', NULL, NULL, 0, NULL);
INSERT INTO `sys_oss` VALUES (10, '2e6040c282ca4023b99154d64e256b1c.jpg', 'profile.jpg', 'jpg', 'http://localhost:8080/file/2025/07/09/2e6040c282ca4023b99154d64e256b1c.jpg', 93672, 'local', '2025/07/09\\2e6040c282ca4023b99154d64e256b1c.jpg', 'image/jpeg', NULL, 1, NULL, '2025-07-09 15:23:25', '2025-07-09 15:23:25', NULL, NULL, 0, NULL);
INSERT INTO `sys_oss` VALUES (11, '738b8d79e4ae4354958001222ddd4004.jpg', 'profile.jpg', 'jpg', 'http://150.158.49.140:9000/test/upload/2025/07/09/738b8d79e4ae4354958001222ddd4004.jpg', 93672, 'minio', 'upload/2025/07/09/738b8d79e4ae4354958001222ddd4004.jpg', 'image/jpeg', NULL, 1, NULL, '2025-07-09 15:35:51', '2025-07-09 15:35:51', NULL, NULL, 0, NULL);
INSERT INTO `sys_oss` VALUES (12, '0a6fadf4d7804fdf86a1b371f819c0cb.jpg', 'profile.jpg', 'jpg', 'http://150.158.49.140:9000/test/upload/2025/07/09/0a6fadf4d7804fdf86a1b371f819c0cb.jpg', 93672, 'minio', 'upload/2025/07/09/0a6fadf4d7804fdf86a1b371f819c0cb.jpg', 'image/jpeg', NULL, 1, NULL, '2025-07-09 15:41:53', '2025-07-09 15:41:53', NULL, NULL, 0, NULL);
INSERT INTO `sys_oss` VALUES (13, '541f2175a8d94b85bbaa2e89f3c82643.jpg', 'profile.jpg', 'jpg', 'http://150.158.49.140:9000/test/upload/2025/07/09/541f2175a8d94b85bbaa2e89f3c82643.jpg', 93672, 'minio', 'upload/2025/07/09/541f2175a8d94b85bbaa2e89f3c82643.jpg', 'image/jpeg', NULL, 1, NULL, '2025-07-09 15:43:43', '2025-07-09 15:43:43', NULL, NULL, 0, NULL);
INSERT INTO `sys_oss` VALUES (14, '7c035439baca475e9434b9e2daec2c00.jpg', 'profile.jpg', 'jpg', 'http://150.158.49.140:9000/test/upload/2025/07/09/7c035439baca475e9434b9e2daec2c00.jpg', 93672, 'minio', 'upload/2025/07/09/7c035439baca475e9434b9e2daec2c00.jpg', 'image/jpeg', NULL, 1, NULL, '2025-07-09 16:29:10', '2025-07-09 16:29:10', NULL, NULL, 0, NULL);

-- ----------------------------
-- Table structure for sys_oss_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss_config`;
CREATE TABLE `sys_oss_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置key',
  `access_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'accessKey',
  `secret_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'secretKey',
  `bucket_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '桶名称',
  `prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '前缀',
  `endpoint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '访问站点',
  `domain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '自定义域名',
  `is_https` tinyint(1) NULL DEFAULT 0 COMMENT '是否https：0-否，1-是',
  `region` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '区域',
  `access_policy` tinyint(1) NULL DEFAULT 1 COMMENT '桶权限类型：0-private，1-public，2-custom',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-停用，1-启用',
  `ext1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '扩展字段1',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_config_key`(`config_key` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_is_deleted`(`is_deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'OSS配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oss_config
-- ----------------------------
INSERT INTO `sys_oss_config` VALUES (1, 'minio', 'admin', 'admin123mars', 'test', '', 'http://150.158.49.140:9000', 'http://150.158.49.140:9000', 0, 'us-east-1', 1, 1, NULL, 'MinIO本地存储配置', '2025-06-27 16:00:00', '2025-06-28 11:43:15', 1, NULL, 0, NULL);
INSERT INTO `sys_oss_config` VALUES (2, 'aliyun', '', '', '', '', '', '', 1, 'oss-cn-beijing', 0, 0, NULL, '阿里云OSS配置', '2025-06-27 16:00:00', '2025-06-27 16:00:00', 1, NULL, 0, NULL);
INSERT INTO `sys_oss_config` VALUES (3, 'local', '', '', '', 'upload', '', '', 0, '', 1, 1, NULL, '本地存储配置', '2025-06-27 16:00:00', '2025-06-27 16:00:00', 1, NULL, 0, NULL);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int NULL DEFAULT 0 COMMENT '显示顺序',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-停用，1-正常',
  `is_system` tinyint(1) NULL DEFAULT 0 COMMENT '是否系统岗位：0-否，1-是(不可删除)',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_post_code`(`post_code` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_post_sort`(`post_sort` ASC) USING BTREE,
  INDEX `idx_is_deleted`(`is_deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统岗位表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'CEO', '董事长', 1, 1, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_post` VALUES (2, 'CTO', '技术总监', 2, 1, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_post` VALUES (3, 'DEV', '开发工程师', 3, 1, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_post` VALUES (4, 'TEST', '测试工程师', 4, 1, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_post` VALUES (5, 'test111', 'test', 0, 1, 0, '', '2025-07-09 16:10:42', '2025-07-09 16:31:42', 1, NULL, 1, '2025-07-09 16:31:42');
INSERT INTO `sys_post` VALUES (6, '2323', 'test1213', 0, 1, 0, '', '2025-07-09 16:30:04', '2025-07-09 16:31:40', 1, 1, 1, '2025-07-09 16:31:40');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色编码',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NULL DEFAULT 0 COMMENT '显示顺序',
  `data_scope` tinyint(1) NULL DEFAULT 1 COMMENT '数据范围：1-全部，2-自定义，3-本部门，4-本部门及以下，5-仅本人',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `is_system` tinyint(1) NULL DEFAULT 0 COMMENT '是否系统角色：0-否，1-是(不可删除)',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色描述',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_code`(`role_code` ASC) USING BTREE,
  UNIQUE INDEX `uk_role_key`(`role_key` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_role_sort`(`role_sort` ASC) USING BTREE,
  INDEX `idx_is_deleted`(`is_deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'SUPER_ADMIN', 'admin', 1, 1, 1, 1, 1, 1, '超级管理员', '', '2025-06-25 16:34:16', '2025-07-08 11:33:30', 1, 1, 0, NULL);
INSERT INTO `sys_role` VALUES (2, '系统管理员', 'SYSTEM_ADMIN', 'system', 2, 2, 1, 1, 1, 1, '系统管理员', NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_role` VALUES (3, '普通用户', 'COMMON_USER', 'common', 3, 5, 1, 1, 1, 0, '普通用户', NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE,
  INDEX `idx_dept_id`(`dept_id` ASC) USING BTREE,
  CONSTRAINT `fk_sys_role_dept_dept` FOREIGN KEY (`dept_id`) REFERENCES `sys_dept` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_sys_role_dept_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色和部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE,
  INDEX `idx_menu_id`(`menu_id` ASC) USING BTREE,
  CONSTRAINT `fk_sys_role_menu_menu` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_sys_role_menu_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 2, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 100, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 101, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 102, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 103, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 104, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 105, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 106, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 107, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 108, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 109, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 301, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 302, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 303, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1000, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1001, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1002, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1003, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1004, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1005, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1006, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1007, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1008, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1009, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1010, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1011, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1012, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1013, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1014, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1015, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1016, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1017, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1018, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1070, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1071, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1072, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1080, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1081, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1082, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1083, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1084, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 2100, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 2101, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 2102, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 2103, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 2104, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 2105, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 2106, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 2107, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 2108, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 2109, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 2110, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 2111, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 2112, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 2113, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30201, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30202, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30203, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30204, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30301, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30302, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30303, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30304, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30305, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30306, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30308, '2025-07-08 11:33:28', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30309, '2025-07-08 11:57:25', NULL);
INSERT INTO `sys_role_menu` VALUES (1, 30310, '2025-07-08 15:02:41', NULL);
INSERT INTO `sys_role_menu` VALUES (1, 30311, '2025-07-08 15:02:49', NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码(MD5加密)',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '微信OpenId，用于小程序登录',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像URL',
  `gender` tinyint(1) NULL DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `user_type` tinyint(1) NULL DEFAULT 1 COMMENT '用户类型：1-系统用户，2-普通用户',
  `login_count` int NULL DEFAULT 0 COMMENT '登录次数',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `password_update_time` datetime NULL DEFAULT NULL COMMENT '密码修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `uk_email`(`email` ASC) USING BTREE,
  UNIQUE INDEX `uk_phone`(`phone` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_user_type`(`user_type` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_is_deleted`(`is_deleted` ASC) USING BTREE,
  INDEX `idx_open_id`(`open_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '超级管理员', '系统管理员', 'admin@mars.com', '184****8369', NULL, 'http://150.158.49.140:9000/test/upload/2025/07/08/6a6007c5eea44aca847344b0ea689c36.jpg', 0, '2025-07-03', 1, 1, 127, '2025-07-09 16:25:08', '0:0:0:0:0:0:0:1', '2025-07-04 11:19:05', '系统默认超级管理员', '2025-06-25 16:34:16', '2025-07-09 16:25:08', 1, 1, 0, NULL);

-- ----------------------------
-- Table structure for sys_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_dept`;
CREATE TABLE `sys_user_dept`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  `is_main` tinyint(1) NULL DEFAULT 0 COMMENT '是否主部门：0-否，1-是',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`user_id`, `dept_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_dept_id`(`dept_id` ASC) USING BTREE,
  INDEX `idx_is_main`(`is_main` ASC) USING BTREE,
  CONSTRAINT `fk_sys_user_dept_dept` FOREIGN KEY (`dept_id`) REFERENCES `sys_dept` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_sys_user_dept_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_dept
-- ----------------------------
INSERT INTO `sys_user_dept` VALUES (1, 100, 1, '2025-06-25 16:34:16', 1);

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_post_id`(`post_id` ASC) USING BTREE,
  CONSTRAINT `fk_sys_user_post_post` FOREIGN KEY (`post_id`) REFERENCES `sys_post` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_sys_user_post_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1, '2025-06-25 16:34:16', 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE,
  CONSTRAINT `fk_sys_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_sys_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, '2025-06-25 16:34:16', 1);

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（1正常 0禁用）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除（0否 1是）',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100015 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_user
-- ----------------------------

-- ----------------------------
-- Procedure structure for insert_fake_users
-- ----------------------------
DROP PROCEDURE IF EXISTS `insert_fake_users`;
delimiter ;;
CREATE PROCEDURE `insert_fake_users`()
BEGIN
  DECLARE i INT DEFAULT 1;
  WHILE i <= 100000 DO
    INSERT INTO tb_user (
      username,
      password,
      email,
      phone,
      status,
      create_time,
      update_time,
      create_by,
      update_by,
      is_deleted,
      delete_time
    )
    VALUES (
      CONCAT('user_', i),
      MD5('123456'),  -- 所有密码都为123456的MD5
      CONCAT('user_', i, '@example.com'),
      CONCAT('139', LPAD(FLOOR(RAND() * 100000000), 8, '0')),
      IF(MOD(i, 2) = 0, 1, 0),
      NOW(),
      NOW(),
      1,
      1,
      0,
      NULL
    );
    SET i = i + 1;
  END WHILE;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
