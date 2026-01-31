package com.mars.admin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mars.common.result.Result;
import com.mars.system.annotation.EncryptResponse;
import com.mars.system.service.CryptoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 响应体加密处理器
 * 只对标注了 @EncryptResponse 的方法进行加密
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final CryptoService cryptoService;
    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 只处理标注了 @EncryptResponse 注解的方法
        return returnType.hasMethodAnnotation(EncryptResponse.class) && cryptoService.isEnabled();
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        try {
            if (body == null) {
                return body;
            }

            // 获取注解
            EncryptResponse annotation = returnType.getMethodAnnotation(EncryptResponse.class);
            if (annotation == null) {
                return body;
            }

            // 如果是Result类型，只加密data部分
            if (body instanceof Result<?> result) {
                Object data = result.getData();
                if (data != null) {
                    String jsonData = objectMapper.writeValueAsString(data);
                    String encryptedData = cryptoService.encryptResponse(jsonData);
                    return Result.ok(encryptedData);
                }
                return body;
            }

            // 其他类型，加密整个对象
            String jsonData;
            if (body instanceof String) {
                jsonData = (String) body;
            } else {
                jsonData = objectMapper.writeValueAsString(body);
            }

            // AES加密数据
            String encryptedData = cryptoService.encryptResponse(jsonData);

            // 返回加密后的结果
            return Result.ok(encryptedData);

        } catch (Exception e) {
            log.error("响应加密失败", e);
            return body;
        }
    }
}
