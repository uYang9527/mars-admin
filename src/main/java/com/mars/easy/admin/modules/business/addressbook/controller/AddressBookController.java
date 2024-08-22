package com.mars.easy.admin.modules.business.addressbook.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import com.mars.easy.admin.framework.entity.ApiPageResult;
import com.mars.easy.admin.framework.entity.ApiResult;
import com.mars.easy.admin.framework.constant.GlobalConstant;
import com.mars.easy.admin.framework.entity.PageResult;
import com.mars.easy.admin.framework.entity.SelectIdsDTO;
import com.mars.easy.admin.modules.business.addressbook.service.AddressBookService;
import com.mars.easy.admin.modules.business.addressbook.pojo.dto.AddressBookCreateDTO;
import com.mars.easy.admin.modules.business.addressbook.pojo.dto.AddressBookUpdateDTO;
import com.mars.easy.admin.modules.business.addressbook.pojo.dto.AddressBookListDTO;
import com.mars.easy.admin.modules.business.addressbook.pojo.vo.AddressBookVO;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * 地址管理 Controller
 *
 * @author Mars
 * @since 2024-08-22
 */
@Tag(name =  "地址管理")
@RestController
@RequestMapping("address-book")
@RequiredArgsConstructor
public class AddressBookController  {

    private final AddressBookService addressBookService;

    @Operation(summary = "新增")
    @SaCheckPermission(value = "address.book.create", orRole = GlobalConstant.SUPER_ROLE)
    @PostMapping
    public ApiResult create(@RequestBody AddressBookCreateDTO dto) {
        addressBookService.create(dto);
        return ApiResult.success();
    }

    @Operation(summary = "修改")
    @SaCheckPermission(value = "address.book.update", orRole = GlobalConstant.SUPER_ROLE)
    @PutMapping
    public ApiResult update(@RequestBody AddressBookUpdateDTO dto) {
        addressBookService.update(dto);
        return ApiResult.success();
    }

    @Operation(summary = "删除")
    @SaCheckPermission(value = "address.book.remove", orRole = GlobalConstant.SUPER_ROLE)
    @DeleteMapping
    public ApiResult remove(@RequestBody SelectIdsDTO dto) {
        addressBookService.remove(dto);
        return ApiResult.success();
    }

    @Operation(summary = "列表查询")
    @SaCheckPermission(value = "address.book.query_table", orRole = GlobalConstant.SUPER_ROLE)
    @GetMapping
    public ApiResult<PageResult<AddressBookVO>> list(AddressBookListDTO dto) {
        return ApiPageResult.success(addressBookService.page(dto));
    }

    @Operation(summary = "详情")
    @SaCheckPermission(value = "address.book.query_table", orRole = GlobalConstant.SUPER_ROLE)
    @GetMapping("/{id}")
    public ApiResult<AddressBookVO> detail(@PathVariable Object id) {
        return ApiResult.success(addressBookService.detail(id));
    }

    @Operation(summary = "导入")
    @Parameters({
      @Parameter(name = "file", description = "上传文件", schema = @Schema(type = "string", format = "binary"), required = true),
    })
    @SaCheckPermission(value = "address.book.import", orRole = GlobalConstant.SUPER_ROLE)
    @PostMapping("/import")
    public void importExcel(MultipartFile file) {
        addressBookService.importExcel(file);
    }

    @Operation(summary = "导出")
    @SaCheckPermission(value = "address.book.export", orRole = GlobalConstant.SUPER_ROLE)
    @PostMapping("/export")
    public void exportExcel(@RequestBody AddressBookListDTO dto, HttpServletResponse response) {
        addressBookService.exportExcel(dto, response);
    }
}