# 认证授权

认证授权是Mars Admin的核心安全功能，提供了完整的用户身份验证和权限控制机制，确保系统的安全性和数据的保护。

## 功能概述

Mars Admin的认证授权系统包含以下核心功能：

- **用户认证**：基于JWT的无状态认证机制
- **权限控制**：基于RBAC的细粒度权限管理
- **数据权限**：基于部门的数据访问控制
- **会话管理**：安全的会话生命周期管理
- **单点登录**：支持多系统统一认证
- **安全防护**：多层次的安全防护机制

## 认证机制

### 1. JWT认证流程

```mermaid
sequenceDiagram
    participant C as 客户端
    participant S as 服务端
    participant R as Redis
    
    C->>S: 1. 登录请求(用户名/密码)
    S->>S: 2. 验证用户信息
    S->>R: 3. 存储用户会话
    S->>C: 4. 返回JWT Token
    C->>S: 5. 携带Token访问API
    S->>S: 6. 验证Token
    S->>R: 7. 获取用户信息
    S->>C: 8. 返回响应数据
```

### 2. 登录认证

**接口地址：** `POST /login`

**请求参数：**
```json
{
  "username": "admin",
  "password": "admin123",
  "code": "1234",
  "uuid": "verification-uuid"
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "access_token": "eyJhbGciOiJIUzUxMiJ9...",
    "expires_in": 7200
  }
}
```

### 3. 获取用户信息

**接口地址：** `GET /getInfo`

**请求头：**
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

