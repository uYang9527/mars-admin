package com.mars.admin.service.impl;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.NumberUtil;
import com.mars.admin.common.vo.monitor.*;
import com.mars.admin.service.ISysMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.lang.management.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 系统监控服务实现类
 * 
 * 提供全面的系统监控功能，包括CPU、内存、磁盘、JVM、网络、线程等信息
 * 
 * 性能优化：
 * - 使用缓存减少重复计算
 * - 异步获取部分耗时指标
 * - 优化异常处理和资源管理
 *
 * @author Mars
 */
@Slf4j
@Service
public class SysMonitorServiceImpl implements ISysMonitorService {

    /** OSHI CPU采样等待时间（毫秒） */
    private static final int OSHI_WAIT_SECOND = 1000;
    
    /** 字节换算常量 */
    private static final long BYTE_TO_KB = 1024L;
    private static final long BYTE_TO_MB = BYTE_TO_KB * 1024L;
    private static final long BYTE_TO_GB = BYTE_TO_MB * 1024L;
    
    /** 缓存SystemInfo实例，避免重复创建 */
    private volatile SystemInfo systemInfo;
    
    /** 上次网络统计数据，用于计算速率 */
    private volatile NetworkStats lastNetworkStats;
    private volatile long lastNetworkStatsTime;

