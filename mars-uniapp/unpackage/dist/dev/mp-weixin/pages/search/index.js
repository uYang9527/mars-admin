"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      statusBarHeight: 0,
      keyword: "",
      hotSearches: ["草莓", "牛油果", "有机蔬菜", "三文鱼", "新鲜水果"],
      history: ["凤梨", "有机草莓"]
    };
  },
  onLoad() {
    const systemInfo = common_vendor.index.getSystemInfoSync();
    this.statusBarHeight = systemInfo.statusBarHeight || 0;
  },
  methods: {
    goBack() {
      common_vendor.index.navigateBack();
    },
    doSearch() {
      if (!this.keyword.trim())
        return;
      common_vendor.index.showToast({ title: `搜索: ${this.keyword}`, icon: "none" });
    },
    searchTag(tag) {
      this.keyword = tag;
      this.doSearch();
    },
    clearHistory() {
      this.history = [];
      common_vendor.index.showToast({ title: "搜索历史已清除", icon: "none" });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.statusBarHeight + "px",
    b: common_vendor.o((...args) => $options.goBack && $options.goBack(...args)),
    c: common_vendor.o((...args) => $options.doSearch && $options.doSearch(...args)),
    d: $data.keyword,
    e: common_vendor.o(($event) => $data.keyword = $event.detail.value),
    f: common_vendor.o((...args) => $options.doSearch && $options.doSearch(...args)),
    g: common_vendor.f($data.hotSearches, (tag, index, i0) => {
      return {
        a: common_vendor.t(tag),
        b: index,
        c: common_vendor.o(($event) => $options.searchTag(tag), index)
      };
    }),
    h: common_vendor.o((...args) => $options.clearHistory && $options.clearHistory(...args)),
    i: common_vendor.f($data.history, (item, index, i0) => {
      return {
        a: common_vendor.t(item),
        b: index,
        c: common_vendor.o(($event) => $options.searchTag(item), index)
      };
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-2dab939d"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/search/index.js.map
