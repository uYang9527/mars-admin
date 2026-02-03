"use strict";
const common_vendor = require("../../common/vendor.js");
const utils_cart = require("../../utils/cart.js");
const utils_api = require("../../utils/api.js");
const _sfc_main = {
  data() {
    return {
      statusBarHeight: 0,
      isManage: false,
      cartList: [],
      showToast: false,
      toastText: "",
      isLoggedIn: false,
      loading: false
    };
  },
  computed: {
    isAllSelected() {
      return this.cartList.length > 0 && this.cartList.every((item) => item.selected);
    },
    selectedCount() {
      return this.cartList.filter((item) => item.selected).length;
    },
    totalPrice() {
      return this.cartList.filter((item) => item.selected).reduce((total, item) => total + parseFloat(item.price) * item.quantity, 0).toFixed(2);
    }
  },
  onLoad() {
    const systemInfo = common_vendor.index.getSystemInfoSync();
    this.statusBarHeight = systemInfo.statusBarHeight || 0;
  },
  onShow() {
    const memberInfo = common_vendor.index.getStorageSync("memberInfo");
    this.isLoggedIn = !!(memberInfo && memberInfo.memberId);
    this.loadCartData();
  },
  methods: {
    // 加载购物车数据
    async loadCartData() {
      if (this.isLoggedIn) {
        this.loading = true;
        try {
          const res = await utils_api.getCartList();
          if (res.code === 200 && res.data) {
            this.cartList = res.data.map((item) => ({
              id: item.id,
              productId: item.productId,
              skuId: item.skuId,
              name: item.productName,
              image: item.productImage,
              spec: item.skuName || "",
              price: item.skuPrice || item.productPrice,
              quantity: item.quantity,
              selected: item.selected === 1
            }));
          }
        } catch (e) {
          common_vendor.index.__f__("error", "at pages/cart/index.vue:135", "加载购物车失败", e);
        } finally {
          this.loading = false;
        }
      } else {
        this.cartList = utils_cart.getCartList();
      }
    },
    toggleManage() {
      this.isManage = !this.isManage;
      this.showToastMessage(this.isManage ? "进入管理模式" : "退出管理模式");
    },
    async toggleSelect(index) {
      const item = this.cartList[index];
      if (this.isLoggedIn) {
        try {
          await utils_api.updateCartSelected(item.id, item.selected ? 0 : 1);
          item.selected = !item.selected;
        } catch (e) {
          common_vendor.index.__f__("error", "at pages/cart/index.vue:155", "更新选中状态失败", e);
        }
      } else {
        utils_cart.toggleCartSelect(item.id);
        this.loadCartData();
      }
    },
    async toggleSelectAll() {
      const allSelected = this.isAllSelected;
      if (this.isLoggedIn) {
        try {
          await utils_api.selectAllCart(allSelected ? 0 : 1);
          this.cartList.forEach((item) => item.selected = !allSelected);
        } catch (e) {
          common_vendor.index.__f__("error", "at pages/cart/index.vue:169", "更新全选状态失败", e);
        }
      } else {
        utils_cart.toggleSelectAll(!allSelected);
        this.loadCartData();
      }
    },
    async changeQuantity(index, delta) {
      const item = this.cartList[index];
      const newQuantity = Math.max(1, item.quantity + delta);
      if (this.isLoggedIn) {
        try {
          await utils_api.updateCartQuantity(item.id, newQuantity);
          item.quantity = newQuantity;
          utils_cart.updateCartBadge();
        } catch (e) {
          common_vendor.index.__f__("error", "at pages/cart/index.vue:186", "更新数量失败", e);
        }
      } else {
        utils_cart.updateCartQuantity(item.id, newQuantity);
        this.loadCartData();
        utils_cart.updateCartBadge();
      }
    },
    goCheckout() {
      if (this.selectedCount === 0) {
        this.showToastMessage("请选择商品");
        return;
      }
      if (!this.isLoggedIn) {
        common_vendor.index.navigateTo({ url: "/pages/login/index" });
        return;
      }
      common_vendor.index.navigateTo({ url: "/pages/order/index" });
    },
    showToastMessage(text) {
      this.toastText = text;
      this.showToast = true;
      setTimeout(() => {
        this.showToast = false;
      }, 2e3);
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.statusBarHeight + "px",
    b: common_vendor.t($data.cartList.length),
    c: common_vendor.t($data.isManage ? "完成" : "管理"),
    d: common_vendor.o((...args) => $options.toggleManage && $options.toggleManage(...args)),
    e: common_vendor.f($data.cartList, (item, index, i0) => {
      return {
        a: common_vendor.n(item.selected ? "fas fa-check-circle" : "far fa-circle"),
        b: common_vendor.o(($event) => $options.toggleSelect(index), index),
        c: item.image,
        d: common_vendor.t(item.name),
        e: common_vendor.t(item.spec),
        f: common_vendor.t(item.price),
        g: common_vendor.o(($event) => $options.changeQuantity(index, -1), index),
        h: common_vendor.t(item.quantity),
        i: common_vendor.o(($event) => $options.changeQuantity(index, 1), index),
        j: index
      };
    }),
    f: common_vendor.n($options.isAllSelected ? "fas fa-check-circle" : "far fa-circle"),
    g: common_vendor.o((...args) => $options.toggleSelectAll && $options.toggleSelectAll(...args)),
    h: common_vendor.t($options.totalPrice),
    i: common_vendor.t($options.selectedCount),
    j: common_vendor.o((...args) => $options.goCheckout && $options.goCheckout(...args)),
    k: common_vendor.t($data.toastText),
    l: $data.showToast ? 1 : ""
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-8039fbf1"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/cart/index.js.map
