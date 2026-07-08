package com.ym.promotion.dto.resp;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author qushutao
 * @since 2026-07-04 22:48
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionListResp {


    private Long id;

    @ApiModelProperty("活动类型")
    private Integer promotionType;
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

    /**
     * 活动预算
     */
    @ApiModelProperty("活动预算")
    private Long promotionBudget;
}
