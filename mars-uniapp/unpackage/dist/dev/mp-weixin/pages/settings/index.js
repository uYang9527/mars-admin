"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      statusBarHeight: 0,
      notification: true,
      accountMenus: [
        { id: 1, icon: "fas fa-user-circle", label: "个人资料" },
        { id: 2, icon: "fas fa-shield-alt", label: "账号安全" },
        { id: 3, icon: "fas fa-key", label: "修改密码" }
      ],
      aboutMenus: [
        { id: 4, icon: "fas fa-info-circle", label: "关于我们" },
        { id: 5, icon: "fas fa-file-contract", label: "用户协议" },
        { id: 6, icon: "fas fa-user-shield", label: "隐私政策" }
      ]
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
    handleMenu(menu) {
      common_vendor.index.showToast({ title: menu.label, icon: "none" });
    },
    toggleNotification(e) {
      this.notification = e.detail.value;
      common_vendor.index.showToast({ title: this.notification ? "已开启通知" : "已关闭通知", icon: "none" });
    },
    changeLanguage() {
      common_vendor.index.showToast({ title: "语言设置", icon: "none" });
    },
    clearCache() {
      common_vendor.index.showModal({
        title: "提示",
        content: "确定要清除缓存吗？",
        success: (res) => {
          if (res.confirm) {
            common_vendor.index.showToast({ title: "清除成功", icon: "success" });
          }
        }
      });
    },
    logout() {
      common_vendor.index.showModal({
        title: "提示",
        content: "确定要退出登录吗？",
        success: (res) => {
          if (res.confirm) {
            common_vendor.index.showToast({ title: "已退出登录", icon: "none" });
            setTimeout(() => {
              common_vendor.index.reLaunch({ url: "/pages/login/index" });
            }, 1e3);
          }
        }
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.statusBarHeight + "px",
    b: common_vendor.o((...args) => $options.goBack && $options.goBack(...args)),
    c: common_vendor.f($data.accountMenus, (menu, index, i0) => {
      return {
        a: common_vendor.n(menu.icon),
        b: common_vendor.t(menu.label),
        c: index,
        d: common_vendor.o(($event) => $options.handleMenu(menu), index)
      };
    }),
    d: $data.notification,
    e: common_vendor.o((...args) => $options.toggleNotification && $options.toggleNotification(...args)),
    f: common_vendor.o((...args) => $options.changeLanguage && $options.changeLanguage(...args)),
    g: common_vendor.o((...args) => $options.clearCache && $options.clearCache(...args)),
    h: common_vendor.f($data.aboutMenus, (menu, index, i0) => {
      return {
        a: common_vendor.n(menu.icon),
        b: common_vendor.t(menu.label),
        c: index,
        d: common_vendor.o(($event) => $options.handleMenu(menu), index)
      };
    }),
    i: common_vendor.o((...args) => $options.logout && $options.logout(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-a11b3e9a"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/settings/index.js.map