**响应示例：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "user": {
      "userId": 1,
      "userName": "admin",
      "nickName": "管理员",
      "email": "admin@example.com",
      "phonenumber": "15888888888",
      "sex": "1",
      "avatar": "",
      "admin": true,
      "loginDate": "2024-01-01T10:00:00",
      "dept": {
        "deptId": 103,
        "deptName": "研发部门"
      },
      "roles": [
        {
          "roleId": 1,
          "roleName": "超级管理员",
          "roleKey": "admin",
          "dataScope": "1"
        }
      ]
    },
    "roles": ["admin"],
    "permissions": [
      "*:*:*",
      "system:user:list",
      "system:user:add"
    ]
  }
}
```

### 4. 退出登录

**接口地址：** `POST /logout`

**功能描述：** 清除服务端会话信息，使Token失效。

## 权限控制

### 1. RBAC权限模型

Mars Admin采用基于角色的访问控制（RBAC）模型：

```
用户(User) ←→ 用户角色(UserRole) ←→ 角色(Role) ←→ 角色菜单(RoleMenu) ←→ 菜单(Menu)
```

#### 权限层级

- **用户（User）**：系统的使用者
- **角色（Role）**：权限的集合，如管理员、操作员
- **菜单（Menu）**：系统功能点，包含页面访问和按钮操作权限
- **部门（Dept）**：组织结构，用于数据权限控制

### 2. 权限验证注解

```java
/**
 * 权限验证注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuthorize {
    String value();
}

// 使用示例
@RestController
@RequestMapping("/system/user")
public class SysUserController {
    
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        // 查询用户列表
    }
    
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @PostMapping
    public AjaxResult add(@RequestBody SysUser user) {
        // 新增用户
    }
    
    @PreAuthorize("@ss.hasRole('admin')")
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        // 删除用户（仅管理员）
    }
}
```

### 3. 权限验证服务

```java
@Service("ss")
public class PermissionService {
    
    /**
     * 验证用户是否具备某权限
     */
    public boolean hasPermi(String permission) {
        if (StringUtils.isEmpty(permission)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        return hasPermissions(loginUser.getPermissions(), permission);
    }
    
    /**
     * 验证用户是否不具备某权限，与 hasPermi逻辑相反
     */
    public boolean lacksPermi(String permission) {
        return hasPermi(permission) != true;
    }
    
    /**
     * 验证用户是否具有以下任意一个权限
     */
    public boolean hasAnyPermi(String permissions) {
        if (StringUtils.isEmpty(permissions)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        Set<String> authorities = loginUser.getPermissions();
        for (String permission : permissions.split(PERMISSION_DELIMETER)) {
            if (permission != null && hasPermissions(authorities, permission)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 验证用户是否具有某个角色
     */
    public boolean hasRole(String role) {
        if (StringUtils.isEmpty(role)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null || CollectionUtils.isEmpty(loginUser.getUser().getRoles())) {
            return false;
        }
        for (SysRole sysRole : loginUser.getUser().getRoles()) {
            String roleKey = sysRole.getRoleKey();
            if (SUPER_ADMIN.equals(roleKey) || roleKey.equals(StringUtils.trim(role))) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 验证用户是否不具备某角色，与isRole逻辑相反
     */
    public boolean lacksRole(String role) {
        return hasRole(role) != true;
    }
    
    /**
     * 验证用户是否具有以下任意一个角色
     */
    public boolean hasAnyRole(String roles) {
        if (StringUtils.isEmpty(roles)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null || CollectionUtils.isEmpty(loginUser.getUser().getRoles())) {
            return false;
        }
        for (String role : roles.split(ROLE_DELIMETER)) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }
}
```

## 数据权限

### 1. 数据权限范围

Mars Admin支持以下数据权限范围：

| 权限范围 | 说明 | 数据过滤规则 |
|---------|------|-------------|
| 全部数据权限 | 可以查看所有数据 | 无过滤条件 |
| 自定数据权限 | 可以查看指定部门数据 | 根据角色关联的部门过滤 |
| 部门数据权限 | 可以查看本部门数据 | 根据用户所在部门过滤 |
| 部门及以下数据权限 | 可以查看本部门及子部门数据 | 根据部门层级关系过滤 |
| 仅本人数据权限 | 只能查看自己的数据 | 根据创建人或用户ID过滤 |

### 2. 数据权限注解

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {
    
    /**
     * 部门表的别名
     */
    String deptAlias() default "";
    
    /**
     * 用户表的别名
     */
    String userAlias() default "";
    
    /**
     * 权限字符
     */
    String permission() default "";
}

// 使用示例
@DataScope(deptAlias = "d", userAlias = "u")
public List<SysUser> selectUserList(SysUser user) {
    return userMapper.selectUserList(user);
}
```

### 3. 数据权限切面

```java
@Aspect
@Component
public class DataScopeAspect {
    
    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";
    
    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";
    
    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";
    
    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";
    
    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";
    
    @Before("@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScope controllerDataScope) throws Throwable {
        clearDataScope(point);
        handleDataScope(point, controllerDataScope);
    }
    
    protected void handleDataScope(final JoinPoint joinPoint, DataScope controllerDataScope) {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null) {
            SysUser currentUser = loginUser.getUser();
            // 如果是超级管理员，则不过滤数据
            if (currentUser != null && !currentUser.isAdmin()) {
                String permission = StringUtils.defaultIfEmpty(controllerDataScope.permission(), PermissionConstants.VIEW_BASE_DATA);
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.deptAlias(), 
                               controllerDataScope.userAlias(), permission);
            }
        }
    }
    
    /**
     * 数据范围过滤
     */
    public static void dataScopeFilter(JoinPoint joinPoint, SysUser user, String deptAlias, String userAlias, String permission) {
        StringBuilder sqlString = new StringBuilder();
        List<String> conditions = new ArrayList<>();
        
        for (SysRole role : user.getRoles()) {
            String dataScope = role.getDataScope();
            if (!dataScope.equals(DATA_SCOPE_CUSTOM) && conditions.contains(dataScope)) {
                continue;
            }
            if (DATA_SCOPE_ALL.equals(dataScope)) {
                sqlString = new StringBuilder();
                conditions.add(dataScope);
                break;
            } else if (DATA_SCOPE_DEPT.equals(dataScope)) {
                sqlString.append(StringUtils.format(" OR {}.dept_id = {} ", deptAlias, user.getDeptId()));
            } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
                sqlString.append(StringUtils.format(" OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )", deptAlias, user.getDeptId(), user.getDeptId()));
            } else if (DATA_SCOPE_SELF.equals(dataScope)) {
                if (StringUtils.isNotBlank(userAlias)) {
                    sqlString.append(StringUtils.format(" OR {}.user_id = {} ", userAlias, user.getUserId()));
                } else {
                    sqlString.append(StringUtils.format(" OR {}.create_by = {} ", deptAlias, user.getUserId()));
                }
            }
            conditions.add(dataScope);
        }
        
        // 多角色情况下，所有角色权限范围以并集计算
        if (StringUtils.isNotEmpty(sqlString.toString())) {
            Object params = joinPoint.getArgs()[0];
            if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) params;
                baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
            }
        }
    }
}
```

## 前端权限控制

### 1. 路由权限

```typescript
// src/router/guard/permission.ts
import { useAuthStore } from '@/store/modules/auth'
import { useRouteStore } from '@/store/modules/route'

export function createPermissionGuard(router: Router) {
  router.beforeEach(async (to, from, next) => {
    const authStore = useAuthStore()
    const routeStore = useRouteStore()
    
    // 检查是否已登录
    if (!authStore.isLogin) {
      if (to.path === '/login') {
        next()
      } else {
        next('/login')
      }
      return
    }
    
    // 检查是否已获取用户信息
    if (!authStore.userInfo) {
      try {
        await authStore.getUserInfo()
        await routeStore.generateRoutes(authStore.userInfo.permissions)
        next({ ...to, replace: true })
      } catch (error) {
        authStore.logout()
        next('/login')
      }
      return
    }
    
    // 检查页面权限
    if (to.meta.requiresAuth && !hasPagePermission(to, authStore.userInfo.permissions)) {
      next('/403')
      return
    }
    
    next()
  })
}

function hasPagePermission(route: RouteLocationNormalized, permissions: string[]): boolean {
  if (!route.meta.permission) {
    return true
  }
  
  return permissions.includes(route.meta.permission) || permissions.includes('*:*:*')
}
```

### 2. 按钮权限

```vue
<!-- 权限指令 -->
<template>
  <div>
    <a-button v-auth="'system:user:add'" type="primary">新增用户</a-button>
    <a-button v-auth="'system:user:edit'" type="default">编辑</a-button>
    <a-button v-auth="'system:user:remove'" type="danger">删除</a-button>
  </div>
</template>

<script setup lang="ts">
// 权限指令实现
import { Directive } from 'vue'
import { useAuthStore } from '@/store/modules/auth'

const authDirective: Directive = {
  mounted(el, binding) {
    const authStore = useAuthStore()
    const permission = binding.value
    
    if (!hasPermission(permission, authStore.userInfo?.permissions)) {
      el.style.display = 'none'
      // 或者移除元素
      // el.parentNode?.removeChild(el)
    }
  },
  updated(el, binding) {
    const authStore = useAuthStore()
    const permission = binding.value
    
    if (!hasPermission(permission, authStore.userInfo?.permissions)) {
      el.style.display = 'none'
    } else {
      el.style.display = ''
    }
  }
}

function hasPermission(permission: string, permissions: string[] = []): boolean {
  if (!permission) return true
  return permissions.includes(permission) || permissions.includes('*:*:*')
}

// 注册指令
app.directive('auth', authDirective)
</script>
```

### 3. 权限组件

```vue
<!-- src/components/Permission/index.vue -->
<template>
  <div v-if="hasPermission">
    <slot />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useAuthStore } from '@/store/modules/auth'

interface Props {
  permission?: string | string[]
  role?: string | string[]
  mode?: 'some' | 'every'
}

const props = withDefaults(defineProps<Props>(), {
  mode: 'some'
})

const authStore = useAuthStore()

const hasPermission = computed(() => {
  const { permission, role, mode } = props
  const userPermissions = authStore.userInfo?.permissions || []
  const userRoles = authStore.userInfo?.roles || []
  
  // 超级管理员拥有所有权限
  if (userPermissions.includes('*:*:*')) {
    return true
  }
  
  let hasPermi = true
  let hasRole = true
  
  if (permission) {
    const permissions = Array.isArray(permission) ? permission : [permission]
    hasPermi = mode === 'every' 
      ? permissions.every(p => userPermissions.includes(p))
      : permissions.some(p => userPermissions.includes(p))
  }
  
  if (role) {
    const roles = Array.isArray(role) ? role : [role]
    hasRole = mode === 'every'
      ? roles.every(r => userRoles.includes(r))
      : roles.some(r => userRoles.includes(r))
  }
  
  return hasPermi && hasRole
})
</script>

<!-- 使用示例 -->
<template>
  <div>
    <!-- 单个权限 -->
    <Permission permission="system:user:add">
      <a-button type="primary">新增用户</a-button>
    </Permission>
    
    <!-- 多个权限（任一满足） -->
    <Permission :permission="['system:user:edit', 'system:user:update']">
      <a-button>编辑用户</a-button>
    </Permission>
    
    <!-- 多个权限（全部满足） -->
    <Permission :permission="['system:user:edit', 'system:user:update']" mode="every">
      <a-button>高级编辑</a-button>
    </Permission>
    
    <!-- 角色权限 -->
    <Permission role="admin">
      <a-button type="danger">删除用户</a-button>
    </Permission>
  </div>
</template>
```

## 会话管理

### 1. Token管理

```java
@Service
public class TokenService {

    protected static final long MILLIS_SECOND = 1000;
    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire-time}")
    private int expireTime;

    @Autowired
    private RedisCache redisCache;

    /**
     * 创建令牌
     */
    public String createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        setUserAgent(loginUser);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);
        return createToken(claims);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 获取用户身份信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            try {
                Claims claims = parseToken(token);
                // 解析对应的权限以及用户信息
                String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);
                LoginUser user = redisCache.getCacheObject(userKey);
                return user;
            } catch (Exception e) {
                log.error("获取用户信息异常'{}'", e.getMessage());
            }
        }
        return null;
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }

    /**
     * 从数据声明生成令牌
     */
    private String createToken(Map<String, Object> claims) {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private String getTokenKey(String uuid) {
        return CacheConstants.LOGIN_TOKEN_KEY + uuid;
    }
}
```

### 2. 在线用户管理

```java
@RestController
@RequestMapping("/monitor/online")
public class SysUserOnlineController {

    @Autowired
    private ISysUserOnlineService userOnlineService;

    @Autowired
    private TokenService tokenService;

    @PreAuthorize("@ss.hasPermi('monitor:online:list')")
    @GetMapping("/list")
    public TableDataInfo list(String ipaddr, String userName) {
        List<SysUserOnline> userOnlineList = userOnlineService.selectUserOnlineList(ipaddr, userName);
        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));
        return getDataTable(userOnlineList);
    }

    @PreAuthorize("@ss.hasPermi('monitor:online:forceLogout')")
    @DeleteMapping("/{tokenId}")
    public AjaxResult forceLogout(@PathVariable String tokenId) {
        redisCache.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + tokenId);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('monitor:online:batchLogout')")
    @DeleteMapping("/{tokenIds}")
    public AjaxResult batchForceLogout(@PathVariable String[] tokenIds) {
        for (String tokenId : tokenIds) {
            redisCache.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + tokenId);
        }
        return success();
    }
}
```

### 3. 登录日志

```java
@Service
public class SysLogininforServiceImpl implements ISysLogininforService {

    @Autowired
    private SysLogininforMapper logininforMapper;

    /**
     * 新增系统登录日志
     */
    @Override
    public void insertLogininfor(SysLogininfor logininfor) {
        logininforMapper.insertLogininfor(logininfor);
    }

    /**
     * 查询系统登录日志集合
     */
    @Override
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor) {
        return logininforMapper.selectLogininforList(logininfor);
    }

    /**
     * 批量删除系统登录日志
     */
    @Override
    public int deleteLogininforByIds(Long[] infoIds) {
        return logininforMapper.deleteLogininforByIds(infoIds);
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor() {
        logininforMapper.cleanLogininfor();
    }
}
```

## 安全配置

### 1. Spring Security配置

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private JwtAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // CSRF禁用，因为不使用session
                .csrf(csrf -> csrf.disable())
                // 认证失败处理类
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedHandler)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                // 基于token，所以不需要session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 过滤请求
                .authorizeHttpRequests(authz -> authz
                        // 对于登录login 注册register 验证码captchaImage 允许匿名访问
                        .requestMatchers("/login", "/register", "/captchaImage").permitAll()
                        // 静态资源，可匿名访问
                        .requestMatchers(HttpMethod.GET, "/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/profile/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/*/api-docs", "/druid/**").permitAll()
                        // 除上面外的所有请求全部需要鉴权认证
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                // 添加JWT filter
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
```

### 2. 验证码配置

```java
@RestController
public class CaptchaController {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysConfigService configService;

    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public AjaxResult getCode(HttpServletResponse response) throws IOException {
        AjaxResult ajax = AjaxResult.success();
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        ajax.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled) {
            return ajax;
        }

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        String captchaType = MarsConfig.getCaptchaType();
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return AjaxResult.error(e.getMessage());
        }

        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));
        return ajax;
    }
}
```

## 最佳实践

### 1. 权限设计原则

- **最小权限原则**：用户只拥有完成工作所需的最小权限
- **职责分离**：关键操作需要多个角色协作完成
- **权限继承**：子角色可以继承父角色的权限
- **动态权限**：支持运行时动态调整权限

### 2. 安全配置建议

```yaml
# JWT配置
jwt:
  secret: ${JWT_SECRET:mars-admin-secret-key-2024}  # 使用环境变量
  expire-time: 7200  # 2小时
  refresh-time: 604800  # 7天

# 密码策略
password:
  min-length: 8
  require-uppercase: true
  require-lowercase: true
  require-numbers: true
  require-special-chars: true
  max-attempts: 5
  lockout-duration: 30  # 分钟

# 会话配置
session:
  timeout: 30  # 分钟
  max-sessions: 1  # 单用户最大会话数
  prevent-login: false  # 是否阻止新登录
```

### 3. 前端安全实践

```typescript
// Token管理
class TokenManager {
  private static readonly TOKEN_KEY = 'access_token'
  private static readonly REFRESH_TOKEN_KEY = 'refresh_token'

  static setToken(token: string) {
    localStorage.setItem(this.TOKEN_KEY, token)
  }

  static getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY)
  }

  static removeToken() {
    localStorage.removeItem(this.TOKEN_KEY)
    localStorage.removeItem(this.REFRESH_TOKEN_KEY)
  }

  static isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]))
      return payload.exp * 1000 < Date.now()
    } catch {
      return true
    }
  }
}

