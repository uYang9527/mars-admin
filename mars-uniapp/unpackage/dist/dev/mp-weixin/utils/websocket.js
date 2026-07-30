"use strict";
const common_vendor = require("../common/vendor.js");
const utils_request = require("./request.js");
const utils_auth = require("./auth.js");
class WebSocketClient {
  constructor() {
    this.socketTask = null;
    this.isConnected = false;
    this.reconnectTimer = null;
    this.heartbeatTimer = null;
    this.listeners = {};
    this.reconnectCount = 0;
    this.maxReconnect = 10;
  }
  /**
   * 建立连接
   */
  connect() {
    if (this.isConnected || this.socketTask)
      return;
    const token = utils_auth.getToken();
    if (!token)
      return;
    const wsUrl = utils_request.BASE_URL.replace("http://", "ws://").replace("https://", "wss://");
    const url = `${wsUrl}/ws/message?token=${token}`;
    this.socketTask = common_vendor.index.connectSocket({
      url,
      success: () => {
        common_vendor.index.__f__("log", "at utils/websocket.js:35", "[WS] 正在连接...");
      },
      fail: (err) => {
        common_vendor.index.__f__("error", "at utils/websocket.js:38", "[WS] 连接失败:", err);
        this.reconnect();
      }
    });
    this.socketTask.onOpen(() => {
      common_vendor.index.__f__("log", "at utils/websocket.js:44", "[WS] 连接成功");
      this.isConnected = true;
      this.reconnectCount = 0;
      this.startHeartbeat();
      this.emit("open");
    });
    this.socketTask.onMessage((res) => {
      try {
        const data = JSON.parse(res.data);
        common_vendor.index.__f__("log", "at utils/websocket.js:54", "[WS] 收到消息:", data);
        if (data.type === "chat") {
          this.emit("chat", data);
        } else if (data.type === "groupChat") {
          this.emit("groupChat", data);
        } else if (data.type === "notice") {
          this.emit("notice", data);
        } else if (data.type === "pong") {
        } else {
          this.emit("message", data);
        }
      } catch (e) {
        common_vendor.index.__f__("log", "at utils/websocket.js:69", "[WS] 消息解析失败:", res.data);
      }
    });
    this.socketTask.onClose(() => {
      common_vendor.index.__f__("log", "at utils/websocket.js:74", "[WS] 连接关闭");
      this.isConnected = false;
      this.socketTask = null;
      this.stopHeartbeat();
      this.emit("close");
      this.reconnect();
    });
    this.socketTask.onError((err) => {
      common_vendor.index.__f__("error", "at utils/websocket.js:83", "[WS] 错误:", err);
      this.isConnected = false;
      this.emit("error", err);
    });
  }
  /**
   * 发送消息
   */
  send(data) {
    if (!this.isConnected || !this.socketTask) {
      common_vendor.index.__f__("warn", "at utils/websocket.js:94", "[WS] 未连接，无法发送");
      return false;
    }
    const msg = typeof data === "string" ? data : JSON.stringify(data);
    this.socketTask.send({ data: msg });
    return true;
  }
  /**
   * 断线重连
   */
  reconnect() {
    if (this.reconnectCount >= this.maxReconnect) {
      common_vendor.index.__f__("log", "at utils/websocket.js:107", "[WS] 超过最大重连次数");
      return;
    }
    if (this.reconnectTimer) {
      clearTimeout(this.reconnectTimer);
    }
    const delay = Math.min(3e3 * Math.pow(1.5, this.reconnectCount), 3e4);
    this.reconnectCount++;
    common_vendor.index.__f__("log", "at utils/websocket.js:118", `[WS] ${delay / 1e3}s 后第 ${this.reconnectCount} 次重连`);
    this.reconnectTimer = setTimeout(() => {
      this.connect();
    }, delay);
  }
  /**
   * 心跳检测
   */
  startHeartbeat() {
    this.stopHeartbeat();
    this.heartbeatTimer = setInterval(() => {
      if (this.isConnected) {
        this.send({ type: "ping" });
      }
    }, 3e4);
  }
  stopHeartbeat() {
    if (this.heartbeatTimer) {
      clearInterval(this.heartbeatTimer);
      this.heartbeatTimer = null;
    }
  }
  /**
   * 关闭连接
   */
  close() {
    this.stopHeartbeat();
    if (this.reconnectTimer) {
      clearTimeout(this.reconnectTimer);
      this.reconnectTimer = null;
    }
    this.maxReconnect = 0;
    if (this.socketTask) {
      this.socketTask.close();
      this.socketTask = null;
    }
    this.isConnected = false;
  }
  /**
   * 注册事件监听
   */
  on(event, callback) {
    if (!this.listeners[event]) {
      this.listeners[event] = [];
    }
    this.listeners[event].push(callback);
  }
  /**
   * 移除事件监听
   */
  off(event, callback) {
    if (!this.listeners[event])
      return;
    if (!callback) {
      this.listeners[event] = [];
    } else {
      this.listeners[event] = this.listeners[event].filter((cb) => cb !== callback);
    }
  }
  /**
   * 触发事件
   */
  emit(event, data) {
    if (this.listeners[event]) {
      this.listeners[event].forEach((cb) => cb(data));
    }
  }
}
const wsClient = new WebSocketClient();
exports.wsClient = wsClient;
//# sourceMappingURL=../../.sourcemap/mp-weixin/utils/websocket.js.map
