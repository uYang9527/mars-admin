package com.mars.admin.modules.base.service.impl;

import com.mars.admin.modules.base.service.BaseService;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import jakarta.annotation.PostConstruct;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 极简 CRUD 服务实现基类
 * 通过反射自动注入 Mapper，只需要实现接口即可
 *
 * @param <T> 实体类型
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-06-23 17:35:08
 */
public class BaseServiceImpl<T> implements BaseService<T> {

    @Autowired
    private ApplicationContext applicationContext;

    private BaseMapper<T> baseMapper;
    private Class<T> entityClass;

    @PostConstruct
    @SuppressWarnings("unchecked")
    private void init() {
        // 获取泛型类型
        Type[] interfaces = getClass().getGenericInterfaces();
        for (Type anInterface : interfaces) {
            if (anInterface instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) anInterface;
                Type rawType = parameterizedType.getRawType();
                if (rawType instanceof Class && BaseService.class.isAssignableFrom((Class<?>) rawType)) {
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    if (actualTypeArguments.length > 0) {
                        entityClass = (Class<T>) actualTypeArguments[0];
                        break;
                    }
                }
            }
        }

        // 如果从接口获取不到，尝试从父类获取
        if (entityClass == null) {
            Type superClass = getClass().getGenericSuperclass();
            if (superClass instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) superClass;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length > 0) {
                    entityClass = (Class<T>) actualTypeArguments[0];
                }
            }
        }

        // 自动注入对应的 Mapper
        if (entityClass != null) {
            String mapperBeanName = getMapperBeanName(entityClass);
            try {
                baseMapper = (BaseMapper<T>) applicationContext.getBean(mapperBeanName);
            } catch (Exception e) {
                // 如果按名称找不到，尝试按类型查找
                Map<String, BaseMapper> mappers = applicationContext.getBeansOfType(BaseMapper.class);
                for (BaseMapper mapper : mappers.values()) {
                    // 简单匹配，实际项目中可以更精确
                    baseMapper = (BaseMapper<T>) mapper;
                    break;
                }
                if (baseMapper == null) {
                    throw new RuntimeException("无法找到对应的 Mapper: " + mapperBeanName + ", 请确保 Mapper 已正确配置");
                }
            }
        } else {
            throw new RuntimeException("无法获取实体类型，请检查泛型参数配置");
        }
    }

    private String getMapperBeanName(Class<T> entityClass) {
        String entityName = entityClass.getSimpleName();
        return Character.toLowerCase(entityName.charAt(0)) + entityName.substring(1) + "Mapper";
    }

    @Override
    public boolean save(T entity) {
        return baseMapper.insert(entity) > 0;
    }

    @Override
    public boolean saveBatch(Collection<T> entityList) {
        return baseMapper.insertBatch(entityList) > 0;
    }

    @Override
    public boolean saveOrUpdate(T entity) {
        return baseMapper.insertOrUpdate(entity) > 0;
    }

    @Override
    public boolean removeById(Serializable id) {
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return baseMapper.deleteBatchByIds(idList) > 0;
    }

    @Override
    public boolean updateById(T entity) {
        return baseMapper.update(entity) > 0;
    }

    @Override
    public T getById(Serializable id) {
        return baseMapper.selectOneById(id);
    }

    @Override
    public List<T> listByIds(Collection<? extends Serializable> idList) {
        return baseMapper.selectListByIds(idList);
    }

    @Override
    public List<T> list() {
        return baseMapper.selectAll();
    }

    @Override
    public List<T> list(QueryWrapper queryWrapper) {
        return baseMapper.selectListByQuery(queryWrapper);
    }

    @Override
    public Page<T> page(Page<T> page) {
        return baseMapper.paginate(page, QueryWrapper.create());
    }

    @Override
    public Page<T> page(Page<T> page, QueryWrapper queryWrapper) {
        return baseMapper.paginate(page, queryWrapper);
    }

    @Override
    public long count() {
        return baseMapper.selectCountByQuery(QueryWrapper.create());
    }

    @Override
    public long count(QueryWrapper queryWrapper) {
        return baseMapper.selectCountByQuery(queryWrapper);
    }

    @Override
    public boolean remove(QueryWrapper queryWrapper) {
        return baseMapper.deleteByQuery(queryWrapper) > 0;
    }

    @Override
    public boolean update(T entity, QueryWrapper updateWrapper) {
        return baseMapper.updateByQuery(entity, updateWrapper) > 0;
    }

    @Override
    public T getOne(QueryWrapper queryWrapper) {
        return baseMapper.selectOneByQuery(queryWrapper);
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList) {
        for (T entity : entityList) {
            saveOrUpdate(entity);
        }
        return true;
    }

    @Override
    public BaseMapper<T> getMapper() {
        return baseMapper;
    }
}
