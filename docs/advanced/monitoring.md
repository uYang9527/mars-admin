# 系统监控

系统监控是Mars Admin的重要组成部分，提供了全面的系统运行状态监控和性能指标统计，帮助运维人员及时发现和解决系统问题。

## 功能概述

Mars Admin的系统监控模块包含以下功能：

- **服务器监控**：CPU、内存、磁盘、网络等硬件资源监控
- **JVM监控**：堆内存、非堆内存、垃圾回收等JVM运行状态
- **应用监控**：线程池、连接池、缓存等应用层面监控
- **实时监控**：实时数据展示和历史趋势分析
- **告警通知**：异常情况的及时告警和通知
- **性能分析**：系统性能瓶颈分析和优化建议

## 监控架构

### 监控层次

```
┌─────────────────────────────────────┐
│           业务监控层                 │
├─────────────────────────────────────┤
│           应用监控层                 │
├─────────────────────────────────────┤
│           中间件监控层               │
├─────────────────────────────────────┤
│           系统监控层                 │
├─────────────────────────────────────┤
│           硬件监控层                 │
└─────────────────────────────────────┘
```

### 监控组件

| 组件 | 功能 | 实现方式 |
|------|------|----------|
| Spring Boot Actuator | 应用健康检查 | 内置端点 |
| Micrometer | 指标收集 | 注解和代码埋点 |
| System Info | 系统信息获取 | OSHI库 |
| Custom Metrics | 自定义指标 | 业务埋点 |

## 服务器监控

### 1. 服务器信息接口

**接口地址：** `GET /monitor/server`

**功能描述：** 获取服务器的详细信息，包括CPU、内存、磁盘、JVM等。

**响应示例：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "cpu": {
      "cpuNum": 8,
      "total": 100.0,
      "sys": 15.2,
      "used": 25.8,
      "wait": 0.1,
      "free": 74.2
    },
    "mem": {
      "total": 16384.0,
      "used": 8192.0,
      "free": 8192.0,
      "usage": 50.0
    },
    "jvm": {
      "total": 1024.0,
      "max": 1024.0,
      "used": 512.0,
      "free": 512.0,
      "usage": 50.0,
      "name": "OpenJDK 64-Bit Server VM",
      "version": "17.0.2+8-Ubuntu-120.04"
    },
    "sys": {
      "computerName": "mars-admin-server",
      "computerIp": "192.168.1.100",
      "osName": "Linux",
      "osArch": "amd64",
      "userDir": "/opt/mars-admin"
    },
    "sysFiles": [
      {
        "dirName": "/",
        "sysTypeName": "ext4",
        "typeName": "本地固定磁盘",
        "total": "100.0 GB",
        "free": "50.0 GB",
        "used": "50.0 GB",
        "usage": 50.0
      }
    ]
  }
}
```

### 2. 服务器监控实现

```java
@RestController
@RequestMapping("/monitor/server")
public class ServerController {
    
    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping()
    public AjaxResult getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return AjaxResult.success(server);
    }
}

/**
 * 服务器相关信息
 */
public class Server {
    private static final int OSHI_WAIT_SECOND = 1000;
    
    /**
     * CPU相关信息
     */
    private Cpu cpu = new Cpu();
    
    /**
     * 內存相关信息
     */
    private Mem mem = new Mem();
    
    /**
     * JVM相关信息
     */
    private Jvm jvm = new Jvm();
    
    /**
     * 服务器信息
     */
    private Sys sys = new Sys();
    
    /**
     * 磁盘相关信息
     */
    private List<SysFile> sysFiles = new LinkedList<SysFile>();
    
    public void copyTo() throws Exception {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        
        setCpuInfo(hal.getProcessor());
        setMemInfo(hal.getMemory());
        setSysInfo();
        setJvmInfo();
        setSysFiles(si.getOperatingSystem());
    }
    
    /**
     * 设置CPU信息
     */
    private void setCpuInfo(CentralProcessor processor) {
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        
        cpu.setCpuNum(processor.getLogicalProcessorCount());
        cpu.setTotal(totalCpu);
        cpu.setSys(cSys);
        cpu.setUsed(user);
        cpu.setWait(iowait);
        cpu.setFree(idle);
    }
    
    /**
     * 设置内存信息
     */
    private void setMemInfo(GlobalMemory memory) {
        mem.setTotal(memory.getTotal());
        mem.setUsed(memory.getTotal() - memory.getAvailable());
        mem.setFree(memory.getAvailable());
    }
    