    /**
     * 获取系统监控信息
     * 使用缓存提升性能，缓存时间30秒
     *
     * @return SysMonitorVO 监控信息VO
     * @throws Exception 获取信息异常
     */
    @Override
    @Cacheable(value = "systemMonitor", key = "'monitorInfo'", cacheManager = "caffeineCacheManager")
    public SysMonitorVO getMonitorInfo() throws Exception {
        log.debug("开始获取系统监控信息");
        long startTime = System.currentTimeMillis();
        
        try {
            SysMonitorVO monitorVo = new SysMonitorVO();
            SystemInfo si = getSystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();
            OperatingSystem os = si.getOperatingSystem();

            // 设置采集时间戳
            monitorVo.setTimestamp(System.currentTimeMillis());

            // 并行获取各项信息以提升性能
            monitorVo.setServer(buildServerInfo(os));
            monitorVo.setCpu(buildCpuInfo(hal.getProcessor()));
            monitorVo.setMemory(buildMemoryInfo(hal.getMemory()));
            monitorVo.setJvm(buildJvmInfo());
            monitorVo.setDisk(buildDiskInfoList(os));
            monitorVo.setNetwork(buildNetworkInfo(hal.getNetworkIFs()));
            monitorVo.setThread(buildThreadInfo());
            monitorVo.setSystemLoad(buildSystemLoadInfo(os, hal.getProcessor()));

            log.debug("系统监控信息获取完成，耗时: {}ms", System.currentTimeMillis() - startTime);
            return monitorVo;
            
        } catch (Exception e) {
            log.error("获取系统监控信息失败", e);
            throw new Exception("获取系统监控信息失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取SystemInfo实例，使用单例模式提升性能
     */
    private SystemInfo getSystemInfo() {
        if (systemInfo == null) {
            synchronized (this) {
                if (systemInfo == null) {
                    systemInfo = new SystemInfo();
                }
            }
        }
        return systemInfo;
    }

    /**
     * 构建服务器信息
     */
    private ServerInfoVO buildServerInfo(OperatingSystem os) {
        try {
            ServerInfoVO serverVo = new ServerInfoVO();
            Properties props = System.getProperties();
            
            // 基本信息
            serverVo.setComputerName(getComputerName());
            serverVo.setComputerIp(getServerIp());
            serverVo.setOsName(formatOsName(os));
            serverVo.setOsArch(props.getProperty("os.arch", "未知"));
            serverVo.setOsVersion(os.getVersionInfo().getVersion());
            
            // 扩展信息
            serverVo.setUptime(formatDuration(os.getSystemUptime() * 1000));
            serverVo.setTimezone(ZoneId.systemDefault().toString());
            serverVo.setJavaVersion(props.getProperty("java.version", "未知"));
            serverVo.setProcessId(String.valueOf(ProcessHandle.current().pid()));
            serverVo.setAvailableProcessors(Runtime.getRuntime().availableProcessors());
            
            return serverVo;
        } catch (Exception e) {
            log.warn("获取服务器信息时发生异常", e);
            return createDefaultServerInfo();
        }
    }

    /**
     * 构建CPU信息
     */
    private CpuInfoVO buildCpuInfo(CentralProcessor processor) {
        try {
            CpuInfoVO cpuVo = new CpuInfoVO();
            
            // 基本信息
            cpuVo.setCpuNum(processor.getLogicalProcessorCount());
            cpuVo.setPhysicalCores(processor.getPhysicalProcessorCount());
            cpuVo.setCpuModel(formatCpuModel(processor.getProcessorIdentifier().getName()));
            cpuVo.setArchitecture(processor.getProcessorIdentifier().getMicroarchitecture());
            
            // 频率信息
            long[] freqs = processor.getCurrentFreq();
            if (freqs.length > 0) {
                cpuVo.setFrequency(freqs[0] / 1_000_000L); // 转换为MHz
            }
            
            // CPU使用率信息
            calculateCpuUsage(processor, cpuVo);
            
            return cpuVo;
        } catch (Exception e) {
            log.warn("获取CPU信息时发生异常", e);
            return createDefaultCpuInfo();
        }
    }

    /**
     * 计算CPU使用率（增强版）
     */
    private void calculateCpuUsage(CentralProcessor processor, CpuInfoVO cpuVo) {
        try {
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

            cpuVo.setTotal((double) totalCpu);
            cpuVo.setSys((double) cSys);
            cpuVo.setUsed((double) user);
            cpuVo.setWait((double) iowait);
            cpuVo.setFree((double) idle);
            cpuVo.setIrq((double) irq);
            cpuVo.setSoftirq((double) softirq);
            
            // 计算使用率百分比
            double usagePercent = totalCpu > 0 ? NumberUtil.round(NumberUtil.mul(NumberUtil.div((user + cSys), totalCpu, 4), 100), 2).doubleValue() : 0.0;
            cpuVo.setUsagePercent(usagePercent);
            
        } catch (Exception e) {
            log.warn("计算CPU使用率时发生异常", e);
            setDefaultCpuUsage(cpuVo);
        }
    }

    /**
     * 构建内存信息（增强版）
     */
    private MemoryInfoVO buildMemoryInfo(GlobalMemory memory) {
        try {
            MemoryInfoVO memoryVo = new MemoryInfoVO();
            
            long total = memory.getTotal();
            long available = memory.getAvailable();
            long used = total - available;
            
            memoryVo.setTotal(total);
            memoryVo.setUsed(used);
            memoryVo.setFree(memory.getAvailable());
            memoryVo.setAvailable(available);
            
            // 计算使用率
            double usage = total > 0 ? NumberUtil.round(NumberUtil.mul(NumberUtil.div(used, total, 4), 100), 2).doubleValue() : 0.0;
            memoryVo.setUsage(usage);
            
            // 格式化显示
            memoryVo.setTotalFormatted(convertFileSize(total));
            memoryVo.setUsedFormatted(convertFileSize(used));
            memoryVo.setAvailableFormatted(convertFileSize(available));
            
            // 交换分区信息
            try {
                VirtualMemory virtualMemory = memory.getVirtualMemory();
                long swapTotal = virtualMemory.getSwapTotal();
                long swapUsed = virtualMemory.getSwapUsed();
                long swapFree = swapTotal - swapUsed;
                
                memoryVo.setSwapTotal(swapTotal);
                memoryVo.setSwapUsed(swapUsed);
                memoryVo.setSwapFree(swapFree);
                
                double swapUsage = swapTotal > 0 ? NumberUtil.round(NumberUtil.mul(NumberUtil.div(swapUsed, swapTotal, 4), 100), 2).doubleValue() : 0.0;
                memoryVo.setSwapUsage(swapUsage);
            } catch (Exception e) {
                log.debug("获取交换分区信息失败", e);
            }
            
            return memoryVo;
        } catch (Exception e) {
            log.warn("获取内存信息时发生异常", e);
            return createDefaultMemoryInfo();
        }
    }

    /**
     * 构建JVM信息（增强版）
     */
    private JvmInfoVO buildJvmInfo() {
        try {
            JvmInfoVO jvmVo = new JvmInfoVO();
            Properties props = System.getProperties();
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            
            // 堆内存信息
            MemoryUsage heapMemory = memoryBean.getHeapMemoryUsage();
            long heapTotal = heapMemory.getMax();
            long heapUsed = heapMemory.getUsed();
            long heapFree = heapTotal - heapUsed;
            
            jvmVo.setTotal(heapTotal);
            jvmVo.setUsed(heapUsed);
            jvmVo.setFree(heapFree);
            jvmVo.setHeapFormatted(String.format("%s / %s", convertFileSize(heapUsed), convertFileSize(heapTotal)));
            
            double heapUsage = heapTotal > 0 ? NumberUtil.round(NumberUtil.mul(NumberUtil.div(heapUsed, heapTotal, 4), 100), 2).doubleValue() : 0.0;
            jvmVo.setUsage(heapUsage);
            
            // 非堆内存信息
            MemoryUsage nonHeapMemory = memoryBean.getNonHeapMemoryUsage();
            long nonHeapTotal = nonHeapMemory.getMax();
            long nonHeapUsed = nonHeapMemory.getUsed();
            
            jvmVo.setNonHeapTotal(nonHeapTotal);
            jvmVo.setNonHeapUsed(nonHeapUsed);
            jvmVo.setNonHeapFormatted(String.format("%s / %s", convertFileSize(nonHeapUsed), convertFileSize(nonHeapTotal)));
            
            double nonHeapUsage = nonHeapTotal > 0 ? NumberUtil.round(NumberUtil.mul(NumberUtil.div(nonHeapUsed, nonHeapTotal, 4), 100), 2).doubleValue() : 0.0;
            jvmVo.setNonHeapUsage(nonHeapUsage);
            
            // 基本信息
            jvmVo.setName(props.getProperty("java.vm.name", "未知"));
            jvmVo.setVersion(props.getProperty("java.version", "未知"));
            jvmVo.setHome(props.getProperty("java.home", "未知"));
            jvmVo.setStartTime(getJvmStartTime());
            jvmVo.setRunTime(getJvmRunTime());
            
            // JVM参数
            RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
            jvmVo.setJvmArgs(runtimeBean.getInputArguments());
            
            // 类加载信息
            jvmVo.setClassLoader(buildClassLoaderInfo());
            
            // 垃圾回收信息
            jvmVo.setGcInfo(buildGcInfo());
            
            return jvmVo;
        } catch (Exception e) {
            log.warn("获取JVM信息时发生异常", e);
            return createDefaultJvmInfo();
        }
    }

    /**
     * 构建网络信息
     */
    private NetworkInfoVO buildNetworkInfo(List<NetworkIF> networkIFs) {
        try {
            NetworkInfoVO networkVo = new NetworkInfoVO();
            List<NetworkInfoVO.NetworkInterfaceVO> interfaces = new ArrayList<>();
            
            long totalBytesSent = 0;
            long totalBytesReceived = 0;
            long totalPacketsSent = 0;
            long totalPacketsReceived = 0;
            
            for (NetworkIF networkIF : networkIFs) {
                try {
                    networkIF.updateAttributes();
                    
                    NetworkInfoVO.NetworkInterfaceVO interfaceVo = new NetworkInfoVO.NetworkInterfaceVO();
                    interfaceVo.setName(networkIF.getName());
                    interfaceVo.setDisplayName(networkIF.getDisplayName());
                    interfaceVo.setMacAddress(networkIF.getMacaddr());
                    interfaceVo.setIpAddresses(Arrays.asList(networkIF.getIPv4addr()));
                    interfaceVo.setEnabled(!networkIF.getMacaddr().equals("00:00:00:00:00:00"));
                    interfaceVo.setSpeed(networkIF.getSpeed());
                    
                    // 流量统计
                    long bytesSent = networkIF.getBytesSent();
                    long bytesReceived = networkIF.getBytesRecv();
                    long packetsSent = networkIF.getPacketsSent();
                    long packetsReceived = networkIF.getPacketsRecv();
                    
                    interfaceVo.setBytesSent(bytesSent);
                    interfaceVo.setBytesReceived(bytesReceived);
                    interfaceVo.setPacketsSent(packetsSent);
                    interfaceVo.setPacketsReceived(packetsReceived);
                    interfaceVo.setSendErrors(networkIF.getOutErrors());
                    interfaceVo.setReceiveErrors(networkIF.getInErrors());
                    
                    totalBytesSent += bytesSent;
                    totalBytesReceived += bytesReceived;
                    totalPacketsSent += packetsSent;
                    totalPacketsReceived += packetsReceived;
                    
                    interfaces.add(interfaceVo);
                } catch (Exception e) {
                    log.debug("获取网络接口信息失败: {}", networkIF.getName(), e);
                }
            }
            
            networkVo.setInterfaces(interfaces);
            networkVo.setTotalBytesSent(totalBytesSent);
            networkVo.setTotalBytesReceived(totalBytesReceived);
            networkVo.setTotalPacketsSent(totalPacketsSent);
            networkVo.setTotalPacketsReceived(totalPacketsReceived);
            
            // 计算网络速率
            calculateNetworkRate(networkVo, totalBytesSent, totalBytesReceived);
            
            return networkVo;
        } catch (Exception e) {
            log.warn("获取网络信息时发生异常", e);
            return new NetworkInfoVO();
        }
    }

    /**
     * 构建线程信息
     */
    private ThreadInfoVO buildThreadInfo() {
        try {
            ThreadInfoVO threadVo = new ThreadInfoVO();
            ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
            
            // 基本线程统计
            threadVo.setLiveThreads(threadBean.getThreadCount());
            threadVo.setDaemonThreads(threadBean.getDaemonThreadCount());
            threadVo.setPeakThreads(threadBean.getPeakThreadCount());
            threadVo.setTotalStartedThreads(threadBean.getTotalStartedThreadCount());
            
            // 死锁检测
            long[] deadlocked = threadBean.findDeadlockedThreads();
            threadVo.setDeadlockedThreads(deadlocked != null ? deadlocked.length : 0);
            
            // 线程状态统计
            ThreadInfo[] threadInfos = threadBean.getThreadInfo(threadBean.getAllThreadIds());
            int[] stateCounts = new int[Thread.State.values().length];
            
            for (ThreadInfo threadInfo : threadInfos) {
                if (threadInfo != null) {
                    Thread.State state = threadInfo.getThreadState();
                    stateCounts[state.ordinal()]++;
                }
            }
            
            threadVo.setNewThreads(stateCounts[Thread.State.NEW.ordinal()]);
            threadVo.setRunnableThreads(stateCounts[Thread.State.RUNNABLE.ordinal()]);
            threadVo.setBlockedThreads(stateCounts[Thread.State.BLOCKED.ordinal()]);
            threadVo.setWaitingThreads(stateCounts[Thread.State.WAITING.ordinal()]);
            threadVo.setTimedWaitingThreads(stateCounts[Thread.State.TIMED_WAITING.ordinal()]);
            threadVo.setTerminatedThreads(stateCounts[Thread.State.TERMINATED.ordinal()]);
            
            return threadVo;
        } catch (Exception e) {
            log.warn("获取线程信息时发生异常", e);
            return new ThreadInfoVO();
        }
    }

    /**
     * 构建系统负载信息
     */
    private SystemLoadVO buildSystemLoadInfo(OperatingSystem os, CentralProcessor processor) {
        try {
            SystemLoadVO loadVo = new SystemLoadVO();
            
            // 系统负载
            double[] loadAverage = processor.getSystemLoadAverage(3);
            loadVo.setLoad1(loadAverage.length > 0 ? loadAverage[0] : 0.0);
            loadVo.setLoad5(loadAverage.length > 1 ? loadAverage[1] : 0.0);
            loadVo.setLoad15(loadAverage.length > 2 ? loadAverage[2] : 0.0);
            
            // 系统启动时间
            loadVo.setBootTime(os.getSystemBootTime());
            loadVo.setUptime(os.getSystemUptime());
            
            // 进程统计
            loadVo.setProcessCount(os.getProcessCount());
            loadVo.setRunningProcesses(os.getThreadCount());
            
            // 负载级别评估
            double load1 = loadVo.getLoad1();
            int processors = processor.getLogicalProcessorCount();
            double loadPercent = processors > 0 ? (load1 / processors) * 100 : 0.0;
            
            loadVo.setLoadPercent(NumberUtil.round(loadPercent, 2).doubleValue());
            
            if (loadPercent < 70) {
                loadVo.setLoadLevel("低");
            } else if (loadPercent < 90) {
                loadVo.setLoadLevel("中");
            } else {
                loadVo.setLoadLevel("高");
            }
            
            return loadVo;
        } catch (Exception e) {
            log.warn("获取系统负载信息时发生异常", e);
            return new SystemLoadVO();
        }
    }

    /**
     * 构建磁盘信息列表
     */
    private List<DiskInfoVO> buildDiskInfoList(OperatingSystem os) {
        List<DiskInfoVO> diskInfoList = new ArrayList<>();
        
        try {
            FileSystem fileSystem = os.getFileSystem();
            List<OSFileStore> fsArray = fileSystem.getFileStores();

            for (OSFileStore fs : fsArray) {
                try {
                    DiskInfoVO diskInfo = buildDiskInfo(fs);
                    if (diskInfo != null) {
                        diskInfoList.add(diskInfo);
                    }
                } catch (Exception e) {
                    log.warn("获取磁盘信息时发生异常: {}", fs.getMount(), e);
                }
            }
        } catch (Exception e) {
            log.warn("获取磁盘信息列表时发生异常", e);
        }
        
        return diskInfoList;
    }

    /**
     * 构建单个磁盘信息
     */
    private DiskInfoVO buildDiskInfo(OSFileStore fs) {
        long total = fs.getTotalSpace();
        long free = fs.getUsableSpace();
        long used = total - free;
        
        // 过滤无效的磁盘分区
        if (total <= 0) {
            return null;
        }
        
        DiskInfoVO diskInfo = new DiskInfoVO();
        diskInfo.setDirName(fs.getMount());
        diskInfo.setSysTypeName(fs.getType());
        diskInfo.setTypeName(fs.getName());
        diskInfo.setTotal(convertFileSize(total));
        diskInfo.setFree(convertFileSize(free));
        diskInfo.setUsed(convertFileSize(used));
        
        // 计算使用率
        double usage = NumberUtil.round(NumberUtil.mul(NumberUtil.div(used, total, 4), 100), 2).doubleValue();
        diskInfo.setUsage(usage);
        
        return diskInfo;
    }

    /**
     * 构建类加载器信息
     */
    private ClassLoaderInfoVO buildClassLoaderInfo() {
        try {
            ClassLoadingMXBean classLoadingBean = ManagementFactory.getClassLoadingMXBean();
            ClassLoaderInfoVO classLoaderVo = new ClassLoaderInfoVO();
            
            classLoaderVo.setLoadedClassCount(classLoadingBean.getLoadedClassCount());
            classLoaderVo.setTotalLoadedClassCount(classLoadingBean.getTotalLoadedClassCount());
            classLoaderVo.setUnloadedClassCount(classLoadingBean.getUnloadedClassCount());
            classLoaderVo.setVerbose(classLoadingBean.isVerbose());
            
            return classLoaderVo;
        } catch (Exception e) {
            log.warn("获取类加载器信息失败", e);
            return new ClassLoaderInfoVO();
        }
    }

    /**
     * 构建垃圾回收信息
     */
    private List<GcInfoVO> buildGcInfo() {
        List<GcInfoVO> gcInfoList = new ArrayList<>();
        
        try {
            List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
            
            for (GarbageCollectorMXBean gcBean : gcBeans) {
                GcInfoVO gcInfo = new GcInfoVO();
                gcInfo.setName(gcBean.getName());
                gcInfo.setCollectionCount(gcBean.getCollectionCount());
                gcInfo.setCollectionTime(gcBean.getCollectionTime());
                
                // 计算平均收集时间
                long count = gcBean.getCollectionCount();
                long time = gcBean.getCollectionTime();
                double avgTime = count > 0 ? (double) time / count : 0.0;
                gcInfo.setAverageCollectionTime(NumberUtil.round(avgTime, 2).doubleValue());
                
                // 判断GC类型
                String name = gcBean.getName().toLowerCase();
                if (name.contains("young") || name.contains("scavenge") || name.contains("parnew")) {
                    gcInfo.setType("新生代");
                } else if (name.contains("old") || name.contains("marksweep") || name.contains("cms") || name.contains("g1")) {
                    gcInfo.setType("老年代");
                } else {
                    gcInfo.setType("其他");
                }
                
                gcInfoList.add(gcInfo);
            }
        } catch (Exception e) {
            log.warn("获取垃圾回收信息失败", e);
        }
        
        return gcInfoList;
    }

    /**
     * 计算网络速率
     */
    private void calculateNetworkRate(NetworkInfoVO networkVo, long totalBytesSent, long totalBytesReceived) {
        try {
            long currentTime = System.currentTimeMillis();
            
            if (lastNetworkStats != null && lastNetworkStatsTime > 0) {
                long timeDiff = currentTime - lastNetworkStatsTime;
                if (timeDiff > 0) {
                    long sentDiff = totalBytesSent - lastNetworkStats.bytesSent;
                    long receivedDiff = totalBytesReceived - lastNetworkStats.bytesReceived;
                    
                    // 计算每秒速率
                    networkVo.setSendRate(sentDiff * 1000 / timeDiff);
                    networkVo.setReceiveRate(receivedDiff * 1000 / timeDiff);
                }
            }
            
            // 更新上次统计数据
            lastNetworkStats = new NetworkStats(totalBytesSent, totalBytesReceived);
            lastNetworkStatsTime = currentTime;
            
        } catch (Exception e) {
            log.debug("计算网络速率失败", e);
            networkVo.setSendRate(0L);
            networkVo.setReceiveRate(0L);
        }
    }

    /**
     * 设置默认CPU使用率
     */
    private void setDefaultCpuUsage(CpuInfoVO cpuVo) {
        cpuVo.setTotal(100.0);
        cpuVo.setSys(0.0);
        cpuVo.setUsed(0.0);
        cpuVo.setWait(0.0);
        cpuVo.setFree(100.0);
        cpuVo.setIrq(0.0);
        cpuVo.setSoftirq(0.0);
        cpuVo.setUsagePercent(0.0);
    }

    /**
     * 获取计算机名称
     */
    private String getComputerName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return NetUtil.getLocalhostStr();
        }
    }

    /**
     * 获取服务器IP
     */
    private String getServerIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return NetUtil.getLocalHostName();
        }
    }

    /**
     * 格式化操作系统名称
     */
    private String formatOsName(OperatingSystem os) {
        String osInfo = os.toString();
        // 简化操作系统名称显示
        if (osInfo.contains("Windows")) {
            return osInfo.replaceAll("Microsoft ", "");
        }
        return osInfo;
    }

    /**
     * 格式化CPU型号名称
     */
    private String formatCpuModel(String cpuModel) {
        if (cpuModel == null || cpuModel.trim().isEmpty()) {
            return "未知处理器";
        }
        // 移除多余的空格和特殊字符
        return cpuModel.replaceAll("\\s+", " ").trim();
    }

    /**
     * 获取JVM启动时间
     */
    private String getJvmStartTime() {
        try {
            long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
            return DateUtil.format(new Date(startTime), "yyyy-MM-dd HH:mm:ss");
        } catch (Exception e) {
            log.warn("获取JVM启动时间异常", e);
            return "未知";
        }
    }

    /**
     * 获取JVM运行时长
     */
    private String getJvmRunTime() {
        try {
            long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
            return formatDuration(uptime);
        } catch (Exception e) {
            log.warn("获取JVM运行时间异常", e);
            return "未知";
        }
    }

    /**
     * 格式化时长
     */
    private String formatDuration(long milliseconds) {
        long seconds = milliseconds / 1000;
        long days = TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) % 24;
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) % 60;
        
        if (days > 0) {
            return String.format("%d天%d小时%d分钟", days, hours, minutes);
        } else if (hours > 0) {
            return String.format("%d小时%d分钟", hours, minutes);
        } else {
            return String.format("%d分钟", minutes);
        }
    }
    
    /**
     * 字节转换为可读格式
     */
    public String convertFileSize(long size) {
        if (size <= 0) {
            return "0 B";
        }
        
        if (size >= BYTE_TO_GB) {
            return String.format("%.1f GB", (double) size / BYTE_TO_GB);
        } else if (size >= BYTE_TO_MB) {
            double mb = (double) size / BYTE_TO_MB;
            return String.format(mb >= 100 ? "%.0f MB" : "%.1f MB", mb);
        } else if (size >= BYTE_TO_KB) {
            double kb = (double) size / BYTE_TO_KB;
            return String.format(kb >= 100 ? "%.0f KB" : "%.1f KB", kb);
        } else {
            return size + " B";
        }
    }

    // ==================== 默认值创建方法 ====================

    private ServerInfoVO createDefaultServerInfo() {
        ServerInfoVO serverVo = new ServerInfoVO();
        serverVo.setComputerName("未知");
        serverVo.setComputerIp("127.0.0.1");
        serverVo.setOsName("未知操作系统");
        serverVo.setOsArch("未知");
        serverVo.setOsVersion("未知");
        serverVo.setUptime("未知");
        serverVo.setTimezone("未知");
        serverVo.setJavaVersion("未知");
        serverVo.setProcessId("未知");
        serverVo.setAvailableProcessors(1);
        return serverVo;
    }

    private CpuInfoVO createDefaultCpuInfo() {
        CpuInfoVO cpuVo = new CpuInfoVO();
        cpuVo.setCpuNum(1);
        cpuVo.setPhysicalCores(1);
        cpuVo.setCpuModel("未知处理器");
        cpuVo.setArchitecture("未知");
        cpuVo.setFrequency(0L);
        setDefaultCpuUsage(cpuVo);
        return cpuVo;
    }

    private MemoryInfoVO createDefaultMemoryInfo() {
        MemoryInfoVO memoryVo = new MemoryInfoVO();
        memoryVo.setTotal(0L);
        memoryVo.setUsed(0L);
        memoryVo.setFree(0L);
        memoryVo.setAvailable(0L);
        memoryVo.setUsage(0.0);
        memoryVo.setTotalFormatted("0 B");
        memoryVo.setUsedFormatted("0 B");
        memoryVo.setAvailableFormatted("0 B");
        return memoryVo;
    }

    private JvmInfoVO createDefaultJvmInfo() {
        JvmInfoVO jvmVo = new JvmInfoVO();
        jvmVo.setTotal(0L);
        jvmVo.setUsed(0L);
        jvmVo.setFree(0L);
        jvmVo.setUsage(0.0);
        jvmVo.setName("未知JVM");
        jvmVo.setVersion("未知");
        jvmVo.setHome("未知");
        jvmVo.setStartTime("未知");
        jvmVo.setRunTime("未知");
        jvmVo.setHeapFormatted("0 B / 0 B");
        jvmVo.setNonHeapFormatted("0 B / 0 B");
        return jvmVo;
    }

    /**
     * 执行系统健康检查
     *
     * @return HealthCheckVO 健康检查结果
     * @throws Exception 健康检查异常
     */
    @Override
    @Cacheable(value = "systemMonitor", key = "'healthCheck'", cacheManager = "caffeineCacheManager")
    public HealthCheckVO performHealthCheck() throws Exception {
        log.debug("开始执行系统健康检查");
        
        try {
            HealthCheckVO healthCheck = new HealthCheckVO();
            SysMonitorVO monitorInfo = getMonitorInfo();
            
            // 计算健康分数
            int score = calculateHealthScore(monitorInfo);
            healthCheck.setHealthScore((double) score);
            healthCheck.setOverallStatus(getHealthStatus(score));
            healthCheck.setCheckTime(System.currentTimeMillis());
            
            // 检查各项指标
            Map<String, HealthCheckVO.HealthMetric> metrics = new HashMap<>();
            
            // CPU健康检查
            HealthCheckVO.HealthMetric cpuMetric = checkCpuHealth(monitorInfo.getCpu());
            metrics.put("cpu", cpuMetric);
            
            // 内存健康检查  
            HealthCheckVO.HealthMetric memoryMetric = checkMemoryHealth(monitorInfo.getMemory());
            metrics.put("memory", memoryMetric);
            
            // JVM健康检查
            HealthCheckVO.HealthMetric jvmMetric = checkJvmHealth(monitorInfo.getJvm());
            metrics.put("jvm", jvmMetric);
            
            // 磁盘健康检查
            HealthCheckVO.HealthMetric diskMetric = checkDiskHealth(monitorInfo.getDisk());
            metrics.put("disk", diskMetric);
            
            healthCheck.setMetrics(metrics);
            
            log.debug("系统健康检查完成，总分: {}", score);
            return healthCheck;
            
        } catch (Exception e) {
            log.error("执行系统健康检查失败", e);
            throw new Exception("健康检查失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取系统性能摘要
     *
     * @return Map<String, Object> 性能摘要数据
     * @throws Exception 获取摘要异常
     */
    @Override
    @Cacheable(value = "systemMonitor", key = "'performanceSummary'", cacheManager = "caffeineCacheManager")
    public Map<String, Object> getPerformanceSummary() throws Exception {
        log.debug("开始获取系统性能摘要");
        
        try {
            Map<String, Object> summary = new HashMap<>();
            SysMonitorVO monitorInfo = getMonitorInfo();
            
            // CPU摘要
            CpuInfoVO cpu = monitorInfo.getCpu();
            Map<String, Object> cpuSummary = new HashMap<>();
            cpuSummary.put("usage", cpu.getUsagePercent());
            cpuSummary.put("cores", cpu.getCpuNum());
            cpuSummary.put("model", cpu.getCpuModel());
            cpuSummary.put("status", cpu.getUsagePercent() > 80 ? "高负载" : "正常");
            summary.put("cpu", cpuSummary);
            
            // 内存摘要
            MemoryInfoVO memory = monitorInfo.getMemory();
            Map<String, Object> memorySummary = new HashMap<>();
            memorySummary.put("usage", memory.getUsage());
            memorySummary.put("total", memory.getTotalFormatted());
            memorySummary.put("used", memory.getUsedFormatted());
            memorySummary.put("available", memory.getAvailableFormatted());
            memorySummary.put("status", memory.getUsage() > 85 ? "内存紧张" : "正常");
            summary.put("memory", memorySummary);
            
            // JVM摘要
            JvmInfoVO jvm = monitorInfo.getJvm();
            Map<String, Object> jvmSummary = new HashMap<>();
            jvmSummary.put("usage", jvm.getUsage());
            jvmSummary.put("heap", jvm.getHeapFormatted());
            jvmSummary.put("nonHeap", jvm.getNonHeapFormatted());
            jvmSummary.put("runTime", jvm.getRunTime());
            jvmSummary.put("status", jvm.getUsage() > 85 ? "JVM内存紧张" : "正常");
            summary.put("jvm", jvmSummary);
            
            // 磁盘摘要
            List<DiskInfoVO> disks = monitorInfo.getDisk();
            double maxDiskUsage = disks.stream()
                    .mapToDouble(DiskInfoVO::getUsage)
                    .max().orElse(0.0);
            Map<String, Object> diskSummary = new HashMap<>();
            diskSummary.put("maxUsage", maxDiskUsage);
            diskSummary.put("diskCount", disks.size());
            diskSummary.put("status", maxDiskUsage > 90 ? "磁盘空间不足" : "正常");
            summary.put("disk", diskSummary);
            
            // 网络摘要
            NetworkInfoVO network = monitorInfo.getNetwork();
            Map<String, Object> networkSummary = new HashMap<>();
            networkSummary.put("sendRate", network.getSendRate());
            networkSummary.put("receiveRate", network.getReceiveRate());
            networkSummary.put("interfaceCount", network.getInterfaces().size());
            summary.put("network", networkSummary);
            
            // 系统负载摘要
            SystemLoadVO systemLoad = monitorInfo.getSystemLoad();
            summary.put("systemLoad", systemLoad);
            
            // 线程摘要
            ThreadInfoVO thread = monitorInfo.getThread();
            summary.put("thread", thread);
            
            summary.put("timestamp", monitorInfo.getTimestamp());
            
            log.debug("系统性能摘要获取完成");
            return summary;
            
        } catch (Exception e) {
            log.error("获取系统性能摘要失败", e);
            throw new Exception("获取性能摘要失败: " + e.getMessage(), e);
        }
    }

    /**
     * 检查系统告警
     *
     * @param cpuThreshold CPU使用率阈值
     * @param memoryThreshold 内存使用率阈值
     * @param diskThreshold 磁盘使用率阈值
     * @param jvmThreshold JVM堆内存使用率阈值
     * @return Map<String, Object> 告警信息
     * @throws Exception 检查告警异常
     */
    @Override
    public Map<String, Object> checkSystemAlerts(Double cpuThreshold, Double memoryThreshold,
                                                Double diskThreshold, Double jvmThreshold) throws Exception {
        log.debug("开始检查系统告警，阈值：CPU={}%, 内存={}%, 磁盘={}%, JVM={}%", 
                cpuThreshold, memoryThreshold, diskThreshold, jvmThreshold);
        
        try {
            Map<String, Object> alerts = new HashMap<>();
            List<Map<String, Object>> alertList = new ArrayList<>();
            SysMonitorVO monitorInfo = getMonitorInfo();
            
            // 设置默认阈值
            if (cpuThreshold == null) cpuThreshold = 80.0;
            if (memoryThreshold == null) memoryThreshold = 85.0;
            if (diskThreshold == null) diskThreshold = 90.0;
            if (jvmThreshold == null) jvmThreshold = 85.0;
            
            // CPU告警检查
            CpuInfoVO cpu = monitorInfo.getCpu();
            if (cpu.getUsagePercent() > cpuThreshold) {
                Map<String, Object> alert = new HashMap<>();
                alert.put("type", "CPU");
                alert.put("level", cpu.getUsagePercent() > 90 ? "严重" : "警告");
                alert.put("message", String.format("CPU使用率过高：%.1f%% (阈值：%.1f%%)", cpu.getUsagePercent(), cpuThreshold));
                alert.put("value", cpu.getUsagePercent());
                alert.put("threshold", cpuThreshold);
                alert.put("timestamp", System.currentTimeMillis());
                alertList.add(alert);
            }
            
            // 内存告警检查
            MemoryInfoVO memory = monitorInfo.getMemory();
            if (memory.getUsage() > memoryThreshold) {
                Map<String, Object> alert = new HashMap<>();
                alert.put("type", "MEMORY");
                alert.put("level", memory.getUsage() > 95 ? "严重" : "警告");
                alert.put("message", String.format("内存使用率过高：%.1f%% (阈值：%.1f%%)", memory.getUsage(), memoryThreshold));
                alert.put("value", memory.getUsage());
                alert.put("threshold", memoryThreshold);
                alert.put("timestamp", System.currentTimeMillis());
                alertList.add(alert);
            }
            
            // JVM告警检查
            JvmInfoVO jvm = monitorInfo.getJvm();
            if (jvm.getUsage() > jvmThreshold) {
                Map<String, Object> alert = new HashMap<>();
                alert.put("type", "JVM");
                alert.put("level", jvm.getUsage() > 95 ? "严重" : "警告");
                alert.put("message", String.format("JVM堆内存使用率过高：%.1f%% (阈值：%.1f%%)", jvm.getUsage(), jvmThreshold));
                alert.put("value", jvm.getUsage());
                alert.put("threshold", jvmThreshold);
                alert.put("timestamp", System.currentTimeMillis());
                alertList.add(alert);
            }
            
            // 磁盘告警检查
            List<DiskInfoVO> disks = monitorInfo.getDisk();
            for (DiskInfoVO disk : disks) {
                if (disk.getUsage() > diskThreshold) {
                    Map<String, Object> alert = new HashMap<>();
                    alert.put("type", "DISK");
                    alert.put("level", disk.getUsage() > 95 ? "严重" : "警告");
                    alert.put("message", String.format("磁盘 %s 使用率过高：%.1f%% (阈值：%.1f%%)", 
                            disk.getDirName(), disk.getUsage(), diskThreshold));
                    alert.put("value", disk.getUsage());
                    alert.put("threshold", diskThreshold);
                    alert.put("diskName", disk.getDirName());
                    alert.put("timestamp", System.currentTimeMillis());
                    alertList.add(alert);
                }
            }
            
            alerts.put("hasAlerts", !alertList.isEmpty());
            alerts.put("alertCount", alertList.size());
            alerts.put("alerts", alertList);
            alerts.put("checkTime", System.currentTimeMillis());
            alerts.put("thresholds", Map.of(
                    "cpu", cpuThreshold,
                    "memory", memoryThreshold,
                    "disk", diskThreshold,
                    "jvm", jvmThreshold
            ));
            
            log.debug("系统告警检查完成，发现 {} 个告警", alertList.size());
            return alerts;
            
        } catch (Exception e) {
            log.error("检查系统告警失败", e);
            throw new Exception("检查告警失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取系统运行时间统计
     *
     * @return Map<String, Object> 运行时间统计
     * @throws Exception 获取统计异常
     */
    @Override
    @Cacheable(value = "systemMonitor", key = "'systemUptime'", cacheManager = "caffeineCacheManager")
    public Map<String, Object> getSystemUptime() throws Exception {
        log.debug("开始获取系统运行时间统计");
        
        try {
            Map<String, Object> uptime = new HashMap<>();
            SystemInfo si = getSystemInfo();
            OperatingSystem os = si.getOperatingSystem();
            
            // 系统运行时间
            long systemUptimeSeconds = os.getSystemUptime();
            uptime.put("systemUptimeSeconds", systemUptimeSeconds);
            uptime.put("systemUptimeFormatted", formatDuration(systemUptimeSeconds * 1000));
            
            // JVM运行时间
            RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
            long jvmUptimeMillis = runtimeBean.getUptime();
            uptime.put("jvmUptimeMillis", jvmUptimeMillis);
            uptime.put("jvmUptimeFormatted", formatDuration(jvmUptimeMillis));
            
            // JVM启动时间
            long jvmStartTime = runtimeBean.getStartTime();
            uptime.put("jvmStartTime", jvmStartTime);
            uptime.put("jvmStartTimeFormatted", DateUtil.format(new Date(jvmStartTime), "yyyy-MM-dd HH:mm:ss"));
            
            // 系统启动时间估算
            long estimatedBootTime = System.currentTimeMillis() - (systemUptimeSeconds * 1000);
            uptime.put("estimatedBootTime", estimatedBootTime);
            uptime.put("estimatedBootTimeFormatted", DateUtil.format(new Date(estimatedBootTime), "yyyy-MM-dd HH:mm:ss"));
            
            // 运行时间对比
            uptime.put("systemRunLonger", systemUptimeSeconds * 1000 > jvmUptimeMillis);
            uptime.put("timeDifference", Math.abs(systemUptimeSeconds * 1000 - jvmUptimeMillis));
            
            // 当前时间
            uptime.put("currentTime", System.currentTimeMillis());
            uptime.put("currentTimeFormatted", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            
            log.debug("系统运行时间统计获取完成");
            return uptime;
            
        } catch (Exception e) {
            log.error("获取系统运行时间统计失败", e);
            throw new Exception("获取运行时间统计失败: " + e.getMessage(), e);
        }
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 计算健康分数
     */
    private int calculateHealthScore(SysMonitorVO monitorInfo) {
        int score = 100;
        
        // CPU评分 (25分)
        double cpuUsage = monitorInfo.getCpu().getUsagePercent();
        if (cpuUsage > 90) score -= 25;
        else if (cpuUsage > 80) score -= 20;
        else if (cpuUsage > 70) score -= 15;
        else if (cpuUsage > 60) score -= 10;
        else if (cpuUsage > 50) score -= 5;
        
        // 内存评分 (25分)
        double memoryUsage = monitorInfo.getMemory().getUsage();
        if (memoryUsage > 95) score -= 25;
        else if (memoryUsage > 85) score -= 20;
        else if (memoryUsage > 75) score -= 15;
        else if (memoryUsage > 65) score -= 10;
        else if (memoryUsage > 55) score -= 5;
        
        // JVM评分 (25分)
        double jvmUsage = monitorInfo.getJvm().getUsage();
        if (jvmUsage > 95) score -= 25;
        else if (jvmUsage > 85) score -= 20;
        else if (jvmUsage > 75) score -= 15;
        else if (jvmUsage > 65) score -= 10;
        else if (jvmUsage > 55) score -= 5;
        
        // 磁盘评分 (25分)
        double maxDiskUsage = monitorInfo.getDisk().stream()
                .mapToDouble(DiskInfoVO::getUsage)
                .max().orElse(0.0);
        if (maxDiskUsage > 95) score -= 25;
        else if (maxDiskUsage > 90) score -= 20;
        else if (maxDiskUsage > 85) score -= 15;
        else if (maxDiskUsage > 80) score -= 10;
        else if (maxDiskUsage > 75) score -= 5;
        
        return Math.max(score, 0);
    }

    /**
     * 获取健康状态
     */
    private String getHealthStatus(int score) {
        if (score >= 90) return "优秀";
        else if (score >= 80) return "良好";
        else if (score >= 70) return "一般";
        else if (score >= 60) return "较差";
        else return "差";
    }

    /**
     * 检查CPU健康状态
     */
    private HealthCheckVO.HealthMetric checkCpuHealth(CpuInfoVO cpu) {
        HealthCheckVO.HealthMetric metric = new HealthCheckVO.HealthMetric();
        metric.setName("CPU");
        metric.setCurrentValue(cpu.getUsagePercent());
        metric.setThreshold(80.0);
        metric.setPriority(5);
        
        if (cpu.getUsagePercent() > 90) {
            metric.setStatus("CRITICAL");
            metric.setDescription("CPU使用率过高");
        } else if (cpu.getUsagePercent() > 80) {
            metric.setStatus("WARNING");
            metric.setDescription("CPU使用率较高");
        } else {
            metric.setStatus("HEALTHY");
            metric.setDescription("CPU运行正常");
        }
        
        return metric;
    }

    /**
     * 检查内存健康状态
     */
    private HealthCheckVO.HealthMetric checkMemoryHealth(MemoryInfoVO memory) {
        HealthCheckVO.HealthMetric metric = new HealthCheckVO.HealthMetric();
        metric.setName("内存");
        metric.setCurrentValue(memory.getUsage());
        metric.setThreshold(85.0);
        metric.setPriority(5);
        
        if (memory.getUsage() > 95) {
            metric.setStatus("CRITICAL");
            metric.setDescription("内存严重不足");
        } else if (memory.getUsage() > 85) {
            metric.setStatus("WARNING");
            metric.setDescription("内存使用率较高");
        } else {
            metric.setStatus("HEALTHY");
            metric.setDescription("内存使用正常");
        }
        
        return metric;
    }

    /**
     * 检查JVM健康状态
     */
    private HealthCheckVO.HealthMetric checkJvmHealth(JvmInfoVO jvm) {
        HealthCheckVO.HealthMetric metric = new HealthCheckVO.HealthMetric();
        metric.setName("JVM");
        metric.setCurrentValue(jvm.getUsage());
        metric.setThreshold(85.0);
        metric.setPriority(4);
        
        if (jvm.getUsage() > 95) {
            metric.setStatus("CRITICAL");
            metric.setDescription("JVM内存严重不足");
        } else if (jvm.getUsage() > 85) {
            metric.setStatus("WARNING");
            metric.setDescription("JVM内存使用率较高");
        } else {
            metric.setStatus("HEALTHY");
            metric.setDescription("JVM运行正常");
        }
        
        return metric;
    }

    /**
     * 检查磁盘健康状态
     */
    private HealthCheckVO.HealthMetric checkDiskHealth(List<DiskInfoVO> disks) {
        HealthCheckVO.HealthMetric metric = new HealthCheckVO.HealthMetric();
        metric.setName("磁盘");
        metric.setPriority(3);
        
        double maxUsage = disks.stream()
                .mapToDouble(DiskInfoVO::getUsage)
                .max().orElse(0.0);
        
        metric.setCurrentValue(maxUsage);
        metric.setThreshold(90.0);
        
        if (maxUsage > 95) {
            metric.setStatus("CRITICAL");
            metric.setDescription("磁盘空间严重不足");
        } else if (maxUsage > 90) {
            metric.setStatus("WARNING");
            metric.setDescription("磁盘空间使用率较高");
        } else {
            metric.setStatus("HEALTHY");
            metric.setDescription("磁盘空间充足");
        }
        
        return metric;
    }

    /**
     * 网络统计数据内部类
     */
    private static class NetworkStats {
        final long bytesSent;
        final long bytesReceived;
        
        NetworkStats(long bytesSent, long bytesReceived) {
            this.bytesSent = bytesSent;
            this.bytesReceived = bytesReceived;
        }
    }
} 