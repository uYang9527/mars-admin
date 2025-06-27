# 🚀 Mars-Admin 管理系统

<div align="center">

![Mars-Admin](https://img.shields.io/badge/Mars--Admin-v1.0.0-brightgreen.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0.5-brightgreen.svg)
![Vue](https://img.shields.io/badge/Vue-3.5.13-brightgreen.svg)
![Java](https://img.shields.io/badge/Java-17-orange.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

**🔥 开箱即用的企业级后台管理系统 🔥**

一套基于 Spring Boot 3 + Vue 3 + TypeScript 的现代化管理系统，采用前后端分离架构，提供完整的权限管理、用户管理、菜单管理等功能，是中小企业快速开发的理想脚手架。

[在线预览](http://localhost:8080) | [API文档](http://localhost:8080/doc.html) | [更新日志](./CHANGELOG.md)

</div>

## ✨ 项目特色

- 🎯 **开箱即用**：完整的RBAC权限模型，开箱即用的管理功能
- 🚀 **技术前沿**：基于Spring Boot 3 + Vue 3 + TypeScript最新技术栈
- 🎨 **美观界面**：基于Ant Design Vue 4，界面美观，交互友好
- 🔐 **安全可靠**：Sa-Token权限认证，Redis会话管理，安全可靠
- 📊 **SQL监控**：彩色SQL日志，性能监控，慢SQL检测
- 🛠️ **开发友好**：代码规范，注释完整，易于二次开发
- 📱 **响应式设计**：支持PC、平板、手机等多端适配

## 🏗️ 技术架构

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

## 📋 功能特性

### 🔐 权限管理
- **用户管理**：用户增删改查、密码重置、状态管理
- **角色管理**：角色权限分配、数据权限控制
- **菜单管理**：动态菜单、权限控制、图标管理
- **部门管理**：树形结构、层级管理
- **岗位管理**：岗位信息维护

### 🛠️ 系统管理
- **参数配置**：系统参数动态配置
- **字典管理**：数据字典维护
- **操作日志**：用户操作记录
- **登录日志**：登录信息记录
- **SQL监控**：实时SQL监控、性能分析

### 🎨 界面特性
- **动态路由**：根据权限动态生成路由
- **多主题**：内置多套主题，支持暗黑模式
- **国际化**：中英文切换支持
- **响应式**：完美适配移动端
- **标签页**：多标签页面管理

### 🔍 开发特性
- **代码生成**：一键生成前后端代码
- **API文档**：自动生成接口文档
- **SQL日志**：彩色SQL日志，支持复制
- **异常处理**：全局异常处理机制
- **参数校验**：完善的参数校验体系

## 📦 项目结构

```
mars-admin/
├── mars-admin/                    # 后端项目
│   ├── src/main/java/com/mars/admin/
│   │   ├── controller/            # 控制器层
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
│   └── sql/mars_admin.sql         # 数据库脚本
├── mars-admin-ui/                 # 前端项目
│   ├── src/
│   │   ├── views/                 # 页面组件
│   │   │   ├── manage/            # 管理页面
│   │   │   ├── home/              # 首页
│   │   │   └── _builtin/          # 内置页面
│   │   ├── components/            # 公共组件
│   │   ├── layouts/               # 布局组件
│   │   ├── router/                # 路由配置
│   │   ├── store/                 # 状态管理
│   │   ├── service/               # API服务
│   │   └── utils/                 # 工具函数
│   ├── packages/                  # 自定义包
│   └── public/                    # 静态资源
└── README.md                      # 项目说明
```

## 🚀 快速开始

### 环境要求

- **Java**: 17+
- **Node.js**: 18.12.0+
- **MySQL**: 8.0+
- **Redis**: 7.0+
- **Maven**: 3.6+
- **pnpm**: 8.7.0+

### 后端启动

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
mysql -u root -p mars_admin < mars-admin/sql/mars_admin.sql
```

3. **修改配置**
```yaml
# mars-admin/src/main/resources/application-dev.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mars_admin?...
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

### 前端启动

1. **安装依赖**
```bash
cd mars-admin-ui
pnpm install
```

2. **启动开发服务器**
```bash
pnpm dev
```

3. **访问前端**
```
http://localhost:3000
```

### 默认账号

```
用户名: admin
密码: admin123
```

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

#### 3. SQL监控
系统内置了彩色SQL日志监控，自动记录SQL执行情况：
- ✅ 正常执行（<500ms）
- ⚠️ 性能提醒（500-1000ms）
- 🐌 慢SQL警告（>1000ms）

### 前端开发

#### 1. 新增页面
```vue
<!-- src/views/your-module/index.vue -->
<template>
  <div class="your-module">
    <!-- 页面内容 -->
  </div>
</template>

<script setup lang="ts">
// 页面逻辑
</script>
```

#### 2. 路由配置
```typescript
// 动态路由会自动从后端获取
// 如需添加静态路由，在 src/router/routes/ 中配置
```

#### 3. API调用
```typescript
// src/service/api/your-module.ts
export function fetchYourData() {
  return request.get<YourData[]>('/your-module/list');
}
```

## 📊 系统监控

### SQL监控面板
访问 `http://localhost:8080/sql-log/recent` 查看SQL执行日志

### API接口
- `GET /sql-log/recent` - 获取最近SQL日志
- `POST /sql-log/format` - 格式化SQL
- `DELETE /sql-log/clear` - 清空SQL日志

## 🔒 安全特性

### 认证机制
- **Sa-Token**: 轻量级权限认证框架
- **Redis**: Token存储，支持分布式
- **密码加密**: BCrypt加密存储
- **登录限制**: 支持单点登录控制

### 权限控制
- **RBAC模型**: 用户-角色-权限三层模型
- **数据权限**: 支持部门数据权限控制
- **接口权限**: 注解式权限控制
- **菜单权限**: 动态菜单权限

## 📈 性能优化

### 后端优化
- **连接池**: HikariCP高性能连接池
- **缓存**: Redis缓存热点数据
- **SQL优化**: MyBatis-Flex高性能ORM
- **异步处理**: 异步日志记录

### 前端优化
- **懒加载**: 路由和组件懒加载
- **Tree Shaking**: Vite自动优化
- **CDN**: 第三方库CDN加速
- **缓存策略**: 浏览器缓存优化

## 🐛 常见问题

### 1. 启动失败
- 检查Java版本是否为17+
- 检查MySQL和Redis是否正常运行
- 检查配置文件中的数据库连接信息

### 2. 权限问题
- 检查用户是否分配了正确的角色
- 检查角色是否分配了相应的菜单权限
- 检查Token是否过期

### 3. 前端访问404
- 检查后端是否正常启动
- 检查前端环境变量配置
- 检查路由配置是否正确

## 🤝 参与贡献

我们欢迎所有形式的贡献，无论是新功能、bug修复还是文档改进。

### 贡献流程

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

### 开发规范

- 遵循阿里巴巴Java开发规范
- 使用ESLint和Prettier格式化代码
- 编写单元测试
- 更新相关文档

## 📄 许可证

本项目基于 [MIT License](LICENSE) 开源协议，免费用于个人和商业用途。

## 🙏 致谢

感谢以下优秀的开源项目：

- [Spring Boot](https://spring.io/projects/spring-boot) - 应用框架
- [Vue.js](https://vuejs.org/) - 前端框架
- [Ant Design Vue](https://antdv.com/) - UI组件库
- [Sa-Token](https://sa-token.cc/) - 权限认证框架
- [MyBatis-Flex](https://mybatis-flex.com/) - ORM框架
- [Soybean Admin](https://github.com/soybeanjs/soybean-admin-antd) - 前端模板

## 📞 联系我们

- **作者**: Mars.wq
- **邮箱**: wqexpore@163.com
- **QQ群**: [待建立]
- **微信群**: [待建立]

---

<div align="center">

**⭐ 如果这个项目对你有帮助，请给我们一个Star！ ⭐**

Made with ❤️ by Mars Team

</div>