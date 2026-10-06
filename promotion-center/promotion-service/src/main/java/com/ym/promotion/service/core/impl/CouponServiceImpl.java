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
import com.ym.promotion.entity.CouponSpu;
import com.ym.promotion.entity.Promotion;
import com.ym.promotion.mapper.CouponMapper;
import com.ym.promotion.mapper.CouponSpuMapper;
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
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    private final CouponSpuMapper couponSpuMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends PromotionBaseDto> Long savePromotionInfo(T promotionBaseDto) {
        Long promotionId = promotionService.savePromotion(promotionBaseDto);
        CouponReq couponReq = (CouponReq) promotionBaseDto;
        Coupon coupon = getCoupon(couponReq);
        coupon.setPromotionId(promotionId);
        save(coupon);
        saveCouponSpuRelations(coupon.getId(), couponReq.getSpuIds());
        return coupon.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends PromotionBaseDto> void updatePromotionInfo(Long id, T promotionBaseDto) {

        Coupon dbCoupon = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(ResultCodeEnum.ACTIVITY_NOT_EXIST));
        promotionService.updatePromoById(dbCoupon.getPromotionId(), promotionBaseDto);

        CouponReq couponReq = (CouponReq) promotionBaseDto;
        Coupon coupon = getCoupon(couponReq);
        coupon.setId(dbCoupon.getId());
        updateById(coupon);
        // 适用商品范围以关联表为唯一数据源,更新时整体重写(范围类型切换时也自然清理)
        rewriteCouponSpuRelations(coupon.getId(), couponReq.getSpuIds());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePromoInfo(Long promotionId) {
        Coupon dbCoupon = Optional.ofNullable(this.getOne(new LambdaQueryWrapper<Coupon>().eq(Coupon::getPromotionId, promotionId))).orElseThrow(() -> new BusinessException(ResultCodeEnum.ACTIVITY_NOT_EXIST));
        promotionService.removeById(promotionId);
        this.removeById(dbCoupon.getId());
        couponSpuMapper.delete(new LambdaQueryWrapper<CouponSpu>().eq(CouponSpu::getCouponId, dbCoupon.getId()));
    }

    private static Coupon getCoupon(CouponReq couponReq) {
        Coupon coupon = CouponConverter.INSTANCE.toCoupon(couponReq);
        // spuIds 由 coupon_spu 关联表维护,不再落逗号串
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
        couponResp.setSpuIds(getSpuIdsByCouponId(coupon.getId()));
        couponResp.setCategoryIds(splitToIds(coupon.getCategoryIds()));
        return couponResp;
    }

    @Override
    public void getPromotionByUser(PromotionDetailDto promotionDetailDto, List<Long> skuIds) {
        Long userId = UserHolderUtil.get();
        List<CouponDto> couponList = baseMapper.getCouponByUser(userId, skuIds);
        if (CollectionUtils.isNotEmpty(couponList)) {
            List<Long> couponIds = couponList.stream().map(CouponDto::getId).toList();
            // 指定商品范围来自 coupon_spu 关联表;指定类目范围仍为逗号串
            Map<Long, List<Long>> spuIdsMap = getSpuIdsByCouponIds(couponIds);
            Map<Long, Coupon> couponMap = listByIds(couponIds)
                    .stream().collect(Collectors.toMap(Coupon::getId, Function.identity(), (a, b) -> a));
            for (CouponDto couponDto : couponList) {
                couponDto.setSpuIds(spuIdsMap.get(couponDto.getId()));
                Coupon coupon = couponMap.get(couponDto.getId());
                if (null == coupon) {
                    continue;
                }
                couponDto.setCategoryIds(splitToIds(coupon.getCategoryIds()));
            }
        }
        promotionDetailDto.setCouponList(couponList);
    }

    private List<Long> splitToIds(String ids) {
        if (StringUtils.isBlank(ids)) {
            return null;
        }
        return Arrays.stream(ids.split(",")).filter(StringUtils::isNotBlank).map(Long::valueOf).toList();
    }

    // ---------- coupon_spu 关联表维护 ----------

    private void saveCouponSpuRelations(Long couponId, List<Long> spuIds) {
        if (CollectionUtils.isEmpty(spuIds)) {
            return;
        }
        for (Long spuId : spuIds) {
            CouponSpu couponSpu = new CouponSpu();
            couponSpu.setCouponId(couponId);
            couponSpu.setSpuId(spuId);
            couponSpuMapper.insert(couponSpu);
        }
    }

    private void rewriteCouponSpuRelations(Long couponId, List<Long> spuIds) {
        couponSpuMapper.delete(new LambdaQueryWrapper<CouponSpu>().eq(CouponSpu::getCouponId, couponId));
        saveCouponSpuRelations(couponId, spuIds);
    }

    private List<Long> getSpuIdsByCouponId(Long couponId) {
        return couponSpuMapper.selectList(new LambdaQueryWrapper<CouponSpu>().eq(CouponSpu::getCouponId, couponId))
                .stream().map(CouponSpu::getSpuId).toList();
    }

    private Map<Long, List<Long>> getSpuIdsByCouponIds(List<Long> couponIds) {
        if (CollectionUtils.isEmpty(couponIds)) {
            return Map.of();
        }
        return couponSpuMapper.selectList(new LambdaQueryWrapper<CouponSpu>().in(CouponSpu::getCouponId, couponIds))
                .stream().collect(Collectors.groupingBy(CouponSpu::getCouponId, Collectors.mapping(CouponSpu::getSpuId, Collectors.toList())));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int migrateSpuRangeToRelation() {
        List<Coupon> coupons = list(new LambdaQueryWrapper<Coupon>().isNotNull(Coupon::getSpuIds).ne(Coupon::getSpuIds, ""));
        int migrated = 0;
        for (Coupon coupon : coupons) {
            List<Long> spuIds = splitToIds(coupon.getSpuIds());
            if (CollectionUtils.isEmpty(spuIds)) {
                continue;
            }
            // 已有关联的跳过,保证幂等
            Set<Long> existSpuIds = getSpuIdsByCouponId(coupon.getId()).stream().collect(Collectors.toSet());
            for (Long spuId : spuIds) {
                if (existSpuIds.contains(spuId)) {
                    continue;
                }
                CouponSpu couponSpu = new CouponSpu();
                couponSpu.setCouponId(coupon.getId());
                couponSpu.setSpuId(spuId);
                couponSpuMapper.insert(couponSpu);
            }
            // 迁移完成清空逗号串,避免双数据源
            Coupon update = new Coupon();
            update.setId(coupon.getId());
            update.setSpuIds("");
            updateById(update);
            migrated++;
        }
        return migrated;
    }
}
