package ${servicePkg};

<#compress>
import com.mybatisflex.core.service.IService;
import ${poPkg}.${poClassName};
<#if GeneratorInfo.generateType != "service">
import com.mars.easy.admin.framework.entity.SelectIdsDTO;
import com.mars.easy.admin.framework.entity.PageResult;
import java.util.List;

import ${dtoPkg}.${dtoCreateClassName};
import ${dtoPkg}.${dtoUpdateClassName};
import ${dtoPkg}.${dtoListClassName};
import ${voPkg}.${voClassName};
<#if GeneratorInfo.hasImport == "1">
import org.springframework.web.multipart.MultipartFile;
</#if>
<#if GeneratorInfo.hasExport == "1">
import jakarta.servlet.http.HttpServletResponse;
</#if>
</#if>
</#compress>


/**
 * ${tableComment} Service
 *
 * @author ${author}
 * @since ${datetime}
 */
public interface ${serviceClassName} extends IService<${poClassName}> {

<#if GeneratorInfo.generateType != "service">
    void create(${dtoCreateClassName} dto);

    void update(${dtoUpdateClassName} dto);

    PageResult<${voClassName}> page(${dtoListClassName} dto);

    List<${voClassName}> list(${dtoListClassName} dto);

    void remove(SelectIdsDTO dto);

    ${voClassName} detail(Object id);

    <#if GeneratorInfo.hasImport == "1">
    void importExcel(MultipartFile file);
    </#if>

    <#if GeneratorInfo.hasExport == "1">
    void exportExcel(${dtoListClassName} dto, HttpServletResponse response);
    </#if>
</#if>

}
