package com.mars.easy.admin.modules.business.category.service;

import com.mybatisflex.core.service.IService;
import com.mars.easy.admin.modules.business.category.pojo.po.Category;
import com.mars.easy.admin.framework.entity.SelectIdsDTO;
import com.mars.easy.admin.framework.entity.PageResult;
import java.util.List;
import com.mars.easy.admin.modules.business.category.pojo.dto.CategoryCreateDTO;
import com.mars.easy.admin.modules.business.category.pojo.dto.CategoryUpdateDTO;
import com.mars.easy.admin.modules.business.category.pojo.dto.CategoryListDTO;
import com.mars.easy.admin.modules.business.category.pojo.vo.CategoryVO;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 菜品及套餐分类 Service
 *
 * @author Mars
 * @since 2024-08-22
 */
public interface CategoryService extends IService<Category> {

    void create(CategoryCreateDTO dto);

    void update(CategoryUpdateDTO dto);

    PageResult<CategoryVO> page(CategoryListDTO dto);

    List<CategoryVO> list(CategoryListDTO dto);

    void remove(SelectIdsDTO dto);

    CategoryVO detail(Object id);

    void importExcel(MultipartFile file);

    void exportExcel(CategoryListDTO dto, HttpServletResponse response);

}
