package com.mars.system.service.mall.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mars.system.entity.mall.MallAddress;
import com.mars.system.mapper.mall.MallAddressMapper;
import com.mars.system.service.mall.MallAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收货地址 Service 实现
 *
 * @author Mars
 * @date 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class MallAddressServiceImpl implements MallAddressService {

    private final MallAddressMapper addressMapper;

    @Override
    public List<MallAddress> listByMemberId(Long memberId) {
        return addressMapper.selectList(
            new LambdaQueryWrapper<MallAddress>()
                .eq(MallAddress::getMemberId, memberId)
                .orderByDesc(MallAddress::getIsDefault)
                .orderByDesc(MallAddress::getUpdateTime)
        );
    }

    @Override
    public MallAddress getById(Long id) {
        return addressMapper.selectById(id);
    }

    @Override
    public MallAddress getDefault(Long memberId) {
        return addressMapper.selectOne(
            new LambdaQueryWrapper<MallAddress>()
                .eq(MallAddress::getMemberId, memberId)
                .eq(MallAddress::getIsDefault, 1)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(MallAddress address) {
        // 如果是第一个地址或者设为默认，则清除其他默认
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            clearDefault(address.getMemberId());
        } else {
            // 检查是否是第一个地址
            Long count = addressMapper.selectCount(
                new LambdaQueryWrapper<MallAddress>()
                    .eq(MallAddress::getMemberId, address.getMemberId())
            );
            if (count == 0) {
                address.setIsDefault(1);
            }
        }
        
        addressMapper.insert(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MallAddress address) {
        // 如果设为默认，清除其他默认
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            clearDefault(address.getMemberId());
        }
        
        addressMapper.updateById(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long memberId, Long id) {
        MallAddress address = addressMapper.selectById(id);
        if (address == null || !address.getMemberId().equals(memberId)) {
            throw new RuntimeException("地址不存在");
        }
        
        addressMapper.deleteById(id);
        
        // 如果删除的是默认地址，设置第一个为默认
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            MallAddress firstAddress = addressMapper.selectOne(
                new LambdaQueryWrapper<MallAddress>()
                    .eq(MallAddress::getMemberId, memberId)
                    .orderByDesc(MallAddress::getUpdateTime)
                    .last("LIMIT 1")
            );
            if (firstAddress != null) {
                firstAddress.setIsDefault(1);
                addressMapper.updateById(firstAddress);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long memberId, Long id) {
        // 清除其他默认
        clearDefault(memberId);
        
        // 设为默认
        addressMapper.update(null, new LambdaUpdateWrapper<MallAddress>()
            .eq(MallAddress::getId, id)
            .eq(MallAddress::getMemberId, memberId)
            .set(MallAddress::getIsDefault, 1)
        );
    }

    /**
     * 清除默认地址
     */
    private void clearDefault(Long memberId) {
        addressMapper.update(null, new LambdaUpdateWrapper<MallAddress>()
            .eq(MallAddress::getMemberId, memberId)
            .set(MallAddress::getIsDefault, 0)
        );
    }
}
