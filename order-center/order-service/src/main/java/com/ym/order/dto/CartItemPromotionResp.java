package com.ym.order.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author qushutao
 * @since 2026-07-21 9:33
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemPromotionResp {

    /**
     * 商品skuId
     */
    private Long skuId;

}
