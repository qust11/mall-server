package com.ym.promotion.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qushutao
 * @since 2026-07-04 22:48
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionIdBaseDto extends PromotionBaseDto {

    /**
     * 活动id
     */
    @ApiModelProperty("活动id")
    private Long id;
}
