"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      statusBarHeight: 0,
      address: {
        name: "Lisa Wong",
        phone: "138****8888",
        detail: "浙江省 杭州市 西湖区 某某街道 101号"
      },
      orderItems: [
        { image: "https://images.unsplash.com/photo-1550258987-190a2d41a8ba?auto=format&fit=crop&w=100", price: 29.9 },
        { image: "https://images.unsplash.com/photo-1601004890684-d8cbf643f5f2?auto=format&fit=crop&w=100", price: 45 }
      ],
      paymentMethods: [
        { id: 1, name: "微信支付", icon: "fab fa-weixin", color: "#07c160" },
        { id: 2, name: "支付宝", icon: "fab fa-alipay", color: "#1677ff" }
      ],
      payment: 2
    };
  },
  computed: {
    subtotal() {
      return this.orderItems.reduce((sum, item) => sum + item.price, 0).toFixed(1);
    }
  },
  onLoad() {
    const systemInfo = common_vendor.index.getSystemInfoSync();
    this.statusBarHeight = systemInfo.statusBarHeight || 0;
  },
  methods: {
    goBack() {
      common_vendor.index.navigateBack();
    },
    selectAddress() {
      common_vendor.index.showToast({ title: "选择收货地址", icon: "none" });
    },
    selectPayment(pay) {
      this.payment = pay.id;
      common_vendor.index.showToast({ title: `已选择${pay.name}`, icon: "none" });
    },
    doPay() {
      common_vendor.index.navigateTo({ url: "/pages/order/success" });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.statusBarHeight + "px",
    b: common_vendor.o((...args) => $options.goBack && $options.goBack(...args)),
    c: common_vendor.t($data.address.name),
    d: common_vendor.t($data.address.phone),
    e: common_vendor.t($data.address.detail),
    f: common_vendor.o((...args) => $options.selectAddress && $options.selectAddress(...args)),
    g: common_vendor.f($data.orderItems, (item, index, i0) => {
      return {
        a: index,
        b: item.image
      };
    }),
    h: common_vendor.t($data.orderItems.length),
    i: common_vendor.t($options.subtotal),
    j: common_vendor.f($data.paymentMethods, (pay, index, i0) => {
      return {
        a: common_vendor.n(pay.icon),
        b: pay.color,
        c: common_vendor.t(pay.name),
        d: common_vendor.n($data.payment === pay.id ? "fas fa-check-circle checked" : "far fa-circle"),
        e: index,
        f: common_vendor.o(($event) => $options.selectPayment(pay), index)
      };
    }),
    k: common_vendor.t($data.orderItems.length),
    l: common_vendor.t($options.subtotal),
    m: common_vendor.o((...args) => $options.doPay && $options.doPay(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-17a44f9d"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/order/index.js.map
