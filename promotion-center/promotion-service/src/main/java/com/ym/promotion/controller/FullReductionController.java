package com.ym.promotion.controller;

import com.ym.common.result.Result;
import com.ym.promotion.dto.PromotionBaseDto;
import com.ym.promotion.dto.req.CouponReq;
import com.ym.promotion.dto.req.FullReductionReq;
import com.ym.promotion.dto.resp.FullReductionResp;
import com.ym.promotion.service.IPromotionService;
import com.ym.promotion.service.abst.impl.FullReductionPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
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
    private final FullReductionPromotionService fullReductionPromotionService;

    @PostMapping
    public Result<Long> createCoupon(@RequestBody FullReductionReq fullReductionReq) {
        return Result.success(fullReductionPromotionService.savePromotion( fullReductionReq));
    }
}
