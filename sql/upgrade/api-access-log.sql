-- API 访问统计日志表
-- 执行: mysql -u root -p mars < sql/upgrade/api-access-log.sql

DROP TABLE IF EXISTS `sys_api_access_log`;
CREATE TABLE `sys_api_access_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `start_time` datetime NULL DEFAULT NULL COMMENT '请求开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '请求结束时间',
  `api_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'API路径',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'HTTP方法',
  `status_code` int NULL DEFAULT NULL COMMENT 'HTTP状态码',
  `success` tinyint NULL DEFAULT 1 COMMENT '是否成功(0否 1是)',
  `cost_time` bigint NULL DEFAULT NULL COMMENT '耗时(毫秒)',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户端IP',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID(未登录为空)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_start_time`(`start_time` ASC) USING BTREE,
  INDEX `idx_api_path`(`api_path`(100) ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'API访问统计日志' ROW_FORMAT = DYNAMIC;
