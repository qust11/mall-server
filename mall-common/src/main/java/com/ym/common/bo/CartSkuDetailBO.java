package com.ym.common.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author qushutao
 * @since 2026-07-01 22:18
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartSkuDetailBO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1339349629017956048L;

    /**
     * 商品skuId
     */
    private Long skuId;


    /**
     * 所属SPU
     */
    @ApiModelProperty("所属SPU")
    private Long spuId;

    @ApiModelProperty("sku名称")
    private String skuName;

    /**
     * 规格值ID组合，逗号 例：1,5
     */
    @ApiModelProperty("规格值ID组合，逗号 例：1,5")
    private String specIds;
    /**
     * 当前SKU规格图
     */
    @ApiModelProperty("当前SKU规格图")
    private String skuImg;

    /**
     * 价格 单位:分
     */
    @ApiModelProperty("价格 单位:分")
    private Long price;

    @ApiModelProperty("优惠价格")
    private Long discountPrice;

    /** Selected quantity in the current user's cart. */
    @ApiModelProperty("购物车数量")
    private Integer quantity;

    /**
     * 总库存
     */
    @ApiModelProperty("总库存")
    private Integer totalStock;

    /**
     * 剩余/可用 库存
     */
    @ApiModelProperty("剩余/可用 库存")
    private Integer remainStock;

    /**
     * 锁单库存
     */
    @ApiModelProperty("锁单库存")
    private Integer lockStock;

}
