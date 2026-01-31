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

 Date: 31/01/2026 16:29:07
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
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '群聊表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_chat_group
-- ----------------------------
INSERT INTO `sys_chat_group` VALUES (1, '测试', NULL, 2, '1111111111', 200, 0, '2026-01-31 11:24:29', '2026-01-31 12:39:26');
INSERT INTO `sys_chat_group` VALUES (2, '内部沟通群', NULL, 1, NULL, 200, 1, '2026-01-31 12:42:47', '2026-01-31 13:54:49');

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '群成员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_chat_group_member
-- ----------------------------
INSERT INTO `sys_chat_group_member` VALUES (4, 2, 1, NULL, 2, 0, '2026-01-31 12:42:47');
INSERT INTO `sys_chat_group_member` VALUES (5, 2, 2, NULL, 0, 0, '2026-01-31 12:42:47');
INSERT INTO `sys_chat_group_member` VALUES (6, 2, 3, NULL, 0, 0, '2026-01-31 12:42:47');

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
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '群消息表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 342 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '聊天消息表' ROW_FORMAT = Dynamic;

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
INSERT INTO `sys_config_group` VALUES (1, 'system', '系统配置', NULL, '{\"siteName\":\"Mars Admin\",\"siteDescription\":\"现代化企业级管理系统\",\"siteLogo\":\"\",\"copyright\":\"版权所有 © 成都火星网络科技有限公司 2025-2030\",\"icp\":\"\"}', 1, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 14:38:29');
INSERT INTO `sys_config_group` VALUES (2, 'register', '注册配置', NULL, '{\"enabled\":true,\"verifyEmail\":false,\"verifyPhone\":false,\"defaultRole\":\"user\",\"needAudit\":false}', 2, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 14:38:29');
INSERT INTO `sys_config_group` VALUES (3, 'login', '登录配置', NULL, '{\"captchaEnabled\":true,\"captchaType\":\"image\",\"maxRetryCount\":5,\"lockTime\":30,\"rememberMe\":true,\"singleLogin\":false}', 3, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 15:38:39');
INSERT INTO `sys_config_group` VALUES (4, 'password', '密码配置', NULL, '{\"minLength\":6,\"maxLength\":20,\"requireUppercase\":false,\"requireLowercase\":false,\"requireNumber\":false,\"requireSpecial\":false,\"expireDays\":0}', 4, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 14:38:29');
INSERT INTO `sys_config_group` VALUES (5, 'email', '邮件配置', NULL, '{\"host\":\"\",\"port\":465,\"username\":\"\",\"password\":\"\",\"fromName\":\"\",\"ssl\":true,\"enabled\":false}', 5, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 14:38:29');
INSERT INTO `sys_config_group` VALUES (6, 'emailTemplate', '邮件模板', NULL, '{\"verifyCode\":\"您的验证码是：{code}，有效期{expire}分钟。\",\"resetPassword\":\"您正在重置密码，验证码：{code}，有效期{expire}分钟。\",\"welcome\":\"欢迎注册{siteName}，您的账号已创建成功。\"}', 6, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 14:38:29');
INSERT INTO `sys_config_group` VALUES (7, 'sms', '短信配置', NULL, '{\"provider\":\"aliyun\",\"accessKeyId\":\"\",\"accessKeySecret\":\"\",\"signName\":\"\",\"enabled\":false}', 7, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 14:38:29');
INSERT INTO `sys_config_group` VALUES (8, 'smsTemplate', '短信模板', NULL, '{\"verifyCode\":\"SMS_123456789\",\"resetPassword\":\"SMS_123456790\",\"notification\":\"SMS_123456791\"}', 8, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 14:38:29');
INSERT INTO `sys_config_group` VALUES (9, 'storage', '文件配置', NULL, '{\r\n  \"provider\": \"local\",\r\n  \"domain\": \"http://localhost:8080\",\r\n  \"localPath\": \"./uploads\",\r\n  \"maxSize\": 10,\r\n  \"allowTypes\": \"jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx\",\r\n  \"minioEndpoint\": \"\",\r\n  \"minioAccessKey\": \"\",\r\n  \"minioSecretKey\": \"\",\r\n  \"minioBucket\": \"\",\r\n  \"aliyunEndpoint\": \"\",\r\n  \"aliyunAccessKey\": \"\",\r\n  \"aliyunSecretKey\": \"\",\r\n  \"aliyunBucket\": \"\",\r\n  \"tencentSecretId\": \"\",\r\n  \"tencentSecretKey\": \"\",\r\n  \"tencentBucket\": \"\",\r\n  \"tencentRegion\": \"\"\r\n}', 9, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 16:26:34');
INSERT INTO `sys_config_group` VALUES (10, 'push', '推送配置', NULL, '{\r\n  \"enabled\": false,\r\n  \"provider\": \"console\",\r\n  \"appKey\": \"\",\r\n  \"masterSecret\": \"\"\r\n}', 10, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 16:26:34');
INSERT INTO `sys_config_group` VALUES (11, 'thirdParty', '第三方配置', NULL, '{\"wechat\":{\"enabled\":false,\"appId\":\"\",\"appSecret\":\"\"},\"alipay\":{\"enabled\":false,\"appId\":\"\",\"privateKey\":\"\",\"publicKey\":\"\"},\"github\":{\"enabled\":false,\"clientId\":\"\",\"clientSecret\":\"\"}}', 11, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 14:38:29');
INSERT INTO `sys_config_group` VALUES (12, 'payment', '支付配置', NULL, '{\r\n    \"wechatPay\": {\r\n        \"enabled\": true,\r\n        \"mchId\": \"1627500294\",\r\n        \"appId\": \"wxe97894ad8c7ef7e0\",\r\n        \"apiKey\": \"\",\r\n        \"apiV3Key\": \"lxpvkwojpnxafnoutgqowbecdwdsmpwq\",\r\n        \"privateKey\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDlGsA4SciJOYYq\\nTL+/hYlaRLkkJ060c+2MrOl7egozzwddhNLHRC0wasgGdQdbDI39mAm34I7mLdMV\\nlv10dgtKXgpQBHc9QPKy3bPFcgFrz7rxS0YcFrqmzzB69a0LVVfAsZE2SD/4yKc3\\nVFW8cLKQZKRYYm3gZGwN0rsJFVU3dfWgOaoNlBkc5bNIbY7j4aHeW7tJXOQCiig6\\nKj+Dh7r1/POzTciCfqVB1Vjf+VkFMuF6oyKLxMBzFzxvXCGw3PySL6HuY1g5xI7j\\nbNi+xfqtzxZEQAv1QjbfBjzygQXeLCpsuYGVFRRVdyNYxkV90FDVI8swLXpMh65b\\nYNgBGtn1AgMBAAECggEBAKlIx+mPk07aI2mUBkcU+7WofAjbxosN8eP1TBxBw9Ie\\nUnnmj/xPQvi4ng4vYP0E3NIaCmxE0DICgCs+ww7Pvm336LTRZ+3p1KsXqCLnp2cr\\nOh3bGfXdUZO6Gj9w0qlCKTInwn2SizpfwTbf6O3xc++/fbQVHs0kRrc8E5mVmr77\\n01aGIJvXxtQPfdn/R2TMBwqiN8pO5igILlDzNAEusXnfSDOp3rYsXwcnCxJqgnVm\\nydlo7JMU2iqRKSD09qeKFgb+Hbr9aJIQdcvjGBSNmF3MsCFgs/XIb47B4xvy2HBN\\nvIBRwBy08fFeih0GE+0IKr0LyAQ8naMjRTD8A6SbPE0CgYEA9HJ+qigfUPsh/Q+u\\nyyoZeIrsR1xoNVcwANwpWnChsic+B3V/D/pWMJxPv9wKRsVt/dc4kVht//j69tS8\\ny3BFoUxSfUuoK5hdhI8osk3wdVFOnrPPs57s2bMcPPF3Rd5iMvcRNqM1IENCpDAR\\n4zlrEqcMpGSNfaSVhFEyo0fvsV8CgYEA7+6gxxkZJD7DwoVUk8w0BJoq7pNUZc43\\nC0uI8EIRCWxSkd5ahruJjreJuFM1IQUmmqFgewdhEIdUjyORwgVQlo469uwqYPQ5\\n8RWMJcQVK8+QEWV/TdywO3P7oEFgFmVlII/h7Janz/ZlOFZ1X8ANVvpenqgeB2j+\\nl1JMVfjnUSsCgYAHmvRT6PGofFe/XtiKW6H1PSVCxx464p6MuEzVEoIFX/EvHDm6\\nzogV9RcKGhd7wjK83hBVfVHWz/FG8rF5BuIztYMvgMYXrSLjt+yFN6WOkNwIVgHV\\nTdGCqG7tennCg7u8aDFx6LwDZ/RP1WsJDcVGDEp5ZuN8ED3SoxAXQmqzswKBgCk3\\nOtM40oLRbVtq//5ro7vup9VX5bWfWQFNtnZfQwH1Y7G/GpnueVDU4omRcZz8f4cs\\nlaBMwjXOqY31NEK6Gv/h6usj4pvJGHL7mpmaN3DRNRRn9RhxAq0T3XPIBzORs2+G\\nh+7WanllADpPT9Zk7WW1mK90fcQUGzfvYUGbglEFAoGARffpCUANEp1oedOyUVRN\\nSKIvSRggxxqMuzSdnm7eGKDmm+kbA8Iig2C0jgcn4vQZpngbhlNsGrb26Bvdh8wE\\nTBtkcxSBjzsFBdE4kSdVqxnZeVezouWixvkxL4ax1xwczS9hyJlunDljsUb2PkwZ\\nBE39glMdpIqGYrpSTM3p6mI=\\n-----END PRIVATE KEY-----\",\r\n        \"certSerialNo\": \"2FD947564972A8536BDD750944C4796CDF3265EB\",\r\n        \"notifyUrl\": \"http://q668fd96.natappfree.cc/api/pay/notify/wechat\"\r\n    },\r\n    \"alipay\": {\r\n        \"enabled\": true,\r\n        \"appId\": \"2021005192689177\",\r\n        \"privateKey\": \"MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCc1KF+8tqemN/+k99xwIxtfrZ8Py7ZtSv6zoDa0Z9pW1IWpYmz+Zm1wtF3CTX5xApyyBTWQgK8Pb/oEz4u4zKzuwcZFX/IdfP2mWaMHRvWnDLunWqH8rQO+JOaWDvzA68lL02AfubuafQldXY+hdeOpt+t5Kj/OnC+o4z0qTAcM9uTpeX9Z8lttlcW9JtCATRP+klr52bRaOcACgh3MIJrQ3OteeiikvbVtZtw4u3X2h5tdRlCl2/youKO6/iZXGmAmtTGRU8Iy8iBAMI6Ow8K6XH5xHccTzTOx8xv1PZ2IszVvMVhLJDXaUg4DyVbhN8hrmKFmu1i9eBbdSZixkNpAgMBAAECggEAXDjFBqu0VxK6lS9Lc86wRSsAECvvVuIsjH2mVAZ0YTXsHZkWUpjyBGodVow6Czd2lWyGpD+I8Dy3frbiGBxOElZmpB96VtzVqyslnDr5xcdwQ9SZcnwL2cnesiI0joCaG5mnT2pQTd5MTUK3V6jIyv/iBJWzsvIgnln6Z1yeB9ai/3c5Lvu0/ZnhC7trqD73BB1x49E0AV90y0/C/IA+FLEKio9/xjgYweSvTiaYTCBKzQv74Oco54HDtd93rlavZUu7F1qdpOWAj903N1xf8A/fepcL8/qPdSZNoRbPr2NgPMZa70hLvnWDfIXRWoaOZ+lFnPtewI8FAaVX4mI3AQKBgQDO8GxdEunrmRuOXbv/JqTj0dG2lXT7kUvDdJ6QVr3HIsmyxkZXQsp/7QdXh/FdRBNFwkOirmClUqrvYq3CbytgjNdxmYdZQ2A/YXqDdTs8J2Li36hbkOPIFNyMZsjtYF39eosf2oF0/ydRSlMqW5B6jpUh5qCYVWkVtUjLuXaM2QKBgQDCAwMqd6Um9X50dKNIqY1X2ImLiRdLVaqn4/pTwylxxIrRO9f5jF7PnenDci809+Sc+yCcZarvdh1QbUE+YGhYOjj2WGaB9sS2TGDFzOguGs7m7hCIQPa6VEyP2I07kaZcpb+r5GqnT9U47mPRcLJe3zop+w3B7cW5JcdtOSCREQKBgQCbpbALzWcOIoncado2Dk3lYPJ4fy+O6/jtWTDOZb+2IQ9OHN3ZUk5XK+PizUgYm1RXmscefEQK9QPGrBT/cnhQ1X5SXmS0Gf4xjdMFP06/buxsskbCIFeDLVW5cLHeASaQufQckE/gvO1IsjudV2NzGv1Gk13lVhCFGGZZfPSS+QKBgG4y2dSAWx1y6d3p9mkqbW9NPms0djfDNAji9GgpfVvyoErSbA2BzsSs1H/AVtIGUCNefRp4oQwdEe+B70In7nzWrU43zhnZ+cf2QC16AxNVBNqktF1AUSRrB4XZIfeI9m6/csyHFJFuRhVtSuNG2PoMX3RC9oCFtv5AWDNQ9I+RAoGAZF1dQs826kCeptQHXnlgTGNNIX9jLGyfO2qysBOCcqwFIrcJpsb11Q1xLrQmju6EHzr4kAINp32Qd5fo/oCM25JuSiw+fK6CgkAEYjSr/9dD4KpGicHmsib3GyfPj850K2RwFz2RckwX+If/NgI3dIecMTgTJ0tfytaaeqFH4PY=\",\r\n        \"publicKey\": \"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiwci3m6eLWeR1kgfeWm/F0V1e68VWUyRB4N5mhnxryTHqiLeN8ilxN9Kn/Ute1C9cL3b4hfx3NYk7zt60QWP9ly8QJQOlqd1H7XsG16AlEpsIaN1SrMYWq16nAD6uwvMmK0nTdzhuNIKOfdC2YWyv3AJTWh0nCTddYV2D+eSH/Ui6xkfgK8pFn/X1Q0xjXvuZrsXxF+WTk5mymEy2u4Kp7/rD/lClfNAv68kOHe92iKj1VzhtROrSp5//xuvL2PA7FLMqo5olZpBmda3eMWgnvHNwvaJvHJENN2ubANwMPNkwMkQ7MKLCBI33fzEERxJBACrJCc6lo8t+wq3zDo/uwIDAQAB\",\r\n        \"signType\": \"RSA2\",\r\n        \"charset\": \"UTF-8\",\r\n        \"gatewayUrl\": \"https://openapi.alipay.com/gateway.do\",\r\n        \"notifyUrl\": \"http://q668fd96.natappfree.cc/api/pay/notify/alipay\",\r\n        \"returnUrl\": \"\"\r\n    }\r\n}', 12, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 16:13:21');
INSERT INTO `sys_config_group` VALUES (13, 'security', '安全配置', NULL, '{\"encryptEnabled\":false,\"encryptPublicKey\":\"\",\"encryptPrivateKey\":\"\",\"xssFilter\":true,\"sqlInject\":true}', 13, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 14:38:29');
INSERT INTO `sys_config_group` VALUES (14, 'other', '其他配置', NULL, '{}', 14, 1, NULL, '2026-01-31 14:38:29', '2026-01-31 14:38:29');

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file
-- ----------------------------

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
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务日志表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 93 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '登录日志表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 140 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1406 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (817, 1, 1);
INSERT INTO `sys_role_menu` VALUES (818, 1, 2);
INSERT INTO `sys_role_menu` VALUES (819, 1, 3);
INSERT INTO `sys_role_menu` VALUES (820, 1, 4);
INSERT INTO `sys_role_menu` VALUES (821, 1, 5);
INSERT INTO `sys_role_menu` VALUES (822, 1, 6);
INSERT INTO `sys_role_menu` VALUES (823, 1, 7);
INSERT INTO `sys_role_menu` VALUES (824, 1, 8);
INSERT INTO `sys_role_menu` VALUES (825, 1, 9);
INSERT INTO `sys_role_menu` VALUES (826, 1, 10);
INSERT INTO `sys_role_menu` VALUES (827, 1, 11);
INSERT INTO `sys_role_menu` VALUES (828, 1, 12);
INSERT INTO `sys_role_menu` VALUES (829, 1, 13);
INSERT INTO `sys_role_menu` VALUES (830, 1, 14);
INSERT INTO `sys_role_menu` VALUES (831, 1, 15);
INSERT INTO `sys_role_menu` VALUES (832, 1, 16);
INSERT INTO `sys_role_menu` VALUES (833, 1, 17);
INSERT INTO `sys_role_menu` VALUES (834, 1, 18);
INSERT INTO `sys_role_menu` VALUES (835, 1, 19);
INSERT INTO `sys_role_menu` VALUES (836, 1, 20);
INSERT INTO `sys_role_menu` VALUES (837, 1, 21);
INSERT INTO `sys_role_menu` VALUES (838, 1, 22);
INSERT INTO `sys_role_menu` VALUES (839, 1, 23);
INSERT INTO `sys_role_menu` VALUES (840, 1, 24);
INSERT INTO `sys_role_menu` VALUES (841, 1, 25);
INSERT INTO `sys_role_menu` VALUES (842, 1, 26);
INSERT INTO `sys_role_menu` VALUES (843, 1, 27);
INSERT INTO `sys_role_menu` VALUES (844, 1, 28);
INSERT INTO `sys_role_menu` VALUES (845, 1, 29);
INSERT INTO `sys_role_menu` VALUES (846, 1, 30);
INSERT INTO `sys_role_menu` VALUES (847, 1, 31);
INSERT INTO `sys_role_menu` VALUES (848, 1, 32);
INSERT INTO `sys_role_menu` VALUES (849, 1, 33);
INSERT INTO `sys_role_menu` VALUES (850, 1, 34);
INSERT INTO `sys_role_menu` VALUES (851, 1, 35);
INSERT INTO `sys_role_menu` VALUES (852, 1, 36);
INSERT INTO `sys_role_menu` VALUES (853, 1, 37);
INSERT INTO `sys_role_menu` VALUES (854, 1, 38);
INSERT INTO `sys_role_menu` VALUES (855, 1, 39);
INSERT INTO `sys_role_menu` VALUES (856, 1, 40);
INSERT INTO `sys_role_menu` VALUES (857, 1, 41);
INSERT INTO `sys_role_menu` VALUES (858, 1, 42);
INSERT INTO `sys_role_menu` VALUES (859, 1, 43);
INSERT INTO `sys_role_menu` VALUES (860, 1, 44);
INSERT INTO `sys_role_menu` VALUES (861, 1, 45);
INSERT INTO `sys_role_menu` VALUES (862, 1, 46);
INSERT INTO `sys_role_menu` VALUES (863, 1, 47);
INSERT INTO `sys_role_menu` VALUES (864, 1, 48);
INSERT INTO `sys_role_menu` VALUES (865, 1, 49);
INSERT INTO `sys_role_menu` VALUES (866, 1, 50);
INSERT INTO `sys_role_menu` VALUES (867, 1, 51);
INSERT INTO `sys_role_menu` VALUES (868, 1, 52);
INSERT INTO `sys_role_menu` VALUES (869, 1, 53);
INSERT INTO `sys_role_menu` VALUES (870, 1, 19);
INSERT INTO `sys_role_menu` VALUES (871, 1, 20);
INSERT INTO `sys_role_menu` VALUES (872, 1, 21);
INSERT INTO `sys_role_menu` VALUES (873, 1, 46);
INSERT INTO `sys_role_menu` VALUES (874, 1, 47);
INSERT INTO `sys_role_menu` VALUES (875, 1, 48);
INSERT INTO `sys_role_menu` VALUES (876, 1, 49);
INSERT INTO `sys_role_menu` VALUES (877, 1, 50);
INSERT INTO `sys_role_menu` VALUES (878, 1, 51);
INSERT INTO `sys_role_menu` VALUES (879, 1, 52);
INSERT INTO `sys_role_menu` VALUES (880, 1, 53);
INSERT INTO `sys_role_menu` VALUES (881, 1, 54);
INSERT INTO `sys_role_menu` VALUES (882, 1, 55);
INSERT INTO `sys_role_menu` VALUES (883, 1, 56);
INSERT INTO `sys_role_menu` VALUES (884, 1, 57);
INSERT INTO `sys_role_menu` VALUES (885, 1, 58);
INSERT INTO `sys_role_menu` VALUES (886, 1, 59);
INSERT INTO `sys_role_menu` VALUES (887, 1, 60);
INSERT INTO `sys_role_menu` VALUES (888, 1, 61);
INSERT INTO `sys_role_menu` VALUES (889, 1, 19);
INSERT INTO `sys_role_menu` VALUES (890, 1, 20);
INSERT INTO `sys_role_menu` VALUES (891, 1, 21);
INSERT INTO `sys_role_menu` VALUES (892, 1, 46);
INSERT INTO `sys_role_menu` VALUES (893, 1, 47);
INSERT INTO `sys_role_menu` VALUES (894, 1, 48);
INSERT INTO `sys_role_menu` VALUES (895, 1, 49);
INSERT INTO `sys_role_menu` VALUES (896, 1, 50);
INSERT INTO `sys_role_menu` VALUES (897, 1, 51);
INSERT INTO `sys_role_menu` VALUES (898, 1, 52);
INSERT INTO `sys_role_menu` VALUES (899, 1, 53);
INSERT INTO `sys_role_menu` VALUES (900, 1, 54);
INSERT INTO `sys_role_menu` VALUES (901, 1, 55);
INSERT INTO `sys_role_menu` VALUES (902, 1, 56);
INSERT INTO `sys_role_menu` VALUES (903, 1, 57);
INSERT INTO `sys_role_menu` VALUES (904, 1, 58);
INSERT INTO `sys_role_menu` VALUES (905, 1, 59);
INSERT INTO `sys_role_menu` VALUES (906, 1, 60);
INSERT INTO `sys_role_menu` VALUES (907, 1, 61);
INSERT INTO `sys_role_menu` VALUES (908, 1, 62);
INSERT INTO `sys_role_menu` VALUES (909, 1, 63);
INSERT INTO `sys_role_menu` VALUES (910, 1, 64);
INSERT INTO `sys_role_menu` VALUES (911, 1, 65);
INSERT INTO `sys_role_menu` VALUES (912, 1, 66);
INSERT INTO `sys_role_menu` VALUES (913, 1, 67);
INSERT INTO `sys_role_menu` VALUES (914, 1, 68);
INSERT INTO `sys_role_menu` VALUES (915, 1, 69);
INSERT INTO `sys_role_menu` VALUES (916, 1, 19);
INSERT INTO `sys_role_menu` VALUES (917, 1, 20);
INSERT INTO `sys_role_menu` VALUES (918, 1, 21);
INSERT INTO `sys_role_menu` VALUES (919, 1, 46);
INSERT INTO `sys_role_menu` VALUES (920, 1, 47);
INSERT INTO `sys_role_menu` VALUES (921, 1, 48);
INSERT INTO `sys_role_menu` VALUES (922, 1, 49);
INSERT INTO `sys_role_menu` VALUES (923, 1, 50);
INSERT INTO `sys_role_menu` VALUES (924, 1, 51);
INSERT INTO `sys_role_menu` VALUES (925, 1, 52);
INSERT INTO `sys_role_menu` VALUES (926, 1, 53);
INSERT INTO `sys_role_menu` VALUES (927, 1, 54);
INSERT INTO `sys_role_menu` VALUES (928, 1, 55);
INSERT INTO `sys_role_menu` VALUES (929, 1, 56);
INSERT INTO `sys_role_menu` VALUES (930, 1, 57);
INSERT INTO `sys_role_menu` VALUES (931, 1, 58);
INSERT INTO `sys_role_menu` VALUES (932, 1, 59);
INSERT INTO `sys_role_menu` VALUES (933, 1, 60);
INSERT INTO `sys_role_menu` VALUES (934, 1, 61);
INSERT INTO `sys_role_menu` VALUES (935, 1, 62);
INSERT INTO `sys_role_menu` VALUES (936, 1, 63);
INSERT INTO `sys_role_menu` VALUES (937, 1, 64);
INSERT INTO `sys_role_menu` VALUES (938, 1, 65);
INSERT INTO `sys_role_menu` VALUES (939, 1, 66);
INSERT INTO `sys_role_menu` VALUES (940, 1, 67);
INSERT INTO `sys_role_menu` VALUES (941, 1, 68);
INSERT INTO `sys_role_menu` VALUES (942, 1, 69);
INSERT INTO `sys_role_menu` VALUES (943, 1, 70);
INSERT INTO `sys_role_menu` VALUES (944, 1, 71);
INSERT INTO `sys_role_menu` VALUES (945, 1, 72);
INSERT INTO `sys_role_menu` VALUES (946, 1, 73);
INSERT INTO `sys_role_menu` VALUES (947, 1, 74);
INSERT INTO `sys_role_menu` VALUES (948, 1, 75);
INSERT INTO `sys_role_menu` VALUES (949, 1, 76);
INSERT INTO `sys_role_menu` VALUES (950, 1, 77);
INSERT INTO `sys_role_menu` VALUES (951, 1, 19);
INSERT INTO `sys_role_menu` VALUES (952, 1, 20);
INSERT INTO `sys_role_menu` VALUES (953, 1, 21);
INSERT INTO `sys_role_menu` VALUES (954, 1, 46);
INSERT INTO `sys_role_menu` VALUES (955, 1, 47);
INSERT INTO `sys_role_menu` VALUES (956, 1, 48);
INSERT INTO `sys_role_menu` VALUES (957, 1, 49);
INSERT INTO `sys_role_menu` VALUES (958, 1, 50);
INSERT INTO `sys_role_menu` VALUES (959, 1, 51);
INSERT INTO `sys_role_menu` VALUES (960, 1, 52);
INSERT INTO `sys_role_menu` VALUES (961, 1, 53);
INSERT INTO `sys_role_menu` VALUES (962, 1, 54);
INSERT INTO `sys_role_menu` VALUES (963, 1, 55);
INSERT INTO `sys_role_menu` VALUES (964, 1, 56);
INSERT INTO `sys_role_menu` VALUES (965, 1, 57);
INSERT INTO `sys_role_menu` VALUES (966, 1, 58);
INSERT INTO `sys_role_menu` VALUES (967, 1, 59);
INSERT INTO `sys_role_menu` VALUES (968, 1, 60);
INSERT INTO `sys_role_menu` VALUES (969, 1, 61);
INSERT INTO `sys_role_menu` VALUES (970, 1, 62);
INSERT INTO `sys_role_menu` VALUES (971, 1, 63);
INSERT INTO `sys_role_menu` VALUES (972, 1, 64);
INSERT INTO `sys_role_menu` VALUES (973, 1, 65);
INSERT INTO `sys_role_menu` VALUES (974, 1, 66);
INSERT INTO `sys_role_menu` VALUES (975, 1, 67);
INSERT INTO `sys_role_menu` VALUES (976, 1, 68);
INSERT INTO `sys_role_menu` VALUES (977, 1, 69);
INSERT INTO `sys_role_menu` VALUES (978, 1, 70);
INSERT INTO `sys_role_menu` VALUES (979, 1, 71);
INSERT INTO `sys_role_menu` VALUES (980, 1, 72);
INSERT INTO `sys_role_menu` VALUES (981, 1, 73);
INSERT INTO `sys_role_menu` VALUES (982, 1, 74);
INSERT INTO `sys_role_menu` VALUES (983, 1, 75);
INSERT INTO `sys_role_menu` VALUES (984, 1, 76);
INSERT INTO `sys_role_menu` VALUES (985, 1, 77);
INSERT INTO `sys_role_menu` VALUES (986, 1, 78);
INSERT INTO `sys_role_menu` VALUES (987, 1, 79);
INSERT INTO `sys_role_menu` VALUES (988, 1, 80);
INSERT INTO `sys_role_menu` VALUES (989, 1, 81);
INSERT INTO `sys_role_menu` VALUES (990, 1, 82);
INSERT INTO `sys_role_menu` VALUES (991, 1, 83);
INSERT INTO `sys_role_menu` VALUES (992, 1, 84);
INSERT INTO `sys_role_menu` VALUES (993, 1, 85);
INSERT INTO `sys_role_menu` VALUES (994, 1, 19);
INSERT INTO `sys_role_menu` VALUES (995, 1, 20);
INSERT INTO `sys_role_menu` VALUES (996, 1, 21);
INSERT INTO `sys_role_menu` VALUES (997, 1, 46);
INSERT INTO `sys_role_menu` VALUES (998, 1, 47);
INSERT INTO `sys_role_menu` VALUES (999, 1, 48);
INSERT INTO `sys_role_menu` VALUES (1000, 1, 49);
INSERT INTO `sys_role_menu` VALUES (1001, 1, 50);
INSERT INTO `sys_role_menu` VALUES (1002, 1, 51);
INSERT INTO `sys_role_menu` VALUES (1003, 1, 52);
INSERT INTO `sys_role_menu` VALUES (1004, 1, 53);
INSERT INTO `sys_role_menu` VALUES (1005, 1, 54);
INSERT INTO `sys_role_menu` VALUES (1006, 1, 55);
INSERT INTO `sys_role_menu` VALUES (1007, 1, 56);
INSERT INTO `sys_role_menu` VALUES (1008, 1, 57);
INSERT INTO `sys_role_menu` VALUES (1009, 1, 58);
INSERT INTO `sys_role_menu` VALUES (1010, 1, 59);
INSERT INTO `sys_role_menu` VALUES (1011, 1, 60);
INSERT INTO `sys_role_menu` VALUES (1012, 1, 61);
INSERT INTO `sys_role_menu` VALUES (1013, 1, 62);
INSERT INTO `sys_role_menu` VALUES (1014, 1, 63);
INSERT INTO `sys_role_menu` VALUES (1015, 1, 64);
INSERT INTO `sys_role_menu` VALUES (1016, 1, 65);
INSERT INTO `sys_role_menu` VALUES (1017, 1, 66);
INSERT INTO `sys_role_menu` VALUES (1018, 1, 67);
INSERT INTO `sys_role_menu` VALUES (1019, 1, 68);
INSERT INTO `sys_role_menu` VALUES (1020, 1, 69);
INSERT INTO `sys_role_menu` VALUES (1021, 1, 70);
INSERT INTO `sys_role_menu` VALUES (1022, 1, 71);
INSERT INTO `sys_role_menu` VALUES (1023, 1, 72);
INSERT INTO `sys_role_menu` VALUES (1024, 1, 73);
INSERT INTO `sys_role_menu` VALUES (1025, 1, 74);
INSERT INTO `sys_role_menu` VALUES (1026, 1, 75);
INSERT INTO `sys_role_menu` VALUES (1027, 1, 76);
INSERT INTO `sys_role_menu` VALUES (1028, 1, 77);
INSERT INTO `sys_role_menu` VALUES (1029, 1, 78);
INSERT INTO `sys_role_menu` VALUES (1030, 1, 79);
INSERT INTO `sys_role_menu` VALUES (1031, 1, 80);
INSERT INTO `sys_role_menu` VALUES (1032, 1, 81);
INSERT INTO `sys_role_menu` VALUES (1033, 1, 82);
INSERT INTO `sys_role_menu` VALUES (1034, 1, 83);
INSERT INTO `sys_role_menu` VALUES (1035, 1, 84);
INSERT INTO `sys_role_menu` VALUES (1036, 1, 85);
INSERT INTO `sys_role_menu` VALUES (1037, 1, 86);
INSERT INTO `sys_role_menu` VALUES (1038, 1, 87);
INSERT INTO `sys_role_menu` VALUES (1039, 1, 88);
INSERT INTO `sys_role_menu` VALUES (1040, 1, 89);
INSERT INTO `sys_role_menu` VALUES (1041, 1, 90);
INSERT INTO `sys_role_menu` VALUES (1042, 1, 91);
INSERT INTO `sys_role_menu` VALUES (1043, 1, 92);
INSERT INTO `sys_role_menu` VALUES (1044, 1, 93);
INSERT INTO `sys_role_menu` VALUES (1045, 1, 19);
INSERT INTO `sys_role_menu` VALUES (1046, 1, 20);
INSERT INTO `sys_role_menu` VALUES (1047, 1, 21);
INSERT INTO `sys_role_menu` VALUES (1048, 1, 46);
INSERT INTO `sys_role_menu` VALUES (1049, 1, 47);
INSERT INTO `sys_role_menu` VALUES (1050, 1, 48);
INSERT INTO `sys_role_menu` VALUES (1051, 1, 49);
INSERT INTO `sys_role_menu` VALUES (1052, 1, 50);
INSERT INTO `sys_role_menu` VALUES (1053, 1, 51);
INSERT INTO `sys_role_menu` VALUES (1054, 1, 52);
INSERT INTO `sys_role_menu` VALUES (1055, 1, 53);
INSERT INTO `sys_role_menu` VALUES (1056, 1, 54);
INSERT INTO `sys_role_menu` VALUES (1057, 1, 55);
INSERT INTO `sys_role_menu` VALUES (1058, 1, 56);
INSERT INTO `sys_role_menu` VALUES (1059, 1, 57);
INSERT INTO `sys_role_menu` VALUES (1060, 1, 58);
INSERT INTO `sys_role_menu` VALUES (1061, 1, 59);
INSERT INTO `sys_role_menu` VALUES (1062, 1, 60);
INSERT INTO `sys_role_menu` VALUES (1063, 1, 61);
INSERT INTO `sys_role_menu` VALUES (1064, 1, 62);
INSERT INTO `sys_role_menu` VALUES (1065, 1, 63);
INSERT INTO `sys_role_menu` VALUES (1066, 1, 64);
INSERT INTO `sys_role_menu` VALUES (1067, 1, 65);
INSERT INTO `sys_role_menu` VALUES (1068, 1, 66);
INSERT INTO `sys_role_menu` VALUES (1069, 1, 67);
INSERT INTO `sys_role_menu` VALUES (1070, 1, 68);
INSERT INTO `sys_role_menu` VALUES (1071, 1, 69);
INSERT INTO `sys_role_menu` VALUES (1072, 1, 70);
INSERT INTO `sys_role_menu` VALUES (1073, 1, 71);
INSERT INTO `sys_role_menu` VALUES (1074, 1, 72);
INSERT INTO `sys_role_menu` VALUES (1075, 1, 73);
INSERT INTO `sys_role_menu` VALUES (1076, 1, 74);
INSERT INTO `sys_role_menu` VALUES (1077, 1, 75);
INSERT INTO `sys_role_menu` VALUES (1078, 1, 76);
INSERT INTO `sys_role_menu` VALUES (1079, 1, 77);
INSERT INTO `sys_role_menu` VALUES (1080, 1, 78);
INSERT INTO `sys_role_menu` VALUES (1081, 1, 79);
INSERT INTO `sys_role_menu` VALUES (1082, 1, 80);
INSERT INTO `sys_role_menu` VALUES (1083, 1, 81);
INSERT INTO `sys_role_menu` VALUES (1084, 1, 82);
INSERT INTO `sys_role_menu` VALUES (1085, 1, 83);
INSERT INTO `sys_role_menu` VALUES (1086, 1, 84);
INSERT INTO `sys_role_menu` VALUES (1087, 1, 85);
INSERT INTO `sys_role_menu` VALUES (1088, 1, 86);
INSERT INTO `sys_role_menu` VALUES (1089, 1, 87);
INSERT INTO `sys_role_menu` VALUES (1090, 1, 88);
INSERT INTO `sys_role_menu` VALUES (1091, 1, 89);
INSERT INTO `sys_role_menu` VALUES (1092, 1, 90);
INSERT INTO `sys_role_menu` VALUES (1093, 1, 91);
INSERT INTO `sys_role_menu` VALUES (1094, 1, 92);
INSERT INTO `sys_role_menu` VALUES (1095, 1, 93);
INSERT INTO `sys_role_menu` VALUES (1096, 1, 94);
INSERT INTO `sys_role_menu` VALUES (1097, 1, 95);
INSERT INTO `sys_role_menu` VALUES (1098, 1, 96);
INSERT INTO `sys_role_menu` VALUES (1099, 1, 97);
INSERT INTO `sys_role_menu` VALUES (1100, 1, 98);
INSERT INTO `sys_role_menu` VALUES (1101, 1, 99);
INSERT INTO `sys_role_menu` VALUES (1102, 1, 100);
INSERT INTO `sys_role_menu` VALUES (1103, 1, 101);
INSERT INTO `sys_role_menu` VALUES (1104, 1, 19);
INSERT INTO `sys_role_menu` VALUES (1105, 1, 20);
INSERT INTO `sys_role_menu` VALUES (1106, 1, 21);
INSERT INTO `sys_role_menu` VALUES (1107, 1, 46);
INSERT INTO `sys_role_menu` VALUES (1108, 1, 47);
INSERT INTO `sys_role_menu` VALUES (1109, 1, 48);
INSERT INTO `sys_role_menu` VALUES (1110, 1, 49);
INSERT INTO `sys_role_menu` VALUES (1111, 1, 50);
INSERT INTO `sys_role_menu` VALUES (1112, 1, 51);
INSERT INTO `sys_role_menu` VALUES (1113, 1, 52);
INSERT INTO `sys_role_menu` VALUES (1114, 1, 53);
INSERT INTO `sys_role_menu` VALUES (1115, 1, 54);
INSERT INTO `sys_role_menu` VALUES (1116, 1, 55);
INSERT INTO `sys_role_menu` VALUES (1117, 1, 56);
INSERT INTO `sys_role_menu` VALUES (1118, 1, 57);
INSERT INTO `sys_role_menu` VALUES (1119, 1, 58);
INSERT INTO `sys_role_menu` VALUES (1120, 1, 59);
INSERT INTO `sys_role_menu` VALUES (1121, 1, 60);
INSERT INTO `sys_role_menu` VALUES (1122, 1, 61);
INSERT INTO `sys_role_menu` VALUES (1123, 1, 62);
INSERT INTO `sys_role_menu` VALUES (1124, 1, 63);
INSERT INTO `sys_role_menu` VALUES (1125, 1, 64);
INSERT INTO `sys_role_menu` VALUES (1126, 1, 65);
INSERT INTO `sys_role_menu` VALUES (1127, 1, 66);
INSERT INTO `sys_role_menu` VALUES (1128, 1, 67);
INSERT INTO `sys_role_menu` VALUES (1129, 1, 68);
INSERT INTO `sys_role_menu` VALUES (1130, 1, 69);
INSERT INTO `sys_role_menu` VALUES (1131, 1, 70);
INSERT INTO `sys_role_menu` VALUES (1132, 1, 71);
INSERT INTO `sys_role_menu` VALUES (1133, 1, 72);
INSERT INTO `sys_role_menu` VALUES (1134, 1, 73);
INSERT INTO `sys_role_menu` VALUES (1135, 1, 74);
INSERT INTO `sys_role_menu` VALUES (1136, 1, 75);
INSERT INTO `sys_role_menu` VALUES (1137, 1, 76);
INSERT INTO `sys_role_menu` VALUES (1138, 1, 77);
INSERT INTO `sys_role_menu` VALUES (1139, 1, 78);
INSERT INTO `sys_role_menu` VALUES (1140, 1, 79);
INSERT INTO `sys_role_menu` VALUES (1141, 1, 80);
INSERT INTO `sys_role_menu` VALUES (1142, 1, 81);
INSERT INTO `sys_role_menu` VALUES (1143, 1, 82);
INSERT INTO `sys_role_menu` VALUES (1144, 1, 83);
INSERT INTO `sys_role_menu` VALUES (1145, 1, 84);
INSERT INTO `sys_role_menu` VALUES (1146, 1, 85);
INSERT INTO `sys_role_menu` VALUES (1147, 1, 86);
INSERT INTO `sys_role_menu` VALUES (1148, 1, 87);
INSERT INTO `sys_role_menu` VALUES (1149, 1, 88);
INSERT INTO `sys_role_menu` VALUES (1150, 1, 89);
INSERT INTO `sys_role_menu` VALUES (1151, 1, 90);
INSERT INTO `sys_role_menu` VALUES (1152, 1, 91);
INSERT INTO `sys_role_menu` VALUES (1153, 1, 92);
INSERT INTO `sys_role_menu` VALUES (1154, 1, 93);
INSERT INTO `sys_role_menu` VALUES (1155, 1, 94);
INSERT INTO `sys_role_menu` VALUES (1156, 1, 95);
INSERT INTO `sys_role_menu` VALUES (1157, 1, 96);
INSERT INTO `sys_role_menu` VALUES (1158, 1, 97);
INSERT INTO `sys_role_menu` VALUES (1159, 1, 98);
INSERT INTO `sys_role_menu` VALUES (1160, 1, 99);
INSERT INTO `sys_role_menu` VALUES (1161, 1, 100);
INSERT INTO `sys_role_menu` VALUES (1162, 1, 101);
INSERT INTO `sys_role_menu` VALUES (1163, 1, 102);
INSERT INTO `sys_role_menu` VALUES (1164, 1, 103);
INSERT INTO `sys_role_menu` VALUES (1165, 1, 104);
INSERT INTO `sys_role_menu` VALUES (1166, 1, 105);
INSERT INTO `sys_role_menu` VALUES (1167, 1, 106);
INSERT INTO `sys_role_menu` VALUES (1168, 1, 107);
INSERT INTO `sys_role_menu` VALUES (1169, 1, 108);
INSERT INTO `sys_role_menu` VALUES (1170, 1, 109);
INSERT INTO `sys_role_menu` VALUES (1171, 1, 19);
INSERT INTO `sys_role_menu` VALUES (1172, 1, 20);
INSERT INTO `sys_role_menu` VALUES (1173, 1, 21);
INSERT INTO `sys_role_menu` VALUES (1174, 1, 46);
INSERT INTO `sys_role_menu` VALUES (1175, 1, 47);
INSERT INTO `sys_role_menu` VALUES (1176, 1, 48);
INSERT INTO `sys_role_menu` VALUES (1177, 1, 49);
INSERT INTO `sys_role_menu` VALUES (1178, 1, 50);
INSERT INTO `sys_role_menu` VALUES (1179, 1, 51);
INSERT INTO `sys_role_menu` VALUES (1180, 1, 52);
INSERT INTO `sys_role_menu` VALUES (1181, 1, 53);
INSERT INTO `sys_role_menu` VALUES (1182, 1, 54);
INSERT INTO `sys_role_menu` VALUES (1183, 1, 55);
INSERT INTO `sys_role_menu` VALUES (1184, 1, 56);
INSERT INTO `sys_role_menu` VALUES (1185, 1, 57);
INSERT INTO `sys_role_menu` VALUES (1186, 1, 58);
INSERT INTO `sys_role_menu` VALUES (1187, 1, 59);
INSERT INTO `sys_role_menu` VALUES (1188, 1, 60);
INSERT INTO `sys_role_menu` VALUES (1189, 1, 61);
INSERT INTO `sys_role_menu` VALUES (1190, 1, 62);
INSERT INTO `sys_role_menu` VALUES (1191, 1, 63);
INSERT INTO `sys_role_menu` VALUES (1192, 1, 64);
INSERT INTO `sys_role_menu` VALUES (1193, 1, 65);
INSERT INTO `sys_role_menu` VALUES (1194, 1, 66);
INSERT INTO `sys_role_menu` VALUES (1195, 1, 67);
INSERT INTO `sys_role_menu` VALUES (1196, 1, 68);
INSERT INTO `sys_role_menu` VALUES (1197, 1, 69);
INSERT INTO `sys_role_menu` VALUES (1198, 1, 70);
INSERT INTO `sys_role_menu` VALUES (1199, 1, 71);
INSERT INTO `sys_role_menu` VALUES (1200, 1, 72);
INSERT INTO `sys_role_menu` VALUES (1201, 1, 73);
INSERT INTO `sys_role_menu` VALUES (1202, 1, 74);
INSERT INTO `sys_role_menu` VALUES (1203, 1, 75);
INSERT INTO `sys_role_menu` VALUES (1204, 1, 76);
INSERT INTO `sys_role_menu` VALUES (1205, 1, 77);
INSERT INTO `sys_role_menu` VALUES (1206, 1, 78);
INSERT INTO `sys_role_menu` VALUES (1207, 1, 79);
INSERT INTO `sys_role_menu` VALUES (1208, 1, 80);
INSERT INTO `sys_role_menu` VALUES (1209, 1, 81);
INSERT INTO `sys_role_menu` VALUES (1210, 1, 82);
INSERT INTO `sys_role_menu` VALUES (1211, 1, 83);
INSERT INTO `sys_role_menu` VALUES (1212, 1, 84);
INSERT INTO `sys_role_menu` VALUES (1213, 1, 85);
INSERT INTO `sys_role_menu` VALUES (1214, 1, 86);
INSERT INTO `sys_role_menu` VALUES (1215, 1, 87);
INSERT INTO `sys_role_menu` VALUES (1216, 1, 88);
INSERT INTO `sys_role_menu` VALUES (1217, 1, 89);
INSERT INTO `sys_role_menu` VALUES (1218, 1, 90);
INSERT INTO `sys_role_menu` VALUES (1219, 1, 91);
INSERT INTO `sys_role_menu` VALUES (1220, 1, 92);
INSERT INTO `sys_role_menu` VALUES (1221, 1, 93);
INSERT INTO `sys_role_menu` VALUES (1222, 1, 94);
INSERT INTO `sys_role_menu` VALUES (1223, 1, 95);
INSERT INTO `sys_role_menu` VALUES (1224, 1, 96);
INSERT INTO `sys_role_menu` VALUES (1225, 1, 97);
INSERT INTO `sys_role_menu` VALUES (1226, 1, 98);
INSERT INTO `sys_role_menu` VALUES (1227, 1, 99);
INSERT INTO `sys_role_menu` VALUES (1228, 1, 100);
INSERT INTO `sys_role_menu` VALUES (1229, 1, 101);
INSERT INTO `sys_role_menu` VALUES (1230, 1, 102);
INSERT INTO `sys_role_menu` VALUES (1231, 1, 103);
INSERT INTO `sys_role_menu` VALUES (1232, 1, 104);
INSERT INTO `sys_role_menu` VALUES (1233, 1, 105);
INSERT INTO `sys_role_menu` VALUES (1234, 1, 106);
INSERT INTO `sys_role_menu` VALUES (1235, 1, 107);
INSERT INTO `sys_role_menu` VALUES (1236, 1, 108);
INSERT INTO `sys_role_menu` VALUES (1237, 1, 109);
INSERT INTO `sys_role_menu` VALUES (1238, 1, 110);
INSERT INTO `sys_role_menu` VALUES (1239, 1, 111);
INSERT INTO `sys_role_menu` VALUES (1240, 1, 112);
INSERT INTO `sys_role_menu` VALUES (1241, 1, 113);
INSERT INTO `sys_role_menu` VALUES (1242, 1, 114);
INSERT INTO `sys_role_menu` VALUES (1243, 1, 115);
INSERT INTO `sys_role_menu` VALUES (1244, 1, 116);
INSERT INTO `sys_role_menu` VALUES (1245, 1, 117);
INSERT INTO `sys_role_menu` VALUES (1246, 1, 19);
INSERT INTO `sys_role_menu` VALUES (1247, 1, 20);
INSERT INTO `sys_role_menu` VALUES (1248, 1, 21);
INSERT INTO `sys_role_menu` VALUES (1249, 1, 46);
INSERT INTO `sys_role_menu` VALUES (1250, 1, 47);
INSERT INTO `sys_role_menu` VALUES (1251, 1, 48);
INSERT INTO `sys_role_menu` VALUES (1252, 1, 49);
INSERT INTO `sys_role_menu` VALUES (1253, 1, 50);
INSERT INTO `sys_role_menu` VALUES (1254, 1, 51);
INSERT INTO `sys_role_menu` VALUES (1255, 1, 52);
INSERT INTO `sys_role_menu` VALUES (1256, 1, 53);
INSERT INTO `sys_role_menu` VALUES (1257, 1, 54);
INSERT INTO `sys_role_menu` VALUES (1258, 1, 55);
INSERT INTO `sys_role_menu` VALUES (1259, 1, 56);
INSERT INTO `sys_role_menu` VALUES (1260, 1, 57);
INSERT INTO `sys_role_menu` VALUES (1261, 1, 58);
INSERT INTO `sys_role_menu` VALUES (1262, 1, 59);
INSERT INTO `sys_role_menu` VALUES (1263, 1, 60);
INSERT INTO `sys_role_menu` VALUES (1264, 1, 61);
INSERT INTO `sys_role_menu` VALUES (1265, 1, 62);
INSERT INTO `sys_role_menu` VALUES (1266, 1, 63);
INSERT INTO `sys_role_menu` VALUES (1267, 1, 64);
INSERT INTO `sys_role_menu` VALUES (1268, 1, 65);
INSERT INTO `sys_role_menu` VALUES (1269, 1, 66);
INSERT INTO `sys_role_menu` VALUES (1270, 1, 67);
INSERT INTO `sys_role_menu` VALUES (1271, 1, 68);
INSERT INTO `sys_role_menu` VALUES (1272, 1, 69);
INSERT INTO `sys_role_menu` VALUES (1273, 1, 70);
INSERT INTO `sys_role_menu` VALUES (1274, 1, 71);
INSERT INTO `sys_role_menu` VALUES (1275, 1, 72);
INSERT INTO `sys_role_menu` VALUES (1276, 1, 73);
INSERT INTO `sys_role_menu` VALUES (1277, 1, 74);
INSERT INTO `sys_role_menu` VALUES (1278, 1, 75);
INSERT INTO `sys_role_menu` VALUES (1279, 1, 76);
INSERT INTO `sys_role_menu` VALUES (1280, 1, 77);
INSERT INTO `sys_role_menu` VALUES (1281, 1, 78);
INSERT INTO `sys_role_menu` VALUES (1282, 1, 79);
INSERT INTO `sys_role_menu` VALUES (1283, 1, 80);
INSERT INTO `sys_role_menu` VALUES (1284, 1, 81);
INSERT INTO `sys_role_menu` VALUES (1285, 1, 82);
INSERT INTO `sys_role_menu` VALUES (1286, 1, 83);
INSERT INTO `sys_role_menu` VALUES (1287, 1, 84);
INSERT INTO `sys_role_menu` VALUES (1288, 1, 85);
INSERT INTO `sys_role_menu` VALUES (1289, 1, 86);
INSERT INTO `sys_role_menu` VALUES (1290, 1, 87);
INSERT INTO `sys_role_menu` VALUES (1291, 1, 88);
INSERT INTO `sys_role_menu` VALUES (1292, 1, 89);
INSERT INTO `sys_role_menu` VALUES (1293, 1, 90);
INSERT INTO `sys_role_menu` VALUES (1294, 1, 91);
INSERT INTO `sys_role_menu` VALUES (1295, 1, 92);
INSERT INTO `sys_role_menu` VALUES (1296, 1, 93);
INSERT INTO `sys_role_menu` VALUES (1297, 1, 94);
INSERT INTO `sys_role_menu` VALUES (1298, 1, 95);
INSERT INTO `sys_role_menu` VALUES (1299, 1, 96);
INSERT INTO `sys_role_menu` VALUES (1300, 1, 97);
INSERT INTO `sys_role_menu` VALUES (1301, 1, 98);
INSERT INTO `sys_role_menu` VALUES (1302, 1, 99);
INSERT INTO `sys_role_menu` VALUES (1303, 1, 100);
INSERT INTO `sys_role_menu` VALUES (1304, 1, 101);
INSERT INTO `sys_role_menu` VALUES (1305, 1, 102);
INSERT INTO `sys_role_menu` VALUES (1306, 1, 103);
INSERT INTO `sys_role_menu` VALUES (1307, 1, 104);
INSERT INTO `sys_role_menu` VALUES (1308, 1, 105);
INSERT INTO `sys_role_menu` VALUES (1309, 1, 106);
INSERT INTO `sys_role_menu` VALUES (1310, 1, 107);
INSERT INTO `sys_role_menu` VALUES (1311, 1, 108);
INSERT INTO `sys_role_menu` VALUES (1312, 1, 109);
INSERT INTO `sys_role_menu` VALUES (1313, 1, 110);
INSERT INTO `sys_role_menu` VALUES (1314, 1, 111);
INSERT INTO `sys_role_menu` VALUES (1315, 1, 112);
INSERT INTO `sys_role_menu` VALUES (1316, 1, 113);
INSERT INTO `sys_role_menu` VALUES (1317, 1, 114);
INSERT INTO `sys_role_menu` VALUES (1318, 1, 115);
INSERT INTO `sys_role_menu` VALUES (1319, 1, 116);
INSERT INTO `sys_role_menu` VALUES (1320, 1, 117);
INSERT INTO `sys_role_menu` VALUES (1321, 1, 118);
INSERT INTO `sys_role_menu` VALUES (1322, 1, 119);
INSERT INTO `sys_role_menu` VALUES (1323, 1, 120);
INSERT INTO `sys_role_menu` VALUES (1324, 1, 121);
INSERT INTO `sys_role_menu` VALUES (1325, 1, 122);
INSERT INTO `sys_role_menu` VALUES (1326, 1, 123);
INSERT INTO `sys_role_menu` VALUES (1327, 1, 124);
INSERT INTO `sys_role_menu` VALUES (1328, 1, 125);
INSERT INTO `sys_role_menu` VALUES (1329, 1, 19);
INSERT INTO `sys_role_menu` VALUES (1330, 1, 20);
INSERT INTO `sys_role_menu` VALUES (1331, 1, 21);
INSERT INTO `sys_role_menu` VALUES (1332, 1, 126);
INSERT INTO `sys_role_menu` VALUES (1333, 1, 127);
INSERT INTO `sys_role_menu` VALUES (1334, 1, 128);
INSERT INTO `sys_role_menu` VALUES (1335, 1, 129);
INSERT INTO `sys_role_menu` VALUES (1336, 1, 130);
INSERT INTO `sys_role_menu` VALUES (1337, 1, 131);
INSERT INTO `sys_role_menu` VALUES (1338, 1, 132);
INSERT INTO `sys_role_menu` VALUES (1339, 1, 133);
INSERT INTO `sys_role_menu` VALUES (1340, 1, 134);
INSERT INTO `sys_role_menu` VALUES (1341, 1, 135);
INSERT INTO `sys_role_menu` VALUES (1342, 1, 136);
INSERT INTO `sys_role_menu` VALUES (1343, 1, 137);
INSERT INTO `sys_role_menu` VALUES (1344, 1, 138);
INSERT INTO `sys_role_menu` VALUES (1345, 1, 139);
INSERT INTO `sys_role_menu` VALUES (1347, 2, 1);
INSERT INTO `sys_role_menu` VALUES (1348, 2, 2);
INSERT INTO `sys_role_menu` VALUES (1349, 2, 3);
INSERT INTO `sys_role_menu` VALUES (1350, 2, 4);
INSERT INTO `sys_role_menu` VALUES (1351, 2, 5);
INSERT INTO `sys_role_menu` VALUES (1352, 2, 6);
INSERT INTO `sys_role_menu` VALUES (1353, 2, 7);
INSERT INTO `sys_role_menu` VALUES (1354, 2, 8);
INSERT INTO `sys_role_menu` VALUES (1355, 2, 9);
INSERT INTO `sys_role_menu` VALUES (1356, 2, 10);
INSERT INTO `sys_role_menu` VALUES (1357, 2, 11);
INSERT INTO `sys_role_menu` VALUES (1358, 2, 12);
INSERT INTO `sys_role_menu` VALUES (1359, 2, 13);
INSERT INTO `sys_role_menu` VALUES (1360, 2, 14);
INSERT INTO `sys_role_menu` VALUES (1361, 2, 15);
INSERT INTO `sys_role_menu` VALUES (1362, 2, 16);
INSERT INTO `sys_role_menu` VALUES (1363, 2, 17);
INSERT INTO `sys_role_menu` VALUES (1364, 2, 18);
INSERT INTO `sys_role_menu` VALUES (1365, 2, 19);
INSERT INTO `sys_role_menu` VALUES (1366, 2, 20);
INSERT INTO `sys_role_menu` VALUES (1367, 2, 21);
INSERT INTO `sys_role_menu` VALUES (1368, 2, 22);
INSERT INTO `sys_role_menu` VALUES (1369, 2, 23);
INSERT INTO `sys_role_menu` VALUES (1370, 2, 24);
INSERT INTO `sys_role_menu` VALUES (1371, 2, 25);
INSERT INTO `sys_role_menu` VALUES (1372, 2, 26);
INSERT INTO `sys_role_menu` VALUES (1373, 2, 27);
INSERT INTO `sys_role_menu` VALUES (1374, 2, 28);
INSERT INTO `sys_role_menu` VALUES (1375, 2, 29);
INSERT INTO `sys_role_menu` VALUES (1376, 2, 30);
INSERT INTO `sys_role_menu` VALUES (1377, 2, 31);
INSERT INTO `sys_role_menu` VALUES (1378, 2, 32);
INSERT INTO `sys_role_menu` VALUES (1379, 2, 33);
INSERT INTO `sys_role_menu` VALUES (1380, 2, 34);
INSERT INTO `sys_role_menu` VALUES (1381, 2, 35);
INSERT INTO `sys_role_menu` VALUES (1382, 2, 36);
INSERT INTO `sys_role_menu` VALUES (1383, 2, 37);
INSERT INTO `sys_role_menu` VALUES (1384, 2, 38);
INSERT INTO `sys_role_menu` VALUES (1385, 2, 39);
INSERT INTO `sys_role_menu` VALUES (1386, 2, 40);
INSERT INTO `sys_role_menu` VALUES (1387, 2, 41);
INSERT INTO `sys_role_menu` VALUES (1388, 2, 42);
INSERT INTO `sys_role_menu` VALUES (1389, 2, 43);
INSERT INTO `sys_role_menu` VALUES (1390, 2, 44);
INSERT INTO `sys_role_menu` VALUES (1391, 2, 45);
INSERT INTO `sys_role_menu` VALUES (1392, 2, 126);
INSERT INTO `sys_role_menu` VALUES (1393, 2, 127);
INSERT INTO `sys_role_menu` VALUES (1394, 2, 128);
INSERT INTO `sys_role_menu` VALUES (1395, 2, 129);
INSERT INTO `sys_role_menu` VALUES (1396, 2, 130);
INSERT INTO `sys_role_menu` VALUES (1397, 2, 131);
INSERT INTO `sys_role_menu` VALUES (1398, 2, 132);
INSERT INTO `sys_role_menu` VALUES (1399, 2, 133);
INSERT INTO `sys_role_menu` VALUES (1400, 2, 134);
INSERT INTO `sys_role_menu` VALUES (1401, 2, 135);
INSERT INTO `sys_role_menu` VALUES (1402, 2, 136);
INSERT INTO `sys_role_menu` VALUES (1403, 2, 137);
INSERT INTO `sys_role_menu` VALUES (1404, 2, 138);
INSERT INTO `sys_role_menu` VALUES (1405, 2, 139);

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
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$NHwBrsRfesK2pSMgG3NjZ.3JiUXd0msi5ib34QAROfTNq5t0UTL6y', '超级管理员', NULL, NULL, NULL, 0, 1, NULL, '2026-01-29 22:42:08', '2026-01-29 22:58:21', NULL, 1, 0);
INSERT INTO `sys_user` VALUES (2, 'test', '$2a$10$kTn0Z9BPDnOAU1qB.sJrF.unLh4bbj9FQ7tVsG4AtSBQXFs1V/ewq', 'test', NULL, '111@qq.com', '1888888888', 1, 1, '', '2026-01-29 23:21:12', '2026-01-29 23:21:12', 1, 1, 0);
INSERT INTO `sys_user` VALUES (3, 'mars', '$2a$10$goR4f6wAzry8a6jTrWHDGeI7Fiq2SovcXYrVprcoRgC6mCnK1fM4G', 'mars', NULL, '1121@qq.com', '18888888881', 1, 1, '', '2026-01-29 23:21:12', '2026-01-29 23:21:12', 1, 1, 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1);
INSERT INTO `sys_user_role` VALUES (3, 2, 2);
INSERT INTO `sys_user_role` VALUES (4, 3, 2);

SET FOREIGN_KEY_CHECKS = 1;
