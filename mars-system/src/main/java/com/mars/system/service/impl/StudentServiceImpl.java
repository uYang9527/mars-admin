package com.mars.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.system.entity.Student;
import com.mars.system.mapper.StudentMapper;
import com.mars.system.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * 学生管理 Service 实现
 * 
 * @author Mars
 * @date 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentMapper studentMapper;

    @Override
    public Page<Student> page(Integer page, Integer pageSize, Long id) {
        Page<Student> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        if (id != null) {
            wrapper.eq(Student::getId, id);
        }
        wrapper.orderByDesc(Student::getId);
        return studentMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public Student getById(Long id) {
        return studentMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Student student) {
        studentMapper.insert(student);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Student student) {
        studentMapper.updateById(student);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        studentMapper.deleteBatchIds(Arrays.asList(ids));
    }
}
