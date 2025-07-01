/*
 Navicat Premium Data Transfer

 Source Server         : qiu
 Source Server Type    : MySQL
 Source Server Version : 80036 (8.0.36)
 Source Host           : localhost:3306
 Source Schema         : mars_admin

 Target Server Type    : MySQL
 Target Server Version : 80036 (8.0.36)
 File Encoding         : 65001

 Date: 01/07/2025 22:49:52
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
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', 1, '用户性别列表', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', 1, '菜单状态列表', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', 1, '系统开关列表', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', 1, '任务状态列表', '2025-06-25 16:34:16', '2025-07-01 11:43:42', 1, NULL, 1, '2025-07-01 11:43:42');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', 1, '任务分组列表', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', 1, '系统是否列表', '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);

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
) ENGINE = InnoDB AUTO_INCREMENT = 91 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统访问记录' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 30306 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', 'SYSTEM', 'M', 'manage', '/manage', 'layout.base', NULL, NULL, 1, 0, '', 1, 1, '', 'system', 1, 1, NULL, '2025-06-25 16:34:16', '2025-06-26 15:01:07', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (2, 0, '系统监控', 'MONITOR', 'M', 'monitor', '/monitor', 'layout.base', NULL, NULL, 1, 0, '', 1, 1, '', 'monitor', 2, 1, NULL, '2025-06-25 16:34:16', '2025-06-26 15:02:57', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (3, 0, '系统工具', 'TOOL', 'M', 'tool', '/tool', 'layout.base', NULL, NULL, 1, 0, '', 1, 1, '', 'tool', 3, 1, NULL, '2025-06-25 16:34:16', '2025-06-26 15:03:19', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (100, 1018, '用户列表', 'USER_MANAGE', 'C', 'manage_user', '/manage/user', 'view.manage_user', NULL, NULL, 1, 0, '', 1, 1, 'system:user:list', 'user', 1, 1, NULL, '2025-06-25 16:34:16', '2025-07-01 18:25:39', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (101, 1017, '角色管理', 'ROLE_MANAGE', 'C', 'manage_role', '/manage/role', 'view.manage_role', NULL, NULL, 1, 0, '', 1, 1, 'system:role:list', 'peoples', 2, 1, NULL, '2025-06-25 16:34:16', '2025-06-30 21:54:17', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (102, 1017, '菜单管理', 'MENU_MANAGE', 'C', 'manage_menu', '/manage/menu', 'view.manage_menu', NULL, NULL, 1, 0, '', 1, 1, 'system:menu:list', 'tree-table', 3, 1, NULL, '2025-06-25 16:34:16', '2025-06-30 21:54:17', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (103, 1017, '部门管理', 'DEPT_MANAGE', 'C', 'manage_dept', '/manage/dept', 'view.manage_dept', NULL, NULL, 1, 0, '', 1, 1, 'system:dept:list', 'tree', 4, 1, NULL, '2025-06-25 16:34:16', '2025-06-30 21:54:17', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (104, 1017, '岗位管理', 'POST_MANAGE', 'C', 'manage_post', '/manage/post', 'view.manage_post', NULL, NULL, 1, 0, '', 1, 1, 'system:post:list', 'post', 5, 1, NULL, '2025-06-25 16:34:16', '2025-06-30 21:54:17', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (105, 1, '字典管理', 'DICT_MANAGE', 'C', 'manage_dict', '/manage/dict', 'view.manage_dict', NULL, NULL, 1, 0, '', 1, 1, 'system:dict:list', 'dict', 6, 1, NULL, '2025-06-25 16:34:16', '2025-06-26 15:02:40', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (106, 1, '参数设置', 'CONFIG_MANAGE', 'C', 'manage_config', '/manage/config', 'view.manage_config', NULL, NULL, 1, 0, '', 1, 1, 'system:config:list', 'edit', 7, 1, NULL, '2025-06-25 16:34:16', '2025-06-26 15:02:49', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (107, 1, '缓存监控', 'CACHE_MONITOR', 'C', 'manage_cache-monitor', '/manage/cache-monitor', 'view.manage_cache-monitor', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:monitor', 'mdi:monitor-dashboard', 8, 1, NULL, '2025-07-01 15:02:21', '2025-07-01 15:02:21', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (108, 1, '缓存列表', 'CACHE_LIST', 'C', 'manage_cache-list', '/manage/cache-list', 'view.manage_cache-list', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:list', 'mdi:database-search', 9, 1, NULL, '2025-07-01 15:02:21', '2025-07-01 15:02:21', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (301, 0, '文件管理', 'FILE_MANAGE', 'M', 'file-manage', '/file-manage', '', NULL, NULL, 1, 0, '', 1, 1, '', 'ic:round-folder', 301, 0, '文件管理主菜单', '2025-07-01 20:04:43', '2025-07-01 20:24:32', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (302, 301, '文件列表', 'FILE_LIST', 'C', 'manage_file-manage', '/manage/file-manage', 'view.manage_file-manage', NULL, NULL, 1, 0, '', 1, 1, 'system:file:list', 'ic:round-insert-drive-file', 1, 0, '文件列表管理', '2025-07-01 20:04:43', '2025-07-01 20:24:37', 1, 1, 0, NULL);
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
INSERT INTO `sys_menu` VALUES (1016, 0, '首页', 'HOME', 'C', 'home', '/home', 'layout.base$view.home', NULL, NULL, 1, 0, '', 1, 1, NULL, 'mdi:home', 0, 1, NULL, '2025-06-26 16:06:53', '2025-06-26 17:20:50', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1017, 0, '权限管理', 'POWER', 'M', 'power', '/power', '', '', NULL, 1, 0, '', 1, 1, '', '', 20, NULL, NULL, '2025-06-30 21:51:59', '2025-06-30 21:51:59', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1018, 0, '用户管理', 'USER', 'M', 'user', '/user', '', '', NULL, 1, 0, '', 1, 1, '', 'user', 30, NULL, NULL, '2025-06-30 21:56:53', '2025-07-01 18:11:10', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (1070, 107, '缓存监控查询', 'CACHE_MONITOR_QUERY', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:monitor:query', '', 1, 1, NULL, '2025-07-01 15:02:52', '2025-07-01 15:02:52', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1071, 107, '缓存监控刷新', 'CACHE_MONITOR_REFRESH', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:monitor:refresh', '', 2, 1, NULL, '2025-07-01 15:02:52', '2025-07-01 15:02:52', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1072, 107, '缓存统计查询', 'CACHE_STATS_QUERY', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:stats:query', '', 3, 1, NULL, '2025-07-01 15:02:52', '2025-07-01 15:02:52', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1080, 108, '缓存列表查询', 'CACHE_LIST_QUERY', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:list:query', '', 1, 1, NULL, '2025-07-01 15:02:58', '2025-07-01 15:02:58', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1081, 108, '缓存详情查看', 'CACHE_DETAIL_VIEW', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:detail:view', '', 2, 1, NULL, '2025-07-01 15:02:58', '2025-07-01 15:02:58', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1082, 108, '缓存删除', 'CACHE_DELETE', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:delete', '', 3, 1, NULL, '2025-07-01 15:02:58', '2025-07-01 15:02:58', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1083, 108, '缓存批量删除', 'CACHE_BATCH_DELETE', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:batch:delete', '', 4, 1, NULL, '2025-07-01 15:02:58', '2025-07-01 15:02:58', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (1084, 108, '缓存空间清空', 'CACHE_SPACE_CLEAR', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:cache:space:clear', '', 5, 1, NULL, '2025-07-01 15:02:58', '2025-07-01 15:02:58', 1, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES (30201, 302, '文件上传', 'FILE_UPLOAD', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:file:upload', '', 1, 0, '文件上传权限', '2025-07-01 20:04:43', '2025-07-01 20:22:45', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30202, 302, '文件下载', 'FILE_DOWNLOAD', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:file:download', '', 2, 0, '文件下载权限', '2025-07-01 20:04:43', '2025-07-01 20:22:55', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30203, 302, '文件删除', 'FILE_DELETE', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:file:delete', '', 3, 0, '文件删除权限', '2025-07-01 20:04:43', '2025-07-01 20:23:06', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30204, 302, '文件预览', 'FILE_PREVIEW', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:file:preview', '', 4, 0, '文件预览权限', '2025-07-01 20:04:43', '2025-07-01 20:23:15', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30301, 303, '配置新增', 'OSS_COMFIG_ADD', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:ossConfig:add', '', 1, 0, 'OSS配置新增权限', '2025-07-01 20:04:43', '2025-07-01 20:23:29', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30302, 303, '配置编辑', 'OSS_COMFIG_EDIT', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:ossConfig:edit', '', 2, 0, 'OSS配置编辑权限', '2025-07-01 20:04:43', '2025-07-01 20:24:13', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30303, 303, '配置删除', 'OSS_COMFIG_REMOVE', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:ossConfig:remove', '', 3, 0, 'OSS配置删除权限', '2025-07-01 20:04:43', '2025-07-01 20:24:05', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30304, 303, '配置查询', 'OSS_COMFIG_QUERY', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:ossConfig:query', '', 4, 0, 'OSS配置查询权限', '2025-07-01 20:04:43', '2025-07-01 20:24:00', 1, 1, 0, NULL);
INSERT INTO `sys_menu` VALUES (30305, 303, '配置测试', 'OSS_COMFIG_TEST', 'F', '', '', '', NULL, NULL, 1, 0, '', 1, 1, 'system:ossConfig:test', '', 5, 0, 'OSS配置测试权限', '2025-07-01 20:04:43', '2025-07-01 20:23:54', 1, 1, 0, NULL);

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '操作日志记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '对象存储表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oss
-- ----------------------------
INSERT INTO `sys_oss` VALUES (1, '59e354e153c64b9886e4f5c7a83f214d.jpg', 'logo.jpg', 'jpg', 'http://150.158.49.140:9000/test/upload/2025/06/28/59e354e153c64b9886e4f5c7a83f214d.jpg', 60453, 'minio', 'upload/2025/06/28/59e354e153c64b9886e4f5c7a83f214d.jpg', 'image/jpeg', NULL, 1, NULL, '2025-06-28 11:55:40', '2025-06-28 11:55:40', 1, NULL, 0, NULL);

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
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统岗位表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'CEO', '董事长', 1, 1, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_post` VALUES (2, 'CTO', '技术总监', 2, 1, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_post` VALUES (3, 'DEV', '开发工程师', 3, 1, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
INSERT INTO `sys_post` VALUES (4, 'TEST', '测试工程师', 4, 1, 1, NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);

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
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'SUPER_ADMIN', 'admin', 1, 1, 1, 1, 1, 1, '超级管理员', NULL, '2025-06-25 16:34:16', '2025-06-25 16:34:16', 1, NULL, 0, NULL);
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
INSERT INTO `sys_role_menu` VALUES (1, 1, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 2, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 3, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 100, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 101, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 102, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 103, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 104, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 105, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 106, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 107, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 108, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 301, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 302, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 303, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1000, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1001, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1002, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1003, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1004, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1005, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1006, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1007, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1008, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1009, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1010, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1011, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1012, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1013, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1014, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1015, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1016, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1017, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1018, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1070, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1071, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1072, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1080, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1081, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1082, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1083, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 1084, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30201, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30202, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30203, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30204, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30301, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30302, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30303, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30304, '2025-07-01 20:20:12', 1);
INSERT INTO `sys_role_menu` VALUES (1, 30305, '2025-07-01 20:20:12', 1);

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
  INDEX `idx_is_deleted`(`is_deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '超级管理员', '系统管理员', 'admin@mars.com', '18483678369', NULL, 0, NULL, 1, 1, 86, '2025-07-01 22:17:42', '0:0:0:0:0:0:0:1', NULL, '系统默认超级管理员', '2025-06-25 16:34:16', '2025-07-01 22:17:41', 1, 1, 0, NULL);

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
