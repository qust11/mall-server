package com.ym.product.controller.brand;


import com.ym.common.result.Result;
import com.ym.product.dto.resp.brands.BrandsResp;
import com.ym.product.service.goods.core.IGoodsBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qushutao
 * @since 2026-06-18 12:04
 **/
@RestController
@RequestMapping("/admin/brands")
@RequiredArgsConstructor
public class AdminGoodsBrandController {

    private final IGoodsBrandService goodsBrandService;

    @GetMapping
    public Result<List<BrandsResp>> pageBrands() {
        List<BrandsResp> result = goodsBrandService.pageBrands();
        return Result.success(result);
    }
}
