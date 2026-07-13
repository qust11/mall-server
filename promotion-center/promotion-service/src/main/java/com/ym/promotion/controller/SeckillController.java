package com.ym.promotion.controller;

import com.ym.common.result.Result;
import com.ym.promotion.dto.req.SeckillReq;
import com.ym.promotion.dto.resp.SeckillResp;
import com.ym.promotion.service.ISeckillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    private final ISeckillService seckillService;

    @PostMapping
    public Result<Long> createSeckill(@RequestBody SeckillReq seckillReq) {
        return Result.success(seckillService.savePromotionInfo( seckillReq));
    }

    @GetMapping("/{promotionId}")
    public Result<SeckillResp> getSeckill(@PathVariable Long promotionId) {
        return Result.success(seckillService.getSeckillByPromotionId(promotionId));
    }

    @PutMapping("/{id}")
    public Result<Void> updateSeckill(@PathVariable Long id, @RequestBody SeckillReq seckillReq) {
        seckillService.updatePromotionInfo(id, seckillReq);
        return Result.success();
    }

    @DeleteMapping("/{promotionId}")
    public Result<Void> deleteSeckill(@PathVariable Long promotionId) {
        seckillService.deletePromoInfo(promotionId);
        return Result.success();
    }
}
