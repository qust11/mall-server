package com.ym.cart.controller;


import com.ym.common.bo.CartBO;
import com.ym.common.util.UserHolderUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qushutao
 * @since 2026-07-01 22:10
 **/
@RestController
@RequestMapping("/cart")
public class CartController {

    @PostMapping("/add")
    public String cartAdd(@RequestBody CartBO cartBO) {
        Long l = UserHolderUtil.get();
        System.out.println(l);
        return "cart";
    }
}
