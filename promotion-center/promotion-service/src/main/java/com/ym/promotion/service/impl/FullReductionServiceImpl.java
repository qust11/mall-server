package com.ym.promotion.service.impl;

import com.ym.promotion.converter.CouponConverter;
import com.ym.promotion.converter.FullReductionConverter;
import com.ym.promotion.dto.PromotionBaseDto;
import com.ym.promotion.dto.PromotionIdBaseDto;
import com.ym.promotion.dto.req.FullReductionReq;
import com.ym.promotion.entity.Coupon;
import com.ym.promotion.entity.FullReduction;
import com.ym.promotion.entity.Promotion;
import com.ym.promotion.mapper.FullReductionMapper;
import com.ym.promotion.service.IFullReductionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ym.promotion.service.IPromotionCommonService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现�?
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
@Service
public class FullReductionServiceImpl extends ServiceImpl<FullReductionMapper, FullReduction> implements IFullReductionService, IPromotionCommonService {

    @Override
    public <T extends PromotionBaseDto> Long savePromotionInfo(Long promotionId, T promotionBaseDto) {
        FullReductionReq fullReductionReq = (FullReductionReq) promotionBaseDto;
        FullReduction fullReduction = FullReductionConverter.INSTANCE.toFullReduction(fullReductionReq);
        fullReduction.setPromotionId(promotionId);
        this.save(fullReduction);
        return fullReduction.getId();
    }

}
