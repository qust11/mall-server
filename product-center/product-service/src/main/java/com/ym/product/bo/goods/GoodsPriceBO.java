package com.ym.product.bo.goods;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qushutao
 * @since 2026-06-23 11:14
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoodsPriceBO {

    private Integer maxPrice;

    private Integer minPrice;
}
