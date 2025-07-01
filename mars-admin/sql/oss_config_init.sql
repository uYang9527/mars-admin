-- OSS配置初始化数据
INSERT INTO `sys_oss_config` (
    `id`, `config_key`, `access_key`, `secret_key`, `bucket_name`, `prefix`, 
    `endpoint`, `domain`, `is_https`, `region`, `access_policy`, `status`, 
    `ext1`, `remark`, `create_time`, `update_time`, `create_by`, `update_by`, `is_deleted`
) VALUES 
-- 本地存储配置（默认启用）
(
    1, 'local', NULL, NULL, NULL, 'upload/', 
    NULL, NULL, 1, NULL, 1, 1, 
    '{"uploadPath":"./upload","baseUrl":"http://localhost:8080/file"}', '本地文件存储配置', 
    NOW(), NOW(), 1, 1, 0
),
-- MinIO配置示例（默认停用）
(
    2, 'minio', 'minioadmin', 'minioadmin', 'mars-admin', 'files/', 
    'http://localhost:9000', 'http://localhost:9000', 0, 'us-east-1', 1, 0, 
    '{"bucketPolicy":"read-write"}', 'MinIO对象存储配置', 
    NOW(), NOW(), 1, 1, 0
),
-- 阿里云OSS配置示例（默认停用）
(
    3, 'aliyun', 'your-access-key', 'your-secret-key', 'your-bucket-name', 'mars-admin/', 
    'oss-cn-hangzhou.aliyuncs.com', 'https://your-bucket-name.oss-cn-hangzhou.aliyuncs.com', 1, 'oss-cn-hangzhou', 1, 0, 
    '{"storageClass":"Standard","acl":"private"}', '阿里云OSS存储配置', 
    NOW(), NOW(), 1, 1, 0
); 