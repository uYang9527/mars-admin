package com.mars.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.mall.entity.MallMember;

/**
 * 商城会员 Service
 *
 * @author Mars
 * @date 2026-02-03
 */
public interface MallMemberService {

    /**
     * 分页查询
     */
    Page<MallMember> page(Integer page, Integer pageSize, String phone, String nickname);

    /**
     * 根据ID查询
     */
    MallMember getById(Long id);

    /**
     * 根据openId查询
     */
    MallMember getByOpenId(String openId);

    /**
     * 根据手机号查询
     */
    MallMember getByPhone(String phone);

    /**
     * 创建会员(小程序登录时)
     */
    MallMember createByWechat(String openId, String unionId);

    /**
     * 更新会员信息
     */
    void update(MallMember member);

    /**
     * 更新最后登录时间
     */
    void updateLastLoginTime(Long memberId);

    /**
     * 绑定手机号
     */
    void bindPhone(Long memberId, String phone);

    /**
     * 增加积分
     */
    void addPoints(Long memberId, Integer points);

    /**
     * 扣减积分
     */
    void deductPoints(Long memberId, Integer points);
}
