# Mars Admin 多端登录策略使用说明

## 概述

Mars Admin 采用策略工厂模式实现了多端统一登录，支持PC、小程序、APP三种登录方式。每种登录方式都有独立的验证逻辑和用户管理策略。

## 架构设计

### 核心组件

1. **LoginType枚举** - 定义登录类型
2. **LoginStrategy接口** - 登录策略抽象
3. **LoginStrategyFactory工厂** - 策略工厂管理
4. **具体策略实现**：
   - PcLoginStrategy - PC端登录
   - MiniProgramLoginStrategy - 小程序登录
   - AppLoginStrategy - APP登录

### 类图关系

```
LoginType (枚举)
    ├── PC
    ├── MINIPROGRAM
    └── APP

LoginStrategy (接口)
    ├── getLoginType()
    ├── login()
    ├── validateLoginRequest()
    └── getUserIdentifier()

LoginStrategyFactory (工厂)
    ├── getStrategy()
    ├── isSupported()
    └── getSupportedLoginTypes()

具体实现
    ├── PcLoginStrategy
    ├── MiniProgramLoginStrategy
    └── AppLoginStrategy
```

## 登录方式详解

### 1. PC端登录

**登录类型**: `pc`

**请求参数**:
```json
{
    "loginType": "pc",
    "username": "admin",
    "password": "123456",
    "rememberMe": false
}
```

**特点**:
- 基于用户名密码验证
- 支持记住我功能
- 密码MD5加密存储

### 2. 小程序登录

**登录类型**: `miniprogram`

**请求参数**:
```json
{
    "loginType": "miniprogram",
    "wxCode": "wx_code_from_wechat",
    "wxUserInfo": "{\"nickName\":\"微信用户\",\"avatarUrl\":\"http://...\",\"gender\":1}"
}
```

**特点**:
- 基于微信授权码登录
- 自动创建用户账号
- 同步微信用户信息
- 支持用户信息更新

**配置要求**:
```yaml
wechat:
  miniprogram:
    appid: your_miniprogram_appid
    secret: your_miniprogram_secret
```

### 3. APP登录

**登录类型**: `app`

**请求参数**:
```json
{
    "loginType": "app",
    "mobile": "13800138000",
    "captcha": "123456",
    "deviceId": "device_123",
    "clientInfo": "iOS 15.0"
}
```

**特点**:
- 基于手机号验证码登录
- 自动创建用户账号
- 支持设备信息记录
- 验证码Redis缓存管理

## API接口

### 统一登录接口

```http
POST /auth/login
Content-Type: application/json

{
    "loginType": "pc|miniprogram|app",
    // 其他参数根据登录类型而定
}
```

### 发送验证码接口

```http
POST /auth/sendCaptcha?mobile=13800138000
```

## 数据库变更

### 新增字段

在`sys_user`表中添加了`open_id`字段：

```sql
ALTER TABLE `sys_user` ADD COLUMN `open_id` VARCHAR(100) DEFAULT NULL COMMENT '微信OpenId' AFTER `phone`;
CREATE INDEX `idx_open_id` ON `sys_user` (`open_id`);
```

## 扩展新的登录方式

### 1. 创建登录类型枚举

```java
// 在LoginType枚举中添加
NEW_TYPE("new_type", "新登录方式");
```

### 2. 实现登录策略

```java
@Component
public class NewLoginStrategy implements LoginStrategy {
    
    @Override
    public LoginType getLoginType() {
        return LoginType.NEW_TYPE;
    }
    
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 实现具体登录逻辑
    }
    
    @Override
    public boolean validateLoginRequest(LoginRequest loginRequest) {
        // 实现参数验证逻辑
    }
    
    @Override
    public String getUserIdentifier(LoginRequest loginRequest) {
        // 实现用户标识获取逻辑
    }
}
```

### 3. 扩展请求参数

在`LoginRequest`中添加新的字段：

```java
@Schema(description = "新登录方式参数")
private String newParam;
```

## 安全考虑

### 1. 验证码安全
- 验证码5分钟过期
- 使用后立即清除
- Redis存储防止内存泄漏

### 2. 密码安全
- PC端密码MD5加密
- 建议升级为BCrypt

### 3. Token安全
- Sa-Token统一管理
- 支持多端并发登录
- 可配置踢出策略

## 监控和日志

### 1. 登录日志
所有登录尝试都会记录到`sys_login_info`表：
- 登录用户标识
- 登录状态（成功/失败）
- 登录消息
- 客户端IP

### 2. 策略工厂日志
系统启动时会打印支持的登录类型：
```
登录策略工厂初始化完成，支持的登录类型: PC端登录, 小程序登录, APP登录
```

## 常见问题

### Q: 如何切换登录方式？
A: 只需要修改请求中的`loginType`参数即可。

### Q: 小程序登录失败怎么办？
A: 检查微信小程序配置是否正确，确保appid和secret配置无误。

### Q: APP验证码收不到？
A: 当前为演示版本，验证码只打印在日志中，需要集成真实的短信服务。

### Q: 如何支持更多登录方式？
A: 按照扩展指南创建新的策略实现类即可，系统会自动识别。

## 测试示例

### PC登录测试
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "loginType": "pc",
    "username": "admin",
    "password": "123456"
  }'
```

### APP登录测试
```bash
# 1. 发送验证码
curl -X POST "http://localhost:8080/auth/sendCaptcha?mobile=13800138000"

# 2. 使用验证码登录
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "loginType": "app",
    "mobile": "13800138000",
    "captcha": "123456"
  }'
```

## 版本信息

- **版本**: v1.0.0
- **更新时间**: 2025-01-16
- **兼容性**: Spring Boot 3.x, Vue 3.x 