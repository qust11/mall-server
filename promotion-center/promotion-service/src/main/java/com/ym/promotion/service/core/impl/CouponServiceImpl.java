package com.ym.promotion.service.core.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ym.common.constant.ResultCodeEnum;
import com.ym.common.exception.BusinessException;
import com.ym.common.util.UserHolderUtil;
import com.ym.promotion.converter.CouponConverter;
import com.ym.promotion.dto.CouponDto;
import com.ym.promotion.dto.PromotionBaseDto;
import com.ym.promotion.dto.PromotionDetailDto;
import com.ym.promotion.dto.req.CouponReq;
import com.ym.promotion.dto.resp.CouponResp;
import com.ym.promotion.entity.Coupon;
import com.ym.promotion.entity.Promotion;
import com.ym.promotion.mapper.CouponMapper;
import com.ym.promotion.service.core.ICouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ym.promotion.service.core.IPromotionService;
import com.ym.promotion.service.user.ClientPromotionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 *  服务实现�?
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
@Service
@RequiredArgsConstructor
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements ICouponService, ClientPromotionService {

    private final IPromotionService promotionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends PromotionBaseDto> Long savePromotionInfo(T promotionBaseDto) {
        Long promotionId = promotionService.savePromotion(promotionBaseDto);
        CouponReq couponReq = (CouponReq) promotionBaseDto;
        Coupon coupon = getCoupon(couponReq);
        coupon.setPromotionId(promotionId);
        save(coupon);
        return coupon.getId();
    }

    @Override
    public <T extends PromotionBaseDto> void updatePromotionInfo(Long id, T promotionBaseDto) {

        Coupon dbCoupon = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(ResultCodeEnum.ACTIVITY_NOT_EXIST));
        promotionService.updatePromoById(dbCoupon.getPromotionId(), promotionBaseDto);

        CouponReq couponReq = (CouponReq) promotionBaseDto;
        Coupon coupon = getCoupon(couponReq);
        coupon.setId(dbCoupon.getId());
        updateById(coupon);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePromoInfo(Long promotionId) {
        Coupon dbCoupon = Optional.ofNullable(this.getOne(new LambdaQueryWrapper<Coupon>().eq(Coupon::getPromotionId, promotionId))).orElseThrow(() -> new BusinessException(ResultCodeEnum.ACTIVITY_NOT_EXIST));
        promotionService.removeById(promotionId);
        this.removeById(dbCoupon.getId());
    }

    private static Coupon getCoupon(CouponReq couponReq) {
        Coupon coupon = CouponConverter.INSTANCE.toCoupon(couponReq);
        if (CollectionUtils.isNotEmpty(couponReq.getSpuIds())) {
            coupon.setSpuIds(StringUtils.join(couponReq.getSpuIds(), ","));
        }
        if (CollectionUtils.isNotEmpty(couponReq.getCategoryIds())) {
            coupon.setCategoryIds(StringUtils.join(couponReq.getCategoryIds(), ","));
        }
        return coupon;
    }

    @Override
    public CouponResp getCouponByPromotionId(Long promotionId) {
        Promotion promotion = Optional.ofNullable(promotionService.getById(promotionId)).orElseThrow(() -> new BusinessException(ResultCodeEnum.ACTIVITY_NOT_EXIST));
        Coupon coupon = this.getOne(new LambdaQueryWrapper<Coupon>().eq(Coupon::getPromotionId, promotionId));
        CouponResp couponResp = CouponConverter.INSTANCE.toCouponResp(promotion, coupon);
        couponResp.setSpuIds(StringUtils.isNotBlank(coupon.getSpuIds()) ? Arrays.stream(coupon.getSpuIds().split(",")).map(Long::valueOf).toList() : null);
        couponResp.setCategoryIds(StringUtils.isNotBlank(coupon.getCategoryIds()) ? Arrays.stream(coupon.getCategoryIds().split(",")).map(Long::valueOf).toList() : null);
        return couponResp;
    }

    @Override
    public void getPromotionByUser(PromotionDetailDto promotionDetailDto, List<Long> skuIds) {
        Long userId = UserHolderUtil.get();
        List<CouponDto> couponList = baseMapper.getCouponByUser(userId, skuIds);
        promotionDetailDto.setCouponList(couponList);
    }
}
