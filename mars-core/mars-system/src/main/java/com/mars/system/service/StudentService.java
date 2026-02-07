package com.mars.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.system.entity.Student;

/**
 * 学生管理 Service
 * 
 * @author Mars
 * @date 2026-02-03
 */
public interface StudentService {

    /**
     * 分页查询
     */
    Page<Student> page(Integer page, Integer pageSize, Long id);

    /**
     * 根据ID查询
     */
    Student getById(Long id);

    /**
     * 新增
     */
    void create(Student student);

    /**
     * 修改
     */
    void update(Student student);

    /**
     * 删除
     */
    void delete(Long[] ids);
}
