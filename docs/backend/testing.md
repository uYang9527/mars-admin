# 测试指南

本文档介绍Mars Admin的测试策略、测试框架和最佳实践，帮助开发者编写高质量的测试代码。

## 测试策略

### 测试金字塔

```
        ┌─────────────────┐
        │   E2E Tests     │  少量
        │   (端到端测试)    │
        ├─────────────────┤
        │ Integration     │  适量
        │ Tests (集成测试) │
        ├─────────────────┤
        │   Unit Tests    │  大量
        │   (单元测试)     │
        └─────────────────┘
```

### 测试分类

| 测试类型 | 测试范围 | 测试工具 | 执行频率 |
|---------|---------|---------|---------|
| 单元测试 | 单个方法/类 | JUnit 5 + Mockito | 每次提交 |
| 集成测试 | 多个组件交互 | Spring Boot Test | 每日构建 |
| 接口测试 | API接口 | MockMvc + TestContainers | 每日构建 |
| 端到端测试 | 完整业务流程 | Selenium + Cypress | 发版前 |

## 测试框架

### 1. 依赖配置

```xml
<dependencies>
    <!-- Spring Boot Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- TestContainers -->
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>mysql</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- H2 Database for testing -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- WireMock for API mocking -->
    <dependency>
        <groupId>com.github.tomakehurst</groupId>
        <artifactId>wiremock-jre8</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### 2. 测试配置

```yaml
# application-test.yml
spring:
  profiles:
    active: test

  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password:

  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true

  redis:
    host: localhost
    port: 6379
    database: 15  # 使用独立的测试数据库

logging:
  level:
    com.mars.admin: DEBUG
    org.springframework.web: DEBUG
    org.springframework.security: DEBUG
```

## 单元测试

### 1. Service层测试

```java
@ExtendWith(MockitoExtension.class)
class SysUserServiceTest {

    @Mock
    private SysUserMapper userMapper;

    @Mock
    private SysUserRoleMapper userRoleMapper;

    @Mock
    private RedisCache redisCache;

    @InjectMocks
    private SysUserServiceImpl userService;

    @Test
    @DisplayName("根据用户ID查询用户信息")
    void testSelectUserById() {
        // Given
        Long userId = 1L;
        SysUser expectedUser = new SysUser();
        expectedUser.setUserId(userId);
        expectedUser.setUserName("admin");
        expectedUser.setEmail("admin@example.com");

        when(userMapper.selectUserById(userId)).thenReturn(expectedUser);

        // When
        SysUser actualUser = userService.selectUserById(userId);

        // Then
        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getUserId()).isEqualTo(userId);
        assertThat(actualUser.getUserName()).isEqualTo("admin");

        verify(userMapper).selectUserById(userId);
    }

    @Test
    @DisplayName("新增用户成功")
    void testInsertUser() {
        // Given
        SysUser user = new SysUser();
        user.setUserName("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");

        when(userMapper.checkUserNameUnique(user.getUserName())).thenReturn(0);
        when(userMapper.checkEmailUnique(user.getEmail())).thenReturn(0);
        when(userMapper.insertUser(user)).thenReturn(1);

        // When
        boolean result = userService.insertUser(user);

        // Then
        assertThat(result).isTrue();
        verify(userMapper).insertUser(user);
        verify(userRoleMapper).batchUserRole(anyList());
    }

    @Test
    @DisplayName("用户名已存在时新增用户失败")
    void testInsertUserWithDuplicateUserName() {
        // Given
        SysUser user = new SysUser();
        user.setUserName("admin");

        when(userMapper.checkUserNameUnique(user.getUserName())).thenReturn(1);

        // When & Then
        assertThatThrownBy(() -> userService.insertUser(user))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("用户名已存在");

        verify(userMapper, never()).insertUser(any());
    }
}
```

### 2. Mapper层测试

```java
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/sql/user-test-data.sql")
class SysUserMapperTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SysUserMapper userMapper;

    @Test
    @DisplayName("根据用户名查询用户")
    void testSelectUserByUserName() {
        // When
        SysUser user = userMapper.selectUserByUserName("admin");

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getUserName()).isEqualTo("admin");
        assertThat(user.getStatus()).isEqualTo("0");
    }

    @Test
    @DisplayName("分页查询用户列表")
    void testSelectUserList() {
        // Given
        SysUser queryUser = new SysUser();
        queryUser.setStatus("0");

        // When
        List<SysUser> users = userMapper.selectUserList(queryUser);

        // Then
        assertThat(users).isNotEmpty();
        assertThat(users).allMatch(user -> "0".equals(user.getStatus()));
    }

    @Test
    @DisplayName("检查用户名唯一性")
    void testCheckUserNameUnique() {
        // When
        int count = userMapper.checkUserNameUnique("admin");

        // Then
        assertThat(count).isEqualTo(1);
    }
}
```

### 3. 工具类测试

```java
class SecurityUtilsTest {

