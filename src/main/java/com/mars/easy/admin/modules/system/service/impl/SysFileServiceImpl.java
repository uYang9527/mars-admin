package com.mars.easy.admin.modules.system.service.impl;


import com.mars.easy.admin.framework.minio.MinioService;
import com.mars.easy.admin.framework.util.FileUploadUtils;
import com.mars.easy.admin.framework.util.PageUtils;
import com.mars.easy.admin.framework.util.Utils;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.mars.easy.admin.modules.system.mapper.CommonFileMapper;
import com.mars.easy.admin.modules.system.pojo.dto.sysfile.SysFileListDTO;
import com.mars.easy.admin.modules.system.pojo.po.SysFile;
import com.mars.easy.admin.modules.system.pojo.po.table.SysFileTableDef;
import com.mars.easy.admin.modules.system.service.SysFileService;
import com.mars.easy.admin.framework.entity.PageResult;
import io.minio.ObjectWriteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @Author 程序员Mars
 * @since 2023-08-31
 */
@Service
@RequiredArgsConstructor
public class SysFileServiceImpl extends ServiceImpl<CommonFileMapper, SysFile> implements SysFileService {


    private final MinioService minioService;

    /**
     * 文件列表
     *
     * @param dto dto
     * @return {@link PageResult}<{@link SysFile}>
     */
    @Override
    public PageResult<SysFile> fileList(SysFileListDTO dto) {
        QueryWrapper wrapper = QueryWrapper.create();
        if (Utils.isNotNull(dto.getFilename())) {
            wrapper.where(SysFileTableDef.SYS_FILE.FILENAME.like(dto.getFilename()));
        }
        wrapper.orderBy(SysFileTableDef.SYS_FILE.CREATE_TIME.desc());
        Page<SysFile> page = page(PageUtils.getPage(dto), wrapper);
        return PageUtils.getPageResult(page);
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @param type
     * @return {@link String}
     */
    @Override
    public String uploadFile(MultipartFile file, String type) {
        String fileUrl = "";
        try {
            // 文件名
            String filename = type + "/" + FileUploadUtils.generateFileName(file.getOriginalFilename());
            // minio 上传文件
            ObjectWriteResponse objectWriteResponse = minioService.uploadFile(file, filename, file.getContentType());
            // 获取上传文件url
            fileUrl = minioService.getPublicObjectUrl(objectWriteResponse.object());
            Map<String, String> map = new HashMap<>();
            map.put("filename", filename);
            map.put("type", type);
            map.put("url", fileUrl);
            fileLog(file, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    /**
     * 文件日志
     * 文件管理数据记录,收集管理追踪文件
     *
     * @param file     上传文件
     * @param fileInfo 文件信息
     */
    private void fileLog(MultipartFile file, Map<String, String> fileInfo) {
        SysFile sysFile = new SysFile();
        sysFile.setFilename(fileInfo.get("filename"));
        sysFile.setType(fileInfo.get("type"));
        sysFile.setSize(String.valueOf(file.getSize()));
        sysFile.setUrl(fileInfo.get("url"));
        this.save(sysFile);
    }
}
