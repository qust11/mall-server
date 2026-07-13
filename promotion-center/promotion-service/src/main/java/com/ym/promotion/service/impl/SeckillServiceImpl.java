package com.ym.promotion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ym.common.constant.ResultCodeEnum;
import com.ym.common.exception.BusinessException;
import com.ym.promotion.constant.PromotionRedisConstant;
import com.ym.promotion.converter.SeckillConverter;
import com.ym.promotion.dto.PromotionBaseDto;
import com.ym.promotion.dto.req.SeckillReq;
import com.ym.promotion.dto.resp.SeckillResp;
import com.ym.promotion.entity.Promotion;
import com.ym.promotion.entity.Seckill;
import com.ym.promotion.mapper.SeckillMapper;
import com.ym.promotion.service.IPromotionService;
import com.ym.promotion.service.ISeckillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现�?
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
@Service
@RequiredArgsConstructor
public class SeckillServiceImpl extends ServiceImpl<SeckillMapper, Seckill> implements ISeckillService {

    private final IPromotionService promotionService;

    private final RedissonClient redisson;

    @Override
    public <T extends PromotionBaseDto> Long savePromotionInfo(T promotionBaseDto) {
        RLock lock = redisson.getLock(PromotionRedisConstant.PROMOTION_SECKILL_CREATED_KEY_PREFIX);
        try {
            boolean isLocked = lock.tryLock(PromotionRedisConstant.EXPIRE_TIME_FIVE_SECONDS, PromotionRedisConstant.EXPIRE_TIME_TEN_SECONDS, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new BusinessException(ResultCodeEnum.ACTIVITY_CREATE_FAILED,"活动创建失败，获取锁信息失败");
            }
            // 查询是否有相同时间段的秒杀商品 如果有则不允许新增秒杀活动
            SeckillReq seckillReq = (SeckillReq) promotionBaseDto;
            int count =  baseMapper.countBySkuAndTime(seckillReq.getSkuId(), promotionBaseDto.getStartTime(), promotionBaseDto.getEndTime());
            if (count > 0) {
                throw new BusinessException(ResultCodeEnum.ACTIVITY_CREATE_FAILED,"活动创建失败，相同时间段的秒杀商品已存在");
            }
            Long promotionId = promotionService.savePromotion(promotionBaseDto);
            Seckill seckill = SeckillConverter.INSTANCE.toSeckill(seckillReq);
            seckill.setPromotionId(promotionId);
            this.save(seckill);
            return seckill.getId();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ResultCodeEnum.ACTIVITY_CREATE_FAILED);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends PromotionBaseDto> void updatePromotionInfo(Long id, T promotionBaseDto) {
        Seckill dbSeckill = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(ResultCodeEnum.ACTIVITY_NOT_EXIST));
        promotionService.updatePromoById(dbSeckill.getPromotionId(), promotionBaseDto);
        SeckillReq seckillReq = (SeckillReq) promotionBaseDto;
        Seckill seckill = SeckillConverter.INSTANCE.toSeckill(seckillReq);
        seckill.setId(dbSeckill.getId());
        this.updateById(seckill);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePromoInfo(Long promotionId) {
        Seckill dbSeckill = Optional.ofNullable(this.getOne(new LambdaQueryWrapper<Seckill>().eq(Seckill::getPromotionId, promotionId))).orElseThrow(() -> new BusinessException(ResultCodeEnum.ACTIVITY_NOT_EXIST));
        promotionService.removeById(promotionId);
        this.removeById(dbSeckill.getId());
    }

    @Override
    public SeckillResp getSeckillByPromotionId(Long promotionId) {
        Promotion promotion = Optional.ofNullable(promotionService.getById(promotionId)).orElseThrow(() -> new BusinessException(ResultCodeEnum.ACTIVITY_NOT_EXIST));
        Seckill seckill = this.getOne(new LambdaQueryWrapper<Seckill>().eq(Seckill::getPromotionId, promotionId));
        SeckillResp seckillResp = SeckillConverter.INSTANCE.toSeckillResp(promotion, seckill);
        return seckillResp;
    }
}
