package com.ym.promotion.service;

import com.ym.promotion.dto.resp.FullReductionResp;
import com.ym.promotion.entity.FullReduction;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
public interface IFullReductionService extends IService<FullReduction>, IPromotionCommonService  {

    FullReductionResp getFullReductionByPromotionId(Long promotionId);
}
