"use strict";
const common_vendor = require("../../common/vendor.js");
const utils_api = require("../../utils/api.js");
const utils_auth = require("../../utils/auth.js");
const _sfc_main = {
  data() {
    return { users: [], groups: [], searchText: "", loading: false, refreshing: false };
  },
  computed: {
    filteredUsers() {
      if (!this.searchText)
        return this.users;
      const kw = this.searchText.toLowerCase();
      return this.users.filter(
        (u) => u.nickname && u.nickname.toLowerCase().includes(kw) || u.username && u.username.toLowerCase().includes(kw)
      );
    }
  },
  onShow() {
    if (!utils_auth.checkLogin())
      return;
    this.loadData();
  },
  methods: {
    getFirstChar(name) {
      if (!name)
        return "?";
      return name.charAt(0).toUpperCase();
    },
    getAvatarColor(name) {
      const colors = ["#25B7D3", "#F56C6C", "#E6A23C", "#67C23A", "#409EFF", "#9B59B6", "#1ABC9C", "#E74C3C", "#3498DB", "#2ECC71"];
      if (!name)
        return colors[0];
      let hash = 0;
      for (let i = 0; i < name.length; i++)
        hash = name.charCodeAt(i) + ((hash << 5) - hash);
      return colors[Math.abs(hash) % colors.length];
    },
    async loadData() {
      this.loading = true;
      try {
        const [usersRes, groupsRes] = await Promise.all([
          utils_api.getChatUsers().catch(() => ({ data: [] })),
          utils_api.getGroupList().catch(() => ({ data: [] }))
        ]);
        if (usersRes.data && Array.isArray(usersRes.data))
          this.users = usersRes.data;
        if (groupsRes.data && Array.isArray(groupsRes.data))
          this.groups = groupsRes.data;
      } catch (err) {
        common_vendor.index.__f__("error", "at pages/contacts/index.vue:99", err);
      } finally {
        this.loading = false;
        this.refreshing = false;
      }
    },
    onRefresh() {
      this.refreshing = true;
      this.loadData();
    },
    handleSearch() {
    },
    openChat(user) {
      common_vendor.index.navigateTo({ url: `/pages/chat/index?targetId=${user.id}&name=${encodeURIComponent(user.nickname || user.username)}&avatar=${encodeURIComponent(user.avatar || "")}` });
    },
    openGroupChat(group) {
      common_vendor.index.navigateTo({ url: `/pages/group-chat/index?groupId=${group.id}&name=${encodeURIComponent(group.name)}` });
    },
    navigateToCreateGroup() {
      common_vendor.index.navigateTo({ url: "/pages/group/create" });
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
      name: "search",
      color: "rgba(255,255,255,0.55)",
      size: "15"
    }),
    b: common_vendor.o([($event) => $data.searchText = $event.detail.value, (...args) => $options.handleSearch && $options.handleSearch(...args)]),
    c: $data.searchText,
    d: common_vendor.p({
      name: "arrow-right",
      color: "#CCC",
      size: "14"
    }),
    e: common_vendor.o((...args) => $options.navigateToCreateGroup && $options.navigateToCreateGroup(...args)),
    f: $options.filteredUsers.length > 0
  }, $options.filteredUsers.length > 0 ? {
    g: common_vendor.t($options.filteredUsers.length)
  } : {}, {
    h: common_vendor.f($options.filteredUsers, (user, k0, i0) => {
      return common_vendor.e({
        a: user.avatar
      }, user.avatar ? {
        b: user.avatar
      } : {
        c: common_vendor.t($options.getFirstChar(user.nickname || user.username)),
        d: $options.getAvatarColor(user.nickname || user.username)
      }, {
        e: common_vendor.t(user.nickname || user.username),
        f: user.id,
        g: common_vendor.o(($event) => $options.openChat(user), user.id)
      });
    }),
    i: $data.groups.length > 0
  }, $data.groups.length > 0 ? {
    j: common_vendor.t($data.groups.length)
  } : {}, {
    k: common_vendor.f($data.groups, (group, k0, i0) => {
      return common_vendor.e({
        a: group.avatar
      }, group.avatar ? {
        b: group.avatar
      } : {
        c: common_vendor.t($options.getFirstChar(group.name)),
        d: $options.getAvatarColor(group.name)
      }, {
        e: common_vendor.t(group.name),
        f: group.memberCount
      }, group.memberCount ? {
        g: common_vendor.t(group.memberCount)
      } : {}, {
        h: "g-" + group.id,
        i: common_vendor.o(($event) => $options.openGroupChat(group), "g-" + group.id)
      });
    }),
    l: $options.filteredUsers.length === 0 && $data.groups.length === 0 && !$data.loading
  }, $options.filteredUsers.length === 0 && $data.groups.length === 0 && !$data.loading ? {
    m: common_vendor.p({
      name: "account",
      color: "#D0D0D0",
      size: "48"
    })
  } : {}, {
    n: $data.refreshing,
    o: common_vendor.o((...args) => $options.onRefresh && $options.onRefresh(...args))
  });
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-822bb6ce"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/contacts/index.js.map
