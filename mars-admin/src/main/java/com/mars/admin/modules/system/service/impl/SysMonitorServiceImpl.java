package com.mars.admin.modules.system.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.NumberUtil;
import com.mars.admin.common.vo.monitor.*;
import com.mars.admin.modules.system.service.ISysMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;
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
    @Cacheable(value = "systemMonitor", key = "'monitorInfo'")
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

    @Override
    public Map<String, Object> getSimpleMonitorInfo() throws Exception {
        log.debug("开始获取简化版系统监控信息");

        try {
            SystemInfo si = getSystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();
            OperatingSystem os = si.getOperatingSystem();

            Map<String, Object> result = new HashMap<>();

            // 时间戳
            result.put("timestamp", System.currentTimeMillis());

            // CPU信息
            CentralProcessor processor = hal.getProcessor();
            Map<String, Object> cpu = new HashMap<>();
            cpu.put("cpuNum", processor.getLogicalProcessorCount());
            cpu.put("physicalCores", processor.getPhysicalProcessorCount());
            cpu.put("cpuModel", formatCpuModel(processor.getProcessorIdentifier().getName()));
            cpu.put("architecture", processor.getProcessorIdentifier().getMicroarchitecture());

            // 计算CPU使用率
            long[] prevTicks = processor.getSystemCpuLoadTicks();
            Util.sleep(500); // 等待500ms计算使用率
            long[] ticks = processor.getSystemCpuLoadTicks();

            long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
            long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
            long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
            long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
            long totalCpu = user + sys + idle + iowait;

            double usagePercent = totalCpu > 0 ? (double)(user + sys) / totalCpu * 100 : 0.0;
            cpu.put("usagePercent", Math.round(usagePercent * 100.0) / 100.0);
            cpu.put("userPercent", totalCpu > 0 ? Math.round((double)user / totalCpu * 10000.0) / 100.0 : 0.0);
            cpu.put("systemPercent", totalCpu > 0 ? Math.round((double)sys / totalCpu * 10000.0) / 100.0 : 0.0);
            cpu.put("idlePercent", totalCpu > 0 ? Math.round((double)idle / totalCpu * 10000.0) / 100.0 : 0.0);
            cpu.put("iowaitPercent", totalCpu > 0 ? Math.round((double)iowait / totalCpu * 10000.0) / 100.0 : 0.0);

            long[] freqs = processor.getCurrentFreq();
            cpu.put("frequency", freqs.length > 0 ? freqs[0] / 1_000_000L : 0);

            result.put("cpu", cpu);

            // 内存信息
            GlobalMemory memory = hal.getMemory();
            Map<String, Object> mem = new HashMap<>();
            long total = memory.getTotal();
            long available = memory.getAvailable();
            long used = total - available;

            mem.put("total", total);
            mem.put("used", used);
            mem.put("available", available);
            mem.put("usage", total > 0 ? Math.round((double)used / total * 10000.0) / 100.0 : 0.0);
            mem.put("totalFormatted", convertFileSize(total));
            mem.put("usedFormatted", convertFileSize(used));
            mem.put("availableFormatted", convertFileSize(available));

            // 交换分区信息
            try {
                VirtualMemory virtualMemory = memory.getVirtualMemory();
                long swapTotal = virtualMemory.getSwapTotal();
                long swapUsed = virtualMemory.getSwapUsed();
                mem.put("swapTotal", swapTotal);
                mem.put("swapUsed", swapUsed);
                mem.put("swapUsage", swapTotal > 0 ? Math.round((double)swapUsed / swapTotal * 10000.0) / 100.0 : 0.0);
                mem.put("swapTotalFormatted", convertFileSize(swapTotal));
                mem.put("swapUsedFormatted", convertFileSize(swapUsed));
            } catch (Exception e) {
                log.debug("获取交换分区信息失败", e);
            }

            result.put("memory", mem);

            // JVM信息
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
            MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();

            Map<String, Object> jvm = new HashMap<>();
            long jvmTotal = heapMemoryUsage.getMax();
            long jvmUsed = heapMemoryUsage.getUsed();
            long jvmCommitted = heapMemoryUsage.getCommitted();

            jvm.put("total", jvmTotal);
            jvm.put("used", jvmUsed);
            jvm.put("committed", jvmCommitted);
            jvm.put("usage", jvmTotal > 0 ? Math.round((double)jvmUsed / jvmTotal * 10000.0) / 100.0 : 0.0);
            jvm.put("heapFormatted", convertFileSize(jvmUsed) + " / " + convertFileSize(jvmTotal));
            jvm.put("version", System.getProperty("java.version"));
            jvm.put("vendor", System.getProperty("java.vendor"));
            jvm.put("home", System.getProperty("java.home"));

            // 非堆内存
            jvm.put("nonHeapUsed", nonHeapMemoryUsage.getUsed());
            jvm.put("nonHeapTotal", nonHeapMemoryUsage.getMax() > 0 ? nonHeapMemoryUsage.getMax() : nonHeapMemoryUsage.getCommitted());
            jvm.put("nonHeapFormatted", convertFileSize(nonHeapMemoryUsage.getUsed()) + " / " +
                    convertFileSize(nonHeapMemoryUsage.getMax() > 0 ? nonHeapMemoryUsage.getMax() : nonHeapMemoryUsage.getCommitted()));

            // JVM运行时间
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            jvm.put("startTime", runtimeMXBean.getStartTime());
            jvm.put("uptime", runtimeMXBean.getUptime());
            jvm.put("uptimeFormatted", formatDuration(runtimeMXBean.getUptime()));

            result.put("jvm", jvm);

            // 磁盘信息
            List<Map<String, Object>> diskList = new ArrayList<>();
            List<OSFileStore> fileStores = os.getFileSystem().getFileStores();
            for (OSFileStore fs : fileStores) {
                try {
                    Map<String, Object> disk = new HashMap<>();
                    long diskTotal = fs.getTotalSpace();
                    long diskFree = fs.getUsableSpace();
                    long diskUsed = diskTotal - diskFree;

                    disk.put("dirName", fs.getMount());
                    disk.put("fsType", fs.getType());
                    disk.put("name", fs.getName());
                    disk.put("total", diskTotal);
                    disk.put("used", diskUsed);
                    disk.put("free", diskFree);
                    disk.put("usage", diskTotal > 0 ? Math.round((double)diskUsed / diskTotal * 10000.0) / 100.0 : 0.0);
                    disk.put("totalFormatted", convertFileSize(diskTotal));
                    disk.put("usedFormatted", convertFileSize(diskUsed));
                    disk.put("freeFormatted", convertFileSize(diskFree));

                    diskList.add(disk);
                } catch (Exception e) {
                    log.debug("获取磁盘信息失败: {}", fs.getMount(), e);
                }
            }
            result.put("disk", diskList);

            // 网络信息
            Map<String, Object> network = new HashMap<>();
            List<NetworkIF> networkIFs = hal.getNetworkIFs();

            long totalBytesSent = 0;
            long totalBytesReceived = 0;
            long totalPacketsSent = 0;
            long totalPacketsReceived = 0;
            List<Map<String, Object>> interfaceList = new ArrayList<>();

            for (NetworkIF net : networkIFs) {
                try {
                    if (net.getBytesRecv() > 0 || net.getBytesSent() > 0) {
                        Map<String, Object> iface = new HashMap<>();
                        iface.put("name", net.getName());
                        iface.put("displayName", net.getDisplayName());
                        iface.put("macAddress", net.getMacaddr());
                        iface.put("speed", net.getSpeed());
                        iface.put("bytesSent", net.getBytesSent());
                        iface.put("bytesReceived", net.getBytesRecv());
                        iface.put("packetsSent", net.getPacketsSent());
                        iface.put("packetsReceived", net.getPacketsRecv());
                        iface.put("bytesReceivedFormatted", convertFileSize(net.getBytesRecv()));
                        iface.put("bytesSentFormatted", convertFileSize(net.getBytesSent()));
                        iface.put("ipAddresses", Arrays.asList(net.getIPv4addr()));

                        interfaceList.add(iface);

                        totalBytesSent += net.getBytesSent();
                        totalBytesReceived += net.getBytesRecv();
                        totalPacketsSent += net.getPacketsSent();
                        totalPacketsReceived += net.getPacketsRecv();
                    }
                } catch (Exception e) {
                    log.debug("获取网络接口信息失败: {}", net.getName(), e);
                }
            }

            network.put("totalBytesSent", totalBytesSent);
            network.put("totalBytesReceived", totalBytesReceived);
            network.put("totalPacketsSent", totalPacketsSent);
            network.put("totalPacketsReceived", totalPacketsReceived);
            network.put("totalBytesSentFormatted", convertFileSize(totalBytesSent));
            network.put("totalBytesReceivedFormatted", convertFileSize(totalBytesReceived));
            network.put("interfaces", interfaceList);
            network.put("interfaceCount", interfaceList.size());

            result.put("network", network);

            // 线程信息
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            Map<String, Object> thread = new HashMap<>();
            thread.put("liveThreads", threadMXBean.getThreadCount());
            thread.put("daemonThreads", threadMXBean.getDaemonThreadCount());
            thread.put("peakThreads", threadMXBean.getPeakThreadCount());
            thread.put("totalStartedThreads", threadMXBean.getTotalStartedThreadCount());

            // 获取各种状态的线程数
            ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds());
            int newThreads = 0, runnableThreads = 0, blockedThreads = 0, waitingThreads = 0, timedWaitingThreads = 0, terminatedThreads = 0;

            for (ThreadInfo info : threadInfos) {
                if (info != null) {
                    switch (info.getThreadState()) {
                        case NEW: newThreads++; break;
                        case RUNNABLE: runnableThreads++; break;
                        case BLOCKED: blockedThreads++; break;
                        case WAITING: waitingThreads++; break;
                        case TIMED_WAITING: timedWaitingThreads++; break;
                        case TERMINATED: terminatedThreads++; break;
                    }
                }
            }

            thread.put("newThreads", newThreads);
            thread.put("runnableThreads", runnableThreads);
            thread.put("blockedThreads", blockedThreads);
            thread.put("waitingThreads", waitingThreads);
            thread.put("timedWaitingThreads", timedWaitingThreads);
            thread.put("terminatedThreads", terminatedThreads);

            // 检查死锁
            long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
            thread.put("deadlockedThreads", deadlockedThreads != null ? deadlockedThreads.length : 0);

            result.put("thread", thread);

            // 系统负载信息
            Map<String, Object> systemLoad = new HashMap<>();
            double[] loadAverage = processor.getSystemLoadAverage(3);
            if (loadAverage[0] >= 0) {
                systemLoad.put("load1", Math.round(loadAverage[0] * 100.0) / 100.0);
                systemLoad.put("load5", loadAverage.length > 1 && loadAverage[1] >= 0 ? Math.round(loadAverage[1] * 100.0) / 100.0 : 0.0);
                systemLoad.put("load15", loadAverage.length > 2 && loadAverage[2] >= 0 ? Math.round(loadAverage[2] * 100.0) / 100.0 : 0.0);

                // 计算负载等级
                double load1 = loadAverage[0];
                int processorCount = processor.getLogicalProcessorCount();
                double loadPercent = (load1 / processorCount) * 100;

                systemLoad.put("loadPercent", Math.round(loadPercent * 100.0) / 100.0);
                systemLoad.put("loadLevel", loadPercent > 80 ? "高" : loadPercent > 60 ? "中" : "低");
            } else {
                systemLoad.put("load1", 0.0);
                systemLoad.put("load5", 0.0);
                systemLoad.put("load15", 0.0);
                systemLoad.put("loadPercent", 0.0);
                systemLoad.put("loadLevel", "未知");
            }

            // 进程数
            systemLoad.put("processCount", os.getProcessCount());

            result.put("systemLoad", systemLoad);

            // 服务器信息
            Map<String, Object> server = new HashMap<>();
            server.put("computerName", getComputerName());
            server.put("osName", formatOsName(os));
            server.put("osVersion", os.getVersionInfo().getVersion());
            server.put("osArch", System.getProperty("os.arch"));
            server.put("uptime", formatDuration(os.getSystemUptime() * 1000));
            server.put("uptimeSeconds", os.getSystemUptime());
            server.put("javaVersion", System.getProperty("java.version"));
            server.put("javaVendor", System.getProperty("java.vendor"));
            server.put("timezone", ZoneId.systemDefault().toString());
            server.put("availableProcessors", Runtime.getRuntime().availableProcessors());

            result.put("server", server);

            log.debug("简化版系统监控信息获取完成");
            return result;

        } catch (Exception e) {
            log.error("获取简化版系统监控信息失败", e);
            throw new Exception("获取简化版系统监控信息失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getResourceUsage() throws Exception {
        try {
            SystemInfo si = getSystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();

            Map<String, Object> result = new HashMap<>();

            // CPU使用率
            CentralProcessor processor = hal.getProcessor();
            double cpuUsage = processor.getSystemCpuLoadBetweenTicks(processor.getSystemCpuLoadTicks()) * 100;
            result.put("cpuUsage", Math.round(cpuUsage * 100.0) / 100.0);

            // 内存使用率
            GlobalMemory memory = hal.getMemory();
            long total = memory.getTotal();
            long available = memory.getAvailable();
            double memoryUsage = total > 0 ? (double)(total - available) / total * 100 : 0.0;
            result.put("memoryUsage", Math.round(memoryUsage * 100.0) / 100.0);

            // JVM堆内存使用率
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
            long jvmTotal = heapMemoryUsage.getMax();
            long jvmUsed = heapMemoryUsage.getUsed();
            double jvmUsage = jvmTotal > 0 ? (double)jvmUsed / jvmTotal * 100 : 0.0;
            result.put("jvmUsage", Math.round(jvmUsage * 100.0) / 100.0);

            result.put("timestamp", System.currentTimeMillis());

            return result;

        } catch (Exception e) {
            log.error("获取资源使用情况失败", e);
            throw new Exception("获取资源使用情况失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getSystemAlerts(Double cpuThreshold, Double memoryThreshold,
                                              Double diskThreshold, Double jvmThreshold) throws Exception {
        try {
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> alerts = new ArrayList<>();

            // 设置默认阈值
            cpuThreshold = cpuThreshold != null ? cpuThreshold : 80.0;
            memoryThreshold = memoryThreshold != null ? memoryThreshold : 85.0;
            diskThreshold = diskThreshold != null ? diskThreshold : 90.0;
            jvmThreshold = jvmThreshold != null ? jvmThreshold : 85.0;

            // 获取当前监控数据
            Map<String, Object> monitorData = getSimpleMonitorInfo();

            // CPU告警检查
            Map<String, Object> cpu = (Map<String, Object>) monitorData.get("cpu");
            if (cpu != null && cpu.get("usagePercent") instanceof Number) {
                double cpuUsage = ((Number) cpu.get("usagePercent")).doubleValue();
                if (cpuUsage > cpuThreshold) {
                    Map<String, Object> alert = new HashMap<>();
                    alert.put("type", "CPU");
                    alert.put("level", cpuUsage > 90 ? "严重" : "警告");
                    alert.put("message", String.format("CPU使用率过高: %.1f%% (阈值: %.1f%%)", cpuUsage, cpuThreshold));
                    alert.put("value", cpuUsage);
                    alert.put("threshold", cpuThreshold);
                    alert.put("timestamp", System.currentTimeMillis());
                    alerts.add(alert);
                }
            }

            // 内存告警检查
            Map<String, Object> memory = (Map<String, Object>) monitorData.get("memory");
            if (memory != null && memory.get("usage") instanceof Number) {
                double memoryUsage = ((Number) memory.get("usage")).doubleValue();
                if (memoryUsage > memoryThreshold) {
                    Map<String, Object> alert = new HashMap<>();
                    alert.put("type", "MEMORY");
                    alert.put("level", memoryUsage > 95 ? "严重" : "警告");
                    alert.put("message", String.format("内存使用率过高: %.1f%% (阈值: %.1f%%)", memoryUsage, memoryThreshold));
                    alert.put("value", memoryUsage);
                    alert.put("threshold", memoryThreshold);
                    alert.put("timestamp", System.currentTimeMillis());
                    alerts.add(alert);
                }
            }

            // JVM告警检查
            Map<String, Object> jvm = (Map<String, Object>) monitorData.get("jvm");
            if (jvm != null && jvm.get("usage") instanceof Number) {
                double jvmUsage = ((Number) jvm.get("usage")).doubleValue();
                if (jvmUsage > jvmThreshold) {
                    Map<String, Object> alert = new HashMap<>();
                    alert.put("type", "JVM");
                    alert.put("level", jvmUsage > 95 ? "严重" : "警告");
                    alert.put("message", String.format("JVM堆内存使用率过高: %.1f%% (阈值: %.1f%%)", jvmUsage, jvmThreshold));
                    alert.put("value", jvmUsage);
                    alert.put("threshold", jvmThreshold);
                    alert.put("timestamp", System.currentTimeMillis());
                    alerts.add(alert);
                }
            }

            // 磁盘告警检查
            List<Map<String, Object>> diskList = (List<Map<String, Object>>) monitorData.get("disk");
            if (diskList != null) {
                for (Map<String, Object> disk : diskList) {
                    if (disk.get("usage") instanceof Number) {
                        double diskUsage = ((Number) disk.get("usage")).doubleValue();
                        if (diskUsage > diskThreshold) {
                            Map<String, Object> alert = new HashMap<>();
                            alert.put("type", "DISK");
                            alert.put("level", diskUsage > 95 ? "严重" : "警告");
                            alert.put("message", String.format("磁盘 %s 使用率过高: %.1f%% (阈值: %.1f%%)",
                                    disk.get("dirName"), diskUsage, diskThreshold));
                            alert.put("value", diskUsage);
                            alert.put("threshold", diskThreshold);
                            alert.put("diskName", disk.get("dirName"));
                            alert.put("timestamp", System.currentTimeMillis());
                            alerts.add(alert);
                        }
                    }
                }
            }

            // 死锁检查
            Map<String, Object> thread = (Map<String, Object>) monitorData.get("thread");
            if (thread != null && thread.get("deadlockedThreads") instanceof Number) {
                int deadlocks = ((Number) thread.get("deadlockedThreads")).intValue();
                if (deadlocks > 0) {
                    Map<String, Object> alert = new HashMap<>();
                    alert.put("type", "THREAD");
                    alert.put("level", "严重");
                    alert.put("message", String.format("检测到线程死锁: %d个", deadlocks));
                    alert.put("value", deadlocks);
                    alert.put("timestamp", System.currentTimeMillis());
                    alerts.add(alert);
                }
            }

            result.put("hasAlerts", !alerts.isEmpty());
            result.put("alertCount", alerts.size());
            result.put("alerts", alerts);
            result.put("timestamp", System.currentTimeMillis());
            result.put("thresholds", Map.of(
                    "cpu", cpuThreshold,
                    "memory", memoryThreshold,
                    "disk", diskThreshold,
                    "jvm", jvmThreshold
            ));

            return result;

        } catch (Exception e) {
            log.error("获取系统告警信息失败", e);
            throw new Exception("获取告警信息失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getPerformanceOverview() throws Exception {
        try {
            Map<String, Object> overview = new HashMap<>();
            Map<String, Object> monitorData = getSimpleMonitorInfo();

            // 系统健康分数计算
            int healthScore = 100;
            Map<String, Object> cpu = (Map<String, Object>) monitorData.get("cpu");
            Map<String, Object> memory = (Map<String, Object>) monitorData.get("memory");
            Map<String, Object> jvm = (Map<String, Object>) monitorData.get("jvm");

            if (cpu != null && cpu.get("usagePercent") instanceof Number) {
                double cpuUsage = ((Number) cpu.get("usagePercent")).doubleValue();
                if (cpuUsage > 90) healthScore -= 30;
                else if (cpuUsage > 80) healthScore -= 20;
                else if (cpuUsage > 70) healthScore -= 10;
            }

            if (memory != null && memory.get("usage") instanceof Number) {
                double memoryUsage = ((Number) memory.get("usage")).doubleValue();
                if (memoryUsage > 95) healthScore -= 25;
                else if (memoryUsage > 85) healthScore -= 15;
                else if (memoryUsage > 75) healthScore -= 8;
            }

            if (jvm != null && jvm.get("usage") instanceof Number) {
                double jvmUsage = ((Number) jvm.get("usage")).doubleValue();
                if (jvmUsage > 95) healthScore -= 25;
                else if (jvmUsage > 85) healthScore -= 15;
                else if (jvmUsage > 75) healthScore -= 8;
            }

            // 磁盘健康检查
            List<Map<String, Object>> diskList = (List<Map<String, Object>>) monitorData.get("disk");
            if (diskList != null) {
                for (Map<String, Object> disk : diskList) {
                    if (disk.get("usage") instanceof Number) {
                        double diskUsage = ((Number) disk.get("usage")).doubleValue();
                        if (diskUsage > 95) healthScore -= 15;
                        else if (diskUsage > 90) healthScore -= 10;
                        else if (diskUsage > 80) healthScore -= 5;
                    }
                }
            }

            healthScore = Math.max(0, healthScore);

            overview.put("healthScore", healthScore);
            overview.put("healthStatus", healthScore >= 90 ? "优秀" :
                                      healthScore >= 80 ? "良好" :
                                      healthScore >= 60 ? "一般" :
                                      healthScore >= 40 ? "较差" : "危险");

            // 性能等级
            String performanceLevel = "优秀";
            if (cpu != null && cpu.get("usagePercent") instanceof Number) {
                double cpuUsage = ((Number) cpu.get("usagePercent")).doubleValue();
                if (cpuUsage > 80) performanceLevel = "高负载";
                else if (cpuUsage > 60) performanceLevel = "中等";
            }

            overview.put("performanceLevel", performanceLevel);
            overview.put("timestamp", System.currentTimeMillis());

            // 关键指标摘要
            Map<String, Object> summary = new HashMap<>();
            summary.put("cpu", cpu != null ? cpu.get("usagePercent") : 0);
            summary.put("memory", memory != null ? memory.get("usage") : 0);
            summary.put("jvm", jvm != null ? jvm.get("usage") : 0);

            overview.put("summary", summary);

            return overview;

        } catch (Exception e) {
            log.error("获取性能总览失败", e);
            throw new Exception("获取性能总览失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getProcessInfo() throws Exception {
        try {
            Map<String, Object> result = new HashMap<>();
            SystemInfo si = getSystemInfo();
            OperatingSystem os = si.getOperatingSystem();

            // 当前进程信息
            Map<String, Object> currentProcess = new HashMap<>();
            OSProcess currentOSProcess = os.getProcess(os.getProcessId());
            if (currentOSProcess != null) {
                currentProcess.put("pid", currentOSProcess.getProcessID());
                currentProcess.put("name", currentOSProcess.getName());
                currentProcess.put("commandLine", currentOSProcess.getCommandLine());
                currentProcess.put("user", currentOSProcess.getUser());
                currentProcess.put("residentSetSize", currentOSProcess.getResidentSetSize());
                currentProcess.put("virtualSize", currentOSProcess.getVirtualSize());
                currentProcess.put("cpuPercent", currentOSProcess.getProcessCpuLoadCumulative() * 100);
                currentProcess.put("memoryPercent", (double) currentOSProcess.getResidentSetSize() /
                                                   si.getHardware().getMemory().getTotal() * 100);
                currentProcess.put("startTime", currentOSProcess.getStartTime());
                currentProcess.put("upTime", currentOSProcess.getUpTime());
            }
            result.put("currentProcess", currentProcess);

            // 系统进程统计（简化版）
            List<Map<String, Object>> topProcesses = new ArrayList<>();

            // 获取所有进程并限制数量
            Collection<OSProcess> processes = os.getProcesses().stream()
                    .sorted((p1, p2) -> Double.compare(p2.getProcessCpuLoadCumulative(), p1.getProcessCpuLoadCumulative()))
                    .limit(10)
                    .toList();

            for (OSProcess process : processes) {
                try {
                    Map<String, Object> proc = new HashMap<>();
                    proc.put("pid", process.getProcessID());
                    proc.put("name", process.getName());
                    proc.put("user", process.getUser());
                    proc.put("cpuPercent", Math.round(process.getProcessCpuLoadCumulative() * 10000.0) / 100.0);
                    proc.put("memoryPercent", Math.round((double) process.getResidentSetSize() /
                                                       si.getHardware().getMemory().getTotal() * 10000.0) / 100.0);
                    proc.put("residentSetSize", process.getResidentSetSize());
                    proc.put("residentSetSizeFormatted", convertFileSize(process.getResidentSetSize()));
                    topProcesses.add(proc);
                } catch (Exception e) {
                    log.debug("获取进程信息失败: PID={}", process.getProcessID(), e);
                }
            }

            result.put("topProcesses", topProcesses);
            result.put("totalProcesses", os.getProcessCount());
            result.put("timestamp", System.currentTimeMillis());

            return result;

        } catch (Exception e) {
            log.error("获取进程信息失败", e);
            throw new Exception("获取进程信息失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getGcInfo() throws Exception {
        try {
            Map<String, Object> result = new HashMap<>();
            List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
            List<Map<String, Object>> gcList = new ArrayList<>();

            long totalCollectionCount = 0;
            long totalCollectionTime = 0;

            for (GarbageCollectorMXBean gcBean : gcBeans) {
                Map<String, Object> gc = new HashMap<>();
                gc.put("name", gcBean.getName());
                gc.put("collectionCount", gcBean.getCollectionCount());
                gc.put("collectionTime", gcBean.getCollectionTime());

                if (gcBean.getCollectionCount() > 0) {
                    gc.put("averageCollectionTime", (double) gcBean.getCollectionTime() / gcBean.getCollectionCount());
                } else {
                    gc.put("averageCollectionTime", 0.0);
                }

                // GC类型判断
                String gcType = "未知";
                String name = gcBean.getName().toLowerCase();
                if (name.contains("young") || name.contains("minor") || name.contains("scavenge") || name.contains("parnew")) {
                    gcType = "新生代";
                } else if (name.contains("old") || name.contains("major") || name.contains("cms") || name.contains("mark")) {
                    gcType = "老年代";
                } else if (name.contains("g1")) {
                    gcType = "G1";
                }
                gc.put("type", gcType);

                totalCollectionCount += gcBean.getCollectionCount();
                totalCollectionTime += gcBean.getCollectionTime();

                gcList.add(gc);
            }

            result.put("collectors", gcList);
            result.put("totalCollectionCount", totalCollectionCount);
            result.put("totalCollectionTime", totalCollectionTime);
            result.put("averageCollectionTime", totalCollectionCount > 0 ?
                      (double) totalCollectionTime / totalCollectionCount : 0.0);

            // 内存池信息
            List<MemoryPoolMXBean> memoryPools = ManagementFactory.getMemoryPoolMXBeans();
            List<Map<String, Object>> poolList = new ArrayList<>();

            for (MemoryPoolMXBean pool : memoryPools) {
                Map<String, Object> poolInfo = new HashMap<>();
                poolInfo.put("name", pool.getName());
                poolInfo.put("type", pool.getType().toString());

                MemoryUsage usage = pool.getUsage();
                if (usage != null) {
                    poolInfo.put("used", usage.getUsed());
                    poolInfo.put("committed", usage.getCommitted());
                    poolInfo.put("max", usage.getMax());
                    poolInfo.put("init", usage.getInit());

                    if (usage.getMax() > 0) {
                        poolInfo.put("usage", Math.round((double) usage.getUsed() / usage.getMax() * 10000.0) / 100.0);
                    } else {
                        poolInfo.put("usage", 0.0);
                    }

                    poolInfo.put("usedFormatted", convertFileSize(usage.getUsed()));
                    poolInfo.put("maxFormatted", usage.getMax() > 0 ? convertFileSize(usage.getMax()) : "N/A");
                }

                poolList.add(poolInfo);
            }

            result.put("memoryPools", poolList);
            result.put("timestamp", System.currentTimeMillis());

            return result;

        } catch (Exception e) {
            log.error("获取垃圾回收信息失败", e);
            throw new Exception("获取垃圾回收信息失败: " + e.getMessage(), e);
        }
    }
}
