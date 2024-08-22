package com.mars.easy.admin.modules.business.addressbook.service;

import com.mybatisflex.core.service.IService;
import com.mars.easy.admin.modules.business.addressbook.pojo.po.AddressBook;
import com.mars.easy.admin.framework.entity.SelectIdsDTO;
import com.mars.easy.admin.framework.entity.PageResult;
import java.util.List;
import com.mars.easy.admin.modules.business.addressbook.pojo.dto.AddressBookCreateDTO;
import com.mars.easy.admin.modules.business.addressbook.pojo.dto.AddressBookUpdateDTO;
import com.mars.easy.admin.modules.business.addressbook.pojo.dto.AddressBookListDTO;
import com.mars.easy.admin.modules.business.addressbook.pojo.vo.AddressBookVO;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 地址管理 Service
 *
 * @author Mars
 * @since 2024-08-22
 */
public interface AddressBookService extends IService<AddressBook> {

    void create(AddressBookCreateDTO dto);

    void update(AddressBookUpdateDTO dto);

    PageResult<AddressBookVO> page(AddressBookListDTO dto);

    List<AddressBookVO> list(AddressBookListDTO dto);

    void remove(SelectIdsDTO dto);

    AddressBookVO detail(Object id);

    void importExcel(MultipartFile file);

    void exportExcel(AddressBookListDTO dto, HttpServletResponse response);

}
