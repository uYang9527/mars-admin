package com.mars.admin.service.impl;

import com.mars.admin.entity.SysDictData;
import com.mars.admin.mapper.SysDictDataMapper;
import com.mars.admin.service.ISysDictDataService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static com.mars.admin.entity.table.SysDictDataTableDef.SYS_DICT_DATA;

/**
 * 系统字典数据Service实现类
 * 继承 BaseServiceImpl 获得更多便捷方法
 *
 * @author Mars
 */
@Service
public class SysDictDataServiceImpl extends BaseServiceImpl<SysDictData> implements ISysDictDataService {

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    @Override
    public List<SysDictData> selectByDictType(String dictType) {
        return sysDictDataMapper.selectByDictType(dictType);
    }

    @Override
    public SysDictData selectByDictTypeAndValue(String dictType, String dictValue) {
        return sysDictDataMapper.selectByDictTypeAndValue(dictType, dictValue);
    }

    @Override
    public Page<SysDictData> selectDictDataPage(Page<SysDictData> page, SysDictData dictData) {
        QueryWrapper query = QueryWrapper.create()
                .select()
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
    public List<SysDictData> selectNormalDictData() {
        return sysDictDataMapper.selectNormalDictData();
    }

    @Override
    public List<SysDictData> selectNormalDictDataByType(String dictType) {
        return sysDictDataMapper.selectNormalDictDataByType(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertDictData(SysDictData dictData) {
        return this.save(dictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDictData(SysDictData dictData) {
        return this.updateById(dictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDictData(Long[] dictDataIds) {
        return this.removeByIds(Arrays.asList(dictDataIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
} 