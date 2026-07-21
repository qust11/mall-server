package com.ym.order;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * ${description}
 * @author qushutao 
 * @since 2026-06-16 14:10
 **/
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration.class,
        DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@ComponentScans( value = {@ComponentScan("com.ym.order"), @ComponentScan("com.ym.common")})
@ServletComponentScan // 关键注解，扫描@WebFilter、@WebServlet
@EnableFeignClients(basePackages = {"com.ym.product.api", "com.ym.promotion.api"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}