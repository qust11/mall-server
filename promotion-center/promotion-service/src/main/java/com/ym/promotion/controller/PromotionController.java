package com.ym.promotion.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ym.common.dto.req.PageReq;
import com.ym.common.result.Result;
import com.ym.promotion.dto.req.CouponReq;
import com.ym.promotion.dto.resp.PromotionListResp;
import com.ym.promotion.service.IPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
@RestController
@RequestMapping("/promotion/common")
@RequiredArgsConstructor
public class PromotionController {

    private final IPromotionService promotionService;

    @GetMapping
    public Result<IPage<PromotionListResp>> pagePromotion(@ModelAttribute PageReq pageReq) {
        return Result.success(promotionService.pagePromotion( pageReq));
    }
}
