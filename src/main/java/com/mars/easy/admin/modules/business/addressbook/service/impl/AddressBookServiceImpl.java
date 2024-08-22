package com.mars.easy.admin.modules.business.addressbook.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.mars.easy.admin.modules.business.addressbook.service.AddressBookService;
import com.mars.easy.admin.modules.business.addressbook.pojo.po.AddressBook;
import com.mars.easy.admin.modules.business.addressbook.mapper.AddressBookMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.query.QueryChain;
import com.mars.easy.admin.framework.enums.CommonResponseEnum;
import com.mars.easy.admin.framework.util.PageUtils;
import com.mars.easy.admin.framework.util.BeanCopyUtils;
import com.mars.easy.admin.framework.util.Utils;
import com.mars.easy.admin.framework.entity.PageResult;
import com.mars.easy.admin.framework.entity.SelectIdsDTO;
import java.io.Serializable;
import java.util.List;
import com.mars.easy.admin.modules.business.addressbook.pojo.dto.AddressBookCreateDTO;
import com.mars.easy.admin.modules.business.addressbook.pojo.dto.AddressBookUpdateDTO;
import com.mars.easy.admin.modules.business.addressbook.pojo.dto.AddressBookListDTO;
import com.mars.easy.admin.modules.business.addressbook.pojo.dto.AddressBookImportDTO;
import org.springframework.web.multipart.MultipartFile;
import com.mars.easy.admin.framework.excel.core.ExcelResult;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import com.mars.easy.admin.framework.excel.utils.ExcelUtils;
import lombok.SneakyThrows;
import com.mars.easy.admin.modules.business.addressbook.pojo.vo.AddressBookVO;

/**
 * 地址管理 服务实现类
 *
 * @author Mars
 * @since 2024-08-22
 */
@Service
@RequiredArgsConstructor
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
    @Override
    public void create(AddressBookCreateDTO dto){
        AddressBook addressBook = BeanCopyUtils.copy(dto, AddressBook.class);
        save(addressBook);
    }

    @Override
    public void update(AddressBookUpdateDTO dto){
        AddressBook addressBook = BeanCopyUtils.copy(dto, AddressBook.class);
        QueryWrapper wrapper;
        // id有效性校验
        wrapper = QueryWrapper.create()
            .eq(AddressBook::getId, dto.getId());
        CommonResponseEnum.INVALID_ID.assertTrue(count(wrapper) <= 0);

        saveOrUpdate(addressBook);
    }

    @Override
    public PageResult<AddressBookVO> page(AddressBookListDTO dto){
        Page<AddressBookVO> page = pageAs(PageUtils.getPage(dto), buildQueryWrapper(dto), AddressBookVO.class);
        return PageUtils.getPageResult(page);
    }

    @Override
    public List<AddressBookVO> list(AddressBookListDTO dto){
        return listAs(buildQueryWrapper(dto), AddressBookVO.class);
    }

    @Override
    public void remove(SelectIdsDTO dto){
        CommonResponseEnum.INVALID_ID.assertTrue(dto.getIds().isEmpty());
        removeByIds(dto.getIds());
    }

    @Override
    public AddressBookVO detail(Object id){
        AddressBook addressBook = getById((Serializable) id);
        CommonResponseEnum.INVALID_ID.assertNull(addressBook);
        return BeanCopyUtils.copy(addressBook, AddressBookVO.class);
    }

    @SneakyThrows
    @Override
    public void importExcel(MultipartFile file) {
        ExcelResult<AddressBookImportDTO> excelResult = ExcelUtils.importExcel(file.getInputStream(), AddressBookImportDTO.class, true);
        List<AddressBookImportDTO> list = excelResult.getList();
        List<String> errorList = excelResult.getErrorList();
        String analysis = excelResult.getAnalysis();
        System.out.println(" analysis : " + analysis);
    }

    @SneakyThrows
    @Override
    public void exportExcel(AddressBookListDTO dto, HttpServletResponse response) {
        List<AddressBookVO> list = list(dto);
        ServletOutputStream os = response.getOutputStream();
        ExcelUtils.exportExcel(list, "地址管理", AddressBookVO.class, os);
    }

    private static QueryWrapper buildQueryWrapper(AddressBookListDTO dto) {
        QueryWrapper wrapper = QueryWrapper.create().from(AddressBook.class);
        wrapper.eq(AddressBook::getUserId, dto.getUserId());
        wrapper.eq(AddressBook::getConsignee, dto.getConsignee());
        wrapper.eq(AddressBook::getSex, dto.getSex());
        wrapper.eq(AddressBook::getPhone, dto.getPhone());
        wrapper.eq(AddressBook::getProvinceCode, dto.getProvinceCode());
        wrapper.like(AddressBook::getProvinceName, dto.getProvinceName());
        wrapper.eq(AddressBook::getCityCode, dto.getCityCode());
        wrapper.like(AddressBook::getCityName, dto.getCityName());
        wrapper.eq(AddressBook::getDistrictCode, dto.getDistrictCode());
        wrapper.like(AddressBook::getDistrictName, dto.getDistrictName());
        wrapper.eq(AddressBook::getDetail, dto.getDetail());
        wrapper.eq(AddressBook::getLabel, dto.getLabel());
        wrapper.eq(AddressBook::getIsDefault, dto.getIsDefault());
        wrapper.eq(AddressBook::getCreateUser, dto.getCreateUser());
        wrapper.eq(AddressBook::getUpdateUser, dto.getUpdateUser());
        wrapper.eq(AddressBook::getIsDeleted, dto.getIsDeleted());
        return wrapper;
    }
}
