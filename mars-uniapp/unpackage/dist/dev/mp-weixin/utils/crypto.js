"use strict";
const common_vendor = require("../common/vendor.js");
const BASE_URL = "http://localhost:8080";
let cryptoConfigCache = null;
function fetchCryptoConfig() {
  if (cryptoConfigCache) {
    return Promise.resolve(cryptoConfigCache);
  }
  return new Promise((resolve) => {
    common_vendor.index.request({
      url: BASE_URL + "/api/crypto/config",
      method: "GET",
      header: { "Content-Type": "application/json" },
      success: (res) => {
        if (res.statusCode === 200 && res.data && res.data.code === 200) {
          cryptoConfigCache = res.data.data;
          resolve(cryptoConfigCache);
        } else {
          resolve({ enabled: false, publicKey: "", aesKey: "" });
        }
      },
      fail: () => {
        common_vendor.index.__f__("error", "at utils/crypto.js:37", "[Crypto] 获取加密配置失败");
        resolve({ enabled: false, publicKey: "", aesKey: "" });
      }
    });
  });
}
function isAesEncryptedData(data) {
  if (typeof data !== "string") {
    return false;
  }
  const parts = data.split(".");
  if (parts.length !== 2) {
    return false;
  }
  const base64Regex = /^[A-Za-z0-9+/]+=*$/;
  if (!base64Regex.test(parts[0]) || !base64Regex.test(parts[1])) {
    return false;
  }
  return parts[0].length === 16 && parts[1].length > 10;
}
function aesGcmDecrypt(encryptedData, aesKeyBase64) {
  const parts = encryptedData.split(".");
  if (parts.length !== 2) {
    throw new Error("加密数据格式错误");
  }
  const iv = common_vendor.CryptoJS.enc.Base64.parse(parts[0]);
  const data = common_vendor.CryptoJS.enc.Base64.parse(parts[1]);
  const key = common_vendor.CryptoJS.enc.Base64.parse(aesKeyBase64);
  const ciphertextSigBytes = data.sigBytes - 16;
  const ciphertext = data.clone();
  ciphertext.sigBytes = ciphertextSigBytes;
  ciphertext.clamp();
  const counterWords = iv.words.slice(0, 3);
  counterWords.push(2);
  const counter = common_vendor.CryptoJS.lib.WordArray.create(counterWords, 16);
  const cipherParams = common_vendor.CryptoJS.lib.CipherParams.create({ ciphertext });
  const decrypted = common_vendor.CryptoJS.AES.decrypt(cipherParams, key, {
    iv: counter,
    mode: common_vendor.CryptoJS.mode.CTR,
    padding: common_vendor.CryptoJS.pad.NoPadding
  });
  return decrypted.toString(common_vendor.CryptoJS.enc.Utf8);
}
async function decryptResponseData(data) {
  const config = await fetchCryptoConfig();
  if (!config.aesKey) {
    return data;
  }
  try {
    const decryptedStr = aesGcmDecrypt(data, config.aesKey);
    return JSON.parse(decryptedStr);
  } catch (error) {
    common_vendor.index.__f__("error", "at utils/crypto.js:133", "[Crypto] 响应解密失败", error);
    cryptoConfigCache = null;
    return data;
  }
}
exports.decryptResponseData = decryptResponseData;
exports.fetchCryptoConfig = fetchCryptoConfig;
exports.isAesEncryptedData = isAesEncryptedData;
//# sourceMappingURL=../../.sourcemap/mp-weixin/utils/crypto.js.map
