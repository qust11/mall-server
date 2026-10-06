package com.ym.promotion.service.core;

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

    /**
     * 将 coupon.spu_ids 逗号串存量数据迁移至 coupon_spu 关联表,幂等可重复执行
     *
     * @return 本次迁移的优惠券数量
     */
    int migrateSpuRangeToRelation();


}