    /**
     * 设置服务器信息
     */
    private void setSysInfo() {
        Properties props = System.getProperties();
        sys.setComputerName(IpUtils.getHostName());
        sys.setComputerIp(IpUtils.getHostIp());
        sys.setOsName(props.getProperty("os.name"));
        sys.setOsArch(props.getProperty("os.arch"));
        sys.setUserDir(props.getProperty("user.dir"));
    }
    
    /**
     * 设置Java虚拟机
     */
    private void setJvmInfo() throws UnknownHostException {
        Properties props = System.getProperties();
        jvm.setTotal(Runtime.getRuntime().totalMemory());
        jvm.setMax(Runtime.getRuntime().maxMemory());
        jvm.setFree(Runtime.getRuntime().freeMemory());
        jvm.setVersion(props.getProperty("java.version"));
        jvm.setName(ManagementFactory.getRuntimeMXBean().getVmName());
    }
    
    /**
     * 设置磁盘信息
     */
    private void setSysFiles(OperatingSystem os) {
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            SysFile sysFile = new SysFile();
            sysFile.setDirName(fs.getMount());
            sysFile.setSysTypeName(fs.getType());
            sysFile.setTypeName(fs.getName());
            sysFile.setTotal(convertFileSize(total));
            sysFile.setFree(convertFileSize(free));
            sysFile.setUsed(convertFileSize(used));
            sysFile.setUsage(Arith.mul(Arith.div(used, total, 4), 100));
            sysFiles.add(sysFile);
        }
    }
    
    /**
     * 字节转换
     */
    public String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }
}
```

### 3. CPU信息类

```java
/**
 * CPU相关信息
 */
public class Cpu {
    /**
     * 核心数
     */
    private int cpuNum;
    
    /**
     * CPU总的使用率
     */
    private double total;
    
    /**
     * CPU系统使用率
     */
    private double sys;
    
    /**
     * CPU用户使用率
     */
    private double used;
    
    /**
     * CPU当前等待率
     */
    private double wait;
    
    /**
     * CPU当前空闲率
     */
    private double free;
    
    public double getTotal() {
        return Arith.round(Arith.mul(total, 100), 2);
    }
    
    public double getSys() {
        return Arith.round(Arith.mul(sys / total, 100), 2);
    }
    
    public double getUsed() {
        return Arith.round(Arith.mul(used / total, 100), 2);
    }
    
    public double getWait() {
        return Arith.round(Arith.mul(wait / total, 100), 2);
    }
    
    public double getFree() {
        return Arith.round(Arith.mul(free / total, 100), 2);
    }
    
    // getter/setter方法
}
```

### 4. 内存信息类

```java
/**
 * 內存相关信息
 */
public class Mem {
    /**
     * 内存总量
     */
    private double total;
    
    /**
     * 已用内存
     */
    private double used;
    
    /**
     * 剩余内存
     */
    private double free;
    
    public double getTotal() {
        return Arith.div(total, (1024 * 1024 * 1024), 2);
    }
    
    public double getUsed() {
        return Arith.div(used, (1024 * 1024 * 1024), 2);
    }
    
    public double getFree() {
        return Arith.div(free, (1024 * 1024 * 1024), 2);
    }
    
    public double getUsage() {
        return Arith.mul(Arith.div(used, total, 4), 100);
    }
    
    // getter/setter方法
}
```

## JVM监控

### 1. JVM信息类

```java
/**
 * JVM相关信息
 */
public class Jvm {
    /**
     * 当前JVM占用的内存总数(M)
     */
    private double total;
    
    /**
     * JVM最大可用内存总数(M)
     */
    private double max;
    
    /**
     * JVM空闲内存(M)
     */
    private double free;
    
    /**
     * JDK版本
     */
    private String version;
    
    /**
     * JDK路径
     */
    private String home;
    
    /**
     * JVM名称
     */
    private String name;
    
    /**
     * JVM启动时间
     */
    private String startTime;
    
    /**
     * JVM运行时间
     */
    private String runTime;
    
    /**
     * 运行参数
     */
    private String inputArgs;
    
    public double getTotal() {
        return Arith.div(total, (1024 * 1024), 2);
    }
    
    public double getMax() {
        return Arith.div(max, (1024 * 1024), 2);
    }
    
    public double getFree() {
        return Arith.div(free, (1024 * 1024), 2);
    }
    
    public double getUsed() {
        return Arith.div(total - free, (1024 * 1024), 2);
    }
    
    public double getUsage() {
        return Arith.mul(Arith.div(total - free, total, 4), 100);
    }
    
