package com.mars.admin.framework.translation.core.impl;

import lombok.AllArgsConstructor;
import com.mars.admin.framework.core.service.DictService;
import com.mars.admin.framework.core.utils.StringUtils;
import com.mars.admin.framework.translation.annotation.TranslationType;
import com.mars.admin.framework.translation.constant.TransConstant;
import com.mars.admin.framework.translation.core.TranslationInterface;

/**
 * 字典翻译实现
 *
 * @author Mars
 */
@AllArgsConstructor
@TranslationType(type = TransConstant.DICT_TYPE_TO_LABEL)
public class DictTypeTranslationImpl implements TranslationInterface<String> {

    private final DictService dictService;

    @Override
    public String translation(Object key, String other) {
        if (key instanceof String dictValue && StringUtils.isNotBlank(other)) {
            return dictService.getDictLabel(other, dictValue);
        }
        return null;
    }
}
