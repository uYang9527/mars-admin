package com.mars.admin.common.enums;

/**
 * 文件上传类型枚举
 *
 * @author Mars.wq
 */
public enum FileUploadType {

    /**
     * 本地存储
     */
    LOCAL("local", "本地存储"),

    /**
     * MinIO对象存储
     */
    MINIO("minio", "MinIO对象存储"),

    /**
     * 阿里云OSS
     */
    ALIYUN("aliyun", "阿里云OSS");

    private final String code;
    private final String description;

    FileUploadType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据代码获取枚举
     *
     * @param code 代码
     * @return 枚举
     */
    public static FileUploadType getByCode(String code) {
        for (FileUploadType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 检查代码是否有效
     *
     * @param code 代码
     * @return 是否有效
     */
    public static boolean isValidCode(String code) {
        return getByCode(code) != null;
    }
} 