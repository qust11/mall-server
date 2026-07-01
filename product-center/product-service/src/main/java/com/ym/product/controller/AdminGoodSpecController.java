package com.ym.product.controller;


import com.ym.common.result.Result;
import com.ym.product.dto.resp.goodspec.GoodsSpecResp;
import com.ym.product.service.goods.IGoodsSpecService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qushutao
 * @since 2026-06-18 12:04
 **/
@RestController
@RequestMapping("/admin/goods/spec")
@RequiredArgsConstructor
public class AdminGoodSpecController {

    private final IGoodsSpecService goodsSpecService;

    @GetMapping()
    public Result<List<GoodsSpecResp>> listGoodsSpecs() {
        return Result.success(goodsSpecService.listGoodsSpecs());
    }

}
