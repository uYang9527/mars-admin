"use strict";
Object.defineProperty(exports, Symbol.toStringTag, { value: "Module" });
const common_vendor = require("./common/vendor.js");
const utils_auth = require("./utils/auth.js");
const utils_websocket = require("./utils/websocket.js");
const utils_crypto = require("./utils/crypto.js");
if (!Math) {
  "./pages/index/index.js";
  "./pages/login/index.js";
  "./pages/chat/index.js";
  "./pages/contacts/index.js";
  "./pages/group-chat/index.js";
  "./pages/group/create.js";
  "./pages/group/detail.js";
  "./pages/profile/index.js";
  "./pages/profile/edit.js";
  "./pages/profile/password.js";
}
const _sfc_main = {
  onLaunch: function() {
    common_vendor.index.__f__("log", "at App.vue:8", "App Launch");
    utils_crypto.fetchCryptoConfig();
    if (utils_auth.isLoggedIn()) {
      utils_websocket.wsClient.connect();
    } else {
      common_vendor.index.reLaunch({ url: "/pages/login/index" });
    }
  },
  onShow: function() {
    common_vendor.index.__f__("log", "at App.vue:18", "App Show");
  },
  onHide: function() {
    common_vendor.index.__f__("log", "at App.vue:21", "App Hide");
  }
};
function createApp() {
  const app = common_vendor.createSSRApp(_sfc_main);
  app.use(common_vendor.uviewPlus);
  return {
    app
  };
}
createApp().app.mount("#app");
exports.createApp = createApp;
//# sourceMappingURL=../.sourcemap/mp-weixin/app.js.map
