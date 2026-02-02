package com.mars.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mars.system.entity.Student;
import org.apache.ibatis.annotations.Mapper;

/**
 * 学生管理 Mapper
 * 
 * @author Mars
 * @date 2026-02-02
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

}
