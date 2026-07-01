package com.ym.gateway.config;


import com.ym.common.result.Result;
import com.ym.common.util.JwtUtil;
import com.ym.common.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

import java.nio.charset.StandardCharsets;

/**
 *
 * @author qushutao
 * @since 2026-06-16 21:11
 **/
@Slf4j
@Configuration
public class GatewayAuthFilterConfig {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MallGatewayProperties gatewayProperties;

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String IP_LIMIT_KEY = "gateway:ip:limit:";
    private static final String USER_LOGIN_KEY = "gateway:login:user:";

    /**
     * 全局过滤器，order越小越先执行
     */
    @Bean
    @Order(-100)
    public GlobalFilter authGlobalFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            String path = request.getPath().value();

            // 1. 拦截黑名单接口
            if (isBlackApi(path)) {
                return writeError(response, 403, "该接口禁止访问");
            }

            // 2. 白名单接口直接放行，无需校验token
            if (isWhiteApi(path)) {
                return chain.filter(exchange);
            }

            // 2. 商品信息直接放行
            if (path.startsWith("/goods")) {
                return chain.filter(exchange);
            }

            // 3. IP限流防刷校验
            String clientIp = getClientIp(request);
            boolean limitPass = checkIpLimit(clientIp);
            if (!limitPass) {
                return writeError(response, 429, "访问过于频繁，请稍后再试");
            }


            // 4. Token鉴权、过期、登录状态校验
            String token = getToken(request);
            if (StringUtils.isBlank(token)) {
                return writeError(response, 401, "未携带登录凭证，请登录");
            }

            try {
                // 校验token合法性与过期
                jwtUtil.validateToken(token);
                Long userId = jwtUtil.getUserIdFromToken(token);
//                // Redis校验登录状态（退出登录会删除该key）
//                Boolean loginExist = redisUtil.hasKey(USER_LOGIN_KEY + userId);
//                if (BooleanUtils.isFalse(loginExist)) {
//                    return writeError(response, 401, "登录已失效，请重新登录");
//                }
                // 透传用户ID到下游微服务
                ServerHttpRequest newReq = request.mutate()
                        .header("X-User-Id", userId.toString())
                        .build();
                return chain.filter(exchange.mutate().request(newReq).build());
            } catch (RuntimeException e) {
                return writeError(response, 401, e.getMessage());
            }
        };
    }

    /**
     * 判断是否黑名单接口
     */
    private boolean isBlackApi(String path) {
        for (String black : gatewayProperties.getBlackListApis()) {
            if (path.startsWith(black)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否白名单接口
     */
    private boolean isWhiteApi(String path) {
        for (String white : gatewayProperties.getWhiteListApis()) {
            if (path.startsWith(white)) {
                return true;
            }
        }
        return false;
    }

    /**
     * IP限流计数
     */
    private boolean checkIpLimit(String ip) {
        String key = IP_LIMIT_KEY + ip;
        long count = redisUtil.increment(key);
        // 第一次设置过期时间
        if (count == 1) {
            redisUtil.expireSecond(key, gatewayProperties.getLimit().getExpireSecond());
        }
        return count <= gatewayProperties.getLimit().getIpMax();
    }

    /**
     * 获取客户端真实IP
     */
    private String getClientIp(ServerHttpRequest request) {
        String xForwarded = request.getHeaders().getFirst("X-Forwarded-For");
        if (StringUtils.isNotBlank(xForwarded)) {
            return xForwarded.split(",")[0].trim();
        }
        return request.getRemoteAddress().getAddress().getHostAddress();
    }

    /**
     * 从请求头提取Token
     */
    private String getToken(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst(TOKEN_HEADER);
        if (StringUtils.isNotBlank(header) && header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    /**
     * 统一返回错误响应
     */
    private Mono<Void> writeError(ServerHttpResponse response, Integer code, String msg) {
        // 1. 强制设置响应状态码（兼容不同Web容器）
        response.setStatusCode(HttpStatus.valueOf(code));
        // 2. 明确设置响应头（包括Content-Length，避免客户端解析异常）
        HttpHeaders headers = response.getHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8); // 显式指定UTF-8
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        // 3. 确保Result序列化正常（兜底空值处理）
        Result<Object> result = Result.fail(code, StringUtils.isBlank(msg) ? "请求失败" : msg);
        String json;
        try {
            json = JSON.toJSONString(result);
            // 兜底：若序列化结果为空，手动构造JSON
            if (StringUtils.isBlank(json)) {
                json = String.format("{\"code\":%d,\"msg\":\"%s\",\"data\":null}", code, msg);
            }
        } catch (Exception e) {
            json = String.format("{\"code\":%d,\"msg\":\"%s\",\"data\":null}", code, "系统异常");
            log.error("错误响应序列化失败", e);
        }

        // 4. 写入响应体并确保流完成（关键：使用Mono.fromSupplier + then()保证完成）
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        headers.setContentLength(bytes.length); // 显式设置Content-Length
        DataBuffer buffer = response.bufferFactory().wrap(bytes);

        // 响应式写回：确保流完成，避免客户端未接收到数据
        return response.writeWith(Mono.just(buffer))
                .doOnError(e -> log.error("写入错误响应失败", e))
                .then(); // 确保响应流完成
    }
}
