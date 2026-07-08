package com.ym.promotion;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * ${description}
 *
 * @author qushutao
 * @since 2026-07-02 18:46
 **/
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration.class
})
@EnableDiscoveryClient
@MapperScan("com.ym.promotion.mapper")
@ComponentScans( value = {@ComponentScan("com.ym.promotion"), @ComponentScan("com.ym.common")})
@ServletComponentScan
public class PromotionApplication {
    public static void main(String[] args) {
        SpringApplication.run(PromotionApplication.class, args);
    }
}