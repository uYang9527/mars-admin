package com.mars.admin.service.impl;

import com.mars.admin.entity.SysDictType;
import com.mars.admin.mapper.SysDictTypeMapper;
import com.mars.admin.service.ISysDictDataService;
import com.mars.admin.service.ISysDictTypeService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static com.mars.admin.entity.table.SysDictTypeTableDef.SYS_DICT_TYPE;

/**
 * 系统字典类型Service实现类
 * 继承 BaseServiceImpl 获得更多便捷方法
 *
 * @author Mars
 */
@Service
public class SysDictTypeServiceImpl extends BaseServiceImpl<SysDictType> implements ISysDictTypeService {

    @Autowired
    private SysDictTypeMapper sysDictTypeMapper;

    @Autowired
    private ISysDictDataService sysDictDataService;

    @Override
    public SysDictType selectByDictType(String dictType) {
        return sysDictTypeMapper.selectByDictType(dictType);
    }

    @Override
    public SysDictType selectByDictName(String dictName) {
        return sysDictTypeMapper.selectByDictName(dictName);
    }

    @Override
    public Page<SysDictType> selectDictTypePage(Page<SysDictType> page, SysDictType dictType) {
        QueryWrapper query = QueryWrapper.create()
                .select()
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
    public SysDictType selectDictTypeDetailById(Long dictTypeId) {
        return sysDictTypeMapper.selectDictTypeDetailById(dictTypeId);
    }

    @Override
    public List<SysDictType> selectNormalDictTypes() {
        return sysDictTypeMapper.selectNormalDictTypes();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertDictType(SysDictType dictType) {
        return this.save(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDictType(SysDictType dictType) {
        return this.updateById(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
} 