package com.ym.promotion.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ym.common.dto.req.PageReq;
import com.ym.promotion.dto.PromotionBaseDto;
import com.ym.promotion.dto.resp.PromotionListResp;
import com.ym.promotion.entity.Promotion;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
public interface IPromotionService extends IService<Promotion> {

    Long savePromotion(PromotionBaseDto promotionBaseDto);

    IPage<PromotionListResp> pagePromotion(PageReq pageReq);

    void updatePromoById(Long promotionId, PromotionBaseDto promotionBaseDto);

}
