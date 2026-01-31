package com.mars.job.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mars.job.entity.SysJobLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志Mapper
 */
@Mapper
public interface SysJobLogMapper extends BaseMapper<SysJobLog> {
}
