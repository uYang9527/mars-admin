package com.mars.easy.admin.modules.system.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.mars.easy.admin.modules.system.pojo.dto.sysfile.SysFileListDTO;
import com.mars.easy.admin.modules.system.pojo.po.SysFile;
import com.mars.easy.admin.modules.system.service.SysFileService;
import com.mars.easy.admin.framework.entity.ApiPageResult;
import com.mars.easy.admin.framework.entity.ApiResult;
import com.mars.easy.admin.framework.entity.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 公共文件 前端控制器
 * </p>
 *
 * @Author 程序员Mars
 * @since 2023-08-31
 */
@Tag(name =  "系统公共文件管理")
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys-file")
public class SysFileController {

    private final SysFileService sysFileService;

    @Operation(summary = "列表查询")
    @GetMapping
    public ApiPageResult<PageResult<SysFile>> list(SysFileListDTO dto) {
        return ApiPageResult.success(sysFileService.fileList(dto));
    }

    @SaIgnore
    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public ApiResult upload(@RequestParam MultipartFile file, @RequestParam(value = "type") String type) {
        return ApiResult.success(sysFileService.uploadFile(file, type));
    }

}
