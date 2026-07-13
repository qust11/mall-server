package com.ym.promotion.controller.coupon;

import com.ym.common.result.Result;
import com.ym.promotion.dto.req.CouponReq;
import com.ym.promotion.dto.resp.CouponResp;
import com.ym.promotion.service.ICouponService;
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

    private final ICouponService couponService;

    @PostMapping
    public Result<Long> createCoupon(@RequestBody CouponReq couponReq) {
        return Result.success(couponService.savePromotionInfo(couponReq));
    }

    @GetMapping("/{promotionId}")
    public Result<CouponResp> getCoupon(@PathVariable Long promotionId) {
        return Result.success(couponService.getCouponByPromotionId(promotionId));
    }

    @PutMapping("/{id}")
    public Result<Void> updateCoupon(@PathVariable Long id, @RequestBody CouponReq couponReq) {
        couponService.updatePromotionInfo(id, couponReq);
        return Result.success();
    }

    @DeleteMapping("/{promotionId}")
    public Result<Void> deleteCoupon(@PathVariable Long promotionId) {
        couponService.deletePromoInfo(promotionId);
        return Result.success();
    }
}
