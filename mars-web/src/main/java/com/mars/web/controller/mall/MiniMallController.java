package com.mars.web.controller.mall;

import cn.dev33.satoken.stp.StpUtil;
import com.mars.common.result.PageResult;
import com.mars.common.result.Result;
import com.mars.system.entity.mall.*;
import com.mars.system.service.mall.*;
import com.mars.system.service.wechat.WechatMiniProgramService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小程序商城接口
 * 注意：小程序接口不加密响应
 *
 * @author Mars
 * @date 2026-02-03
 */
@Slf4j
@RestController
@RequestMapping("/api/mall")
@RequiredArgsConstructor
public class MiniMallController {

    private final MallMemberService memberService;
    private final MallCategoryService categoryService;
    private final MallProductService productService;
    private final MallCartService cartService;
    private final MallAddressService addressService;
    private final MallOrderService orderService;
    private final MallBannerService bannerService;
    private final MallFavoriteService favoriteService;
    private final WechatMiniProgramService wechatMiniProgramService;

    // ==================== 登录相关 ====================

    /**
     * 小程序登录（带头像昵称）
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginRequest request) {
        try {
            // 调用微信登录
            WechatMiniProgramService.MiniProgramLoginResult wxResult = 
                wechatMiniProgramService.login(request.getCode());
            
            // 查找或创建会员
            MallMember member = memberService.getByOpenId(wxResult.getOpenId());
            if (member == null) {
                // 新用户，创建并设置头像昵称
                member = memberService.createByWechat(wxResult.getOpenId(), wxResult.getUnionId());
            }
            
            // 更新头像和昵称（如果有传入）
            boolean needUpdate = false;
            MallMember updateMember = new MallMember();
            updateMember.setId(member.getId());
            
            if (request.getNickname() != null && !request.getNickname().isEmpty()) {
                updateMember.setNickname(request.getNickname());
                member.setNickname(request.getNickname());
                needUpdate = true;
            }
            if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
                updateMember.setAvatar(request.getAvatar());
                member.setAvatar(request.getAvatar());
                needUpdate = true;
            }
            
            if (needUpdate) {
                memberService.update(updateMember);
            }
            memberService.updateLastLoginTime(member.getId());
            
            // 使用 Sa-Token 登录，生成 token
            // 使用 "mall_" 前缀区分小程序会员和后台用户
            StpUtil.login("mall_" + member.getId());
            String token = StpUtil.getTokenValue();
            
            Map<String, Object> data = new HashMap<>();
            data.put("memberId", member.getId());
            data.put("openId", member.getOpenId());
            data.put("nickname", member.getNickname());
            data.put("avatar", member.getAvatar());
            data.put("phone", member.getPhone());
            data.put("points", member.getPoints());
            data.put("level", member.getLevel());
            data.put("token", token);
            
            return Result.ok(data);
        } catch (Exception e) {
            log.error("小程序登录失败", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 手机号一键登录（带头像昵称）
     * 同时传入登录code和手机号code，一步完成登录+绑定手机号+设置头像昵称
     */
    @PostMapping("/loginByPhone")
    public Result<Map<String, Object>> loginByPhone(@RequestBody PhoneLoginRequest request) {
        try {
            // 1. 调用微信登录获取openId
            WechatMiniProgramService.MiniProgramLoginResult wxResult = 
                wechatMiniProgramService.login(request.getLoginCode());
            
            // 2. 获取手机号
            String phone = wechatMiniProgramService.getPhoneNumber(request.getPhoneCode());
            
            // 3. 查找或创建会员
            MallMember member = memberService.getByOpenId(wxResult.getOpenId());
            if (member == null) {
                // 新用户，创建并绑定手机号
                member = memberService.createByWechat(wxResult.getOpenId(), wxResult.getUnionId());
                memberService.bindPhone(member.getId(), phone);
            } else {
                // 老用户，更新手机号（如果还没绑定）
                if (member.getPhone() == null || member.getPhone().isEmpty()) {
                    memberService.bindPhone(member.getId(), phone);
                }
            }
            
            // 4. 更新头像和昵称（如果有传入）
            MallMember updateMember = new MallMember();
            updateMember.setId(member.getId());
            boolean needUpdate = false;
            
            if (request.getNickname() != null && !request.getNickname().isEmpty()) {
                updateMember.setNickname(request.getNickname());
                member.setNickname(request.getNickname());
                needUpdate = true;
            }
            if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
                updateMember.setAvatar(request.getAvatar());
                member.setAvatar(request.getAvatar());
                needUpdate = true;
            }
            
            if (needUpdate) {
                memberService.update(updateMember);
            }
            memberService.updateLastLoginTime(member.getId());
            
            // 重新获取完整信息
            member = memberService.getById(member.getId());
            
            // 使用 Sa-Token 登录，生成 token
            StpUtil.login("mall_" + member.getId());
            String token = StpUtil.getTokenValue();
            
            Map<String, Object> data = new HashMap<>();
            data.put("memberId", member.getId());
            data.put("openId", member.getOpenId());
            data.put("nickname", member.getNickname());
            data.put("avatar", member.getAvatar());
            data.put("phone", member.getPhone());
            data.put("points", member.getPoints());
            data.put("level", member.getLevel());
            data.put("token", token);
            
            return Result.ok(data);
        } catch (Exception e) {
            log.error("手机号一键登录失败", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取手机号（绑定手机号用）
     */
    @PostMapping("/phone")
    public Result<Map<String, String>> getPhone(@RequestBody GetPhoneRequest request) {
        try {
            String phone = wechatMiniProgramService.getPhoneNumber(request.getCode());
            
            // 绑定手机号
            memberService.bindPhone(request.getMemberId(), phone);
            
            Map<String, String> data = new HashMap<>();
            data.put("phone", phone);
            return Result.ok(data);
        } catch (Exception e) {
            log.error("获取手机号失败", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取会员信息
     */
    @GetMapping("/member/info")
    public Result<MallMember> getMemberInfo(@RequestParam Long memberId) {
        MallMember member = memberService.getById(memberId);
        if (member != null) {
            member.setOpenId(null); // 隐藏敏感信息
            member.setUnionId(null);
        }
        return Result.ok(member);
    }

    /**
     * 更新会员信息
     */
    @PutMapping("/member/info")
    public Result<Void> updateMemberInfo(@RequestBody MallMember member) {
        // 只允许更新部分字段
        MallMember updateMember = new MallMember();
        updateMember.setId(member.getId());
        updateMember.setNickname(member.getNickname());
        updateMember.setAvatar(member.getAvatar());
        updateMember.setGender(member.getGender());
        updateMember.setBirthday(member.getBirthday());
        memberService.update(updateMember);
        return Result.ok();
    }

    // ==================== 首页数据 ====================

    /**
     * 获取首页数据
     */
    @GetMapping("/home")
    public Result<Map<String, Object>> getHomeData() {
        Map<String, Object> data = new HashMap<>();
        
        // 轮播图
        data.put("banners", bannerService.listByPosition("home"));
        
        // 分类
        data.put("categories", categoryService.listParentCategories());
        
        // 推荐商品
        data.put("recommendProducts", productService.listRecommend(10));
        
        // 热门商品
        data.put("hotProducts", productService.listHot(10));
        
        // 新品
        data.put("newProducts", productService.listNew(10));
        
        return Result.ok(data);
    }

    // ==================== 分类相关 ====================

    /**
     * 获取分类列表(树形)
     */
    @GetMapping("/category/tree")
    public Result<List<MallCategory>> getCategoryTree() {
        return Result.ok(categoryService.listTreeEnabled());
    }

    /**
     * 获取一级分类
     */
    @GetMapping("/category/list")
    public Result<List<MallCategory>> getCategoryList() {
        return Result.ok(categoryService.listParentCategories());
    }

    // ==================== 商品相关 ====================

    /**
     * 商品列表
     */
    @GetMapping("/product/list")
    public Result<PageResult<MallProduct>> getProductList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer isHot,
            @RequestParam(required = false) Integer isNew,
            @RequestParam(required = false) Integer isRecommend,
            @RequestParam(required = false) String orderBy) {
        var result = productService.pageForMini(page, pageSize, keyword, categoryId, 
                                                 isHot, isNew, isRecommend, orderBy);
        return Result.ok(PageResult.of(result));
    }

    /**
     * 商品详情
     */
    @GetMapping("/product/{id}")
    public Result<MallProduct> getProductDetail(
            @PathVariable Long id,
            @RequestParam(required = false) Long memberId) {
        return Result.ok(productService.getDetailForMini(id, memberId));
    }

    // ==================== 购物车相关 ====================

    /**
     * 购物车列表
     */
    @GetMapping("/cart/list")
    public Result<List<MallCart>> getCartList(@RequestParam Long memberId) {
        return Result.ok(cartService.listByMemberId(memberId));
    }

    /**
     * 购物车数量
     */
    @GetMapping("/cart/count")
    public Result<Integer> getCartCount(@RequestParam Long memberId) {
        return Result.ok(cartService.countByMemberId(memberId));
    }

    /**
     * 添加到购物车
     */
    @PostMapping("/cart/add")
    public Result<Void> addToCart(@RequestBody CartAddRequest request) {
        cartService.add(request.getMemberId(), request.getProductId(), 
                        request.getSkuId(), request.getQuantity());
        return Result.ok();
    }

    /**
     * 更新购物车数量
     */
    @PutMapping("/cart/quantity")
    public Result<Void> updateCartQuantity(@RequestBody CartUpdateRequest request) {
        cartService.updateQuantity(request.getMemberId(), request.getCartId(), request.getQuantity());
        return Result.ok();
    }

    /**
     * 更新购物车选中状态
     */
    @PutMapping("/cart/selected")
    public Result<Void> updateCartSelected(@RequestBody CartSelectedRequest request) {
        cartService.updateSelected(request.getMemberId(), request.getCartId(), request.getSelected());
        return Result.ok();
    }

    /**
     * 购物车全选/取消全选
     */
    @PutMapping("/cart/selectAll")
    public Result<Void> selectAllCart(@RequestBody CartSelectAllRequest request) {
        cartService.selectAll(request.getMemberId(), request.getSelected());
        return Result.ok();
    }

    /**
     * 删除购物车商品
     */
    @DeleteMapping("/cart")
    public Result<Void> deleteCart(
            @RequestParam Long memberId,
            @RequestParam Long[] cartIds) {
        cartService.delete(memberId, cartIds);
        return Result.ok();
    }

    /**
     * 获取购物车选中商品金额
     */
    @GetMapping("/cart/amount")
    public Result<BigDecimal> getCartSelectedAmount(@RequestParam Long memberId) {
        return Result.ok(cartService.calculateSelectedAmount(memberId));
    }

    // ==================== 地址相关 ====================

    /**
     * 地址列表
     */
    @GetMapping("/address/list")
    public Result<List<MallAddress>> getAddressList(@RequestParam Long memberId) {
        return Result.ok(addressService.listByMemberId(memberId));
    }

    /**
     * 地址详情
     */
    @GetMapping("/address/{id}")
    public Result<MallAddress> getAddressDetail(@PathVariable Long id) {
        return Result.ok(addressService.getById(id));
    }

    /**
     * 默认地址
     */
    @GetMapping("/address/default")
    public Result<MallAddress> getDefaultAddress(@RequestParam Long memberId) {
        return Result.ok(addressService.getDefault(memberId));
    }

    /**
     * 新增地址
     */
    @PostMapping("/address")
    public Result<Void> createAddress(@RequestBody MallAddress address) {
        addressService.create(address);
        return Result.ok();
    }

    /**
     * 修改地址
     */
    @PutMapping("/address")
    public Result<Void> updateAddress(@RequestBody MallAddress address) {
        addressService.update(address);
        return Result.ok();
    }

    /**
     * 删除地址
     */
    @DeleteMapping("/address/{id}")
    public Result<Void> deleteAddress(
            @RequestParam Long memberId,
            @PathVariable Long id) {
        addressService.delete(memberId, id);
        return Result.ok();
    }

    /**
     * 设为默认地址
     */
    @PutMapping("/address/{id}/default")
    public Result<Void> setDefaultAddress(
            @RequestParam Long memberId,
            @PathVariable Long id) {
        addressService.setDefault(memberId, id);
        return Result.ok();
    }

    // ==================== 订单相关 ====================

    /**
     * 订单列表
     */
    @GetMapping("/order/list")
    public Result<PageResult<MallOrder>> getOrderList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam Long memberId,
            @RequestParam(required = false) Integer status) {
        var result = orderService.pageForMini(page, pageSize, memberId, status);
        return Result.ok(PageResult.of(result));
    }

    /**
     * 订单详情
     */
    @GetMapping("/order/{id}")
    public Result<MallOrder> getOrderDetail(@PathVariable Long id) {
        return Result.ok(orderService.getById(id));
    }

    /**
     * 获取各状态订单数量
     */
    @GetMapping("/order/count")
    public Result<Map<String, Integer>> getOrderCount(@RequestParam Long memberId) {
        return Result.ok(orderService.countByStatus(memberId));
    }

    /**
     * 从购物车创建订单
     */
    @PostMapping("/order/fromCart")
    public Result<MallOrder> createOrderFromCart(@RequestBody CreateOrderRequest request) {
        MallOrder order = orderService.createFromCart(
            request.getMemberId(),
            request.getAddressId(),
            request.getRemark(),
            request.getCouponId()
        );
        return Result.ok(order);
    }

    /**
     * 直接购买创建订单
     */
    @PostMapping("/order/direct")
    public Result<MallOrder> createOrderDirect(@RequestBody CreateDirectOrderRequest request) {
        MallOrder order = orderService.createDirect(
            request.getMemberId(),
            request.getProductId(),
            request.getSkuId(),
            request.getQuantity(),
            request.getAddressId(),
            request.getRemark(),
            request.getCouponId()
        );
        return Result.ok(order);
    }

    /**
     * 取消订单
     */
    @PutMapping("/order/{id}/cancel")
    public Result<Void> cancelOrder(
            @RequestParam Long memberId,
            @PathVariable Long id) {
        orderService.cancel(memberId, id);
        return Result.ok();
    }

    /**
     * 支付订单
     */
    @PostMapping("/order/{id}/pay")
    public Result<Map<String, String>> payOrder(
            @PathVariable Long id,
            @RequestBody PayOrderRequest request) {
        // 从 Sa-Token 获取当前登录会员ID
        String loginId = StpUtil.getLoginIdAsString();
        Long memberId = Long.parseLong(loginId.replace("mall_", ""));
        
        Integer payType = request.getPayType() != null ? request.getPayType() : 1;
        Map<String, String> payParams = orderService.pay(memberId, id, payType);
        return Result.ok(payParams);
    }

    /**
     * 确认收货
     */
    @PutMapping("/order/{id}/receive")
    public Result<Void> receiveOrder(
            @RequestParam Long memberId,
            @PathVariable Long id) {
        orderService.receive(memberId, id);
        return Result.ok();
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/order/{id}")
    public Result<Void> deleteOrder(
            @RequestParam Long memberId,
            @PathVariable Long id) {
        orderService.delete(memberId, id);
        return Result.ok();
    }

    // ==================== 收藏相关 ====================

    /**
     * 收藏列表
     */
    @GetMapping("/favorite/list")
    public Result<PageResult<MallFavorite>> getFavoriteList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam Long memberId) {
        var result = favoriteService.pageByMemberId(page, pageSize, memberId);
        return Result.ok(PageResult.of(result));
    }

    /**
     * 添加收藏
     */
    @PostMapping("/favorite")
    public Result<Void> addFavorite(
            @RequestParam Long memberId,
            @RequestParam Long productId) {
        favoriteService.add(memberId, productId);
        return Result.ok();
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/favorite")
    public Result<Void> removeFavorite(
            @RequestParam Long memberId,
            @RequestParam Long productId) {
        favoriteService.remove(memberId, productId);
        return Result.ok();
    }

    /**
     * 切换收藏状态
     */
    @PostMapping("/favorite/toggle")
    public Result<Boolean> toggleFavorite(
            @RequestParam Long memberId,
            @RequestParam Long productId) {
        return Result.ok(favoriteService.toggle(memberId, productId));
    }

    /**
     * 收藏数量
     */
    @GetMapping("/favorite/count")
    public Result<Integer> getFavoriteCount(@RequestParam Long memberId) {
        return Result.ok(favoriteService.countByMemberId(memberId));
    }

    // ==================== 请求对象 ====================

    @Data
    public static class LoginRequest {
        /** 微信登录code */
        private String code;
        /** 用户昵称 */
        private String nickname;
        /** 用户头像 */
        private String avatar;
    }

    @Data
    public static class PhoneLoginRequest {
        /** 微信登录code */
        private String loginCode;
        /** 手机号授权code */
        private String phoneCode;
        /** 用户昵称 */
        private String nickname;
        /** 用户头像 */
        private String avatar;
    }

    @Data
    public static class GetPhoneRequest {
        private String code;
        private Long memberId;
    }

    @Data
    public static class CartAddRequest {
        private Long memberId;
        private Long productId;
        private Long skuId;
        private Integer quantity = 1;
    }

    @Data
    public static class CartUpdateRequest {
        private Long memberId;
        private Long cartId;
        private Integer quantity;
    }

    @Data
    public static class CartSelectedRequest {
        private Long memberId;
        private Long cartId;
        private Integer selected;
    }

    @Data
    public static class CartSelectAllRequest {
        private Long memberId;
        private Integer selected;
    }

    @Data
    public static class CreateOrderRequest {
        private Long memberId;
        private Long addressId;
        private String remark;
        private Long couponId;
    }

    @Data
    public static class CreateDirectOrderRequest {
        private Long memberId;
        private Long productId;
        private Long skuId;
        private Integer quantity;
        private Long addressId;
        private String remark;
        private Long couponId;
    }

    @Data
    public static class PayOrderRequest {
        /** 支付方式：1-微信支付 2-支付宝 */
        private Integer payType;
    }
}
