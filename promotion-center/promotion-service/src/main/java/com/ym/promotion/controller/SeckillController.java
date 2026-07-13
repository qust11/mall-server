package com.ym.promotion.controller;

import com.ym.common.result.Result;
import com.ym.promotion.dto.req.FullReductionReq;
import com.ym.promotion.dto.req.SeckillReq;
import com.ym.promotion.service.abst.impl.FullReductionPromotionService;
import com.ym.promotion.service.abst.impl.SeckillPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  优惠券前端控制器
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
@RestController
@RequestMapping("/promotion/seckill")
@RequiredArgsConstructor
public class SeckillController {
    private final SeckillPromotionService seckillPromotionService;

    @PostMapping
    public Result<Long> createCoupon(@RequestBody SeckillReq seckillReq) {
        return Result.success(seckillPromotionService.savePromotion( seckillReq));
    }
}
