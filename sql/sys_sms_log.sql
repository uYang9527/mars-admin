-- ----------------------------
-- 短信发送记录表
-- ----------------------------
DROP TABLE IF EXISTS `sys_sms_log`;
CREATE TABLE `sys_sms_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `phone` varchar(20) NOT NULL COMMENT '手机号',
  `content` varchar(500) DEFAULT NULL COMMENT '短信内容/验证码',
  `sms_type` varchar(20) DEFAULT 'verify_code' COMMENT '短信类型：verify_code-验证码 notice-通知 marketing-营销',
  `template_id` varchar(50) DEFAULT NULL COMMENT '模板ID',
  `template_params` varchar(500) DEFAULT NULL COMMENT '模板参数（JSON格式）',
  `provider` varchar(20) DEFAULT NULL COMMENT '服务商：aliyun-阿里云 tencent-腾讯云 console-控制台',
  `status` tinyint DEFAULT 0 COMMENT '发送状态：0-发送中 1-成功 2-失败',
  `result_msg` varchar(500) DEFAULT NULL COMMENT '发送结果消息',
  `biz_id` varchar(100) DEFAULT NULL COMMENT '服务商返回的消息ID',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `biz_type` varchar(30) DEFAULT NULL COMMENT '业务类型：login-登录 register-注册 reset_password-重置密码 bind_phone-绑定手机',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_phone` (`phone`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_send_time` (`send_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='短信发送记录表';
