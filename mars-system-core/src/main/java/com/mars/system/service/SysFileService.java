package com.mars.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.system.entity.SysFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务接口
 */
public interface SysFileService {

    /**
     * 分页查询文件列表
     */
    Page<SysFile> page(Integer page, Integer pageSize, String originalName, String fileType);

    /**
     * 上传文件
     */
    SysFile upload(MultipartFile file, String path);

    /**
     * 上传图片
     */
    SysFile uploadImage(MultipartFile file);

    /**
     * 获取文件详情
     */
    SysFile getById(Long id);

    /**
     * 获取文件字节
     */
    byte[] getFileBytes(Long id);

    /**
     * 删除文件
     */
    void delete(Long id);

    /**
     * 批量删除文件
     */
    void deleteBatch(Long[] ids);
}
