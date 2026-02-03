"use strict";
const common_vendor = require("../../common/vendor.js");
const utils_api = require("../../utils/api.js");
const _sfc_main = {
  data() {
    return {
      statusBarHeight: 0,
      activeCategory: null,
      categories: [],
      products: [],
      loading: false
    };
  },
  onLoad() {
    const systemInfo = common_vendor.index.getSystemInfoSync();
    this.statusBarHeight = systemInfo.statusBarHeight || 0;
    this.loadCategories();
  },
  methods: {
    // 加载分类列表
    async loadCategories() {
      try {
        const res = await utils_api.getCategoryList();
        if (res.code === 200 && res.data) {
          this.categories = [
            { id: 0, name: "推荐分类" },
            ...res.data.map((cat) => ({
              id: cat.id,
              name: cat.name,
              icon: cat.icon
            }))
          ];
          if (this.categories.length > 0) {
            this.activeCategory = this.categories[0].id;
            this.loadProducts();
          }
        }
      } catch (e) {
        common_vendor.index.__f__("error", "at pages/category/index.vue:103", "加载分类失败", e);
        this.useDefaultCategories();
      }
    },
    // 加载商品列表
    async loadProducts() {
      this.loading = true;
      try {
        const params = { pageSize: 20 };
        if (this.activeCategory && this.activeCategory !== 0) {
          params.categoryId = this.activeCategory;
        } else {
          params.isRecommend = 1;
        }
        const res = await utils_api.getProductList(params);
        if (res.code === 200 && res.data) {
          this.products = (res.data.list || []).map((item) => ({
            id: item.id,
            name: item.name,
            image: item.mainImage
          }));
        }
      } catch (e) {
        common_vendor.index.__f__("error", "at pages/category/index.vue:128", "加载商品失败", e);
      } finally {
        this.loading = false;
      }
    },
    // 使用默认分类数据
    useDefaultCategories() {
      this.categories = [
        { id: 0, name: "推荐分类" },
        { id: 1, name: "新鲜水果" },
        { id: 2, name: "时令蔬菜" },
        { id: 3, name: "肉禽蛋品" },
        { id: 4, name: "海鲜水产" },
        { id: 5, name: "乳品烘焙" }
      ];
      this.activeCategory = 0;
      this.products = [
        { id: 1, name: "香蕉", image: "https://images.unsplash.com/photo-1528825871115-3581a5387919?auto=format&fit=crop&w=200" },
        { id: 2, name: "葡萄", image: "https://images.unsplash.com/photo-1573501740349-335c03c80a6d?auto=format&fit=crop&w=200" },
        { id: 3, name: "凤梨", image: "https://images.unsplash.com/photo-1550258987-190a2d41a8ba?auto=format&fit=crop&w=200" },
        { id: 4, name: "柠檬", image: "https://images.unsplash.com/photo-1582281298055-e25b84a30b0b?auto=format&fit=crop&w=200" }
      ];
    },
    goSearch() {
      common_vendor.index.navigateTo({ url: "/pages/search/index" });
    },
    selectCategory(cat) {
      this.activeCategory = cat.id;
      this.loadProducts();
    },
    goDetail(id) {
      common_vendor.index.navigateTo({ url: `/pages/detail/index?id=${id}` });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.statusBarHeight + "px",
    b: common_vendor.o((...args) => $options.goSearch && $options.goSearch(...args)),
    c: common_vendor.f($data.categories, (cat, k0, i0) => {
      return {
        a: common_vendor.t(cat.name),
        b: $data.activeCategory === cat.id ? 1 : "",
        c: cat.id,
        d: common_vendor.o(($event) => $options.selectCategory(cat), cat.id)
      };
    }),
    d: common_vendor.f($data.products, (product, k0, i0) => {
      return {
        a: product.image,
        b: common_vendor.t(product.name),
        c: product.id,
        d: common_vendor.o(($event) => $options.goDetail(product.id), product.id)
      };
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-3cdc7548"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/category/index.js.map
