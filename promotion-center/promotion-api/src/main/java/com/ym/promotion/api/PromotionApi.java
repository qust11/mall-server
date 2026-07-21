package com.ym.promotion.api;


import com.ym.promotion.dto.PromotionDetailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *
 * @author qushutao
 * @since 2026-07-20 18:22
 **/
@FeignClient(value="promotion-service")
public interface PromotionApi {


    @PutMapping("/api/promotion/all")
    PromotionDetailDto getUserAllPromotion(@RequestParam List<Long> skuIds);
}
