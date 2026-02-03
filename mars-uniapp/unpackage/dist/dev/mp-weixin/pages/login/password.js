"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      statusBarHeight: 0,
      phone: "",
      password: "",
      remember: false,
      showPassword: false,
      phoneFocus: false,
      passwordFocus: false,
      showToast: false,
      toastText: ""
    };
  },
  onLoad() {
    const systemInfo = common_vendor.index.getSystemInfoSync();
    this.statusBarHeight = systemInfo.statusBarHeight || 0;
  },
  methods: {
    goBack() {
      common_vendor.index.navigateBack();
    },
    doLogin() {
      if (!this.phone) {
        this.showToastMessage("请输入手机号");
        return;
      }
      if (!this.password) {
        this.showToastMessage("请输入密码");
        return;
      }
      this.showToastMessage("登录成功");
      setTimeout(() => {
        common_vendor.index.switchTab({ url: "/pages/index/index" });
      }, 1e3);
    },
    forgotPassword() {
      common_vendor.index.showToast({ title: "忘记密码", icon: "none" });
    },
    goRegister() {
      common_vendor.index.showToast({ title: "立即注册", icon: "none" });
    },
    showToastMessage(text) {
      this.toastText = text;
      this.showToast = true;
      setTimeout(() => {
        this.showToast = false;
      }, 2e3);
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.statusBarHeight + "px",
    b: common_vendor.o((...args) => $options.goBack && $options.goBack(...args)),
    c: common_vendor.o(($event) => $data.phoneFocus = true),
    d: common_vendor.o(($event) => $data.phoneFocus = false),
    e: $data.phone,
    f: common_vendor.o(($event) => $data.phone = $event.detail.value),
    g: $data.phoneFocus ? 1 : "",
    h: $data.showPassword ? "text" : "password",
    i: common_vendor.o(($event) => $data.passwordFocus = true),
    j: common_vendor.o(($event) => $data.passwordFocus = false),
    k: $data.password,
    l: common_vendor.o(($event) => $data.password = $event.detail.value),
    m: common_vendor.n($data.showPassword ? "fas fa-eye" : "far fa-eye-slash"),
    n: common_vendor.o(($event) => $data.showPassword = !$data.showPassword),
    o: $data.passwordFocus ? 1 : "",
    p: $data.remember,
    q: common_vendor.o(($event) => $data.remember = !$data.remember),
    r: common_vendor.o((...args) => $options.forgotPassword && $options.forgotPassword(...args)),
    s: common_vendor.o((...args) => $options.doLogin && $options.doLogin(...args)),
    t: common_vendor.o((...args) => $options.goRegister && $options.goRegister(...args)),
    v: common_vendor.t($data.toastText),
    w: $data.showToast ? 1 : ""
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-26a2d353"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/login/password.js.map
