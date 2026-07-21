package com.ym.promotion.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * @author qushutao
 * @since 2026-07-20 18:23
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PromotionDetailDto {

    private List<CouponDto> couponList;

    private List<SeckillDto> seckillList;

    private List<FullReductionDto> fullReductionList;
}
