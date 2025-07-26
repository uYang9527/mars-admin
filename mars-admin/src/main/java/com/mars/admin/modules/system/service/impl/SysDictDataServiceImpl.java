package com.mars.admin.modules.system.service.impl;

import com.mars.admin.modules.system.entity.SysDictData;
import com.mars.admin.modules.system.mapper.SysDictDataMapper;
import com.mars.admin.modules.system.service.ISysDictDataService;
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

import java.util.Arrays;
import java.util.List;

import static com.mars.admin.modules.system.entity.table.SysDictDataTableDef.SYS_DICT_DATA;


/**
 * 系统字典数据Service实现类
 * 继承 CacheableServiceImpl 获得更多便捷方法和缓存功能
 *
 * @author Mars
 */
@Service
@CacheConfig(cacheNames = "dictData")
public class SysDictDataServiceImpl extends CacheableServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    @Override
    @Cacheable(key = "'type:' + #dictType")
    public List<SysDictData> selectByDictType(String dictType) {
        return sysDictDataMapper.selectByDictType(dictType);
    }

    @Override
    @Cacheable(key = "'typeValue:' + #dictType + ':' + #dictValue")
    public SysDictData selectByDictTypeAndValue(String dictType, String dictValue) {
        return sysDictDataMapper.selectByDictTypeAndValue(dictType, dictValue);
    }

    @Override
    public Page<SysDictData> selectDictDataPage(Page<SysDictData> page, SysDictData dictData) {
        QueryWrapper query = QueryWrapper.create()
                .select(SYS_DICT_DATA.ALL_COLUMNS)
                .from(SYS_DICT_DATA)
                .where(SYS_DICT_DATA.IS_DELETED.eq(0));

        if (StringUtils.hasText(dictData.getDictLabel())) {
            query.and(SYS_DICT_DATA.DICT_LABEL.like(dictData.getDictLabel()));
        }
        if (StringUtils.hasText(dictData.getDictValue())) {
            query.and(SYS_DICT_DATA.DICT_VALUE.like(dictData.getDictValue()));
        }
        if (StringUtils.hasText(dictData.getDictType())) {
            query.and(SYS_DICT_DATA.DICT_TYPE.eq(dictData.getDictType()));
        }
        if (dictData.getStatus() != null) {
            query.and(SYS_DICT_DATA.STATUS.eq(dictData.getStatus()));
        }

        query.orderBy(SYS_DICT_DATA.DICT_SORT.asc(), SYS_DICT_DATA.CREATE_TIME.desc());

        return this.page(page, query);
    }

    @Override
    @Cacheable(key = "'normal'")
    public List<SysDictData> selectNormalDictData() {
        return sysDictDataMapper.selectNormalDictData();
    }

    @Override
    @Cacheable(key = "'normalType:' + #dictType")
    public List<SysDictData> selectNormalDictDataByType(String dictType) {
        return sysDictDataMapper.selectNormalDictDataByType(dictType);
    }

    @Override
    @Cacheable(key = "#id")
    public SysDictData getDictDataById(Long id) {
        return super.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public boolean insertDictData(SysDictData dictData) {
        return this.save(dictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
        @CacheEvict(key = "'type:' + #dictData.dictType", condition = "#dictData.dictType != null"),
        @CacheEvict(key = "'normalType:' + #dictData.dictType", condition = "#dictData.dictType != null"),
        @CacheEvict(key = "'typeValue:' + #dictData.dictType + ':' + #dictData.dictValue",
                   condition = "#dictData.dictType != null and #dictData.dictValue != null"),
        @CacheEvict(key = "#dictData.id", condition = "#dictData.id != null"),
        @CacheEvict(key = "'normal'")
    })
    public boolean updateDictData(SysDictData dictData) {
        return this.updateById(dictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public boolean deleteDictData(Long[] dictDataIds) {
        return this.removeByIds(Arrays.asList(dictDataIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public boolean deleteDictDataByType(String dictType) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_DICT_DATA.DICT_TYPE.eq(dictType));
        return this.remove(query);
    }

    @Override
    public boolean checkDictValueUnique(String dictType, String dictValue, Long dictDataId) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_DICT_DATA.DICT_TYPE.eq(dictType))
                .and(SYS_DICT_DATA.DICT_VALUE.eq(dictValue))
                .and(SYS_DICT_DATA.IS_DELETED.eq(0));

        if (dictDataId != null) {
            query.and(SYS_DICT_DATA.ID.ne(dictDataId));
        }

        return this.count(query) == 0;
    }

    @Override
    @CacheEvict(allEntries = true)
    public void refreshCache() {
        // 清除所有字典数据缓存
        // @CacheEvict注解会自动清除dictData缓存空间的所有数据
    }
}
