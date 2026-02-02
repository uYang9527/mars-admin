package com.mars.system.service.pay;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.mars.system.service.SystemConfigHelper;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayService extends AbstractPayService {

    private final SystemConfigHelper configHelper;

    @Override
    public Map<String, String> createTestOrder() {
        Map<String, String> result = new HashMap<>();
        String orderNo = "WX" + IdUtil.getSnowflakeNextIdStr();
        result.put("orderNo", orderNo);

        try {
            JsonNode paymentConfig = configHelper.getConfig("payment");
            if (paymentConfig == null || paymentConfig.get("wechatPay") == null) {
                throw new RuntimeException("微信支付配置不存在");
            }

            JsonNode config = paymentConfig.get("wechatPay");
            boolean enabled = config.has("enabled") && config.get("enabled").asBoolean();
            if (!enabled) {
                throw new RuntimeException("微信支付未启用");
            }

            String mchId = getConfigValue(config, "mchId");
            String appId = getConfigValue(config, "appId");
            String apiV3Key = getConfigValue(config, "apiV3Key");
            String privateKey = getConfigValue(config, "privateKey");
            String certSerialNo = getConfigValue(config, "certSerialNo");
            String notifyUrl = getConfigValue(config, "notifyUrl");

            if (mchId.isEmpty() || appId.isEmpty() || apiV3Key.isEmpty() || privateKey.isEmpty()) {
                throw new RuntimeException("微信支付配置不完整，请检查商户号、AppID、APIv3密钥、商户私钥");
            }

            // 构建配置
            Config wechatConfig = new RSAAutoCertificateConfig.Builder()
                    .merchantId(mchId)
                    .privateKey(privateKey)
                    .merchantSerialNumber(certSerialNo)
                    .apiV3Key(apiV3Key)
                    .build();

            // 创建Native支付服务
            NativePayService service = new NativePayService.Builder().config(wechatConfig).build();

            // 构建请求
            PrepayRequest request = new PrepayRequest();
            request.setAppid(appId);
            request.setMchid(mchId);
            request.setDescription("支付测试订单");
            request.setOutTradeNo(orderNo);
            request.setNotifyUrl(notifyUrl);

            Amount amount = new Amount();
            amount.setTotal(1); // 1分钱
            amount.setCurrency("CNY");
            request.setAmount(amount);

            // 调用接口
            PrepayResponse response = service.prepay(request);
            String codeUrl = response.getCodeUrl();

            // 生成二维码
            String qrcode = generateQRCode(codeUrl);
            result.put("qrcode", qrcode);
            result.put("payUrl", codeUrl);

            log.info("微信支付测试订单创建成功: orderNo={}, codeUrl={}", orderNo, codeUrl);

        } catch (Exception e) {
            log.error("创建微信支付测试订单失败", e);
            throw new RuntimeException("创建微信支付测试订单失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public String getPayType() {
        return "wechat";
    }

    @Override
    public String getPayTypeName() {
        return "微信支付";
    }

    private String getConfigValue(JsonNode config, String key) {
        return config.has(key) && !config.get(key).isNull() ? config.get(key).asText() : "";
    }
}
