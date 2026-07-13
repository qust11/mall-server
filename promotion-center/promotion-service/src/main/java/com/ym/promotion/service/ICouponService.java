package com.ym.promotion.service;

import com.ym.promotion.dto.resp.CouponResp;
import com.ym.promotion.entity.Coupon;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
public interface ICouponService extends IService<Coupon>, IPromotionCommonService {

    CouponResp getCouponByPromotionId(Long promotionId);


}
