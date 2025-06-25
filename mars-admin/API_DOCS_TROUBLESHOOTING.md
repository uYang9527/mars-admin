# API文档访问故障排除指南

## 🔍 问题诊断步骤

### 1. 首先检查应用是否正常启动

```bash
# 在mars-admin目录下执行
mvn spring-boot:run
```

确保应用在端口9022上正常启动，没有报错。

### 2. 验证基础接口是否可访问

访问以下测试接口（无需登录）：

- **测试接口**: http://localhost:9022/test/hello
- **系统状态**: http://localhost:9022/test/status
- **系统诊断**: http://localhost:9022/diagnostic/dependencies

如果这些接口返回正常JSON响应，说明应用基本正常。

### 3. 检查OpenAPI配置

访问诊断接口：
- **OpenAPI配置检查**: http://localhost:9022/diagnostic/openapi
- **获取文档链接**: http://localhost:9022/diagnostic/links

### 4. 尝试访问API文档

按以下顺序尝试访问：

1. **OpenAPI JSON**: http://localhost:9022/v3/api-docs
2. **Swagger UI**: http://localhost:9022/swagger-ui.html
3. **Knife4j UI**: http://localhost:9022/doc.html

## 🚨 常见问题及解决方案

### 问题1: 端口被占用
**症状**: 应用启动失败，提示端口9022被占用

**解决方案**:
```bash
# Windows: 查找占用端口的进程
netstat -ano | findstr :9022

# 终止进程（替换PID为实际进程ID）
taskkill /PID <PID> /F
```

### 问题2: 依赖冲突
**症状**: 启动时出现类找不到或版本冲突错误

**解决方案**:
```bash
# 清理并重新编译
mvn clean compile

# 查看依赖树
mvn dependency:tree
```

### 问题3: SaToken拦截了文档路径
**症状**: 访问文档页面返回401未授权

**解决方案**: 检查 `SaTokenConfiguration.java` 中的路径排除配置是否包含：
```java
"/doc.html",        // knife4j文档
"/swagger-ui.html", // swagger文档
"/swagger-ui/**",   // swagger ui 静态资源
"/v3/api-docs",     // OpenAPI 3 文档
"/v3/api-docs/**",  // OpenAPI 3 文档子路径
```

### 问题4: OpenAPI配置未生效
**症状**: 访问 `/v3/api-docs` 返回404

**解决方案**: 检查 `application.yml` 配置：
```yaml
springdoc:
  api-docs:
    enabled: true
    path: /v3/api-docs
  swagger-ui:
    enabled: true
    path: /swagger-ui.html
```

### 问题5: Knife4j页面空白或加载失败
**症状**: `/doc.html` 页面打开但内容空白

**解决方案**:
1. 检查浏览器控制台是否有JavaScript错误
2. 确认 `knife4j.enable: true` 配置正确
3. 尝试清除浏览器缓存

## 🔧 配置检查清单

### Maven依赖检查
- [ ] `knife4j-openapi3-jakarta-spring-boot-starter` 版本4.4.0
- [ ] 没有旧的Swagger 2依赖冲突
- [ ] Spring Boot版本为3.2.0

### 配置文件检查
- [ ] `application.yml` 中SpringDoc配置正确
- [ ] `application.yml` 中Knife4j配置正确
- [ ] `OpenApiConfig.java` 配置类存在

### 安全配置检查
- [ ] `SaTokenConfiguration.java` 排除了必要的文档路径
- [ ] 没有其他安全过滤器拦截文档路径

### 注解检查
- [ ] 控制器使用了正确的OpenAPI 3注解
- [ ] 没有混用Swagger 2和OpenAPI 3注解

## 📋 快速验证命令

```bash
# 1. 检查应用是否启动
curl http://localhost:9022/test/hello

# 2. 检查OpenAPI JSON
curl http://localhost:9022/v3/api-docs

# 3. 检查系统诊断
curl http://localhost:9022/diagnostic/dependencies

# 4. 检查配置状态
curl http://localhost:9022/diagnostic/openapi
```

## 🆘 如果问题仍然存在

1. **查看应用日志**: 检查控制台输出中的错误信息
2. **检查浏览器网络面板**: 查看请求是否成功发送
3. **验证Java和Maven版本**: 确保使用JDK 17+和Maven 3.8.0+
4. **重新编译项目**: `mvn clean install`

## 📞 成功访问的标志

当一切正常时，您应该能够：

1. ✅ 访问 http://localhost:9022/test/hello 并看到JSON响应
2. ✅ 访问 http://localhost:9022/v3/api-docs 并看到OpenAPI规范
3. ✅ 访问 http://localhost:9022/swagger-ui.html 并看到Swagger UI界面
4. ✅ 访问 http://localhost:9022/doc.html 并看到Knife4j界面
5. ✅ 在文档界面中看到所有定义的API接口

如果以上步骤都能正常访问，说明API文档系统已经正常工作！ 