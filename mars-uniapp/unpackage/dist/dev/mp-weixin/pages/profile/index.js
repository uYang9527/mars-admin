"use strict";
const common_vendor = require("../../common/vendor.js");
const utils_api = require("../../utils/api.js");
const _sfc_main = {
  data() {
    return {
      statusBarHeight: 0,
      isLoggedIn: false,
      userInfo: {
        name: "请登录",
        avatar: "/static/default-avatar.png"
      },
      stats: [
        {
          icon: "fas fa-ticket-alt",
          label: "优惠券",
          value: "0",
          color: "#059669",
          bg: "linear-gradient(135deg, rgba(5, 150, 105, 0.1), rgba(5, 150, 105, 0.05))"
        },
        {
          icon: "fas fa-star",
          label: "积分",
          value: "0",
          color: "#f59e0b",
          bg: "linear-gradient(135deg, rgba(245, 158, 11, 0.1), rgba(245, 158, 11, 0.05))"
        },
        {
          icon: "fas fa-coins",
          label: "余额",
          value: "¥0.00",
          color: "#ef4444",
          bg: "linear-gradient(135deg, rgba(239, 68, 68, 0.1), rgba(239, 68, 68, 0.05))"
        },
        {
          icon: "fas fa-wallet",
          label: "钱包",
          value: "",
          color: "#6366f1",
          bg: "linear-gradient(135deg, rgba(99, 102, 241, 0.1), rgba(99, 102, 241, 0.05))"
        }
      ],
      orders: [
        { id: 0, icon: "far fa-credit-card", label: "待付款", badge: 0 },
        { id: 1, icon: "fas fa-box", label: "待发货", badge: 0 },
        { id: 2, icon: "fas fa-shipping-fast", label: "待收货", badge: 0 },
        { id: 3, icon: "far fa-comment-dots", label: "已完成", badge: 0 }
      ],
      menus1: [
        {
          id: 1,
          icon: "fas fa-map-marker-alt",
          label: "收货地址",
          color: "#059669",
          bg: "linear-gradient(135deg, rgba(5, 150, 105, 0.1), rgba(5, 150, 105, 0.05))",
          url: "/pages/address/list"
        },
        {
          id: 2,
          icon: "fas fa-heart",
          label: "我的收藏",
          color: "#ef4444",
          bg: "linear-gradient(135deg, rgba(239, 68, 68, 0.1), rgba(239, 68, 68, 0.05))",
          url: "/pages/favorite/index"
        }
      ],
      menus2: [
        {
          id: 3,
          icon: "fas fa-headset",
          label: "客服中心",
          color: "#3b82f6",
          bg: "linear-gradient(135deg, rgba(59, 130, 246, 0.1), rgba(59, 130, 246, 0.05))"
        },
        {
          id: 4,
          icon: "fas fa-cog",
          label: "设置",
          color: "#6b7280",
          bg: "linear-gradient(135deg, rgba(107, 114, 128, 0.1), rgba(107, 114, 128, 0.05))",
          url: "/pages/settings/index"
        }
      ]
    };
  },
  onLoad() {
    const systemInfo = common_vendor.index.getSystemInfoSync();
    this.statusBarHeight = systemInfo.statusBarHeight || 0;
  },
  onShow() {
    this.checkLoginAndLoad();
  },
  methods: {
    // 检查登录状态并加载数据
    async checkLoginAndLoad() {
      const memberInfo = common_vendor.index.getStorageSync("memberInfo");
      this.isLoggedIn = !!(memberInfo && memberInfo.memberId);
      if (this.isLoggedIn) {
        this.userInfo = {
          name: memberInfo.nickname || "用户",
          avatar: memberInfo.avatar || "/static/default-avatar.png"
        };
        this.stats[1].value = memberInfo.points || "0";
        this.loadMemberInfo();
        this.loadOrderCount();
      } else {
        this.userInfo = {
          name: "请登录",
          avatar: "/static/default-avatar.png"
        };
      }
    },
    // 加载会员信息
    async loadMemberInfo() {
      try {
        const res = await utils_api.getMemberInfo();
        if (res.code === 200 && res.data) {
          this.userInfo.name = res.data.nickname || "用户";
          this.userInfo.avatar = res.data.avatar || "/static/default-avatar.png";
          this.stats[1].value = res.data.points || "0";
          this.stats[2].value = `¥${res.data.balance || "0.00"}`;
          const stored = common_vendor.index.getStorageSync("memberInfo");
          common_vendor.index.setStorageSync("memberInfo", { ...stored, ...res.data });
        }
      } catch (e) {
        common_vendor.index.__f__("error", "at pages/profile/index.vue:212", "加载会员信息失败", e);
      }
    },
    // 加载订单数量
    async loadOrderCount() {
      try {
        const res = await utils_api.getOrderCount();
        if (res.code === 200 && res.data) {
          this.orders[0].badge = res.data.pendingPay || 0;
          this.orders[1].badge = res.data.pendingShip || 0;
          this.orders[2].badge = res.data.pendingReceive || 0;
          this.orders[3].badge = res.data.completed || 0;
        }
      } catch (e) {
        common_vendor.index.__f__("error", "at pages/profile/index.vue:230", "加载订单数量失败", e);
      }
    },
    goSettings() {
      common_vendor.index.navigateTo({ url: "/pages/settings/index" });
    },
    goProfile() {
      if (!this.isLoggedIn) {
        common_vendor.index.navigateTo({ url: "/pages/login/index" });
        return;
      }
      common_vendor.index.showToast({ title: "编辑资料", icon: "none" });
    },
    goOrders() {
      if (!this.isLoggedIn) {
        common_vendor.index.navigateTo({ url: "/pages/login/index" });
        return;
      }
      common_vendor.index.navigateTo({ url: "/pages/orders/index" });
    },
    handleStat(stat) {
      if (!this.isLoggedIn) {
        common_vendor.index.navigateTo({ url: "/pages/login/index" });
        return;
      }
      common_vendor.index.showToast({ title: stat.label, icon: "none" });
    },
    handleOrder(order) {
      if (!this.isLoggedIn) {
        common_vendor.index.navigateTo({ url: "/pages/login/index" });
        return;
      }
      common_vendor.index.navigateTo({ url: `/pages/orders/index?status=${order.id}` });
    },
    handleMenu(menu) {
      if (menu.url) {
        if (!this.isLoggedIn && menu.id !== 4 && menu.id !== 3) {
          common_vendor.index.navigateTo({ url: "/pages/login/index" });
          return;
        }
        common_vendor.index.navigateTo({ url: menu.url });
      } else {
        common_vendor.index.showToast({ title: menu.label, icon: "none" });
      }
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.statusBarHeight + "px",
    b: $data.userInfo.avatar,
    c: common_vendor.t($data.userInfo.name),
    d: common_vendor.o((...args) => $options.goProfile && $options.goProfile(...args)),
    e: common_vendor.o((...args) => $options.goOrders && $options.goOrders(...args)),
    f: common_vendor.f($data.orders, (order, k0, i0) => {
      return common_vendor.e({
        a: common_vendor.n(order.icon),
        b: order.badge
      }, order.badge ? {
        c: common_vendor.t(order.badge)
      } : {}, {
        d: order.badge ? 1 : "",
        e: common_vendor.t(order.label),
        f: order.id,
        g: common_vendor.o(($event) => $options.handleOrder(order), order.id)
      });
    }),
    g: common_vendor.f($data.menus1, (menu, index, i0) => {
      return {
        a: common_vendor.n(menu.icon),
        b: menu.color,
        c: menu.bg,
        d: common_vendor.t(menu.label),
        e: index,
        f: common_vendor.o(($event) => $options.handleMenu(menu), index)
      };
    }),
    h: common_vendor.f($data.menus2, (menu, index, i0) => {
      return {
        a: common_vendor.n(menu.icon),
        b: menu.color,
        c: menu.bg,
        d: common_vendor.t(menu.label),
        e: index,
        f: common_vendor.o(($event) => $options.handleMenu(menu), index)
      };
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-201c0da5"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/profile/index.js.map
