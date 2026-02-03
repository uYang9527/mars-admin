"use strict";
const common_vendor = require("../../common/vendor.js");
const utils_api = require("../../utils/api.js");
const utils_cart = require("../../utils/cart.js");
const _sfc_main = {
  data() {
    return {
      statusBarHeight: 0,
      showToast: false,
      toastText: "",
      logging: false,
      // 弹窗控制
      showPopup: false,
      loginType: "wechat",
      // 'wechat' 或 'phone'
      // 用户头像和昵称
      avatar: "",
      nickname: ""
    };
  },
  onLoad() {
    const systemInfo = common_vendor.index.getSystemInfoSync();
    this.statusBarHeight = systemInfo.statusBarHeight || 0;
  },
  methods: {
    // 显示设置头像昵称弹窗
    showProfilePopup(type) {
      this.loginType = type;
      this.showPopup = true;
    },
    // 关闭弹窗
    closePopup() {
      if (this.logging)
        return;
      this.showPopup = false;
    },
    // 选择头像回调
    onChooseAvatar(e) {
      common_vendor.index.__f__("log", "at pages/login/index.vue:121", "选择头像:", e.detail.avatarUrl);
      this.avatar = e.detail.avatarUrl;
    },
    // 昵称输入完成
    onNicknameBlur(e) {
      this.nickname = e.detail.value;
    },
    // ========== 微信授权登录确认 ==========
    confirmWechatLogin() {
      if (!this.avatar) {
        this.showToastMessage("请先设置头像");
        return;
      }
      if (!this.nickname || !this.nickname.trim()) {
        this.showToastMessage("请先输入昵称");
        return;
      }
      if (this.logging)
        return;
      this.logging = true;
      this.showToastMessage("微信授权登录中...");
      common_vendor.index.login({
        provider: "weixin",
        success: async (loginRes) => {
          common_vendor.index.__f__("log", "at pages/login/index.vue:150", "wx.login code:", loginRes.code);
          try {
            const res = await utils_api.login(loginRes.code, this.nickname, this.avatar);
            if (res.code === 200 && res.data) {
              this.handleLoginSuccess(res.data);
            } else {
              this.showToastMessage(res.message || "登录失败");
            }
          } catch (e) {
            common_vendor.index.__f__("error", "at pages/login/index.vue:159", "登录失败", e);
            this.showToastMessage(e.message || "登录失败，请重试");
          } finally {
            this.logging = false;
          }
        },
        fail: (err) => {
          common_vendor.index.__f__("error", "at pages/login/index.vue:166", "wx.login失败", err);
          this.showToastMessage("获取登录凭证失败");
          this.logging = false;
        }
      });
    },
    // ========== 手机号一键登录确认 ==========
    confirmPhoneLogin(e) {
      if (!this.avatar) {
        this.showToastMessage("请先设置头像");
        return;
      }
      if (!this.nickname || !this.nickname.trim()) {
        this.showToastMessage("请先输入昵称");
        return;
      }
      if (e.detail.errMsg !== "getPhoneNumber:ok") {
        if (e.detail.errMsg.indexOf("deny") > -1 || e.detail.errMsg.indexOf("cancel") > -1) {
          this.showToastMessage("您取消了手机号授权");
        } else {
          this.showToastMessage("获取手机号失败");
        }
        return;
      }
      if (this.logging)
        return;
      this.logging = true;
      this.showToastMessage("手机号登录中...");
      const phoneCode = e.detail.code;
      common_vendor.index.login({
        provider: "weixin",
        success: async (loginRes) => {
          common_vendor.index.__f__("log", "at pages/login/index.vue:205", "wx.login code:", loginRes.code);
          common_vendor.index.__f__("log", "at pages/login/index.vue:206", "phone code:", phoneCode);
          try {
            const res = await utils_api.loginByPhone(loginRes.code, phoneCode, this.nickname, this.avatar);
            if (res.code === 200 && res.data) {
              this.handleLoginSuccess(res.data);
            } else {
              this.showToastMessage(res.message || "登录失败");
            }
          } catch (e2) {
            common_vendor.index.__f__("error", "at pages/login/index.vue:215", "手机号登录失败", e2);
            this.showToastMessage(e2.message || "登录失败，请重试");
          } finally {
            this.logging = false;
          }
        },
        fail: (err) => {
          common_vendor.index.__f__("error", "at pages/login/index.vue:222", "wx.login失败", err);
          this.showToastMessage("获取登录凭证失败");
          this.logging = false;
        }
      });
    },
    // 登录成功统一处理
    handleLoginSuccess(data) {
      common_vendor.index.setStorageSync("memberInfo", data);
      common_vendor.index.setStorageSync("token", data.token);
      this.showToastMessage("登录成功");
      this.showPopup = false;
      utils_cart.updateCartBadge();
      setTimeout(() => {
        common_vendor.index.switchTab({ url: "/pages/index/index" });
      }, 500);
    },
    // 账号密码登录（跳转到密码登录页）
    passwordLogin() {
      common_vendor.index.navigateTo({ url: "/pages/login/password" });
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
  return common_vendor.e({
    a: $data.statusBarHeight + "px",
    b: common_vendor.o(($event) => $options.showProfilePopup("wechat")),
    c: $data.logging,
    d: common_vendor.o(($event) => $options.showProfilePopup("phone")),
    e: $data.logging,
    f: common_vendor.o((...args) => $options.passwordLogin && $options.passwordLogin(...args)),
    g: $data.showPopup ? 1 : "",
    h: common_vendor.o((...args) => $options.closePopup && $options.closePopup(...args)),
    i: common_vendor.o((...args) => $options.closePopup && $options.closePopup(...args)),
    j: $data.avatar || "/static/default-avatar.png",
    k: common_vendor.o((...args) => $options.onChooseAvatar && $options.onChooseAvatar(...args)),
    l: common_vendor.o((...args) => $options.onNicknameBlur && $options.onNicknameBlur(...args)),
    m: $data.nickname,
    n: common_vendor.o(($event) => $data.nickname = $event.detail.value),
    o: $data.loginType === "wechat"
  }, $data.loginType === "wechat" ? {
    p: common_vendor.t($data.logging ? "登录中..." : "确认登录"),
    q: common_vendor.o((...args) => $options.confirmWechatLogin && $options.confirmWechatLogin(...args)),
    r: $data.logging
  } : {
    s: common_vendor.t($data.logging ? "登录中..." : "授权手机号并登录"),
    t: common_vendor.o((...args) => $options.confirmPhoneLogin && $options.confirmPhoneLogin(...args)),
    v: $data.logging
  }, {
    w: $data.showPopup ? 1 : "",
    x: common_vendor.t($data.toastText),
    y: $data.showToast ? 1 : ""
  });
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-d08ef7d4"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/login/index.js.map
