package com.ym.product.controller.api;


import com.ym.common.result.Result;
import com.ym.product.api.GoodSkuClient;
import com.ym.product.dto.GoodsSkuLockDto;
import com.ym.product.service.goods.core.IGoodsSkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author qushutao
 * @since 2026-07-14 8:47
 **/
@RestController
@RequestMapping("/api/goods/sku")
@RequiredArgsConstructor
public class GoodSkuApi implements GoodSkuClient {

    private final IGoodsSkuService goodsSkuService;
    @Override
    @PutMapping("/lock-stock")
    public Result<Void> skuLockStock(GoodsSkuLockDto goodsSkuLockDto) {
        goodsSkuService.skuLockStock(goodsSkuLockDto);
        return Result.success();
    }
}
