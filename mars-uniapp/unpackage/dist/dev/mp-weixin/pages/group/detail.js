"use strict";
const common_vendor = require("../../common/vendor.js");
const utils_api = require("../../utils/api.js");
const utils_auth = require("../../utils/auth.js");
const _sfc_main = {
  data() {
    return { groupId: 0, group: {}, members: [], showAllMembers: false, myUserId: 0 };
  },
  computed: {
    isOwner() {
      return this.group.ownerId === this.myUserId;
    },
    isOwnerOrAdmin() {
      const me = this.members.find((m) => m.userId === this.myUserId);
      return me && me.role >= 1;
    },
    displayMembers() {
      return this.showAllMembers ? this.members : this.members.slice(0, 15);
    }
  },
  onLoad(options) {
    var _a;
    this.groupId = Number(options.groupId);
    this.myUserId = ((_a = utils_auth.getUserInfo()) == null ? void 0 : _a.userId) || 0;
    this.loadGroupInfo();
  },
  methods: {
    async loadGroupInfo() {
      try {
        const [groupRes, membersRes] = await Promise.all([utils_api.getGroupDetail(this.groupId), utils_api.getGroupMembers(this.groupId)]);
        if (groupRes.data)
          this.group = groupRes.data;
        if (membersRes.data)
          this.members = membersRes.data;
      } catch (err) {
        common_vendor.index.__f__("error", "at pages/group/detail.vue:80", "加载群信息失败:", err);
      }
    },
    handleMemberTap(member) {
      if (member.userId === this.myUserId)
        return;
      common_vendor.index.showActionSheet({
        itemList: this.isOwnerOrAdmin ? ["发送消息", "设为管理员", "移出群聊"] : ["发送消息"],
        success: (res) => {
          if (res.tapIndex === 0)
            common_vendor.index.navigateTo({ url: `/pages/chat/index?targetId=${member.userId}&name=${encodeURIComponent(member.userNickname || member.nickname)}&avatar=${encodeURIComponent(member.avatar || "")}` });
        }
      });
    },
    handleAddMember() {
      common_vendor.index.showToast({ title: "添加成员功能开发中", icon: "none" });
    },
    editAnnouncement() {
      common_vendor.index.showModal({
        title: "编辑群公告",
        editable: true,
        placeholderText: "请输入群公告",
        content: this.group.announcement || "",
        success: async (res) => {
          if (res.confirm && res.content !== void 0) {
            try {
              await utils_api.updateGroup({ id: this.groupId, announcement: res.content });
              this.group.announcement = res.content;
              common_vendor.index.showToast({ title: "公告已更新", icon: "success" });
            } catch (err) {
              common_vendor.index.__f__("error", "at pages/group/detail.vue:97", "更新公告失败:", err);
            }
          }
        }
      });
    },
    handleClearHistory() {
      common_vendor.index.showModal({ title: "提示", content: "确定要清空聊天记录吗？", success: (res) => {
        if (res.confirm)
          common_vendor.index.showToast({ title: "已清空", icon: "success" });
      } });
    },
    handleQuitGroup() {
      common_vendor.index.showModal({
        title: "退出群聊",
        content: "确定要退出该群聊吗？",
        confirmColor: "#FA5151",
        success: async (res) => {
          if (res.confirm) {
            try {
              await utils_api.quitGroup(this.groupId);
              common_vendor.index.showToast({ title: "已退出群聊", icon: "success" });
              setTimeout(() => common_vendor.index.navigateBack({ delta: 2 }), 500);
            } catch (err) {
            }
          }
        }
      });
    },
    handleDissolveGroup() {
      common_vendor.index.showModal({
        title: "解散群聊",
        content: "确定要解散该群聊吗？此操作不可撤销。",
        confirmColor: "#FA5151",
        success: async (res) => {
          if (res.confirm) {
            try {
              await utils_api.dissolveGroup(this.groupId);
              common_vendor.index.showToast({ title: "群聊已解散", icon: "success" });
              setTimeout(() => common_vendor.index.navigateBack({ delta: 2 }), 500);
            } catch (err) {
            }
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
  return common_vendor.e({
    a: $data.group.avatar || "/static/default-avatar.png",
    b: common_vendor.t($data.group.name),
    c: common_vendor.t($data.members.length),
    d: common_vendor.t($data.showAllMembers ? "收起" : "查看全部"),
    e: common_vendor.o(($event) => $data.showAllMembers = !$data.showAllMembers),
    f: common_vendor.f($options.displayMembers, (member, k0, i0) => {
      return common_vendor.e({
        a: member.avatar || "/static/default-avatar.png",
        b: common_vendor.t(member.nickname || member.userNickname),
        c: member.role === 2
      }, member.role === 2 ? {} : member.role === 1 ? {} : {}, {
        d: member.role === 1,
        e: member.id,
        f: common_vendor.o(($event) => $options.handleMemberTap(member), member.id)
      });
    }),
    g: $options.isOwnerOrAdmin
  }, $options.isOwnerOrAdmin ? {
    h: common_vendor.p({
      name: "plus",
      color: "#999",
      size: "36"
    }),
    i: common_vendor.o((...args) => $options.handleAddMember && $options.handleAddMember(...args))
  } : {}, {
    j: $options.isOwnerOrAdmin
  }, $options.isOwnerOrAdmin ? {
    k: common_vendor.o((...args) => $options.editAnnouncement && $options.editAnnouncement(...args))
  } : {}, {
    l: common_vendor.t($data.group.announcement || "暂无群公告"),
    m: common_vendor.p({
      name: "trash",
      color: "#666",
      size: "32"
    }),
    n: common_vendor.o((...args) => $options.handleClearHistory && $options.handleClearHistory(...args)),
    o: !$options.isOwner
  }, !$options.isOwner ? {
    p: common_vendor.p({
      name: "minus-circle",
      color: "#FA5151",
      size: "32"
    }),
    q: common_vendor.o((...args) => $options.handleQuitGroup && $options.handleQuitGroup(...args))
  } : {}, {
    r: $options.isOwner
  }, $options.isOwner ? {
    s: common_vendor.p({
      name: "close-circle",
      color: "#FA5151",
      size: "32"
    }),
    t: common_vendor.o((...args) => $options.handleDissolveGroup && $options.handleDissolveGroup(...args))
  } : {});
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-858a584d"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/group/detail.js.map
