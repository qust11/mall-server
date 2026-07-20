package com.ym.order.controller;


import com.ym.common.bo.CartBO;
import com.ym.common.bo.CartSkuDetailBO;
import com.ym.common.result.Result;
import com.ym.order.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qushutao
 * @since 2026-07-01 22:10
 **/
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final ICartService cartService;

    @PostMapping("/add")
    public Result<Void> addCart(@RequestBody CartBO cartBO) {
        cartService.addCart(cartBO);
        return Result.success();
    }

    @GetMapping("/sku-detail")
    public Result<List<CartSkuDetailBO>> getCartSkuDetail() {

        return Result.success(cartService.getCartSkuDetail());
    }
}
