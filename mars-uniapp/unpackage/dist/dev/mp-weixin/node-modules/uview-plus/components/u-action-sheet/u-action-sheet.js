"use strict";
const common_vendor = require("../../../../common/vendor.js");
const _sfc_main = {
  name: "u-action-sheet",
  // 一些props参数和methods方法，通过mixin混入，因为其他文件也会用到
  mixins: [common_vendor.openType, common_vendor.buttonMixin, common_vendor.mixin, common_vendor.props$1],
  data() {
    return {};
  },
  computed: {
    // 操作项目的样式
    itemStyle() {
      return (index) => {
        let style = {};
        if (this.actions[index].color)
          style.color = this.actions[index].color;
        if (this.actions[index].fontSize)
          style.fontSize = common_vendor.addUnit(this.actions[index].fontSize);
        if (this.actions[index].disabled)
          style.color = "#c0c4cc";
        return style;
      };
    }
  },
  emits: ["close", "select", "update:show"],
  methods: {
    closeHandler() {
      if (this.closeOnClickOverlay) {
        this.$emit("update:show", false);
        this.$emit("close");
      }
    },
    // 点击取消按钮
    cancel() {
      this.$emit("update:show", false);
      this.$emit("close");
    },
    selectHandler(index) {
      const item = this.actions[index];
      if (item && !item.disabled && !item.loading) {
        this.$emit("select", item);
        if (this.closeOnClickAction) {
          this.$emit("update:show", false);
          this.$emit("close");
        }
      }
    }
  }
};
if (!Array) {
  const _easycom_u_icon2 = common_vendor.resolveComponent("u-icon");
  const _easycom_u_line2 = common_vendor.resolveComponent("u-line");
  const _easycom_u_loading_icon2 = common_vendor.resolveComponent("u-loading-icon");
  const _easycom_u_gap2 = common_vendor.resolveComponent("u-gap");
  const _easycom_u_popup2 = common_vendor.resolveComponent("u-popup");
  (_easycom_u_icon2 + _easycom_u_line2 + _easycom_u_loading_icon2 + _easycom_u_gap2 + _easycom_u_popup2)();
}
const _easycom_u_icon = () => "../u-icon/u-icon.js";
const _easycom_u_line = () => "../u-line/u-line.js";
const _easycom_u_loading_icon = () => "../u-loading-icon/u-loading-icon.js";
const _easycom_u_gap = () => "../u-gap/u-gap.js";
const _easycom_u_popup = () => "../u-popup/u-popup.js";
if (!Math) {
  (_easycom_u_icon + _easycom_u_line + _easycom_u_loading_icon + _easycom_u_gap + _easycom_u_popup)();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: _ctx.title
  }, _ctx.title ? {
    b: common_vendor.t(_ctx.title),
    c: common_vendor.p({
      name: "close",
      size: "17",
      color: "#c8c9cc",
      bold: true
    }),
    d: common_vendor.o((...args) => $options.cancel && $options.cancel(...args))
  } : {}, {
    e: _ctx.description
  }, _ctx.description ? {
    f: common_vendor.t(_ctx.description),
    g: common_vendor.s({
      marginTop: `${_ctx.title && _ctx.description ? 0 : "18px"}`
    })
  } : {}, {
    h: _ctx.description
  }, _ctx.description ? {} : {}, {
    i: common_vendor.f(_ctx.actions, (item, index, i0) => {
      return common_vendor.e({
        a: !item.loading
      }, !item.loading ? common_vendor.e({
        b: common_vendor.t(item.name),
        c: common_vendor.s($options.itemStyle(index)),
        d: item.subname
      }, item.subname ? {
        e: common_vendor.t(item.subname)
      } : {}) : {
        f: "05ea451b-3-" + i0 + ",05ea451b-0",
        g: common_vendor.p({
          ["custom-class"]: "van-action-sheet__loading",
          size: "18",
          mode: "circle"
        })
      }, {
        h: common_vendor.o(($event) => $options.selectHandler(index), index),
        i: !item.disabled && !item.loading ? "u-action-sheet--hover" : "",
        j: item.openType,
        k: common_vendor.o((...args) => _ctx.onGetUserInfo && _ctx.onGetUserInfo(...args), index),
        l: common_vendor.o((...args) => _ctx.onContact && _ctx.onContact(...args), index),
        m: common_vendor.o((...args) => _ctx.onGetPhoneNumber && _ctx.onGetPhoneNumber(...args), index),
        n: common_vendor.o((...args) => _ctx.onError && _ctx.onError(...args), index),
        o: common_vendor.o((...args) => _ctx.onLaunchApp && _ctx.onLaunchApp(...args), index),
        p: common_vendor.o((...args) => _ctx.onOpenSetting && _ctx.onOpenSetting(...args), index),
        q: common_vendor.o(($event) => $options.selectHandler(index), index),
        r: !item.disabled && !item.loading ? "u-action-sheet--hover" : "",
        s: index !== _ctx.actions.length - 1
      }, index !== _ctx.actions.length - 1 ? {
        t: "05ea451b-4-" + i0 + ",05ea451b-0"
      } : {}, {
        v: index
      });
    }),
    j: _ctx.lang,
    k: _ctx.sessionFrom,
    l: _ctx.sendMessageTitle,
    m: _ctx.sendMessagePath,
    n: _ctx.sendMessageImg,
    o: _ctx.showMessageCard,
    p: _ctx.appParameter,
    q: _ctx.wrapMaxHeight,
    r: _ctx.cancelText
  }, _ctx.cancelText ? {
    s: common_vendor.p({
      bgColor: "#eaeaec",
      height: "6"
    })
  } : {}, {
    t: _ctx.cancelText
  }, _ctx.cancelText ? {
    v: common_vendor.t(_ctx.cancelText),
    w: common_vendor.o(() => {
    }),
    x: common_vendor.o((...args) => $options.cancel && $options.cancel(...args))
  } : {}, {
    y: common_vendor.o($options.closeHandler),
    z: common_vendor.p({
      show: _ctx.show,
      mode: "bottom",
      safeAreaInsetBottom: _ctx.safeAreaInsetBottom,
      round: _ctx.round
    })
  });
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-05ea451b"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/node-modules/uview-plus/components/u-action-sheet/u-action-sheet.js.map