    /**
     * 获取JDK名称
     */
    public String getName() {
        return ManagementFactory.getRuntimeMXBean().getVmName();
    }
    
    /**
     * 获取JDK版本
     */
    public String getVersion() {
        return System.getProperty("java.version");
    }
    
    /**
     * 获取JDK路径
     */
    public String getHome() {
        return System.getProperty("java.home");
    }
    
    /**
     * JVM启动时间
     */
    public String getStartTime() {
        return DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, DateUtils.getServerStartDate());
    }
    
    /**
     * JVM运行时间
     */
    public String getRunTime() {
        return DateUtils.timeDistance(DateUtils.getNowDate(), DateUtils.getServerStartDate());
    }
    
    /**
     * 运行参数
     */
    public String getInputArgs() {
        return ManagementFactory.getRuntimeMXBean().getInputArguments().toString();
    }
    
    // getter/setter方法
}
```

## 前端监控页面

### 1. 服务器监控页面

```vue
<template>
  <div class="server-monitor">
    <a-row :gutter="16">
      <!-- CPU监控 -->
      <a-col :span="12">
        <a-card title="CPU监控" class="monitor-card">
          <div class="monitor-item">
            <div class="monitor-label">CPU核心数</div>
            <div class="monitor-value">{{ serverInfo.cpu?.cpuNum || 0 }} 核</div>
          </div>
          <div class="monitor-item">
            <div class="monitor-label">用户使用率</div>
            <a-progress 
              :percent="serverInfo.cpu?.used || 0" 
              :status="getProgressStatus(serverInfo.cpu?.used)"
            />
          </div>
          <div class="monitor-item">
            <div class="monitor-label">系统使用率</div>
            <a-progress 
              :percent="serverInfo.cpu?.sys || 0"
              :status="getProgressStatus(serverInfo.cpu?.sys)"
            />
          </div>
          <div class="monitor-item">
            <div class="monitor-label">空闲率</div>
            <a-progress 
              :percent="serverInfo.cpu?.free || 0"
              status="success"
            />
          </div>
        </a-card>
      </a-col>

      <!-- 内存监控 -->
      <a-col :span="12">
        <a-card title="内存监控" class="monitor-card">
          <div class="monitor-item">
            <div class="monitor-label">总内存</div>
            <div class="monitor-value">{{ serverInfo.mem?.total || 0 }} GB</div>
          </div>
          <div class="monitor-item">
            <div class="monitor-label">已用内存</div>
            <a-progress 
              :percent="serverInfo.mem?.usage || 0"
              :status="getProgressStatus(serverInfo.mem?.usage)"
            />
            <div class="monitor-detail">
              {{ serverInfo.mem?.used || 0 }} GB / {{ serverInfo.mem?.total || 0 }} GB
            </div>
          </div>
          <div class="monitor-item">
            <div class="monitor-label">剩余内存</div>
            <div class="monitor-value">{{ serverInfo.mem?.free || 0 }} GB</div>
          </div>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="16" style="margin-top: 16px;">
      <!-- JVM监控 -->
      <a-col :span="12">
        <a-card title="JVM监控" class="monitor-card">
          <div class="monitor-item">
            <div class="monitor-label">JVM名称</div>
            <div class="monitor-value">{{ serverInfo.jvm?.name }}</div>
          </div>
          <div class="monitor-item">
            <div class="monitor-label">Java版本</div>
            <div class="monitor-value">{{ serverInfo.jvm?.version }}</div>
          </div>
          <div class="monitor-item">
            <div class="monitor-label">最大内存</div>
            <div class="monitor-value">{{ serverInfo.jvm?.max || 0 }} MB</div>
          </div>
          <div class="monitor-item">
            <div class="monitor-label">已用内存</div>
            <a-progress 
              :percent="serverInfo.jvm?.usage || 0"
              :status="getProgressStatus(serverInfo.jvm?.usage)"
            />
            <div class="monitor-detail">
              {{ serverInfo.jvm?.used || 0 }} MB / {{ serverInfo.jvm?.max || 0 }} MB
            </div>
          </div>
        </a-card>
      </a-col>

      <!-- 系统信息 -->
      <a-col :span="12">
        <a-card title="系统信息" class="monitor-card">
          <div class="monitor-item">
            <div class="monitor-label">服务器名称</div>
            <div class="monitor-value">{{ serverInfo.sys?.computerName }}</div>
          </div>
          <div class="monitor-item">
            <div class="monitor-label">服务器IP</div>
            <div class="monitor-value">{{ serverInfo.sys?.computerIp }}</div>
          </div>
          <div class="monitor-item">
            <div class="monitor-label">操作系统</div>
            <div class="monitor-value">{{ serverInfo.sys?.osName }} {{ serverInfo.sys?.osArch }}</div>
          </div>
          <div class="monitor-item">
            <div class="monitor-label">系统架构</div>
            <div class="monitor-value">{{ serverInfo.sys?.osArch }}</div>
          </div>
        </a-card>
      </a-col>
    </a-row>

    <!-- 磁盘监控 -->
    <a-row style="margin-top: 16px;">
      <a-col :span="24">
        <a-card title="磁盘监控" class="monitor-card">
          <a-table 
            :columns="diskColumns" 
            :data-source="serverInfo.sysFiles || []"
            :pagination="false"
            size="small"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'usage'">
                <a-progress 
                  :percent="record.usage"
                  :status="getProgressStatus(record.usage)"
                  size="small"
                />
              </template>
            </template>
          </a-table>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { fetchServerInfo } from '@/service/api/monitor'

