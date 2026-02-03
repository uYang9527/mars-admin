"use strict";
const common_vendor = require("../../common/vendor.js");
const utils_cart = require("../../utils/cart.js");
const utils_api = require("../../utils/api.js");
const _sfc_main = {
  data() {
    return {
      statusBarHeight: 0,
      loading: true,
      banners: [],
      categories: [],
      products: [],
      showToast: false,
      toastText: ""
    };
  },
  onLoad() {
    const systemInfo = common_vendor.index.getSystemInfoSync();
    this.statusBarHeight = systemInfo.statusBarHeight || 0;
    this.loadHomeData();
  },
  onShow() {
    utils_cart.updateCartBadge();
  },
  onPullDownRefresh() {
    this.loadHomeData().finally(() => {
      common_vendor.index.stopPullDownRefresh();
    });
  },
  methods: {
    // 加载首页数据
    async loadHomeData() {
      this.loading = true;
      try {
        const res = await utils_api.getHomeData();
        if (res.code === 200 && res.data) {
          this.banners = (res.data.banners || []).map((item) => ({
            id: item.id,
            image: item.image,
            title: item.title,
            subtitle: item.subtitle,
            linkType: item.linkType,
            linkValue: item.linkValue
          }));
          this.categories = (res.data.categories || []).map((item) => ({
            id: item.id,
            name: item.name,
            icon: this.getCategoryIcon(item.icon)
          }));
          this.products = (res.data.recommendProducts || []).map((item) => ({
            id: item.id,
            name: item.name,
            desc: item.subtitle,
            price: item.price,
            image: item.mainImage,
            isFavorite: false
          }));
        }
      } catch (e) {
        common_vendor.index.__f__("error", "at pages/index/index.vue:155", "加载首页数据失败", e);
        this.useDefaultData();
      } finally {
        this.loading = false;
      }
    },
    // 获取分类图标
    getCategoryIcon(icon) {
      if (!icon)
        return "fas fa-tag";
      if (icon.startsWith("fa"))
        return icon;
      return `fas ${icon}`;
    },
    // 使用默认数据（接口失败时）
    useDefaultData() {
      this.banners = [{
        id: 1,
        image: "https://images.unsplash.com/photo-1542838132-92c53300491e?q=80&w=1000&auto=format&fit=crop",
        title: "有机牛油果 5折起"
      }];
      this.categories = [
        { id: 1, name: "水果", icon: "fas fa-apple-alt" },
        { id: 2, name: "蔬菜", icon: "fas fa-carrot" },
        { id: 3, name: "海鲜", icon: "fas fa-fish" },
        { id: 4, name: "肉类", icon: "fas fa-drumstick-bite" }
      ];
      this.products = [
        {
          id: 1,
          name: "智利进口菠萝",
          desc: "单果重约1.5kg",
          price: "29.9",
          image: "https://images.unsplash.com/photo-1550258987-190a2d41a8ba?auto=format&fit=crop&w=400",
          isFavorite: false
        },
        {
          id: 2,
          name: "有机阳光草莓",
          desc: "甜度15+ 500g/盒",
          price: "45.0",
          image: "https://images.unsplash.com/photo-1601004890684-d8cbf643f5f2?auto=format&fit=crop&w=400",
          isFavorite: false
        }
      ];
    },
    goSearch() {
      common_vendor.index.navigateTo({ url: "/pages/search/index" });
    },
    goNotification() {
      common_vendor.index.showToast({ title: "暂无通知", icon: "none" });
    },
    goCategory(id) {
      common_vendor.index.switchTab({ url: "/pages/category/index" });
    },
    goProductList() {
      common_vendor.index.navigateTo({ url: "/pages/search/index" });
    },
    goDetail(id) {
      common_vendor.index.navigateTo({ url: `/pages/detail/index?id=${id}` });
    },
    async addCart(product) {
      const memberInfo = common_vendor.index.getStorageSync("memberInfo");
      if (memberInfo && memberInfo.memberId) {
        try {
          await utils_api.addToCart(product.id, null, 1);
          utils_cart.updateCartBadge();
          this.showToastMessage(`${product.name} 已加入购物车`);
        } catch (e) {
          common_vendor.index.__f__("error", "at pages/index/index.vue:215", "添加购物车失败", e);
        }
      } else {
        utils_cart.addToCart(product);
        this.showToastMessage(`${product.name} 已加入购物车`);
      }
    },
    toggleFavorite(product) {
      product.isFavorite = !product.isFavorite;
      this.showToastMessage(product.isFavorite ? "已收藏" : "已取消收藏");
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
    b: common_vendor.o((...args) => $options.goNotification && $options.goNotification(...args)),
    c: common_vendor.o((...args) => $options.goSearch && $options.goSearch(...args)),
    d: common_vendor.f($data.banners, (banner, index, i0) => {
      return {
        a: banner.image,
        b: common_vendor.t(banner.title),
        c: index
      };
    }),
    e: common_vendor.o((...args) => $options.goProductList && $options.goProductList(...args)),
    f: common_vendor.f($data.products, (product, k0, i0) => {
      return {
        a: product.image,
        b: common_vendor.t(product.name),
        c: common_vendor.t(product.desc),
        d: common_vendor.t(product.price),
        e: common_vendor.o(($event) => $options.addCart(product), product.id),
        f: product.id,
        g: common_vendor.o(($event) => $options.goDetail(product.id), product.id)
      };
    }),
    g: common_vendor.t($data.toastText),
    h: $data.showToast ? 1 : ""
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-1cf27b2a"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/index/index.js.map
