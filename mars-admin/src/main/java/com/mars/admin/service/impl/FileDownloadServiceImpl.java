package com.mars.admin.service.impl;

import com.mars.admin.common.response.FilePreviewInfo;
import com.mars.admin.entity.SysOss;
import com.mars.admin.entity.SysOssConfig;
import com.mars.admin.framework.oss.factory.FileUploadStrategyFactory;
import com.mars.admin.framework.oss.strategy.FileUploadStrategy;
import com.mars.admin.mapper.SysOssMapper;
import com.mars.admin.service.IFileDownloadService;
import com.mars.admin.service.ISysOssConfigService;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;

import static com.mars.admin.entity.table.SysOssTableDef.SYS_OSS;

/**
 * 文件下载服务实现类
 *
 * @author Mars.wq
 */
@Slf4j
@Service
public class FileDownloadServiceImpl implements IFileDownloadService {

    @Autowired
    private SysOssMapper sysOssMapper;

    @Autowired
    private ISysOssConfigService ossConfigService;

    @Autowired
    private FileUploadStrategyFactory strategyFactory;

    @Override
    public SysOss getFileInfo(Long fileId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(SYS_OSS.ID.eq(fileId))
                .and(SYS_OSS.IS_DELETED.eq(0));
        return sysOssMapper.selectOneByQuery(queryWrapper);
    }

    @Override
    @Transactional(readOnly = true)
    public DownloadResult downloadFile(Long fileId) throws IOException {
        // 获取文件信息
        SysOss fileInfo = getFileInfo(fileId);
        if (fileInfo == null) {
            log.error("文件不存在, ID: {}", fileId);
            throw new IOException("文件不存在");
        }

        // 获取OSS配置
        SysOssConfig config = ossConfigService.selectConfigByKey(fileInfo.getConfigKey());
        if (config == null) {
            log.error("存储配置不存在: {}", fileInfo.getConfigKey());
            throw new IOException("存储配置不存在");
        }

        // 获取上传策略
        FileUploadStrategy strategy = strategyFactory.getStrategy(fileInfo.getConfigKey());

        // 获取文件流
        InputStream inputStream = strategy.getFileInputStream(fileInfo.getFilePath(), config);

        // 设置下载结果
        DownloadResult result = new DownloadResult();
        result.setInputStream(inputStream);
        return result;
    }

    @Override
    public FilePreviewInfo getPreviewInfo(Long fileId) throws IOException {
        // 获取文件信息
        SysOss fileInfo = getFileInfo(fileId);
        if (fileInfo == null) {
            log.error("文件不存在, ID: {}", fileId);
            throw new IOException("文件不存在");
        }

        // 获取OSS配置
        SysOssConfig config = ossConfigService.selectConfigByKey(fileInfo.getConfigKey());
        if (config == null) {
            log.error("存储配置不存在: {}", fileInfo.getConfigKey());
            throw new IOException("存储配置不存在");
        }

        // 获取上传策略
        FileUploadStrategy strategy = strategyFactory.getStrategy(fileInfo.getConfigKey());

        // 获取预览信息
        return strategy.getPreviewInfo(
            fileInfo.getFilePath(), 
            fileInfo.getOriginalName(), 
            fileInfo.getContentType(), 
            fileInfo.getSize(), 
            config
        );
    }
} 