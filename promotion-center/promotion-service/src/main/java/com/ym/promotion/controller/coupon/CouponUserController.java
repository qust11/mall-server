package com.ym.promotion.controller.coupon;

import com.ym.common.result.Result;
import com.ym.promotion.service.ICouponMemberService;
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
@RequestMapping("/promotion/coupon/user")
@RequiredArgsConstructor
public class CouponUserController {

    private final ICouponMemberService couponMemberService;

    @PostMapping("/receive/{couponId}")
    public Result<Long> receiveCoupon(@PathVariable Long couponId) {
        return Result.success(couponMemberService.saveCouponMember(couponId));
    }

}
