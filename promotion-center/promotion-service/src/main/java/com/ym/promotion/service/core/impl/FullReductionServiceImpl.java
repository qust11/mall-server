package com.ym.promotion.service.core.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ym.common.constant.ResultCodeEnum;
import com.ym.common.enums.PromotionTypeEnum;
import com.ym.common.exception.BusinessException;
import com.ym.promotion.converter.FullReductionConverter;
import com.ym.promotion.dto.FullReductionDto;
import com.ym.promotion.dto.PromotionBaseDto;
import com.ym.promotion.dto.PromotionDetailDto;
import com.ym.promotion.dto.req.FullReductionReq;
import com.ym.promotion.dto.resp.FullReductionResp;
import com.ym.promotion.entity.FullReduction;
import com.ym.promotion.entity.Promotion;
import com.ym.promotion.mapper.FullReductionMapper;
import com.ym.promotion.service.core.IFullReductionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ym.promotion.service.core.IPromotionService;
import com.ym.promotion.service.user.ClientPromotionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
public class FullReductionServiceImpl extends ServiceImpl<FullReductionMapper, FullReduction> implements IFullReductionService, ClientPromotionService {

    private final IPromotionService promotionService;

    @Override
    public <T extends PromotionBaseDto> Long savePromotionInfo(T promotionBaseDto) {
        Long promotionId = promotionService.savePromotion(promotionBaseDto);
        FullReductionReq fullReductionReq = (FullReductionReq) promotionBaseDto;
        FullReduction fullReduction = FullReductionConverter.INSTANCE.toFullReduction(fullReductionReq);
        fullReduction.setPromotionId(promotionId);
        this.save(fullReduction);
        return fullReduction.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends PromotionBaseDto> void updatePromotionInfo(Long id, T promotionBaseDto) {
        FullReduction dbFullReduction = Optional.ofNullable(getById(id)).orElseThrow(() -> new BusinessException(ResultCodeEnum.ACTIVITY_NOT_EXIST));
        promotionService.updatePromoById(dbFullReduction.getPromotionId(), promotionBaseDto);

        FullReductionReq fullReductionReq = (FullReductionReq) promotionBaseDto;
        FullReduction fullReduction = FullReductionConverter.INSTANCE.toFullReduction(fullReductionReq);
        fullReduction.setId(dbFullReduction.getId());
        updateById(fullReduction);
    }

    @Override
    public FullReductionResp getFullReductionByPromotionId(Long promotionId) {
        Promotion promotion = Optional.ofNullable(promotionService.getById(promotionId)).orElseThrow(() -> new BusinessException(ResultCodeEnum.ACTIVITY_NOT_EXIST));
        FullReduction fullReduction = this.getOne(new LambdaQueryWrapper<FullReduction>().eq(FullReduction::getPromotionId, promotionId));
        return FullReductionConverter.INSTANCE.toFullReductionResp(promotion, fullReduction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePromoInfo(Long promotionId) {
        FullReduction dbFullReduction = Optional.ofNullable(getOne(new LambdaQueryWrapper<FullReduction>().eq(FullReduction::getPromotionId, promotionId))).orElseThrow(() -> new BusinessException(ResultCodeEnum.ACTIVITY_NOT_EXIST));
        promotionService.removeById(promotionId);
        this.removeById(dbFullReduction.getId());
    }

    @Override
    public void getPromotionByUser(PromotionDetailDto promotionDetailDto, List<Long> skuIds) {
        LocalDateTime now = LocalDateTime.now();
        List<Promotion> promotions = promotionService.list(new LambdaQueryWrapper<Promotion>().eq(Promotion::getPromotionType, PromotionTypeEnum.SECKILL)
                .le(Promotion::getStartTime, now).ge(Promotion::getEndTime, now));
        if (CollectionUtils.isEmpty(promotions)){
            return;
        }
        List<Long> promotionIds = promotions.stream().map(Promotion::getId).toList();
        List<FullReduction> fullReductions = list(new LambdaQueryWrapper<FullReduction>().eq(FullReduction::getPromotionId, promotionIds));
        List<FullReductionDto> fullReductionList = FullReductionConverter.INSTANCE.batchToFullReductionDto(fullReductions);
        promotionDetailDto.setFullReductionList(fullReductionList);
    }
}
