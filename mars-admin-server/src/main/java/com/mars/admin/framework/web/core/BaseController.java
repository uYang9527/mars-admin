package com.mars.admin.framework.web.core;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mars.admin.framework.core.domain.R;
import com.mars.admin.framework.core.utils.StringUtils;
import com.mars.admin.framework.mybatis.core.page.PageQuery;
import com.mars.admin.framework.web.utils.ApprenticeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

/**
 * web层通用数据处理
 *
 * @author Mars
 */
public class BaseController<S extends IService<E>, E> {

    @Autowired
    protected S baseService;

    @PostMapping("/insert")
    public R<Void> insert(@RequestBody E entity) {
        baseService.save(entity);
        return R.ok("添加成功");
    }

    @PostMapping("/deleteById")
    public R<Void> delete(@RequestBody List<Integer> ids) {

        baseService.removeByIds(ids);
        return R.ok("添加成功");
    }

    @PostMapping("/updateById")
    public R<Void> updateById(@RequestBody E entity) {
        baseService.updateById(entity);
        return R.ok("添加成功");
    }

    @GetMapping("/getById")
    public R<E> getById(@RequestParam Integer id) {

        return R.ok(baseService.getById(id));
    }

    @PostMapping("/save")
    public R<Void> save(@RequestBody E entity) {
        baseService.saveOrUpdate(entity);
        return R.ok("添加成功");
    }

    @PostMapping("/list")
    public R<List<E>> list(@RequestBody E entity) {
        QueryWrapper<E> queryWrapper = ApprenticeUtil.getQueryWrapper(entity);
        List<E> list = baseService.list(queryWrapper);
        return R.ok(list);
    }

    @PostMapping("/page")
    public R<Page<E>> page(@RequestBody PageQuery pageParamDto) {
        //限制条件
        if (pageParamDto.getPageNum() < 1) {
            pageParamDto.setPageNum(1);
        }

        if (pageParamDto.getPageSize() > 100) {
            pageParamDto.setPageSize(100);
        }
        Page<E> page = new Page<>(pageParamDto.getPageNum(), pageParamDto.getPageSize());
        QueryWrapper<E> queryWrapper = new QueryWrapper<>();
        //升序
        String asc = pageParamDto.getAsc();
        if (!StrUtil.isEmpty(asc) && !"null".equals(asc)) {
            String[] split = asc.split(",");
            queryWrapper.orderByAsc(Arrays.asList(split));
        }
        //降序
        String desc = pageParamDto.getDesc();
        if (!StrUtil.isEmpty(desc) && !"null".equals(desc)) {
            String[] split = desc.split(",");
            queryWrapper.orderByDesc(Arrays.asList(split));
        }
        Page<E> ePage = baseService.page(page, queryWrapper);
        return R.ok(ePage);
    }

    @PostMapping("/count")
    public R<Long> count(@RequestBody E entity) {
        QueryWrapper<E> queryWrapper = ApprenticeUtil.getQueryWrapper(entity);
        long count = baseService.count(queryWrapper);
        return R.ok(count);
    }


    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected R<Void> toAjax(int rows) {
        return rows > 0 ? R.ok() : R.fail();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected R<Void> toAjax(boolean result) {
        return result ? R.ok() : R.fail();
    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return StringUtils.format("redirect:{}", url);
    }

}
