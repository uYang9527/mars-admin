package com.mars.admin.modules.system.domain.bo;

import com.mars.admin.modules.system.domain.SysTenantPackage;
import io.github.linpeilie.AutoMapperConfig__21;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
    config = AutoMapperConfig__21.class,
    uses = {},
    imports = {}
)
public interface SysTenantPackageBoToSysTenantPackageMapper extends BaseMapper<SysTenantPackageBo, SysTenantPackage> {
  @Mapping(
      target = "menuIds",
      expression = "java(com.mars.admin.framework.core.utils.StringUtils.join(source.getMenuIds(), \",\"))"
  )
  SysTenantPackage convert(SysTenantPackageBo source);

  @Mapping(
      target = "menuIds",
      expression = "java(com.mars.admin.framework.core.utils.StringUtils.join(source.getMenuIds(), \",\"))"
  )
  SysTenantPackage convert(SysTenantPackageBo source, @MappingTarget SysTenantPackage target);
}
