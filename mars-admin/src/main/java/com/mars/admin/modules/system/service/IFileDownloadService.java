package com.mars.admin.modules.system.service;

import com.mars.admin.common.response.FilePreviewInfo;
import com.mars.admin.modules.system.entity.SysOss;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件下载服务接口
 *
 * @author Mars.wq
 */
public interface IFileDownloadService {

    /**
     * 文件下载结果类
     */
    class DownloadResult {
        private InputStream inputStream;

        public InputStream getInputStream() {
            return inputStream;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }
    }

    /**
     * 获取文件信息
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    SysOss getFileInfo(Long fileId);

    /**
     * 下载文件
     *
     * @param fileId 文件ID
     * @return 下载结果
     * @throws IOException IO异常
     */
    DownloadResult downloadFile(Long fileId) throws IOException;

    /**
     * 获取文件预览信息
     *
     * @param fileId 文件ID
     * @return 预览信息
     * @throws IOException IO异常
     */
    FilePreviewInfo getPreviewInfo(Long fileId) throws IOException;
}
