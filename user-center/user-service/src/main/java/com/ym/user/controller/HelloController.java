package com.ym.user.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author qushutao
 * @since 2026-06-16 14:48
 **/
@RestController
@RequestMapping("/user")
public class HelloController {
    @RequestMapping("/hello")
    public String hello() {
        return "hello user";
    }
}
