package com.ym.gateway;


import com.ym.gateway.config.MallGatewayProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * ${description}
 * @author qushutao
 * @since 2026-06-16 14:07
 **/
@EnableDiscoveryClient
@ComponentScan({"com.ym.common", "com.ym"})
// 注册配置绑定类
@EnableConfigurationProperties(MallGatewayProperties.class)
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}