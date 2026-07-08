package com.ym.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;
/**
 *
 * @author qushutao
 * @since 2026-06-16 21:52
 **/

@Data
@ConfigurationProperties(prefix = "gateway")
public class MallGatewayProperties {
    // 白名单接口
    private List<String> whiteListApis;
    // 黑名单接口
    private List<String> blackListApis;
    // 限流配置子对象
    private Limit limit;

    @Data
    public static class Limit {
        // 单ip最大次数
        private Integer ipMax = 1000;
        // 过期秒数
        private Integer expireSecond = 600;
    }
}