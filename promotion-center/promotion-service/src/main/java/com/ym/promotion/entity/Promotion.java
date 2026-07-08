package com.ym.promotion.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 活动
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
@ApiModel(value = "Promotion对象", description = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Promotion implements Serializable {

    @Serial
    private static final long serialVersionUID = 8030953259550562422L;

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
}
