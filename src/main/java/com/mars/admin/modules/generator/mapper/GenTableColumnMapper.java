package com.mars.admin.modules.generator.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.mars.admin.framework.mybatis.core.mapper.BaseMapperPlus;
import com.mars.admin.modules.generator.domain.GenTableColumn;

/**
 * 业务字段 数据层
 *
 * @author Mars
 */
@InterceptorIgnore(dataPermission = "true", tenantLine = "true")
public interface GenTableColumnMapper extends BaseMapperPlus<GenTableColumn, GenTableColumn> {

}