// 响应式数据
const serverInfo = ref<any>({})
let timer: NodeJS.Timeout | null = null

// 磁盘监控表格列
const diskColumns = [
  {
    title: '盘符路径',
    dataIndex: 'dirName',
    key: 'dirName'
  },
  {
    title: '文件系统',
    dataIndex: 'sysTypeName',
    key: 'sysTypeName'
  },
  {
    title: '盘符类型',
    dataIndex: 'typeName',
    key: 'typeName'
  },
  {
    title: '总大小',
    dataIndex: 'total',
    key: 'total'
  },
  {
    title: '可用大小',
    dataIndex: 'free',
    key: 'free'
  },
  {
    title: '已用大小',
    dataIndex: 'used',
    key: 'used'
  },
  {
    title: '已用百分比',
    dataIndex: 'usage',
    key: 'usage'
  }
]

// 方法定义
const loadServerInfo = async () => {
  try {
    const { data } = await fetchServerInfo()
    serverInfo.value = data
  } catch (error) {
    console.error('获取服务器信息失败:', error)
  }
}

const getProgressStatus = (percent: number) => {
  if (percent >= 90) return 'exception'
  if (percent >= 70) return 'active'
  return 'success'
}

const startAutoRefresh = () => {
  timer = setInterval(() => {
    loadServerInfo()
  }, 5000) // 每5秒刷新一次
}

const stopAutoRefresh = () => {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
}

// 生命周期
onMounted(() => {
  loadServerInfo()
  startAutoRefresh()
})

onUnmounted(() => {
  stopAutoRefresh()
})
</script>

<style scoped>
.server-monitor {
  .monitor-card {
    height: 100%;
    
    .monitor-item {
      margin-bottom: 16px;
      
      .monitor-label {
        font-size: 14px;
        color: #666;
        margin-bottom: 8px;
      }
      
      .monitor-value {
        font-size: 16px;
        font-weight: 500;
        color: #333;
      }
      
      .monitor-detail {
        font-size: 12px;
        color: #999;
        margin-top: 4px;
      }
    }
  }
}
</style>
```

### 2. 监控API服务

```typescript
// src/service/api/monitor.ts
import { request } from '@/service/request'

/**
 * 获取服务器信息
 */
export function fetchServerInfo() {
  return request.get<ServerInfo>('/monitor/server')
}

/**
 * 获取在线用户列表
 */
export function fetchOnlineUsers(params?: OnlineUserQuery) {
  return request.get<PageResult<OnlineUser>>('/monitor/online/list', { params })
}

/**
 * 强制退出用户
 */
export function forceLogout(tokenId: string) {
  return request.delete(`/monitor/online/${tokenId}`)
}

/**
 * 批量强制退出用户
 */
export function batchForceLogout(tokenIds: string[]) {
  return request.delete(`/monitor/online/${tokenIds.join(',')}`)
}

/**
 * 获取缓存信息
 */
export function fetchCacheInfo() {
  return request.get<CacheInfo>('/monitor/cache')
}

/**
 * 清理缓存
 */
export function clearCache(cacheNames?: string[]) {
  return request.delete('/monitor/cache', { data: cacheNames })
}

// 类型定义
export interface ServerInfo {
  cpu: CpuInfo
  mem: MemInfo
  jvm: JvmInfo
  sys: SysInfo
  sysFiles: SysFile[]
}

export interface CpuInfo {
  cpuNum: number
  total: number
  sys: number
  used: number
  wait: number
  free: number
}

