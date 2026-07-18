package com.ym.cart;


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
 *
 * @author qushutao
 * @since 2026-07-01 21:52
 **/
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration.class,
        DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@ComponentScans( value = {@ComponentScan("com.ym.cart"), @ComponentScan("com.ym.common")})
@ServletComponentScan // 关键注解，扫描@WebFilter、@WebServlet
@EnableFeignClients(basePackages = "com.ym.product.api")
public class CartApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartApplication.class, args);
    }
}