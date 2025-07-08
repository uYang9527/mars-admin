-- 为sys_user表添加open_id字段以支持小程序登录
-- 执行时间：2025-01-16

-- 添加open_id字段
ALTER TABLE `sys_user` ADD COLUMN `open_id` VARCHAR(100) DEFAULT NULL COMMENT '微信OpenId' AFTER `phone`;

-- 为open_id字段创建索引，提高查询性能
CREATE INDEX `idx_open_id` ON `sys_user` (`open_id`);

-- 添加注释
ALTER TABLE `sys_user` MODIFY COLUMN `open_id` VARCHAR(100) DEFAULT NULL COMMENT '微信OpenId，用于小程序登录'; 