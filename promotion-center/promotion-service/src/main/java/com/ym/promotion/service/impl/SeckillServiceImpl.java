package com.ym.promotion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ym.promotion.converter.SeckillConverter;
import com.ym.promotion.dto.PromotionBaseDto;
import com.ym.promotion.dto.PromotionIdBaseDto;
import com.ym.promotion.dto.req.SeckillReq;
import com.ym.promotion.dto.resp.SeckillResp;
import com.ym.promotion.entity.Promotion;
import com.ym.promotion.entity.Seckill;
import com.ym.promotion.mapper.SeckillMapper;
import com.ym.promotion.service.IPromotionCommonService;
import com.ym.promotion.service.ISeckillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现�?
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
@Service
public class SeckillServiceImpl extends ServiceImpl<SeckillMapper, Seckill> implements ISeckillService, IPromotionCommonService {

    @Override
    public <T extends PromotionBaseDto> Long savePromotionInfo(Long promotionId, T promotionBaseDto) {
        SeckillReq seckillReq = (SeckillReq) promotionBaseDto;
        Seckill seckill = SeckillConverter.INSTANCE.toSeckill(seckillReq);
        seckill.setPromotionId(promotionId);
        this.save(seckill);
        return seckill.getId();
    }

}
