# Mars Admin 项目说明文档

## 项目简介
Mars Admin 是一个基于 Spring Boot 的后台管理系统，提供了基础的管理功能和 API 接口。该项目包含用户认证、用户管理、角色管理、系统诊断等核心模块，适用于快速搭建企业级后台管理系统。

## 主要功能
- **用户认证**：提供登录、登出、获取用户信息等接口
- **用户管理**：实现用户的增删改查、分页查询等功能
- **系统诊断**：检查 OpenAPI 配置、依赖信息，提供文档访问链接
- **API 文档**：集成 OpenAPI 3，提供 API 文档访问地址
- **异常处理**：统一的异常处理机制，支持多种异常类型的捕获和响应
- **基础实体类**：提供包含公共字段的 BaseEntity，支持自动填充机制

## 技术栈
- Spring Boot
- MyBatis Flex
- SaToken（权限认证）
- OpenAPI 3
- Maven

## 快速开始

### 环境要求
- Java 17+
- Maven 3.8+
- MySQL 8.0+

### 项目启动
1. 克隆项目：
```bash
git clone [项目地址]
```

2. 进入项目目录：
```bash
cd mars-admin
```

3. 构建项目：
```bash
mvn clean install
```

4. 启动应用：
```bash
mvn spring-boot:run
```

## API 文档访问
项目集成 Knife4j 提供 API 文档可视化界面，访问地址：
```
http://localhost:8080/doc.html
```

## 系统诊断
提供系统配置和依赖状态检查接口：
```
GET /diagnostic/openapi   # 检查OpenAPI配置
GET /diagnostic/dependencies # 检查依赖信息
GET /diagnostic/links      # 获取文档访问链接
```

## 用户认证
```
POST /auth/login           # 用户登录
POST /auth/logout          # 用户登出
GET /auth/userinfo         # 获取当前用户信息
```

## 用户管理
```
GET /user/list             # 获取用户列表
GET /user/page             # 分页查询用户
GET /user/{id}             # 根据ID获取用户
POST /user                 # 新增用户
PUT /user                  # 更新用户
DELETE /user/{id}          # 删除用户
```

## 测试接口
```
GET /test/hello            # Hello World 测试
GET /test/time             # 获取服务器时间
GET /test/echo             # 消息回显测试
GET /test/status           # 系统状态检查
```

## 开发规范
- Git 提交遵循 [Conventional Commits](https://www.conventionalcommits.org/) 规范
- 使用 Lombok 简化 Java Bean 开发
- 使用 Swagger 注解规范 API 文档
- 统一返回结果格式 Result<T>

## 常见问题
### 端口被占用
Windows 系统可使用以下命令查找并终止占用端口的进程：
```bash
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### 依赖冲突
清理并重新编译项目：
```bash
mvn clean install -U
```

### SaToken 拦截文档路径
检查 SaToken 配置，确保放行 `/doc.html` 和 `/v3/api-docs` 等相关路径

## 贡献指南
欢迎贡献代码，请遵循以下步骤：
1. Fork 项目
2. 创建新分支
3. 提交代码改动
4. 编写清晰的提交信息
5. 提交 Pull Request

## 开源协议
本项目采用 MIT License 协议，详细信息请参阅 LICENSE 文件。

## 联系方式
如有任何问题或建议，请提交 Issue 或联系项目维护者。

## 致谢
感谢以下开源项目的贡献：
- Spring Boot
- MyBatis Flex
- SaToken
- Knife4j
- Swagger OpenAPI

## 版本更新
详细版本更新记录请参阅 CHANGELOG.md 文件。