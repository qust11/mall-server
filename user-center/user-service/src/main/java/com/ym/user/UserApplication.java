package com.ym.user;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * ${description}
 *
 * @author qushutao
 * @since 2026-06-16 14:12
 **/
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration.class
})
@EnableDiscoveryClient
@MapperScan("com.ym.user.mapper")
@ComponentScan(value = {"com.ym.*"})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
