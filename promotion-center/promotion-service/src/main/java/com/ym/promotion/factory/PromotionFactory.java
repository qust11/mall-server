package com.ym.promotion.factory;


import com.ym.common.enums.PromotionTypeEnum;
import com.ym.promotion.service.IPromotionCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author qushutao
 * @since 2026-07-05 22:22
 **/
@Component
public class PromotionFactory {
    @Autowired
    private Map<String, IPromotionCommonService> promotionCommonServiceMap;

    public IPromotionCommonService getPromotionService(Integer promotionType) {
        PromotionTypeEnum promotionTypeEnum = PromotionTypeEnum.getByCode(promotionType);
        if (promotionTypeEnum == null){
            return null;
        }
        return promotionCommonServiceMap.get(promotionTypeEnum.getServiceName());
    }

}
