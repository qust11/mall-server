package com.ym.promotion.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author qushutao
 * @since 2026-07-12 16:42
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillDto {

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
     * 秒杀价格
     */
    @ApiModelProperty("秒杀价格")
    private Long seckillPrice;

    /**
     * 秒杀库存
     */
    @ApiModelProperty("秒杀库存")
    private Integer seckillStock;

    /**
     * 秒杀商品
     */
    @ApiModelProperty("秒杀商品")
    private Long skuId;

    /**
     * 每人限购
     */
    @ApiModelProperty("每人限购")
    private Integer perLimit;

}
