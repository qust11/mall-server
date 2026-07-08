package com.ym.promotion.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 优惠券
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
@ApiModel(value = "Coupon对象", description = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coupon implements Serializable {

    @Serial
    private static final long serialVersionUID = 8407520053007514442L;

    private Long id;

    /**
     * 1：全平台 2: 全店 3: 指定类目 4: 指定商品
     */
    @ApiModelProperty("1：全平台 2: 全店 3: 指定类目 4: 指定商品")
    private Integer rangeType;

    /**
     * 活动id
     */
    @ApiModelProperty("活动id")
    private Long promotionId;

    /**
     * 券面额
     */
    @ApiModelProperty("券面额")
    private Long couponAmount;

    /**
     * 使用最低门槛 0没限制
     */
    @ApiModelProperty("使用最低门槛 0没限制")
    private Long couponThreshold;

    /**
     * 发放数量
     */
    @ApiModelProperty("发放数量/优惠券库存")
    private Integer stock;


    /**
     * 剩余数量
     */
    @ApiModelProperty("剩余数量")
    private Integer remainStock;

    /**
     * 用户限领
     */
    @ApiModelProperty("用户限领")
    private Integer perLimit;

    /**
     * 领取限制
     */
    @ApiModelProperty("当为指定商品时选定的商品sku集合 以,分割")
    private String skuIds;

    @ApiModelProperty("当为指定类目时选定的类目id集合 以,分割")
    private String categoryIds;
}
