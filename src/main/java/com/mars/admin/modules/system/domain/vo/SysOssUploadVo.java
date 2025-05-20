package com.mars.admin.modules.system.domain.vo;

import lombok.Data;

/**
 * 上传对象信息
 *
 * @author mars
 */
@Data
public class SysOssUploadVo {

    /**
     * URL地址
     */
    private String url;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 对象存储主键
     */
    private String ossId;

}
