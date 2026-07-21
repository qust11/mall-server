package com.ym.promotion.controller.api;


import com.ym.promotion.api.PromotionApi;
import com.ym.promotion.dto.PromotionDetailDto;
import com.ym.promotion.service.user.ClientPromotionProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author qushutao
 * @since 2026-07-20 18:25
 **/
@RestController
@RequestMapping("/api/promotion")
@RequiredArgsConstructor
public class PromotionClient implements PromotionApi {

    private final ClientPromotionProxy clientPromotionProxy;

    @GetMapping("/all")
    @Override
    public PromotionDetailDto getUserAllPromotion(@RequestParam List<Long> skuIds) {
        return clientPromotionProxy.getPromotionByUser(skuIds);
    }
}
