"use strict";
const common_vendor = require("../../common/vendor.js");
const utils_api = require("../../utils/api.js");
const _sfc_main = {
  data() {
    return {
      statusBarHeight: 0,
      activeTab: -1,
      // -1 表示全部
      tabs: [
        { id: -1, name: "全部", status: null },
        { id: 0, name: "待付款", status: 0 },
        { id: 1, name: "待发货", status: 1 },
        { id: 2, name: "待收货", status: 2 },
        { id: 3, name: "已完成", status: 3 }
      ],
      orders: [],
      loading: false,
      page: 1,
      hasMore: true
    };
  },
  onLoad(options) {
    const systemInfo = common_vendor.index.getSystemInfoSync();
    this.statusBarHeight = systemInfo.statusBarHeight || 0;
    if (options.status !== void 0) {
      this.activeTab = parseInt(options.status);
    }
    this.loadOrders();
  },
  methods: {
    goBack() {
      common_vendor.index.navigateBack();
    },
    switchTab(id) {
      this.activeTab = id;
      this.page = 1;
      this.orders = [];
      this.hasMore = true;
      this.loadOrders();
    },
    // 加载订单列表
    async loadOrders() {
      if (this.loading || !this.hasMore)
        return;
      this.loading = true;
      try {
        const params = {
          page: this.page,
          pageSize: 10
        };
        if (this.activeTab !== -1) {
          params.status = this.activeTab;
        }
        const res = await utils_api.getOrderList(params);
        if (res.code === 200 && res.data) {
          const list = (res.data.list || []).map((order) => this.formatOrder(order));
          if (this.page === 1) {
            this.orders = list;
          } else {
            this.orders = [...this.orders, ...list];
          }
          this.hasMore = list.length >= 10;
          this.page++;
        }
      } catch (e) {
        common_vendor.index.__f__("error", "at pages/orders/index.vue:123", "加载订单失败", e);
      } finally {
        this.loading = false;
      }
    },
    // 格式化订单数据
    formatOrder(order) {
      const statusMap = {
        0: { text: "待付款", color: "#f59e0b", btn: "去支付" },
        1: { text: "待发货", color: "#3b82f6", btn: "提醒发货" },
        2: { text: "待收货", color: "#8b5cf6", btn: "确认收货" },
        3: { text: "已完成", color: "#059669", btn: "再次购买" },
        4: { text: "已取消", color: "#94a3b8", btn: "删除订单" },
        5: { text: "已退款", color: "#ef4444", btn: "查看详情" }
      };
      const statusInfo = statusMap[order.status] || { text: "未知", color: "#94a3b8", btn: "查看详情" };
      const firstItem = order.items && order.items.length > 0 ? order.items[0] : {};
      return {
        id: order.id,
        orderNo: order.orderNo,
        time: order.createTime,
        status: statusInfo.text,
        statusCode: order.status,
        statusColor: statusInfo.color,
        name: firstItem.productName || "商品",
        spec: firstItem.skuName || "",
        price: firstItem.price || order.payAmount,
        quantity: order.items ? order.items.reduce((sum, item) => sum + item.quantity, 0) : 1,
        total: order.payAmount,
        image: firstItem.productImage || "",
        btnText: statusInfo.btn
      };
    },
    // 处理订单操作
    async handleOrder(order) {
      switch (order.statusCode) {
        case 0:
          this.goPay(order);
          break;
        case 2:
          await this.confirmReceive(order);
          break;
        case 3:
          common_vendor.index.showToast({ title: "功能开发中", icon: "none" });
          break;
        default:
          common_vendor.index.navigateTo({ url: `/pages/order/detail?id=${order.id}` });
      }
    },
    // 去支付
    goPay(order) {
      common_vendor.index.navigateTo({ url: `/pages/order/detail?id=${order.id}` });
    },
    // 确认收货
    async confirmReceive(order) {
      common_vendor.index.showModal({
        title: "确认收货",
        content: "确认已收到商品？",
        success: async (res) => {
          if (res.confirm) {
            try {
              await utils_api.receiveOrder(order.id);
              common_vendor.index.showToast({ title: "确认收货成功", icon: "success" });
              this.page = 1;
              this.orders = [];
              this.hasMore = true;
              this.loadOrders();
            } catch (e) {
              common_vendor.index.__f__("error", "at pages/orders/index.vue:196", "确认收货失败", e);
            }
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
    c: common_vendor.f($data.tabs, (tab, k0, i0) => {
      return {
        a: common_vendor.t(tab.name),
        b: $data.activeTab === tab.id ? 1 : "",
        c: tab.id,
        d: common_vendor.o(($event) => $options.switchTab(tab.id), tab.id)
      };
    }),
    d: common_vendor.f($data.orders, (order, k0, i0) => {
      return {
        a: common_vendor.t(order.time),
        b: common_vendor.t(order.status),
        c: order.statusColor,
        d: order.image,
        e: common_vendor.t(order.name),
        f: common_vendor.t(order.spec),
        g: common_vendor.t(order.price),
        h: common_vendor.t(order.quantity),
        i: common_vendor.t(order.quantity),
        j: common_vendor.t(order.total),
        k: common_vendor.t(order.btnText),
        l: common_vendor.o(($event) => $options.handleOrder(order), order.id),
        m: order.id
      };
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-e1e6274e"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/orders/index.js.map