    @Test
    @DisplayName("密码加密测试")
    void testEncryptPassword() {
        // Given
        String rawPassword = "password123";

        // When
        String encryptedPassword = SecurityUtils.encryptPassword(rawPassword);

        // Then
        assertThat(encryptedPassword).isNotNull();
        assertThat(encryptedPassword).isNotEqualTo(rawPassword);
        assertThat(encryptedPassword).startsWith("$2a$");
    }

    @Test
    @DisplayName("密码匹配测试")
    void testMatchesPassword() {
        // Given
        String rawPassword = "password123";
        String encryptedPassword = SecurityUtils.encryptPassword(rawPassword);

        // When
        boolean matches = SecurityUtils.matchesPassword(rawPassword, encryptedPassword);

        // Then
        assertThat(matches).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "short", "verylongpasswordthatexceedslimit"})
    @DisplayName("无效密码测试")
    void testInvalidPasswords(String password) {
        // When & Then
        assertThatThrownBy(() -> SecurityUtils.validatePassword(password))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
```

## 集成测试

### 1. Web层集成测试

```java
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
class SysUserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ISysUserService userService;

    @Test
    @DisplayName("获取用户列表")
    @WithMockUser(authorities = "system:user:list")
    void testGetUserList() throws Exception {
        // Given
        List<SysUser> users = Arrays.asList(
            createTestUser(1L, "admin"),
            createTestUser(2L, "user")
        );

        when(userService.selectUserList(any())).thenReturn(users);

        // When & Then
        mockMvc.perform(get("/system/user/list")
                .param("pageNum", "1")
                .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.rows").isArray())
                .andExpect(jsonPath("$.rows", hasSize(2)))
                .andExpect(jsonPath("$.rows[0].userName").value("admin"));
    }

    @Test
    @DisplayName("新增用户")
    @WithMockUser(authorities = "system:user:add")
    void testAddUser() throws Exception {
        // Given
        SysUser user = createTestUser(null, "newuser");
        when(userService.insertUser(any())).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/system/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("操作成功"));

        verify(userService).insertUser(any());
    }

    @Test
    @DisplayName("无权限访问返回403")
    @WithMockUser
    void testAccessDenied() throws Exception {
        mockMvc.perform(get("/system/user/list"))
                .andExpect(status().isForbidden());
    }

    private SysUser createTestUser(Long userId, String userName) {
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setEmail(userName + "@example.com");
        user.setStatus("0");
        return user;
    }
}
```

### 2. 数据库集成测试

```java
@SpringBootTest
@Testcontainers
@Transactional
class UserServiceIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("mars_admin_test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysUserMapper userMapper;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Test
    @DisplayName("用户完整业务流程测试")
    void testUserLifecycle() {
        // 1. 创建用户
        SysUser user = new SysUser();
        user.setUserName("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setStatus("0");

        boolean created = userService.insertUser(user);
        assertThat(created).isTrue();
        assertThat(user.getUserId()).isNotNull();

        // 2. 查询用户
        SysUser foundUser = userService.selectUserById(user.getUserId());
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserName()).isEqualTo("testuser");

        // 3. 更新用户
        foundUser.setEmail("updated@example.com");
        boolean updated = userService.updateUser(foundUser);
        assertThat(updated).isTrue();

        // 4. 验证更新
        SysUser updatedUser = userService.selectUserById(user.getUserId());
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");

        // 5. 删除用户
        boolean deleted = userService.deleteUserByIds(new Long[]{user.getUserId()});
        assertThat(deleted).isTrue();

        // 6. 验证删除
        SysUser deletedUser = userService.selectUserById(user.getUserId());
        assertThat(deletedUser).isNull();
    }
}
```

## 接口测试

### 1. REST API测试

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
class UserApiTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl;
    private String authToken;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    @Order(1)
    @DisplayName("用户登录获取Token")
    void testLogin() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin123");

        // When
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
            baseUrl + "/login",
            loginRequest,
            LoginResponse.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getToken()).isNotBlank();

        authToken = response.getBody().getToken();
    }

    @Test
    @Order(2)
    @DisplayName("获取用户信息")
    void testGetUserInfo() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // When
        ResponseEntity<UserInfoResponse> response = restTemplate.exchange(
            baseUrl + "/getInfo",
            HttpMethod.GET,
            entity,
            UserInfoResponse.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUser().getUserName()).isEqualTo("admin");
    }

    @Test
    @Order(3)
    @DisplayName("创建新用户")
    void testCreateUser() {
        // Given
        SysUser newUser = new SysUser();
        newUser.setUserName("apitest");
        newUser.setEmail("apitest@example.com");
        newUser.setPassword("password123");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SysUser> entity = new HttpEntity<>(newUser, headers);

        // When
        ResponseEntity<AjaxResult> response = restTemplate.exchange(
            baseUrl + "/system/user",
            HttpMethod.POST,
            entity,
            AjaxResult.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(200);
    }
}
```

