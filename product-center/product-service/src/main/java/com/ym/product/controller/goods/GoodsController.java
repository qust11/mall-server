package com.ym.product.controller.goods;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ym.common.dto.req.PageReq;
import com.ym.common.result.Result;
import com.ym.product.dto.resp.goods.spu.GoodsSpuResp;
import com.ym.product.service.goods.client.IUserGoodsService;
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
    public Result<IPage<GoodsSpuResp>> pageHotGoods(@ModelAttribute PageReq pageReq) {
        return Result.success(userGoodsService.pageHotGoods(pageReq));
    }
}
