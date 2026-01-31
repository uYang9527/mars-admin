/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : mars_system

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 01/02/2026 00:06:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob NULL COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Blob类型的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`, `calendar_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '日历信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Cron类型的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint NOT NULL COMMENT '触发的时间',
  `sched_time` bigint NOT NULL COMMENT '定时器制定的时间',
  `priority` int NOT NULL COMMENT '优先级',
  `state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`, `entry_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '已触发的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务组名',
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务详细信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`, `lock_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '存储的悲观锁信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`, `trigger_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '暂停的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`, `instance_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '调度器状态表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '简单触发器的信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int NULL DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int NULL DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint NULL DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint NULL DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '同步机制的行锁表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint NULL DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint NULL DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int NULL DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '触发器的类型',
  `start_time` bigint NOT NULL COMMENT '开始时间',
  `end_time` bigint NULL DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint NULL DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  INDEX `sched_name`(`sched_name` ASC, `job_name` ASC, `job_group` ASC) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '触发器详细信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for sys_chat_group
-- ----------------------------
DROP TABLE IF EXISTS `sys_chat_group`;
CREATE TABLE `sys_chat_group`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '群ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '群名称',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '群头像',
  `owner_id` bigint NOT NULL COMMENT '群主ID',
  `announcement` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '群公告',
  `max_members` int NULL DEFAULT 200 COMMENT '最大成员数',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-解散 1-正常',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_owner_id`(`owner_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '群聊表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_chat_group
-- ----------------------------
INSERT INTO `sys_chat_group` VALUES (1, '测试', NULL, 2, '1111111111', 200, 0, '2026-01-31 11:24:29', '2026-01-31 12:39:26');
INSERT INTO `sys_chat_group` VALUES (2, '内部沟通群', NULL, 1, NULL, 200, 1, '2026-01-31 12:42:47', '2026-01-31 13:54:49');
INSERT INTO `sys_chat_group` VALUES (3, '测试', NULL, 1, NULL, 200, 1, '2026-01-31 23:20:05', '2026-01-31 23:20:37');

-- ----------------------------
-- Table structure for sys_chat_group_member
-- ----------------------------
DROP TABLE IF EXISTS `sys_chat_group_member`;
CREATE TABLE `sys_chat_group_member`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `group_id` bigint NOT NULL COMMENT '群ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '群内昵称',
  `role` tinyint NULL DEFAULT 0 COMMENT '角色：0-普通成员 1-管理员 2-群主',
  `muted` tinyint NULL DEFAULT 0 COMMENT '是否禁言：0-否 1-是',
  `join_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_user`(`group_id` ASC, `user_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '群成员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_chat_group_member
-- ----------------------------
INSERT INTO `sys_chat_group_member` VALUES (4, 2, 1, NULL, 2, 0, '2026-01-31 12:42:47');
INSERT INTO `sys_chat_group_member` VALUES (5, 2, 2, NULL, 0, 0, '2026-01-31 12:42:47');
INSERT INTO `sys_chat_group_member` VALUES (6, 2, 3, NULL, 0, 0, '2026-01-31 12:42:47');
INSERT INTO `sys_chat_group_member` VALUES (7, 3, 1, NULL, 2, 0, '2026-01-31 23:20:05');
INSERT INTO `sys_chat_group_member` VALUES (8, 3, 2, NULL, 0, 0, '2026-01-31 23:20:05');
INSERT INTO `sys_chat_group_member` VALUES (9, 3, 3, NULL, 0, 0, '2026-01-31 23:20:05');
INSERT INTO `sys_chat_group_member` VALUES (10, 3, 4, NULL, 0, 0, '2026-01-31 23:20:05');
INSERT INTO `sys_chat_group_member` VALUES (11, 3, 5, NULL, 0, 0, '2026-01-31 23:20:05');

-- ----------------------------
-- Table structure for sys_chat_group_message
-- ----------------------------
DROP TABLE IF EXISTS `sys_chat_group_message`;
CREATE TABLE `sys_chat_group_message`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `group_id` bigint NOT NULL COMMENT '群ID',
  `sender_id` bigint NOT NULL COMMENT '发送者ID',
  `sender_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发送者名称',
  `sender_avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发送者头像',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息内容',
  `msg_type` tinyint NULL DEFAULT 1 COMMENT '消息类型：1-文本 2-图片 3-文件 4-系统消息',
  `send_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_group_id`(`group_id` ASC) USING BTREE,
  INDEX `idx_send_time`(`send_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 109 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '群消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_chat_group_message
-- ----------------------------
INSERT INTO `sys_chat_group_message` VALUES (1, 1, 0, '系统消息', NULL, 'test 创建了群聊', 4, '2026-01-31 11:24:29');
INSERT INTO `sys_chat_group_message` VALUES (2, 1, 2, 'test', NULL, '你好', 1, '2026-01-31 11:24:35');
INSERT INTO `sys_chat_group_message` VALUES (3, 1, 1, '超级管理员', NULL, '1111', 1, '2026-01-31 11:24:49');
INSERT INTO `sys_chat_group_message` VALUES (4, 1, 2, 'test', NULL, '1212', 1, '2026-01-31 11:24:51');
INSERT INTO `sys_chat_group_message` VALUES (5, 1, 2, 'test', NULL, '哈哈哈', 1, '2026-01-31 11:24:57');
INSERT INTO `sys_chat_group_message` VALUES (6, 1, 1, '超级管理员', NULL, '牛逼的额啊', 1, '2026-01-31 11:25:03');
INSERT INTO `sys_chat_group_message` VALUES (7, 1, 2, 'test', NULL, '111', 1, '2026-01-31 11:25:13');
INSERT INTO `sys_chat_group_message` VALUES (8, 1, 1, '超级管理员', NULL, '112', 1, '2026-01-31 11:25:17');
INSERT INTO `sys_chat_group_message` VALUES (9, 1, 2, 'test', NULL, '哈哈哈', 1, '2026-01-31 11:25:21');
INSERT INTO `sys_chat_group_message` VALUES (10, 1, 2, 'test', NULL, '牛逼', 1, '2026-01-31 11:25:24');
INSERT INTO `sys_chat_group_message` VALUES (11, 1, 1, '超级管理员', NULL, '老牛逼', 1, '2026-01-31 11:25:27');
INSERT INTO `sys_chat_group_message` VALUES (12, 1, 2, 'test', NULL, '你是', 1, '2026-01-31 11:25:30');
INSERT INTO `sys_chat_group_message` VALUES (13, 1, 1, '超级管理员', NULL, '？？？', 1, '2026-01-31 11:25:34');
INSERT INTO `sys_chat_group_message` VALUES (14, 1, 1, '超级管理员', NULL, '傻逼的吧', 1, '2026-01-31 11:25:37');
INSERT INTO `sys_chat_group_message` VALUES (15, 1, 1, '超级管理员', NULL, '111', 1, '2026-01-31 12:26:07');
INSERT INTO `sys_chat_group_message` VALUES (16, 1, 1, '超级管理员', NULL, '1', 1, '2026-01-31 12:26:08');
INSERT INTO `sys_chat_group_message` VALUES (17, 1, 1, '超级管理员', NULL, '1', 1, '2026-01-31 12:26:08');
INSERT INTO `sys_chat_group_message` VALUES (18, 1, 1, '超级管理员', NULL, '1', 1, '2026-01-31 12:26:08');
INSERT INTO `sys_chat_group_message` VALUES (19, 1, 2, 'test', NULL, '121212', 1, '2026-01-31 12:26:13');
INSERT INTO `sys_chat_group_message` VALUES (20, 1, 1, '超级管理员', NULL, '222', 1, '2026-01-31 12:26:15');
INSERT INTO `sys_chat_group_message` VALUES (21, 1, 2, 'test', NULL, '牛逼啊', 1, '2026-01-31 12:26:19');
INSERT INTO `sys_chat_group_message` VALUES (22, 1, 1, '超级管理员', NULL, '111', 1, '2026-01-31 12:26:21');
INSERT INTO `sys_chat_group_message` VALUES (23, 1, 1, '超级管理员', NULL, 'http://localhost:8080/file/images/2026/01/31/1b4f6f9062ab400f9259bdac0eb8db10.jpg', 2, '2026-01-31 12:26:52');
INSERT INTO `sys_chat_group_message` VALUES (24, 1, 2, 'test', NULL, '111', 1, '2026-01-31 12:26:58');
INSERT INTO `sys_chat_group_message` VALUES (25, 1, 1, '超级管理员', NULL, 'http://localhost:8080/file/images/2026/01/31/788f706ff0a548c0a4b744de208e73a5.jpg', 2, '2026-01-31 12:27:03');
INSERT INTO `sys_chat_group_message` VALUES (26, 1, 1, '超级管理员', NULL, '😊', 1, '2026-01-31 12:29:07');
INSERT INTO `sys_chat_group_message` VALUES (27, 1, 2, 'test', NULL, '22', 1, '2026-01-31 12:29:17');
INSERT INTO `sys_chat_group_message` VALUES (28, 1, 2, 'test', NULL, '22', 1, '2026-01-31 12:29:24');
INSERT INTO `sys_chat_group_message` VALUES (29, 1, 1, '超级管理员', NULL, '22222', 1, '2026-01-31 12:38:58');
INSERT INTO `sys_chat_group_message` VALUES (30, 1, 2, 'test', NULL, '哈哈哈', 1, '2026-01-31 12:39:05');
INSERT INTO `sys_chat_group_message` VALUES (31, 1, 1, '超级管理员', NULL, '牛逼啊', 1, '2026-01-31 12:39:10');
INSERT INTO `sys_chat_group_message` VALUES (32, 1, 2, 'test', NULL, '111', 1, '2026-01-31 12:39:26');
INSERT INTO `sys_chat_group_message` VALUES (33, 2, 0, '系统消息', NULL, '超级管理员 创建了群聊', 4, '2026-01-31 12:42:48');
INSERT INTO `sys_chat_group_message` VALUES (34, 2, 1, '超级管理员', NULL, '1111', 1, '2026-01-31 12:42:51');
INSERT INTO `sys_chat_group_message` VALUES (35, 2, 2, 'test', NULL, '牛逼啊', 1, '2026-01-31 12:43:00');
INSERT INTO `sys_chat_group_message` VALUES (36, 2, 1, '超级管理员', NULL, '111', 1, '2026-01-31 12:43:03');
INSERT INTO `sys_chat_group_message` VALUES (37, 2, 2, 'test', NULL, '12', 1, '2026-01-31 12:43:04');
INSERT INTO `sys_chat_group_message` VALUES (38, 2, 2, 'test', NULL, '2', 1, '2026-01-31 12:43:05');
INSERT INTO `sys_chat_group_message` VALUES (39, 2, 2, 'test', NULL, '1', 1, '2026-01-31 12:43:05');
INSERT INTO `sys_chat_group_message` VALUES (40, 2, 2, 'test', NULL, '21', 1, '2026-01-31 12:43:05');
INSERT INTO `sys_chat_group_message` VALUES (41, 2, 2, 'test', NULL, '2', 1, '2026-01-31 12:43:05');
INSERT INTO `sys_chat_group_message` VALUES (42, 2, 2, 'test', NULL, '12', 1, '2026-01-31 12:43:05');
INSERT INTO `sys_chat_group_message` VALUES (43, 2, 1, '超级管理员', NULL, '12', 1, '2026-01-31 12:43:07');
INSERT INTO `sys_chat_group_message` VALUES (44, 2, 1, '超级管理员', NULL, '1', 1, '2026-01-31 12:43:07');
INSERT INTO `sys_chat_group_message` VALUES (45, 2, 1, '超级管理员', NULL, '3', 1, '2026-01-31 12:43:07');
INSERT INTO `sys_chat_group_message` VALUES (46, 2, 1, '超级管理员', NULL, '2', 1, '2026-01-31 12:43:07');
INSERT INTO `sys_chat_group_message` VALUES (47, 2, 1, '超级管理员', NULL, '1', 1, '2026-01-31 12:43:07');
INSERT INTO `sys_chat_group_message` VALUES (48, 2, 1, '超级管理员', NULL, '2', 1, '2026-01-31 12:43:08');
INSERT INTO `sys_chat_group_message` VALUES (49, 2, 1, '超级管理员', NULL, '12', 1, '2026-01-31 12:43:08');
INSERT INTO `sys_chat_group_message` VALUES (50, 2, 2, 'test', NULL, '1', 1, '2026-01-31 12:43:09');
INSERT INTO `sys_chat_group_message` VALUES (51, 2, 2, 'test', NULL, '21', 1, '2026-01-31 12:43:09');
INSERT INTO `sys_chat_group_message` VALUES (52, 2, 2, 'test', NULL, '2', 1, '2026-01-31 12:43:09');
INSERT INTO `sys_chat_group_message` VALUES (53, 2, 2, 'test', NULL, '1', 1, '2026-01-31 12:43:09');
INSERT INTO `sys_chat_group_message` VALUES (54, 2, 2, 'test', NULL, '3', 1, '2026-01-31 12:43:10');
INSERT INTO `sys_chat_group_message` VALUES (55, 2, 2, 'test', NULL, '12', 1, '2026-01-31 12:43:10');
INSERT INTO `sys_chat_group_message` VALUES (56, 2, 1, '超级管理员', NULL, '可以的', 1, '2026-01-31 12:43:29');
INSERT INTO `sys_chat_group_message` VALUES (57, 2, 2, 'test', NULL, '好用的额', 1, '2026-01-31 12:43:32');
INSERT INTO `sys_chat_group_message` VALUES (58, 2, 1, '超级管理员', NULL, '111', 1, '2026-01-31 12:44:53');
INSERT INTO `sys_chat_group_message` VALUES (59, 2, 1, '超级管理员', NULL, '111', 1, '2026-01-31 12:45:00');
INSERT INTO `sys_chat_group_message` VALUES (60, 2, 1, '超级管理员', NULL, '222', 1, '2026-01-31 12:45:13');
INSERT INTO `sys_chat_group_message` VALUES (61, 2, 2, 'test', NULL, '111', 1, '2026-01-31 12:51:12');
INSERT INTO `sys_chat_group_message` VALUES (62, 2, 1, '超级管理员', NULL, '1111111', 1, '2026-01-31 12:51:16');
INSERT INTO `sys_chat_group_message` VALUES (63, 2, 2, 'test', NULL, '66666666', 1, '2026-01-31 12:51:20');
INSERT INTO `sys_chat_group_message` VALUES (64, 2, 1, '超级管理员', NULL, '111', 1, '2026-01-31 12:52:08');
INSERT INTO `sys_chat_group_message` VALUES (65, 2, 1, '超级管理员', NULL, '212', 1, '2026-01-31 12:52:09');
INSERT INTO `sys_chat_group_message` VALUES (66, 2, 1, '超级管理员', NULL, '2', 1, '2026-01-31 12:52:09');
INSERT INTO `sys_chat_group_message` VALUES (67, 2, 1, '超级管理员', NULL, '1', 1, '2026-01-31 12:52:09');
INSERT INTO `sys_chat_group_message` VALUES (68, 2, 1, '超级管理员', NULL, '21', 1, '2026-01-31 12:52:10');
INSERT INTO `sys_chat_group_message` VALUES (69, 2, 1, '超级管理员', NULL, '2', 1, '2026-01-31 12:52:10');
INSERT INTO `sys_chat_group_message` VALUES (70, 2, 1, '超级管理员', NULL, '1111111111', 1, '2026-01-31 12:52:26');
INSERT INTO `sys_chat_group_message` VALUES (71, 2, 1, '超级管理员', NULL, '222', 1, '2026-01-31 12:52:30');
INSERT INTO `sys_chat_group_message` VALUES (72, 2, 1, '超级管理员', NULL, '111111111', 1, '2026-01-31 12:53:21');
INSERT INTO `sys_chat_group_message` VALUES (73, 2, 1, '超级管理员', NULL, '1212', 1, '2026-01-31 12:53:23');
INSERT INTO `sys_chat_group_message` VALUES (74, 2, 1, '超级管理员', NULL, '1', 1, '2026-01-31 12:53:23');
INSERT INTO `sys_chat_group_message` VALUES (75, 2, 1, '超级管理员', NULL, '2', 1, '2026-01-31 12:53:24');
INSERT INTO `sys_chat_group_message` VALUES (76, 2, 1, '超级管理员', NULL, '12', 1, '2026-01-31 12:53:24');
INSERT INTO `sys_chat_group_message` VALUES (77, 2, 1, '超级管理员', NULL, '1', 1, '2026-01-31 12:53:24');
INSERT INTO `sys_chat_group_message` VALUES (78, 2, 1, '超级管理员', NULL, '2', 1, '2026-01-31 12:53:25');
INSERT INTO `sys_chat_group_message` VALUES (79, 2, 1, '超级管理员', NULL, '12', 1, '2026-01-31 12:53:25');
INSERT INTO `sys_chat_group_message` VALUES (80, 2, 1, '超级管理员', NULL, '12', 1, '2026-01-31 12:53:26');
INSERT INTO `sys_chat_group_message` VALUES (81, 2, 1, '超级管理员', NULL, '1', 1, '2026-01-31 12:53:26');
INSERT INTO `sys_chat_group_message` VALUES (82, 2, 1, '超级管理员', NULL, '2', 1, '2026-01-31 12:53:26');
INSERT INTO `sys_chat_group_message` VALUES (83, 2, 1, '超级管理员', NULL, '1', 1, '2026-01-31 12:53:26');
INSERT INTO `sys_chat_group_message` VALUES (84, 2, 1, '超级管理员', NULL, '21', 1, '2026-01-31 12:53:26');
INSERT INTO `sys_chat_group_message` VALUES (85, 2, 1, '超级管理员', NULL, '2', 1, '2026-01-31 12:53:26');
INSERT INTO `sys_chat_group_message` VALUES (86, 2, 1, '超级管理员', NULL, '1', 1, '2026-01-31 12:53:27');
INSERT INTO `sys_chat_group_message` VALUES (87, 2, 1, '超级管理员', NULL, '2', 1, '2026-01-31 12:53:27');
INSERT INTO `sys_chat_group_message` VALUES (88, 2, 1, '超级管理员', NULL, '1', 1, '2026-01-31 12:53:27');
INSERT INTO `sys_chat_group_message` VALUES (89, 2, 1, '超级管理员', NULL, '2', 1, '2026-01-31 12:53:27');
INSERT INTO `sys_chat_group_message` VALUES (90, 2, 1, '超级管理员', NULL, '12', 1, '2026-01-31 12:53:27');
INSERT INTO `sys_chat_group_message` VALUES (91, 2, 2, 'test', NULL, '1212', 1, '2026-01-31 12:53:30');
INSERT INTO `sys_chat_group_message` VALUES (92, 2, 2, 'test', NULL, '12', 1, '2026-01-31 12:53:31');
INSERT INTO `sys_chat_group_message` VALUES (93, 2, 2, 'test', NULL, '12', 1, '2026-01-31 12:53:31');
INSERT INTO `sys_chat_group_message` VALUES (94, 2, 2, 'test', NULL, '12', 1, '2026-01-31 12:53:31');
INSERT INTO `sys_chat_group_message` VALUES (95, 2, 3, 'mars', NULL, '111', 1, '2026-01-31 13:54:46');
INSERT INTO `sys_chat_group_message` VALUES (96, 2, 3, 'mars', NULL, '11', 1, '2026-01-31 13:54:48');
INSERT INTO `sys_chat_group_message` VALUES (97, 2, 3, 'mars', NULL, '12', 1, '2026-01-31 13:54:49');
INSERT INTO `sys_chat_group_message` VALUES (98, 2, 3, 'mars', NULL, '12', 1, '2026-01-31 13:54:49');
INSERT INTO `sys_chat_group_message` VALUES (99, 2, 3, 'mars', NULL, '1', 1, '2026-01-31 13:54:49');
INSERT INTO `sys_chat_group_message` VALUES (100, 3, 0, '系统消息', NULL, '超级管理员 创建了群聊', 4, '2026-01-31 23:20:06');
INSERT INTO `sys_chat_group_message` VALUES (101, 3, 1, '超级管理员', NULL, '1111', 1, '2026-01-31 23:20:08');
INSERT INTO `sys_chat_group_message` VALUES (102, 3, 4, 'lisi', NULL, 'hahah', 1, '2026-01-31 23:20:20');
INSERT INTO `sys_chat_group_message` VALUES (103, 3, 4, 'lisi', NULL, '111', 1, '2026-01-31 23:20:28');
INSERT INTO `sys_chat_group_message` VALUES (104, 3, 1, '超级管理员', NULL, '牛逼的', 1, '2026-01-31 23:20:34');
INSERT INTO `sys_chat_group_message` VALUES (105, 3, 4, 'lisi', NULL, '11212', 1, '2026-01-31 23:20:37');
INSERT INTO `sys_chat_group_message` VALUES (106, 3, 4, 'lisi', NULL, '1', 1, '2026-01-31 23:20:37');
INSERT INTO `sys_chat_group_message` VALUES (107, 3, 4, 'lisi', NULL, '12', 1, '2026-01-31 23:20:37');
INSERT INTO `sys_chat_group_message` VALUES (108, 3, 4, 'lisi', NULL, '1', 1, '2026-01-31 23:20:37');

-- ----------------------------
-- Table structure for sys_chat_message
-- ----------------------------
DROP TABLE IF EXISTS `sys_chat_message`;
CREATE TABLE `sys_chat_message`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `sender_id` bigint NOT NULL COMMENT '发送者ID',
  `sender_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发送者名称',
  `sender_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发送者头像',
  `receiver_id` bigint NOT NULL COMMENT '接收者ID(0表示群发)',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '消息内容',
  `msg_type` tinyint NULL DEFAULT 1 COMMENT '消息类型(1文本 2图片 3文件)',
  `is_read` tinyint NULL DEFAULT 0 COMMENT '是否已读(0未读 1已读)',
  `send_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sender_id`(`sender_id` ASC) USING BTREE,
  INDEX `idx_receiver_id`(`receiver_id` ASC) USING BTREE,
  INDEX `idx_send_time`(`send_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 351 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '聊天消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_chat_message
-- ----------------------------
INSERT INTO `sys_chat_message` VALUES (137, 1, '超级管理员', NULL, 3, '111', 1, 1, '2026-01-31 11:09:21');
INSERT INTO `sys_chat_message` VALUES (216, 1, '超级管理员', NULL, 2, '111', 1, 1, '2026-01-31 12:50:28');
INSERT INTO `sys_chat_message` VALUES (217, 2, 'test', NULL, 1, '1111', 1, 1, '2026-01-31 12:50:35');
INSERT INTO `sys_chat_message` VALUES (218, 2, 'test', NULL, 1, '222', 1, 1, '2026-01-31 12:50:39');
INSERT INTO `sys_chat_message` VALUES (219, 1, '超级管理员', NULL, 2, '1111', 1, 1, '2026-01-31 12:50:41');
INSERT INTO `sys_chat_message` VALUES (220, 1, '超级管理员', NULL, 2, '222', 1, 1, '2026-01-31 12:50:46');
INSERT INTO `sys_chat_message` VALUES (221, 1, '超级管理员', NULL, 2, '222', 1, 1, '2026-01-31 12:51:07');
INSERT INTO `sys_chat_message` VALUES (222, 2, 'test', NULL, 1, '2222222222', 1, 1, '2026-01-31 12:53:37');
INSERT INTO `sys_chat_message` VALUES (223, 1, '超级管理员', NULL, 2, '哈哈哈', 1, 1, '2026-01-31 12:53:44');
INSERT INTO `sys_chat_message` VALUES (224, 1, '超级管理员', NULL, 2, '牛逼的', 1, 1, '2026-01-31 12:53:45');
INSERT INTO `sys_chat_message` VALUES (225, 2, 'test', NULL, 1, '哈哈哈', 1, 1, '2026-01-31 12:54:24');
INSERT INTO `sys_chat_message` VALUES (226, 2, 'test', NULL, 1, '牛逼的额啊', 1, 1, '2026-01-31 12:54:26');
INSERT INTO `sys_chat_message` VALUES (227, 1, '超级管理员', NULL, 2, '11', 1, 1, '2026-01-31 12:55:04');
INSERT INTO `sys_chat_message` VALUES (228, 2, 'test', NULL, 1, '111', 1, 1, '2026-01-31 13:48:21');
INSERT INTO `sys_chat_message` VALUES (229, 1, '超级管理员', NULL, 2, '1212', 1, 1, '2026-01-31 13:48:25');
INSERT INTO `sys_chat_message` VALUES (230, 1, '超级管理员', NULL, 2, '1212', 1, 1, '2026-01-31 13:48:37');
INSERT INTO `sys_chat_message` VALUES (231, 2, 'test', NULL, 1, '222', 1, 1, '2026-01-31 13:49:04');
INSERT INTO `sys_chat_message` VALUES (232, 1, '超级管理员', NULL, 2, '1111111', 1, 1, '2026-01-31 13:49:07');
INSERT INTO `sys_chat_message` VALUES (233, 2, 'test', NULL, 1, '121', 1, 1, '2026-01-31 13:49:08');
INSERT INTO `sys_chat_message` VALUES (234, 1, '超级管理员', NULL, 2, '1111111', 1, 1, '2026-01-31 13:49:11');
INSERT INTO `sys_chat_message` VALUES (235, 2, 'test', NULL, 1, '222', 1, 1, '2026-01-31 13:49:12');
INSERT INTO `sys_chat_message` VALUES (236, 1, '超级管理员', NULL, 2, '222222222222', 1, 1, '2026-01-31 13:49:14');
INSERT INTO `sys_chat_message` VALUES (237, 2, 'test', NULL, 1, '11111', 1, 1, '2026-01-31 13:52:30');
INSERT INTO `sys_chat_message` VALUES (238, 1, '超级管理员', NULL, 2, '111', 1, 1, '2026-01-31 13:52:37');
INSERT INTO `sys_chat_message` VALUES (239, 1, '超级管理员', NULL, 2, '22222222222', 1, 1, '2026-01-31 13:52:52');
INSERT INTO `sys_chat_message` VALUES (240, 2, 'test', NULL, 1, '牛逼的', 1, 1, '2026-01-31 13:52:55');
INSERT INTO `sys_chat_message` VALUES (241, 1, '超级管理员', NULL, 2, '哈哈哈', 1, 1, '2026-01-31 13:52:58');
INSERT INTO `sys_chat_message` VALUES (242, 3, 'mars', NULL, 1, '222', 1, 1, '2026-01-31 13:54:24');
INSERT INTO `sys_chat_message` VALUES (243, 3, 'mars', NULL, 2, '1111', 1, 1, '2026-01-31 13:54:27');
INSERT INTO `sys_chat_message` VALUES (244, 1, '超级管理员', NULL, 3, 'nihao', 1, 1, '2026-01-31 13:54:35');
INSERT INTO `sys_chat_message` VALUES (245, 1, '超级管理员', NULL, 3, '11', 1, 1, '2026-01-31 13:55:37');
INSERT INTO `sys_chat_message` VALUES (246, 3, 'mars', NULL, 1, '1212', 1, 1, '2026-01-31 13:55:45');
INSERT INTO `sys_chat_message` VALUES (247, 1, '超级管理员', NULL, 2, '111', 1, 1, '2026-01-31 13:57:32');
INSERT INTO `sys_chat_message` VALUES (248, 1, '超级管理员', NULL, 2, '111', 1, 1, '2026-01-31 13:57:41');
INSERT INTO `sys_chat_message` VALUES (249, 2, 'test', NULL, 1, '1212', 1, 1, '2026-01-31 13:57:52');
INSERT INTO `sys_chat_message` VALUES (250, 1, '超级管理员', NULL, 2, '2222', 1, 1, '2026-01-31 13:57:55');
INSERT INTO `sys_chat_message` VALUES (251, 2, 'test', NULL, 1, '222', 1, 1, '2026-01-31 14:00:09');
INSERT INTO `sys_chat_message` VALUES (252, 2, 'test', NULL, 1, '1212', 1, 1, '2026-01-31 14:00:18');
INSERT INTO `sys_chat_message` VALUES (253, 2, 'test', NULL, 1, '2222222', 1, 1, '2026-01-31 14:00:21');
INSERT INTO `sys_chat_message` VALUES (254, 2, 'test', NULL, 1, '111111111111111111111', 1, 1, '2026-01-31 14:00:25');
INSERT INTO `sys_chat_message` VALUES (255, 1, '超级管理员', NULL, 2, '22222222222', 1, 1, '2026-01-31 14:00:30');
INSERT INTO `sys_chat_message` VALUES (256, 1, '超级管理员', NULL, 2, '1111111111111', 1, 0, '2026-01-31 14:00:38');
INSERT INTO `sys_chat_message` VALUES (257, 1, '超级管理员', NULL, 2, '1212', 1, 0, '2026-01-31 14:00:41');
INSERT INTO `sys_chat_message` VALUES (258, 1, '超级管理员', NULL, 2, '121312', 1, 0, '2026-01-31 14:00:43');
INSERT INTO `sys_chat_message` VALUES (259, 1, '超级管理员', NULL, 2, '121212', 1, 0, '2026-01-31 14:00:47');
INSERT INTO `sys_chat_message` VALUES (260, 1, '超级管理员', NULL, 2, '121', 1, 0, '2026-01-31 14:00:47');
INSERT INTO `sys_chat_message` VALUES (261, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:47');
INSERT INTO `sys_chat_message` VALUES (262, 1, '超级管理员', NULL, 2, '12', 1, 0, '2026-01-31 14:00:48');
INSERT INTO `sys_chat_message` VALUES (263, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:48');
INSERT INTO `sys_chat_message` VALUES (264, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:48');
INSERT INTO `sys_chat_message` VALUES (265, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:48');
INSERT INTO `sys_chat_message` VALUES (266, 1, '超级管理员', NULL, 2, '21', 1, 0, '2026-01-31 14:00:48');
INSERT INTO `sys_chat_message` VALUES (267, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:49');
INSERT INTO `sys_chat_message` VALUES (268, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:49');
INSERT INTO `sys_chat_message` VALUES (269, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:49');
INSERT INTO `sys_chat_message` VALUES (270, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:49');
INSERT INTO `sys_chat_message` VALUES (271, 1, '超级管理员', NULL, 2, '21', 1, 0, '2026-01-31 14:00:49');
INSERT INTO `sys_chat_message` VALUES (272, 1, '超级管理员', NULL, 2, '3', 1, 0, '2026-01-31 14:00:49');
INSERT INTO `sys_chat_message` VALUES (273, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:50');
INSERT INTO `sys_chat_message` VALUES (274, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:50');
INSERT INTO `sys_chat_message` VALUES (275, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:50');
INSERT INTO `sys_chat_message` VALUES (276, 1, '超级管理员', NULL, 2, '21', 1, 0, '2026-01-31 14:00:50');
INSERT INTO `sys_chat_message` VALUES (277, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:50');
INSERT INTO `sys_chat_message` VALUES (278, 1, '超级管理员', NULL, 2, '12', 1, 0, '2026-01-31 14:00:51');
INSERT INTO `sys_chat_message` VALUES (279, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:51');
INSERT INTO `sys_chat_message` VALUES (280, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:51');
INSERT INTO `sys_chat_message` VALUES (281, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:51');
INSERT INTO `sys_chat_message` VALUES (282, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:51');
INSERT INTO `sys_chat_message` VALUES (283, 1, '超级管理员', NULL, 2, '3', 1, 0, '2026-01-31 14:00:52');
INSERT INTO `sys_chat_message` VALUES (284, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:52');
INSERT INTO `sys_chat_message` VALUES (285, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:52');
INSERT INTO `sys_chat_message` VALUES (286, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:52');
INSERT INTO `sys_chat_message` VALUES (287, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:52');
INSERT INTO `sys_chat_message` VALUES (288, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:52');
INSERT INTO `sys_chat_message` VALUES (289, 1, '超级管理员', NULL, 2, '121', 1, 0, '2026-01-31 14:00:53');
INSERT INTO `sys_chat_message` VALUES (290, 1, '超级管理员', NULL, 2, '3', 1, 0, '2026-01-31 14:00:53');
INSERT INTO `sys_chat_message` VALUES (291, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:53');
INSERT INTO `sys_chat_message` VALUES (292, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:53');
INSERT INTO `sys_chat_message` VALUES (293, 1, '超级管理员', NULL, 2, '12', 1, 0, '2026-01-31 14:00:54');
INSERT INTO `sys_chat_message` VALUES (294, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:54');
INSERT INTO `sys_chat_message` VALUES (295, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:54');
INSERT INTO `sys_chat_message` VALUES (296, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:54');
INSERT INTO `sys_chat_message` VALUES (297, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:54');
INSERT INTO `sys_chat_message` VALUES (298, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:55');
INSERT INTO `sys_chat_message` VALUES (299, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:55');
INSERT INTO `sys_chat_message` VALUES (300, 1, '超级管理员', NULL, 2, '21', 1, 0, '2026-01-31 14:00:55');
INSERT INTO `sys_chat_message` VALUES (301, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:55');
INSERT INTO `sys_chat_message` VALUES (302, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:55');
INSERT INTO `sys_chat_message` VALUES (303, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:55');
INSERT INTO `sys_chat_message` VALUES (304, 1, '超级管理员', NULL, 2, '13', 1, 0, '2026-01-31 14:00:56');
INSERT INTO `sys_chat_message` VALUES (305, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:56');
INSERT INTO `sys_chat_message` VALUES (306, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:56');
INSERT INTO `sys_chat_message` VALUES (307, 1, '超级管理员', NULL, 2, '12', 1, 0, '2026-01-31 14:00:56');
INSERT INTO `sys_chat_message` VALUES (308, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:56');
INSERT INTO `sys_chat_message` VALUES (309, 1, '超级管理员', NULL, 2, '12', 1, 0, '2026-01-31 14:00:57');
INSERT INTO `sys_chat_message` VALUES (310, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:57');
INSERT INTO `sys_chat_message` VALUES (311, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:57');
INSERT INTO `sys_chat_message` VALUES (312, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:57');
INSERT INTO `sys_chat_message` VALUES (313, 1, '超级管理员', NULL, 2, '12', 1, 0, '2026-01-31 14:00:58');
INSERT INTO `sys_chat_message` VALUES (314, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:58');
INSERT INTO `sys_chat_message` VALUES (315, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:58');
INSERT INTO `sys_chat_message` VALUES (316, 1, '超级管理员', NULL, 2, '12', 1, 0, '2026-01-31 14:00:58');
INSERT INTO `sys_chat_message` VALUES (317, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:59');
INSERT INTO `sys_chat_message` VALUES (318, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:59');
INSERT INTO `sys_chat_message` VALUES (319, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:00:59');
INSERT INTO `sys_chat_message` VALUES (320, 1, '超级管理员', NULL, 2, '31', 1, 0, '2026-01-31 14:00:59');
INSERT INTO `sys_chat_message` VALUES (321, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:00:59');
INSERT INTO `sys_chat_message` VALUES (322, 1, '超级管理员', NULL, 2, '12', 1, 0, '2026-01-31 14:01:00');
INSERT INTO `sys_chat_message` VALUES (323, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:01:00');
INSERT INTO `sys_chat_message` VALUES (324, 1, '超级管理员', NULL, 2, '3', 1, 0, '2026-01-31 14:01:00');
INSERT INTO `sys_chat_message` VALUES (325, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:01:00');
INSERT INTO `sys_chat_message` VALUES (326, 1, '超级管理员', NULL, 2, '21', 1, 0, '2026-01-31 14:01:00');
INSERT INTO `sys_chat_message` VALUES (327, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:01:01');
INSERT INTO `sys_chat_message` VALUES (328, 1, '超级管理员', NULL, 2, '12', 1, 0, '2026-01-31 14:01:03');
INSERT INTO `sys_chat_message` VALUES (329, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:01:03');
INSERT INTO `sys_chat_message` VALUES (330, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:01:04');
INSERT INTO `sys_chat_message` VALUES (331, 1, '超级管理员', NULL, 2, '2', 1, 0, '2026-01-31 14:01:04');
INSERT INTO `sys_chat_message` VALUES (332, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:01:04');
INSERT INTO `sys_chat_message` VALUES (333, 1, '超级管理员', NULL, 2, '1', 1, 0, '2026-01-31 14:01:04');
INSERT INTO `sys_chat_message` VALUES (334, 1, '超级管理员', NULL, 2, '3', 1, 0, '2026-01-31 14:01:04');
INSERT INTO `sys_chat_message` VALUES (335, 1, '超级管理员', NULL, 2, '12', 1, 0, '2026-01-31 14:01:05');
INSERT INTO `sys_chat_message` VALUES (336, 1, '超级管理员', NULL, 2, '121', 1, 0, '2026-01-31 14:01:05');
INSERT INTO `sys_chat_message` VALUES (337, 1, '超级管理员', NULL, 2, '111', 1, 0, '2026-01-31 14:08:18');
INSERT INTO `sys_chat_message` VALUES (338, 1, '超级管理员', NULL, 2, '1212', 1, 0, '2026-01-31 14:24:15');
INSERT INTO `sys_chat_message` VALUES (339, 1, '超级管理员', NULL, 2, '1212', 1, 0, '2026-01-31 14:25:30');
INSERT INTO `sys_chat_message` VALUES (340, 1, '超级管理员', NULL, 3, '111', 1, 1, '2026-01-31 14:31:43');
INSERT INTO `sys_chat_message` VALUES (341, 3, 'mars', NULL, 1, '222', 1, 1, '2026-01-31 14:31:48');
INSERT INTO `sys_chat_message` VALUES (342, 1, '超级管理员', NULL, 2, '你好', 1, 0, '2026-01-31 20:48:31');
INSERT INTO `sys_chat_message` VALUES (343, 1, '超级管理员', NULL, 4, '11', 1, 1, '2026-01-31 21:37:40');
INSERT INTO `sys_chat_message` VALUES (344, 4, 'lisi', NULL, 1, 'nihao', 1, 1, '2026-01-31 21:37:46');
INSERT INTO `sys_chat_message` VALUES (345, 5, 'mars666', NULL, 2, '1212', 1, 0, '2026-01-31 22:31:32');
INSERT INTO `sys_chat_message` VALUES (346, 5, 'mars666', NULL, 1, '11', 1, 1, '2026-01-31 22:32:42');
INSERT INTO `sys_chat_message` VALUES (347, 1, '超级管理员', NULL, 4, '111', 1, 0, '2026-01-31 22:54:12');
INSERT INTO `sys_chat_message` VALUES (348, 1, '超级管理员', NULL, 4, '111', 1, 0, '2026-01-31 23:19:38');
INSERT INTO `sys_chat_message` VALUES (349, 1, '超级管理员', NULL, 4, '稍等，我确认一下', 1, 0, '2026-01-31 23:19:48');
INSERT INTO `sys_chat_message` VALUES (350, 1, '超级管理员', NULL, 4, '感谢你的反馈', 1, 0, '2026-01-31 23:19:52');

-- ----------------------------
-- Table structure for sys_config_group
-- ----------------------------
DROP TABLE IF EXISTS `sys_config_group`;
CREATE TABLE `sys_config_group`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分组编码',
  `group_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分组名称',
  `group_icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分组图标',
  `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '配置值(JSON格式)',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(0-禁用 1-启用)',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_code`(`group_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统配置分组表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config_group
-- ----------------------------
INSERT INTO `sys_config_group` VALUES (1, 'system', '系统配置', NULL, '{\"siteName\":\"Mars Admin\",\"siteDescription\":\"现代化企业级管理系统\",\"siteLogo\":\"\",\"copyright\":\"版权所有 © 成都火星网络科技有限公司 2025-2030\",\"icp\":\"\",\"watermarkEnabled\":true,\"watermarkType\":\"sitename\",\"watermarkOpacity\":0.09}', 1, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 14:38:29');
INSERT INTO `sys_config_group` VALUES (2, 'register', '注册配置', NULL, '{\"enabled\":true,\"verifyEmail\":false,\"verifyPhone\":false,\"defaultRole\":\"user\",\"needAudit\":false}', 2, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 14:38:29');
INSERT INTO `sys_config_group` VALUES (3, 'login', '登录配置', NULL, '{\"captchaEnabled\":true,\"captchaType\":\"image\",\"maxRetryCount\":5,\"lockTime\":30,\"rememberMe\":true,\"singleLogin\":false}', 3, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 15:38:39');
INSERT INTO `sys_config_group` VALUES (4, 'password', '密码配置', NULL, '{\"minLength\":6,\"maxLength\":20,\"requireUppercase\":true,\"requireLowercase\":true,\"requireNumber\":true,\"requireSpecial\":true,\"expireDays\":0}', 4, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 14:38:29');
INSERT INTO `sys_config_group` VALUES (5, 'email', '邮件配置', NULL, '{\"host\":\"smtp.qq.com\",\"port\":465,\"username\":\"850994281@qq.com\",\"password\":\"pbfbulghhkqmbedj\",\"fromName\":\"Mars管理系统\",\"ssl\":true,\"enabled\":true}', 5, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 22:13:14');
INSERT INTO `sys_config_group` VALUES (6, 'emailTemplate', '邮件模板', NULL, '{\"verifyCode\":\"您的验证码是：{code}，有效期{expire}分钟。\",\"resetPassword\":\"您正在重置密码，验证码：{code}，有效期{expire}分钟。\",\"welcome\":\"欢迎注册{siteName}，您的账号已创建成功。\"}', 6, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 14:38:29');
INSERT INTO `sys_config_group` VALUES (7, 'sms', '短信配置', NULL, '{\"provider\":\"aliyun\",\"accessKeyId\":\"\",\"accessKeySecret\":\"\",\"signName\":\"\",\"enabled\":false}', 7, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 14:38:29');
INSERT INTO `sys_config_group` VALUES (9, 'storage', '文件配置', NULL, '{\r\n  \"provider\": \"local\",\r\n  \"domain\": \"http://localhost:8080\",\r\n  \"localPath\": \"./uploads\",\r\n  \"maxSize\": 10,\r\n  \"allowTypes\": \"jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx\",\r\n  \"minioEndpoint\": \"\",\r\n  \"minioAccessKey\": \"\",\r\n  \"minioSecretKey\": \"\",\r\n  \"minioBucket\": \"\",\r\n  \"aliyunEndpoint\": \"\",\r\n  \"aliyunAccessKey\": \"\",\r\n  \"aliyunSecretKey\": \"\",\r\n  \"aliyunBucket\": \"\",\r\n  \"tencentSecretId\": \"\",\r\n  \"tencentSecretKey\": \"\",\r\n  \"tencentBucket\": \"\",\r\n  \"tencentRegion\": \"\"\r\n}', 9, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 16:26:34');
INSERT INTO `sys_config_group` VALUES (10, 'push', '推送配置', NULL, '{\r\n  \"enabled\": false,\r\n  \"provider\": \"console\",\r\n  \"appKey\": \"\",\r\n  \"masterSecret\": \"\"\r\n}', 10, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 16:26:34');
INSERT INTO `sys_config_group` VALUES (11, 'thirdParty', '第三方配置', NULL, '{\"wechat\":{\"enabled\":false,\"appId\":\"\",\"appSecret\":\"\"},\"alipay\":{\"enabled\":false,\"appId\":\"\",\"privateKey\":\"\",\"publicKey\":\"\"},\"github\":{\"enabled\":false,\"clientId\":\"\",\"clientSecret\":\"\"}}', 11, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 14:38:29');
INSERT INTO `sys_config_group` VALUES (12, 'payment', '支付配置', NULL, '{\r\n    \"wechatPay\": {\r\n        \"enabled\": true,\r\n        \"mchId\": \"1627500294\",\r\n        \"appId\": \"wxe97894ad8c7ef7e0\",\r\n        \"apiKey\": \"\",\r\n        \"apiV3Key\": \"lxpvkwojpnxafnoutgqowbecdwdsmpwq\",\r\n        \"privateKey\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDlGsA4SciJOYYq\\nTL+/hYlaRLkkJ060c+2MrOl7egozzwddhNLHRC0wasgGdQdbDI39mAm34I7mLdMV\\nlv10dgtKXgpQBHc9QPKy3bPFcgFrz7rxS0YcFrqmzzB69a0LVVfAsZE2SD/4yKc3\\nVFW8cLKQZKRYYm3gZGwN0rsJFVU3dfWgOaoNlBkc5bNIbY7j4aHeW7tJXOQCiig6\\nKj+Dh7r1/POzTciCfqVB1Vjf+VkFMuF6oyKLxMBzFzxvXCGw3PySL6HuY1g5xI7j\\nbNi+xfqtzxZEQAv1QjbfBjzygQXeLCpsuYGVFRRVdyNYxkV90FDVI8swLXpMh65b\\nYNgBGtn1AgMBAAECggEBAKlIx+mPk07aI2mUBkcU+7WofAjbxosN8eP1TBxBw9Ie\\nUnnmj/xPQvi4ng4vYP0E3NIaCmxE0DICgCs+ww7Pvm336LTRZ+3p1KsXqCLnp2cr\\nOh3bGfXdUZO6Gj9w0qlCKTInwn2SizpfwTbf6O3xc++/fbQVHs0kRrc8E5mVmr77\\n01aGIJvXxtQPfdn/R2TMBwqiN8pO5igILlDzNAEusXnfSDOp3rYsXwcnCxJqgnVm\\nydlo7JMU2iqRKSD09qeKFgb+Hbr9aJIQdcvjGBSNmF3MsCFgs/XIb47B4xvy2HBN\\nvIBRwBy08fFeih0GE+0IKr0LyAQ8naMjRTD8A6SbPE0CgYEA9HJ+qigfUPsh/Q+u\\nyyoZeIrsR1xoNVcwANwpWnChsic+B3V/D/pWMJxPv9wKRsVt/dc4kVht//j69tS8\\ny3BFoUxSfUuoK5hdhI8osk3wdVFOnrPPs57s2bMcPPF3Rd5iMvcRNqM1IENCpDAR\\n4zlrEqcMpGSNfaSVhFEyo0fvsV8CgYEA7+6gxxkZJD7DwoVUk8w0BJoq7pNUZc43\\nC0uI8EIRCWxSkd5ahruJjreJuFM1IQUmmqFgewdhEIdUjyORwgVQlo469uwqYPQ5\\n8RWMJcQVK8+QEWV/TdywO3P7oEFgFmVlII/h7Janz/ZlOFZ1X8ANVvpenqgeB2j+\\nl1JMVfjnUSsCgYAHmvRT6PGofFe/XtiKW6H1PSVCxx464p6MuEzVEoIFX/EvHDm6\\nzogV9RcKGhd7wjK83hBVfVHWz/FG8rF5BuIztYMvgMYXrSLjt+yFN6WOkNwIVgHV\\nTdGCqG7tennCg7u8aDFx6LwDZ/RP1WsJDcVGDEp5ZuN8ED3SoxAXQmqzswKBgCk3\\nOtM40oLRbVtq//5ro7vup9VX5bWfWQFNtnZfQwH1Y7G/GpnueVDU4omRcZz8f4cs\\nlaBMwjXOqY31NEK6Gv/h6usj4pvJGHL7mpmaN3DRNRRn9RhxAq0T3XPIBzORs2+G\\nh+7WanllADpPT9Zk7WW1mK90fcQUGzfvYUGbglEFAoGARffpCUANEp1oedOyUVRN\\nSKIvSRggxxqMuzSdnm7eGKDmm+kbA8Iig2C0jgcn4vQZpngbhlNsGrb26Bvdh8wE\\nTBtkcxSBjzsFBdE4kSdVqxnZeVezouWixvkxL4ax1xwczS9hyJlunDljsUb2PkwZ\\nBE39glMdpIqGYrpSTM3p6mI=\\n-----END PRIVATE KEY-----\",\r\n        \"certSerialNo\": \"2FD947564972A8536BDD750944C4796CDF3265EB\",\r\n        \"notifyUrl\": \"http://q668fd96.natappfree.cc/api/pay/notify/wechat\"\r\n    },\r\n    \"alipay\": {\r\n        \"enabled\": true,\r\n        \"appId\": \"2021005192689177\",\r\n        \"privateKey\": \"MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCc1KF+8tqemN/+k99xwIxtfrZ8Py7ZtSv6zoDa0Z9pW1IWpYmz+Zm1wtF3CTX5xApyyBTWQgK8Pb/oEz4u4zKzuwcZFX/IdfP2mWaMHRvWnDLunWqH8rQO+JOaWDvzA68lL02AfubuafQldXY+hdeOpt+t5Kj/OnC+o4z0qTAcM9uTpeX9Z8lttlcW9JtCATRP+klr52bRaOcACgh3MIJrQ3OteeiikvbVtZtw4u3X2h5tdRlCl2/youKO6/iZXGmAmtTGRU8Iy8iBAMI6Ow8K6XH5xHccTzTOx8xv1PZ2IszVvMVhLJDXaUg4DyVbhN8hrmKFmu1i9eBbdSZixkNpAgMBAAECggEAXDjFBqu0VxK6lS9Lc86wRSsAECvvVuIsjH2mVAZ0YTXsHZkWUpjyBGodVow6Czd2lWyGpD+I8Dy3frbiGBxOElZmpB96VtzVqyslnDr5xcdwQ9SZcnwL2cnesiI0joCaG5mnT2pQTd5MTUK3V6jIyv/iBJWzsvIgnln6Z1yeB9ai/3c5Lvu0/ZnhC7trqD73BB1x49E0AV90y0/C/IA+FLEKio9/xjgYweSvTiaYTCBKzQv74Oco54HDtd93rlavZUu7F1qdpOWAj903N1xf8A/fepcL8/qPdSZNoRbPr2NgPMZa70hLvnWDfIXRWoaOZ+lFnPtewI8FAaVX4mI3AQKBgQDO8GxdEunrmRuOXbv/JqTj0dG2lXT7kUvDdJ6QVr3HIsmyxkZXQsp/7QdXh/FdRBNFwkOirmClUqrvYq3CbytgjNdxmYdZQ2A/YXqDdTs8J2Li36hbkOPIFNyMZsjtYF39eosf2oF0/ydRSlMqW5B6jpUh5qCYVWkVtUjLuXaM2QKBgQDCAwMqd6Um9X50dKNIqY1X2ImLiRdLVaqn4/pTwylxxIrRO9f5jF7PnenDci809+Sc+yCcZarvdh1QbUE+YGhYOjj2WGaB9sS2TGDFzOguGs7m7hCIQPa6VEyP2I07kaZcpb+r5GqnT9U47mPRcLJe3zop+w3B7cW5JcdtOSCREQKBgQCbpbALzWcOIoncado2Dk3lYPJ4fy+O6/jtWTDOZb+2IQ9OHN3ZUk5XK+PizUgYm1RXmscefEQK9QPGrBT/cnhQ1X5SXmS0Gf4xjdMFP06/buxsskbCIFeDLVW5cLHeASaQufQckE/gvO1IsjudV2NzGv1Gk13lVhCFGGZZfPSS+QKBgG4y2dSAWx1y6d3p9mkqbW9NPms0djfDNAji9GgpfVvyoErSbA2BzsSs1H/AVtIGUCNefRp4oQwdEe+B70In7nzWrU43zhnZ+cf2QC16AxNVBNqktF1AUSRrB4XZIfeI9m6/csyHFJFuRhVtSuNG2PoMX3RC9oCFtv5AWDNQ9I+RAoGAZF1dQs826kCeptQHXnlgTGNNIX9jLGyfO2qysBOCcqwFIrcJpsb11Q1xLrQmju6EHzr4kAINp32Qd5fo/oCM25JuSiw+fK6CgkAEYjSr/9dD4KpGicHmsib3GyfPj850K2RwFz2RckwX+If/NgI3dIecMTgTJ0tfytaaeqFH4PY=\",\r\n        \"publicKey\": \"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiwci3m6eLWeR1kgfeWm/F0V1e68VWUyRB4N5mhnxryTHqiLeN8ilxN9Kn/Ute1C9cL3b4hfx3NYk7zt60QWP9ly8QJQOlqd1H7XsG16AlEpsIaN1SrMYWq16nAD6uwvMmK0nTdzhuNIKOfdC2YWyv3AJTWh0nCTddYV2D+eSH/Ui6xkfgK8pFn/X1Q0xjXvuZrsXxF+WTk5mymEy2u4Kp7/rD/lClfNAv68kOHe92iKj1VzhtROrSp5//xuvL2PA7FLMqo5olZpBmda3eMWgnvHNwvaJvHJENN2ubANwMPNkwMkQ7MKLCBI33fzEERxJBACrJCc6lo8t+wq3zDo/uwIDAQAB\",\r\n        \"signType\": \"RSA2\",\r\n        \"charset\": \"UTF-8\",\r\n        \"gatewayUrl\": \"https://openapi.alipay.com/gateway.do\",\r\n        \"notifyUrl\": \"http://q668fd96.natappfree.cc/api/pay/notify/alipay\",\r\n        \"returnUrl\": \"\"\r\n    }\r\n}', 12, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 16:13:21');
INSERT INTO `sys_config_group` VALUES (13, 'security', '安全配置', NULL, '{\"encryptEnabled\":true,\"encryptScope\":\"global\",\"encryptPublicKey\":\"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxwcKZj5Wdozt6+8i9H2WW2eNaNUvI9iyU7Ot2P5XW9MSfTqRTCbv/aVEUOm60SHm7OXFAbuwUeuo6Pu2P7qPffXiqCXBdC1joo7VywNlapnmkwXP6jhuP+oHM31BvG2uInv40LHocUIRbMhREavnw+By7kT3Cq2SmgLBGsRkoIrpAuMBe47n8DjRGq2cvFde/EoChO0uO0AxlTUpfNXatUDGH0NtCEJeECoMBkg4nI0JAPnZETkimurbryPFoAVk5ld/GJg5WruQ1piicy9YgbOhjWnmb6gJ1RUU9xypNeHI/jLQCdjBn4NGQFtD73v36/WFnv4MgFAZV6iKr5kSdQIDAQAB\",\"encryptPrivateKey\":\"MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDHBwpmPlZ2jO3r7yL0fZZbZ41o1S8j2LJTs63Y/ldb0xJ9OpFMJu/9pURQ6brRIebs5cUBu7BR66jo+7Y/uo999eKoJcF0LWOijtXLA2VqmeaTBc/qOG4/6gczfUG8ba4ie/jQsehxQhFsyFERq+fD4HLuRPcKrZKaAsEaxGSgiukC4wF7jufwONEarZy8V178SgKE7S47QDGVNSl81dq1QMYfQ20IQl4QKgwGSDicjQkA+dkROSKa6tuvI8WgBWTmV38YmDlau5DWmKJzL1iBs6GNaeZvqAnVFRT3HKk14cj+MtAJ2MGfg0ZAW0Pve/fr9YWe/gyAUBlXqIqvmRJ1AgMBAAECggEAIOYACRCK2EPJXDOGMqXDwc4nKMn8Zc9/AqjztqesJwiHyN1ygQT6rJGx7jIEaGdTNZtxaiztI01x+TkKUhRzfZ20XpkHFj4edxNnMYyZKfrQi0LtsEitqLD1icRNpmj23MpjQZP22SnTmYivJd2ljNJADTSnJUO1tPF5nAQUohipaHm9ikipKzT+Qa605nj1TvG1NF1a0y/IElBGb5FFyQGISgUoiPh8/aZXeO5pS6YMJTTQul/9Q7f9fwJFrzPl3qqc3kDxYjagJcPtV5VmX/nSrMpeLnaTvRIg78ocwNF+XYJ5L1Sr9wxYEADykw4P8E0ijGYynSeZlo0u+Q7U7QKBgQDZaG5ITWYmt+4KQrR0r1HHGFWJPtFVKcwjC+EIm9I1S+gTOjZ/6SG45upDqlHtmCOMf1drRFhSehdD6UHUFL4xN/fAxkP3F+iKU/KfJy6yclCuhW+k0Efi6W4mKR9ZkhINJvVibsNdA0vXQa603bbr7hfHVeJl1xI761htsnEFuwKBgQDqW1s5f67gXowzjmK6a40Z+/DIoHBTd267zOIEknhUg6oaMtW1v/yPjwWrf6wJmpUFO3Mq3xTDd/k1iXBOke2vHmZG2AplNVScreRx20lRBmzuGe+9sSDozTfFJO25oPhH86wmIAmqMB5nu1L1TJjbKRAU+hcdC+v22NWMQ48tjwKBgALF9kIt2pO73Ol8mFi0s9JaWRz7FCiF8/iuehxmAHR1l2xHXdKb4rY9G9fpIEprmmh8Z10S7h1/OTTAkPpnmVV/ZUWsQcmxIGJDV+D32vyjwKu5QAdWMNSQLbuG4sN9vYU1bgPnbc6N8DW6vMPJ4D96Ngtw6QZri+v/wI0FrbNpAoGAcpvuxvXMXemfAu+VFLnYLWbqYBMmG4uC2dDej4HZ2urw2xMVNGcJamN1UGOFjMTOL9rc/ZBPJTCc7TOjeqke5c8mEWtB2jD0ihL4bz3gYwGTb/W7Krde8rq5lW3z3B3+jaF7BMISN+qEVBJmBZRKBJPWS4vqlcfow7VS6d94O70CgYBTLo2LdYZV9rn7FGmgC9/fuJOgWEfeqmunNx8SsYUjaXSyy+Vb+dlgH/YRfypb37rxxsNwWQKggZww6gSO1/TkFoV73W035XBKbMB3XLEFHp2v75qYBYEHvVpW1YEl2QGlUzOUWXrP5G/3v8O0/+5yJwjKcmkWDjPGIIKj8GPZsQ==\",\"xssFilter\":true,\"sqlInject\":true}', 13, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 14:38:29');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门ID',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  `sort` int NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(0-停用 1-正常)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '删除标识(0-未删除 1-已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, 0, '0', 'Mars火星网络', 0, '管理员', NULL, NULL, 1, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, 1, 0);
INSERT INTO `sys_dept` VALUES (2, 1, '0,1', '技术部', 1, '张三', NULL, NULL, 1, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_dept` VALUES (3, 1, '0,1', '产品部', 2, '李四', NULL, NULL, 1, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_dept` VALUES (4, 1, '0,1', '运营部', 3, '王五', NULL, NULL, 1, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '样式属性',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` tinyint NULL DEFAULT 0 COMMENT '是否默认(0-否 1-是)',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(0-停用 1-正常)',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '删除标识(0-未删除 1-已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '1', 'sys_user_sex', NULL, 'default', 0, 1, NULL, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '2', 'sys_user_sex', NULL, 'default', 0, 1, NULL, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '0', 'sys_user_sex', NULL, 'default', 0, 1, NULL, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (4, 1, '正常', '1', 'sys_status', NULL, 'success', 0, 1, NULL, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (5, 2, '停用', '0', 'sys_status', NULL, 'error', 0, 1, NULL, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (6, 1, '是', '1', 'sys_yes_no', NULL, 'success', 0, 1, NULL, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (7, 2, '否', '0', 'sys_yes_no', NULL, 'error', 0, 1, NULL, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (8, 0, 'sex', '1', 'sex', NULL, 'default', 0, 1, '', '2026-01-29 23:21:38', '2026-01-29 23:21:38', 1, 1, 0);
INSERT INTO `sys_dict_data` VALUES (9, 0, '女', '0', 'sex', NULL, 'default', 0, 1, '', '2026-01-29 23:21:58', '2026-01-29 23:21:58', 1, 1, 0);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典类型',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(0-停用 1-正常)',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '删除标识(0-未删除 1-已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', 1, '用户性别列表', '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (2, '系统状态', 'sys_status', 1, '系统通用状态', '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (3, '是否', 'sys_yes_no', 1, '是否选项', '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (4, '性别', 'sex', 1, '', '2026-01-29 23:21:29', '2026-01-29 23:21:29', 1, 1, 0);

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `original_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '原始文件名',
  `file_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '存储文件名',
  `file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件路径',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '文件访问URL',
  `file_size` bigint NULL DEFAULT 0 COMMENT '文件大小（字节）',
  `file_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '文件类型（MIME类型）',
  `file_suffix` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '文件后缀',
  `storage_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '存储类型',
  `bucket_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '存储桶名称',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_file_path`(`file_path`(191) ASC) USING BTREE,
  INDEX `idx_storage_type`(`storage_type` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file
-- ----------------------------
INSERT INTO `sys_file` VALUES (1, 'FPj0uHDhtvlIgwhfEk0-Lu-bp4Duopj5_GorB8Sxbqe4pKUR4-7HxwbA7VFa8fTK.jpg', '9f375134efa54ddeb9d19adb5295939f.jpg', '2026/01/31/9f375134efa54ddeb9d19adb5295939f.jpg', 'http://localhost:8080/files/2026/01/31/9f375134efa54ddeb9d19adb5295939f.jpg', 823196, 'image/jpeg', '.jpg', 'local', '', '', '1', '2026-01-31 16:50:36');

-- ----------------------------
-- Table structure for sys_file_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_config`;
CREATE TABLE `sys_file_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置名称',
  `storage_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '存储类型(local/minio/aliyun)',
  `master` tinyint NULL DEFAULT 0 COMMENT '是否为主配置(0否 1是)',
  `domain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访问域名',
  `base_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '基础路径(本地存储)',
  `bucket_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '存储桶名称',
  `access_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访问密钥',
  `secret_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '秘密密钥',
  `endpoint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '端点地址',
  `region` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地域',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(0禁用 1启用)',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_storage_type`(`storage_type` ASC) USING BTREE,
  INDEX `idx_master`(`master` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件存储配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file_config
-- ----------------------------
INSERT INTO `sys_file_config` VALUES (1, '本地存储', 'local', 1, 'http://localhost:8080', 'D:/uploads', NULL, NULL, NULL, NULL, NULL, 1, '默认本地存储配置', NULL, '2026-01-30 23:35:08', NULL, '2026-01-30 23:35:08');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'cron执行表达式',
  `misfire_policy` tinyint NULL DEFAULT 3 COMMENT '计划执行错误策略(1-立即执行 2-执行一次 3-放弃执行)',
  `concurrent` tinyint NULL DEFAULT 1 COMMENT '是否并发执行(0-允许 1-禁止)',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态(0-暂停 1-正常)',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '删除标识(0-未删除 1-已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, '系统默认（无参）', 'DEFAULT', 'sampleTask.noParams', '0/10 * * * * ?', 3, 1, 0, '无参数的示例任务', '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_job` VALUES (2, '系统默认（有参）', 'DEFAULT', 'sampleTask.withParams(\'hello\')', '0/15 * * * * ?', 3, 1, 0, '有参数的示例任务', '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_job` VALUES (3, '1', 'DEFAULT', 'sampleTask.noParams', '0/10 * * * * ?', 3, 1, 0, '111', '2026-01-29 22:59:47', '2026-01-29 22:59:47', 1, 1, 0);

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日志信息',
  `status` tinyint NULL DEFAULT 0 COMMENT '执行状态(0-正常 1-失败)',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '异常信息',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `stop_time` datetime NULL DEFAULT NULL COMMENT '停止时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------
INSERT INTO `sys_job_log` VALUES (1, '1', 'DEFAULT', '1', '执行失败', 1, 'java.lang.StringIndexOutOfBoundsException: begin 0, end -1, length 1\r\n	at java.base/java.lang.String.checkBoundsBeginEnd(String.java:4602)\r\n	at java.base/java.lang.String.substring(String.java:2705)\r\n	at com.mars.job.util.JobInvokeUtil.getBeanName(JobInvokeUtil.java:45)\r\n	at com.mars.job.util.JobInvokeUtil.invokeMethod(JobInvokeUtil.java:20)\r\n	at com.mars.job.util.QuartzDisallowConcurrentExecution.doExecute(QuartzDisallowConcurrentExecution.java:15)\r\n	at com.mars.job.util.AbstractQuartzJob.execute(AbstractQuartzJob.java:40)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', '2026-01-29 22:59:50', '2026-01-29 22:59:50');
INSERT INTO `sys_job_log` VALUES (2, '1', 'DEFAULT', '1', '执行失败', 1, 'java.lang.StringIndexOutOfBoundsException: begin 0, end -1, length 1\r\n	at java.base/java.lang.String.checkBoundsBeginEnd(String.java:4602)\r\n	at java.base/java.lang.String.substring(String.java:2705)\r\n	at com.mars.job.util.JobInvokeUtil.getBeanName(JobInvokeUtil.java:45)\r\n	at com.mars.job.util.JobInvokeUtil.invokeMethod(JobInvokeUtil.java:20)\r\n	at com.mars.job.util.QuartzDisallowConcurrentExecution.doExecute(QuartzDisallowConcurrentExecution.java:15)\r\n	at com.mars.job.util.AbstractQuartzJob.execute(AbstractQuartzJob.java:40)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', '2026-01-29 22:59:55', '2026-01-29 22:59:55');
INSERT INTO `sys_job_log` VALUES (3, '1', 'DEFAULT', '1', '执行失败', 1, 'java.lang.StringIndexOutOfBoundsException: begin 0, end -1, length 1\r\n	at java.base/java.lang.String.checkBoundsBeginEnd(String.java:4602)\r\n	at java.base/java.lang.String.substring(String.java:2705)\r\n	at com.mars.job.util.JobInvokeUtil.getBeanName(JobInvokeUtil.java:45)\r\n	at com.mars.job.util.JobInvokeUtil.invokeMethod(JobInvokeUtil.java:20)\r\n	at com.mars.job.util.QuartzDisallowConcurrentExecution.doExecute(QuartzDisallowConcurrentExecution.java:15)\r\n	at com.mars.job.util.AbstractQuartzJob.execute(AbstractQuartzJob.java:40)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', '2026-01-29 23:01:29', '2026-01-29 23:01:29');
INSERT INTO `sys_job_log` VALUES (4, '1', 'DEFAULT', '1', '执行失败', 1, 'java.lang.StringIndexOutOfBoundsException: begin 0, end -1, length 1\r\n	at java.base/java.lang.String.checkBoundsBeginEnd(String.java:4602)\r\n	at java.base/java.lang.String.substring(String.java:2705)\r\n	at com.mars.job.util.JobInvokeUtil.getBeanName(JobInvokeUtil.java:45)\r\n	at com.mars.job.util.JobInvokeUtil.invokeMethod(JobInvokeUtil.java:20)\r\n	at com.mars.job.util.QuartzDisallowConcurrentExecution.doExecute(QuartzDisallowConcurrentExecution.java:15)\r\n	at com.mars.job.util.AbstractQuartzJob.execute(AbstractQuartzJob.java:40)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\n', '2026-01-29 23:01:54', '2026-01-29 23:01:54');
INSERT INTO `sys_job_log` VALUES (5, '1', 'DEFAULT', 'sampleTask.noParams', '执行成功', 0, NULL, '2026-01-29 23:02:57', '2026-01-29 23:02:57');
INSERT INTO `sys_job_log` VALUES (6, '1', 'DEFAULT', 'sampleTask.noParams', '执行成功', 0, NULL, '2026-01-29 23:03:02', '2026-01-29 23:03:02');
INSERT INTO `sys_job_log` VALUES (7, '1', 'DEFAULT', 'sampleTask.noParams', '执行成功', 0, NULL, '2026-01-31 12:33:00', '2026-01-31 12:33:00');
INSERT INTO `sys_job_log` VALUES (8, '1', 'DEFAULT', 'sampleTask.noParams', '执行成功', 0, NULL, '2026-01-31 22:36:20', '2026-01-31 22:36:20');
INSERT INTO `sys_job_log` VALUES (9, '1', 'DEFAULT', 'sampleTask.noParams', '执行成功', 0, NULL, '2026-01-31 22:36:30', '2026-01-31 22:36:30');

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `status` tinyint NULL DEFAULT 0 COMMENT '登录状态(0-成功 1-失败)',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 120 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '登录日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
INSERT INTO `sys_login_log` VALUES (1, 'admin', '127.0.0.1', NULL, 'Chrome', 'Windows', 0, '登录成功', '2026-01-29 23:12:29');
INSERT INTO `sys_login_log` VALUES (2, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-29 23:20:11');
INSERT INTO `sys_login_log` VALUES (3, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-29 23:31:18');
INSERT INTO `sys_login_log` VALUES (4, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-29 23:43:45');
INSERT INTO `sys_login_log` VALUES (5, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-29 23:43:49');
INSERT INTO `sys_login_log` VALUES (6, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-29 23:43:52');
INSERT INTO `sys_login_log` VALUES (7, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-29 23:53:22');
INSERT INTO `sys_login_log` VALUES (8, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-29 23:55:49');
INSERT INTO `sys_login_log` VALUES (9, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-30 15:53:09');
INSERT INTO `sys_login_log` VALUES (10, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-30 23:12:24');
INSERT INTO `sys_login_log` VALUES (11, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-30 23:36:27');
INSERT INTO `sys_login_log` VALUES (12, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-30 23:39:07');
INSERT INTO `sys_login_log` VALUES (13, 'mars', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '用户不存在', '2026-01-30 23:57:19');
INSERT INTO `sys_login_log` VALUES (14, 'mars', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '用户不存在', '2026-01-30 23:57:20');
INSERT INTO `sys_login_log` VALUES (15, 'mars', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '用户不存在', '2026-01-30 23:57:21');
INSERT INTO `sys_login_log` VALUES (16, 'mars', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '用户不存在', '2026-01-30 23:57:24');
INSERT INTO `sys_login_log` VALUES (17, 'test', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-30 23:57:52');
INSERT INTO `sys_login_log` VALUES (18, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 00:03:15');
INSERT INTO `sys_login_log` VALUES (19, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 10:31:44');
INSERT INTO `sys_login_log` VALUES (20, 'test', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 10:32:13');
INSERT INTO `sys_login_log` VALUES (21, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 10:39:20');
INSERT INTO `sys_login_log` VALUES (22, 'mars', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 13:54:15');
INSERT INTO `sys_login_log` VALUES (23, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:04:34');
INSERT INTO `sys_login_log` VALUES (24, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:04:35');
INSERT INTO `sys_login_log` VALUES (25, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:04:36');
INSERT INTO `sys_login_log` VALUES (26, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:04:42');
INSERT INTO `sys_login_log` VALUES (27, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:04:42');
INSERT INTO `sys_login_log` VALUES (28, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:04:42');
INSERT INTO `sys_login_log` VALUES (29, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:04:43');
INSERT INTO `sys_login_log` VALUES (30, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:04:48');
INSERT INTO `sys_login_log` VALUES (31, 'admin', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:04:52');
INSERT INTO `sys_login_log` VALUES (32, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:05:38');
INSERT INTO `sys_login_log` VALUES (33, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:05:51');
INSERT INTO `sys_login_log` VALUES (34, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:05:53');
INSERT INTO `sys_login_log` VALUES (35, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:05:54');
INSERT INTO `sys_login_log` VALUES (36, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:05:55');
INSERT INTO `sys_login_log` VALUES (37, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:05:55');
INSERT INTO `sys_login_log` VALUES (38, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:05:56');
INSERT INTO `sys_login_log` VALUES (39, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:05:57');
INSERT INTO `sys_login_log` VALUES (40, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:05:58');
INSERT INTO `sys_login_log` VALUES (41, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:05:59');
INSERT INTO `sys_login_log` VALUES (42, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:05:59');
INSERT INTO `sys_login_log` VALUES (43, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:06:00');
INSERT INTO `sys_login_log` VALUES (44, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:06:01');
INSERT INTO `sys_login_log` VALUES (45, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:06:01');
INSERT INTO `sys_login_log` VALUES (46, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:06:02');
INSERT INTO `sys_login_log` VALUES (47, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:06:03');
INSERT INTO `sys_login_log` VALUES (48, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:06:04');
INSERT INTO `sys_login_log` VALUES (49, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:06:04');
INSERT INTO `sys_login_log` VALUES (50, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:06:05');
INSERT INTO `sys_login_log` VALUES (51, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:06:06');
INSERT INTO `sys_login_log` VALUES (52, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:06:07');
INSERT INTO `sys_login_log` VALUES (53, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:06:07');
INSERT INTO `sys_login_log` VALUES (54, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:06:15');
INSERT INTO `sys_login_log` VALUES (55, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:06:22');
INSERT INTO `sys_login_log` VALUES (56, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:06:31');
INSERT INTO `sys_login_log` VALUES (57, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:07:27');
INSERT INTO `sys_login_log` VALUES (58, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 14:07:30');
INSERT INTO `sys_login_log` VALUES (59, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:12:31');
INSERT INTO `sys_login_log` VALUES (60, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:12:34');
INSERT INTO `sys_login_log` VALUES (61, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:22:09');
INSERT INTO `sys_login_log` VALUES (62, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:22:13');
INSERT INTO `sys_login_log` VALUES (63, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 14:22:17');
INSERT INTO `sys_login_log` VALUES (64, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:23:58');
INSERT INTO `sys_login_log` VALUES (65, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 14:24:08');
INSERT INTO `sys_login_log` VALUES (66, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 14:24:56');
INSERT INTO `sys_login_log` VALUES (67, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:27:19');
INSERT INTO `sys_login_log` VALUES (68, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 14:27:22');
INSERT INTO `sys_login_log` VALUES (69, 'mars', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 14:29:20');
INSERT INTO `sys_login_log` VALUES (70, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 14:44:07');
INSERT INTO `sys_login_log` VALUES (71, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 14:56:09');
INSERT INTO `sys_login_log` VALUES (72, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 14:57:18');
INSERT INTO `sys_login_log` VALUES (73, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 14:57:44');
INSERT INTO `sys_login_log` VALUES (74, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 14:58:03');
INSERT INTO `sys_login_log` VALUES (75, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 14:59:00');
INSERT INTO `sys_login_log` VALUES (76, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 15:00:17');
INSERT INTO `sys_login_log` VALUES (77, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 15:04:11');
INSERT INTO `sys_login_log` VALUES (78, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 15:11:02');
INSERT INTO `sys_login_log` VALUES (79, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 15:11:19');
INSERT INTO `sys_login_log` VALUES (80, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 15:14:05');
INSERT INTO `sys_login_log` VALUES (81, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 15:14:15');
INSERT INTO `sys_login_log` VALUES (82, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 15:14:25');
INSERT INTO `sys_login_log` VALUES (83, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 15:15:06');
INSERT INTO `sys_login_log` VALUES (84, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 15:23:23');
INSERT INTO `sys_login_log` VALUES (85, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 15:36:27');
INSERT INTO `sys_login_log` VALUES (86, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 15:39:07');
INSERT INTO `sys_login_log` VALUES (87, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 15:39:16');
INSERT INTO `sys_login_log` VALUES (88, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 15:49:43');
INSERT INTO `sys_login_log` VALUES (89, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 15:50:22');
INSERT INTO `sys_login_log` VALUES (90, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 16:08:16');
INSERT INTO `sys_login_log` VALUES (91, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 16:28:17');
INSERT INTO `sys_login_log` VALUES (92, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 16:28:53');
INSERT INTO `sys_login_log` VALUES (93, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 16:40:01');
INSERT INTO `sys_login_log` VALUES (94, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 16:49:24');
INSERT INTO `sys_login_log` VALUES (95, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 1, '密码错误', '2026-01-31 20:18:03');
INSERT INTO `sys_login_log` VALUES (96, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 20:18:12');
INSERT INTO `sys_login_log` VALUES (97, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 20:24:34');
INSERT INTO `sys_login_log` VALUES (98, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 20:25:02');
INSERT INTO `sys_login_log` VALUES (99, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 20:25:22');
INSERT INTO `sys_login_log` VALUES (100, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 20:26:57');
INSERT INTO `sys_login_log` VALUES (101, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 20:31:18');
INSERT INTO `sys_login_log` VALUES (102, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 20:35:54');
INSERT INTO `sys_login_log` VALUES (103, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 20:42:37');
INSERT INTO `sys_login_log` VALUES (104, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 20:47:54');
INSERT INTO `sys_login_log` VALUES (105, 'mars', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 20:48:43');
INSERT INTO `sys_login_log` VALUES (106, 'lisi', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 20:49:49');
INSERT INTO `sys_login_log` VALUES (107, 'lisi', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 21:04:05');
INSERT INTO `sys_login_log` VALUES (108, 'lisi', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 21:29:05');
INSERT INTO `sys_login_log` VALUES (109, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 22:06:00');
INSERT INTO `sys_login_log` VALUES (110, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 22:24:30');
INSERT INTO `sys_login_log` VALUES (111, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 22:28:54');
INSERT INTO `sys_login_log` VALUES (112, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 22:30:00');
INSERT INTO `sys_login_log` VALUES (113, 'mars666', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 22:31:01');
INSERT INTO `sys_login_log` VALUES (114, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 22:32:57');
INSERT INTO `sys_login_log` VALUES (115, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 22:42:56');
INSERT INTO `sys_login_log` VALUES (116, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 23:07:42');
INSERT INTO `sys_login_log` VALUES (117, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 23:08:49');
INSERT INTO `sys_login_log` VALUES (118, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 23:09:00');
INSERT INTO `sys_login_log` VALUES (119, 'admin', '127.0.0.1', '内网IP', 'Chrome', 'Windows 10 or Windows Server 2016', 0, '登录成功', '2026-01-31 23:57:25');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父级ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `type` tinyint NOT NULL COMMENT '菜单类型(1-目录 2-菜单 3-按钮)',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由地址',
  `component` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `permission` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `visible` tinyint NULL DEFAULT 1 COMMENT '是否可见(0-隐藏 1-显示)',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(0-禁用 1-启用)',
  `is_frame` tinyint NULL DEFAULT 0 COMMENT '是否外链(0-否 1-是)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '删除标识(0-未删除 1-已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 161 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', 1, '/system', NULL, NULL, 'SettingsOutline', 1, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (2, 1, '用户管理', 2, '/system/user', '/system/user/index', 'sys:user:list', 'PersonOutline', 1, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (3, 2, '新增用户', 3, NULL, NULL, 'sys:user:add', NULL, 1, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (4, 2, '编辑用户', 3, NULL, NULL, 'sys:user:edit', NULL, 2, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (5, 2, '删除用户', 3, NULL, NULL, 'sys:user:delete', NULL, 3, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (6, 1, '角色管理', 2, '/system/role', '/system/role/index', 'sys:role:list', 'PeopleOutline', 2, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (7, 6, '新增角色', 3, NULL, NULL, 'sys:role:add', NULL, 1, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (8, 6, '编辑角色', 3, NULL, NULL, 'sys:role:edit', NULL, 2, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (9, 6, '删除角色', 3, NULL, NULL, 'sys:role:delete', NULL, 3, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (10, 1, '菜单管理', 2, '/system/menu', '/system/menu/index', 'sys:menu:list', 'MenuOutline', 3, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (11, 10, '新增菜单', 3, NULL, NULL, 'sys:menu:add', NULL, 1, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (12, 10, '编辑菜单', 3, NULL, NULL, 'sys:menu:edit', NULL, 2, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (13, 10, '删除菜单', 3, NULL, NULL, 'sys:menu:delete', NULL, 3, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (14, 1, '字典管理', 2, '/system/dict', '/system/dict/index', 'sys:dict:list', 'BookOutline', 4, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (15, 14, '新增字典', 3, NULL, NULL, 'sys:dict:add', NULL, 1, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (16, 14, '编辑字典', 3, NULL, NULL, 'sys:dict:edit', NULL, 2, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (17, 14, '删除字典', 3, NULL, NULL, 'sys:dict:delete', NULL, 3, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (18, 1, '系统配置', 2, '/system/config', '/system/config/index', 'sys:config:list', 'SettingsSharp', 5, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (19, 18, '新增配置', 3, NULL, NULL, 'sys:config:add', NULL, 1, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (20, 18, '编辑配置', 3, NULL, NULL, 'sys:config:edit', NULL, 2, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (21, 18, '删除配置', 3, NULL, NULL, 'sys:config:delete', NULL, 3, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (22, 0, '组织管理', 1, '/org', NULL, NULL, 'BusinessOutline', 2, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (23, 22, '部门管理', 2, '/org/dept', '/org/dept/index', 'sys:dept:list', 'GitNetworkOutline', 1, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (24, 23, '新增部门', 3, NULL, NULL, 'sys:dept:add', NULL, 1, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (25, 23, '编辑部门', 3, NULL, NULL, 'sys:dept:edit', NULL, 2, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (26, 23, '删除部门', 3, NULL, NULL, 'sys:dept:delete', NULL, 3, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (27, 22, '岗位管理', 2, '/org/post', '/org/post/index', 'sys:post:list', 'IdCardOutline', 2, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (28, 27, '新增岗位', 3, NULL, NULL, 'sys:post:add', NULL, 1, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (29, 27, '编辑岗位', 3, NULL, NULL, 'sys:post:edit', NULL, 2, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (30, 27, '删除岗位', 3, NULL, NULL, 'sys:post:delete', NULL, 3, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (31, 0, '系统日志', 1, '/log', NULL, NULL, 'DocumentTextOutline', 3, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (32, 31, '操作日志', 2, '/log/operlog', '/log/operlog/index', 'monitor:operlog:list', 'ListOutline', 1, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (33, 32, '删除日志', 3, NULL, NULL, 'monitor:operlog:delete', NULL, 1, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (34, 31, '登录日志', 2, '/log/loginlog', '/log/loginlog/index', 'monitor:loginlog:list', 'LogInOutline', 2, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (35, 34, '删除日志', 3, NULL, NULL, 'monitor:loginlog:delete', NULL, 1, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (36, 0, '系统监控', 1, '/monitor', NULL, NULL, 'PulseOutline', 4, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (37, 36, '在线用户', 2, '/monitor/online', '/monitor/online/index', 'monitor:online:list', 'PeopleCircleOutline', 1, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (38, 37, '强退用户', 3, NULL, NULL, 'monitor:online:forceLogout', NULL, 1, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (39, 36, '定时任务', 2, '/monitor/job', '/monitor/job/index', 'monitor:job:list', 'TimerOutline', 2, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (40, 39, '新增任务', 3, NULL, NULL, 'monitor:job:add', NULL, 1, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (41, 39, '编辑任务', 3, NULL, NULL, 'monitor:job:edit', NULL, 2, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (42, 39, '删除任务', 3, NULL, NULL, 'monitor:job:delete', NULL, 3, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (43, 36, '缓存监控', 2, '/monitor/cache', '/monitor/cache/index', 'monitor:cache:list', 'ServerOutline', 3, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (44, 43, '删除缓存', 3, NULL, NULL, 'monitor:cache:delete', NULL, 1, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (45, 36, '服务监控', 2, '/monitor/server', '/monitor/server/index', 'monitor:server:list', 'DesktopOutline', 4, 1, 1, 0, '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (126, 0, '文件管理', 1, '/file', NULL, NULL, 'FolderOpenOutline', 5, 1, 1, 0, '2026-01-30 23:40:01', '2026-01-30 23:40:01', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (127, 126, '文件列表', 2, '/system/file', '/system/file/index', 'sys:file:list', 'DocumentOutline', 1, 1, 1, 0, '2026-01-30 23:40:01', '2026-01-30 23:40:01', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (128, 127, '上传文件', 3, NULL, NULL, 'sys:file:upload', NULL, 1, 1, 1, 0, '2026-01-30 23:40:01', '2026-01-30 23:40:01', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (129, 127, '删除文件', 3, NULL, NULL, 'sys:file:delete', NULL, 2, 1, 1, 0, '2026-01-30 23:40:01', '2026-01-30 23:40:01', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (130, 126, '文件配置', 2, '/system/file-config', '/system/file-config/index', 'sys:fileConfig:list', 'CloudOutline', 2, 1, 1, 0, '2026-01-30 23:40:01', '2026-01-31 14:43:52', NULL, 1, 1);
INSERT INTO `sys_menu` VALUES (131, 130, '新增配置', 3, NULL, NULL, 'sys:fileConfig:add', NULL, 1, 1, 1, 0, '2026-01-30 23:40:01', '2026-01-31 14:43:45', NULL, 1, 1);
INSERT INTO `sys_menu` VALUES (132, 130, '编辑配置', 3, NULL, NULL, 'sys:fileConfig:edit', NULL, 2, 1, 1, 0, '2026-01-30 23:40:01', '2026-01-31 14:43:47', NULL, 1, 1);
INSERT INTO `sys_menu` VALUES (133, 130, '删除配置', 3, NULL, NULL, 'sys:fileConfig:delete', NULL, 3, 1, 1, 0, '2026-01-30 23:40:01', '2026-01-31 14:43:50', NULL, 1, 1);
INSERT INTO `sys_menu` VALUES (134, 0, '消息中心', 1, '/message', NULL, NULL, 'NotificationsOutline', 6, 1, 1, 0, '2026-01-30 23:53:55', '2026-01-30 23:53:55', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (135, 134, '系统通知', 2, '/message/notice', '/message/notice/index', 'sys:notice:list', 'NotificationsOutline', 1, 1, 1, 0, '2026-01-30 23:53:55', '2026-01-30 23:53:55', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (136, 135, '新增通知', 3, NULL, NULL, 'sys:notice:add', NULL, 1, 1, 1, 0, '2026-01-30 23:53:55', '2026-01-30 23:53:55', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (137, 135, '编辑通知', 3, NULL, NULL, 'sys:notice:edit', NULL, 2, 1, 1, 0, '2026-01-30 23:53:55', '2026-01-30 23:53:55', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (138, 135, '删除通知', 3, NULL, NULL, 'sys:notice:delete', NULL, 3, 1, 1, 0, '2026-01-30 23:53:55', '2026-01-30 23:53:55', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (139, 134, '即时聊天', 2, '/message/chat', '/message/chat/index', 'sys:chat:list', 'ChatbubbleOutline', 2, 1, 1, 0, '2026-01-30 23:53:55', '2026-01-30 23:53:55', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (140, 0, '测试菜单', 1, '/test', '', '', 'StarOutline', 7, 1, 1, 0, '2026-01-31 20:17:37', '2026-01-31 20:17:37', 1, 1, 0);
INSERT INTO `sys_menu` VALUES (141, 140, '测试菜单', 2, '/test/test', '/test/test/index', '', 'SearchOutline', 0, 1, 1, 0, '2026-01-31 20:23:37', '2026-01-31 20:23:37', 1, 1, 0);
INSERT INTO `sys_menu` VALUES (142, 2, '用户列表', 3, '', '', 'sys:user:list', '', 0, 1, 1, 0, '2026-01-31 20:51:50', '2026-01-31 20:51:50', 4, 4, 0);
INSERT INTO `sys_menu` VALUES (143, 6, '角色列表', 3, '', '', 'sys:role:list', '', 0, 1, 1, 0, '2026-01-31 20:52:13', '2026-01-31 20:52:13', 4, 4, 0);
INSERT INTO `sys_menu` VALUES (144, 10, '菜单列表', 3, '', '', 'sys:menu:list', '', 0, 1, 1, 0, '2026-01-31 20:52:32', '2026-01-31 20:52:32', 4, 4, 0);
INSERT INTO `sys_menu` VALUES (145, 14, '字典列表', 3, '', '', 'sys:dict:list', '', 0, 1, 1, 0, '2026-01-31 20:52:52', '2026-01-31 20:52:52', 4, 4, 0);
INSERT INTO `sys_menu` VALUES (146, 23, '部门列表', 3, '', '', 'sys:dept:list', '', 0, 1, 1, 0, '2026-01-31 20:54:26', '2026-01-31 20:54:26', 4, 4, 0);
INSERT INTO `sys_menu` VALUES (147, 27, '岗位列表', 3, '', '', 'sys:post:list', '', 0, 1, 1, 0, '2026-01-31 20:54:45', '2026-01-31 20:54:45', 4, 4, 0);
INSERT INTO `sys_menu` VALUES (148, 32, '查询日志', 3, '', '', 'monitor:operlog:list', '', 0, 1, 1, 0, '2026-01-31 20:55:14', '2026-01-31 20:55:14', 4, 4, 0);
INSERT INTO `sys_menu` VALUES (149, 34, '日志列表', 3, '', '', 'monitor:loginlog:list', '', 0, 1, 1, 0, '2026-01-31 20:55:32', '2026-01-31 20:55:32', 4, 4, 0);
INSERT INTO `sys_menu` VALUES (150, 37, '在线用户列表', 3, '', '', 'monitor:online:list', '', 0, 1, 1, 0, '2026-01-31 20:56:04', '2026-01-31 20:56:04', 4, 4, 0);
INSERT INTO `sys_menu` VALUES (151, 39, '任务列表', 3, '', '', 'monitor:job:list', '', 0, 1, 1, 0, '2026-01-31 20:56:20', '2026-01-31 20:56:20', 4, 4, 0);
INSERT INTO `sys_menu` VALUES (152, 127, '文件列表', 3, '', '', 'sys:file:list', '', 0, 1, 1, 0, '2026-01-31 20:57:18', '2026-01-31 20:57:18', 1, 1, 0);
INSERT INTO `sys_menu` VALUES (153, 135, '系统通知', 3, '', '', 'sys:notice:list', '', 0, 1, 1, 0, '2026-01-31 20:57:55', '2026-01-31 20:57:55', 1, 1, 0);
INSERT INTO `sys_menu` VALUES (154, 36, '服务器管理', 2, '/monitor/server-manager', '/monitor/server-manager/index', NULL, 'ServerOutline', 5, 1, 1, 0, '2026-01-31 23:37:21', '2026-01-31 23:37:21', NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (155, 154, '服务器列表', 3, NULL, NULL, 'monitor:server:list', NULL, 1, 1, 1, 0, '2026-01-31 23:37:21', '2026-01-31 23:37:21', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (156, 154, '服务器详情', 3, NULL, NULL, 'monitor:server:query', NULL, 2, 1, 1, 0, '2026-01-31 23:37:21', '2026-01-31 23:37:21', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (157, 154, '新增服务器', 3, NULL, NULL, 'monitor:server:add', NULL, 3, 1, 1, 0, '2026-01-31 23:37:21', '2026-01-31 23:37:21', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (158, 154, '编辑服务器', 3, NULL, NULL, 'monitor:server:edit', NULL, 4, 1, 1, 0, '2026-01-31 23:37:21', '2026-01-31 23:37:21', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (159, 154, '删除服务器', 3, NULL, NULL, 'monitor:server:remove', NULL, 5, 1, 1, 0, '2026-01-31 23:37:21', '2026-01-31 23:37:21', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (160, 154, '测试连接', 3, NULL, NULL, 'monitor:server:test', NULL, 6, 1, 1, 0, '2026-01-31 23:37:21', '2026-01-31 23:37:21', NULL, NULL, 0);

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '通知内容',
  `notice_type` tinyint NULL DEFAULT 1 COMMENT '通知类型(1通知 2公告)',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态(0草稿 1发布)',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建者ID',
  `create_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者名称',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '删除标识',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_notice_type`(`notice_type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统通知表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '欢迎使用Mars系统', '欢迎使用Mars后台管理系统！这是一条测试通知。', 1, 1, 1, '超级管理员', '2026-01-30 23:53:55', '2026-01-30 23:53:55', 0);

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模块标题',
  `business_type` int NULL DEFAULT 0 COMMENT '业务类型(0-其它 1-新增 2-修改 3-删除)',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求方式',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人员',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主机地址',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '返回参数',
  `status` int NULL DEFAULT 0 COMMENT '操作状态(0-正常 1-异常)',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint NULL DEFAULT 0 COMMENT '消耗时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 104 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO `sys_oper_log` VALUES (2, '用户管理', 1, 'com.mars.admin.controller.SysUserController.create()', 'POST', 'admin', '/sys/user', '127.0.0.1', '{\"user\":{\"id\":2,\"createTime\":\"2026-01-29T23:21:12.4730368\",\"updateTime\":\"2026-01-29T23:21:12.4730368\",\"createBy\":1,\"updateBy\":1,\"deleted\":null,\"username\":\"test\",\"password\":\"$2a$10$kTn0Z9BPDnOAU1qB.sJrF.unLh4bbj9FQ7tVsG4AtSBQXFs1V/ewq\",\"nickname\":\"mars\",\"avatar\":null,\"email\":\"111@qq.com\",\"phone\":\"1888888888\",\"gender\":1,\"status\":1,\"remark\":\"\"},\"roleIds\":[2]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-29 23:21:12', 0);
INSERT INTO `sys_oper_log` VALUES (3, '字典类型', 1, 'com.mars.admin.controller.SysDictTypeController.create()', 'POST', 'admin', '/sys/dict/type', '127.0.0.1', '{\"id\":4,\"createTime\":\"2026-01-29T23:21:28.6284556\",\"updateTime\":\"2026-01-29T23:21:28.6284556\",\"createBy\":1,\"updateBy\":1,\"deleted\":null,\"dictName\":\"男\",\"dictType\":\"sex\",\"status\":1,\"remark\":\"\"}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-29 23:21:29', 0);
INSERT INTO `sys_oper_log` VALUES (4, '字典数据', 1, 'com.mars.admin.controller.SysDictDataController.create()', 'POST', 'admin', '/sys/dict/data', '127.0.0.1', '{\"id\":8,\"createTime\":\"2026-01-29T23:21:37.720816\",\"updateTime\":\"2026-01-29T23:21:37.720816\",\"createBy\":1,\"updateBy\":1,\"deleted\":null,\"sort\":0,\"dictLabel\":\"sex\",\"dictValue\":\"1\",\"dictType\":\"sex\",\"cssClass\":null,\"listClass\":\"default\",\"isDefault\":0,\"status\":1,\"remark\":\"\"}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-29 23:21:38', 0);
INSERT INTO `sys_oper_log` VALUES (5, '字典数据', 1, 'com.mars.admin.controller.SysDictDataController.create()', 'POST', 'admin', '/sys/dict/data', '127.0.0.1', '{\"id\":9,\"createTime\":\"2026-01-29T23:21:57.5830655\",\"updateTime\":\"2026-01-29T23:21:57.5830655\",\"createBy\":1,\"updateBy\":1,\"deleted\":null,\"sort\":0,\"dictLabel\":\"女\",\"dictValue\":\"0\",\"dictType\":\"sex\",\"cssClass\":null,\"listClass\":\"default\",\"isDefault\":0,\"status\":1,\"remark\":\"\"}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-29 23:21:58', 0);
INSERT INTO `sys_oper_log` VALUES (6, '字典类型', 2, 'com.mars.admin.controller.SysDictTypeController.update()', 'PUT', 'admin', '/sys/dict/type', '127.0.0.1', '{\"id\":4,\"createTime\":\"2026-01-29T23:21:29\",\"updateTime\":\"2026-01-29T23:21:29\",\"createBy\":1,\"updateBy\":1,\"deleted\":0,\"dictName\":\"性别\",\"dictType\":\"sex\",\"status\":1,\"remark\":\"\"}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-29 23:22:16', 0);
INSERT INTO `sys_oper_log` VALUES (7, '岗位管理', 1, 'com.mars.admin.controller.SysPostController.create()', 'POST', 'admin', '/sys/post', '127.0.0.1', '{\"id\":5,\"createTime\":\"2026-01-29T23:22:32.6209502\",\"updateTime\":\"2026-01-29T23:22:32.6209502\",\"createBy\":1,\"updateBy\":1,\"deleted\":null,\"postCode\":\"11\",\"postName\":\"11\",\"sort\":0,\"status\":1,\"remark\":\"\"}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-29 23:22:33', 0);
INSERT INTO `sys_oper_log` VALUES (8, '岗位管理', 3, 'com.mars.admin.controller.SysPostController.delete()', 'DELETE', 'admin', '/sys/post/5', '127.0.0.1', '5', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-29 23:22:36', 0);
INSERT INTO `sys_oper_log` VALUES (9, '系统配置', 0, 'com.mars.admin.controller.SysConfigController.refresh()', 'POST', 'admin', '/sys/config/refresh', '127.0.0.1', NULL, '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-29 23:52:59', 0);
INSERT INTO `sys_oper_log` VALUES (10, '系统配置', 0, 'com.mars.admin.controller.SysConfigController.refresh()', 'POST', 'admin', '/sys/config/refresh', '127.0.0.1', NULL, '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-29 23:53:50', 0);
INSERT INTO `sys_oper_log` VALUES (11, '系统配置', 0, 'com.mars.admin.controller.SysConfigController.refresh()', 'POST', 'admin', '/sys/config/refresh', '127.0.0.1', NULL, '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-29 23:53:51', 0);
INSERT INTO `sys_oper_log` VALUES (12, '系统配置', 0, 'com.mars.admin.controller.SysConfigController.refresh()', 'POST', 'admin', '/sys/config/refresh', '127.0.0.1', NULL, '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-29 23:53:51', 0);
INSERT INTO `sys_oper_log` VALUES (13, '系统配置', 0, 'com.mars.admin.controller.SysConfigController.refresh()', 'POST', 'admin', '/sys/config/refresh', '127.0.0.1', NULL, '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-29 23:53:52', 0);
INSERT INTO `sys_oper_log` VALUES (14, '系统配置', 0, 'com.mars.admin.controller.SysConfigController.refresh()', 'POST', 'admin', '/sys/config/refresh', '127.0.0.1', NULL, '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-29 23:53:53', 0);
INSERT INTO `sys_oper_log` VALUES (15, '系统配置', 0, 'com.mars.admin.controller.SysConfigController.refresh()', 'POST', 'admin', '/sys/config/refresh', '127.0.0.1', NULL, '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-29 23:53:53', 0);
INSERT INTO `sys_oper_log` VALUES (16, '系统配置', 0, 'com.mars.admin.controller.SysConfigController.refresh()', 'POST', 'admin', '/sys/config/refresh', '127.0.0.1', NULL, '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-29 23:53:53', 0);
INSERT INTO `sys_oper_log` VALUES (17, '系统配置', 0, 'com.mars.admin.controller.SysConfigController.refresh()', 'POST', 'admin', '/sys/config/refresh', '127.0.0.1', NULL, '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-29 23:53:53', 0);
INSERT INTO `sys_oper_log` VALUES (18, '系统配置', 0, 'com.mars.admin.controller.SysConfigController.refresh()', 'POST', 'admin', '/sys/config/refresh', '127.0.0.1', NULL, '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-29 23:53:54', 0);
INSERT INTO `sys_oper_log` VALUES (19, '系统配置', 2, 'com.mars.admin.controller.SysConfigController.update()', 'PUT', 'admin', '/sys/config', '127.0.0.1', '{\"id\":9,\"createTime\":\"2026-01-29T23:55:15\",\"updateTime\":\"2026-01-29T23:55:15\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"configName\":\"接口加密开关\",\"configKey\":\"sys.api.encrypt.enabled\",\"configValue\":\"true\",\"configType\":1,\"remark\":\"是否启用RSA接口加密(true/false)\"}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-29 23:55:26', 0);
INSERT INTO `sys_oper_log` VALUES (20, '系统配置', 0, 'com.mars.admin.controller.SysConfigController.refresh()', 'POST', 'admin', '/sys/config/refresh', '127.0.0.1', NULL, '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-29 23:55:26', 0);
INSERT INTO `sys_oper_log` VALUES (21, '系统配置', 2, 'com.mars.admin.controller.SysConfigController.update()', 'PUT', 'admin', '/sys/config', '127.0.0.1', '{\"id\":9,\"createTime\":\"2026-01-29T23:55:15\",\"updateTime\":\"2026-01-29T23:55:15\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"configName\":\"接口加密开关\",\"configKey\":\"sys.api.encrypt.enabled\",\"configValue\":\"false\",\"configType\":1,\"remark\":\"是否启用RSA接口加密(true/false)\"}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-30 23:12:50', 0);
INSERT INTO `sys_oper_log` VALUES (22, '系统配置', 0, 'com.mars.admin.controller.SysConfigController.refresh()', 'POST', 'admin', '/sys/config/refresh', '127.0.0.1', NULL, '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-30 23:12:50', 0);
INSERT INTO `sys_oper_log` VALUES (23, '系统配置', 2, 'com.mars.admin.controller.SysConfigController.update()', 'PUT', 'admin', '/sys/config', '127.0.0.1', '{\"id\":9,\"createTime\":\"2026-01-29T23:55:15\",\"updateTime\":\"2026-01-29T23:55:15\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"configName\":\"接口加密开关\",\"configKey\":\"sys.api.encrypt.enabled\",\"configValue\":\"true\",\"configType\":1,\"remark\":\"是否启用RSA接口加密(true/false)\"}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-30 23:13:07', 0);
INSERT INTO `sys_oper_log` VALUES (24, '系统配置', 0, 'com.mars.admin.controller.SysConfigController.refresh()', 'POST', 'admin', '/sys/config/refresh', '127.0.0.1', NULL, '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-30 23:13:07', 0);
INSERT INTO `sys_oper_log` VALUES (25, '角色管理', 2, 'com.mars.admin.controller.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-30 23:36:54', 0);
INSERT INTO `sys_oper_log` VALUES (26, '角色管理', 2, 'com.mars.admin.controller.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,19,20,21,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,19,20,21,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,19,20,21,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,19,20,21,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,19,20,21,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,19,20,21,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,19,20,21,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,19,20,21,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,19,20,21,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,19,20,21,126,127,128,129,130,131,132,133]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-30 23:40:20', 0);
INSERT INTO `sys_oper_log` VALUES (27, '用户管理', 2, 'com.mars.admin.controller.SysUserController.update()', 'PUT', 'admin', '/sys/user', '127.0.0.1', '{\"user\":{\"id\":2,\"createTime\":\"2026-01-29T23:21:12\",\"updateTime\":\"2026-01-29T23:21:12\",\"createBy\":1,\"updateBy\":1,\"deleted\":0,\"username\":\"test\",\"password\":null,\"nickname\":\"test\",\"avatar\":null,\"email\":\"111@qq.com\",\"phone\":\"1888888888\",\"gender\":1,\"status\":1,\"remark\":\"\"},\"roleIds\":[2]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 00:04:11', 0);
INSERT INTO `sys_oper_log` VALUES (28, '用户管理', 1, 'com.mars.admin.controller.SysUserController.create()', 'POST', 'admin', '/sys/user', '127.0.0.1', '{\"user\":{\"id\":3,\"createTime\":\"2026-01-29T23:21:12\",\"updateTime\":\"2026-01-29T23:21:12\",\"createBy\":1,\"updateBy\":1,\"deleted\":0,\"username\":\"mars\",\"password\":\"$2a$10$goR4f6wAzry8a6jTrWHDGeI7Fiq2SovcXYrVprcoRgC6mCnK1fM4G\",\"nickname\":\"mars\",\"avatar\":null,\"email\":\"1121@qq.com\",\"phone\":\"18888888881\",\"gender\":1,\"status\":1,\"remark\":\"\"},\"roleIds\":[2]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 00:04:32', 0);
INSERT INTO `sys_oper_log` VALUES (29, '上传图片', 1, 'com.mars.admin.controller.SysFileController.uploadImage()', 'POST', 'admin', '/sys/file/upload/image', '127.0.0.1', '', '{\"code\":200,\"message\":\"操作成功\",\"data\":{\"id\":1,\"originalName\":\"FPj0uHDhtvlIgwhfEk0-Lu-bp4Duopj5_GorB8Sxbqe4pKUR4-7HxwbA7VFa8fTK.jpg\",\"fileName\":\"4e01eb2d5b474a4cb9d981c9a38382c7.jpg\",\"filePath\":\"images/2026/01/31/4e01eb2d5b474a4cb9d981c9a38382c7.jpg\",\"url\":\"http://localhost:8080/file/images/2026/01/31/4e01eb2d5b474a4cb9d981c9a38382c7.jpg\",\"fileSize\":823196,\"fileType\":\"image/jpeg\",\"fileSuffix\":\".jpg\",\"storageType\":\"local\",\"bucketName\":null,\"createBy\":\"1\",\"createTime\":\"2026-01-31T00:08:56.7388473\",\"remark\":null}}', 0, NULL, '2026-01-31 00:08:57', 0);
INSERT INTO `sys_oper_log` VALUES (30, '上传图片', 1, 'com.mars.admin.controller.SysFileController.uploadImage()', 'POST', 'admin', '/sys/file/upload/image', '127.0.0.1', '', '{\"code\":200,\"message\":\"操作成功\",\"data\":{\"id\":2,\"originalName\":\"FPj0uHDhtvlIgwhfEk0-Lu-bp4Duopj5_GorB8Sxbqe4pKUR4-7HxwbA7VFa8fTK.jpg\",\"fileName\":\"1f44b8e3286f4ea8b770c51398f15b22.jpg\",\"filePath\":\"images/2026/01/31/1f44b8e3286f4ea8b770c51398f15b22.jpg\",\"url\":\"http://localhost:8080/file/images/2026/01/31/1f44b8e3286f4ea8b770c51398f15b22.jpg\",\"fileSize\":823196,\"fileType\":\"image/jpeg\",\"fileSuffix\":\".jpg\",\"storageType\":\"local\",\"bucketName\":null,\"createBy\":\"1\",\"createTime\":\"2026-01-31T10:11:54.9186654\",\"remark\":null}}', 0, NULL, '2026-01-31 10:11:55', 0);
INSERT INTO `sys_oper_log` VALUES (31, '角色管理', 2, 'com.mars.admin.controller.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":2,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"普通用户\",\"code\":\"user\",\"sort\":2,\"status\":1,\"remark\":\"普通用户角色\"},\"menuIds\":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,126,127,128,129,130,131,132,133,134,135,136,137,138,139]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 10:32:02', 0);
INSERT INTO `sys_oper_log` VALUES (32, '上传图片', 1, 'com.mars.admin.controller.SysFileController.uploadImage()', 'POST', 'admin', '/sys/file/upload/image', '127.0.0.1', '', '{\"code\":200,\"message\":\"操作成功\",\"data\":{\"id\":3,\"originalName\":\"FPj0uHDhtvlIgwhfEk0-Lu-bp4Duopj5_GorB8Sxbqe4pKUR4-7HxwbA7VFa8fTK.jpg\",\"fileName\":\"1b4f6f9062ab400f9259bdac0eb8db10.jpg\",\"filePath\":\"images/2026/01/31/1b4f6f9062ab400f9259bdac0eb8db10.jpg\",\"url\":\"http://localhost:8080/file/images/2026/01/31/1b4f6f9062ab400f9259bdac0eb8db10.jpg\",\"fileSize\":823196,\"fileType\":\"image/jpeg\",\"fileSuffix\":\".jpg\",\"storageType\":\"local\",\"bucketName\":null,\"createBy\":\"1\",\"createTime\":\"2026-01-31T12:26:51.507376\",\"remark\":null}}', 0, NULL, '2026-01-31 12:26:52', 0);
INSERT INTO `sys_oper_log` VALUES (33, '上传图片', 1, 'com.mars.admin.controller.SysFileController.uploadImage()', 'POST', 'admin', '/sys/file/upload/image', '127.0.0.1', '', '{\"code\":200,\"message\":\"操作成功\",\"data\":{\"id\":4,\"originalName\":\"FPj0uHDhtvlIgwhfEk0-Lu-bp4Duopj5_GorB8Sxbqe4pKUR4-7HxwbA7VFa8fTK.jpg\",\"fileName\":\"788f706ff0a548c0a4b744de208e73a5.jpg\",\"filePath\":\"images/2026/01/31/788f706ff0a548c0a4b744de208e73a5.jpg\",\"url\":\"http://localhost:8080/file/images/2026/01/31/788f706ff0a548c0a4b744de208e73a5.jpg\",\"fileSize\":823196,\"fileType\":\"image/jpeg\",\"fileSuffix\":\".jpg\",\"storageType\":\"local\",\"bucketName\":null,\"createBy\":\"1\",\"createTime\":\"2026-01-31T12:27:02.4947877\",\"remark\":null}}', 0, NULL, '2026-01-31 12:27:03', 0);
INSERT INTO `sys_oper_log` VALUES (34, '上传文件', 1, 'com.mars.admin.controller.SysFileController.upload()', 'POST', 'admin', '/sys/file/upload', '127.0.0.1', '', '{\"code\":200,\"message\":\"操作成功\",\"data\":{\"id\":5,\"originalName\":\"FPj0uHDhtvlIgwhfEk0-Lu-bp4Duopj5_GorB8Sxbqe4pKUR4-7HxwbA7VFa8fTK.jpg\",\"fileName\":\"40015680077f4bdf99a934b4c8c4110b.jpg\",\"filePath\":\"2026/01/31/40015680077f4bdf99a934b4c8c4110b.jpg\",\"url\":\"http://localhost:8080/file/2026/01/31/40015680077f4bdf99a934b4c8c4110b.jpg\",\"fileSize\":823196,\"fileType\":\"image/jpeg\",\"fileSuffix\":\".jpg\",\"storageType\":\"local\",\"bucketName\":null,\"createBy\":\"1\",\"createTime\":\"2026-01-31T14:28:03.0908998\",\"remark\":null}}', 0, NULL, '2026-01-31 14:28:03', 0);
INSERT INTO `sys_oper_log` VALUES (35, '上传文件', 1, 'com.mars.admin.controller.SysFileController.upload()', 'POST', 'admin', '/sys/file/upload', '127.0.0.1', '', '{\"code\":200,\"message\":\"操作成功\",\"data\":{\"id\":6,\"originalName\":\"caa41ac5c974e4fa96b1df176aeba849.png\",\"fileName\":\"c04f38976b2d42a3b6607c796a7f932e.png\",\"filePath\":\"2026/01/31/c04f38976b2d42a3b6607c796a7f932e.png\",\"url\":\"http://localhost:8080/file/2026/01/31/c04f38976b2d42a3b6607c796a7f932e.png\",\"fileSize\":16286,\"fileType\":\"image/png\",\"fileSuffix\":\".png\",\"storageType\":\"local\",\"bucketName\":null,\"createBy\":\"1\",\"createTime\":\"2026-01-31T14:28:06.1062097\",\"remark\":null}}', 0, NULL, '2026-01-31 14:28:06', 0);
INSERT INTO `sys_oper_log` VALUES (36, '上传文件', 1, 'com.mars.admin.controller.SysFileController.upload()', 'POST', 'admin', '/sys/file/upload', '127.0.0.1', '', '{\"code\":200,\"message\":\"操作成功\",\"data\":{\"id\":7,\"originalName\":\"img_v3_02t8_80c939c9-3e79-4c59-b10c-1716a88818ag.jpg\",\"fileName\":\"c0bf62e221c0443b86e890f160767edb.jpg\",\"filePath\":\"2026/01/31/c0bf62e221c0443b86e890f160767edb.jpg\",\"url\":\"http://localhost:8080/file/2026/01/31/c0bf62e221c0443b86e890f160767edb.jpg\",\"fileSize\":108824,\"fileType\":\"image/jpeg\",\"fileSuffix\":\".jpg\",\"storageType\":\"local\",\"bucketName\":null,\"createBy\":\"1\",\"createTime\":\"2026-01-31T14:28:08.2984259\",\"remark\":null}}', 0, NULL, '2026-01-31 14:28:08', 0);
INSERT INTO `sys_oper_log` VALUES (37, '删除文件', 3, 'com.mars.admin.controller.SysFileController.delete()', 'DELETE', 'admin', '/sys/file/7', '127.0.0.1', '7', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 14:28:17', 0);
INSERT INTO `sys_oper_log` VALUES (38, '菜单管理', 3, 'com.mars.admin.controller.SysMenuController.delete()', 'DELETE', 'admin', '/sys/menu/131', '127.0.0.1', '131', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 14:43:45', 0);
INSERT INTO `sys_oper_log` VALUES (39, '菜单管理', 3, 'com.mars.admin.controller.SysMenuController.delete()', 'DELETE', 'admin', '/sys/menu/132', '127.0.0.1', '132', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 14:43:47', 0);
INSERT INTO `sys_oper_log` VALUES (40, '菜单管理', 3, 'com.mars.admin.controller.SysMenuController.delete()', 'DELETE', 'admin', '/sys/menu/133', '127.0.0.1', '133', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 14:43:50', 0);
INSERT INTO `sys_oper_log` VALUES (41, '菜单管理', 3, 'com.mars.admin.controller.SysMenuController.delete()', 'DELETE', 'admin', '/sys/menu/130', '127.0.0.1', '130', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 14:43:52', 0);
INSERT INTO `sys_oper_log` VALUES (42, '上传文件', 1, 'com.mars.admin.controller.SysFileController.upload()', 'POST', 'admin', '/sys/file/upload', '127.0.0.1', '', '{\"code\":200,\"message\":\"操作成功\",\"data\":{\"id\":8,\"originalName\":\"FPj0uHDhtvlIgwhfEk0-Lu-bp4Duopj5_GorB8Sxbqe4pKUR4-7HxwbA7VFa8fTK.jpg\",\"fileName\":\"4cd0becad45c44649f88d78000ad9b9d.jpg\",\"filePath\":\"2026/01/31/4cd0becad45c44649f88d78000ad9b9d.jpg\",\"url\":\"http://localhost:8080/file/2026/01/31/4cd0becad45c44649f88d78000ad9b9d.jpg\",\"fileSize\":823196,\"fileType\":\"image/jpeg\",\"fileSuffix\":\".jpg\",\"storageType\":\"local\",\"bucketName\":null,\"createBy\":\"1\",\"createTime\":\"2026-01-31T15:00:46.5929261\",\"remark\":null}}', 0, NULL, '2026-01-31 15:00:47', 0);
INSERT INTO `sys_oper_log` VALUES (43, '删除文件', 3, 'com.mars.admin.controller.SysFileController.delete()', 'DELETE', 'admin', '/sys/file/8', '127.0.0.1', '8', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 15:01:02', 0);
INSERT INTO `sys_oper_log` VALUES (44, '上传文件', 1, 'com.mars.admin.controller.file.SysFileController.upload()', 'POST', 'admin', '/sys/file/upload', '127.0.0.1', '', '{\"code\":200,\"message\":\"操作成功\",\"data\":{\"id\":1,\"originalName\":\"FPj0uHDhtvlIgwhfEk0-Lu-bp4Duopj5_GorB8Sxbqe4pKUR4-7HxwbA7VFa8fTK.jpg\",\"fileName\":\"9f375134efa54ddeb9d19adb5295939f.jpg\",\"filePath\":\"2026/01/31/9f375134efa54ddeb9d19adb5295939f.jpg\",\"url\":\"http://localhost:8080/files/2026/01/31/9f375134efa54ddeb9d19adb5295939f.jpg\",\"fileSize\":823196,\"fileType\":\"image/jpeg\",\"fileSuffix\":\".jpg\",\"storageType\":\"local\",\"bucketName\":null,\"createBy\":\"1\",\"createTime\":\"2026-01-31T16:50:36.1890809\",\"remark\":null}}', 0, NULL, '2026-01-31 16:50:36', 0);
INSERT INTO `sys_oper_log` VALUES (45, '菜单管理', 1, 'com.mars.admin.controller.system.SysMenuController.create()', 'POST', 'admin', '/sys/menu', '127.0.0.1', '{\"id\":140,\"createTime\":\"2026-01-31T20:17:37.2734227\",\"updateTime\":\"2026-01-31T20:17:37.2734227\",\"createBy\":1,\"updateBy\":1,\"deleted\":null,\"parentId\":0,\"name\":\"测试菜单\",\"type\":1,\"path\":\"/test\",\"component\":\"\",\"permission\":\"\",\"icon\":\"StarOutline\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":null,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:17:37', 0);
INSERT INTO `sys_oper_log` VALUES (46, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:17:50', 0);
INSERT INTO `sys_oper_log` VALUES (47, '菜单管理', 1, 'com.mars.admin.controller.system.SysMenuController.create()', 'POST', 'admin', '/sys/menu', '127.0.0.1', '{\"id\":141,\"createTime\":\"2026-01-31T20:23:37.2535381\",\"updateTime\":\"2026-01-31T20:23:37.2535381\",\"createBy\":1,\"updateBy\":1,\"deleted\":null,\"parentId\":140,\"name\":\"测试菜单\",\"type\":2,\"path\":\"/test\",\"component\":\"/test\",\"permission\":\"\",\"icon\":\"SearchOutline\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":null,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:23:37', 0);
INSERT INTO `sys_oper_log` VALUES (48, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:24:22', 0);
INSERT INTO `sys_oper_log` VALUES (49, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:35:26', 0);
INSERT INTO `sys_oper_log` VALUES (50, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:39:47', 0);
INSERT INTO `sys_oper_log` VALUES (51, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:40:08', 0);
INSERT INTO `sys_oper_log` VALUES (52, '菜单管理', 2, 'com.mars.admin.controller.system.SysMenuController.update()', 'PUT', 'admin', '/sys/menu', '127.0.0.1', '{\"id\":141,\"createTime\":\"2026-01-31T20:23:37\",\"updateTime\":\"2026-01-31T20:23:37\",\"createBy\":1,\"updateBy\":1,\"deleted\":0,\"parentId\":140,\"name\":\"测试菜单\",\"type\":2,\"path\":\"/test\",\"component\":\"/test/test\",\"permission\":\"\",\"icon\":\"SearchOutline\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":0,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:40:35', 0);
INSERT INTO `sys_oper_log` VALUES (53, '菜单管理', 2, 'com.mars.admin.controller.system.SysMenuController.update()', 'PUT', 'admin', '/sys/menu', '127.0.0.1', '{\"id\":141,\"createTime\":\"2026-01-31T20:23:37\",\"updateTime\":\"2026-01-31T20:23:37\",\"createBy\":1,\"updateBy\":1,\"deleted\":0,\"parentId\":140,\"name\":\"测试菜单\",\"type\":2,\"path\":\"/test/test\",\"component\":\"/test/test\",\"permission\":\"\",\"icon\":\"SearchOutline\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":0,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:41:14', 0);
INSERT INTO `sys_oper_log` VALUES (54, '菜单管理', 2, 'com.mars.admin.controller.system.SysMenuController.update()', 'PUT', 'admin', '/sys/menu', '127.0.0.1', '{\"id\":141,\"createTime\":\"2026-01-31T20:23:37\",\"updateTime\":\"2026-01-31T20:23:37\",\"createBy\":1,\"updateBy\":1,\"deleted\":0,\"parentId\":140,\"name\":\"测试菜单\",\"type\":2,\"path\":\"/test/test\",\"component\":\"/test/test/index\",\"permission\":\"\",\"icon\":\"SearchOutline\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":0,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:42:14', 0);
INSERT INTO `sys_oper_log` VALUES (55, '菜单管理', 2, 'com.mars.admin.controller.system.SysMenuController.update()', 'PUT', 'admin', '/sys/menu', '127.0.0.1', '{\"id\":140,\"createTime\":\"2026-01-31T20:17:37\",\"updateTime\":\"2026-01-31T20:17:37\",\"createBy\":1,\"updateBy\":1,\"deleted\":0,\"parentId\":0,\"name\":\"测试菜单\",\"type\":1,\"path\":\"/test\",\"component\":\"\",\"permission\":\"\",\"icon\":\"StarOutline\",\"sort\":7,\"visible\":1,\"status\":1,\"isFrame\":0,\"children\":[{\"id\":141,\"createTime\":\"2026-01-31T20:23:37\",\"updateTime\":\"2026-01-31T20:23:37\",\"createBy\":1,\"updateBy\":1,\"deleted\":0,\"parentId\":140,\"name\":\"测试菜单\",\"type\":2,\"path\":\"/test/test\",\"component\":\"/test/test/index\",\"permission\":\"\",\"icon\":\"SearchOutline\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":0,\"children\":null}]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:45:25', 0);
INSERT INTO `sys_oper_log` VALUES (56, '菜单管理', 1, 'com.mars.admin.controller.system.SysMenuController.create()', 'POST', 'lisi', '/sys/menu', '127.0.0.1', '{\"id\":142,\"createTime\":\"2026-01-31T20:51:50.0831627\",\"updateTime\":\"2026-01-31T20:51:50.0841601\",\"createBy\":4,\"updateBy\":4,\"deleted\":null,\"parentId\":2,\"name\":\"用户列表\",\"type\":3,\"path\":\"\",\"component\":\"\",\"permission\":\"sys:user:list\",\"icon\":\"\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":null,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:51:50', 0);
INSERT INTO `sys_oper_log` VALUES (57, '菜单管理', 1, 'com.mars.admin.controller.system.SysMenuController.create()', 'POST', 'lisi', '/sys/menu', '127.0.0.1', '{\"id\":143,\"createTime\":\"2026-01-31T20:52:12.9077313\",\"updateTime\":\"2026-01-31T20:52:12.9077313\",\"createBy\":4,\"updateBy\":4,\"deleted\":null,\"parentId\":6,\"name\":\"角色列表\",\"type\":3,\"path\":\"\",\"component\":\"\",\"permission\":\"sys:role:list\",\"icon\":\"\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":null,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:52:13', 0);
INSERT INTO `sys_oper_log` VALUES (58, '菜单管理', 1, 'com.mars.admin.controller.system.SysMenuController.create()', 'POST', 'lisi', '/sys/menu', '127.0.0.1', '{\"id\":144,\"createTime\":\"2026-01-31T20:52:31.5487596\",\"updateTime\":\"2026-01-31T20:52:31.5487596\",\"createBy\":4,\"updateBy\":4,\"deleted\":null,\"parentId\":10,\"name\":\"菜单列表\",\"type\":3,\"path\":\"\",\"component\":\"\",\"permission\":\"sys:menu:list\",\"icon\":\"\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":null,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:52:32', 0);
INSERT INTO `sys_oper_log` VALUES (59, '菜单管理', 1, 'com.mars.admin.controller.system.SysMenuController.create()', 'POST', 'lisi', '/sys/menu', '127.0.0.1', '{\"id\":145,\"createTime\":\"2026-01-31T20:52:51.9327195\",\"updateTime\":\"2026-01-31T20:52:51.9327195\",\"createBy\":4,\"updateBy\":4,\"deleted\":null,\"parentId\":14,\"name\":\"字典列表\",\"type\":3,\"path\":\"\",\"component\":\"\",\"permission\":\"sys:dict:list\",\"icon\":\"\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":null,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:52:52', 0);
INSERT INTO `sys_oper_log` VALUES (60, '菜单管理', 1, 'com.mars.admin.controller.system.SysMenuController.create()', 'POST', 'lisi', '/sys/menu', '127.0.0.1', '{\"id\":146,\"createTime\":\"2026-01-31T20:54:25.6538971\",\"updateTime\":\"2026-01-31T20:54:25.6538971\",\"createBy\":4,\"updateBy\":4,\"deleted\":null,\"parentId\":23,\"name\":\"部门列表\",\"type\":3,\"path\":\"\",\"component\":\"\",\"permission\":\"sys:dept:list\",\"icon\":\"\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":null,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:54:26', 0);
INSERT INTO `sys_oper_log` VALUES (61, '菜单管理', 1, 'com.mars.admin.controller.system.SysMenuController.create()', 'POST', 'lisi', '/sys/menu', '127.0.0.1', '{\"id\":147,\"createTime\":\"2026-01-31T20:54:44.7654349\",\"updateTime\":\"2026-01-31T20:54:44.7654349\",\"createBy\":4,\"updateBy\":4,\"deleted\":null,\"parentId\":27,\"name\":\"岗位列表\",\"type\":3,\"path\":\"\",\"component\":\"\",\"permission\":\"sys:post:list\",\"icon\":\"\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":null,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:54:45', 0);
INSERT INTO `sys_oper_log` VALUES (62, '菜单管理', 1, 'com.mars.admin.controller.system.SysMenuController.create()', 'POST', 'lisi', '/sys/menu', '127.0.0.1', '{\"id\":148,\"createTime\":\"2026-01-31T20:55:13.9316756\",\"updateTime\":\"2026-01-31T20:55:13.9316756\",\"createBy\":4,\"updateBy\":4,\"deleted\":null,\"parentId\":32,\"name\":\"查询日志\",\"type\":3,\"path\":\"\",\"component\":\"\",\"permission\":\"monitor:operlog:list\",\"icon\":\"\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":null,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:55:14', 0);
INSERT INTO `sys_oper_log` VALUES (63, '菜单管理', 1, 'com.mars.admin.controller.system.SysMenuController.create()', 'POST', 'lisi', '/sys/menu', '127.0.0.1', '{\"id\":149,\"createTime\":\"2026-01-31T20:55:31.7721299\",\"updateTime\":\"2026-01-31T20:55:31.7721299\",\"createBy\":4,\"updateBy\":4,\"deleted\":null,\"parentId\":34,\"name\":\"日志列表\",\"type\":3,\"path\":\"\",\"component\":\"\",\"permission\":\"monitor:loginlog:list\",\"icon\":\"\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":null,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:55:32', 0);
INSERT INTO `sys_oper_log` VALUES (64, '菜单管理', 1, 'com.mars.admin.controller.system.SysMenuController.create()', 'POST', 'lisi', '/sys/menu', '127.0.0.1', '{\"id\":150,\"createTime\":\"2026-01-31T20:56:04.216496\",\"updateTime\":\"2026-01-31T20:56:04.216496\",\"createBy\":4,\"updateBy\":4,\"deleted\":null,\"parentId\":37,\"name\":\"在线用户列表\",\"type\":3,\"path\":\"\",\"component\":\"\",\"permission\":\"monitor:online:list\",\"icon\":\"\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":null,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:56:04', 0);
INSERT INTO `sys_oper_log` VALUES (65, '菜单管理', 1, 'com.mars.admin.controller.system.SysMenuController.create()', 'POST', 'lisi', '/sys/menu', '127.0.0.1', '{\"id\":151,\"createTime\":\"2026-01-31T20:56:20.3485179\",\"updateTime\":\"2026-01-31T20:56:20.3485179\",\"createBy\":4,\"updateBy\":4,\"deleted\":null,\"parentId\":39,\"name\":\"任务列表\",\"type\":3,\"path\":\"\",\"component\":\"\",\"permission\":\"monitor:job:list\",\"icon\":\"\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":null,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:56:20', 0);
INSERT INTO `sys_oper_log` VALUES (66, '菜单管理', 1, 'com.mars.admin.controller.system.SysMenuController.create()', 'POST', 'admin', '/sys/menu', '127.0.0.1', '{\"id\":152,\"createTime\":\"2026-01-31T20:57:18.2372336\",\"updateTime\":\"2026-01-31T20:57:18.2372336\",\"createBy\":1,\"updateBy\":1,\"deleted\":null,\"parentId\":127,\"name\":\"文件列表\",\"type\":3,\"path\":\"\",\"component\":\"\",\"permission\":\"sys:file:list\",\"icon\":\"\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":null,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:57:18', 0);
INSERT INTO `sys_oper_log` VALUES (67, '菜单管理', 1, 'com.mars.admin.controller.system.SysMenuController.create()', 'POST', 'admin', '/sys/menu', '127.0.0.1', '{\"id\":153,\"createTime\":\"2026-01-31T20:57:55.1807857\",\"updateTime\":\"2026-01-31T20:57:55.1807857\",\"createBy\":1,\"updateBy\":1,\"deleted\":null,\"parentId\":135,\"name\":\"系统通知\",\"type\":3,\"path\":\"\",\"component\":\"\",\"permission\":\"sys:notice:list\",\"icon\":\"\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":null,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:57:55', 0);
INSERT INTO `sys_oper_log` VALUES (68, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,146,147,148,149,150,151,152,153]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 20:58:24', 0);
INSERT INTO `sys_oper_log` VALUES (69, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[6,7,8,9,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,146,147,148,149,150,151,152,153,143,1,2,142,3,4,5,10,144,11,12,13,14,145,15,16,17,18,19,20,21]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:01:34', 0);
INSERT INTO `sys_oper_log` VALUES (70, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[6,7,8,9,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,148,149,150,151,152,153,143,1,2,142,3,4,5,10,144,11,12,13,14,145,15,16,17,18,19,20,21]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:01:44', 0);
INSERT INTO `sys_oper_log` VALUES (71, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[6,7,8,9,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,148,149,150,151,152,153,143,1,2,142,3,4,5,10,144,11,12,13,14,145,15,16,17,18,19,20,21,22,23,146,24,25,26,27,147,28,29,30]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:01:52', 0);
INSERT INTO `sys_oper_log` VALUES (72, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[6,7,8,9,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,150,151,152,153,143,1,2,142,3,4,5,10,144,11,12,13,14,145,15,16,17,18,19,20,21,22,23,146,24,25,26,27,147,28,29,30]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:01:56', 0);
INSERT INTO `sys_oper_log` VALUES (73, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[6,7,8,9,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,150,151,152,153,143,1,2,142,3,4,5,10,144,11,12,13,14,145,15,16,17,18,19,20,21,22,23,146,24,25,26,27,147,28,29,30,31,32,148,33,34,149,35]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:02:00', 0);
INSERT INTO `sys_oper_log` VALUES (74, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[6,7,8,9,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,152,153,143,1,2,142,3,4,5,10,144,11,12,13,14,145,15,16,17,18,19,20,21,22,23,146,24,25,26,27,147,28,29,30,31,32,148,33,34,149,35]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:02:04', 0);
INSERT INTO `sys_oper_log` VALUES (75, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[6,7,8,9,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,152,153,143,1,2,142,3,4,5,10,144,11,12,13,14,145,15,16,17,18,19,20,21,22,23,146,24,25,26,27,147,28,29,30,31,32,148,33,34,149,35,36,37,150,38,39,151,40,41,42,43,44,45]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:02:08', 0);
INSERT INTO `sys_oper_log` VALUES (76, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[6,7,8,9,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,130,131,132,133,134,135,136,137,138,139,140,141,153,143,1,2,142,3,4,5,10,144,11,12,13,14,145,15,16,17,18,19,20,21,22,23,146,24,25,26,27,147,28,29,30,31,32,148,33,34,149,35,36,37,150,38,39,151,40,41,42,43,44,45]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:02:12', 0);
INSERT INTO `sys_oper_log` VALUES (77, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[6,7,8,9,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,130,131,132,133,134,135,136,137,138,139,140,141,153,143,1,2,142,3,4,5,10,144,11,12,13,14,145,15,16,17,18,19,20,21,22,23,146,24,25,26,27,147,28,29,30,31,32,148,33,34,149,35,36,37,150,38,39,151,40,41,42,43,44,45,126,127,152,128,129]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:02:15', 0);
INSERT INTO `sys_oper_log` VALUES (78, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[6,7,8,9,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,130,131,132,133,140,141,143,1,2,142,3,4,5,10,144,11,12,13,14,145,15,16,17,18,19,20,21,22,23,146,24,25,26,27,147,28,29,30,31,32,148,33,34,149,35,36,37,150,38,39,151,40,41,42,43,44,45,126,127,152,128,129]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:02:20', 0);
INSERT INTO `sys_oper_log` VALUES (79, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[6,7,8,9,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,130,131,132,133,140,141,143,1,2,142,3,4,5,10,144,11,12,13,14,145,15,16,17,18,19,20,21,22,23,146,24,25,26,27,147,28,29,30,31,32,148,33,34,149,35,36,37,150,38,39,151,40,41,42,43,44,45,126,127,152,128,129,134,135,153,136,137,138,139]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:02:23', 0);
INSERT INTO `sys_oper_log` VALUES (80, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[6,7,8,9,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,130,131,132,133,143,1,2,142,3,4,5,10,144,11,12,13,14,145,15,16,17,18,19,20,21,22,23,146,24,25,26,27,147,28,29,30,31,32,148,33,34,149,35,36,37,150,38,39,151,40,41,42,43,44,45,126,127,152,128,129,134,135,153,136,137,138,139]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:02:26', 0);
INSERT INTO `sys_oper_log` VALUES (81, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[6,7,8,9,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,130,131,132,133,143,1,2,142,3,4,5,10,144,11,12,13,14,145,15,16,17,18,19,20,21,22,23,146,24,25,26,27,147,28,29,30,31,32,148,33,34,149,35,36,37,150,38,39,151,40,41,42,43,44,45,126,127,152,128,129,134,135,153,136,137,138,139,140,141]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:02:29', 0);
INSERT INTO `sys_oper_log` VALUES (82, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":2,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"普通用户\",\"code\":\"user\",\"sort\":2,\"status\":1,\"remark\":\"普通用户角色\"},\"menuIds\":[18,19,20,21,43,44,45,130,131,132,133,139,142,143,144,145,146,147,148,149,150,151,152,153,128]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:03:45', 0);
INSERT INTO `sys_oper_log` VALUES (83, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":2,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"普通用户\",\"code\":\"user\",\"sort\":2,\"status\":1,\"remark\":\"普通用户角色\"},\"menuIds\":[18,19,20,21,43,44,45,130,131,132,133,139,142,143,144,145,146,147,148,149,150,151,152,153,128,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:08:30', 0);
INSERT INTO `sys_oper_log` VALUES (84, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":2,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"普通用户\",\"code\":\"user\",\"sort\":2,\"status\":1,\"remark\":\"普通用户角色\"},\"menuIds\":[18,19,20,21,43,44,45,130,131,132,133,139,143,144,145,146,147,148,149,150,151,152,153,128,6,7,8,9,10,11,12,13,14,15,16,17,142]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:08:40', 0);
INSERT INTO `sys_oper_log` VALUES (85, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":2,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"普通用户\",\"code\":\"user\",\"sort\":2,\"status\":1,\"remark\":\"普通用户角色\"},\"menuIds\":[18,19,20,21,43,44,45,130,131,132,133,139,143,144,145,146,147,148,149,150,151,152,153,128,6,7,8,9,10,11,12,13,14,15,16,17,142,2,3,4,5,1,23,27,32,34,37,39,127,135,22,31,36,126,134]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:11:10', 0);
INSERT INTO `sys_oper_log` VALUES (86, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":2,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"普通用户\",\"code\":\"user\",\"sort\":2,\"status\":1,\"remark\":\"普通用户角色\"},\"menuIds\":[18,19,20,21,43,44,45,130,131,132,133,139,143,144,145,146,147,148,149,150,151,152,153,128,6,7,8,9,10,11,12,13,14,15,16,17,23,27,32,34,37,39,127,135,22,31,36,126,134,24,25,26,28,29,30,33,35,38,40,41,42,129,136,137,138,142,2,1]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:11:30', 0);
INSERT INTO `sys_oper_log` VALUES (87, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":2,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"普通用户\",\"code\":\"user\",\"sort\":2,\"status\":1,\"remark\":\"普通用户角色\"},\"menuIds\":[18,19,20,21,43,44,45,130,131,132,133,139,143,144,145,146,147,148,149,150,151,152,153,128,6,7,8,9,10,11,12,13,14,15,16,17,23,27,32,34,37,39,127,135,22,31,36,126,134,24,25,26,28,29,30,33,35,38,40,41,42,129,136,137,138,142,2,1]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:11:57', 0);
INSERT INTO `sys_oper_log` VALUES (88, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":2,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"普通用户\",\"code\":\"user\",\"sort\":2,\"status\":1,\"remark\":\"普通用户角色\"},\"menuIds\":[18,19,20,21,43,44,45,130,131,132,133,139,143,144,145,146,147,148,149,150,151,152,153,128,6,7,8,9,10,11,12,13,14,15,16,17,23,27,32,34,37,39,127,135,22,31,36,126,134,24,25,26,28,29,30,33,35,38,40,41,42,129,136,137,138,142]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:17:02', 0);
INSERT INTO `sys_oper_log` VALUES (89, '菜单管理', 2, 'com.mars.admin.controller.system.SysMenuController.update()', 'PUT', 'admin', '/sys/menu', '127.0.0.1', '{\"id\":142,\"createTime\":\"2026-01-31T20:51:50\",\"updateTime\":\"2026-01-31T20:51:50\",\"createBy\":4,\"updateBy\":4,\"deleted\":0,\"parentId\":2,\"name\":\"用户列表\",\"type\":2,\"path\":\"\",\"component\":\"\",\"permission\":\"sys:user:list\",\"icon\":\"\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":0,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:20:25', 0);
INSERT INTO `sys_oper_log` VALUES (90, '菜单管理', 2, 'com.mars.admin.controller.system.SysMenuController.update()', 'PUT', 'admin', '/sys/menu', '127.0.0.1', '{\"id\":143,\"createTime\":\"2026-01-31T20:52:13\",\"updateTime\":\"2026-01-31T20:52:13\",\"createBy\":4,\"updateBy\":4,\"deleted\":0,\"parentId\":6,\"name\":\"角色列表\",\"type\":2,\"path\":\"\",\"component\":\"\",\"permission\":\"sys:role:list\",\"icon\":\"\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":0,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:20:36', 0);
INSERT INTO `sys_oper_log` VALUES (91, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":2,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"普通用户\",\"code\":\"user\",\"sort\":2,\"status\":1,\"remark\":\"普通用户角色\"},\"menuIds\":[18,19,20,21,43,44,45,130,131,132,133,139,143,144,145,146,147,148,149,150,151,152,153,128,6,7,8,9,10,11,12,13,14,15,16,17,23,27,32,34,37,39,127,135,22,31,36,126,134,24,25,26,28,29,30,33,35,38,40,41,42,129,136,137,138,142]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:20:46', 0);
INSERT INTO `sys_oper_log` VALUES (92, '菜单管理', 2, 'com.mars.admin.controller.system.SysMenuController.update()', 'PUT', 'admin', '/sys/menu', '127.0.0.1', '{\"id\":142,\"createTime\":\"2026-01-31T20:51:50\",\"updateTime\":\"2026-01-31T20:51:50\",\"createBy\":4,\"updateBy\":4,\"deleted\":0,\"parentId\":2,\"name\":\"用户列表\",\"type\":3,\"path\":\"\",\"component\":\"\",\"permission\":\"sys:user:list\",\"icon\":\"\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":0,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:21:24', 0);
INSERT INTO `sys_oper_log` VALUES (93, '菜单管理', 2, 'com.mars.admin.controller.system.SysMenuController.update()', 'PUT', 'admin', '/sys/menu', '127.0.0.1', '{\"id\":143,\"createTime\":\"2026-01-31T20:52:13\",\"updateTime\":\"2026-01-31T20:52:13\",\"createBy\":4,\"updateBy\":4,\"deleted\":0,\"parentId\":6,\"name\":\"角色列表\",\"type\":3,\"path\":\"\",\"component\":\"\",\"permission\":\"sys:role:list\",\"icon\":\"\",\"sort\":0,\"visible\":1,\"status\":1,\"isFrame\":0,\"children\":null}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:21:34', 0);
INSERT INTO `sys_oper_log` VALUES (94, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":2,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"普通用户\",\"code\":\"user\",\"sort\":2,\"status\":1,\"remark\":\"普通用户角色\"},\"menuIds\":[18,19,20,21,43,44,45,130,131,132,133,139,143,144,145,146,147,148,149,150,151,152,153,128,6,7,8,9,10,11,12,13,14,15,16,17,23,27,32,34,37,39,127,135,22,31,36,126,134,24,25,26,28,29,30,33,35,38,40,41,42,129,136,137,138,142]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:25:14', 0);
INSERT INTO `sys_oper_log` VALUES (95, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":2,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"普通用户\",\"code\":\"user\",\"sort\":2,\"status\":1,\"remark\":\"普通用户角色\"},\"menuIds\":[18,19,20,21,43,44,45,130,131,132,133,139,143,144,145,146,147,148,149,150,151,152,153,128,6,7,8,9,10,11,12,13,14,15,16,17,23,27,32,34,37,39,127,135,22,31,36,126,134,24,25,26,28,29,30,33,35,38,40,41,42,129,136,137,138,142,2,3,4,5,1]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:25:35', 0);
INSERT INTO `sys_oper_log` VALUES (96, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":2,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"普通用户\",\"code\":\"user\",\"sort\":2,\"status\":1,\"remark\":\"普通用户角色\"},\"menuIds\":[18,19,20,21,43,44,45,130,131,132,133,139,143,144,145,146,147,148,149,150,151,152,153,128,6,7,8,9,10,11,12,13,14,15,16,17,23,27,32,34,37,39,127,135,22,31,36,126,134,24,25,26,28,29,30,33,35,38,40,41,42,129,136,137,138,142]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:26:16', 0);
INSERT INTO `sys_oper_log` VALUES (97, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":2,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"普通用户\",\"code\":\"user\",\"sort\":2,\"status\":1,\"remark\":\"普通用户角色\"},\"menuIds\":[19,20,21,44,45,139,143,144,145,146,147,148,149,150,151,152,153,128,7,8,9,11,12,13,15,16,17,24,25,26,28,29,30,33,35,38,40,41,42,129,136,137,138,142,6,10,14,18,23,27,32,34,37,39,43,127,135,22,31,36,126,134,1,2,3,4,5]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:31:45', 0);
INSERT INTO `sys_oper_log` VALUES (98, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":2,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"普通用户\",\"code\":\"user\",\"sort\":2,\"status\":1,\"remark\":\"普通用户角色\"},\"menuIds\":[19,20,21,44,45,139,143,144,145,146,147,148,149,150,151,152,153,128,7,8,9,11,12,13,15,16,17,24,25,26,28,29,30,33,35,38,40,41,42,129,136,137,138,142,6,10,14,18,23,27,32,34,37,39,43,127,135,22,31,36,126,134,2,1]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:31:57', 0);
INSERT INTO `sys_oper_log` VALUES (99, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":2,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"普通用户\",\"code\":\"user\",\"sort\":2,\"status\":1,\"remark\":\"普通用户角色\"},\"menuIds\":[19,20,21,44,45,139,143,144,145,146,147,148,149,150,151,152,153,128,11,12,13,15,16,17,24,25,26,28,29,30,33,35,38,40,41,42,129,136,137,138,142,10,14,18,23,27,32,34,37,39,43,127,135,22,31,36,126,134,2,6,1]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:32:39', 0);
INSERT INTO `sys_oper_log` VALUES (100, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":2,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"普通用户\",\"code\":\"user\",\"sort\":2,\"status\":1,\"remark\":\"普通用户角色\"},\"menuIds\":[19,20,21,44,45,139,143,144,145,146,147,148,149,150,151,152,153,142,18,43,128,2,6,10,14,23,27,32,34,37,39,127,135,1,22,31,36,126,134]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 21:41:49', 0);
INSERT INTO `sys_oper_log` VALUES (101, '用户管理', 2, 'com.mars.admin.controller.system.SysUserController.update()', 'PUT', 'admin', '/sys/user', '127.0.0.1', '{\"user\":{\"id\":4,\"createTime\":\"2026-01-31T20:49:34\",\"updateTime\":\"2026-01-31T20:49:34\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"username\":\"lisi\",\"password\":null,\"nickname\":\"lisi\",\"avatar\":null,\"email\":null,\"phone\":null,\"gender\":0,\"status\":1,\"remark\":null},\"roleIds\":[2]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 22:28:30', 0);
INSERT INTO `sys_oper_log` VALUES (102, '角色管理', 2, 'com.mars.admin.controller.system.SysRoleController.update()', 'PUT', 'admin', '/sys/role', '127.0.0.1', '{\"role\":{\"id\":1,\"createTime\":\"2026-01-29T22:42:08\",\"updateTime\":\"2026-01-29T22:42:08\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"name\":\"超级管理员\",\"code\":\"admin\",\"sort\":1,\"status\":1,\"remark\":\"拥有所有权限\"},\"menuIds\":[7,8,9,143,142,3,4,5,144,11,12,13,145,15,16,17,19,20,21,146,24,25,26,147,28,29,30,148,33,149,35,150,38,151,40,41,42,44,45,152,128,129,153,136,137,138,139,141,2,6,10,14,18,23,27,32,34,37,39,43,127,135,1,22,31,126,134,140,154,155,156,157,158,159,160,36]}', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 23:42:41', 0);
INSERT INTO `sys_oper_log` VALUES (103, '菜单管理', 2, 'com.mars.admin.controller.system.SysMenuController.update()', 'PUT', 'admin', '/sys/menu', '127.0.0.1', '{\"id\":154,\"createTime\":\"2026-01-31T23:37:21\",\"updateTime\":\"2026-01-31T23:37:21\",\"createBy\":null,\"updateBy\":1,\"deleted\":0,\"parentId\":36,\"name\":\"服务器管理\",\"type\":2,\"path\":\"/monitor/server-manager\",\"component\":\"/monitor/server-manager/index\",\"permission\":null,\"icon\":\"ServerOutline\",\"sort\":5,\"visible\":1,\"status\":1,\"isFrame\":0,\"children\":[{\"id\":155,\"createTime\":\"2026-01-31T23:37:21\",\"updateTime\":\"2026-01-31T23:37:21\",\"createBy\":null,\"updateBy\":null,\"deleted\":0,\"parentId\":154,\"name\":\"服务器列表\",\"type\":3,\"path\":null,\"component\":null,\"permission\":\"monitor:server:list\",\"icon\":null,\"sort\":1,\"visible\":1,\"status\":1,\"isFrame\":0,\"children\":null},{\"id\":156,\"createTime\":\"2026-01-31T23:37:21\",\"updateTime\":\"2026-01-31T23:37:21\",\"createBy\":null,\"updateBy\":null,\"deleted\":0,\"parentId\":154,\"name\":\"服务器详情\",\"type\":3,\"path\":null,\"component\":null,\"permission\":\"monitor:server:query\",\"icon\":null,\"sort\":2,\"visible\":1,\"status\":1,\"isFrame\":0,\"children\":null},{\"id\":157,\"createTime\":\"2026-01-31T23:37:21\",\"updateTime\":\"2026-01-31T23:37:21\",\"createBy\":null,\"updateBy\":null,\"deleted\":0,\"parentId\":154,\"name\":\"新增服务器\",\"type\":3,\"path\":null,\"component\":null,\"permission\":\"monitor:server:add\",\"icon\":null,\"sort\":3,\"visible\":1,\"status\":1,\"isFrame\":0,\"children\":null},{\"id\":158,\"createTime\":\"2026-01-31T23:37:21\",\"updateTime\":\"2026-01-31T23:37:21\",\"createBy\":null,\"updateBy\":null,\"deleted\":0,\"parentId\":154,\"name\":\"编辑服务器\",\"type\":3,\"path\":null,\"component\":null,\"permission\":\"monitor:server:edit\",\"icon\":null,\"sort\":4,\"visible\":1,\"status\":1,\"isFrame\":0,\"children\":null},{\"id\":159,\"createTime\":\"2026-01-31T23:37:21\",\"updateTime\":\"2026-01-31T23:37:21\",\"createBy\":null,\"updateBy\":null,\"deleted\":0,\"parentId\":154,\"name\":\"删除服务器\",\"type\":3,\"path\":null,\"component\":null,\"permission\":\"monitor:server:remove\",\"icon\":null,\"sort\":5,\"visible\":1,\"status\":1,\"isFrame\":0,\"children\":null},{\"id\":160,\"createTime\":\"2026-01-31T23:37:21\",\"updateTime\":\"2026-01-31T23:37:21\",\"createBy\":null,\"updateBy\":null,\"deleted\":0,\"parentId\":154,\"name\":\"测试连接\",\"type\":3,\"pa', '{\"code\":200,\"message\":\"操作成功\",\"data\":null}', 0, NULL, '2026-01-31 23:45:06', 0);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `post_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位名称',
  `sort` int NULL DEFAULT 0 COMMENT '显示顺序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(0-停用 1-正常)',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '删除标识(0-未删除 1-已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_post_code`(`post_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '岗位表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, 1, '公司董事长', '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_post` VALUES (2, 'cto', '技术总监', 2, 1, '技术总监', '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_post` VALUES (3, 'pm', '产品经理', 3, 1, '产品经理', '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_post` VALUES (4, 'dev', '开发工程师', 4, 1, '开发工程师', '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, NULL, 0);
INSERT INTO `sys_post` VALUES (5, '11', '11', 0, 1, '', '2026-01-29 23:22:33', '2026-01-29 23:22:36', 1, 1, 1);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(0-禁用 1-启用)',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '删除标识(0-未删除 1-已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, 1, '拥有所有权限', '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, 1, 0);
INSERT INTO `sys_role` VALUES (2, '普通用户', 'user', 2, 1, '普通用户角色', '2026-01-29 22:42:08', '2026-01-29 22:42:08', NULL, 1, 0);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE,
  INDEX `idx_menu_id`(`menu_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5116 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (5001, 2, 19);
INSERT INTO `sys_role_menu` VALUES (5002, 2, 20);
INSERT INTO `sys_role_menu` VALUES (5003, 2, 21);
INSERT INTO `sys_role_menu` VALUES (5004, 2, 44);
INSERT INTO `sys_role_menu` VALUES (5005, 2, 45);
INSERT INTO `sys_role_menu` VALUES (5006, 2, 139);
INSERT INTO `sys_role_menu` VALUES (5007, 2, 143);
INSERT INTO `sys_role_menu` VALUES (5008, 2, 144);
INSERT INTO `sys_role_menu` VALUES (5009, 2, 145);
INSERT INTO `sys_role_menu` VALUES (5010, 2, 146);
INSERT INTO `sys_role_menu` VALUES (5011, 2, 147);
INSERT INTO `sys_role_menu` VALUES (5012, 2, 148);
INSERT INTO `sys_role_menu` VALUES (5013, 2, 149);
INSERT INTO `sys_role_menu` VALUES (5014, 2, 150);
INSERT INTO `sys_role_menu` VALUES (5015, 2, 151);
INSERT INTO `sys_role_menu` VALUES (5016, 2, 152);
INSERT INTO `sys_role_menu` VALUES (5017, 2, 153);
INSERT INTO `sys_role_menu` VALUES (5018, 2, 142);
INSERT INTO `sys_role_menu` VALUES (5019, 2, 18);
INSERT INTO `sys_role_menu` VALUES (5020, 2, 43);
INSERT INTO `sys_role_menu` VALUES (5021, 2, 128);
INSERT INTO `sys_role_menu` VALUES (5022, 2, 2);
INSERT INTO `sys_role_menu` VALUES (5023, 2, 6);
INSERT INTO `sys_role_menu` VALUES (5024, 2, 10);
INSERT INTO `sys_role_menu` VALUES (5025, 2, 14);
INSERT INTO `sys_role_menu` VALUES (5026, 2, 23);
INSERT INTO `sys_role_menu` VALUES (5027, 2, 27);
INSERT INTO `sys_role_menu` VALUES (5028, 2, 32);
INSERT INTO `sys_role_menu` VALUES (5029, 2, 34);
INSERT INTO `sys_role_menu` VALUES (5030, 2, 37);
INSERT INTO `sys_role_menu` VALUES (5031, 2, 39);
INSERT INTO `sys_role_menu` VALUES (5032, 2, 127);
INSERT INTO `sys_role_menu` VALUES (5033, 2, 135);
INSERT INTO `sys_role_menu` VALUES (5034, 2, 1);
INSERT INTO `sys_role_menu` VALUES (5035, 2, 22);
INSERT INTO `sys_role_menu` VALUES (5036, 2, 31);
INSERT INTO `sys_role_menu` VALUES (5037, 2, 36);
INSERT INTO `sys_role_menu` VALUES (5038, 2, 126);
INSERT INTO `sys_role_menu` VALUES (5039, 2, 134);
INSERT INTO `sys_role_menu` VALUES (5040, 1, 7);
INSERT INTO `sys_role_menu` VALUES (5041, 1, 8);
INSERT INTO `sys_role_menu` VALUES (5042, 1, 9);
INSERT INTO `sys_role_menu` VALUES (5043, 1, 143);
INSERT INTO `sys_role_menu` VALUES (5044, 1, 142);
INSERT INTO `sys_role_menu` VALUES (5045, 1, 3);
INSERT INTO `sys_role_menu` VALUES (5046, 1, 4);
INSERT INTO `sys_role_menu` VALUES (5047, 1, 5);
INSERT INTO `sys_role_menu` VALUES (5048, 1, 144);
INSERT INTO `sys_role_menu` VALUES (5049, 1, 11);
INSERT INTO `sys_role_menu` VALUES (5050, 1, 12);
INSERT INTO `sys_role_menu` VALUES (5051, 1, 13);
INSERT INTO `sys_role_menu` VALUES (5052, 1, 145);
INSERT INTO `sys_role_menu` VALUES (5053, 1, 15);
INSERT INTO `sys_role_menu` VALUES (5054, 1, 16);
INSERT INTO `sys_role_menu` VALUES (5055, 1, 17);
INSERT INTO `sys_role_menu` VALUES (5056, 1, 19);
INSERT INTO `sys_role_menu` VALUES (5057, 1, 20);
INSERT INTO `sys_role_menu` VALUES (5058, 1, 21);
INSERT INTO `sys_role_menu` VALUES (5059, 1, 146);
INSERT INTO `sys_role_menu` VALUES (5060, 1, 24);
INSERT INTO `sys_role_menu` VALUES (5061, 1, 25);
INSERT INTO `sys_role_menu` VALUES (5062, 1, 26);
INSERT INTO `sys_role_menu` VALUES (5063, 1, 147);
INSERT INTO `sys_role_menu` VALUES (5064, 1, 28);
INSERT INTO `sys_role_menu` VALUES (5065, 1, 29);
INSERT INTO `sys_role_menu` VALUES (5066, 1, 30);
INSERT INTO `sys_role_menu` VALUES (5067, 1, 148);
INSERT INTO `sys_role_menu` VALUES (5068, 1, 33);
INSERT INTO `sys_role_menu` VALUES (5069, 1, 149);
INSERT INTO `sys_role_menu` VALUES (5070, 1, 35);
INSERT INTO `sys_role_menu` VALUES (5071, 1, 150);
INSERT INTO `sys_role_menu` VALUES (5072, 1, 38);
INSERT INTO `sys_role_menu` VALUES (5073, 1, 151);
INSERT INTO `sys_role_menu` VALUES (5074, 1, 40);
INSERT INTO `sys_role_menu` VALUES (5075, 1, 41);
INSERT INTO `sys_role_menu` VALUES (5076, 1, 42);
INSERT INTO `sys_role_menu` VALUES (5077, 1, 44);
INSERT INTO `sys_role_menu` VALUES (5078, 1, 45);
INSERT INTO `sys_role_menu` VALUES (5079, 1, 152);
INSERT INTO `sys_role_menu` VALUES (5080, 1, 128);
INSERT INTO `sys_role_menu` VALUES (5081, 1, 129);
INSERT INTO `sys_role_menu` VALUES (5082, 1, 153);
INSERT INTO `sys_role_menu` VALUES (5083, 1, 136);
INSERT INTO `sys_role_menu` VALUES (5084, 1, 137);
INSERT INTO `sys_role_menu` VALUES (5085, 1, 138);
INSERT INTO `sys_role_menu` VALUES (5086, 1, 139);
INSERT INTO `sys_role_menu` VALUES (5087, 1, 141);
INSERT INTO `sys_role_menu` VALUES (5088, 1, 2);
INSERT INTO `sys_role_menu` VALUES (5089, 1, 6);
INSERT INTO `sys_role_menu` VALUES (5090, 1, 10);
INSERT INTO `sys_role_menu` VALUES (5091, 1, 14);
INSERT INTO `sys_role_menu` VALUES (5092, 1, 18);
INSERT INTO `sys_role_menu` VALUES (5093, 1, 23);
INSERT INTO `sys_role_menu` VALUES (5094, 1, 27);
INSERT INTO `sys_role_menu` VALUES (5095, 1, 32);
INSERT INTO `sys_role_menu` VALUES (5096, 1, 34);
INSERT INTO `sys_role_menu` VALUES (5097, 1, 37);
INSERT INTO `sys_role_menu` VALUES (5098, 1, 39);
INSERT INTO `sys_role_menu` VALUES (5099, 1, 43);
INSERT INTO `sys_role_menu` VALUES (5100, 1, 127);
INSERT INTO `sys_role_menu` VALUES (5101, 1, 135);
INSERT INTO `sys_role_menu` VALUES (5102, 1, 1);
INSERT INTO `sys_role_menu` VALUES (5103, 1, 22);
INSERT INTO `sys_role_menu` VALUES (5104, 1, 31);
INSERT INTO `sys_role_menu` VALUES (5105, 1, 126);
INSERT INTO `sys_role_menu` VALUES (5106, 1, 134);
INSERT INTO `sys_role_menu` VALUES (5107, 1, 140);
INSERT INTO `sys_role_menu` VALUES (5108, 1, 154);
INSERT INTO `sys_role_menu` VALUES (5109, 1, 155);
INSERT INTO `sys_role_menu` VALUES (5110, 1, 156);
INSERT INTO `sys_role_menu` VALUES (5111, 1, 157);
INSERT INTO `sys_role_menu` VALUES (5112, 1, 158);
INSERT INTO `sys_role_menu` VALUES (5113, 1, 159);
INSERT INTO `sys_role_menu` VALUES (5114, 1, 160);
INSERT INTO `sys_role_menu` VALUES (5115, 1, 36);

-- ----------------------------
-- Table structure for sys_server
-- ----------------------------
DROP TABLE IF EXISTS `sys_server`;
CREATE TABLE `sys_server`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '服务器名称',
  `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '服务器地址',
  `port` int NOT NULL DEFAULT 22 COMMENT 'SSH端口',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `auth_type` tinyint NOT NULL DEFAULT 1 COMMENT '认证方式：1-密码 2-密钥',
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码（加密存储）',
  `private_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '私钥内容',
  `passphrase` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '私钥密码（加密存储）',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `last_connect_time` datetime NULL DEFAULT NULL COMMENT '最后连接时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除：0-否 1-是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_deleted`(`deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '服务器管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_server
-- ----------------------------
INSERT INTO `sys_server` VALUES (1, '测试服务器', '47.108.187.11', 22, 'root', 1, 'mars6662026@', '', '', '', 1, 0, '2026-01-31 23:53:15', 1, '2026-01-31 23:46:31', 1, '2026-01-31 23:53:15', 0);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `gender` tinyint NULL DEFAULT 0 COMMENT '性别(0-未知 1-男 2-女)',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(0-禁用 1-启用)',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '删除标识(0-未删除 1-已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$NHwBrsRfesK2pSMgG3NjZ.3JiUXd0msi5ib34QAROfTNq5t0UTL6y', '超级管理员', NULL, NULL, NULL, 0, 1, NULL, '2026-01-29 22:42:08', '2026-01-29 22:58:21', NULL, 1, 0);
INSERT INTO `sys_user` VALUES (2, 'test', '$2a$10$kTn0Z9BPDnOAU1qB.sJrF.unLh4bbj9FQ7tVsG4AtSBQXFs1V/ewq', 'test', NULL, '111@qq.com', '1888888888', 1, 1, '', '2026-01-29 23:21:12', '2026-01-29 23:21:12', 1, 1, 0);
INSERT INTO `sys_user` VALUES (3, 'mars', '$2a$10$goR4f6wAzry8a6jTrWHDGeI7Fiq2SovcXYrVprcoRgC6mCnK1fM4G', 'mars', NULL, '1121@qq.com', '18888888881', 1, 1, '', '2026-01-29 23:21:12', '2026-01-29 23:21:12', 1, 1, 0);
INSERT INTO `sys_user` VALUES (4, 'lisi', '$2a$10$4pFtybVAOwePb9T9LsnYU.OJzo7PIOf3ZxU4MOylb03D6MUK/bSb6', 'lisi', NULL, NULL, NULL, 0, 1, NULL, '2026-01-31 20:49:34', '2026-01-31 20:49:34', NULL, 1, 0);
INSERT INTO `sys_user` VALUES (5, 'mars666', '$2a$10$DrZPHQH49c0ywbmlYOmhBeNDOquQlC/VqulezUj5jHlif.jVkTKRO', 'mars666', NULL, NULL, NULL, 0, 1, NULL, '2026-01-31 22:30:46', '2026-01-31 22:30:46', NULL, NULL, 0);

-- ----------------------------
-- Table structure for sys_user_blacklist
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_blacklist`;
CREATE TABLE `sys_user_blacklist`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `blocked_user_id` bigint NOT NULL COMMENT '被拉黑的用户ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '拉黑时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_blocked`(`user_id` ASC, `blocked_user_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_blocked_user_id`(`blocked_user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户黑名单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_blacklist
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_notice`;
CREATE TABLE `sys_user_notice`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `notice_id` bigint NOT NULL COMMENT '通知ID',
  `is_read` tinyint NULL DEFAULT 0 COMMENT '是否已读(0未读 1已读)',
  `read_time` datetime NULL DEFAULT NULL COMMENT '阅读时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_notice`(`user_id` ASC, `notice_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_notice_id`(`notice_id` ASC) USING BTREE,
  INDEX `idx_is_read`(`is_read` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户通知关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_notice
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1);
INSERT INTO `sys_user_role` VALUES (3, 2, 2);
INSERT INTO `sys_user_role` VALUES (4, 3, 2);
INSERT INTO `sys_user_role` VALUES (6, 4, 2);
INSERT INTO `sys_user_role` VALUES (7, 5, 2);

SET FOREIGN_KEY_CHECKS = 1;
