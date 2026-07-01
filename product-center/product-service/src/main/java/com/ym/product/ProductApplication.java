package com.ym.product;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * ${description}
 * @author qushutao 
 * @since 2026-06-16 14:11
 **/
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration.class
})
@EnableDiscoveryClient
@MapperScan("com.ym.product.mapper")
@ComponentScans( value = {@ComponentScan("com.ym.product"), @ComponentScan("com.ym.common")})
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}