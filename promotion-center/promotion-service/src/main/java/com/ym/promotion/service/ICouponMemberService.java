package com.ym.promotion.service;

import com.ym.promotion.entity.CouponMember;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
public interface ICouponMemberService extends IService<CouponMember> {

    Long saveCouponMember(Long couponId);

}
