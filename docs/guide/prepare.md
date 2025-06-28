# 环境准备

在开始使用Mars Admin之前，请确保您的开发环境满足以下要求。

## 系统要求

- 操作系统：Windows 10+、macOS 10.15+、Linux
- 内存：至少8GB RAM（推荐16GB或更高）
- 硬盘空间：至少10GB可用空间

## 后端环境

### 必需软件

1. **JDK 17+**
   - 下载地址：[Oracle JDK](https://www.oracle.com/java/technologies/downloads/#java17) 或 [OpenJDK](https://adoptium.net/)
   - 安装后配置JAVA_HOME环境变量

2. **MySQL 8.x**
   - 下载地址：[MySQL官网](https://dev.mysql.com/downloads/mysql/)
   - 安装后创建数据库：`mars_admin`
   - 字符集：utf8mb4
   - 排序规则：utf8mb4_general_ci

3. **Redis 6.x+**
   - 下载地址：[Redis官网](https://redis.io/download)
   - Windows用户可使用：[Redis for Windows](https://github.com/tporadowski/redis/releases)

4. **Maven 3.8+**
   - 下载地址：[Maven官网](https://maven.apache.org/download.cgi)
   - 安装后配置MAVEN_HOME环境变量

### 可选软件

1. **IntelliJ IDEA**（推荐）
   - 下载地址：[IDEA官网](https://www.jetbrains.com/idea/download/)
   - 社区版即可，但Ultimate版提供更好的Spring Boot支持

2. **Navicat/DBeaver**（数据库管理工具）
   - Navicat下载：[Navicat官网](https://www.navicat.com/)
   - DBeaver下载：[DBeaver官网](https://dbeaver.io/download/)

## 前端环境

### 必需软件

1. **Node.js 18+**
   - 下载地址：[Node.js官网](https://nodejs.org/)
   - 安装后确保npm可用

2. **pnpm**（推荐）
   - 安装命令：`npm install -g pnpm`

### 可选软件

1. **Visual Studio Code**（推荐）
   - 下载地址：[VS Code官网](https://code.visualstudio.com/)
   - 推荐插件：
     - Vue Language Features (Volar)
     - TypeScript Vue Plugin (Volar)
     - ESLint
     - Prettier
     - Auto Import

## 环境验证

完成安装后，请验证环境是否正确配置：

```bash
# 验证Java版本
java -version

# 验证Maven版本
mvn -v

# 验证Node.js版本
node -v

# 验证npm版本
npm -v

# 验证pnpm版本
pnpm -v
```

确保所有版本满足要求后，您可以继续[项目结构](structure.md)和[本地开发](development.md)章节。 