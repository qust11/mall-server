package com.ym.product.api;


import com.ym.common.bo.CartSkuDetailBO;
import com.ym.common.result.Result;
import com.ym.product.dto.GoodsSkuLockDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *
 * @author qushutao
 * @since 2026-07-14 8:36
 **/
@FeignClient(name = "product-service")
public interface GoodSkuClient {
    // 路径、请求方式、参数必须和服务端对外API完全一致
    @PutMapping("/api/goods/sku/lock-stock")
    Result<Void> skuLockStock(@RequestBody GoodsSkuLockDto goodsSkuLockDto);

    // 路径、请求方式、参数必须和服务端对外API完全一致
    @GetMapping("/api/goods/sku")
    Result<List<CartSkuDetailBO>> getSkuInfo(@RequestParam List<Long> skuIds);
}
