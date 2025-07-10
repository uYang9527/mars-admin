

<h1 align="center">Mars-Admin 企业级管理系统</h1>

<div align="center">

![Mars-Admin](https://img.shields.io/badge/Mars--Admin-v1.0.0-brightgreen.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0.5-brightgreen.svg)
![Vue](https://img.shields.io/badge/Vue-3.5.13-brightgreen.svg)
![UniApp](https://img.shields.io/badge/UniApp-2.0-blue.svg)
![Java](https://img.shields.io/badge/Java-17-orange.svg)
![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)

**🔥 开箱即用的企业级全栈管理系统 🔥**

一套基于 Spring Boot 3 + Vue 3 + UniApp 的现代化企业管理平台，采用前后端分离架构，提供完整的权限管理、用户管理、移动端支持等功能，是中小企业快速开发的理想选择。

[🌐 在线预览](https://web.marsadmin.cn/) | [📱 移动端预览](https://web.marsadmin.cn/) | [📖 API文档](https://web.marsadmin.cn/doc.html) | [📚 使用文档](./docs/)

**体验账号**: `admin` | **密码**: `123456`

</div>

## ✨ 项目特色

- 🎯 **全栈解决方案**：后端API + Web管理端 + 移动端小程序，三端统一
- 🚀 **技术前沿**：基于Spring Boot 3 + Vue 3 + UniApp最新技术栈
- 🎨 **现代化UI**：Web端基于Ant Design Vue 4，移动端采用毛玻璃设计风格
- 🔐 **安全可靠**：Sa-Token权限认证，Redis会话管理，完善的RBAC权限模型
- 📊 **SQL监控**：彩色SQL日志，性能监控，慢SQL检测，实时监控系统
- 🛠️ **开发友好**：代码规范，注释完整，易于二次开发
- 📱 **多端支持**：支持微信小程序、H5、App等多平台部署
- 🎪 **任务调度**：集成Snail-Job分布式任务调度平台

## 🏗️ 系统架构

### 整体架构
```
Mars-Admin 企业级管理系统
├── mars-admin/                    # 后端API服务 (Spring Boot 3)
├── mars-admin-ui/                 # Web管理端 (Vue 3 + Ant Design Vue)
├── mars-admin-uniapp/             # 移动端小程序 (UniApp)
├── mars-admin-web/                # 静态Web页面
├── snail-job/                     # 分布式任务调度平台
├── docs/                          # 项目文档
└── upload/                        # 文件上传目录
```

### 技术架构图

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   移动端 UniApp   │    │   Web管理端      │    │   静态Web页面    │
│                 │    │   (Vue 3)       │    │                 │
│  • 微信小程序    │    │  • Ant Design    │    │  • 企业官网      │
│  • 支付宝小程序  │    │  • TypeScript    │    │  • 产品展示      │
│  • H5/App      │    │  • Vite + UnoCSS │    │  • 在线体验      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │   后端API服务    │
                    │  (Spring Boot 3) │
                    │                 │
                    │  • RESTful API  │
                    │  • Sa-Token认证  │
                    │  • MyBatis-Flex │
                    │  • Redis缓存    │
                    └─────────────────┘
                                 │
                    ┌─────────────────┐
                    │   数据存储层     │
                    │                 │
                    │  • MySQL 8.0+   │
                    │  • Redis 7.0+   │
                    │  • 文件存储      │
                    └─────────────────┘
```

## 🎯 功能模块

### 🖥️ Web管理端功能

#### 🔐 权限管理
- **用户管理**：用户增删改查、密码重置、状态管理、部门分配
- **角色管理**：角色权限分配、数据权限控制、角色继承
- **菜单管理**：动态菜单、权限控制、图标管理、路由配置
- **部门管理**：树形结构、层级管理、负责人设置
- **岗位管理**：岗位信息维护、权限关联

#### 🛠️ 系统管理
- **参数配置**：系统参数动态配置、环境变量管理
- **字典管理**：数据字典维护、分类管理
- **操作日志**：用户操作记录、审计追踪
- **登录日志**：登录信息记录、安全监控
- **SQL监控**：实时SQL监控、性能分析、慢SQL检测

#### 📊 监控中心
- **系统监控**：CPU、内存、磁盘使用情况
- **缓存监控**：Redis缓存状态、命中率统计
- **在线用户**：当前在线用户管理、强制下线
- **服务监控**：接口调用统计、响应时间分析

#### 📁 文件管理
- **文件上传**：支持多种文件格式上传
- **存储配置**：本地存储、云存储配置
- **文件预览**：图片、文档在线预览
- **批量操作**：文件批量上传、删除

### 📱 移动端功能 (UniApp)

#### 🏠 首页模块
- **数据看板**：企业关键数据统计展示
- **轮播展示**：企业文化、通知公告轮播
- **快捷入口**：常用功能快速访问（考勤、请假、报销等）
- **公司公告**：重要通知、政策发布
- **工作动态**：实时工作状态更新

#### 👥 组织架构
- **部门树**：树形组织架构展示，支持展开收起
- **员工信息**：员工详细信息查看、联系方式
- **搜索功能**：部门、员工快速搜索定位
- **通讯录**：一键拨打电话、发起聊天

#### 📬 消息中心
- **消息分类**：系统消息、工作消息、审批消息、通知消息
- **未读提醒**：红点提醒、数量显示
- **优先级标签**：重要、紧急、普通消息标识
- **批量操作**：全部已读、批量删除、消息搜索

#### 👤 个人中心
- **个人信息**：头像、基本信息展示和编辑
- **数据统计**：个人工作数据、绩效展示
- **功能菜单**：个人设置、消息通知、帮助中心
- **系统设置**：主题切换、语言设置、退出登录

### 🎪 任务调度 (Snail-Job)

#### 📋 任务管理
- **任务创建**：支持多种任务类型创建
- **任务调度**：Cron表达式、固定频率调度
- **任务监控**：执行状态、成功率统计
- **任务重试**：失败自动重试、重试策略配置

#### 📊 监控告警
- **执行监控**：实时任务执行状态监控
- **性能分析**：任务执行时间、资源消耗分析
- **告警通知**：任务失败、超时告警
- **日志查看**：详细执行日志、错误追踪

## 🛠️ 技术栈

### 后端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.0.5 | 应用框架 |
| Java | 17 | 开发语言 |
| MyBatis-Flex | 1.10.9 | ORM框架 |
| Sa-Token | 1.38.0 | 权限认证 |
| MySQL | 8.x | 数据库 |
| Redis | 7.x | 缓存数据库 |
| Knife4j | 4.4.0 | API文档 |
| FastJSON2 | 2.0.43 | JSON处理 |
| HikariCP | - | 数据库连接池 |
| Druid | 1.2.23 | 数据源监控 |
| Hutool | 5.8.20 | Java工具库 |
| Snail-Job | 1.5.0 | 分布式任务调度 |

### 前端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.5.13 | 前端框架 |
| TypeScript | 5.7.3 | 开发语言 |
| Vite | 6.1.0 | 构建工具 |
| Ant Design Vue | 4.2.6 | UI组件库 |
| Pinia | 3.0.0 | 状态管理 |
| Vue Router | 4.5.0 | 路由管理 |
| UnoCSS | 65.4.3 | 原子化CSS |
| ECharts | 5.6.0 | 图表库 |

### 移动端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| UniApp | 2.0 | 跨平台框架 |
| Vue.js | 2.x | 前端框架 |
| CSS3 | - | 样式语言 |
| JavaScript | ES6+ | 开发语言 |
| 毛玻璃设计 | - | 现代化UI风格 |

## 📦 项目结构

```
mars-admin/
├── mars-admin/                    # 后端API服务
│   ├── src/main/java/com/mars/admin/
│   │   ├── controller/            # 控制器层
│   │   │   ├── AuthController.java          # 认证控制器
│   │   │   ├── SysUserController.java       # 用户管理
│   │   │   ├── SysRoleController.java       # 角色管理
│   │   │   ├── SysMenuController.java       # 菜单管理
│   │   │   ├── SysDeptController.java       # 部门管理
│   │   │   ├── SysConfigController.java     # 参数配置
│   │   │   ├── SysDictController.java       # 字典管理
│   │   │   ├── SqlLogController.java        # SQL监控
│   │   │   ├── FileUploadController.java    # 文件上传
│   │   │   └── SysMonitorController.java    # 系统监控
│   │   ├── service/               # 服务层
│   │   ├── mapper/                # 数据访问层
│   │   ├── entity/                # 实体类
│   │   ├── framework/             # 框架配置
│   │   │   ├── config/            # 配置类
│   │   │   ├── filter/            # 过滤器
│   │   │   ├── listener/          # 监听器
│   │   │   └── util/              # 工具类
│   │   └── common/                # 公共模块
│   ├── src/main/resources/
│   │   ├── mapper/                # MyBatis映射文件
│   │   ├── application.yml        # 主配置文件
│   │   └── application-dev.yml    # 开发环境配置
│   └── doc/mars_admin.sql         # 数据库脚本
├── mars-admin-ui/                 # Web管理端
│   ├── src/
│   │   ├── views/                 # 页面组件
│   │   │   ├── manage/            # 管理页面
│   │   │   │   ├── user/          # 用户管理
│   │   │   │   ├── role/          # 角色管理
│   │   │   │   ├── menu/          # 菜单管理
│   │   │   │   ├── dept/          # 部门管理
│   │   │   │   ├── dict/          # 字典管理
│   │   │   │   └── config/        # 参数配置
│   │   │   ├── home/              # 首页
│   │   │   └── _builtin/          # 内置页面
│   │   ├── components/            # 公共组件
│   │   ├── layouts/               # 布局组件
│   │   ├── router/                # 路由配置
│   │   ├── store/                 # 状态管理
│   │   ├── service/               # API服务
│   │   └── utils/                 # 工具函数
│   └── packages/                  # 自定义包
├── mars-admin-uniapp/             # 移动端小程序
│   ├── pages/                     # 页面文件
│   │   ├── index/                 # 首页
│   │   ├── organization/          # 组织架构
│   │   ├── message/               # 消息中心
│   │   └── profile/               # 个人中心
│   ├── static/                    # 静态资源
│   │   ├── css/common.css         # 全局样式
│   │   └── images/                # 图片资源
│   ├── utils/                     # 工具函数
│   │   ├── request.js             # 网络请求
│   │   └── common.js              # 通用工具
│   ├── App.vue                    # 应用入口
│   ├── pages.json                 # 页面配置
│   └── manifest.json              # 应用配置
├── mars-admin-web/                # 静态Web页面
│   ├── index.html                 # 企业官网
│   ├── nginx.conf                 # Nginx配置
│   └── image/                     # 静态图片
├── snail-job/                     # 分布式任务调度
│   ├── snail-job-server/          # 调度服务端
│   ├── snail-job-client/          # 客户端SDK
│   ├── snail-job-admin/           # 管理界面
│   └── doc/                       # 相关文档
├── docs/                          # 项目文档
│   ├── guide/                     # 使用指南
│   ├── features/                  # 功能介绍
│   ├── advanced/                  # 高级特性
│   └── backend/                   # 后端文档
└── upload/                        # 文件上传目录
```

## 🚀 快速开始

### 环境要求

- **Java**: 17+
- **Node.js**: 18.12.0+
- **MySQL**: 8.0+
- **Redis**: 7.0+
- **Maven**: 3.6+
- **pnpm**: 8.7.0+
- **HBuilderX**: 3.0+ (移动端开发)

### 🔧 后端启动

1. **克隆项目**
```bash
git clone https://github.com/your-username/mars-admin.git
cd mars-admin
```

2. **数据库配置**
```bash
# 创建数据库
CREATE DATABASE mars_admin CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据
mysql -u root -p mars_admin < mars-admin/doc/mars_admin.sql
```

3. **修改配置**
```yaml
# mars-admin/src/main/resources/application-dev.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mars_admin?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&allowPublicKeyRetrieval=true
    username: root
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password
```

4. **启动后端**
```bash
cd mars-admin
mvn clean install
mvn spring-boot:run
```

5. **访问API文档**
```
http://localhost:8080/doc.html
```

### 🖥️ Web管理端启动

1. **安装依赖**
```bash
cd mars-admin-ui
pnpm install
```

2. **启动开发服务器**
```bash
pnpm dev
```

3. **访问管理端**
```
http://localhost:3000
```

### 📱 移动端启动

1. **使用HBuilderX打开项目**
   - 打开HBuilderX
   - 文件 -> 打开目录 -> 选择 `mars-admin-uniapp` 文件夹

2. **配置后端接口**
```javascript
// mars-admin-uniapp/utils/request.js
const config = {
  baseURL: 'http://localhost:8080', // 修改为你的后端API地址
  timeout: 10000
}
```

3. **运行项目**
   - 点击HBuilderX顶部菜单 "运行" -> "运行到小程序模拟器" -> "微信开发者工具"

### 🎪 任务调度启动

1. **启动Snail-Job服务**
```bash
cd snail-job
mvn clean install
java -jar snail-job-server/target/snail-job-server.jar
```

2. **访问调度管理界面**
```
http://localhost:8081
```

### 默认账号

```
用户名: admin
密码: 123456
```

## 🌐 在线体验

### 生产环境地址

- **🖥️ Web管理端**: [https://web.marsadmin.cn/](https://web.marsadmin.cn/)
- **📱 移动端演示**: [https://web.marsadmin.cn/](https://web.marsadmin.cn/)
- **📖 API接口文档**: [https://web.marsadmin.cn/doc.html](https://web.marsadmin.cn/doc.html)
- **🎪 任务调度平台**: [https://web.marsadmin.cn/snail-job](https://web.marsadmin.cn/snail-job)

### 体验账号

```
管理员账号: admin
登录密码: 123456
```

### 功能预览

| 功能模块 | 预览地址 | 说明 |
|---------|---------|------|
| 用户管理 | [用户列表](https://web.marsadmin.cn/#/manage/user) | 用户增删改查、权限分配 |
| 角色管理 | [角色列表](https://web.marsadmin.cn/#/manage/role) | 角色权限管理 |
| 菜单管理 | [菜单列表](https://web.marsadmin.cn/#/manage/menu) | 动态菜单配置 |
| 部门管理 | [部门列表](https://web.marsadmin.cn/#/manage/dept) | 组织架构管理 |
| SQL监控 | [SQL日志](https://web.marsadmin.cn/#/manage/sql-monitor) | 实时SQL监控 |
| 系统监控 | [系统状态](https://web.marsadmin.cn/#/manage/system-monitor) | 服务器状态监控 |
│   │   │   ├── AuthController.java          # 认证控制器
│   │   │   ├── SysUserController.java       # 用户管理
│   │   │   ├── SysRoleController.java       # 角色管理
│   │   │   ├── SysMenuController.java       # 菜单管理
│   │   │   ├── SysDeptController.java       # 部门管理
│   │   │   ├── SysConfigController.java     # 参数配置
│   │   │   ├── SysDictController.java       # 字典管理
│   │   │   ├── SqlLogController.java        # SQL监控
│   │   │   ├── FileUploadController.java    # 文件上传
│   │   │   └── SysMonitorController.java    # 系统监控
│   │   ├── service/               # 服务层
│   │   ├── mapper/                # 数据访问层
│   │   ├── entity/                # 实体类
│   │   ├── framework/             # 框架配置
│   │   │   ├── config/            # 配置类
│   │   │   ├── filter/            # 过滤器
│   │   │   ├── listener/          # 监听器
│   │   │   └── util/              # 工具类
│   │   └── common/                # 公共模块
│   ├── src/main/resources/
│   │   ├── mapper/                # MyBatis映射文件
│   │   ├── application.yml        # 主配置文件
│   │   └── application-dev.yml    # 开发环境配置
│   └── doc/mars_admin.sql         # 数据库脚本
├── mars-admin-ui/                 # Web管理端
│   ├── src/
│   │   ├── views/                 # 页面组件
│   │   │   ├── manage/            # 管理页面
│   │   │   │   ├── user/          # 用户管理
│   │   │   │   ├── role/          # 角色管理
│   │   │   │   ├── menu/          # 菜单管理
│   │   │   │   ├── dept/          # 部门管理
│   │   │   │   ├── dict/          # 字典管理
│   │   │   │   └── config/        # 参数配置
│   │   │   ├── home/              # 首页
│   │   │   └── _builtin/          # 内置页面
│   │   ├── components/            # 公共组件
│   │   ├── layouts/               # 布局组件
│   │   ├── router/                # 路由配置
│   │   ├── store/                 # 状态管理
│   │   ├── service/               # API服务
│   │   └── utils/                 # 工具函数
│   └── packages/                  # 自定义包
├── mars-admin-uniapp/             # 移动端小程序
│   ├── pages/                     # 页面文件
│   │   ├── index/                 # 首页
│   │   ├── organization/          # 组织架构
│   │   ├── message/               # 消息中心
│   │   └── profile/               # 个人中心
│   ├── static/                    # 静态资源
│   │   ├── css/common.css         # 全局样式
│   │   └── images/                # 图片资源
│   ├── utils/                     # 工具函数
│   │   ├── request.js             # 网络请求
│   │   └── common.js              # 通用工具
│   ├── App.vue                    # 应用入口
│   ├── pages.json                 # 页面配置
│   └── manifest.json              # 应用配置
├── mars-admin-web/                # 静态Web页面
│   ├── index.html                 # 企业官网
│   ├── nginx.conf                 # Nginx配置
│   └── image/                     # 静态图片
├── snail-job/                     # 分布式任务调度
│   ├── snail-job-server/          # 调度服务端
│   ├── snail-job-client/          # 客户端SDK
│   ├── snail-job-admin/           # 管理界面
│   └── doc/                       # 相关文档
├── docs/                          # 项目文档
│   ├── guide/                     # 使用指南
│   ├── features/                  # 功能介绍
│   ├── advanced/                  # 高级特性
│   └── backend/                   # 后端文档
└── upload/                        # 文件上传目录
```

## 🚀 快速开始

### 环境要求

- **Java**: 17+
- **Node.js**: 18.12.0+
- **MySQL**: 8.0+
- **Redis**: 7.0+
- **Maven**: 3.6+
- **pnpm**: 8.7.0+
- **HBuilderX**: 3.0+ (移动端开发)

### 🔧 后端启动

1. **克隆项目**
```bash
git clone https://github.com/your-username/mars-admin.git
cd mars-admin
```

2. **数据库配置**
```bash
# 创建数据库
CREATE DATABASE mars_admin CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据
mysql -u root -p mars_admin < mars-admin/doc/mars_admin.sql
```

3. **修改配置**
```yaml
# mars-admin/src/main/resources/application-dev.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mars_admin?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&allowPublicKeyRetrieval=true
    username: root
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password
```

4. **启动后端**
```bash
cd mars-admin
mvn clean install
mvn spring-boot:run
```

5. **访问API文档**
```
http://localhost:8080/doc.html
```

### 🖥️ Web管理端启动

1. **安装依赖**
```bash
cd mars-admin-ui
pnpm install
```

2. **启动开发服务器**
```bash
pnpm dev
```

3. **访问管理端**
```
http://localhost:3000
```

### 📱 移动端启动

1. **使用HBuilderX打开项目**
   - 打开HBuilderX
   - 文件 -> 打开目录 -> 选择 `mars-admin-uniapp` 文件夹

2. **配置后端接口**
```javascript
// mars-admin-uniapp/utils/request.js
const config = {
  baseURL: 'http://localhost:8080', // 修改为你的后端API地址
  timeout: 10000
}
```

3. **运行项目**
   - 点击HBuilderX顶部菜单 "运行" -> "运行到小程序模拟器" -> "微信开发者工具"

### 🎪 任务调度启动

1. **启动Snail-Job服务**
```bash
cd snail-job
mvn clean install
java -jar snail-job-server/target/snail-job-server.jar
```

2. **访问调度管理界面**
```
http://localhost:8081
```

### 默认账号

```
用户名: admin
密码: 123456
```

## 🌐 在线体验

### 生产环境地址

- **🖥️ Web管理端**: [https://web.marsadmin.cn/](https://web.marsadmin.cn/)
- **📱 移动端演示**: [https://web.marsadmin.cn/](https://web.marsadmin.cn/)
- **📖 API接口文档**: [https://web.marsadmin.cn/doc.html](https://web.marsadmin.cn/doc.html)
- **🎪 任务调度平台**: [https://web.marsadmin.cn/snail-job](https://web.marsadmin.cn/snail-job)

### 体验账号

```
管理员账号: admin
登录密码: 123456
```

### 功能预览

| 功能模块 | 预览地址 | 说明 |
|---------|---------|------|
| 用户管理 | [用户列表](https://web.marsadmin.cn/#/manage/user) | 用户增删改查、权限分配 |
| 角色管理 | [角色列表](https://web.marsadmin.cn/#/manage/role) | 角色权限管理 |
| 菜单管理 | [菜单列表](https://web.marsadmin.cn/#/manage/menu) | 动态菜单配置 |
| 部门管理 | [部门列表](https://web.marsadmin.cn/#/manage/dept) | 组织架构管理 |
| SQL监控 | [SQL日志](https://web.marsadmin.cn/#/manage/sql-monitor) | 实时SQL监控 |
| 系统监控 | [系统状态](https://web.marsadmin.cn/#/manage/system-monitor) | 服务器状态监控 |

## 🎨 界面展示

### Web管理端界面

| 登录页面 | 首页仪表板 |
|---------|-----------|
| ![登录页面](docs/images/login.png) | ![首页](docs/images/dashboard.png) |

| 用户管理 | 角色管理 |
|---------|----------|
| ![用户管理](docs/images/user.png) | ![角色管理](docs/images/role.png) |

| SQL监控 | 系统监控 |
|---------|----------|
| ![SQL监控](docs/images/sql-monitor.png) | ![系统监控](docs/images/system-monitor.png) |

### 移动端界面

| 首页 | 组织架构 | 消息中心 | 个人中心 |
|------|---------|----------|----------|
| ![首页](docs/images/mobile-home.png) | ![组织](docs/images/mobile-org.png) | ![消息](docs/images/mobile-message.png) | ![个人](docs/images/mobile-profile.png) |

## 🔧 开发指南

### 后端开发

#### 1. 新增模块
```java
// 1. 创建实体类
@Data
@Table(value = "your_table", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
public class YourEntity extends BaseEntity {
    @Id(keyType = KeyType.Auto)
    private Long id;
    // 其他字段...
}

// 2. 创建Mapper接口
public interface YourMapper extends BaseMapper<YourEntity> {
    // 自定义方法...
}

// 3. 创建Service
@Service
public class YourService {
    @Autowired
    private YourMapper yourMapper;
    // 业务方法...
}

// 4. 创建Controller
@RestController
@RequestMapping("/your-module")
public class YourController {
    @Autowired
    private YourService yourService;
    // 接口方法...
}
```

#### 2. 权限控制
```java
// 方法级权限控制
@SaCheckPermission("system:user:list")
@GetMapping("/list")
public Result<List<User>> list() {
    // 方法实现...
}

// 角色权限控制
@SaCheckRole("admin")
@PostMapping("/save")
public Result<Void> save(@RequestBody User user) {
    // 方法实现...
}
```

#### 3. SQL监控使用
系统内置了彩色SQL日志监控，自动记录SQL执行情况：
- ✅ 正常执行（<500ms）- 绿色显示
- ⚠️ 性能提醒（500-1000ms）- 黄色显示
- 🐌 慢SQL警告（>1000ms）- 红色显示

访问 `/sql-log/recent` 查看最近SQL执行记录。

### 前端开发

#### 1. 新增页面
```vue
<!-- src/views/your-module/index.vue -->
<template>
  <div class="your-module">
    <a-card title="模块标题">
      <!-- 页面内容 -->
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { fetchYourData } from '@/service/api/your-module';

// 页面逻辑
const dataList = ref([]);

onMounted(() => {
  loadData();
});

const loadData = async () => {
  const { data } = await fetchYourData();
  dataList.value = data;
};
</script>
```

#### 2. API调用
```typescript
// src/service/api/your-module.ts
import { request } from '@sa/axios';

export function fetchYourData() {
  return request.get<YourData[]>('/your-module/list');
}

export function saveYourData(data: YourData) {
  return request.post('/your-module/save', data);
}
```

### 移动端开发

#### 1. 新增页面
```vue
<!-- pages/your-page/your-page.vue -->
<template>
  <view class="container">
    <view class="header">
      <text class="title">页面标题</text>
    </view>
    <view class="content">
      <!-- 页面内容 -->
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      // 页面数据
    };
  },
  onLoad() {
    // 页面加载
  },
  methods: {
    // 页面方法
  }
};
</script>

<style>
.container {
  padding: 20rpx;
}
</style>
```

#### 2. 页面配置
```json
// pages.json
{
  "pages": [
    {
      "path": "pages/your-page/your-page",
      "style": {
        "navigationBarTitleText": "页面标题"
      }
    }
  ]
}
```

## 📊 系统监控

### SQL监控面板
- **实时监控**: 访问 `/sql-log/recent` 查看SQL执行日志
- **性能分析**: 自动标识慢SQL，提供优化建议
- **统计报表**: SQL执行次数、平均耗时统计

### 系统监控
- **服务器状态**: CPU、内存、磁盘使用情况
- **JVM监控**: 堆内存、非堆内存、GC情况
- **数据库连接**: 连接池状态、活跃连接数

### 缓存监控
- **Redis状态**: 连接状态、内存使用、命中率
- **缓存统计**: 键值数量、过期策略、持久化状态

## 🔒 安全特性

### 认证机制
- **Sa-Token**: 轻量级权限认证框架
- **Redis存储**: Token存储，支持分布式
- **密码加密**: BCrypt加密存储
- **登录限制**: 支持单点登录控制
- **会话管理**: 自动续期、强制下线

### 权限控制
- **RBAC模型**: 用户-角色-权限三层模型
- **数据权限**: 支持部门数据权限控制
- **接口权限**: 注解式权限控制
- **菜单权限**: 动态菜单权限
- **按钮权限**: 细粒度按钮权限控制

### 安全防护
- **XSS防护**: 输入输出过滤
- **CSRF防护**: Token验证
- **SQL注入**: 参数化查询
- **接口限流**: 防止恶意请求
- **操作审计**: 完整的操作日志记录

## 📈 性能优化

### 后端优化
- **连接池**: HikariCP高性能连接池
- **缓存策略**: Redis缓存热点数据
- **SQL优化**: MyBatis-Flex高性能ORM
- **异步处理**: 异步日志记录
- **数据库索引**: 合理的索引设计
- **分页查询**: 高效的分页实现

### 前端优化
- **懒加载**: 路由和组件懒加载
- **Tree Shaking**: Vite自动优化
- **CDN加速**: 第三方库CDN加速
- **缓存策略**: 浏览器缓存优化
- **代码分割**: 按需加载
- **图片优化**: WebP格式、懒加载

### 移动端优化
- **图片懒加载**: 优化图片加载性能
- **请求缓存**: 减少重复网络请求
- **代码分割**: 按需加载页面组件
- **资源压缩**: 压缩图片和代码
- **本地缓存**: 合理使用本地存储

## 🐛 常见问题

### 启动相关

**Q: 后端启动失败怎么办？**
A: 
1. 检查Java版本是否为17+
2. 检查MySQL和Redis是否正常运行
3. 检查配置文件中的数据库连接信息
4. 查看启动日志，定位具体错误

**Q: 前端启动失败怎么办？**
A:
1. 检查Node.js版本是否为18.12.0+
2. 清除缓存：`rm -rf node_modules pnpm-lock.yaml && pnpm install`
3. 检查网络连接，确保能正常下载依赖
4. 查看控制台错误信息

### 权限相关

**Q: 登录后看不到菜单怎么办？**
A:
1. 检查用户是否分配了正确的角色
2. 检查角色是否分配了相应的菜单权限
3. 检查菜单状态是否为"显示"
4. 清除浏览器缓存，重新登录

**Q: 接口返回403权限不足？**
A:
1. 检查Token是否过期，重新登录
2. 检查用户角色是否有接口访问权限
3. 检查Sa-Token配置是否正确
4. 查看后端日志，确认权限检查逻辑

### 移动端相关

**Q: 小程序无法连接后端接口？**
A:
1. 检查小程序是否配置了正确的服务器域名
2. 确保后端接口支持HTTPS（生产环境）
3. 检查跨域配置是否正确
4. 确认网络请求配置中的baseURL

**Q: 小程序页面显示异常？**
A:
1. 检查页面路径配置是否正确
2. 确认页面在pages.json中已注册
3. 检查样式兼容性问题
4. 查看开发者工具控制台错误信息

### 部署相关

**Q: 生产环境部署注意事项？**
A:
1. 修改数据库连接为生产环境配置
2. 配置Redis连接信息
3. 设置正确的文件上传路径
4. 配置Nginx反向代理
5. 开启HTTPS证书
6. 设置防火墙规则

## 🤝 参与贡献

我们欢迎所有形式的贡献，无论是新功能、bug修复还是文档改进。

### 贡献流程

1. **Fork项目**: 点击右上角Fork按钮
2. **创建分支**: `git checkout -b feature/AmazingFeature`
3. **提交更改**: `git commit -m 'Add some AmazingFeature'`
4. **推送分支**: `git push origin feature/AmazingFeature`
5. **创建PR**: 提交Pull Request

### 开发规范

- **代码规范**: 遵循阿里巴巴Java开发规范
- **提交规范**: 使用约定式提交格式
- **代码格式**: 使用ESLint和Prettier格式化
- **单元测试**: 为新功能编写测试用例
- **文档更新**: 更新相关API文档

### 贡献指南

- **Issue反馈**: 详细描述问题，提供复现步骤
- **功能建议**: 说明功能需求和使用场景
- **代码贡献**: 确保代码质量，添加必要注释
- **文档完善**: 帮助完善项目文档

## 📄 许可证

本项目基于 [Apache License 2.0](LICENSE) 开源协议，免费用于个人和商业用途。

### 使用须知

1. ✅ **商业使用**: 允许商业项目使用
2. ✅ **修改代码**: 允许修改源代码
3. ✅ **分发**: 允许分发和再分发
4. ✅ **私人使用**: 允许私人项目使用
5. ⚠️ **署名要求**: 需保留版权声明
6. ⚠️ **责任限制**: 使用风险自担

## 🙏 致谢

感谢以下优秀的开源项目：

### 后端框架
- [Spring Boot](https://spring.io/projects/spring-boot) - 强大的Java应用框架
- [Sa-Token](https://sa-token.cc/) - 轻量级权限认证框架
- [MyBatis-Flex](https://mybatis-flex.com/) - 高性能ORM框架
- [Hutool](https://hutool.cn/) - Java工具类库

### 前端框架
- [Vue.js](https://vuejs.org/) - 渐进式JavaScript框架
- [Ant Design Vue](https://antdv.com/) - 企业级UI组件库
- [Soybean Admin](https://github.com/soybeanjs/soybean-admin-antd) - 优秀的后台管理模板
- [Vite](https://vitejs.dev/) - 下一代前端构建工具

### 移动端框架
- [UniApp](https://uniapp.dcloud.io/) - 跨平台应用开发框架
- [HBuilderX](https://www.dcloud.io/hbuilderx.html) - 专业的前端开发工具

### 任务调度
- [Snail-Job](https://snailjob.opensnail.com/) - 分布式任务重试和调度平台

## 📞 联系我们

### 开发团队
- **项目负责人**: Mars.wq
- **邮箱**: wqexpore@163.com
- **官网**: [https://www.marsadmin.cn](https://www.marsadmin.cn)

### 技术交流
- **QQ群**: [待建立]
- **微信群**: [待建立]
- **技术博客**: [待建立]

### 商务合作
- **定制开发**: 支持功能定制和二次开发
- **技术支持**: 提供技术咨询和培训服务
- **部署服务**: 提供生产环境部署和运维服务

### 问题反馈
- **GitHub Issues**: [提交Bug和功能建议](https://github.com/your-username/mars-admin/issues)
- **Gitee Issues**: [国内用户反馈](https://gitee.com/your-username/mars-admin/issues)

---

<div align="center">

**⭐ 如果这个项目对你有帮助，请给我们一个Star！ ⭐**

**🚀 让我们一起构建更好的企业管理系统！ 🚀**

Made with ❤️ by Mars Team

[🏠 首页](https://www.marsadmin.cn) | [📖 文档](./docs/) | [🐛 反馈](https://github.com/your-username/mars-admin/issues) | [💬 讨论](https://github.com/your-username/mars-admin/discussions)

</div>