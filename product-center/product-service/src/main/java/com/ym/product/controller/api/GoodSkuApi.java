package com.ym.product.controller.api;


import com.ym.common.bo.CartSkuDetailBO;
import com.ym.common.result.Result;
import com.ym.product.api.GoodSkuClient;
import com.ym.product.dto.GoodsSkuLockDto;
import com.ym.product.service.goods.api.IApiGoodsSkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author qushutao
 * @since 2026-07-14 8:47
 **/
@RestController
@RequestMapping("/api/goods/sku")
@RequiredArgsConstructor
public class GoodSkuApi implements GoodSkuClient {

    private final IApiGoodsSkuService apiGoodsSkuService;
    @Override
    @PutMapping("/lock-stock")
    public Result<Void> skuLockStock(GoodsSkuLockDto goodsSkuLockDto) {
        apiGoodsSkuService.skuLockStock(goodsSkuLockDto);
        return Result.success();
    }

    @Override
    @GetMapping
    public Result<List<CartSkuDetailBO>> getSkuInfo(List<Long> skuIds) {
        return Result.success(apiGoodsSkuService.getSkuInfo(skuIds));
    }
}
