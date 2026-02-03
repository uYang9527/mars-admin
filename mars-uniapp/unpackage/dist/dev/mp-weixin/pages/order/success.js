"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      statusBarHeight: 0,
      amount: "74.90"
    };
  },
  onLoad() {
    const systemInfo = common_vendor.index.getSystemInfoSync();
    this.statusBarHeight = systemInfo.statusBarHeight || 0;
  },
  methods: {
    goOrders() {
      common_vendor.index.navigateTo({ url: "/pages/orders/index" });
    },
    goHome() {
      common_vendor.index.switchTab({ url: "/pages/index/index" });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.statusBarHeight + "px",
    b: common_vendor.t($data.amount),
    c: common_vendor.o((...args) => $options.goOrders && $options.goOrders(...args)),
    d: common_vendor.o((...args) => $options.goHome && $options.goHome(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-2795c576"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/order/success.js.map
