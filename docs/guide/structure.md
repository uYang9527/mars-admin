# 项目结构

Mars Admin采用前后端分离的架构，项目分为后端（mars-admin）和前端（mars-admin-ui）两个主要部分。

## 整体结构

```
mars-admin/
├── docs/                       # 项目文档
├── mars-admin/                 # 后端项目
│   ├── sql/                    # SQL脚本
│   ├── src/                    # 源代码
│   └── pom.xml                 # Maven配置
├── mars-admin-ui/              # 前端项目
│   ├── packages/               # 内部包
│   ├── public/                 # 静态资源
│   ├── src/                    # 源代码
│   └── package.json            # npm配置
└── README.md                   # 项目说明
```

## 后端项目结构

后端项目采用标准的Spring Boot项目结构，主要目录如下：

```
mars-admin/
├── sql/                        # SQL脚本
│   ├── mars_admin.sql          # 主数据库脚本
│   └── file_upload_ddl.sql     # 文件上传表结构
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/mars/admin/
│   │   │       ├── common/     # 通用类
│   │   │       ├── controller/ # 控制器
│   │   │       ├── entity/     # 实体类
│   │   │       ├── framework/  # 框架相关
│   │   │       ├── mapper/     # 数据访问
│   │   │       ├── service/    # 业务逻辑
│   │   │       └── MarsAdminApplication.java # 启动类
│   │   └── resources/
│   │       ├── mapper/         # MyBatis XML映射文件
│   │       ├── static/         # 静态资源
│   │       ├── application.yml # 主配置文件
│   │       └── application-dev.yml # 开发环境配置
│   └── test/                   # 测试代码
└── pom.xml                     # Maven配置文件
```

### 核心包说明

- **common**: 通用类，包含枚举、请求/响应模型等
- **controller**: REST API控制器
- **entity**: 数据库实体类
- **framework**: 框架相关配置和工具
  - **config**: 框架配置类
  - **exception**: 全局异常处理
  - **filter**: 过滤器
  - **oss**: 对象存储服务
  - **util**: 工具类
- **mapper**: MyBatis数据访问接口
- **service**: 业务逻辑实现

## 前端项目结构

前端项目采用基于Vite的Vue 3项目结构，主要目录如下：

```
mars-admin-ui/
├── packages/                   # 内部包
│   ├── axios/                  # HTTP客户端
│   ├── color/                  # 颜色工具
│   ├── hooks/                  # Vue Hooks
│   ├── materials/              # UI组件
│   └── utils/                  # 工具函数
├── public/                     # 静态资源
├── src/
│   ├── assets/                 # 资源文件
│   ├── components/             # 组件
│   ├── constants/              # 常量
│   ├── hooks/                  # 钩子函数
│   ├── layouts/                # 布局组件
│   ├── locales/                # 国际化
│   ├── router/                 # 路由配置
│   ├── service/                # API服务
│   ├── store/                  # 状态管理
│   ├── styles/                 # 样式文件
│   ├── typings/                # 类型定义
│   ├── utils/                  # 工具函数
│   ├── views/                  # 页面组件
│   ├── App.vue                 # 根组件
│   └── main.ts                 # 入口文件
├── index.html                  # HTML模板
└── package.json                # npm配置文件
```

### 核心目录说明

- **components**: 可重用组件
  - **advanced**: 高级组件
  - **common**: 通用组件
  - **custom**: 自定义组件
- **layouts**: 布局组件
  - **base-layout**: 基础布局
  - **blank-layout**: 空白布局
  - **modules**: 布局模块（头部、侧边栏、菜单等）
- **router**: 路由配置
- **service**: API服务和请求封装
- **store**: Pinia状态管理
- **views**: 页面组件
  - **_builtin**: 内置页面（登录、错误页等）
  - **manage**: 管理页面（用户、角色、菜单等）

## 配置文件

### 后端配置

主要配置文件位于`mars-admin/src/main/resources/`目录：

- **application.yml**: 主配置文件
- **application-dev.yml**: 开发环境配置

### 前端配置

主要配置文件：

- **vite.config.ts**: Vite构建配置
- **package.json**: 依赖管理
- **tsconfig.json**: TypeScript配置

## 数据库脚本

数据库初始化脚本位于`mars-admin/sql/`目录：

- **mars_admin.sql**: 主数据库脚本，包含表结构和初始数据
- **file_upload_ddl.sql**: 文件上传表结构

下一步，请参考[本地开发](development.md)章节，了解如何启动和开发项目。 