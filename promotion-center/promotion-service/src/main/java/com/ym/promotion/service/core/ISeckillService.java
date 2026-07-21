package com.ym.promotion.service.core;

import com.ym.promotion.dto.resp.SeckillResp;
import com.ym.promotion.entity.Seckill;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
public interface ISeckillService extends IService<Seckill>, IPromotionCommonService {

    SeckillResp getSeckillByPromotionId(Long promotionId);
}
