"use strict";
const common_vendor = require("../common/vendor.js");
const utils_request = require("./request.js");
const getMemberId = () => {
  const memberInfo = common_vendor.index.getStorageSync("memberInfo");
  return memberInfo ? memberInfo.memberId : null;
};
const login = (code, nickname, avatar) => {
  return utils_request.post("/api/mall/login", { code, nickname, avatar });
};
const loginByPhone = (loginCode, phoneCode, nickname, avatar) => {
  return utils_request.post("/api/mall/loginByPhone", { loginCode, phoneCode, nickname, avatar });
};
const getMemberInfo = () => {
  return utils_request.get("/api/mall/member/info", { memberId: getMemberId() });
};
const getHomeData = () => {
  return utils_request.get("/api/mall/home");
};
const getCategoryList = () => {
  return utils_request.get("/api/mall/category/list");
};
const getProductList = (params) => {
  return utils_request.get("/api/mall/product/list", params);
};
const getProductDetail = (id) => {
  return utils_request.get(`/api/mall/product/${id}`, { memberId: getMemberId() });
};
const getCartList = () => {
  return utils_request.get("/api/mall/cart/list", { memberId: getMemberId() });
};
const addToCart = (productId, skuId, quantity = 1) => {
  return utils_request.post("/api/mall/cart/add", {
    memberId: getMemberId(),
    productId,
    skuId,
    quantity
  });
};
const updateCartQuantity = (cartId, quantity) => {
  return utils_request.put("/api/mall/cart/quantity", {
    memberId: getMemberId(),
    cartId,
    quantity
  });
};
const updateCartSelected = (cartId, selected) => {
  return utils_request.put("/api/mall/cart/selected", {
    memberId: getMemberId(),
    cartId,
    selected
  });
};
const selectAllCart = (selected) => {
  return utils_request.put("/api/mall/cart/selectAll", {
    memberId: getMemberId(),
    selected
  });
};
const getOrderList = (params) => {
  return utils_request.get("/api/mall/order/list", { ...params, memberId: getMemberId() });
};
const getOrderCount = () => {
  return utils_request.get("/api/mall/order/count", { memberId: getMemberId() });
};
const receiveOrder = (id) => {
  return utils_request.put(`/api/mall/order/${id}/receive`, { memberId: getMemberId() });
};
const toggleFavorite = (productId) => {
  return utils_request.post("/api/mall/favorite/toggle", { memberId: getMemberId(), productId });
};
exports.addToCart = addToCart;
exports.getCartList = getCartList;
exports.getCategoryList = getCategoryList;
exports.getHomeData = getHomeData;
exports.getMemberInfo = getMemberInfo;
exports.getOrderCount = getOrderCount;
exports.getOrderList = getOrderList;
exports.getProductDetail = getProductDetail;
exports.getProductList = getProductList;
exports.login = login;
exports.loginByPhone = loginByPhone;
exports.receiveOrder = receiveOrder;
exports.selectAllCart = selectAllCart;
exports.toggleFavorite = toggleFavorite;
exports.updateCartQuantity = updateCartQuantity;
exports.updateCartSelected = updateCartSelected;
//# sourceMappingURL=../../.sourcemap/mp-weixin/utils/api.js.map