// 请求拦截器
axios.interceptors.request.use(
  (config) => {
    const token = TokenManager.getToken()
    if (token && !TokenManager.isTokenExpired(token)) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截器
axios.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      TokenManager.removeToken()
      router.push('/login')
    }
    return Promise.reject(error)
  }
)
```

## 常见问题

### Q: 如何实现单点登录（SSO）？

**解决方案**：
```java
@Service
public class SSOService {

    @Autowired
    private RedisCache redisCache;

    /**
     * 检查用户是否已在其他地方登录
     */
    public void checkSingleLogin(String username, String currentToken) {
        String userKey = "sso:user:" + username;
        String existingToken = redisCache.getCacheObject(userKey);

        if (StringUtils.isNotEmpty(existingToken) && !existingToken.equals(currentToken)) {
            // 踢出之前的登录
            redisCache.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + existingToken);
        }

        // 记录当前登录
        redisCache.setCacheObject(userKey, currentToken, 7200, TimeUnit.SECONDS);
    }
}
```

### Q: 如何实现权限的动态配置？

**解决方案**：
```java
@RestController
@RequestMapping("/system/permission")
public class PermissionController {

    @PostMapping("/refresh")
    @PreAuthorize("@ss.hasPermi('system:permission:refresh')")
    public AjaxResult refreshPermissions(@RequestBody List<Long> userIds) {
        for (Long userId : userIds) {
            // 清除用户权限缓存
            String userKey = CacheConstants.LOGIN_TOKEN_KEY + "*";
            Set<String> keys = redisCache.keys(userKey);

            for (String key : keys) {
                LoginUser loginUser = redisCache.getCacheObject(key);
                if (loginUser != null && userId.equals(loginUser.getUserId())) {
                    // 重新加载用户权限
                    loginUser = userDetailsService.loadUserByUsername(loginUser.getUsername());
                    redisCache.setCacheObject(key, loginUser);
                }
            }
        }
        return AjaxResult.success();
    }
}
```

### Q: 如何处理权限变更后的实时生效？

**解决方案**：
```java
@Component
public class PermissionChangeListener {

    @Autowired
    private RedisCache redisCache;

    @EventListener
    public void handlePermissionChange(PermissionChangeEvent event) {
        // 清除相关用户的权限缓存
        List<Long> userIds = event.getUserIds();
        for (Long userId : userIds) {
            clearUserPermissionCache(userId);
        }

        // 发送WebSocket通知前端刷新权限
        webSocketService.sendPermissionChangeNotification(userIds);
    }

    private void clearUserPermissionCache(Long userId) {
        Set<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        for (String key : keys) {
            LoginUser loginUser = redisCache.getCacheObject(key);
            if (loginUser != null && userId.equals(loginUser.getUserId())) {
                redisCache.deleteObject(key);
            }
        }
    }
}
```
```
