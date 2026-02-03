"use strict";
const common_vendor = require("../common/vendor.js");
const getBaseUrl = () => {
  return getApp().globalData.baseUrl || "http://localhost:8080";
};
const getMemberId = () => {
  const memberInfo = common_vendor.index.getStorageSync("memberInfo");
  return memberInfo ? memberInfo.memberId : null;
};
const getToken = () => {
  return common_vendor.index.getStorageSync("token") || "";
};
const request = (options) => {
  return new Promise((resolve, reject) => {
    const baseUrl = getBaseUrl();
    const url = options.url.startsWith("http") ? options.url : baseUrl + options.url;
    let data = options.data || {};
    const memberId = getMemberId();
    if (memberId && !data.memberId) {
      if (options.method === "GET") {
        data.memberId = memberId;
      }
    }
    const token = getToken();
    const header = {
      "Content-Type": options.contentType || "application/json",
      ...options.header
    };
    if (token) {
      header["Authorization"] = token;
    }
    common_vendor.index.request({
      url,
      method: options.method || "GET",
      data,
      header,
      success: (res) => {
        if (res.statusCode === 200) {
          if (res.data.code === 200) {
            resolve(res.data);
          } else if (res.data.code === 401 || res.data.code === 11011 || res.data.code === 11012) {
            common_vendor.index.removeStorageSync("token");
            common_vendor.index.removeStorageSync("memberInfo");
            common_vendor.index.showToast({
              title: "请先登录",
              icon: "none"
            });
            setTimeout(() => {
              common_vendor.index.navigateTo({ url: "/pages/login/index" });
            }, 500);
            reject(res.data);
          } else {
            common_vendor.index.showToast({
              title: res.data.message || "请求失败",
              icon: "none"
            });
            reject(res.data);
          }
        } else if (res.statusCode === 401) {
          common_vendor.index.removeStorageSync("token");
          common_vendor.index.removeStorageSync("memberInfo");
          common_vendor.index.showToast({
            title: "请先登录",
            icon: "none"
          });
          setTimeout(() => {
            common_vendor.index.navigateTo({ url: "/pages/login/index" });
          }, 500);
          reject(res);
        } else {
          common_vendor.index.showToast({
            title: "网络请求失败",
            icon: "none"
          });
          reject(res);
        }
      },
      fail: (err) => {
        common_vendor.index.showToast({
          title: "网络连接失败",
          icon: "none"
        });
        reject(err);
      }
    });
  });
};
const get = (url, data = {}) => {
  return request({ url, method: "GET", data });
};
const post = (url, data = {}) => {
  return request({ url, method: "POST", data });
};
const put = (url, data = {}) => {
  return request({ url, method: "PUT", data });
};
exports.get = get;
exports.post = post;
exports.put = put;
//# sourceMappingURL=../../.sourcemap/mp-weixin/utils/request.js.map
