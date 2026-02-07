package com.mars.mall.service;

import com.mars.mall.entity.MallAddress;

import java.util.List;

/**
 * 收货地址 Service
 *
 * @author Mars
 * @date 2026-02-03
 */
public interface MallAddressService {

    /**
     * 查询会员地址列表
     */
    List<MallAddress> listByMemberId(Long memberId);

    /**
     * 根据ID查询
     */
    MallAddress getById(Long id);

    /**
     * 获取默认地址
     */
    MallAddress getDefault(Long memberId);

    /**
     * 新增
     */
    void create(MallAddress address);

    /**
     * 修改
     */
    void update(MallAddress address);

    /**
     * 删除
     */
    void delete(Long memberId, Long id);

    /**
     * 设为默认地址
     */
    void setDefault(Long memberId, Long id);
}
