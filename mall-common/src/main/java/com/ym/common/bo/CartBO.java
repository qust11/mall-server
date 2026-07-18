package com.ym.common.bo;


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
public class CartBO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1339349629017956048L;

    /**
     * 商品skuId
     */
    private Long skuId;

    /**
     * 商品数量
     */
    private Integer quantity;


    private Long spuId;
}
