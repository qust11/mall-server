package com.ym.promotion.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ym.common.constant.ResultCodeEnum;
import com.ym.common.dto.req.PageReq;
import com.ym.common.exception.BusinessException;
import com.ym.common.util.PageUtil;
import com.ym.promotion.converter.PromotionConverter;
import com.ym.promotion.dto.PromotionBaseDto;
import com.ym.promotion.dto.resp.PromotionListResp;
import com.ym.promotion.entity.Promotion;
import com.ym.promotion.mapper.PromotionMapper;
import com.ym.promotion.service.IPromotionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
@Service
@RequiredArgsConstructor
public class PromotionServiceImpl extends ServiceImpl<PromotionMapper, Promotion> implements IPromotionService {

    @Override
    public Long savePromotion(PromotionBaseDto promotionBaseDto) {
        checkActivityTime(promotionBaseDto);
        Promotion promotion = PromotionConverter.INSTANCE.toPromotion(promotionBaseDto);
        this.save(promotion);
        return promotion.getId();
    }

    @Override
    public IPage<PromotionListResp> pagePromotion(PageReq pageReq) {
        IPage<Promotion> page = page(new Page<>(pageReq.getCurrent(), pageReq.getSize()));

        if (CollectionUtils.isEmpty(page.getRecords())){
            return new Page<>(pageReq.getCurrent(), pageReq.getSize(), page.getTotal());
        }

        List<PromotionListResp> promotionList = PromotionConverter.INSTANCE.batchToPromotionRespList(page.getRecords());
        return PageUtil.getPage(page, promotionList);
    }

    @Override
    public void updatePromoById(Long promotionId, PromotionBaseDto promotionBaseDto) {
        checkActivityTime(promotionBaseDto);
        Promotion promotion = PromotionConverter.INSTANCE.toPromotion(promotionBaseDto);
        promotion.setId(promotionId);
        updateById(promotion);
    }

    private static void checkActivityTime(PromotionBaseDto promotionBaseDto) {
        if (promotionBaseDto.getStartTime().isAfter(promotionBaseDto.getEndTime())){
            throw new BusinessException(ResultCodeEnum.ACTIVITY_START_TIME_ERR);
        }

        if (promotionBaseDto.getStartTime().isBefore(LocalDateTime.now())){
            throw new BusinessException(ResultCodeEnum.ACTIVITY_START_TIME_EARLY);
        }
    }
}
