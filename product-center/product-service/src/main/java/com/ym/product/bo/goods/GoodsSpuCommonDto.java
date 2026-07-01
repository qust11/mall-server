package com.ym.product.bo.goods;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qushutao
 * @since 2026-07-01 13:58
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSpuCommonDto {

    private Long id;


    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * spu名称
     */
    private Long brandId;

    /**
     * sku数量
     */
    private Integer skuCount;

    /**
     * 库存总数量
     */
    private Integer stockCount;
    /**
     * 最高价
     */
    private Long maxPrice;

    /**
     * 最低价
     */
    private Long minPrice;

    private String skuId;
}
