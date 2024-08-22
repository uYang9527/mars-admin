package com.mars.easy.admin.modules.business.category.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.mars.easy.admin.modules.business.category.service.CategoryService;
import com.mars.easy.admin.modules.business.category.pojo.po.Category;
import com.mars.easy.admin.modules.business.category.mapper.CategoryMapper;
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
import com.mars.easy.admin.modules.business.category.pojo.dto.CategoryCreateDTO;
import com.mars.easy.admin.modules.business.category.pojo.dto.CategoryUpdateDTO;
import com.mars.easy.admin.modules.business.category.pojo.dto.CategoryListDTO;
import com.mars.easy.admin.modules.business.category.pojo.dto.CategoryImportDTO;
import org.springframework.web.multipart.MultipartFile;
import com.mars.easy.admin.framework.excel.core.ExcelResult;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import com.mars.easy.admin.framework.excel.utils.ExcelUtils;
import lombok.SneakyThrows;
import com.mars.easy.admin.modules.business.category.pojo.vo.CategoryVO;

/**
 * 菜品及套餐分类 服务实现类
 *
 * @author Mars
 * @since 2024-08-22
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Override
    public void create(CategoryCreateDTO dto){
        Category category = BeanCopyUtils.copy(dto, Category.class);
        save(category);
    }

    @Override
    public void update(CategoryUpdateDTO dto){
        Category category = BeanCopyUtils.copy(dto, Category.class);
        QueryWrapper wrapper;
        // id有效性校验
        wrapper = QueryWrapper.create()
            .eq(Category::getId, dto.getId());
        CommonResponseEnum.INVALID_ID.assertTrue(count(wrapper) <= 0);

        saveOrUpdate(category);
    }

    @Override
    public PageResult<CategoryVO> page(CategoryListDTO dto){
        Page<CategoryVO> page = pageAs(PageUtils.getPage(dto), buildQueryWrapper(dto), CategoryVO.class);
        return PageUtils.getPageResult(page);
    }

    @Override
    public List<CategoryVO> list(CategoryListDTO dto){
        return listAs(buildQueryWrapper(dto), CategoryVO.class);
    }

    @Override
    public void remove(SelectIdsDTO dto){
        CommonResponseEnum.INVALID_ID.assertTrue(dto.getIds().isEmpty());
        removeByIds(dto.getIds());
    }

    @Override
    public CategoryVO detail(Object id){
        Category category = getById((Serializable) id);
        CommonResponseEnum.INVALID_ID.assertNull(category);
        return BeanCopyUtils.copy(category, CategoryVO.class);
    }

    @SneakyThrows
    @Override
    public void importExcel(MultipartFile file) {
        ExcelResult<CategoryImportDTO> excelResult = ExcelUtils.importExcel(file.getInputStream(), CategoryImportDTO.class, true);
        List<CategoryImportDTO> list = excelResult.getList();
        List<String> errorList = excelResult.getErrorList();
        String analysis = excelResult.getAnalysis();
        System.out.println(" analysis : " + analysis);
    }

    @SneakyThrows
    @Override
    public void exportExcel(CategoryListDTO dto, HttpServletResponse response) {
        List<CategoryVO> list = list(dto);
        ServletOutputStream os = response.getOutputStream();
        ExcelUtils.exportExcel(list, "菜品及套餐分类", CategoryVO.class, os);
    }

    private static QueryWrapper buildQueryWrapper(CategoryListDTO dto) {
        QueryWrapper wrapper = QueryWrapper.create().from(Category.class);
        wrapper.eq(Category::getType, dto.getType());
        wrapper.like(Category::getName, dto.getName());
        wrapper.eq(Category::getSort, dto.getSort());
        wrapper.eq(Category::getCreateUser, dto.getCreateUser());
        wrapper.eq(Category::getUpdateUser, dto.getUpdateUser());
        return wrapper;
    }
}
