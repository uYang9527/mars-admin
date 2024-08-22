package ${voPkg};

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
<#list importPackages as pkg>
import ${pkg};
</#list>
<#if hasExcel == true>
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
  <#if hasDict == true>
import com.mars.easy.admin.framework.excel.convert.ExcelDictConvert;
import com.mars.easy.admin.framework.excel.annotation.DictFormat;
  </#if>
</#if>
<#if hasDateFormat == true>
import org.springframework.format.annotation.DateTimeFormat;
</#if>

/**
 * ${poClassName}返回vo
 *
 * @author ${author}
 * @since ${datetime}
 */
@Data
@Schema(description = "${poClassName}返回vo")
public class ${voClassName} {

<#list columns as field>
<#if field.isList == "1" >
  <#if field.isExport == "1">
    <#if field.dictType != "">
    @ExcelProperty(value = "${field.columnComment}", converter = ExcelDictConvert.class)
    @DictFormat(dictType = "${field.dictType}")
    <#else>
    @ExcelProperty(value = "${field.columnComment}")
    </#if>
    <#if field.javaType == "LocalDateTime">
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    </#if>
  <#elseif hasExcel == true>
    @ExcelIgnore
  </#if>
    @Schema(description =  "${field.columnComment}")
    private ${field.javaType} ${field.javaField};

</#if>
</#list>
}
