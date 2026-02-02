package com.mars.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mars.common.result.Result;
import com.mars.system.annotation.EncryptResponse;
import com.mars.system.service.CryptoService;
import com.mars.system.service.SystemConfigHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;
import java.util.List;

/**
 * 响应体加密处理器
 * 支持全局加密和部分加密两种模式：
 * - 全局加密：所有接口返回都加密（排除公开接口）
 * - 部分加密：只对标注了 @EncryptResponse 的方法进行加密
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final CryptoService cryptoService;
    private final ObjectMapper objectMapper;
    private final SystemConfigHelper configHelper;

    /**
     * 不需要加密的公开接口路径
     */
    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/crypto/",           // 加密配置接口
            "/auth/login",        // 登录接口
            "/auth/register",     // 注册接口
            "/auth/captcha",      // 验证码接口
            "/auth/sms-code",     // 短信验证码接口
            "/sys/config-group/public",  // 公开配置接口
            "/file/",             // 文件接口
            "/sys/file/"          // 系统文件接口
    );

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 检查加密是否启用
        if (!cryptoService.isEnabled()) {
            return false;
        }

        // 检查加密范围
        if (configHelper.isGlobalEncrypt()) {
            // 全局加密模式：返回true，在beforeBodyWrite中再检查路径
            return true;
        } else {
            // 部分加密：只有标注 @EncryptResponse 注解的方法才加密
            return returnType.hasMethodAnnotation(EncryptResponse.class);
        }
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        try {
            if (body == null) {
                return body;
            }

            // 如果是 byte[] 类型，不加密（文件下载等场景）
            if (body instanceof byte[]) {
                return body;
            }

            // 全局加密模式下，检查是否是公开接口
            if (configHelper.isGlobalEncrypt()) {
                String path = request.getURI().getPath();
                for (String excludePath : EXCLUDE_PATHS) {
                    if (path.contains(excludePath)) {
                        // 公开接口不加密
                        return body;
                    }
                }
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
