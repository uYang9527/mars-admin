# 部署指南

本章节将指导您如何将Mars Admin项目部署到生产环境。

## 部署准备

### 环境要求

- **操作系统**：CentOS 7+、Ubuntu 18.04+、Windows Server 2016+
- **JDK**：17+
- **MySQL**：8.0+
- **Redis**：6.0+
- **Nginx**：1.18+（用于前端部署和反向代理）

### 服务器配置推荐

- **CPU**：2核+
- **内存**：4GB+
- **磁盘**：50GB+
- **带宽**：5Mbps+

## 后端部署

### 打包构建

在开发环境中执行以下命令打包后端项目：

```bash
# 进入后端项目目录
cd mars-admin

# 打包（跳过测试）
mvn clean package -DskipTests
```

打包完成后，将在`target`目录下生成`mars-admin.jar`文件。

### 配置文件准备

1. 创建生产环境配置文件`application-prod.yml`：

```yaml
# 服务配置
server:
  port: 8080
  servlet:
    context-path: /

# Spring配置
spring:
  # 数据库配置
  datasource:
    url: jdbc:mysql://localhost:3306/mars_admin?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
    username: production_user
    password: strong_password
    driver-class-name: com.mysql.cj.jdbc.Driver
    
  # Redis配置
  redis:
    host: 127.0.0.1
    port: 6379
    password: redis_password
    database: 0
    
# 日志配置
logging:
  level:
    root: warn
    com.mars.admin: info
  file:
    name: /var/log/mars-admin/app.log
    
# 文件上传配置
file:
  upload-dir: /var/mars-admin/uploads/
```

### 部署步骤

1. **传输文件到服务器**

   将以下文件上传到服务器：
   - `mars-admin.jar`
   - `application-prod.yml`
   - 数据库初始化脚本`mars_admin.sql`

2. **初始化数据库**

   ```bash
   # 登录MySQL
   mysql -u root -p
   
   # 创建数据库
   CREATE DATABASE mars_admin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
   
   # 创建生产用户并授权
   CREATE USER 'production_user'@'localhost' IDENTIFIED BY 'strong_password';
   GRANT ALL PRIVILEGES ON mars_admin.* TO 'production_user'@'localhost';
   FLUSH PRIVILEGES;
   
   # 退出MySQL
   exit
   
   # 导入数据库脚本
   mysql -u production_user -p mars_admin < mars_admin.sql
   ```

3. **创建服务目录**

   ```bash
   # 创建应用目录
   mkdir -p /opt/mars-admin
   
   # 创建日志目录
   mkdir -p /var/log/mars-admin
   
   # 创建上传目录
   mkdir -p /var/mars-admin/uploads
   
   # 设置权限
   chmod 755 /opt/mars-admin
   chmod 755 /var/log/mars-admin
   chmod 755 /var/mars-admin/uploads
   ```

4. **复制文件到应用目录**

   ```bash
   # 复制JAR文件
   cp mars-admin.jar /opt/mars-admin/
   
   # 复制配置文件
   cp application-prod.yml /opt/mars-admin/
   ```

5. **创建启动脚本**

   创建`/opt/mars-admin/start.sh`文件：

   ```bash
   #!/bin/bash
   
   nohup java -jar /opt/mars-admin/mars-admin.jar --spring.profiles.active=prod --spring.config.location=file:/opt/mars-admin/application-prod.yml > /dev/null 2>&1 &
   
   echo $! > /opt/mars-admin/app.pid
   
   echo "Mars Admin started with PID: $(cat /opt/mars-admin/app.pid)"
   ```

   创建`/opt/mars-admin/stop.sh`文件：

   ```bash
   #!/bin/bash
   
   if [ -f /opt/mars-admin/app.pid ]; then
     PID=$(cat /opt/mars-admin/app.pid)
     echo "Stopping Mars Admin with PID: $PID"
     kill -15 $PID
     rm -f /opt/mars-admin/app.pid
     echo "Mars Admin stopped"
   else
     echo "PID file not found"
   fi
   ```

   设置执行权限：

   ```bash
   chmod +x /opt/mars-admin/start.sh
   chmod +x /opt/mars-admin/stop.sh
   ```

6. **启动应用**

   ```bash
   cd /opt/mars-admin
   ./start.sh
   ```

