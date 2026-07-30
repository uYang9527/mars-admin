"use strict";
const common_vendor = require("../../common/vendor.js");
const utils_api = require("../../utils/api.js");
const _sfc_main = {
  data() {
    return { groupName: "", users: [], selectedIds: [], searchText: "", loading: false, creating: false };
  },
  computed: {
    filteredUsers() {
      if (!this.searchText)
        return this.users;
      const kw = this.searchText.toLowerCase();
      return this.users.filter((u) => u.nickname && u.nickname.toLowerCase().includes(kw) || u.username && u.username.toLowerCase().includes(kw));
    },
    canCreate() {
      return this.groupName.trim() && this.selectedIds.length >= 2;
    }
  },
  onLoad() {
    this.loadUsers();
  },
  methods: {
    async loadUsers() {
      this.loading = true;
      try {
        const res = await utils_api.getChatUsers();
        if (res.data && Array.isArray(res.data))
          this.users = res.data;
      } catch (err) {
        common_vendor.index.__f__("error", "at pages/group/create.vue:61", "加载用户列表失败:", err);
      } finally {
        this.loading = false;
      }
    },
    isSelected(id) {
      return this.selectedIds.includes(id);
    },
    toggleSelect(user) {
      const idx = this.selectedIds.indexOf(user.id);
      if (idx > -1)
        this.selectedIds.splice(idx, 1);
      else
        this.selectedIds.push(user.id);
    },
    async handleCreate() {
      if (!this.canCreate || this.creating)
        return;
      this.creating = true;
      try {
        const res = await utils_api.createGroup({ name: this.groupName.trim(), memberIds: this.selectedIds });
        common_vendor.index.showToast({ title: "群聊创建成功", icon: "success" });
        setTimeout(() => {
          const g = res.data;
          common_vendor.index.redirectTo({ url: `/pages/group-chat/index?groupId=${g.id}&name=${encodeURIComponent(g.name)}` });
        }, 500);
      } catch (err) {
        common_vendor.index.__f__("error", "at pages/group/create.vue:74", "创建群聊失败:", err);
      } finally {
        this.creating = false;
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
    a: $data.groupName,
    b: common_vendor.o(($event) => $data.groupName = $event.detail.value),
    c: common_vendor.t($data.selectedIds.length),
    d: common_vendor.p({
      name: "search",
      color: "#999",
      size: "28"
    }),
    e: $data.searchText,
    f: common_vendor.o(($event) => $data.searchText = $event.detail.value),
    g: common_vendor.f($options.filteredUsers, (user, k0, i0) => {
      return common_vendor.e({
        a: $options.isSelected(user.id)
      }, $options.isSelected(user.id) ? {
        b: "0261f0b8-1-" + i0,
        c: common_vendor.p({
          name: "checkmark",
          color: "#FFFFFF",
          size: "24"
        })
      } : {}, {
        d: $options.isSelected(user.id) ? 1 : "",
        e: user.avatar || "/static/default-avatar.png",
        f: common_vendor.t(user.nickname || user.username),
        g: user.id,
        h: common_vendor.o(($event) => $options.toggleSelect(user), user.id)
      });
    }),
    h: $options.filteredUsers.length === 0 && !$data.loading
  }, $options.filteredUsers.length === 0 && !$data.loading ? {} : {}, {
    i: common_vendor.t($data.selectedIds.length > 0 ? ` (${$data.selectedIds.length})` : ""),
    j: !$options.canCreate ? 1 : "",
    k: !$options.canCreate,
    l: $data.creating,
    m: common_vendor.o((...args) => $options.handleCreate && $options.handleCreate(...args))
  });
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-0261f0b8"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/group/create.js.map
