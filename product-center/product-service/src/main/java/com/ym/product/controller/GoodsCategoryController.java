package com.ym.product.controller;


import com.ym.common.result.Result;
import com.ym.product.dto.resp.category.PrimaryCategoryResp;
import com.ym.product.service.goods.core.IGoodsCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author qushutao
 * @since 2026-06-18 12:04
 **/
@RestController
@RequestMapping("/goods/category")
@RequiredArgsConstructor
public class GoodsCategoryController {

    private final IGoodsCategoryService goodsCategoryService;

    @GetMapping("/primary")
    public Result<List<PrimaryCategoryResp>> listPrimaryCategory() {
        List<PrimaryCategoryResp> result = goodsCategoryService.listPrimaryCategory();
        return Result.success(result);
    }
}
