package com.ym.promotion.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ym.common.constant.NumberConstan;
import com.ym.common.constant.ResultCodeEnum;
import com.ym.common.enums.PromotionCouponReceivedStateEnum;
import com.ym.common.exception.BusinessException;
import com.ym.common.util.UserHolderUtil;
import com.ym.promotion.constant.PromotionRedisConstant;
import com.ym.promotion.entity.Coupon;
import com.ym.promotion.entity.CouponMember;
import com.ym.promotion.entity.Promotion;
import com.ym.promotion.mapper.CouponMemberMapper;
import com.ym.promotion.service.ICouponMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ym.promotion.service.ICouponService;
import com.ym.promotion.service.IPromotionService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
@Service
@RequiredArgsConstructor
public class CouponMemberServiceImpl extends ServiceImpl<CouponMemberMapper, CouponMember> implements ICouponMemberService {

    private final ICouponService couponService;

    private final IPromotionService promotionService;

    private final RedissonClient redisson;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveCouponMember(Long promotionId) {
        RLock lock = redisson.getLock(PromotionRedisConstant.PROMOTION_SECKILL_CREATED_KEY_PREFIX + promotionId);
        try {
            boolean isLocked = lock.tryLock(PromotionRedisConstant.EXPIRE_TIME_FIVE_SECONDS, PromotionRedisConstant.EXPIRE_TIME_TEN_SECONDS, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new BusinessException(ResultCodeEnum.COUPON_RECEIVE_HAS_ERROR);
            }
            LocalDateTime now = LocalDateTime.now();
            Promotion promotion = Optional.ofNullable( promotionService.getById(promotionId)).orElseThrow(() -> new BusinessException(ResultCodeEnum.ACTIVITY_NOT_EXIST,"优惠券信息不存在"));
            if (promotion.getStartTime().isAfter(now)){
                throw new BusinessException(ResultCodeEnum.COUPON_RECEIVE_HAS_ERROR,"优惠券领取失败，优惠券未开始");
            }
            if (promotion.getEndTime().isBefore(now)){
                throw new BusinessException(ResultCodeEnum.COUPON_RECEIVE_HAS_ERROR,"优惠券领取失败，优惠券已结束");
            }
            Coupon coupon = Optional.ofNullable(couponService.getById(promotionId)).orElseThrow(() -> new BusinessException(ResultCodeEnum.ACTIVITY_NOT_EXIST,"优惠券信息不存在"));

            if (coupon.getRemainStock() <= NumberConstan.ZERO) {
                throw new BusinessException(ResultCodeEnum.STOCK_SHORT,"优惠券库存不足");
            }
            // 优惠券扣减内存
            couponService.update(new LambdaUpdateWrapper<Coupon>().eq(Coupon::getId, promotionId).setDecrBy(Coupon::getRemainStock, NumberConstan.ONE));
            // 获取活动时间
            CouponMember couponMember = new CouponMember();
            couponMember.setExpireTime(promotion.getEndTime());
            couponMember.setReceivedTime(LocalDateTime.now());
            couponMember.setStatus(PromotionCouponReceivedStateEnum.NOT_USED.getCode());
            couponMember.setUserId(UserHolderUtil.get());
            couponMember.setCouponId(coupon.getId());
            save(couponMember);
            return couponMember.getId();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ResultCodeEnum.COUPON_RECEIVE_HAS_ERROR);
        }
    }
}
