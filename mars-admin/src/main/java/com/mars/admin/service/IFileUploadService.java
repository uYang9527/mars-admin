package com.mars.admin.service;

import com.mars.admin.common.response.FileUploadResult;
import com.mars.admin.entity.SysOss;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * 文件上传服务接口
 *
 * @author Mars.wq
 */
public interface IFileUploadService {

    /**
     * 上传文件（使用默认配置）
     *
     * @param file 文件
     * @return 上传结果
     */
    FileUploadResult upload(MultipartFile file);

    /**
     * 上传文件
     *
     * @param file 文件
     * @param configKey 配置key
     * @return 上传结果
     */
    FileUploadResult upload(MultipartFile file, String configKey);

    /**
     * 上传文件流
     *
     * @param inputStream 文件流
     * @param fileName 文件名
     * @param contentType 文件类型
     * @param configKey 配置key
     * @return 上传结果
     */
    FileUploadResult upload(InputStream inputStream, String fileName, String contentType, String configKey);

    /**
     * 删除文件
     *
     * @param id 文件ID
     * @return 是否成功
     */
    boolean delete(Long id);

    /**
     * 批量删除文件
     *
     * @param ids 文件ID列表
     * @return 是否成功
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 根据文件名删除文件
     *
     * @param fileName 文件名
     * @param configKey 配置key
     * @return 是否成功
     */
    boolean deleteByFileName(String fileName, String configKey);

    /**
     * 获取文件预签名URL
     *
     * @param id 文件ID
     * @param expireTime 过期时间(秒)
     * @return 预签名URL
     */
    String getPresignedUrl(Long id, int expireTime);

    /**
     * 根据文件名获取预签名URL
     *
     * @param fileName 文件名
     * @param configKey 配置key
     * @param expireTime 过期时间(秒)
     * @return 预签名URL
     */
    String getPresignedUrl(String fileName, String configKey, int expireTime);

    /**
     * 分页查询文件列表
     *
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return 分页结果
     */
    Page<SysOss> selectFilePage(Page<SysOss> page, QueryWrapper queryWrapper);

    /**
     * 根据ID获取文件信息
     *
     * @param id 文件ID
     * @return 文件信息
     */
    SysOss getFileById(Long id);

    /**
     * 获取可用的上传策略类型
     *
     * @return 策略类型列表
     */
    java.util.Set<String> getAvailableTypes();
} 