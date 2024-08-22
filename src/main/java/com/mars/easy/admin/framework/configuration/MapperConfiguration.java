package com.mars.easy.admin.framework.configuration;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author: mars
 * @date: 2022/8/26 15:14
 */
@Slf4j
@Configuration
@MapperScan({"com.mars.easy.admin.modules.*.mapper","com.mars.easy.admin.modules.business.*.mapper"})
public class MapperConfiguration {
}
