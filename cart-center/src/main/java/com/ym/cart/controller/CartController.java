package com.ym.cart.controller;


import com.ym.cart.service.ICartService;
import com.ym.common.bo.CartBO;
import com.ym.common.bo.CartSkuDetailBO;
import com.ym.common.result.Result;
import com.ym.common.util.UserHolderUtil;
import lombok.Getter;
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