### 2. 性能测试

```java
@SpringBootTest
class PerformanceTest {

    @Autowired
    private ISysUserService userService;

    @Test
    @DisplayName("用户查询性能测试")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testUserQueryPerformance() {
        // Given
        int iterations = 1000;

        // When
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            userService.selectUserList(new SysUser());
        }
        long endTime = System.currentTimeMillis();

        // Then
        long duration = endTime - startTime;
        double avgTime = (double) duration / iterations;

        assertThat(avgTime).isLessThan(10.0); // 平均响应时间小于10ms

        System.out.printf("执行 %d 次查询，总耗时: %d ms，平均耗时: %.2f ms%n",
                         iterations, duration, avgTime);
    }

    @Test
    @DisplayName("并发用户查询测试")
    void testConcurrentUserQuery() throws InterruptedException {
        // Given
        int threadCount = 10;
        int iterationsPerThread = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);

        // When
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < iterationsPerThread; j++) {
                        userService.selectUserList(new SysUser());
                        successCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(30, TimeUnit.SECONDS);
        long endTime = System.currentTimeMillis();
        executor.shutdown();

        // Then
        int totalOperations = threadCount * iterationsPerThread;
        assertThat(successCount.get()).isEqualTo(totalOperations);
        assertThat(errorCount.get()).isEqualTo(0);

        long duration = endTime - startTime;
        double throughput = (double) totalOperations / duration * 1000;

        System.out.printf("并发测试: %d 线程，每线程 %d 次操作，总耗时: %d ms，吞吐量: %.2f ops/s%n",
                         threadCount, iterationsPerThread, duration, throughput);
    }
}
```

## 端到端测试

### 1. Selenium测试

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
class UserManagementE2ETest {

    private WebDriver driver;
    private String baseUrl = "http://localhost:5173";

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    @DisplayName("用户登录流程")
    void testUserLogin() {
        // Given
        driver.get(baseUrl + "/login");

        // When
        WebElement usernameInput = driver.findElement(By.name("username"));
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.className("login-button"));

        usernameInput.sendKeys("admin");
        passwordInput.sendKeys("admin123");
        loginButton.click();

        // Then
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/dashboard"));

        assertThat(driver.getCurrentUrl()).contains("/dashboard");

