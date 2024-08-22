package com.mars.easy.admin.modules.system.service;

import com.mybatisflex.core.service.IService;
import com.mars.easy.admin.modules.system.pojo.dto.sysfile.SysFileListDTO;
import com.mars.easy.admin.modules.system.pojo.po.SysFile;
import com.mars.easy.admin.framework.entity.ApiResult;
import com.mars.easy.admin.framework.entity.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @Author 程序员Mars
 * @since 2023-08-31
 */
public interface SysFileService extends IService<SysFile> {

    /**
     * 文件列表
     *
     * @param dto dto
     * @return {@link List}<{@link SysFile}>
     */
    PageResult<SysFile> fileList(SysFileListDTO dto);

    /**
     * 上传文件
     *
     * @param file 文件
     * @return {@link ApiResult}
     */
    String uploadFile(MultipartFile file, String type);
}
