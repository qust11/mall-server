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
public class FullReductionDto {

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
     * 折扣类型 1:满减 2:满折
     */
    @ApiModelProperty("折扣类型 1:满减 2:满折")
    private Integer discountType;

    /**
     * 满足金额
     */
    @ApiModelProperty("满足金额")
    private Long useThreshold;

    /**
     * 减免金额
     */
    @ApiModelProperty("减免金额")
    private Long reduceAmount;

    /**
     *折扣
     */
    @ApiModelProperty("折扣值")
    private Long discountRate;

}
