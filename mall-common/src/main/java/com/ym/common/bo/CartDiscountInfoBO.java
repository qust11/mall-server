package com.ym.common.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 优惠明细,行级与整单级共用:
 * 行级明细描述单个 SKU 分摊到的每项优惠,整单级明细描述订单命中的每个活动/券
 *
 * @author qushutao
 * @since 2026-10-06 15:30
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDiscountInfoBO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 满减
     */
    public static final int TYPE_FULL_REDUCTION = 1;
    /**
     * 满折
     */
    public static final int TYPE_FULL_RATE = 2;
    /**
     * 优惠券
     */
    public static final int TYPE_COUPON = 3;

    /**
     * 优惠类型 1:满减 2:满折 3:优惠券
     */
    @ApiModelProperty("优惠类型 1:满减 2:满折 3:优惠券")
    private Integer promotionType;

    /**
     * 类型展示名,如"满减"、"优惠券"
     */
    @ApiModelProperty("类型展示名")
    private String typeDesc;

    /**
     * 活动/优惠券 id
     */
    @ApiModelProperty("活动/优惠券id")
    private Long promotionId;

    /**
     * 活动/优惠券名称,如"满300减50"
     */
    @ApiModelProperty("活动/优惠券名称")
    private String promotionName;

    /**
     * 活动说明
     */
    @ApiModelProperty("活动说明")
    private String promotionDesc;

    /**
     * 优惠金额 单位:分。行级为该行分摊到的金额,整单级为该活动整单优惠金额
     */
    @ApiModelProperty("优惠金额 单位:分")
    private Long discountAmount;
}