7. **设置开机自启**

   创建系统服务文件`/etc/systemd/system/mars-admin.service`：

   ```
   [Unit]
   Description=Mars Admin Service
   After=network.target mysql.service redis.service
   
   [Service]
   Type=forking
   ExecStart=/opt/mars-admin/start.sh
   ExecStop=/opt/mars-admin/stop.sh
   Restart=on-failure
   User=root
   Group=root
   
   [Install]
   WantedBy=multi-user.target
   ```

   启用服务：

   ```bash
   systemctl daemon-reload
   systemctl enable mars-admin
   systemctl start mars-admin
   ```

## 前端部署

### 打包构建

在开发环境中执行以下命令打包前端项目：

```bash
# 进入前端项目目录
cd mars-admin-ui

# 安装依赖
pnpm install

# 构建生产环境版本
pnpm build
```

打包完成后，将在`dist`目录下生成静态文件。

### 配置Nginx

1. **安装Nginx**（如果未安装）

   ```bash
   # CentOS
   yum install -y nginx
   
   # Ubuntu
   apt-get install -y nginx
   ```

2. **创建Nginx配置**

   创建配置文件`/etc/nginx/conf.d/mars-admin.conf`：

   ```nginx
   server {
       listen 80;
       server_name your-domain.com;  # 替换为您的域名
       
       # 前端静态文件
       location / {
           root /var/www/mars-admin;
           index index.html;
           try_files $uri $uri/ /index.html;
       }
       
       # API代理
       location /api/ {
           proxy_pass http://localhost:8080/;
           proxy_set_header Host $host;
           proxy_set_header X-Real-IP $remote_addr;
           proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
       }
       
       # 静态资源缓存
       location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
           root /var/www/mars-admin;
           expires 7d;
           add_header Cache-Control "public, max-age=604800";
       }
   }
   ```

3. **部署前端文件**

   ```bash
   # 创建部署目录
   mkdir -p /var/www/mars-admin
   
   # 复制前端文件
   cp -r dist/* /var/www/mars-admin/
   
   # 设置权限
   chown -R nginx:nginx /var/www/mars-admin
   chmod -R 755 /var/www/mars-admin
   ```

4. **重启Nginx**

   ```bash
   # 检查配置
   nginx -t
   
   # 重启Nginx
   systemctl restart nginx
   ```

## HTTPS配置（推荐）

为了提高安全性，建议配置HTTPS。

### 获取SSL证书

1. 使用Let's Encrypt免费证书：

   ```bash
   # 安装certbot
   yum install -y certbot python3-certbot-nginx
   
   # 获取证书
   certbot --nginx -d your-domain.com
   ```

2. 或使用购买的商业证书，将证书文件放置在适当位置。

### 配置Nginx HTTPS

修改Nginx配置文件`/etc/nginx/conf.d/mars-admin.conf`：

```nginx
server {
    listen 80;
    server_name your-domain.com;
    return 301 https://$host$request_uri;
}

server {
    listen 443 ssl;
    server_name your-domain.com;
    
    ssl_certificate /etc/letsencrypt/live/your-domain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/your-domain.com/privkey.pem;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    
    # 前端静态文件
    location / {
        root /var/www/mars-admin;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
    
    # API代理
    location /api/ {
        proxy_pass http://localhost:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
    
    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        root /var/www/mars-admin;
        expires 7d;
        add_header Cache-Control "public, max-age=604800";
    }
}
```

重启Nginx：

```bash
nginx -t
systemctl restart nginx
```

## 生产环境优化

### JVM优化

修改启动脚本`/opt/mars-admin/start.sh`，添加JVM参数：

```bash
#!/bin/bash

nohup java -Xms1g -Xmx2g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m -jar /opt/mars-admin/mars-admin.jar --spring.profiles.active=prod --spring.config.location=file:/opt/mars-admin/application-prod.yml > /dev/null 2>&1 &

echo $! > /opt/mars-admin/app.pid

echo "Mars Admin started with PID: $(cat /opt/mars-admin/app.pid)"
```

### 数据库优化

1. **配置MySQL**

   编辑MySQL配置文件`/etc/my.cnf`或`/etc/mysql/my.cnf`：

   ```ini
   [mysqld]
   # 缓冲池大小
   innodb_buffer_pool_size = 1G
   
   # 日志文件大小
   innodb_log_file_size = 256M
   
   # 连接数
   max_connections = 500
   
   # 查询缓存
   query_cache_size = 64M
   query_cache_type = 1
   ```

2. **创建数据库索引**

   根据业务需求和查询性能，为常用查询添加适当的索引。

### Redis优化

编辑Redis配置文件`/etc/redis/redis.conf`：

```
# 内存限制
maxmemory 1gb

# 内存策略
maxmemory-policy allkeys-lru

# 持久化
appendonly yes
appendfsync everysec
```

