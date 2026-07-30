"use strict";
const common_vendor = require("../../common/vendor.js");
const utils_api = require("../../utils/api.js");
const utils_auth = require("../../utils/auth.js");
const utils_websocket = require("../../utils/websocket.js");
const _sfc_main = {
  data() {
    return {
      targetId: 0,
      targetName: "",
      targetAvatar: "",
      myUserId: 0,
      myAvatar: "",
      myName: "",
      messages: [],
      inputText: "",
      scrollToId: "",
      page: 1,
      hasMore: true,
      loadingMore: false,
      isVoiceMode: false,
      isRecording: false,
      showEmojiPanel: false,
      showExtraPanel: false,
      emojis: ["😀", "😁", "😂", "🤣", "😃", "😄", "😅", "😆", "😉", "😊", "😋", "😎", "😍", "🥰", "😘", "😗", "😙", "😚", "🙂", "🤗", "🤔", "😐", "😑", "😶", "🙄", "😏", "😣", "😥", "😮", "🤐", "😯", "😪", "😫", "😴", "😌", "😛", "😜", "😝", "🤤", "😒", "😓", "😔", "😕", "🙃", "🤑", "😲", "😤", "😢", "😭", "😦", "😧", "😨", "😩", "🤯", "😬", "😰", "😱", "🥵", "🥶", "😳", "🤪", "😵", "😡", "😠", "🤬", "😷", "🤒", "🤕", "🤢", "🤮", "🤧", "😇", "🥳", "🥺", "🤡", "👍", "👎", "👌", "✌️", "🤞", "👋", "🙏", "💪", "❤️", "💔", "💯", "🔥", "⭐", "🎉", "🎊", "💐"]
    };
  },
  onLoad(options) {
    this.targetId = Number(options.targetId);
    this.targetName = decodeURIComponent(options.name || "聊天");
    this.targetAvatar = decodeURIComponent(options.avatar || "");
    const u = utils_auth.getUserInfo();
    this.myUserId = (u == null ? void 0 : u.userId) || 0;
    this.myAvatar = (u == null ? void 0 : u.avatar) || "";
    this.myName = (u == null ? void 0 : u.nickname) || (u == null ? void 0 : u.username) || "我";
    common_vendor.index.setNavigationBarTitle({ title: this.targetName });
    this.loadMessages();
    this.markRead();
    this.setupWebSocket();
  },
  onUnload() {
    this.removeWebSocketListeners();
  },
  methods: {
    isSelf(senderId) {
      return String(senderId) === String(this.myUserId);
    },
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
    toggleVoice() {
      this.isVoiceMode = !this.isVoiceMode;
      this.showEmojiPanel = false;
      this.showExtraPanel = false;
    },
    toggleEmoji() {
      this.showEmojiPanel = !this.showEmojiPanel;
      this.showExtraPanel = false;
      this.isVoiceMode = false;
    },
    toggleExtra() {
      this.showExtraPanel = !this.showExtraPanel;
      this.showEmojiPanel = false;
      this.isVoiceMode = false;
    },
    hideExtra() {
      this.showEmojiPanel = false;
      this.showExtraPanel = false;
    },
    insertEmoji(emoji) {
      this.inputText += emoji;
    },
    onInputFocus() {
      this.showEmojiPanel = false;
      this.showExtraPanel = false;
      this.$nextTick(() => this.scrollToBottom());
    },
    onVoiceStart() {
      this.isRecording = true;
      common_vendor.index.showToast({ title: "语音功能开发中", icon: "none" });
    },
    onVoiceEnd() {
      this.isRecording = false;
    },
    handleTakePhoto() {
      common_vendor.index.chooseImage({
        count: 1,
        sourceType: ["camera"],
        sizeType: ["compressed"],
        success: async (res) => {
          await this.sendImageFile(res.tempFilePaths[0]);
        }
      });
    },
    handleChooseFile() {
      common_vendor.index.showToast({ title: "文件功能开发中", icon: "none" });
    },
    async sendImageFile(filePath) {
      var _a;
      common_vendor.index.showLoading({ title: "发送中..." });
      try {
        const r = await utils_api.uploadFile(filePath);
        const url = ((_a = r.data) == null ? void 0 : _a.url) || r.data;
        await utils_api.sendMessage({ receiverId: this.targetId, content: url, msgType: 2 });
        this.messages.push({
          id: Date.now(),
          senderId: this.myUserId,
          senderAvatar: this.myAvatar,
          receiverId: this.targetId,
          content: url,
          msgType: 2,
          sendTime: (/* @__PURE__ */ new Date()).toISOString()
        });
        this.$nextTick(() => this.scrollToBottom());
      } catch (e) {
        common_vendor.index.showToast({ title: "发送失败", icon: "none" });
      } finally {
        common_vendor.index.hideLoading();
      }
    },
    async loadMessages() {
      try {
        const res = await utils_api.getChatHistory(this.targetId, 1, 50);
        if (res.data && res.data.list) {
          this.messages = res.data.list.reverse();
          this.hasMore = res.data.list.length >= 50;
        } else if (res.data && Array.isArray(res.data)) {
          this.messages = res.data.reverse();
          this.hasMore = res.data.length >= 50;
        }
        this.$nextTick(() => this.scrollToBottom());
      } catch (err) {
      }
    },
    async loadMoreHistory() {
      var _a;
      if (this.loadingMore || !this.hasMore)
        return;
      this.loadingMore = true;
      this.page++;
      try {
        const res = await utils_api.getChatHistory(this.targetId, this.page, 50);
        const list = ((_a = res.data) == null ? void 0 : _a.list) || res.data || [];
        if (list.length > 0) {
          this.messages = [...list.reverse(), ...this.messages];
          this.hasMore = list.length >= 50;
        } else
          this.hasMore = false;
      } catch (err) {
        this.page--;
      } finally {
        this.loadingMore = false;
      }
    },
    async handleSend() {
      const c = this.inputText.trim();
      if (!c)
        return;
      this.inputText = "";
      this.showEmojiPanel = false;
      this.showExtraPanel = false;
      const tmp = {
        id: Date.now(),
        senderId: this.myUserId,
        senderAvatar: this.myAvatar,
        receiverId: this.targetId,
        content: c,
        msgType: 1,
        sendTime: (/* @__PURE__ */ new Date()).toISOString()
      };
      this.messages.push(tmp);
      this.$nextTick(() => this.scrollToBottom());
      try {
        const res = await utils_api.sendMessage({ receiverId: this.targetId, content: c, msgType: 1 });
        const i = this.messages.findIndex((m) => m.id === tmp.id);
        if (i > -1 && res.data)
          this.messages[i] = { ...res.data };
      } catch (err) {
        common_vendor.index.showToast({ title: "发送失败", icon: "none" });
      }
    },
    handleChooseImage() {
      this.showExtraPanel = false;
      common_vendor.index.chooseImage({
        count: 9,
        sizeType: ["compressed"],
        success: async (res) => {
          for (const filePath of res.tempFilePaths) {
            await this.sendImageFile(filePath);
          }
        }
      });
    },
    previewImage(url) {
      common_vendor.index.previewImage({
        current: url,
        urls: this.messages.filter((m) => m.msgType === 2).map((m) => m.content)
      });
    },
    setupWebSocket() {
      this.chatHandler = (data) => {
        if (String(data.senderId) === String(this.targetId)) {
          this.messages.push({
            id: Date.now(),
            senderId: data.senderId,
            senderName: data.senderName,
            senderAvatar: this.targetAvatar,
            receiverId: this.myUserId,
            content: data.content,
            msgType: data.msgType || 1,
            sendTime: (/* @__PURE__ */ new Date()).toISOString()
          });
          this.$nextTick(() => this.scrollToBottom());
          this.markRead();
        }
      };
      utils_websocket.wsClient.on("chat", this.chatHandler);
    },
    removeWebSocketListeners() {
      if (this.chatHandler)
        utils_websocket.wsClient.off("chat", this.chatHandler);
    },
    markRead() {
      utils_api.markAsRead(this.targetId).catch(() => {
      });
    },
    scrollToBottom() {
      this.scrollToId = "";
      this.$nextTick(() => {
        this.scrollToId = "msg-bottom";
      });
    },
    showTimeDivider(i) {
      return i === 0 || new Date(this.messages[i].sendTime).getTime() - new Date(this.messages[i - 1].sendTime).getTime() > 3e5;
    },
    formatFullTime(t) {
      if (!t)
        return "";
      const d = new Date(t), n = /* @__PURE__ */ new Date();
      const h = String(d.getHours()).padStart(2, "0"), m = String(d.getMinutes()).padStart(2, "0");
      if (d.toDateString() === n.toDateString())
        return `${h}:${m}`;
      const y = new Date(n);
      y.setDate(y.getDate() - 1);
      if (d.toDateString() === y.toDateString())
        return `昨天 ${h}:${m}`;
      return `${d.getMonth() + 1}月${d.getDate()}日 ${h}:${m}`;
    },
    handleMsgLongPress(msg) {
      common_vendor.index.showActionSheet({
        itemList: this.isSelf(msg.senderId) ? ["复制", "撤回"] : ["复制"],
        success: (r) => {
          if (r.tapIndex === 0)
            common_vendor.index.setClipboardData({ data: msg.content });
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
    a: $data.hasMore
  }, $data.hasMore ? {
    b: common_vendor.t($data.loadingMore ? "加载中..." : "查看更多消息"),
    c: common_vendor.o((...args) => $options.loadMoreHistory && $options.loadMoreHistory(...args))
  } : {}, {
    d: common_vendor.f($data.messages, (msg, index, i0) => {
      return common_vendor.e({
        a: $options.showTimeDivider(index)
      }, $options.showTimeDivider(index) ? {
        b: common_vendor.t($options.formatFullTime(msg.sendTime))
      } : {}, {
        c: !$options.isSelf(msg.senderId)
      }, !$options.isSelf(msg.senderId) ? common_vendor.e({
        d: msg.senderAvatar || $data.targetAvatar
      }, msg.senderAvatar || $data.targetAvatar ? {
        e: msg.senderAvatar || $data.targetAvatar
      } : {
        f: common_vendor.t($options.getFirstChar(msg.senderName || $data.targetName)),
        g: $options.getAvatarColor(msg.senderName || $data.targetName)
      }, {
        h: msg.msgType === 1
      }, msg.msgType === 1 ? {
        i: common_vendor.t(msg.content)
      } : msg.msgType === 2 ? {
        k: msg.content,
        l: common_vendor.o(($event) => $options.previewImage(msg.content), msg.id)
      } : msg.msgType === 3 ? {
        n: "5a559478-0-" + i0,
        o: common_vendor.p({
          name: "attach",
          color: "#666",
          size: "18"
        }),
        p: common_vendor.t(msg.content),
        q: common_vendor.o(($event) => _ctx.previewFile(msg), msg.id)
      } : {}, {
        j: msg.msgType === 2,
        m: msg.msgType === 3,
        r: common_vendor.o(($event) => $options.handleMsgLongPress(msg), msg.id)
      }) : common_vendor.e({
        s: msg.msgType === 1
      }, msg.msgType === 1 ? {
        t: common_vendor.t(msg.content)
      } : msg.msgType === 2 ? {
        w: msg.content,
        x: common_vendor.o(($event) => $options.previewImage(msg.content), msg.id)
      } : msg.msgType === 3 ? {
        z: "5a559478-1-" + i0,
        A: common_vendor.p({
          name: "attach",
          color: "#666",
          size: "18"
        }),
        B: common_vendor.t(msg.content),
        C: common_vendor.o(($event) => _ctx.previewFile(msg), msg.id)
      } : {}, {
        v: msg.msgType === 2,
        y: msg.msgType === 3,
        D: common_vendor.o(($event) => $options.handleMsgLongPress(msg), msg.id),
        E: $data.myAvatar
      }, $data.myAvatar ? {
        F: $data.myAvatar
      } : {
        G: common_vendor.t($options.getFirstChar($data.myName)),
        H: $options.getAvatarColor($data.myName)
      }), {
        I: msg.id,
        J: "msg-" + msg.id
      });
    }),
    e: $data.scrollToId,
    f: common_vendor.o((...args) => $options.loadMoreHistory && $options.loadMoreHistory(...args)),
    g: common_vendor.o((...args) => $options.hideExtra && $options.hideExtra(...args)),
    h: common_vendor.p({
      name: $data.isVoiceMode ? "edit-pen" : "mic",
      color: "#181818",
      size: "28"
    }),
    i: common_vendor.o((...args) => $options.toggleVoice && $options.toggleVoice(...args)),
    j: $data.isVoiceMode
  }, $data.isVoiceMode ? {
    k: common_vendor.t($data.isRecording ? "松开 结束" : "按住 说话"),
    l: common_vendor.o((...args) => $options.onVoiceStart && $options.onVoiceStart(...args)),
    m: common_vendor.o((...args) => $options.onVoiceEnd && $options.onVoiceEnd(...args)),
    n: common_vendor.o((...args) => $options.onVoiceEnd && $options.onVoiceEnd(...args))
  } : {
    o: common_vendor.o((...args) => $options.handleSend && $options.handleSend(...args)),
    p: common_vendor.o((...args) => $options.onInputFocus && $options.onInputFocus(...args)),
    q: -1,
    r: $data.inputText,
    s: common_vendor.o(($event) => $data.inputText = $event.detail.value)
  }, {
    t: common_vendor.p({
      name: $data.showEmojiPanel ? "edit-pen" : "red-packet",
      color: "#181818",
      size: "28"
    }),
    v: common_vendor.o((...args) => $options.toggleEmoji && $options.toggleEmoji(...args)),
    w: $data.inputText.trim()
  }, $data.inputText.trim() ? {
    x: common_vendor.o((...args) => $options.handleSend && $options.handleSend(...args))
  } : {
    y: common_vendor.p({
      name: "plus-circle",
      color: "#181818",
      size: "30"
    }),
    z: common_vendor.o((...args) => $options.toggleExtra && $options.toggleExtra(...args))
  }, {
    A: $data.showEmojiPanel
  }, $data.showEmojiPanel ? {
    B: common_vendor.f($data.emojis, (emoji, idx, i0) => {
      return {
        a: common_vendor.t(emoji),
        b: idx,
        c: common_vendor.o(($event) => $options.insertEmoji(emoji), idx)
      };
    })
  } : {}, {
    C: $data.showExtraPanel
  }, $data.showExtraPanel ? {
    D: common_vendor.p({
      name: "photo",
      color: "#FFF",
      size: "28"
    }),
    E: common_vendor.o((...args) => $options.handleChooseImage && $options.handleChooseImage(...args)),
    F: common_vendor.p({
      name: "camera",
      color: "#FFF",
      size: "28"
    }),
    G: common_vendor.o((...args) => $options.handleTakePhoto && $options.handleTakePhoto(...args)),
    H: common_vendor.p({
      name: "attach",
      color: "#FFF",
      size: "28"
    }),
    I: common_vendor.o((...args) => $options.handleChooseFile && $options.handleChooseFile(...args))
  } : {});
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-5a559478"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/chat/index.js.map
