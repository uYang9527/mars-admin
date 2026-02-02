package com.mars.web.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mars.system.service.CryptoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * 请求体解密处理器
 * 自动解密加密的请求数据
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class DecryptRequestBodyAdvice extends RequestBodyAdviceAdapter {

    private final CryptoService cryptoService;
    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // 只在启用加密时处理
        return cryptoService.isEnabled();
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter,
                                           Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        // 读取原始请求体（读取后流会被消耗，必须创建新的 HttpInputMessage）
        String body;
        try {
            body = new String(inputMessage.getBody().readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("读取请求体失败", e);
            return inputMessage;
        }

        // 如果请求体为空，返回空内容的 HttpInputMessage
        if (body == null || body.isEmpty()) {
            return new DecryptedHttpInputMessage(inputMessage, "");
        }

        try {
            // 解析JSON
            JsonNode jsonNode = objectMapper.readTree(body);

            // 检查是否有 encryptedData 字段（表示整体加密）
            if (jsonNode.has("encryptedData")) {
                String encryptedData = jsonNode.get("encryptedData").asText();
                String decryptedBody = cryptoService.decrypt(encryptedData);
                log.debug("请求体已解密");
                return new DecryptedHttpInputMessage(inputMessage, decryptedBody);
            }

            // 检查是否有需要解密的字段（如密码字段）
            if (jsonNode.isObject()) {
                ObjectNode objectNode = (ObjectNode) jsonNode;
                boolean modified = false;

                // 解密 password 字段
                if (objectNode.has("password") && objectNode.get("password").isTextual()) {
                    String encryptedPassword = objectNode.get("password").asText();
                    // 检查是否是加密的数据（Base64格式且长度较长）
                    if (isEncrypted(encryptedPassword)) {
                        try {
                            String decryptedPassword = cryptoService.decrypt(encryptedPassword);
                            objectNode.put("password", decryptedPassword);
                            modified = true;
                            log.debug("密码字段已解密");
                        } catch (Exception e) {
                            // 解密失败，可能是明文密码，保持原样
                            log.debug("密码字段解密失败，可能是明文");
                        }
                    }
                }

                // 解密 oldPassword 字段
                if (objectNode.has("oldPassword") && objectNode.get("oldPassword").isTextual()) {
                    String encryptedPassword = objectNode.get("oldPassword").asText();
                    if (isEncrypted(encryptedPassword)) {
                        try {
                            String decryptedPassword = cryptoService.decrypt(encryptedPassword);
                            objectNode.put("oldPassword", decryptedPassword);
                            modified = true;
                        } catch (Exception e) {
                            log.debug("oldPassword字段解密失败");
                        }
                    }
                }

                // 解密 newPassword 字段
                if (objectNode.has("newPassword") && objectNode.get("newPassword").isTextual()) {
                    String encryptedPassword = objectNode.get("newPassword").asText();
                    if (isEncrypted(encryptedPassword)) {
                        try {
                            String decryptedPassword = cryptoService.decrypt(encryptedPassword);
                            objectNode.put("newPassword", decryptedPassword);
                            modified = true;
                        } catch (Exception e) {
                            log.debug("newPassword字段解密失败");
                        }
                    }
                }

                if (modified) {
                    String modifiedBody = objectMapper.writeValueAsString(objectNode);
                    return new DecryptedHttpInputMessage(inputMessage, modifiedBody);
                }
            }

            // 没有修改，返回原始内容（必须创建新的 HttpInputMessage，因为流已被消耗）
            return new DecryptedHttpInputMessage(inputMessage, body);
        } catch (Exception e) {
            log.error("请求解密处理异常", e);
            // 出错时也要返回原始内容
            return new DecryptedHttpInputMessage(inputMessage, body);
        }
    }

    /**
     * 判断是否是加密数据
     */
    private boolean isEncrypted(String data) {
        if (data == null || data.length() < 100) {
            return false;
        }
        // 检查是否是有效的Base64
        try {
            java.util.Base64.getDecoder().decode(data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解密后的HttpInputMessage
     */
    private static class DecryptedHttpInputMessage implements HttpInputMessage {
        private final HttpInputMessage original;
        private final String body;

        public DecryptedHttpInputMessage(HttpInputMessage original, String body) {
            this.original = original;
            this.body = body;
        }

        @Override
        public InputStream getBody() {
            return new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public org.springframework.http.HttpHeaders getHeaders() {
            return original.getHeaders();
        }
    }
}
