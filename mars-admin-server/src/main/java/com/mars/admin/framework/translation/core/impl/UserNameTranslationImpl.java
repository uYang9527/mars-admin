package com.mars.admin.framework.translation.core.impl;

import lombok.AllArgsConstructor;
import com.mars.admin.framework.core.service.UserService;
import com.mars.admin.framework.translation.annotation.TranslationType;
import com.mars.admin.framework.translation.constant.TransConstant;
import com.mars.admin.framework.translation.core.TranslationInterface;

/**
 * 用户名翻译实现
 *
 * @author Mars
 */
@AllArgsConstructor
@TranslationType(type = TransConstant.USER_ID_TO_NAME)
public class UserNameTranslationImpl implements TranslationInterface<String> {

    private final UserService userService;

    @Override
    public String translation(Object key, String other) {
        if (key instanceof Long id) {
            return userService.selectUserNameById(id);
        }
        return null;
    }
}
