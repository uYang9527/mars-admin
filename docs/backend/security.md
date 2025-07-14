# 安全机制

Mars Admin采用多层次的安全防护机制，确保系统的安全性和数据的完整性。本文档详细介绍系统的安全架构和实现方案。

## 安全架构

### 安全层次

```
┌─────────────────────────────────────┐
│           前端安全层                 │
├─────────────────────────────────────┤
│           网关安全层                 │
├─────────────────────────────────────┤
│           应用安全层                 │
├─────────────────────────────────────┤
│           数据安全层                 │
├─────────────────────────────────────┤
│           基础设施安全层              │
└─────────────────────────────────────┘
```

### 安全组件

| 组件 | 功能 | 实现方式 |
|------|------|----------|
| 身份认证 | 用户身份验证 | JWT + Spring Security |
| 权限控制 | 访问权限管理 | RBAC模型 |
| 数据加密 | 敏感数据保护 | AES + RSA |
| 防护机制 | 攻击防护 | 多种安全策略 |
| 审计日志 | 操作追踪 | 自定义注解 |

## 身份认证

### 1. JWT认证机制

Mars Admin使用JWT（JSON Web Token）进行身份认证：

```java
@Component
public class JwtTokenProvider {
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expire-time}")
    private int jwtExpirationInMs;
    
    /**
     * 生成JWT令牌
     */
    public String generateToken(LoginUser loginUser) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationInMs * 1000L);
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", loginUser.getUserId());
        claims.put("username", loginUser.getUsername());
        claims.put("uuid", loginUser.getToken());
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(loginUser.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    
    /**
     * 验证JWT令牌
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT token validation failed: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 从JWT令牌获取用户信息
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
```

### 2. 认证过滤器

```java
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    
    @Autowired
    private TokenService tokenService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain chain) throws ServletException, IOException {
        
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser != null && SecurityUtils.getAuthentication() == null) {
            tokenService.verifyToken(loginUser);
            
            UsernamePasswordAuthenticationToken authenticationToken = 
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        
        chain.doFilter(request, response);
    }
}
```

### 3. 登录认证

```java
@Service
public class SysLoginService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private TokenService tokenService;
    
    /**
     * 用户登录
     */
    public String login(String username, String password, String code, String uuid) {
        // 验证码校验
        validateCaptcha(username, code, uuid);
        
        // 登录前置校验
        loginPreCheck(username, password);
        
        // 用户验证
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = 
                new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        
        // 生成token
        return tokenService.createToken(loginUser);
    }
    
    /**
     * 验证码校验
     */
    public void validateCaptcha(String username, String code, String uuid) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            redisCache.deleteObject(verifyKey);
            
            if (captcha == null) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                throw new CaptchaExpireException();
            }
            if (!code.equalsIgnoreCase(captcha)) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                throw new CaptchaException();
            }
        }
    }
}
```

## 权限控制

### 1. RBAC权限模型

Mars Admin采用基于角色的访问控制（RBAC）模型：

```
用户(User) ←→ 用户角色(UserRole) ←→ 角色(Role) ←→ 角色权限(RoleMenu) ←→ 权限(Menu)
```

### 2. 权限注解

```java
/**
 * 权限验证注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuthorize {
    
    /**
     * 权限表达式
     */
    String value();
}

// 使用示例
@PreAuthorize("@ss.hasPermi('system:user:list')")
@GetMapping("/list")
public TableDataInfo list(SysUser user) {
    // 查询用户列表
}

@PreAuthorize("@ss.hasRole('admin')")
@PostMapping
public AjaxResult add(@RequestBody SysUser user) {
    // 新增用户
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
     * 判断权限是否存在
     */
    private boolean hasPermissions(Set<String> permissions, String permission) {
        return permissions.contains(ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
    }
}
```

### 4. 数据权限

```java
@Component
public class DataScopeAspect {
    
    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";
    
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
            if (!dataScope.equals(DataScopeAspect.DATA_SCOPE_CUSTOM) && conditions.contains(dataScope)) {
                continue;
            }
            if (DataScopeAspect.DATA_SCOPE_ALL.equals(dataScope)) {
                sqlString = new StringBuilder();
                conditions.add(dataScope);
                break;
            } else if (DataScopeAspect.DATA_SCOPE_DEPT.equals(dataScope)) {
                sqlString.append(StringUtils.format(" OR {}.dept_id = {} ", deptAlias, user.getDeptId()));
            } else if (DataScopeAspect.DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
                sqlString.append(StringUtils.format(" OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )", deptAlias, user.getDeptId(), user.getDeptId()));
            } else if (DataScopeAspect.DATA_SCOPE_SELF.equals(dataScope)) {
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

## 数据加密

### 1. 密码加密

```java
@Component
public class SecurityUtils {
    
    /**
     * 生成BCryptPasswordEncoder密码
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
    
    /**
     * 判断密码是否相同
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
```

### 2. 敏感数据加密

```java
@Component
public class AESUtil {
    
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    
    @Value("${security.aes.key}")
    private String secretKey;
    
    /**
     * 加密
     */
    public String encrypt(String plainText) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }
    
    /**
     * 解密
     */
    public String decrypt(String cipherText) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("解密失败", e);
        }
    }
}
```

## 防护机制

### 1. XSS防护

```java
@Component
public class XssFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
    }
}

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }
    
    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
    }
    
    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        return cleanXSS(value);
    }
    
    private String cleanXSS(String value) {
        if (value != null) {
            value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
            value = value.replaceAll("'", "&#39;");
            value = value.replaceAll("eval\\((.*)\\)", "");
            value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
            value = value.replaceAll("script", "");
        }
        return value;
    }
}
```

### 2. SQL注入防护

```java
@Component
public class SqlInjectionUtils {
    
