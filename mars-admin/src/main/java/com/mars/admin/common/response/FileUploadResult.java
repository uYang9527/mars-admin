package com.mars.admin.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 文件上传结果
 *
 * @author Mars.wq
 */
@Data
@Accessors(chain = true)
@Schema(description = "文件上传结果")
public class FileUploadResult {

    @Schema(description = "文件ID")
    private Long id;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "原始文件名")
    private String originalName;

    @Schema(description = "文件后缀")
    private String fileSuffix;

    @Schema(description = "文件URL")
    private String url;

    @Schema(description = "文件大小")
    private Long size;

    @Schema(description = "文件路径")
    private String filePath;

    @Schema(description = "文件类型")
    private String contentType;

    @Schema(description = "配置类型")
    private String configKey;

    @Schema(description = "上传状态")
    private Integer uploadStatus;
} 