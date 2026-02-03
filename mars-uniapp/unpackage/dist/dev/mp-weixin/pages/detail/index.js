"use strict";
const common_vendor = require("../../common/vendor.js");
const utils_cart = require("../../utils/cart.js");
const utils_api = require("../../utils/api.js");
const _sfc_main = {
  data() {
    return {
      statusBarHeight: 0,
      productId: null,
      loading: true,
      product: {
        id: null,
        name: "",
        desc: "",
        price: "0",
        originPrice: "0",
        image: "",
        detail: ""
      },
      specs: [],
      activeSpec: null,
      selectedSku: null,
      isFavorite: false,
      cartCount: 0,
      showToast: false,
      toastText: ""
    };
  },
  onLoad(options) {
    const systemInfo = common_vendor.index.getSystemInfoSync();
    this.statusBarHeight = systemInfo.statusBarHeight || 0;
    if (options.id) {
      this.productId = options.id;
      this.loadProductDetail();
    }
    this.cartCount = utils_cart.getCartCount();
  },
  onShow() {
    this.cartCount = utils_cart.getCartCount();
  },
  methods: {
    // 加载商品详情
    async loadProductDetail() {
      this.loading = true;
      try {
        const res = await utils_api.getProductDetail(this.productId);
        if (res.code === 200 && res.data) {
          const data = res.data;
          this.product = {
            id: data.id,
            name: data.name,
            desc: data.subtitle,
            price: data.price,
            originPrice: data.originalPrice || data.price,
            image: data.mainImage,
            images: data.images ? JSON.parse(data.images) : [],
            detail: data.detail || ""
          };
          if (data.skuList && data.skuList.length > 0) {
            this.specs = data.skuList.map((sku) => ({
              id: sku.id,
              name: sku.skuName,
              price: sku.price,
              stock: sku.stock
            }));
            this.activeSpec = this.specs[0].id;
            this.selectedSku = this.specs[0];
          }
          this.isFavorite = data.isFavorite || false;
        }
      } catch (e) {
        common_vendor.index.__f__("error", "at pages/detail/index.vue:180", "加载商品详情失败", e);
        this.showToastMessage("加载失败，请重试");
      } finally {
        this.loading = false;
      }
    },
    goBack() {
      common_vendor.index.navigateBack();
    },
    goHome() {
      common_vendor.index.switchTab({ url: "/pages/index/index" });
    },
    goCart() {
      common_vendor.index.switchTab({ url: "/pages/cart/index" });
    },
    async toggleFavorite() {
      const memberInfo = common_vendor.index.getStorageSync("memberInfo");
      if (!memberInfo || !memberInfo.memberId) {
        common_vendor.index.navigateTo({ url: "/pages/login/index" });
        return;
      }
      try {
        const res = await utils_api.toggleFavorite(this.productId);
        if (res.code === 200) {
          this.isFavorite = res.data;
          this.showToastMessage(this.isFavorite ? "已收藏" : "已取消收藏");
        }
      } catch (e) {
        common_vendor.index.__f__("error", "at pages/detail/index.vue:209", "收藏操作失败", e);
      }
    },
    selectSpec(spec) {
      this.activeSpec = spec.id;
      this.selectedSku = spec;
      if (spec.price) {
        this.product.price = spec.price;
      }
    },
    async addCart() {
      const memberInfo = common_vendor.index.getStorageSync("memberInfo");
      if (memberInfo && memberInfo.memberId) {
        try {
          await utils_api.addToCart(this.productId, this.activeSpec, 1);
          utils_cart.updateCartBadge();
          this.cartCount = utils_cart.getCartCount();
          this.showToastMessage("已加入购物车");
        } catch (e) {
          common_vendor.index.__f__("error", "at pages/detail/index.vue:230", "添加购物车失败", e);
        }
      } else {
        utils_cart.addToCart({
          id: this.product.id,
          name: this.product.name,
          image: this.product.image,
          price: this.product.price,
          spec: this.selectedSku ? this.selectedSku.name : ""
        });
        this.cartCount = utils_cart.getCartCount();
        this.showToastMessage("已加入购物车");
      }
    },
    async buyNow() {
      const memberInfo = common_vendor.index.getStorageSync("memberInfo");
      if (!memberInfo || !memberInfo.memberId) {
        common_vendor.index.navigateTo({ url: "/pages/login/index" });
        return;
      }
      try {
        await utils_api.addToCart(this.productId, this.activeSpec, 1);
        utils_cart.updateCartBadge();
        common_vendor.index.navigateTo({
          url: `/pages/order/confirm?productId=${this.productId}&skuId=${this.activeSpec || ""}&quantity=1&direct=1`
        });
      } catch (e) {
        common_vendor.index.__f__("error", "at pages/detail/index.vue:262", "立即购买失败", e);
      }
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
  return common_vendor.e({
    a: common_vendor.o((...args) => $options.goBack && $options.goBack(...args)),
    b: $data.statusBarHeight + "px",
    c: $data.product.image,
    d: common_vendor.t($data.product.name),
    e: common_vendor.n($data.isFavorite ? "fas fa-heart" : "far fa-heart"),
    f: common_vendor.o((...args) => $options.toggleFavorite && $options.toggleFavorite(...args)),
    g: common_vendor.t($data.product.desc),
    h: common_vendor.t($data.product.price),
    i: common_vendor.t($data.product.originPrice),
    j: common_vendor.f($data.specs, (spec, k0, i0) => {
      return {
        a: common_vendor.t(spec.name),
        b: $data.activeSpec === spec.id ? 1 : "",
        c: spec.id,
        d: common_vendor.o(($event) => $options.selectSpec(spec), spec.id)
      };
    }),
    k: common_vendor.o((...args) => $options.goHome && $options.goHome(...args)),
    l: $data.cartCount
  }, $data.cartCount ? {} : {}, {
    m: common_vendor.o((...args) => $options.goCart && $options.goCart(...args)),
    n: common_vendor.o((...args) => $options.addCart && $options.addCart(...args)),
    o: common_vendor.o((...args) => $options.buyNow && $options.buyNow(...args)),
    p: common_vendor.t($data.toastText),
    q: $data.showToast ? 1 : ""
  });
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-2fd5b0a7"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/detail/index.js.map
