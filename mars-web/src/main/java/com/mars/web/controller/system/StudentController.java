package com.mars.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mars.common.result.PageResult;
import com.mars.common.result.Result;
import com.mars.system.annotation.Log;
import com.mars.system.annotation.RepeatSubmit;
import com.mars.system.annotation.Log.BusinessType;
import com.mars.system.entity.Student;
import com.mars.system.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 学生管理
 * 
 * @author Mars
 * @date 2026-02-03
 */
@RestController
@RequestMapping("/system/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @SaCheckPermission("system:student:list")
    public Result<PageResult<Student>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long id) {
        var result = studentService.page(page, pageSize, id);
        return Result.ok(PageResult.of(result));
    }

    /**
     * 获取详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("system:student:query")
    public Result<Student> getInfo(@PathVariable Long id) {
        return Result.ok(studentService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping
    @SaCheckPermission("system:student:add")
    @RepeatSubmit
    @Log(title = "学生管理", businessType = BusinessType.INSERT)
    public Result<Void> add(@RequestBody Student student) {
        studentService.create(student);
        return Result.ok();
    }

    /**
     * 修改
     */
    @PutMapping
    @SaCheckPermission("system:student:edit")
    @Log(title = "学生管理", businessType = BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody Student student) {
        studentService.update(student);
        return Result.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    @SaCheckPermission("system:student:remove")
    @Log(title = "学生管理", businessType = BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long[] ids) {
        studentService.delete(ids);
        return Result.ok();
    }
}
