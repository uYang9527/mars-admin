package com.mars.admin.modules.system.service.impl;

import com.mars.admin.modules.system.entity.SysDictType;
import com.mars.admin.modules.system.mapper.SysDictTypeMapper;
import com.mars.admin.modules.system.service.ISysDictDataService;
import com.mars.admin.modules.system.service.ISysDictTypeService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.CacheableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.mars.admin.modules.system.entity.table.SysDictTypeTableDef.SYS_DICT_TYPE;


/**
 * 系统字典类型Service实现类
 * 继承 CacheableServiceImpl 获得更多便捷方法和缓存功能
 *
 * @author Mars
 */
@Service
@CacheConfig(cacheNames = "dictType")
public class SysDictTypeServiceImpl extends CacheableServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {

    @Autowired
    private SysDictTypeMapper sysDictTypeMapper;

    @Autowired
    private ISysDictDataService sysDictDataService;

    @Override
    @Cacheable(key = "'type:' + #dictType")
    public SysDictType selectByDictType(String dictType) {
        return sysDictTypeMapper.selectByDictType(dictType);
    }

    @Override
    @Cacheable(key = "'name:' + #dictName")
    public SysDictType selectByDictName(String dictName) {
        return sysDictTypeMapper.selectByDictName(dictName);
    }

    @Override
    public Page<SysDictType> selectDictTypePage(Page<SysDictType> page, SysDictType dictType) {
        QueryWrapper query = QueryWrapper.create()
                .select(SYS_DICT_TYPE.ALL_COLUMNS)
                .from(SYS_DICT_TYPE)
                .where(SYS_DICT_TYPE.IS_DELETED.eq(0));

        if (StringUtils.hasText(dictType.getDictName())) {
            query.and(SYS_DICT_TYPE.DICT_NAME.like(dictType.getDictName()));
        }
        if (StringUtils.hasText(dictType.getDictType())) {
            query.and(SYS_DICT_TYPE.DICT_TYPE.like(dictType.getDictType()));
        }
        if (dictType.getStatus() != null) {
            query.and(SYS_DICT_TYPE.STATUS.eq(dictType.getStatus()));
        }

        query.orderBy(SYS_DICT_TYPE.CREATE_TIME.desc());

        return this.page(page, query);
    }

    @Override
    @Cacheable(key = "'detail:' + #dictTypeId")
    public SysDictType selectDictTypeDetailById(Long dictTypeId) {
        return sysDictTypeMapper.selectDictTypeDetailById(dictTypeId);
    }

    @Override
    @Cacheable(key = "'normal'")
    public List<SysDictType> selectNormalDictTypes() {
        return sysDictTypeMapper.selectNormalDictTypes();
    }

    @Override
    @Cacheable(key = "#id")
    public SysDictType getDictTypeById(Long id) {
        return super.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public boolean insertDictType(SysDictType dictType) {
        return this.save(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
        @CacheEvict(key = "'type:' + #dictType.dictType", condition = "#dictType.dictType != null"),
        @CacheEvict(key = "'name:' + #dictType.dictName", condition = "#dictType.dictName != null"),
        @CacheEvict(key = "'detail:' + #dictType.id", condition = "#dictType.id != null"),
        @CacheEvict(key = "#dictType.id", condition = "#dictType.id != null"),
        @CacheEvict(key = "'normal'")
    })
    public boolean updateDictType(SysDictType dictType) {
        return this.updateById(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public boolean deleteDictTypeById(Long dictTypeId) {
        // 查询字典类型信息
        SysDictType dictType = this.getById(dictTypeId);
        if (dictType == null) {
            return false;
        }

        // 删除关联的字典数据
        sysDictDataService.deleteDictDataByType(dictType.getDictType());

        // 删除字典类型
        return this.removeById(dictTypeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public boolean deleteDictTypes(Long[] dictTypeIds) {
        if (dictTypeIds == null || dictTypeIds.length == 0) {
            return false;
        }

        // 依次删除每个字典类型及其关联的字典数据
        for (Long dictTypeId : dictTypeIds) {
            deleteDictTypeById(dictTypeId);
        }

        return true;
    }

    @Override
    public boolean checkDictTypeUnique(String dictType, Long dictTypeId) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_DICT_TYPE.DICT_TYPE.eq(dictType))
                .and(SYS_DICT_TYPE.IS_DELETED.eq(0));

        if (dictTypeId != null) {
            query.and(SYS_DICT_TYPE.ID.ne(dictTypeId));
        }

        return this.count(query) == 0;
    }

    @Override
    public boolean checkDictNameUnique(String dictName, Long dictTypeId) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_DICT_TYPE.DICT_NAME.eq(dictName))
                .and(SYS_DICT_TYPE.IS_DELETED.eq(0));

        if (dictTypeId != null) {
            query.and(SYS_DICT_TYPE.ID.ne(dictTypeId));
        }

        return this.count(query) == 0;
    }

    @Override
    @CacheEvict(allEntries = true)
    public void refreshCache() {
        // 清除所有字典类型缓存
        // @CacheEvict注解会自动清除dictType缓存空间的所有数据
    }
}
