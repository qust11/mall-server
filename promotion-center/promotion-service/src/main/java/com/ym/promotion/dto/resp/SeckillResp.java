package com.ym.promotion.dto.resp;


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
public class SeckillResp extends PromotionIdBaseDto {

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
    private Integer userLimitNum;

}
