package com.ym.product.controller.goods;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ym.common.dto.req.PageReq;
import com.ym.common.result.Result;
import com.ym.product.dto.resp.goods.spu.GoodsSpuUserDetailResp;
import com.ym.product.dto.resp.goods.spu.GoodsSpuUserResp;
import com.ym.product.service.goods.user.IUserGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author qushutao
 * @since 2026-07-01 11:50
 **/
@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {

    private final IUserGoodsService userGoodsService;

    @GetMapping("/hot")
    public Result<IPage<GoodsSpuUserResp>> pageHotGoods(@ModelAttribute PageReq pageReq) {
        return Result.success(userGoodsService.pageHotGoods(pageReq));
    }

    @GetMapping("/{id}")
    public Result<GoodsSpuUserDetailResp> getSpuAndSkuDetailBySpuId(@PathVariable Long id) {
        return Result.success(userGoodsService.getSpuAndSkuDetailBySpuId(id));
    }
}
