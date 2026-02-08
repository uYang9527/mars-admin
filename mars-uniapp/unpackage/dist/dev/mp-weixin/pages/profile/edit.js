"use strict";
const common_vendor = require("../../common/vendor.js");
const utils_api = require("../../utils/api.js");
const utils_auth = require("../../utils/auth.js");
const _sfc_main = {
  data() {
    return {
      form: {
        id: "",
        username: "",
        nickname: "",
        avatar: "",
        gender: 0,
        phone: "",
        email: ""
      },
      genderSheetVisible: false,
      genderActions: [
        { name: "男", value: 1 },
        { name: "女", value: 2 },
        { name: "保密", value: 0 }
      ]
    };
  },
  computed: {
    genderText() {
      const map = { 0: "保密", 1: "男", 2: "女" };
      return map[this.form.gender] || "保密";
    }
  },
  onLoad() {
    this.loadProfile();
  },
  methods: {
    async loadProfile() {
      try {
        common_vendor.index.showLoading({ title: "加载中..." });
        const res = await utils_api.getAppProfile();
        if (res.data) {
          this.form = {
            id: res.data.id || "",
            username: res.data.username || "",
            nickname: res.data.nickname || "",
            avatar: res.data.avatar || "",
            gender: res.data.gender ?? 0,
            phone: res.data.phone || "",
            email: res.data.email || ""
          };
        }
      } catch (err) {
        common_vendor.index.showToast({ title: "加载失败", icon: "none" });
      } finally {
        common_vendor.index.hideLoading();
      }
    },
    handleChangeAvatar() {
      common_vendor.index.chooseImage({
        count: 1,
        sizeType: ["compressed"],
        success: async (res) => {
          var _a;
          common_vendor.index.showLoading({ title: "上传中..." });
          try {
            const r = await utils_api.uploadFile(res.tempFilePaths[0]);
            const url = ((_a = r.data) == null ? void 0 : _a.url) || r.data;
            await utils_api.updateAppProfile({ avatar: url });
            this.form.avatar = url;
            const userInfo = utils_auth.getUserInfo() || {};
            userInfo.avatar = url;
            utils_auth.setUserInfo(userInfo);
            common_vendor.index.showToast({ title: "头像已更新", icon: "success" });
          } catch (e) {
            common_vendor.index.showToast({ title: "上传失败", icon: "none" });
          } finally {
            common_vendor.index.hideLoading();
          }
        }
      });
    },
    editNickname() {
      common_vendor.index.showModal({
        title: "修改昵称",
        editable: true,
        placeholderText: "请输入新昵称",
        content: this.form.nickname || "",
        success: async (res) => {
          if (res.confirm && res.content && res.content.trim()) {
            await this.saveField({ nickname: res.content.trim() });
            this.form.nickname = res.content.trim();
            const userInfo = utils_auth.getUserInfo() || {};
            userInfo.nickname = res.content.trim();
            utils_auth.setUserInfo(userInfo);
          }
        }
      });
    },
    showGenderPicker() {
      this.genderSheetVisible = true;
    },
    async onGenderSelect(item) {
      this.genderSheetVisible = false;
      await this.saveField({ gender: item.value });
      this.form.gender = item.value;
    },
    editPhone() {
      common_vendor.index.showModal({
        title: "修改手机号",
        editable: true,
        placeholderText: "请输入手机号",
        content: this.form.phone || "",
        success: async (res) => {
          if (res.confirm && res.content) {
            const phone = res.content.trim();
            if (phone && !/^1[3-9]\d{9}$/.test(phone)) {
              common_vendor.index.showToast({ title: "请输入正确的手机号", icon: "none" });
              return;
            }
            await this.saveField({ phone });
            this.form.phone = phone;
          }
        }
      });
    },
    editEmail() {
      common_vendor.index.showModal({
        title: "修改邮箱",
        editable: true,
        placeholderText: "请输入邮箱地址",
        content: this.form.email || "",
        success: async (res) => {
          if (res.confirm && res.content) {
            const email = res.content.trim();
            if (email && !/^[\w.-]+@[\w.-]+\.\w+$/.test(email)) {
              common_vendor.index.showToast({ title: "请输入正确的邮箱", icon: "none" });
              return;
            }
            await this.saveField({ email });
            this.form.email = email;
          }
        }
      });
    },
    async saveField(data) {
      try {
        common_vendor.index.showLoading({ title: "保存中..." });
        await utils_api.updateAppProfile(data);
        common_vendor.index.showToast({ title: "已更新", icon: "success" });
      } catch (e) {
        common_vendor.index.showToast({ title: e.message || "保存失败", icon: "none" });
        throw e;
      } finally {
        common_vendor.index.hideLoading();
      }
    }
  }
};
if (!Array) {
  const _easycom_u_icon2 = common_vendor.resolveComponent("u-icon");
  const _easycom_u_action_sheet2 = common_vendor.resolveComponent("u-action-sheet");
  (_easycom_u_icon2 + _easycom_u_action_sheet2)();
}
const _easycom_u_icon = () => "../../node-modules/uview-plus/components/u-icon/u-icon.js";
const _easycom_u_action_sheet = () => "../../node-modules/uview-plus/components/u-action-sheet/u-action-sheet.js";
if (!Math) {
  (_easycom_u_icon + _easycom_u_action_sheet)();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.form.avatar || "/static/default-avatar.png",
    b: common_vendor.p({
      name: "arrow-right",
      color: "#CCCCCC",
      size: "14"
    }),
    c: common_vendor.o((...args) => $options.handleChangeAvatar && $options.handleChangeAvatar(...args)),
    d: common_vendor.t($data.form.nickname || "未设置"),
    e: common_vendor.p({
      name: "arrow-right",
      color: "#CCCCCC",
      size: "14"
    }),
    f: common_vendor.o((...args) => $options.editNickname && $options.editNickname(...args)),
    g: common_vendor.t($options.genderText),
    h: common_vendor.p({
      name: "arrow-right",
      color: "#CCCCCC",
      size: "14"
    }),
    i: common_vendor.o((...args) => $options.showGenderPicker && $options.showGenderPicker(...args)),
    j: common_vendor.t($data.form.phone || "未设置"),
    k: common_vendor.p({
      name: "arrow-right",
      color: "#CCCCCC",
      size: "14"
    }),
    l: common_vendor.o((...args) => $options.editPhone && $options.editPhone(...args)),
    m: common_vendor.t($data.form.email || "未设置"),
    n: common_vendor.p({
      name: "arrow-right",
      color: "#CCCCCC",
      size: "14"
    }),
    o: common_vendor.o((...args) => $options.editEmail && $options.editEmail(...args)),
    p: common_vendor.t($data.form.id || "--"),
    q: common_vendor.t($data.form.username || "--"),
    r: common_vendor.o(($event) => $data.genderSheetVisible = false),
    s: common_vendor.o($options.onGenderSelect),
    t: common_vendor.p({
      show: $data.genderSheetVisible,
      actions: $data.genderActions,
      cancelText: "取消"
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-ead3e541"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/profile/edit.js.map
