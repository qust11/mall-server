package com.ym.promotion.dto.resp;


import com.ym.promotion.dto.PromotionBaseDto;
import com.ym.promotion.dto.PromotionIdBaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qushutao
 * @since 2026-07-12 16:42
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullReductionResp extends PromotionIdBaseDto {

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