    /**
     * SQL注入过滤
     */
    public static String sqlInject(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        // 去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");
        
        // 转换成小写
        str = str.toLowerCase();
        
        // 非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alert", "drop"};
        
        // 判断是否包含非法字符
        for (String keyword : keywords) {
            if (str.contains(keyword)) {
                throw new UtilException("包含非法字符");
            }
        }
        return str;
    }
}
```

### 3. CSRF防护

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // CSRF禁用，因为不使用session
                .csrf().disable()
                // 认证失败处理类
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
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
                .headers().frameOptions().disable()
                .and()
                // 添加JWT filter
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
```

## 审计日志

### 1. 操作日志注解

```java
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    
    /**
     * 模块
     */
    String title() default "";
    
    /**
     * 功能
     */
    BusinessType businessType() default BusinessType.OTHER;
    
    /**
     * 操作人类别
     */
    OperatorType operatorType() default OperatorType.MANAGE;
    
    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;
    
    /**
     * 是否保存响应的参数
     */
    boolean isSaveResponseData() default true;
    
    /**
     * 排除指定的请求参数
     */
    String[] excludeParamNames() default {};
}
```

### 2. 日志切面

```java
@Aspect
@Component
public class LogAspect {
    
    @Autowired
    private AsyncLogService asyncLogService;
    
    /**
     * 处理完请求后执行
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }
    
    /**
     * 拦截异常操作
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }
    
    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            // 获取当前的用户
            LoginUser loginUser = SecurityUtils.getLoginUser();
            
            // *========数据库日志=========*
            SysOperLog operLog = new SysOperLog();
            operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            // 请求的地址
            String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
            operLog.setOperIp(ip);
            operLog.setOperUrl(StringUtils.substring(ServletUtils.getRequest().getRequestURI(), 0, 255));
            if (loginUser != null) {
                operLog.setOperName(loginUser.getUsername());
            }
            
            if (e != null) {
                operLog.setStatus(BusinessStatus.FAIL.ordinal());
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, operLog, jsonResult);
            // 保存数据库
            asyncLogService.saveSysLog(operLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }
}
```

## 安全配置

### 1. HTTPS配置

```yaml
server:
  port: 443
  ssl:
    enabled: true
    key-store: classpath:keystore.p12
    key-store-password: password
    key-store-type: PKCS12
    key-alias: tomcat
```

### 2. 安全头配置

```java
@Configuration
public class SecurityHeaderConfig {
    
    @Bean
    public FilterRegistrationBean<SecurityHeaderFilter> securityHeaderFilter() {
        FilterRegistrationBean<SecurityHeaderFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SecurityHeaderFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }
}

public class SecurityHeaderFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // 防止点击劫持
        httpResponse.setHeader("X-Frame-Options", "DENY");
        
        // 防止MIME类型嗅探
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        
        // XSS保护
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");
        
        // 严格传输安全
        httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        
        // 内容安全策略
        httpResponse.setHeader("Content-Security-Policy", "default-src 'self'");
        
        chain.doFilter(request, response);
    }
}
```

## 安全最佳实践

### 1. 密码策略

- 最小长度8位
- 包含大小写字母、数字、特殊字符
- 定期强制修改密码
- 防止密码重用

### 2. 会话管理

- 使用安全的会话ID
- 设置合理的会话超时时间
- 登出时清除会话信息
- 防止会话固定攻击

### 3. 输入验证

- 服务端验证所有输入
- 使用白名单验证
- 对特殊字符进行转义
- 限制输入长度和格式

### 4. 错误处理

- 不暴露敏感信息
- 统一错误响应格式
- 记录详细的错误日志
- 友好的用户提示

## 安全监控

### 1. 异常登录检测

```java
@Component
public class LoginSecurityService {
    
    /**
     * 检测异常登录
     */
    public void checkAbnormalLogin(String username, String ip) {
        // 检查登录频率
        String key = "login_attempt:" + username + ":" + ip;
        Integer attempts = redisCache.getCacheObject(key);
        if (attempts != null && attempts > 5) {
            throw new ServiceException("登录尝试过于频繁，请稍后再试");
        }
        
        // 检查异地登录
        String lastLoginIp = userService.getLastLoginIp(username);
        if (StringUtils.isNotEmpty(lastLoginIp) && !lastLoginIp.equals(ip)) {
            // 发送异地登录通知
            notificationService.sendAbnormalLoginNotification(username, ip);
        }
    }
}
```

### 2. 安全事件监控

```java
@Component
public class SecurityEventListener {
    
    @EventListener
    public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        log.info("用户 {} 登录成功", username);
        
        // 记录登录日志
        recordLoginLog(username, "登录成功", true);
    }
    
    @EventListener
    public void handleAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
        String username = event.getAuthentication().getName();
        log.warn("用户 {} 登录失败: {}", username, event.getException().getMessage());
        
        // 记录登录失败日志
        recordLoginLog(username, "登录失败: " + event.getException().getMessage(), false);
        
        // 增加失败次数
        incrementFailureCount(username);
    }
}
```

## 常见安全问题

### Q: 如何防止暴力破解？

**解决方案**：
1. 实现账户锁定机制
2. 使用验证码验证
3. 限制登录频率
4. 监控异常登录行为

### Q: 如何保护敏感API？

**解决方案**：
1. 使用HTTPS传输
2. 实现API限流
3. 添加请求签名验证
4. 记录详细的访问日志

### Q: 如何处理权限提升攻击？

**解决方案**：
1. 严格的权限验证
2. 最小权限原则
3. 定期权限审计
4. 敏感操作二次验证
