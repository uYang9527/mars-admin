package com.mars.admin.modules.system.service.impl;

import com.mars.admin.common.response.FileUploadResult;
import com.mars.admin.modules.system.entity.SysOss;
import com.mars.admin.modules.system.entity.SysOssConfig;
import com.mars.admin.framework.oss.factory.FileUploadStrategyFactory;
import com.mars.admin.framework.oss.strategy.FileUploadStrategy;
import com.mars.admin.modules.system.mapper.SysOssMapper;
import com.mars.admin.modules.system.service.IFileUploadService;
import com.mars.admin.modules.system.service.ISysOssConfigService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

import static com.mars.admin.modules.system.entity.table.SysOssTableDef.SYS_OSS;


/**
 * 文件上传服务实现类
 *
 * @author Mars.wq
 */
@Slf4j
@Service
public class FileUploadServiceImpl extends ServiceImpl<SysOssMapper, SysOss> implements IFileUploadService {

    @Autowired
    private FileUploadStrategyFactory strategyFactory;

    @Autowired
    private ISysOssConfigService ossConfigService;

    @Value("${file.upload.default-config:local}")
    private String defaultConfigKey;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileUploadResult upload(MultipartFile file) {
        return upload(file, defaultConfigKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileUploadResult upload(MultipartFile file, String configKey) {
        // 获取OSS配置
        SysOssConfig config = ossConfigService.selectConfigByKey(configKey);
        if (config == null) {
            throw new RuntimeException("OSS配置不存在: " + configKey);
        }

        if (config.getStatus() == 0) {
            throw new RuntimeException("OSS配置已停用: " + configKey);
        }

        // 获取上传策略
        FileUploadStrategy strategy = strategyFactory.getStrategy(configKey);

        // 执行上传
        FileUploadResult result = strategy.upload(file, config);

        // 保存文件记录
        SysOss sysOss = new SysOss();
        BeanUtils.copyProperties(result, sysOss);
        this.save(sysOss);

        // 设置文件ID
        result.setId(sysOss.getId());

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileUploadResult upload(InputStream inputStream, String fileName, String contentType, String configKey) {
        // 获取OSS配置
        SysOssConfig config = ossConfigService.selectConfigByKey(configKey);
        if (config == null) {
            throw new RuntimeException("OSS配置不存在: " + configKey);
        }

        if (config.getStatus() == 0) {
            throw new RuntimeException("OSS配置已停用: " + configKey);
        }

        // 获取上传策略
        FileUploadStrategy strategy = strategyFactory.getStrategy(configKey);

        // 执行上传
        FileUploadResult result = strategy.upload(inputStream, fileName, contentType, config);

        // 保存文件记录
        SysOss sysOss = new SysOss();
        BeanUtils.copyProperties(result, sysOss);
        this.save(sysOss);

        // 设置文件ID
        result.setId(sysOss.getId());

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        SysOss sysOss = this.getById(id);
        if (sysOss == null) {
            return false;
        }

        // 删除云端文件
        try {
            SysOssConfig config = ossConfigService.selectConfigByKey(sysOss.getConfigKey());
            if (config != null) {
                FileUploadStrategy strategy = strategyFactory.getStrategy(sysOss.getConfigKey());
                strategy.delete(sysOss.getFileName(), config);
            }
        } catch (Exception e) {
            log.warn("删除云端文件失败: {}", sysOss.getFileName(), e);
            // 继续删除数据库记录
        }

        // 删除数据库记录
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Long> ids) {
        for (Long id : ids) {
            delete(id);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByFileName(String fileName, String configKey) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(SYS_OSS.FILE_NAME.eq(fileName))
                .and(SYS_OSS.CONFIG_KEY.eq(configKey))
                .and(SYS_OSS.IS_DELETED.eq(0));

        SysOss sysOss = this.getOne(queryWrapper);
        if (sysOss != null) {
            return delete(sysOss.getId());
        }
        return false;
    }

    @Override
    public String getPresignedUrl(Long id, int expireTime) {
        SysOss sysOss = this.getById(id);
        if (sysOss == null) {
            return null;
        }
        return getPresignedUrl(sysOss.getFileName(), sysOss.getConfigKey(), expireTime);
    }

    @Override
    public String getPresignedUrl(String fileName, String configKey, int expireTime) {
        try {
            SysOssConfig config = ossConfigService.selectConfigByKey(configKey);
            if (config == null) {
                return null;
            }

            FileUploadStrategy strategy = strategyFactory.getStrategy(configKey);
            return strategy.getPresignedUrl(fileName, config, expireTime);
        } catch (Exception e) {
            log.error("获取预签名URL失败: {}", fileName, e);
            return null;
        }
    }

    @Override
    public Page<SysOss> selectFilePage(Page<SysOss> page, QueryWrapper queryWrapper) {
        return this.page(page, queryWrapper);
    }

    @Override
    public SysOss getFileById(Long id) {
        return this.getById(id);
    }

    @Override
    public java.util.Set<String> getAvailableTypes() {
        return strategyFactory.getAllTypes();
    }
}
