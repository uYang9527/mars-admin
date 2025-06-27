# 环境变量配置说明

## 开发环境配置 (.env.development)

请在项目根目录创建 `.env.development` 文件，内容如下：

```bash
# 应用标题
VITE_APP_TITLE=Mars Admin

# 应用描述
VITE_APP_DESC=基于Vue3、Vite、TypeScript、Ant Design Vue的现代化管理系统

# 基础URL
VITE_BASE_URL=/

# 路由历史模式
VITE_ROUTER_HISTORY_MODE=history

# 图标前缀
VITE_ICON_PREFIX=icon

# 本地图标前缀
VITE_ICON_LOCAL_PREFIX=local-icon

# 后台服务基础URL (开发环境使用代理)
VITE_SERVICE_BASE_URL=/proxy-default

# 是否启用HTTP代理
VITE_HTTP_PROXY=Y

# 后台响应成功状态码
VITE_SERVICE_SUCCESS_CODE=200

# 需要退出登录的状态码
VITE_SERVICE_LOGOUT_CODES=401

# 需要弹窗提示退出登录的状态码  
VITE_SERVICE_MODAL_LOGOUT_CODES=403

# Token过期状态码
VITE_SERVICE_EXPIRED_TOKEN_CODES=402

# 其他后台服务基础URL (JSON格式)
VITE_OTHER_SERVICE_BASE_URL={}

# 认证路由模式 (static: 前端生成, dynamic: 后端生成)
VITE_AUTH_ROUTE_MODE=static

# 首页路由键
VITE_ROUTE_HOME=home

# 默认菜单图标
VITE_MENU_ICON=mdi:menu

# 是否构建sourcemap
VITE_SOURCE_MAP=N

# 存储前缀 (用于区分不同域名下的存储)
VITE_STORAGE_PREFIX=mars-admin

# 是否自动检测更新
VITE_AUTOMATICALLY_DETECT_UPDATE=N
```

## 生产环境配置 (.env.production)

```bash
# 应用标题
VITE_APP_TITLE=Mars Admin

# 应用描述
VITE_APP_DESC=基于Vue3、Vite、TypeScript、Ant Design Vue的现代化管理系统

# 基础URL
VITE_BASE_URL=/

# 路由历史模式
VITE_ROUTER_HISTORY_MODE=history

# 图标前缀
VITE_ICON_PREFIX=icon

# 本地图标前缀
VITE_ICON_LOCAL_PREFIX=local-icon

# 后台服务基础URL (生产环境直接使用后端地址)
VITE_SERVICE_BASE_URL=http://your-backend-domain.com

# 是否启用HTTP代理 (生产环境关闭)
VITE_HTTP_PROXY=N

# 后台响应成功状态码
VITE_SERVICE_SUCCESS_CODE=200

# 需要退出登录的状态码
VITE_SERVICE_LOGOUT_CODES=401

# 需要弹窗提示退出登录的状态码  
VITE_SERVICE_MODAL_LOGOUT_CODES=403

# Token过期状态码
VITE_SERVICE_EXPIRED_TOKEN_CODES=402

# 其他后台服务基础URL (JSON格式)
VITE_OTHER_SERVICE_BASE_URL={}

# 认证路由模式 (static: 前端生成, dynamic: 后端生成)
VITE_AUTH_ROUTE_MODE=static

# 首页路由键
VITE_ROUTE_HOME=home

# 默认菜单图标
VITE_MENU_ICON=mdi:menu

# 是否构建sourcemap
VITE_SOURCE_MAP=N

# 存储前缀 (用于区分不同域名下的存储)
VITE_STORAGE_PREFIX=mars-admin

# 是否自动检测更新
VITE_AUTOMATICALLY_DETECT_UPDATE=Y
```

## 测试环境配置 (.env.test)

```bash
# 应用标题
VITE_APP_TITLE=Mars Admin (测试环境)

# 应用描述
VITE_APP_DESC=基于Vue3、Vite、TypeScript、Ant Design Vue的现代化管理系统

# 基础URL
VITE_BASE_URL=/

# 路由历史模式
VITE_ROUTER_HISTORY_MODE=history

# 图标前缀
VITE_ICON_PREFIX=icon

# 本地图标前缀
VITE_ICON_LOCAL_PREFIX=local-icon

# 后台服务基础URL (测试环境使用代理)
VITE_SERVICE_BASE_URL=/proxy-default

# 是否启用HTTP代理
VITE_HTTP_PROXY=Y

# 后台响应成功状态码
VITE_SERVICE_SUCCESS_CODE=200

# 需要退出登录的状态码
VITE_SERVICE_LOGOUT_CODES=401

# 需要弹窗提示退出登录的状态码  
VITE_SERVICE_MODAL_LOGOUT_CODES=403

# Token过期状态码
VITE_SERVICE_EXPIRED_TOKEN_CODES=402

# 其他后台服务基础URL (JSON格式)
VITE_OTHER_SERVICE_BASE_URL={}

# 认证路由模式 (static: 前端生成, dynamic: 后端生成)
VITE_AUTH_ROUTE_MODE=static

# 首页路由键
VITE_ROUTE_HOME=home

# 默认菜单图标
VITE_MENU_ICON=mdi:menu

# 是否构建sourcemap
VITE_SOURCE_MAP=Y

# 存储前缀 (用于区分不同域名下的存储)
VITE_STORAGE_PREFIX=mars-admin

# 是否自动检测更新
VITE_AUTOMATICALLY_DETECT_UPDATE=N
```

## 代理配置说明

### 代理工作原理

1. **开发环境**: 前端运行在 `http://localhost:9527`，后端运行在 `http://localhost:8080`
2. **代理规则**: 前端请求 `/proxy-default/*` 会被代理到 `http://localhost:8080/*`
3. **示例**: 
   - 前端请求: `http://localhost:9527/proxy-default/auth/login`
   - 实际请求: `http://localhost:8080/auth/login`

### 代理配置特性

- ✅ 自动处理跨域问题
- ✅ 支持所有HTTP方法
- ✅ 保持Cookie和认证头
- ✅ 30秒超时配置
- ✅ 详细的代理日志
- ✅ 错误处理和重试

### 使用步骤

1. 在项目根目录创建对应的 `.env` 文件
2. 启动后端服务: `cd mars-admin && mvn spring-boot:run`
3. 启动前端服务: `cd soybean-admin-antd && pnpm dev`
4. 访问前端地址: `http://localhost:9527`

### 注意事项

- 确保后端服务在8080端口启动
- 开发环境必须设置 `VITE_HTTP_PROXY=Y` 启用代理
- 生产环境建议设置 `VITE_HTTP_PROXY=N` 关闭代理
- 代理仅在开发环境生效，生产环境需要配置Nginx等反向代理 