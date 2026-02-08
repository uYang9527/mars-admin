"use strict";
const common_vendor = require("../common/vendor.js");
const TOKEN_KEY = "token";
const USER_KEY = "userInfo";
const getToken = () => {
  return common_vendor.index.getStorageSync(TOKEN_KEY);
};
const setToken = (token) => {
  common_vendor.index.setStorageSync(TOKEN_KEY, token);
};
const removeToken = () => {
  common_vendor.index.removeStorageSync(TOKEN_KEY);
};
const getUserInfo = () => {
  const info = common_vendor.index.getStorageSync(USER_KEY);
  return info ? typeof info === "string" ? JSON.parse(info) : info : null;
};
const setUserInfo = (userInfo) => {
  common_vendor.index.setStorageSync(USER_KEY, userInfo);
};
const removeUserInfo = () => {
  common_vendor.index.removeStorageSync(USER_KEY);
};
const isLoggedIn = () => {
  return !!getToken();
};
const clearAuth = () => {
  removeToken();
  removeUserInfo();
};
const checkLogin = () => {
  if (!isLoggedIn()) {
    common_vendor.index.reLaunch({ url: "/pages/login/index" });
    return false;
  }
  return true;
};
exports.checkLogin = checkLogin;
exports.clearAuth = clearAuth;
exports.getToken = getToken;
exports.getUserInfo = getUserInfo;
exports.isLoggedIn = isLoggedIn;
exports.setToken = setToken;
exports.setUserInfo = setUserInfo;
//# sourceMappingURL=../../.sourcemap/mp-weixin/utils/auth.js.map
