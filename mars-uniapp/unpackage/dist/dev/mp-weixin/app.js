"use strict";
Object.defineProperty(exports, Symbol.toStringTag, { value: "Module" });
const common_vendor = require("./common/vendor.js");
if (!Math) {
  "./pages/index/index.js";
  "./pages/category/index.js";
  "./pages/cart/index.js";
  "./pages/profile/index.js";
  "./pages/detail/index.js";
  "./pages/search/index.js";
  "./pages/login/index.js";
  "./pages/orders/index.js";
  "./pages/settings/index.js";
  "./pages/login/password.js";
  "./pages/order/index.js";
  "./pages/order/success.js";
}
const _sfc_main = {
  onLaunch: function() {
    common_vendor.index.__f__("log", "at App.vue:4", "App Launch");
    const systemInfo = common_vendor.index.getSystemInfoSync();
    common_vendor.index.setStorageSync("statusBarHeight", systemInfo.statusBarHeight);
    common_vendor.index.getStorageSync("token");
  },
  onShow: function() {
    common_vendor.index.__f__("log", "at App.vue:19", "App Show");
  },
  onHide: function() {
    common_vendor.index.__f__("log", "at App.vue:22", "App Hide");
  }
};
const globalData = {
  // 后端API地址 - 开发环境使用本地地址，生产环境需要改成实际域名
  baseUrl: "http://localhost:8080",
  // 用户信息
  userInfo: null,
  // 会员信息
  memberInfo: null
};
function createApp() {
  const app = common_vendor.createSSRApp(_sfc_main);
  app.config.globalProperties.$globalData = globalData;
  if (!getApp()) {
    _sfc_main.globalData = globalData;
  }
  return {
    app
  };
}
createApp().app.mount("#app");
exports.createApp = createApp;
//# sourceMappingURL=../.sourcemap/mp-weixin/app.js.map