        WebElement userMenu = driver.findElement(By.className("user-menu"));
        assertThat(userMenu.getText()).contains("admin");
    }

    @Test
    @Order(2)
    @DisplayName("用户管理页面操作")
    void testUserManagementPage() {
        // 先登录
        loginAsAdmin();

        // 导航到用户管理页面
        driver.get(baseUrl + "/system/user");

        // 验证页面加载
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement userTable = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("user-table")));

        assertThat(userTable.isDisplayed()).isTrue();

        // 测试搜索功能
        WebElement searchInput = driver.findElement(By.name("userName"));
        searchInput.sendKeys("admin");

        WebElement searchButton = driver.findElement(By.className("search-button"));
        searchButton.click();

        // 验证搜索结果
        wait.until(ExpectedConditions.textToBePresentInElement(userTable, "admin"));

        List<WebElement> userRows = driver.findElements(By.cssSelector(".user-table tbody tr"));
        assertThat(userRows).isNotEmpty();
    }

    @Test
    @Order(3)
    @DisplayName("新增用户流程")
    void testAddUser() {
        // 先登录并导航到用户管理页面
        loginAsAdmin();
        driver.get(baseUrl + "/system/user");

        // 点击新增按钮
        WebElement addButton = driver.findElement(By.className("add-user-button"));
        addButton.click();

        // 填写用户信息
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement modal = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("user-modal")));

        WebElement userNameInput = modal.findElement(By.name("userName"));
        WebElement emailInput = modal.findElement(By.name("email"));
        WebElement passwordInput = modal.findElement(By.name("password"));

        userNameInput.sendKeys("e2etest");
        emailInput.sendKeys("e2etest@example.com");
        passwordInput.sendKeys("password123");

        // 提交表单
        WebElement submitButton = modal.findElement(By.className("submit-button"));
        submitButton.click();

        // 验证成功消息
        WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("success-message")));
        assertThat(successMessage.getText()).contains("新增成功");

        // 验证用户出现在列表中
        wait.until(ExpectedConditions.textToBePresentInElement(
            driver.findElement(By.className("user-table")), "e2etest"));
    }

    private void loginAsAdmin() {
        driver.get(baseUrl + "/login");

        WebElement usernameInput = driver.findElement(By.name("username"));
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.className("login-button"));

        usernameInput.sendKeys("admin");
        passwordInput.sendKeys("admin123");
        loginButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/dashboard"));
    }
}
```

## 测试数据管理

### 1. 测试数据准备

```sql
-- /src/test/resources/sql/user-test-data.sql
INSERT INTO sys_user (user_id, user_name, nick_name, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, create_by, create_time, update_by, update_time, remark) VALUES
(1, 'admin', '管理员', 'admin@example.com', '15888888888', '1', '', '$2a$10$7JB720yubVSOfvVMe6/th.2UDdh4EkVdQ+VEuYJmur2e0CzW1/oRy', '0', '0', '127.0.0.1', sysdate(), 'admin', sysdate(), '', null, '管理员'),
(2, 'user', '普通用户', 'user@example.com', '15666666666', '1', '', '$2a$10$7JB720yubVSOfvVMe6/th.2UDdh4EkVdQ+VEuYJmur2e0CzW1/oRy', '0', '0', '127.0.0.1', sysdate(), 'admin', sysdate(), '', null, '普通用户');

