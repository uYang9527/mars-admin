"use strict";
const common_vendor = require("../common/vendor.js");
const utils_crypto = require("./crypto.js");
const BASE_URL = "http://localhost:8080";
const request = (options) => {
  return new Promise((resolve, reject) => {
    const token = common_vendor.index.getStorageSync("token");
    const header = {
      "Content-Type": "application/json",
      ...options.header
    };
    if (token) {
      header["Authorization"] = token;
    }
    common_vendor.index.request({
      url: BASE_URL + options.url,
      method: options.method || "GET",
      data: options.data,
      header,
      success: (res) => {
        if (res.statusCode === 200) {
          const data = res.data;
          if (data.code === 200 || data.code === 0) {
            if (utils_crypto.isAesEncryptedData(data.data)) {
              utils_crypto.decryptResponseData(data.data).then((decrypted) => {
                data.data = decrypted;
                resolve(data);
              }).catch(() => {
                resolve(data);
              });
            } else {
              resolve(data);
            }
          } else if (data.code === 401) {
            common_vendor.index.removeStorageSync("token");
            common_vendor.index.removeStorageSync("userInfo");
            common_vendor.index.reLaunch({ url: "/pages/login/index" });
            reject(new Error(data.msg || "登录已过期"));
          } else {
            common_vendor.index.showToast({ title: data.msg || "请求失败", icon: "none" });
            reject(new Error(data.msg || "请求失败"));
          }
        } else if (res.statusCode === 401) {
          common_vendor.index.removeStorageSync("token");
          common_vendor.index.removeStorageSync("userInfo");
          common_vendor.index.reLaunch({ url: "/pages/login/index" });
          reject(new Error("登录已过期"));
        } else {
          common_vendor.index.showToast({ title: "网络错误", icon: "none" });
          reject(new Error("网络错误"));
        }
      },
      fail: (err) => {
        common_vendor.index.showToast({ title: "网络连接失败", icon: "none" });
        reject(err);
      }
    });
  });
};
const get = (url, data) => request({ url, method: "GET", data });
const post = (url, data) => request({ url, method: "POST", data });
const put = (url, data) => request({ url, method: "PUT", data });
const del = (url, data) => request({ url, method: "DELETE", data });
const upload = (url, filePath, name = "file") => {
  return new Promise((resolve, reject) => {
    const token = common_vendor.index.getStorageSync("token");
    common_vendor.index.uploadFile({
      url: BASE_URL + url,
      filePath,
      name,
      header: {
        "Authorization": token || ""
      },
      success: (res) => {
        if (res.statusCode === 200) {
          const data = JSON.parse(res.data);
          if (data.code === 200 || data.code === 0) {
            if (utils_crypto.isAesEncryptedData(data.data)) {
              utils_crypto.decryptResponseData(data.data).then((decrypted) => {
                data.data = decrypted;
                resolve(data);
              }).catch(() => resolve(data));
            } else {
              resolve(data);
            }
          } else {
            reject(new Error(data.msg || "上传失败"));
          }
        } else {
          reject(new Error("上传失败"));
        }
      },
      fail: reject
    });
  });
};
exports.BASE_URL = BASE_URL;
exports.del = del;
exports.get = get;
exports.post = post;
exports.put = put;
exports.upload = upload;
//# sourceMappingURL=../../.sourcemap/mp-weixin/utils/request.js.map
