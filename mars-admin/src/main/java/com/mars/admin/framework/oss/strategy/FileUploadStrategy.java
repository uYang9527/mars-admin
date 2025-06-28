package com.mars.admin.framework.oss.strategy;

import com.mars.admin.common.response.FileUploadResult;
import com.mars.admin.entity.SysOssConfig;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 文件上传策略接口
 *
 * @author Mars.wq
 */
public interface FileUploadStrategy {

    /**
     * 获取策略类型
     */
    String getType();

    /**
     * 上传文件
     *
     * @param file 文件
     * @param config OSS配置
     * @return 上传结果
     */
    FileUploadResult upload(MultipartFile file, SysOssConfig config);

    /**
     * 上传文件流
     *
     * @param inputStream 文件流
     * @param fileName 文件名
     * @param contentType 文件类型
     * @param config OSS配置
     * @return 上传结果
     */
    FileUploadResult upload(InputStream inputStream, String fileName, String contentType, SysOssConfig config);

    /**
     * 删除文件
     *
     * @param fileName 文件名
     * @param config OSS配置
     * @return 删除结果
     */
    boolean delete(String fileName, SysOssConfig config);

    /**
     * 获取文件预签名URL
     *
     * @param fileName 文件名
     * @param config OSS配置
     * @param expireTime 过期时间(秒)
     * @return 预签名URL
     */
    String getPresignedUrl(String fileName, SysOssConfig config, int expireTime);
} 