"use strict";
const common_vendor = require("../../common/vendor.js");
const utils_api = require("../../utils/api.js");
const utils_auth = require("../../utils/auth.js");
const utils_websocket = require("../../utils/websocket.js");
const _sfc_main = {
  data() {
    return {
      conversations: [],
      groups: [],
      loading: false,
      refreshing: false,
      showAddMenu: false,
      userInfo: null
    };
  },
  onShow() {
    if (!utils_auth.checkLogin())
      return;
    this.userInfo = utils_auth.getUserInfo();
    this.loadData();
    this.setupWebSocket();
  },
  onHide() {
    this.removeWebSocketListeners();
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
    // 按时间倒序排列会话
    sortConversations() {
      this.conversations.sort((a, b) => {
        const ta = a.sendTime ? new Date(a.sendTime).getTime() : 0;
        const tb = b.sendTime ? new Date(b.sendTime).getTime() : 0;
        return tb - ta;
      });
    },
    async loadData() {
      var _a;
      this.loading = true;
      try {
        const [contactsRes, groupsRes, usersRes] = await Promise.all([
          utils_api.getRecentContacts().catch(() => ({ data: [] })),
          utils_api.getGroupList().catch(() => ({ data: [] })),
          utils_api.getChatUsers().catch(() => ({ data: [] }))
        ]);
        const userMap = {};
        if (usersRes.data && Array.isArray(usersRes.data)) {
          usersRes.data.forEach((u) => {
            userMap[String(u.id)] = u;
          });
        }
        const userId = (_a = this.userInfo) == null ? void 0 : _a.userId;
        if (contactsRes.data && Array.isArray(contactsRes.data)) {
          const oldUnreadMap = {};
          this.conversations.forEach((c) => {
            if (c.unreadCount > 0)
              oldUnreadMap[String(c.contactId)] = c.unreadCount;
          });
          this.conversations = contactsRes.data.map((msg) => {
            const isMe = String(msg.senderId) === String(userId);
            const otherId = isMe ? msg.receiverId : msg.senderId;
            const otherUser = userMap[String(otherId)] || {};
            const otherName = isMe ? msg.receiverName || msg.receiverNickname || otherUser.nickname || otherUser.username || "" : msg.senderName || "";
            const otherAvatar = isMe ? msg.receiverAvatar || otherUser.avatar || "" : msg.senderAvatar || "";
            return {
              contactId: otherId,
              nickname: otherName,
              avatar: otherAvatar,
              lastMessage: msg.msgType === 2 ? "[图片]" : msg.content,
              sendTime: msg.sendTime,
              unreadCount: oldUnreadMap[String(otherId)] || 0
            };
          });
          this.sortConversations();
        }
        if (groupsRes.data && Array.isArray(groupsRes.data))
          this.groups = groupsRes.data;
      } catch (err) {
        common_vendor.index.__f__("error", "at pages/index/index.vue:166", err);
      } finally {
        this.loading = false;
        this.refreshing = false;
      }
    },
    setupWebSocket() {
      this.removeWebSocketListeners();
      this.chatHandler = (data) => {
        const senderId = data.senderId;
        const senderName = data.senderName || "";
        const senderAvatar = data.senderAvatar || "";
        const content = data.msgType === 2 ? "[图片]" : data.content || "";
        const now = (/* @__PURE__ */ new Date()).toISOString();
        const idx = this.conversations.findIndex((c) => String(c.contactId) === String(senderId));
        if (idx > -1) {
          const conv = this.conversations[idx];
          conv.lastMessage = content;
          conv.sendTime = now;
          conv.unreadCount = (conv.unreadCount || 0) + 1;
          if (senderName && !conv.nickname)
            conv.nickname = senderName;
          if (senderAvatar && !conv.avatar)
            conv.avatar = senderAvatar;
          this.conversations.splice(idx, 1);
          this.conversations.unshift(conv);
        } else {
          this.conversations.unshift({
            contactId: senderId,
            nickname: senderName,
            avatar: senderAvatar,
            lastMessage: content,
            sendTime: now,
            unreadCount: 1
          });
        }
        this.conversations = [...this.conversations];
        common_vendor.index.vibrateShort();
      };
      this.groupChatHandler = (data) => {
        const groupId = data.groupId;
        const content = data.msgType === 2 ? "[图片]" : data.content || "";
        const now = (/* @__PURE__ */ new Date()).toISOString();
        const idx = this.groups.findIndex((g) => String(g.id) === String(groupId));
        if (idx > -1) {
          this.groups[idx].lastMessage = content;
          this.groups[idx].lastMessageTime = now;
          this.groups[idx].unreadCount = (this.groups[idx].unreadCount || 0) + 1;
          this.groups = [...this.groups];
        }
        common_vendor.index.vibrateShort();
      };
      utils_websocket.wsClient.on("chat", this.chatHandler);
      utils_websocket.wsClient.on("groupChat", this.groupChatHandler);
    },
    removeWebSocketListeners() {
      if (this.chatHandler) {
        utils_websocket.wsClient.off("chat", this.chatHandler);
        this.chatHandler = null;
      }
      if (this.groupChatHandler) {
        utils_websocket.wsClient.off("groupChat", this.groupChatHandler);
        this.groupChatHandler = null;
      }
    },
    onRefresh() {
      this.refreshing = true;
      this.loadData();
    },
    loadMore() {
    },
    openChat(item) {
      item.unreadCount = 0;
      this.conversations = [...this.conversations];
      common_vendor.index.navigateTo({ url: `/pages/chat/index?targetId=${item.contactId}&name=${encodeURIComponent(item.nickname || "聊天")}&avatar=${encodeURIComponent(item.avatar || "")}` });
    },
    openGroupChat(group) {
      group.unreadCount = 0;
      this.groups = [...this.groups];
      common_vendor.index.navigateTo({ url: `/pages/group-chat/index?groupId=${group.id}&name=${encodeURIComponent(group.name)}` });
    },
    handleCreateGroup() {
      this.showAddMenu = false;
      common_vendor.index.navigateTo({ url: "/pages/group/create" });
    },
    navigateToSearch() {
      common_vendor.index.showToast({ title: "搜索功能开发中", icon: "none" });
    },
    handleLongPress(item) {
      common_vendor.index.showActionSheet({
        itemList: ["删除会话", "标记已读"],
        success: (res) => {
          if (res.tapIndex === 0) {
            this.conversations = this.conversations.filter((c) => c.contactId !== item.contactId);
          } else if (res.tapIndex === 1) {
            item.unreadCount = 0;
            this.conversations = [...this.conversations];
          }
        }
      });
    },
    formatTime(time) {
      if (!time)
        return "";
      const d = new Date(time), now = /* @__PURE__ */ new Date(), diff = now - d;
      if (diff < 6e4)
        return "刚刚";
      if (diff < 36e5)
        return Math.floor(diff / 6e4) + "分钟前";
      if (diff < 864e5)
        return `${String(d.getHours()).padStart(2, "0")}:${String(d.getMinutes()).padStart(2, "0")}`;
      if (diff < 1728e5)
        return "昨天";
      if (diff < 6048e5)
        return "周" + ["日", "一", "二", "三", "四", "五", "六"][d.getDay()];
      return `${d.getMonth() + 1}/${d.getDate()}`;
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
    b: common_vendor.o((...args) => $options.navigateToSearch && $options.navigateToSearch(...args)),
    c: common_vendor.p({
      name: "plus-circle",
      color: "rgba(255,255,255,0.9)",
      size: "22"
    }),
    d: common_vendor.o(($event) => $data.showAddMenu = !$data.showAddMenu),
    e: $data.showAddMenu
  }, $data.showAddMenu ? {
    f: common_vendor.o((...args) => $options.handleCreateGroup && $options.handleCreateGroup(...args)),
    g: common_vendor.o(() => {
    }),
    h: common_vendor.o(($event) => $data.showAddMenu = false)
  } : {}, {
    i: common_vendor.f($data.conversations, (item, k0, i0) => {
      return common_vendor.e({
        a: item.avatar
      }, item.avatar ? {
        b: item.avatar
      } : {
        c: common_vendor.t($options.getFirstChar(item.nickname || item.username)),
        d: $options.getAvatarColor(item.nickname || item.username)
      }, {
        e: item.unreadCount > 0
      }, item.unreadCount > 0 ? {
        f: common_vendor.t(item.unreadCount > 99 ? "99+" : item.unreadCount)
      } : {}, {
        g: common_vendor.t(item.nickname || item.username),
        h: common_vendor.t($options.formatTime(item.sendTime)),
        i: common_vendor.t(item.lastMessage),
        j: "c-" + item.contactId,
        k: common_vendor.o(($event) => $options.openChat(item), "c-" + item.contactId),
        l: common_vendor.o(($event) => $options.handleLongPress(item), "c-" + item.contactId)
      });
    }),
    j: common_vendor.f($data.groups, (group, k0, i0) => {
      return common_vendor.e({
        a: group.avatar
      }, group.avatar ? {
        b: group.avatar
      } : {
        c: common_vendor.t($options.getFirstChar(group.name)),
        d: $options.getAvatarColor(group.name)
      }, {
        e: group.unreadCount > 0
      }, group.unreadCount > 0 ? {
        f: common_vendor.t(group.unreadCount > 99 ? "99+" : group.unreadCount)
      } : {}, {
        g: common_vendor.t(group.name),
        h: common_vendor.t($options.formatTime(group.lastMessageTime)),
        i: common_vendor.t(group.lastMessage || "暂无消息"),
        j: "g-" + group.id,
        k: common_vendor.o(($event) => $options.openGroupChat(group), "g-" + group.id)
      });
    }),
    k: $data.conversations.length === 0 && $data.groups.length === 0 && !$data.loading
  }, $data.conversations.length === 0 && $data.groups.length === 0 && !$data.loading ? {
    l: common_vendor.p({
      name: "chat",
      color: "#D0D0D0",
      size: "56"
    })
  } : {}, {
    m: common_vendor.o((...args) => $options.loadMore && $options.loadMore(...args)),
    n: $data.refreshing,
    o: common_vendor.o((...args) => $options.onRefresh && $options.onRefresh(...args))
  });
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-1cf27b2a"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/index/index.js.map
