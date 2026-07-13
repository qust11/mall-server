package com.ym.promotion.controller;

import com.ym.common.result.Result;
import com.ym.promotion.dto.req.FullReductionReq;
import com.ym.promotion.dto.resp.FullReductionResp;
import com.ym.promotion.service.IFullReductionService;
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
@RequestMapping("/promotion/full-reduction")
@RequiredArgsConstructor
public class FullReductionController {

    private final IFullReductionService fullReductionService;
    @PostMapping
    public Result<Long> createFullReduction(@RequestBody FullReductionReq fullReductionReq) {
        return Result.success(fullReductionService.savePromotionInfo( fullReductionReq));
    }


    @GetMapping("/{promotionId}")
    public Result<FullReductionResp> getFullReduction(@PathVariable Long promotionId) {
        return Result.success(fullReductionService.getFullReductionByPromotionId(promotionId));
    }

    @PutMapping("/{id}")
    public Result<Void> updateCoupon(@PathVariable Long id, @RequestBody FullReductionReq fullReductionReq) {
        fullReductionService.updatePromotionInfo(id, fullReductionReq);
        return Result.success();
    }

    @DeleteMapping("/{promotionId}")
    public Result<Void> deleteFullReduction(@PathVariable Long promotionId) {
        fullReductionService.deletePromoInfo(promotionId);
        return Result.success();
    }
}
