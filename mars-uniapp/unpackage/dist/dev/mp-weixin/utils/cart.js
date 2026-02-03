"use strict";
const common_vendor = require("../common/vendor.js");
const CART_KEY = "cart_list";
function getCartList() {
  try {
    const cartData = common_vendor.index.getStorageSync(CART_KEY);
    return cartData ? JSON.parse(cartData) : [];
  } catch (e) {
    return [];
  }
}
function saveCartList(list) {
  try {
    common_vendor.index.setStorageSync(CART_KEY, JSON.stringify(list));
    return true;
  } catch (e) {
    return false;
  }
}
function addToCart(product) {
  const cartList = getCartList();
  const existIndex = cartList.findIndex((item) => item.id === product.id);
  if (existIndex > -1) {
    cartList[existIndex].quantity += 1;
  } else {
    cartList.push({
      id: product.id,
      name: product.name,
      desc: product.desc,
      price: product.price,
      image: product.image,
      spec: product.desc || "默认规格",
      quantity: 1,
      selected: true
    });
  }
  saveCartList(cartList);
  updateCartBadge(cartList);
  return cartList.length;
}
function updateCartQuantity(id, quantity) {
  const cartList = getCartList();
  const item = cartList.find((item2) => item2.id === id);
  if (item) {
    item.quantity = Math.max(1, quantity);
    saveCartList(cartList);
    updateCartBadge(cartList);
  }
}
function toggleCartSelect(id) {
  const cartList = getCartList();
  const item = cartList.find((item2) => item2.id === id);
  if (item) {
    item.selected = !item.selected;
    saveCartList(cartList);
  }
  return item ? item.selected : false;
}
function toggleSelectAll(selectAll) {
  const cartList = getCartList();
  cartList.forEach((item) => {
    item.selected = selectAll;
  });
  saveCartList(cartList);
}
function getCartCount() {
  const cartList = getCartList();
  return cartList.reduce((total, item) => total + item.quantity, 0);
}
function updateCartBadge(cartList) {
  const count = cartList ? cartList.reduce((total, item) => total + item.quantity, 0) : getCartCount();
  if (count > 0) {
    common_vendor.index.setTabBarBadge({
      index: 2,
      // 购物车是第3个tab（index从0开始）
      text: count > 99 ? "99+" : count.toString()
    });
  } else {
    common_vendor.index.removeTabBarBadge({
      index: 2
    });
  }
}
exports.addToCart = addToCart;
exports.getCartCount = getCartCount;
exports.getCartList = getCartList;
exports.toggleCartSelect = toggleCartSelect;
exports.toggleSelectAll = toggleSelectAll;
exports.updateCartBadge = updateCartBadge;
exports.updateCartQuantity = updateCartQuantity;
//# sourceMappingURL=../../.sourcemap/mp-weixin/utils/cart.js.map
