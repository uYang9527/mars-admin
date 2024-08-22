package com.mars.easy.admin.modules.tool.core.builder.java;


import com.mars.easy.admin.modules.tool.core.AbstractCodeGenerationTemplate;
import com.mars.easy.admin.modules.tool.core.GeneratorConstants;
import com.mars.easy.admin.modules.tool.pojo.vo.GeneratorDetailVO;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.util.Map;

/**
 * @ClassName PoCodeBuilder
 * @Author 程序员Mars
 * @Date 2024/1/16 8:02
 * @Version 1.0
 */
public class DtoImportCodeBuilder extends AbstractCodeGenerationTemplate {

    public DtoImportCodeBuilder(FreeMarkerConfigurer configurer, String rootPath, GeneratorDetailVO detailVO, Map<String, Object> model) {
        super(configurer, rootPath, detailVO, model);
    }

    @Override
    protected String getTemplateFileName() {
        return File.separator + "api" + File.separator + "dtoImport.java.ftl";
    }

    @Override
    protected String getOutputFileName(Map<String, Object> model) {
        return model.get("dtoImportClassName").toString();
    }

    @Override
    protected String getOutputPackage(Map<String, Object> model) {
        return model.get("dtoPkg").toString();
    }

    @Override
    protected String getProjectPrefix() {
        return GeneratorConstants.PROJECT_JAVA_PREFIX;
    }

    @Override
    protected String getExtension() {
        return ".java";
    }

    @Override
    protected String getZipParentPackage() {
        return "java";
    }

    @Override
    protected String alias() {
        return "dtoImport";
    }

    @Override
    protected String language() {
        return "java";
    }
}
