package com.mars.admin.framework.listener;

import com.mars.admin.framework.context.UserContext;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.SetListener;
import com.mybatisflex.annotation.UpdateListener;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * 数据表change事件处理
 * 自动填充公共字段：创建时间、更新时间、创建人、更新人
 *
 * @author 程序员Mars
 * @version 1.0
 * @date 2025-07-05
 */
@Slf4j
public class EntityChangeListener implements InsertListener, UpdateListener, SetListener {

    @Override
    public void onInsert(Object entity) {
        // 设置创建时间
        setPropertyIfPresent(entity, "createTime", LocalDateTime.now());
        
        // 设置创建人
        Long currentUserId = UserContext.getCurrentUserId();
        if (currentUserId != null) {
            setPropertyIfPresent(entity, "createBy", currentUserId);
        }
        
        log.debug("Auto fill insert fields for entity: {}", entity.getClass().getSimpleName());
    }

    @Override
    public void onUpdate(Object entity) {
        // 设置更新时间
        setPropertyIfPresent(entity, "updateTime", LocalDateTime.now());
        
        // 设置更新人
        Long currentUserId = UserContext.getCurrentUserId();
        if (currentUserId != null) {
            setPropertyIfPresent(entity, "updateBy", currentUserId);
        }
        
        log.debug("Auto fill update fields for entity: {}", entity.getClass().getSimpleName());
    }

    @Override
    public Object onSet(Object entity, String property, Object value) {
        return value;
    }

    /**
     * 设置对象属性值（如果属性存在）
     *
     * @param object        目标对象
     * @param propertyName  属性名
     * @param propertyValue 属性值
     */
    private void setPropertyIfPresent(Object object, String propertyName, Object propertyValue) {
        try {
            // 获取对象的 Class 对象
            Class<?> clazz = object.getClass();
            
            // 尝试在当前类中查找字段
            Field field = findField(clazz, propertyName);
            
            if (field != null) {
                // 设置字段为可访问，以便访问私有字段
                field.setAccessible(true);
                // 设置字段的值
                field.set(object, propertyValue);
                log.debug("Auto filled field [{}] with value [{}] for entity [{}]", 
                    propertyName, propertyValue, clazz.getSimpleName());
            }
        } catch (IllegalAccessException e) {
            log.warn("Failed to set property [{}] for entity [{}]: {}", 
                propertyName, object.getClass().getSimpleName(), e.getMessage());
        }
    }

    /**
     * 在类及其父类中查找字段
     *
     * @param clazz     类对象
     * @param fieldName 字段名
     * @return 字段对象，如果未找到返回null
     */
    private Field findField(Class<?> clazz, String fieldName) {
        while (clazz != null && clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // 在父类中继续查找
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }
}
