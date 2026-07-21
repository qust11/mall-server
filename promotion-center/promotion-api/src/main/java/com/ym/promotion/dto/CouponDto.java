package com.ym.promotion.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author qushutao
 * @since 2026-07-20 18:28
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponDto {


    /**
     * id
     */
    private Long id;

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String promotionName;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty("活动类型")
    private Integer promotionType;

    /**
     * 活动预算
     */
    @ApiModelProperty("活动预算")
    private Long promotionBudget;

    /**
     * 活动说明
     */
    @ApiModelProperty("活动说明")
    private String promotionDesc;


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
    private List<Long> spuIds;

    @ApiModelProperty("当为指定类目时选定的类目id集合 以,分割")
    private List<Long> categoryIds;
}