### Nginx优化

编辑Nginx配置文件`/etc/nginx/nginx.conf`：

```nginx
worker_processes auto;
worker_rlimit_nofile 65535;

events {
    worker_connections 10240;
    multi_accept on;
    use epoll;
}

http {
    # 开启gzip压缩
    gzip on;
    gzip_comp_level 5;
    gzip_min_length 256;
    gzip_proxied any;
    gzip_vary on;
    gzip_types
        application/javascript
        application/json
        application/x-javascript
        application/xml
        text/css
        text/javascript
        text/plain
        text/xml;
    
    # 缓存设置
    client_body_buffer_size 10K;
    client_header_buffer_size 1k;
    client_max_body_size 20m;
    large_client_header_buffers 4 4k;
    
    # 超时设置
    client_body_timeout 12;
    client_header_timeout 12;
    keepalive_timeout 15;
    send_timeout 10;
}
```

## 监控与维护

### 日志管理

使用Logrotate管理日志文件：

创建配置文件`/etc/logrotate.d/mars-admin`：

```
/var/log/mars-admin/*.log {
    daily
    missingok
    rotate 7
    compress
    delaycompress
    notifempty
    create 644 root root
    postrotate
        systemctl reload mars-admin >/dev/null 2>&1 || true
    endscript
}
```

### 备份策略

1. **数据库备份**

   创建备份脚本`/opt/mars-admin/backup.sh`：

   ```bash
   #!/bin/bash
   
   BACKUP_DIR="/var/backups/mars-admin"
   DATE=$(date +%Y%m%d_%H%M%S)
   BACKUP_FILE="$BACKUP_DIR/mars_admin_$DATE.sql"
   
   # 创建备份目录
   mkdir -p $BACKUP_DIR
   
   # 备份数据库
   mysqldump -u production_user -p'strong_password' mars_admin > $BACKUP_FILE
   
   # 压缩备份文件
   gzip $BACKUP_FILE
   
   # 删除7天前的备份
   find $BACKUP_DIR -name "mars_admin_*.sql.gz" -mtime +7 -delete
   ```

   设置执行权限并添加到crontab：

   ```bash
   chmod +x /opt/mars-admin/backup.sh
   
   # 添加到crontab
   echo "0 2 * * * /opt/mars-admin/backup.sh" >> /etc/crontab
   ```

2. **应用备份**

   定期备份应用目录、配置文件和上传文件。

### 监控系统

推荐使用以下工具监控系统：

1. **Prometheus + Grafana**：监控系统指标
2. **ELK Stack**：日志收集与分析
3. **Pinpoint/SkyWalking**：分布式追踪

## 安全加固

1. **防火墙配置**

   ```bash
   # 开启防火墙
   systemctl start firewalld
   systemctl enable firewalld
   
   # 只允许必要端口
   firewall-cmd --permanent --add-service=http
   firewall-cmd --permanent --add-service=https
   firewall-cmd --permanent --add-port=8080/tcp
   firewall-cmd --reload
   ```

2. **定期更新**

   ```bash
   # CentOS
   yum update -y
   
   # Ubuntu
   apt-get update && apt-get upgrade -y
   ```

3. **安全审计**

   - 定期检查系统日志
   - 监控异常登录
   - 定期修改密码

## 故障排除

### 常见问题

1. **应用无法启动**
   - 检查日志文件`/var/log/mars-admin/app.log`
   - 验证数据库连接
   - 确认端口未被占用

2. **前端无法访问**
   - 检查Nginx错误日志`/var/log/nginx/error.log`
   - 验证静态文件权限
   - 测试API代理配置

3. **性能问题**
   - 检查JVM内存使用情况
   - 分析数据库慢查询
   - 监控系统资源使用率

### 联系支持

如遇到无法解决的问题，请通过以下方式获取支持：

- GitHub Issues: https://github.com/Marsfactory/mars-admin/issues
- 电子邮件: support@mars-admin.com

## 升级指南

当有新版本发布时，请按照以下步骤进行升级：

1. **备份数据**
   - 备份数据库
   - 备份配置文件
   - 备份上传文件

2. **停止服务**
   ```bash
   systemctl stop mars-admin
   ```

3. **替换文件**
   - 上传新版本JAR文件
   - 更新前端静态文件

4. **执行数据库迁移**（如有）
   - 运行升级SQL脚本

5. **启动服务**
   ```bash
   systemctl start mars-admin
   ```

6. **验证升级**
   - 检查应用是否正常运行
   - 验证新功能是否可用 