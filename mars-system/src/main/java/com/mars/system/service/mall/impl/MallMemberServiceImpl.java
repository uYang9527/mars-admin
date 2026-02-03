package com.mars.system.service.mall.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.system.entity.mall.MallMember;
import com.mars.system.mapper.mall.MallMemberMapper;
import com.mars.system.service.mall.MallMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商城会员 Service 实现
 *
 * @author Mars
 * @date 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class MallMemberServiceImpl implements MallMemberService {

    private final MallMemberMapper memberMapper;

    @Override
    public Page<MallMember> page(Integer page, Integer pageSize, String phone, String nickname) {
        Page<MallMember> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<MallMember> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(phone)) {
            wrapper.like(MallMember::getPhone, phone);
        }
        if (StringUtils.hasText(nickname)) {
            wrapper.like(MallMember::getNickname, nickname);
        }
        wrapper.orderByDesc(MallMember::getId);
        return memberMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public MallMember getById(Long id) {
        return memberMapper.selectById(id);
    }

    @Override
    public MallMember getByOpenId(String openId) {
        return memberMapper.selectOne(
            new LambdaQueryWrapper<MallMember>().eq(MallMember::getOpenId, openId)
        );
    }

    @Override
    public MallMember getByPhone(String phone) {
        return memberMapper.selectOne(
            new LambdaQueryWrapper<MallMember>().eq(MallMember::getPhone, phone)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MallMember createByWechat(String openId, String unionId) {
        MallMember member = new MallMember();
        member.setOpenId(openId);
        member.setUnionId(unionId);
        member.setNickname("微信用户");
        member.setPoints(0);
        member.setBalance(BigDecimal.ZERO);
        member.setLevel(1);
        member.setStatus(1);
        member.setLastLoginTime(LocalDateTime.now());
        memberMapper.insert(member);
        return member;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MallMember member) {
        memberMapper.updateById(member);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLastLoginTime(Long memberId) {
        MallMember member = new MallMember();
        member.setId(memberId);
        member.setLastLoginTime(LocalDateTime.now());
        memberMapper.updateById(member);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindPhone(Long memberId, String phone) {
        // 检查手机号是否已被绑定
        MallMember existMember = getByPhone(phone);
        if (existMember != null && !existMember.getId().equals(memberId)) {
            throw new RuntimeException("该手机号已被其他账号绑定");
        }
        
        MallMember member = new MallMember();
        member.setId(memberId);
        member.setPhone(phone);
        memberMapper.updateById(member);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPoints(Long memberId, Integer points) {
        memberMapper.update(null, new LambdaUpdateWrapper<MallMember>()
            .eq(MallMember::getId, memberId)
            .setSql("points = points + " + points)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deductPoints(Long memberId, Integer points) {
        MallMember member = getById(memberId);
        if (member.getPoints() < points) {
            throw new RuntimeException("积分不足");
        }
        memberMapper.update(null, new LambdaUpdateWrapper<MallMember>()
            .eq(MallMember::getId, memberId)
            .setSql("points = points - " + points)
        );
    }
}
