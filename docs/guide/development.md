# 本地开发

本章节将指导您如何在本地环境中启动和开发Mars Admin项目。

## 准备工作

在开始开发之前，请确保您已经完成了[环境准备](prepare.md)章节中的所有步骤，并且已经了解了[项目结构](structure.md)。

## 获取源码

### 克隆仓库

```bash
# 从Gitee克隆
git clone https://gitee.com/Marsfactory/mars-admin.git

# 或从GitHub克隆
git clone https://github.com/Marsfactory/mars-admin.git

# 进入项目目录
cd mars-admin
```

## 后端开发

### 数据库初始化

1. 创建数据库
   ```sql
   CREATE DATABASE mars_admin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
   ```

2. 导入SQL脚本
   - 使用MySQL客户端工具（如Navicat、DBeaver等）
   - 导入`mars-admin/sql/mars_admin.sql`脚本

### 配置修改

1. 修改数据库连接信息
   - 打开`mars-admin/src/main/resources/application-dev.yml`
   - 修改以下配置：
     ```yaml
     spring:
       datasource:
         url: jdbc:mysql://localhost:3306/mars_admin?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
         username: your_username
         password: your_password
     ```

2. 修改Redis配置（如需要）
   ```yaml
   spring:
     redis:
       host: 127.0.0.1
       port: 6379
       password: 
   ```

### 启动后端服务

#### 使用IDE启动（推荐）

1. 导入项目到IDE（IntelliJ IDEA或Eclipse）
2. 运行`com.mars.admin.MarsAdminApplication`类的main方法

#### 使用命令行启动

```bash
# 进入后端项目目录
cd mars-admin

# 编译打包
mvn clean package -DskipTests

# 运行
java -jar target/mars-admin.jar
```

### 验证后端服务

访问以下URL，确认服务是否正常启动：

- API文档：http://localhost:8080/doc.html
- 健康检查：http://localhost:8080/actuator/health

## 前端开发

### 安装依赖

```bash
# 进入前端项目目录
cd mars-admin-ui

# 使用pnpm安装依赖（推荐）
pnpm install

# 或使用npm
npm install
```

### 配置修改

1. 修改API请求地址（如需要）
   - 打开`mars-admin-ui/packages/axios/src/constant.ts`
   - 修改API基础URL：
     ```typescript
     export const BASE_URL = 'http://localhost:8080';
     ```

### 启动前端服务

```bash
# 开发模式启动
pnpm dev

# 或使用npm
npm run dev
```

启动成功后，可以通过浏览器访问：http://localhost:5173

### 构建前端项目

```bash
# 构建生产环境版本
pnpm build

# 或使用npm
npm run build
```

构建后的文件将生成在`dist`目录中。

## 开发流程

### Git工作流

推荐使用以下Git工作流：

1. 从主分支创建特性分支
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. 提交代码
   ```bash
   git add .
   git commit -m "feat: 添加新功能"
   ```

3. 推送到远程仓库
   ```bash
   git push origin feature/your-feature-name
   ```

4. 创建Pull Request/Merge Request

### 提交规范

提交信息请遵循以下格式：

```
<type>(<scope>): <subject>

<body>

<footer>
```

常用的`type`类型：

- `feat`: 新功能
- `fix`: 修复Bug
- `docs`: 文档更新
- `style`: 代码风格调整（不影响代码逻辑）
- `refactor`: 代码重构
- `perf`: 性能优化
- `test`: 测试相关
- `chore`: 构建过程或辅助工具变动

示例：
```
feat(user): 添加用户导出功能

- 支持Excel导出
- 支持选择导出字段

Closes #123
```

## 调试技巧

### 后端调试

1. 日志查看
   - 开发环境日志默认输出到控制台
   - 生产环境日志位于`logs`目录

2. SQL监控
   - 访问：http://localhost:8080/static/sqlLog.html
   - 可查看SQL执行情况和性能分析

### 前端调试

1. Vue Devtools
   - 安装[Vue Devtools浏览器扩展](https://devtools.vuejs.org/)
   - 可查看组件结构、状态等

2. 网络请求
   - 使用浏览器开发者工具的Network面板
   - 查看API请求和响应

## 常见问题

### 后端启动失败

1. 端口占用
   ```
   Web server failed to start. Port 8080 was already in use.
   ```
   解决方法：修改`application.yml`中的`server.port`配置，或关闭占用端口的程序

2. 数据库连接失败
   ```
   Failed to configure a DataSource: 'url' attribute is not specified
   ```
   解决方法：检查数据库配置是否正确

### 前端构建失败

1. 依赖安装问题
   ```
   ERR_PNPM_PEER_DEP_ISSUES
   ```
   解决方法：使用`pnpm install --no-strict-peer-dependencies`

2. Node版本过低
   ```
   The engine "node" is incompatible with this module
   ```
   解决方法：升级Node.js到要求的版本

## 下一步

完成本地开发环境搭建后，您可以参考[部署指南](deploy.md)了解如何部署项目到生产环境。 