export interface MemInfo {
  total: number
  used: number
  free: number
  usage: number
}

export interface JvmInfo {
  total: number
  max: number
  used: number
  free: number
  usage: number
  name: string
  version: string
  home: string
  startTime: string
  runTime: string
  inputArgs: string
}

export interface SysInfo {
  computerName: string
  computerIp: string
  osName: string
  osArch: string
  userDir: string
}

export interface SysFile {
  dirName: string
  sysTypeName: string
  typeName: string
  total: string
  free: string
  used: string
  usage: number
}
```

## 在线用户监控

### 1. 在线用户管理

```java
@RestController
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController {

    @Autowired
    private ISysUserOnlineService userOnlineService;

    @Autowired
    private RedisCache redisCache;

    @PreAuthorize("@ss.hasPermi('monitor:online:list')")
    @GetMapping("/list")
    public TableDataInfo list(String ipaddr, String userName) {
        List<SysUserOnline> userOnlineList = userOnlineService.selectUserOnlineList(ipaddr, userName);
        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));
        return getDataTable(userOnlineList);
    }

    @PreAuthorize("@ss.hasPermi('monitor:online:forceLogout')")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    public AjaxResult forceLogout(@PathVariable String tokenId) {
        redisCache.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + tokenId);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('monitor:online:batchLogout')")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenIds}")
    public AjaxResult batchForceLogout(@PathVariable String[] tokenIds) {
        for (String tokenId : tokenIds) {
            redisCache.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + tokenId);
        }
        return success();
    }
}
```

### 2. 在线用户服务

```java
@Service
public class SysUserOnlineServiceImpl implements ISysUserOnlineService {

    @Autowired
    private RedisCache redisCache;

