package com.ym.promotion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ym.common.constant.ResultCodeEnum;
import com.ym.common.exception.BusinessException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Override
    public <T extends PromotionBaseDto> Long savePromotionInfo(T promotionBaseDto) {
        Long promotionId = promotionService.savePromotion(promotionBaseDto);
        SeckillReq seckillReq = (SeckillReq) promotionBaseDto;
        Seckill seckill = SeckillConverter.INSTANCE.toSeckill(seckillReq);
        seckill.setPromotionId(promotionId);
        this.save(seckill);
        return seckill.getId();
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
