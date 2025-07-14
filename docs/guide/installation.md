# 安装配置

本文档将指导您完成Mars Admin的安装和基础配置，确保项目能够正常运行。

## 环境要求

### 后端环境
- **Java**: JDK 17 或更高版本
- **Maven**: 3.6.0 或更高版本
- **MySQL**: 8.0 或更高版本
- **Redis**: 6.0 或更高版本

### 前端环境
- **Node.js**: 18.0 或更高版本
- **pnpm**: 8.0 或更高版本（推荐）
- **现代浏览器**: Chrome 90+, Firefox 88+, Safari 14+

## 获取源码

### 1. 克隆项目

```bash
# 使用 HTTPS
git clone https://github.com/your-org/mars-admin.git

# 或使用 SSH
git clone git@github.com:your-org/mars-admin.git

# 进入项目目录
cd mars-admin
```

### 2. 项目结构

```
mars-admin/
├── mars-admin-server/     # 后端项目
├── mars-admin-ui/         # 前端项目
├── docs/                  # 文档目录
├── sql/                   # 数据库脚本
├── docker/                # Docker配置
├── README.md
└── pom.xml               # Maven父项目配置
```

## 数据库配置

### 1. 创建数据库

```sql
-- 创建数据库
CREATE DATABASE mars_admin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户（可选）
CREATE USER 'mars_admin'@'%' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON mars_admin.* TO 'mars_admin'@'%';
FLUSH PRIVILEGES;
```

### 2. 导入数据

```bash
# 进入SQL目录
cd sql

# 导入基础结构
mysql -u root -p mars_admin < mars_admin_structure.sql

# 导入初始数据
mysql -u root -p mars_admin < mars_admin_data.sql

# 导入菜单数据
mysql -u root -p mars_admin < mars_admin_menu.sql
```

### 3. 验证数据

```sql
-- 连接数据库
mysql -u root -p mars_admin

-- 查看表结构
SHOW TABLES;

-- 验证用户数据
SELECT * FROM sys_user LIMIT 5;

-- 验证菜单数据
SELECT * FROM sys_menu WHERE parent_id = 0;
```

## 后端配置

### 1. 配置文件

复制配置模板：

```bash
cd mars-admin-server/src/main/resources

# 复制开发环境配置
cp application-dev.yml.template application-dev.yml

# 复制生产环境配置
cp application-prod.yml.template application-prod.yml
```

### 2. 数据库配置

编辑 `application-dev.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mars_admin?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 3. Redis配置

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: your_redis_password
    database: 0
    timeout: 10s
    lettuce:
      pool:
        max-active: 8
        max-wait: -1ms
        max-idle: 8
        min-idle: 0
```

### 4. 文件存储配置

```yaml
# MinIO配置
minio:
  endpoint: http://localhost:9000
  access-key: minioadmin
  secret-key: minioadmin
  bucket-name: mars-admin

# 本地存储配置
file:
  upload:
    path: /opt/mars-admin/upload
    max-size: 100MB
    allowed-types: jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx
```

### 5. JWT配置

```yaml
jwt:
  secret: mars-admin-secret-key-2024
  expire-time: 7200  # 2小时，单位：秒
  refresh-time: 604800  # 7天，单位：秒
```

## 前端配置

### 1. 安装依赖

```bash
cd mars-admin-ui

# 安装依赖
pnpm install

# 或使用 npm
npm install
```

### 2. 环境配置

复制环境配置文件：

```bash
# 复制开发环境配置
cp .env.development.template .env.development

# 复制生产环境配置
cp .env.production.template .env.production
```

### 3. 开发环境配置

编辑 `.env.development`：

```bash
# 应用标题
VITE_APP_TITLE=Mars Admin

# 应用描述
VITE_APP_DESC=基于Vue3的现代化后台管理系统

# API基础路径
VITE_BASE_URL=http://localhost:9999

# API前缀
VITE_API_PREFIX=/api

# 是否开启Mock
VITE_ENABLE_MOCK=false

# 是否开启代理
VITE_ENABLE_PROXY=true

# 代理目标地址
VITE_PROXY_TARGET=http://localhost:9999
```

### 4. 生产环境配置

编辑 `.env.production`：

```bash
# 应用标题
VITE_APP_TITLE=Mars Admin

# API基础路径
VITE_BASE_URL=https://your-domain.com

# API前缀
VITE_API_PREFIX=/api

# 是否开启Mock
VITE_ENABLE_MOCK=false

# 是否开启代理
VITE_ENABLE_PROXY=false
```

## 启动项目

### 1. 启动后端

```bash
cd mars-admin-server

# 使用Maven启动
mvn spring-boot:run

# 或者先编译再启动
mvn clean package -DskipTests
java -jar target/mars-admin-server-1.0.0.jar

# 指定环境
java -jar target/mars-admin-server-1.0.0.jar --spring.profiles.active=dev
```

### 2. 启动前端

```bash
cd mars-admin-ui

# 开发模式启动
pnpm dev

# 或使用 npm
npm run dev

# 指定端口启动
pnpm dev --port 3000
```

### 3. 验证启动

- **后端服务**: http://localhost:9999
- **前端应用**: http://localhost:5173
- **API文档**: http://localhost:9999/doc.html
- **Druid监控**: http://localhost:9999/druid/login.html

## 默认账号

系统提供以下默认账号用于初始登录：

| 角色 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 超级管理员 | admin | admin123 | 拥有所有权限 |
| 系统管理员 | system | system123 | 系统管理权限 |
| 普通用户 | user | user123 | 基础查看权限 |

**⚠️ 安全提醒**: 首次登录后请立即修改默认密码！

## 常见问题

### Q: 数据库连接失败

**解决方案**:
1. 检查MySQL服务是否启动
2. 验证数据库连接信息是否正确
3. 确认数据库用户权限
4. 检查防火墙设置

```bash
# 检查MySQL服务状态
systemctl status mysql

# 测试数据库连接
mysql -h localhost -u root -p
```

### Q: Redis连接失败

**解决方案**:
1. 检查Redis服务是否启动
2. 验证Redis连接配置
3. 检查Redis密码设置

```bash
# 检查Redis服务状态
systemctl status redis

# 测试Redis连接
redis-cli ping
```

### Q: 前端启动失败

**解决方案**:
1. 检查Node.js版本是否符合要求
2. 清除依赖缓存重新安装
3. 检查网络连接

```bash
# 检查Node.js版本
node --version

# 清除缓存
pnpm store prune
rm -rf node_modules
pnpm install
```

### Q: 后端启动失败

**解决方案**:
1. 检查Java版本是否符合要求
2. 验证配置文件格式
3. 检查端口占用情况

```bash
# 检查Java版本
java --version

# 检查端口占用
netstat -tlnp | grep 9999

# 查看详细错误日志
tail -f logs/mars-admin.log
```

## 下一步

安装完成后，您可以：

1. [查看项目结构](structure.md) - 了解项目的目录结构和组织方式
2. [开始本地开发](development.md) - 学习如何进行本地开发
3. [配置说明](configuration.md) - 了解详细的配置选项
4. [部署指南](deploy.md) - 学习如何部署到生产环境

## 技术支持

如果在安装过程中遇到问题，可以：

1. 查看 [常见问题](../faq.md)
2. 提交 [Issue](https://github.com/your-org/mars-admin/issues)
3. 加入技术交流群获取帮助
