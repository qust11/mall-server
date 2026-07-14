package com.ym.product.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author qushutao
 * @since 2026-07-14 8:44
 **/
@AllArgsConstructor
@Data
public class GoodsSkuLockDto {

    @ApiModelProperty("商品skuId")
    private Long skuId;

    @ApiModelProperty("锁定库存")
    private Integer lockStock;
}
