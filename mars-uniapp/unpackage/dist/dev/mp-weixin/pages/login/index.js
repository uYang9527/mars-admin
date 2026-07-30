"use strict";
const common_vendor = require("../../common/vendor.js");
const utils_api = require("../../utils/api.js");
const utils_request = require("../../utils/request.js");
const utils_auth = require("../../utils/auth.js");
const utils_websocket = require("../../utils/websocket.js");
const common_assets = require("../../common/assets.js");
const _sfc_main = {
  data() {
    return {
      loading: false,
      confirming: false,
      showPhoneLogin: false,
      showProfilePopup: false,
      phone: "",
      smsCode: "",
      codeCountdown: 0,
      codeTimer: null,
      chosenAvatar: "",
      chosenNickname: "",
      pendingLoginData: null
    };
  },
  onUnload() {
    if (this.codeTimer)
      clearInterval(this.codeTimer);
  },
  methods: {
    async handleWxLogin() {
      if (this.loading)
        return;
      this.loading = true;
      try {
        const loginRes = await new Promise((resolve, reject) => {
          common_vendor.index.login({ provider: "weixin", success: resolve, fail: reject });
        });
        const res = await utils_api.wxLogin({ wxCode: loginRes.code, loginType: "MINIPROGRAM" });
        this.pendingLoginData = res.data;
        this.chosenNickname = res.data.nickname || "";
        this.chosenAvatar = res.data.avatar || "";
        this.showProfilePopup = true;
      } catch (err) {
        common_vendor.index.__f__("error", "at pages/login/index.vue:137", "登录失败:", err);
        common_vendor.index.showToast({ title: "登录失败，请重试", icon: "none" });
      } finally {
        this.loading = false;
      }
    },
    onChooseAvatar(e) {
      this.chosenAvatar = e.detail.avatarUrl;
    },
    async confirmLogin() {
      if (this.confirming)
        return;
      if (!this.chosenNickname.trim()) {
        common_vendor.index.showToast({ title: "请输入昵称", icon: "none" });
        return;
      }
      this.confirming = true;
      try {
        const data = this.pendingLoginData;
        utils_auth.setToken(data.token);
        const nickname = this.chosenNickname.trim();
        let avatarUrl = data.avatar || "";
        if (this.chosenAvatar && this.chosenAvatar !== data.avatar) {
          if (this.chosenAvatar.startsWith("https://") || this.chosenAvatar.startsWith("http://thirdwx.") || this.chosenAvatar.startsWith("https://thirdwx.")) {
            avatarUrl = this.chosenAvatar;
          } else if (this.chosenAvatar.startsWith("http://tmp/") || this.chosenAvatar.startsWith("wxfile://")) {
            try {
              const uploadRes = await utils_api.uploadAvatar(this.chosenAvatar);
              const url = uploadRes.data || "";
              avatarUrl = url.startsWith("http") ? url : url ? utils_request.BASE_URL + url : avatarUrl;
            } catch (e) {
              common_vendor.index.__f__("warn", "at pages/login/index.vue:173", "头像上传失败，使用选择的头像:", e);
              avatarUrl = this.chosenAvatar;
            }
          } else {
            avatarUrl = this.chosenAvatar;
          }
        }
        try {
          await utils_api.updateAppProfile({ nickname, avatar: avatarUrl });
        } catch (e) {
          common_vendor.index.__f__("warn", "at pages/login/index.vue:186", "更新个人资料失败:", e);
        }
        utils_auth.setUserInfo({
          userId: data.userId,
          username: nickname,
          nickname,
          avatar: avatarUrl
        });
        utils_websocket.wsClient.connect();
        this.showProfilePopup = false;
        common_vendor.index.showToast({ title: "登录成功", icon: "success" });
        setTimeout(() => {
          common_vendor.index.switchTab({ url: "/pages/index/index" });
        }, 500);
      } catch (err) {
        common_vendor.index.__f__("error", "at pages/login/index.vue:200", "确认登录失败:", err);
      } finally {
        this.confirming = false;
      }
    },
    async handleSendCode() {
      if (this.codeCountdown > 0)
        return;
      if (!this.phone || !/^1[3-9]\d{9}$/.test(this.phone)) {
        common_vendor.index.showToast({ title: "请输入正确的手机号", icon: "none" });
        return;
      }
      try {
        await utils_api.sendSmsCode({ phone: this.phone });
        common_vendor.index.showToast({ title: "验证码已发送", icon: "success" });
        this.codeCountdown = 60;
        this.codeTimer = setInterval(() => {
          this.codeCountdown--;
          if (this.codeCountdown <= 0)
            clearInterval(this.codeTimer);
        }, 1e3);
      } catch (err) {
        common_vendor.index.__f__("error", "at pages/login/index.vue:217", "发送验证码失败:", err);
      }
    },
    async handlePhoneLogin() {
      if (this.loading)
        return;
      if (!this.phone || !/^1[3-9]\d{9}$/.test(this.phone)) {
        common_vendor.index.showToast({ title: "请输入正确的手机号", icon: "none" });
        return;
      }
      if (!this.smsCode || this.smsCode.length < 4) {
        common_vendor.index.showToast({ title: "请输入验证码", icon: "none" });
        return;
      }
      this.loading = true;
      try {
        const res = await utils_api.wxLogin({ phone: this.phone, smsCode: this.smsCode, loginType: "SMS" });
        this.pendingLoginData = res.data;
        this.chosenNickname = res.data.nickname || "";
        this.chosenAvatar = res.data.avatar || "";
        this.showProfilePopup = true;
      } catch (err) {
        common_vendor.index.__f__("error", "at pages/login/index.vue:235", "登录失败:", err);
      } finally {
        this.loading = false;
      }
    }
  }
};
if (!Array) {
  const _easycom_u_icon2 = common_vendor.resolveComponent("u-icon");
  _easycom_u_icon2();
}
const _easycom_u_icon = () => "../../node-modules/uview-plus/components/u-icon/u-icon.js";
if (!Math) {
  _easycom_u_icon();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: common_assets._imports_0,
    b: common_vendor.p({
      name: "weixin-fill",
      color: "#FFFFFF",
      size: "20"
    }),
    c: common_vendor.o((...args) => $options.handleWxLogin && $options.handleWxLogin(...args)),
    d: $data.loading,
    e: common_vendor.t($data.showPhoneLogin ? "返回微信登录" : "手机号登录"),
    f: common_vendor.o(($event) => $data.showPhoneLogin = !$data.showPhoneLogin),
    g: $data.showPhoneLogin
  }, $data.showPhoneLogin ? {
    h: common_vendor.p({
      name: "phone",
      color: "#BBB",
      size: "18"
    }),
    i: $data.phone,
    j: common_vendor.o(($event) => $data.phone = $event.detail.value),
    k: common_vendor.p({
      name: "lock",
      color: "#BBB",
      size: "18"
    }),
    l: $data.smsCode,
    m: common_vendor.o(($event) => $data.smsCode = $event.detail.value),
    n: common_vendor.t($data.codeCountdown > 0 ? `${$data.codeCountdown}s` : "获取验证码"),
    o: $data.codeCountdown > 0 ? 1 : "",
    p: common_vendor.o((...args) => $options.handleSendCode && $options.handleSendCode(...args)),
    q: common_vendor.o((...args) => $options.handlePhoneLogin && $options.handlePhoneLogin(...args)),
    r: $data.loading
  } : {}, {
    s: $data.showProfilePopup
  }, $data.showProfilePopup ? {
    t: common_vendor.o(($event) => $data.showProfilePopup = false)
  } : {}, {
    v: common_vendor.p({
      name: "close",
      color: "#999",
      size: "18"
    }),
    w: common_vendor.o(($event) => $data.showProfilePopup = false),
    x: $data.chosenAvatar || "/static/default-avatar.png",
    y: common_vendor.p({
      name: "camera-fill",
      color: "#FFF",
      size: "12"
    }),
    z: common_vendor.o((...args) => $options.onChooseAvatar && $options.onChooseAvatar(...args)),
    A: $data.chosenNickname,
    B: common_vendor.o(($event) => $data.chosenNickname = $event.detail.value),
    C: common_vendor.o((...args) => $options.confirmLogin && $options.confirmLogin(...args)),
    D: $data.confirming,
    E: $data.showProfilePopup ? 1 : ""
  });
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-d08ef7d4"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/login/index.js.map
