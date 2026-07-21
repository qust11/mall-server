package com.ym.promotion.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ym.promotion.dto.CouponDto;
import com.ym.promotion.entity.Coupon;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
public interface CouponMapper extends BaseMapper<Coupon> {

    List<CouponDto> getCouponByUser(Long userId, List<Long> skuIds);

}
