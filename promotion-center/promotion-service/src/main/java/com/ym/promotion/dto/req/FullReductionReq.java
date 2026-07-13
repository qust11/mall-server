package com.ym.promotion.dto.req;


import com.ym.promotion.dto.PromotionBaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qushutao
 * @since 2026-07-05 21:58
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullReductionReq extends PromotionBaseDto {

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
