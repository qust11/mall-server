package com.ym.promotion.controller;

import com.ym.common.result.Result;
import com.ym.promotion.dto.req.CouponReq;
import com.ym.promotion.service.abst.impl.CouponPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 优惠券前端控制器
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
@RestController
@RequestMapping("/promotion/coupon")
@RequiredArgsConstructor
public class CouponController {
    private final CouponPromotionService couponPromotionService;


    @PostMapping
    public Result<Long> createCoupon(@RequestBody CouponReq couponReq) {
        return Result.success(couponPromotionService.savePromotion( couponReq));
    }
}
