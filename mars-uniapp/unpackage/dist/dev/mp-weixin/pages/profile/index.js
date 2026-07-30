"use strict";
const common_vendor = require("../../common/vendor.js");
const utils_api = require("../../utils/api.js");
const utils_auth = require("../../utils/auth.js");
const utils_websocket = require("../../utils/websocket.js");
const _sfc_main = {
  data() {
    return {
      userInfo: {},
      cacheSize: "0 KB",
      statusBarHeight: 20
    };
  },
  onLoad() {
    const sysInfo = common_vendor.index.getSystemInfoSync();
    this.statusBarHeight = sysInfo.statusBarHeight || 20;
  },
  onShow() {
    this.userInfo = utils_auth.getUserInfo() || {};
    this.loadProfile();
    this.calcCacheSize();
  },
  methods: {
    async loadProfile() {
      try {
        const res = await utils_api.getAppProfile();
        if (res.data) {
          this.userInfo = { ...this.userInfo, ...res.data };
          utils_auth.setUserInfo(this.userInfo);
        }
      } catch (err) {
      }
    },
    calcCacheSize() {
      try {
        const info = common_vendor.index.getStorageInfoSync();
        const s = info.currentSize || 0;
        this.cacheSize = s > 1024 ? (s / 1024).toFixed(1) + " MB" : s + " KB";
      } catch (e) {
        this.cacheSize = "0 KB";
      }
    },
    handleChangeAvatar() {
      common_vendor.index.chooseImage({
        count: 1,
        sizeType: ["compressed"],
        success: async (res) => {
          common_vendor.index.showLoading({ title: "上传中..." });
          try {
            const r = await utils_api.uploadAvatar(res.tempFilePaths[0]);
            const url = r.data || "";
            await utils_api.updateAppProfile({ avatar: url });
            this.userInfo.avatar = url;
            utils_auth.setUserInfo(this.userInfo);
            common_vendor.index.showToast({ title: "头像已更新", icon: "success" });
          } catch (e) {
            common_vendor.index.showToast({ title: "上传失败", icon: "none" });
          } finally {
            common_vendor.index.hideLoading();
          }
        }
      });
    },
    handleEditProfile() {
      common_vendor.index.navigateTo({ url: "/pages/profile/edit" });
    },
    handleChangePassword() {
      common_vendor.index.navigateTo({ url: "/pages/profile/password" });
    },
    handleBlacklist() {
      common_vendor.index.showToast({ title: "功能开发中", icon: "none" });
    },
    handleClearCache() {
      common_vendor.index.showModal({
        title: "清除缓存",
        content: `当前缓存 ${this.cacheSize}，确定清除？`,
        success: (res) => {
          if (res.confirm) {
            const t = common_vendor.index.getStorageSync("token");
            const u = common_vendor.index.getStorageSync("userInfo");
            common_vendor.index.clearStorageSync();
            if (t)
              common_vendor.index.setStorageSync("token", t);
            if (u)
              common_vendor.index.setStorageSync("userInfo", u);
            this.calcCacheSize();
            common_vendor.index.showToast({ title: "已清除", icon: "success" });
          }
        }
      });
    },
    handleAbout() {
      common_vendor.index.showModal({
        title: "Mars办公",
        content: "v1.0.0\n高效沟通，智慧办公",
        showCancel: false
      });
    },
    handleLogout() {
      common_vendor.index.showModal({
        title: "退出登录",
        content: "确定要退出当前账号吗？",
        confirmColor: "#FA5151",
        success: async (res) => {
          if (res.confirm) {
            try {
              await utils_api.logout().catch(() => {
              });
            } catch (e) {
            }
            utils_websocket.wsClient.close();
            utils_auth.clearAuth();
            common_vendor.index.reLaunch({ url: "/pages/login/index" });
          }
        }
      });
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
  return {
    a: $data.statusBarHeight + "px",
    b: $data.userInfo.avatar || "/static/default-avatar.png",
    c: common_vendor.p({
      name: "camera-fill",
      color: "#FFF",
      size: "11"
    }),
    d: common_vendor.o((...args) => $options.handleChangeAvatar && $options.handleChangeAvatar(...args)),
    e: common_vendor.t($data.userInfo.nickname || "未设置昵称"),
    f: common_vendor.t($data.userInfo.userId || "--"),
    g: common_vendor.p({
      name: "account",
      color: "#07C160",
      size: "18"
    }),
    h: common_vendor.p({
      name: "arrow-right",
      color: "#CCCCCC",
      size: "14"
    }),
    i: common_vendor.o((...args) => $options.handleEditProfile && $options.handleEditProfile(...args)),
    j: common_vendor.p({
      name: "lock",
      color: "#1890FF",
      size: "18"
    }),
    k: common_vendor.p({
      name: "arrow-right",
      color: "#CCCCCC",
      size: "14"
    }),
    l: common_vendor.o((...args) => $options.handleChangePassword && $options.handleChangePassword(...args)),
    m: common_vendor.p({
      name: "minus-circle",
      color: "#FAAD14",
      size: "18"
    }),
    n: common_vendor.p({
      name: "arrow-right",
      color: "#CCCCCC",
      size: "14"
    }),
    o: common_vendor.o((...args) => $options.handleBlacklist && $options.handleBlacklist(...args)),
    p: common_vendor.p({
      name: "trash",
      color: "#999",
      size: "18"
    }),
    q: common_vendor.t($data.cacheSize),
    r: common_vendor.p({
      name: "arrow-right",
      color: "#CCCCCC",
      size: "14"
    }),
    s: common_vendor.o((...args) => $options.handleClearCache && $options.handleClearCache(...args)),
    t: common_vendor.p({
      name: "info-circle",
      color: "#722ED1",
      size: "18"
    }),
    v: common_vendor.p({
      name: "arrow-right",
      color: "#CCCCCC",
      size: "14"
    }),
    w: common_vendor.o((...args) => $options.handleAbout && $options.handleAbout(...args)),
    x: common_vendor.o((...args) => $options.handleLogout && $options.handleLogout(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-201c0da5"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/profile/index.js.map
