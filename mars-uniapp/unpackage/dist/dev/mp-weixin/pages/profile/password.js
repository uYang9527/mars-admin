"use strict";
const common_vendor = require("../../common/vendor.js");
const utils_api = require("../../utils/api.js");
const utils_auth = require("../../utils/auth.js");
const utils_websocket = require("../../utils/websocket.js");
const _sfc_main = {
  data() {
    return {
      form: {
        oldPassword: "",
        newPassword: "",
        confirmPassword: ""
      },
      showOld: false,
      showNew: false,
      showConfirm: false,
      submitting: false
    };
  },
  computed: {
    passwordStrength() {
      const pwd = this.form.newPassword;
      if (!pwd)
        return 0;
      let strength = 0;
      if (pwd.length >= 6)
        strength++;
      if (/[A-Z]/.test(pwd) && /[a-z]/.test(pwd))
        strength++;
      if (/\d/.test(pwd) && /[^A-Za-z0-9]/.test(pwd))
        strength++;
      return strength;
    },
    strengthText() {
      const map = { 0: "", 1: "弱", 2: "中", 3: "强" };
      return map[this.passwordStrength] || "";
    },
    strengthClass() {
      const map = { 1: "text-weak", 2: "text-medium", 3: "text-strong" };
      return map[this.passwordStrength] || "";
    }
  },
  methods: {
    validate() {
      if (!this.form.oldPassword) {
        common_vendor.index.showToast({ title: "请输入原密码", icon: "none" });
        return false;
      }
      if (!this.form.newPassword) {
        common_vendor.index.showToast({ title: "请输入新密码", icon: "none" });
        return false;
      }
      if (this.form.newPassword.length < 6) {
        common_vendor.index.showToast({ title: "新密码至少6位", icon: "none" });
        return false;
      }
      if (this.form.newPassword === this.form.oldPassword) {
        common_vendor.index.showToast({ title: "新密码不能与原密码相同", icon: "none" });
        return false;
      }
      if (this.form.newPassword !== this.form.confirmPassword) {
        common_vendor.index.showToast({ title: "两次输入的密码不一致", icon: "none" });
        return false;
      }
      return true;
    },
    async handleSubmit() {
      if (!this.validate())
        return;
      this.submitting = true;
      try {
        await utils_api.changeAppPassword({
          oldPassword: this.form.oldPassword,
          newPassword: this.form.newPassword
        });
        common_vendor.index.showModal({
          title: "修改成功",
          content: "密码已修改，请重新登录",
          showCancel: false,
          success: () => {
            utils_websocket.wsClient.close();
            utils_auth.clearAuth();
            common_vendor.index.reLaunch({ url: "/pages/login/index" });
          }
        });
      } catch (e) {
        common_vendor.index.showToast({ title: e.message || "修改失败", icon: "none" });
      } finally {
        this.submitting = false;
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
    a: common_vendor.p({
      name: "info-circle",
      color: "#1890FF",
      size: "15"
    }),
    b: $data.showOld ? "text" : "password",
    c: $data.form.oldPassword,
    d: common_vendor.o(($event) => $data.form.oldPassword = $event.detail.value),
    e: common_vendor.p({
      name: $data.showOld ? "eye" : "eye-off",
      color: "#999",
      size: "18"
    }),
    f: common_vendor.o(($event) => $data.showOld = !$data.showOld),
    g: $data.showNew ? "text" : "password",
    h: $data.form.newPassword,
    i: common_vendor.o(($event) => $data.form.newPassword = $event.detail.value),
    j: common_vendor.p({
      name: $data.showNew ? "eye" : "eye-off",
      color: "#999",
      size: "18"
    }),
    k: common_vendor.o(($event) => $data.showNew = !$data.showNew),
    l: $data.showConfirm ? "text" : "password",
    m: $data.form.confirmPassword,
    n: common_vendor.o(($event) => $data.form.confirmPassword = $event.detail.value),
    o: common_vendor.p({
      name: $data.showConfirm ? "eye" : "eye-off",
      color: "#999",
      size: "18"
    }),
    p: common_vendor.o(($event) => $data.showConfirm = !$data.showConfirm),
    q: $data.form.newPassword
  }, $data.form.newPassword ? {
    r: $options.passwordStrength >= 1 ? 1 : "",
    s: $options.passwordStrength === 1 ? 1 : "",
    t: $options.passwordStrength >= 2 ? 1 : "",
    v: $options.passwordStrength === 2 ? 1 : "",
    w: $options.passwordStrength >= 3 ? 1 : "",
    x: $options.passwordStrength >= 3 ? 1 : "",
    y: common_vendor.t($options.strengthText),
    z: common_vendor.n($options.strengthClass)
  } : {}, {
    A: common_vendor.t($data.submitting ? "提交中..." : "确认修改"),
    B: $data.submitting,
    C: common_vendor.o((...args) => $options.handleSubmit && $options.handleSubmit(...args))
  });
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-6fcd4848"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/profile/password.js.map
