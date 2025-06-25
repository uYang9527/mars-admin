# BaseEntity 基础实体类使用说明

## 概述

`BaseEntity` 是一个抽象基类，包含了所有实体类的公共字段，并提供自动填充功能。通过继承此类，可以减少重复代码，统一管理公共字段。

## 包含的公共字段

| 字段名 | 类型 | 描述 | 自动填充规则 |
|--------|------|------|-------------|
| createTime | LocalDateTime | 创建时间 | 插入时自动填充当前时间 |
| updateTime | LocalDateTime | 更新时间 | 插入和更新时自动填充当前时间 |
| createUser | Long | 创建人ID | 插入时自动填充当前登录用户ID |
| updateUser | Long | 更新人ID | 更新时自动填充当前登录用户ID |
| isDelete | Boolean | 逻辑删除标记 | 逻辑删除字段 |
| deleteTime | LocalDateTime | 删除时间 | 逻辑删除时填充 |

## 使用方式

### 1. 实体类继承 BaseEntity

```java
@Data
@Schema(name = "用户实体", description = "用户信息")
@Accessors(chain = true)
@Table(value = "tb_user", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    
    @Schema(description = "用户ID", example = "1")
    @Id(keyType = KeyType.Auto)
    private Long id;
    
    @Schema(description = "用户名", example = "张三")
    private String username;
    
    // 其他业务字段...
}
```

### 2. 必要的注解配置

- `@Table` 注解中必须配置 `onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class`
- `@EqualsAndHashCode(callSuper = true)` 确保父类字段参与equals和hashCode计算

### 3. 数据库表结构

确保数据库表包含对应的字段：

```sql
CREATE TABLE tb_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    -- 其他业务字段...
    
    -- BaseEntity 公共字段
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_user BIGINT COMMENT '创建人',
    update_user BIGINT COMMENT '更新人',
    is_delete TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    delete_time DATETIME COMMENT '删除时间'
);
```

## 自动填充机制

### EntityChangeListener

通过 `EntityChangeListener` 实现自动填充：

- **插入时**: 自动填充 `createTime` 和 `createUser`
- **更新时**: 自动填充 `updateTime` 和 `updateUser`

### UserContext 用户上下文

`UserContext` 工具类用于获取当前登录用户信息：

```java
// 获取当前登录用户ID
Long userId = UserContext.getCurrentUserId();

// 获取当前登录用户ID，未登录返回默认值
Long userId = UserContext.getCurrentUserId(0L);

// 检查是否已登录
boolean isLogin = UserContext.isLogin();
```

## 注意事项

1. **继承关系**: 实体类必须继承 `BaseEntity`
2. **监听器配置**: 必须在 `@Table` 注解中配置 `EntityChangeListener`
3. **用户上下文**: 自动填充用户ID依赖于 SaToken 的登录状态
4. **字段命名**: 公共字段采用驼峰命名，数据库字段采用下划线命名
5. **逻辑删除**: `isDelete` 字段用于逻辑删除，配置了 `@Column(isLogicDelete = true)`

## 示例

### 完整的实体类示例

```java
@Data
@Schema(name = "角色实体", description = "角色信息")
@Accessors(chain = true)
@Table(value = "tb_role", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {

    @Schema(description = "角色ID", example = "1")
    @Id(keyType = KeyType.Auto)
    private Long id;

    @Schema(description = "角色名称", example = "管理员")
    @Column
    private String roleName;

    @Schema(description = "角色代码", example = "ADMIN")
    @Column
    private String roleCode;
}
```

### 使用效果

当执行插入或更新操作时，公共字段会自动填充：

```java
// 插入用户
User user = new User()
    .setUsername("张三")
    .setEmail("zhangsan@example.com");

userService.save(user);
// createTime 和 createUser 会自动填充

// 更新用户
user.setEmail("newemail@example.com");
userService.updateById(user);
// updateTime 和 updateUser 会自动填充
```

## 优势

1. **减少重复代码**: 无需在每个实体类中重复定义公共字段
2. **统一管理**: 公共字段的定义和行为统一管理
3. **自动填充**: 无需手动设置创建时间、更新时间等字段
4. **审计功能**: 自动记录操作人和操作时间，便于审计
5. **逻辑删除**: 内置逻辑删除支持 