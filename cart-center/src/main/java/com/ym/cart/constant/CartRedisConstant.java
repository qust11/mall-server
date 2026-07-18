package com.ym.cart.constant;


/**
 *
 * @author qushutao
 * @since 2026-07-17 20:26
 **/
public class CartRedisConstant {
    private CartRedisConstant() {
    }

    public static final long CART_EXPIRE_SECONDS = 60 * 60 * 24 * 60L;

    /**
     * 购物车不同类别sku最大数量
     */
    public static final int CART_SKU_MAX_NUM = 5;

    public static final String CART_KEY_PREFIX = "CART:GOODS:USER_ID:";


}
