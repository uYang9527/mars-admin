package com.mars.admin.framework.translation.core.impl;

import lombok.AllArgsConstructor;
import com.mars.admin.framework.core.service.OssService;
import com.mars.admin.framework.translation.annotation.TranslationType;
import com.mars.admin.framework.translation.constant.TransConstant;
import com.mars.admin.framework.translation.core.TranslationInterface;

/**
 * OSS翻译实现
 *
 * @author Mars
 */
@AllArgsConstructor
@TranslationType(type = TransConstant.OSS_ID_TO_URL)
public class OssUrlTranslationImpl implements TranslationInterface<String> {

    private final OssService ossService;

    @Override
    public String translation(Object key, String other) {
        if (key instanceof String ids) {
            return ossService.selectUrlByIds(ids);
        } else if (key instanceof Long id) {
            return ossService.selectUrlByIds(id.toString());
        }
        return null;
    }
}