    /**
     * 通过登录地址查询信息
     */
    @Override
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user) {
        if (StringUtils.equals(ipaddr, user.getIpaddr())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过用户名称查询信息
     */
    @Override
    public SysUserOnline selectOnlineByUserName(String userName, LoginUser user) {
        if (StringUtils.equals(userName, user.getUsername())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过登录地址/用户名称查询信息
     */
    @Override
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user) {
        if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 查询所有会话
     */
    @Override
    public List<SysUserOnline> selectUserOnlineList(String ipaddr, String userName) {
        List<SysUserOnline> userOnlineList = new ArrayList<SysUserOnline>();
        Collection<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        for (String key : keys) {
            LoginUser user = redisCache.getCacheObject(key);
            if (user != null) {
                SysUserOnline userOnline = new SysUserOnline();
                userOnline.setTokenId(user.getToken());
                userOnline.setUserName(user.getUsername());
                userOnline.setIpaddr(user.getIpaddr());
                userOnline.setLoginLocation(AddressUtils.getRealAddressByIP(user.getIpaddr()));
                userOnline.setBrowser(user.getBrowser());
                userOnline.setOs(user.getOs());
                userOnline.setLoginTime(user.getLoginTime());
                if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName)) {
                    userOnline = selectOnlineByInfo(ipaddr, userName, user);
                } else if (StringUtils.isNotEmpty(ipaddr)) {
                    userOnline = selectOnlineByIpaddr(ipaddr, user);
                } else if (StringUtils.isNotEmpty(userName) && StringUtils.isNotNull(selectOnlineByUserName(userName, user))) {
                    userOnline = selectOnlineByUserName(userName, user);
                }
                if (userOnline != null) {
                    userOnlineList.add(userOnline);
                }
            }
        }
        return userOnlineList;
    }

    /**
     * 强退用户
     */
    @Override
    public void forceLogout(String tokenId) {
        redisCache.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + tokenId);
    }

    /**
     * 登录用户信息转为在线用户信息
     */
    private SysUserOnline loginUserToUserOnline(LoginUser user) {
        if (user == null) {
            return null;
        }
        SysUserOnline sysUserOnline = new SysUserOnline();
        sysUserOnline.setTokenId(user.getToken());
        sysUserOnline.setUserName(user.getUsername());
        sysUserOnline.setIpaddr(user.getIpaddr());
        sysUserOnline.setLoginLocation(AddressUtils.getRealAddressByIP(user.getIpaddr()));
        sysUserOnline.setBrowser(user.getBrowser());
        sysUserOnline.setOs(user.getOs());
        sysUserOnline.setLoginTime(user.getLoginTime());
        if (user.getUser().getDept() != null) {
            sysUserOnline.setDeptName(user.getUser().getDept().getDeptName());
        }
        return sysUserOnline;
    }
}
```

## 缓存监控

### 1. 缓存信息接口

```java
@RestController
@RequestMapping("/monitor/cache")
public class CacheController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping()
    public AjaxResult getInfo() throws Exception {
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) connection -> connection.dbSize());

        Map<String, Object> result = new HashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        if (commandStats != null) {
            commandStats.stringPropertyNames().forEach(key -> {
                Map<String, String> data = new HashMap<>(2);
                String property = commandStats.getProperty(key);
                data.put("name", StringUtils.removeStart(key, "cmdstat_"));
                data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
                pieList.add(data);
            });
        }
        result.put("commandStats", pieList);
        return AjaxResult.success(result);
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/getNames")
    public AjaxResult cache() {
        return AjaxResult.success(redisTemplate.keys("*"));
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/getKeys/{cacheName}")
    public AjaxResult getCacheKeys(@PathVariable String cacheName) {
        Set<String> cacheKeys = redisTemplate.keys(cacheName + "*");
        return AjaxResult.success(cacheKeys);
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/getValue/{cacheName}/{cacheKey}")
    public AjaxResult getCacheValue(@PathVariable String cacheName, @PathVariable String cacheKey) {
        String cacheValue = redisTemplate.opsForValue().get(cacheKey);
        SysCache sysCache = new SysCache(cacheName, cacheKey, cacheValue);
        return AjaxResult.success(sysCache);
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:clear')")
    @DeleteMapping("/clearCacheName/{cacheName}")
    public AjaxResult clearCacheName(@PathVariable String cacheName) {
        Collection<String> cacheKeys = redisTemplate.keys(cacheName + "*");
        redisTemplate.delete(cacheKeys);
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:clear')")
    @DeleteMapping("/clearCacheKey/{cacheKey}")
    public AjaxResult clearCacheKey(@PathVariable String cacheKey) {
        redisTemplate.delete(cacheKey);
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:clear')")
    @DeleteMapping("/clearCacheAll")
    public AjaxResult clearCacheAll() {
        Collection<String> cacheKeys = redisTemplate.keys("*");
        redisTemplate.delete(cacheKeys);
        return AjaxResult.success();
    }
}
```

## 应用监控

### 1. Spring Boot Actuator配置

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
      base-path: /actuator
  endpoint:
    health:
      show-details: always
    metrics:
      enabled: true
  metrics:
    export:
      prometheus:
        enabled: true
  info:
    env:
      enabled: true
    java:
      enabled: true
    os:
      enabled: true
```

### 2. 自定义健康检查

```java
@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Health health() {
        Health.Builder builder = new Health.Builder();

        try {
            // 检查数据库连接
            checkDatabase(builder);

            // 检查Redis连接
            checkRedis(builder);

            // 检查磁盘空间
            checkDiskSpace(builder);

            return builder.up().build();
        } catch (Exception e) {
            return builder.down().withException(e).build();
        }
    }

    private void checkDatabase(Health.Builder builder) {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1)) {
                builder.withDetail("database", "UP");
            } else {
                builder.down().withDetail("database", "Connection invalid");
            }
        } catch (Exception e) {
            builder.down().withDetail("database", "Connection failed: " + e.getMessage());
        }
    }

    private void checkRedis(Health.Builder builder) {
        try {
            String pong = redisTemplate.execute((RedisCallback<String>) connection -> {
                return connection.ping();
            });
            if ("PONG".equals(pong)) {
                builder.withDetail("redis", "UP");
            } else {
                builder.down().withDetail("redis", "Ping failed");
            }
        } catch (Exception e) {
            builder.down().withDetail("redis", "Connection failed: " + e.getMessage());
        }
    }

    private void checkDiskSpace(Health.Builder builder) {
        File disk = new File("/");
        long freeSpace = disk.getFreeSpace();
        long totalSpace = disk.getTotalSpace();
        double freePercentage = (double) freeSpace / totalSpace * 100;

        if (freePercentage > 10) {
            builder.withDetail("diskSpace", Map.of(
                "free", freeSpace,
                "total", totalSpace,
                "freePercentage", freePercentage,
                "status", "UP"
            ));
        } else {
            builder.down().withDetail("diskSpace", Map.of(
                "free", freeSpace,
                "total", totalSpace,
                "freePercentage", freePercentage,
                "status", "LOW_DISK_SPACE"
            ));
        }
    }
}
```

### 3. 自定义指标

```java
@Component
public class CustomMetrics {

    private final Counter userLoginCounter;
    private final Timer requestTimer;
    private final Gauge activeUsersGauge;

    @Autowired
    private RedisCache redisCache;

    public CustomMetrics(MeterRegistry meterRegistry) {
        this.userLoginCounter = Counter.builder("user.login.count")
                .description("用户登录次数")
                .register(meterRegistry);

        this.requestTimer = Timer.builder("http.request.duration")
                .description("HTTP请求耗时")
                .register(meterRegistry);

        this.activeUsersGauge = Gauge.builder("user.active.count")
                .description("在线用户数")
                .register(meterRegistry, this, CustomMetrics::getActiveUserCount);
    }

    public void incrementLoginCount() {
        userLoginCounter.increment();
    }

    public Timer.Sample startRequestTimer() {
        return Timer.start();
    }

    public void recordRequestTime(Timer.Sample sample) {
        sample.stop(requestTimer);
    }

    private double getActiveUserCount() {
        Collection<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        return keys.size();
    }
}
```

## 告警通知

### 1. 告警配置

```java
@ConfigurationProperties(prefix = "monitor.alert")
@Data
public class AlertProperties {

    /**
     * 是否启用告警
     */
    private boolean enabled = true;

    /**
     * CPU使用率告警阈值
     */
    private double cpuThreshold = 80.0;

    /**
     * 内存使用率告警阈值
     */
    private double memoryThreshold = 80.0;

    /**
     * 磁盘使用率告警阈值
     */
    private double diskThreshold = 90.0;

    /**
     * 告警通知方式
     */
    private List<String> notificationTypes = Arrays.asList("email", "webhook");

    /**
     * 邮件配置
     */
    private EmailConfig email = new EmailConfig();

    /**
     * Webhook配置
     */
    private WebhookConfig webhook = new WebhookConfig();

    @Data
    public static class EmailConfig {
        private List<String> recipients = new ArrayList<>();
        private String subject = "Mars Admin 系统告警";
    }

    @Data
    public static class WebhookConfig {
        private String url;
        private Map<String, String> headers = new HashMap<>();
    }
}
```

### 2. 告警服务

```java
@Service
public class AlertService {

    @Autowired
    private AlertProperties alertProperties;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WebhookService webhookService;

    /**
     * 检查系统指标并发送告警
     */
    @Scheduled(fixedRate = 60000) // 每分钟检查一次
    public void checkSystemMetrics() {
        if (!alertProperties.isEnabled()) {
            return;
        }

        try {
            Server server = new Server();
            server.copyTo();

            List<AlertMessage> alerts = new ArrayList<>();

            // 检查CPU使用率
            if (server.getCpu().getUsed() > alertProperties.getCpuThreshold()) {
                alerts.add(new AlertMessage(
                    AlertLevel.WARNING,
                    "CPU使用率过高",
                    String.format("当前CPU使用率: %.2f%%, 超过阈值: %.2f%%",
                                server.getCpu().getUsed(), alertProperties.getCpuThreshold())
                ));
            }

            // 检查内存使用率
            if (server.getMem().getUsage() > alertProperties.getMemoryThreshold()) {
                alerts.add(new AlertMessage(
                    AlertLevel.WARNING,
                    "内存使用率过高",
                    String.format("当前内存使用率: %.2f%%, 超过阈值: %.2f%%",
                                server.getMem().getUsage(), alertProperties.getMemoryThreshold())
                ));
            }

            // 检查磁盘使用率
            for (SysFile sysFile : server.getSysFiles()) {
                if (sysFile.getUsage() > alertProperties.getDiskThreshold()) {
                    alerts.add(new AlertMessage(
                        AlertLevel.CRITICAL,
                        "磁盘空间不足",
                        String.format("磁盘 %s 使用率: %.2f%%, 超过阈值: %.2f%%",
                                    sysFile.getDirName(), sysFile.getUsage(), alertProperties.getDiskThreshold())
                    ));
                }
            }

            // 发送告警
            if (!alerts.isEmpty()) {
                sendAlerts(alerts);
            }

        } catch (Exception e) {
            log.error("系统监控告警检查失败", e);
        }
    }

    private void sendAlerts(List<AlertMessage> alerts) {
        for (String notificationType : alertProperties.getNotificationTypes()) {
            switch (notificationType) {
                case "email":
                    sendEmailAlert(alerts);
                    break;
                case "webhook":
                    sendWebhookAlert(alerts);
                    break;
                default:
                    log.warn("未知的告警通知方式: {}", notificationType);
            }
        }
    }

    private void sendEmailAlert(List<AlertMessage> alerts) {
        try {
            String content = buildAlertContent(alerts);
            emailService.sendAlert(
                alertProperties.getEmail().getRecipients(),
                alertProperties.getEmail().getSubject(),
                content
            );
        } catch (Exception e) {
            log.error("发送邮件告警失败", e);
        }
    }

    private void sendWebhookAlert(List<AlertMessage> alerts) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("timestamp", System.currentTimeMillis());
            payload.put("alerts", alerts);

            webhookService.sendAlert(
                alertProperties.getWebhook().getUrl(),
                alertProperties.getWebhook().getHeaders(),
                payload
            );
        } catch (Exception e) {
            log.error("发送Webhook告警失败", e);
        }
    }

    private String buildAlertContent(List<AlertMessage> alerts) {
        StringBuilder content = new StringBuilder();
        content.append("Mars Admin 系统告警通知\n\n");
        content.append("告警时间: ").append(DateUtils.getTime()).append("\n\n");

        for (AlertMessage alert : alerts) {
            content.append("告警级别: ").append(alert.getLevel()).append("\n");
            content.append("告警标题: ").append(alert.getTitle()).append("\n");
            content.append("告警内容: ").append(alert.getMessage()).append("\n");
            content.append("---\n");
        }

        return content.toString();
    }
}
```

## 性能分析

### 1. 性能指标收集

```java
@Component
public class PerformanceCollector {

    private final MeterRegistry meterRegistry;
    private final Timer.Sample sample;

    public PerformanceCollector(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.sample = Timer.start(meterRegistry);
    }

    @EventListener
    public void handleRequestStart(RequestStartEvent event) {
        Timer.Sample.start(meterRegistry)
            .tag("uri", event.getUri())
            .tag("method", event.getMethod());
    }

    @EventListener
    public void handleRequestEnd(RequestEndEvent event) {
        Timer.builder("http.request.duration")
            .tag("uri", event.getUri())
            .tag("method", event.getMethod())
            .tag("status", String.valueOf(event.getStatus()))
            .register(meterRegistry)
            .record(event.getDuration(), TimeUnit.MILLISECONDS);
    }

    @Scheduled(fixedRate = 30000)
    public void collectSystemMetrics() {
        // 收集JVM指标
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();

        Gauge.builder("jvm.memory.heap.used")
            .register(meterRegistry, heapUsage, MemoryUsage::getUsed);
        Gauge.builder("jvm.memory.heap.max")
            .register(meterRegistry, heapUsage, MemoryUsage::getMax);

        // 收集线程指标
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        Gauge.builder("jvm.threads.live")
            .register(meterRegistry, threadBean, ThreadMXBean::getThreadCount);
        Gauge.builder("jvm.threads.daemon")
            .register(meterRegistry, threadBean, ThreadMXBean::getDaemonThreadCount);
    }
}
```

## 常见问题

### Q: 如何优化监控性能？

**解决方案**：
1. 合理设置监控频率，避免过于频繁的数据收集
2. 使用异步方式收集和处理监控数据
3. 对历史数据进行定期清理和归档
4. 使用缓存减少重复计算

### Q: 如何处理监控数据存储？

**解决方案**：
```java
@Service
public class MonitorDataService {

    @Async
    public void saveMonitorData(MonitorData data) {
        // 异步保存监控数据
        monitorDataRepository.save(data);
    }

    @Scheduled(cron = "0 0 2 * * ?") // 每天凌晨2点执行
    public void cleanOldData() {
        // 清理30天前的监控数据
        Date cutoffDate = DateUtils.addDays(new Date(), -30);
        monitorDataRepository.deleteByCreateTimeBefore(cutoffDate);
    }
}
```

### Q: 如何实现分布式监控？

**解决方案**：
```java
@Component
public class DistributedMonitor {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void reportMetrics(String instanceId, Map<String, Object> metrics) {
        String key = "monitor:metrics:" + instanceId;
        redisTemplate.opsForHash().putAll(key, metrics);
        redisTemplate.expire(key, Duration.ofMinutes(5));
    }

    public Map<String, Map<String, Object>> getAllInstanceMetrics() {
        Set<String> keys = redisTemplate.keys("monitor:metrics:*");
        Map<String, Map<String, Object>> result = new HashMap<>();

        for (String key : keys) {
            String instanceId = key.substring("monitor:metrics:".length());
            Map<Object, Object> metrics = redisTemplate.opsForHash().entries(key);
            result.put(instanceId, (Map<String, Object>) metrics);
        }

        return result;
    }
}
```
```
