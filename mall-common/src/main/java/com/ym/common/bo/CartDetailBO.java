package com.ym.common.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 购物车详情(整单口径),包含各行商品明细与整单金额汇总
 *
 * @author qushutao
 * @since 2026-10-06 15:30
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDetailBO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 各 SKU 行明细(含行级优惠分摊)
     */
    @ApiModelProperty("购物车各行明细")
    private List<CartSkuDetailBO> items;

    /**
     * 整单原价合计(未优惠) 单位:分
     */
    @ApiModelProperty("整单原价合计 单位:分")
    private Long totalPrice;

    /**
     * 整单优惠合计 单位:分
     */
    @ApiModelProperty("整单优惠合计 单位:分")
    private Long discountPrice;

    /**
     * 整单应付金额 单位:分,等于 items 各行 finalPrice 之和
     */
    @ApiModelProperty("整单应付金额 单位:分")
    private Long finalPrice;

    /**
     * 整单命中的优惠活动/使用的优惠券明细
     */
    @ApiModelProperty("整单优惠明细")
    private List<CartDiscountInfoBO> orderPromotions;
}
