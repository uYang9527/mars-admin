package com.mars.admin.controller.base;

import com.mars.admin.framework.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * API文档访问控制器
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-07-05
 */
@Tag(name = "API文档", description = "API文档相关信息")
@RestController
@RequestMapping("/api")
public class ApiDocController {

    @Operation(summary = "获取API文档访问地址", description = "获取API文档的访问地址信息")
    @GetMapping("/doc")
    public Result<Map<String, String>> getApiDocInfo() {
        Map<String, String> data = new HashMap<>();
        data.put("knife4j", "http://localhost:9022/doc.html");
        data.put("swagger-ui", "http://localhost:9022/swagger-ui.html");
        data.put("description", "请访问上述地址查看API文档");
        return Result.success("获取API文档地址成功", data);
    }

    @Operation(summary = "系统信息", description = "获取系统基本信息")
    @GetMapping("/info")
    public Result<Map<String, String>> getSystemInfo() {
        Map<String, String> data = new HashMap<>();
        data.put("project", "Mars ORM MyBatis-Flex");
        data.put("version", "1.0.0");
        data.put("description", "基于MyBatis-Flex的ORM框架演示项目");
        data.put("author", "Mars.wq");
        return Result.success("获取系统信息成功", data);
    }
} 