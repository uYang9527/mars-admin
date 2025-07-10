package com.mars.admin.common.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 文件预览信息
 *
 * @author Mars.wq
 */
@Data
@Accessors(chain = true)
public class FilePreviewInfo {
    /**
     * 预览类型：1-直接预览(图片等)，2-Office预览，3-PDF预览，4-视频预览，5-音频预览，0-不支持预览
     */
    private Integer previewType;
    
    /**
     * 预览URL
     */
    private String previewUrl;
    
    /**
     * 文件内容类型
     */
    private String contentType;
    
    /**
     * 文件原始名称
     */
    private String originalName;
    
    /**
     * 文件大小
     */
    private Long size;
    
    /**
     * 是否需要转换
     */
    private Boolean needConvert = false;
    
    /**
     * 预览参数（如页面数、分辨率等）
     */
    private String previewParams;
} 