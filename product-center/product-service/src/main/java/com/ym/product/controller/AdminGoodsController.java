package com.ym.product.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ym.common.dto.req.PageReq;
import com.ym.common.result.Result;
import com.ym.product.dto.req.GoodsReq;
import com.ym.product.dto.req.GoodsSkuReq;
import com.ym.product.dto.req.GoodsUpdateReq;
import com.ym.product.dto.resp.goods.sku.GoodsSkuListResp;
import com.ym.product.dto.resp.goods.spu.GoodsSpuDetailResp;
import com.ym.product.dto.resp.goods.spu.GoodsSpuListResp;
import com.ym.product.service.goods.IGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author qushutao
 * @since 2026-06-18 12:04
 **/
@RestController
@RequestMapping("/admin/goods")
@RequiredArgsConstructor
public class AdminGoodsController {

    private final IGoodsService goodsService;

    @GetMapping
    public Result<IPage<GoodsSpuListResp>> pageProduct(@ModelAttribute GoodsReq goodsReq) {
        IPage<GoodsSpuListResp> result = goodsService.pageProduct(goodsReq);
        return Result.success(result);
    }

    @PutMapping("/{id}")
    public Result<GoodsSpuListResp> updateGoods(@PathVariable Long id,
                                                @RequestBody GoodsUpdateReq goodsUpdateReq) {
        return Result.success(goodsService.updateGoods(id, goodsUpdateReq));
    }

    @GetMapping("/{id}")
    public Result<GoodsSpuDetailResp> getGood(@PathVariable Long id) {
        return Result.success(goodsService.getGood(id));
    }

    @PostMapping
    public Result<GoodsSpuListResp> saveGoods(@RequestBody GoodsUpdateReq goodsUpdateReq) {
        return Result.success(goodsService.saveGoodsSpu(goodsUpdateReq));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteGood(@PathVariable Long id) {
        goodsService.deleteGood(id);
        return Result.success();
    }

    @GetMapping("/{spuId}/skus")
    public Result<IPage<GoodsSkuListResp>> pageSkusBySpuId(@PathVariable Long spuId, @ModelAttribute PageReq pageReq) {
        return Result.success(goodsService.pageSkusBySpuId(spuId, pageReq));
    }


    @GetMapping("/all-skus")
    public Result<IPage<GoodsSkuListResp>> pageSkusBySpuId(@ModelAttribute PageReq pageReq) {
        return Result.success(goodsService.pageSkus( pageReq));
    }


    @PostMapping("/{spuId}/skus")
    public Result<Page<GoodsSkuListResp>> saveSkusBySkuId(@PathVariable Long spuId,
                                                          @RequestBody GoodsSkuReq goodsSkuReq) {
        goodsService.saveSkusBySkuId(spuId, goodsSkuReq);
        return Result.success();
    }

    @PutMapping("/{spuId}/skus/{skuId}")
    public Result<Page<GoodsSkuListResp>> updateSkusBySpuId(@PathVariable Long spuId,
                                                            @PathVariable Long skuId,
                                                            @RequestBody GoodsSkuReq goodsSkuReq) {
        goodsService.updateSkusBySkuId(spuId, skuId, goodsSkuReq);
        return Result.success();
    }


    @DeleteMapping("/skus/{skuId}")
    public Result<Page<GoodsSkuListResp>> deleteSku(@PathVariable Long skuId) {
        goodsService.deleteSku(skuId);
        return Result.success();
    }
}