INSERT INTO sys_role (role_id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES
(1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', sysdate(), '', null, '超级管理员'),
(2, '普通角色', 'common', 2, '2', 1, 1, '0', '0', 'admin', sysdate(), '', null, '普通角色');

INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1),
(2, 2);
```

### 2. 测试数据工厂

```java
@Component
public class TestDataFactory {

    public static SysUser createTestUser() {
        return createTestUser("testuser" + System.currentTimeMillis());
    }

    public static SysUser createTestUser(String userName) {
        SysUser user = new SysUser();
        user.setUserName(userName);
        user.setNickName(userName);
        user.setEmail(userName + "@example.com");
        user.setPhonenumber("1388888" + String.format("%04d", new Random().nextInt(10000)));
        user.setSex("1");
        user.setPassword("$2a$10$7JB720yubVSOfvVMe6/th.2UDdh4EkVdQ+VEuYJmur2e0CzW1/oRy");
        user.setStatus("0");
        user.setDelFlag("0");
        user.setCreateBy("test");
        user.setCreateTime(new Date());
        return user;
    }

    public static SysRole createTestRole() {
        return createTestRole("testrole" + System.currentTimeMillis());
    }

    public static SysRole createTestRole(String roleName) {
        SysRole role = new SysRole();
        role.setRoleName(roleName);
        role.setRoleKey(roleName.toLowerCase());
        role.setRoleSort(99);
        role.setDataScope("2");
        role.setMenuCheckStrictly(true);
        role.setDeptCheckStrictly(true);
        role.setStatus("0");
        role.setDelFlag("0");
        role.setCreateBy("test");
        role.setCreateTime(new Date());
        return role;
    }
}
```

## 测试覆盖率

### 1. JaCoCo配置

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.8</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
        <execution>
            <id>check</id>
            <goals>
                <goal>check</goal>
            </goals>
            <configuration>
                <rules>
                    <rule>
                        <element>PACKAGE</element>
                        <limits>
                            <limit>
                                <counter>LINE</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.80</minimum>
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### 2. 覆盖率报告

```bash
# 运行测试并生成覆盖率报告
mvn clean test jacoco:report

# 检查覆盖率是否达标
mvn jacoco:check

# 查看覆盖率报告
open target/site/jacoco/index.html
```

## 持续集成

### 1. GitHub Actions配置

```yaml
# .github/workflows/test.yml
name: Tests

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest

    services:
      mysql:
        image: mysql:8.0
        env:
          MYSQL_ROOT_PASSWORD: root
          MYSQL_DATABASE: mars_admin_test
        ports:
          - 3306:3306
        options: --health-cmd="mysqladmin ping" --health-interval=10s --health-timeout=5s --health-retries=3

      redis:
        image: redis:6.0
        ports:
          - 6379:6379
        options: --health-cmd="redis-cli ping" --health-interval=10s --health-timeout=5s --health-retries=3

    steps:
    - uses: actions/checkout@v3

    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: Cache Maven packages
      uses: actions/cache@v3
      with:
        path: ~/.m2
        key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
        restore-keys: ${{ runner.os }}-m2

    - name: Run tests
      run: mvn clean test -Dspring.profiles.active=test

    - name: Generate test report
      run: mvn jacoco:report

    - name: Upload coverage to Codecov
      uses: codecov/codecov-action@v3
      with:
        file: ./target/site/jacoco/jacoco.xml
```

## 测试最佳实践

### 1. 测试命名规范

```java
// 好的测试命名
@Test
@DisplayName("当用户名已存在时新增用户应该抛出异常")
void shouldThrowExceptionWhenUserNameAlreadyExists() {
    // 测试实现
}

// 不好的测试命名
@Test
void testUser() {
    // 测试实现
}
```

### 2. AAA模式

```java
@Test
void testUserCreation() {
    // Arrange - 准备测试数据
    SysUser user = new SysUser();
    user.setUserName("testuser");
    user.setEmail("test@example.com");

    // Act - 执行被测试的方法
    boolean result = userService.insertUser(user);

    // Assert - 验证结果
    assertThat(result).isTrue();
    assertThat(user.getUserId()).isNotNull();
}
```

### 3. 测试隔离

```java
@TestMethodOrder(OrderAnnotation.class)
class UserServiceTest {

    @BeforeEach
    void setUp() {
        // 每个测试前重置状态
        testDataCleanup();
    }

    @AfterEach
    void tearDown() {
        // 每个测试后清理数据
        testDataCleanup();
    }
}
```

### 4. 异常测试

```java
@Test
@DisplayName("空用户名应该抛出参数异常")
void shouldThrowExceptionForNullUserName() {
    // Given
    SysUser user = new SysUser();
    user.setUserName(null);

    // When & Then
    assertThatThrownBy(() -> userService.insertUser(user))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("用户名不能为空");
}
```

## 常见问题

### Q: 如何处理测试中的时间依赖？

**解决方案**：
```java
@MockBean
private Clock clock;

@Test
void testTimeBasedLogic() {
    // 固定时间
    Instant fixedInstant = Instant.parse("2024-01-01T00:00:00Z");
    when(clock.instant()).thenReturn(fixedInstant);

    // 执行测试
    // ...
}
```

### Q: 如何测试异步方法？

**解决方案**：
```java
@Test
void testAsyncMethod() throws InterruptedException {
    // 使用CountDownLatch等待异步完成
    CountDownLatch latch = new CountDownLatch(1);

    asyncService.processAsync(data, result -> {
        // 验证结果
        assertThat(result).isNotNull();
        latch.countDown();
    });

    // 等待异步完成
    assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
}
```

### Q: 如何测试事务回滚？

**解决方案**：
```java
@Test
@Transactional
@Rollback
void testTransactionRollback() {
    // 测试事务操作
    userService.insertUser(user);

    // 抛出异常触发回滚
    assertThatThrownBy(() -> {
        userService.updateUserWithError(user);
    }).isInstanceOf(ServiceException.class);

    // 验证数据已回滚
    SysUser foundUser = userService.selectUserById(user.getUserId());
    assertThat(foundUser).isNull();
}
```
```