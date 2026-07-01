package com.ym.product.constant;


/**
 * @author qushutao
 * @since 2026-06-30 9:17
 **/
public class GoodsRedisConstant {
    public static final Long EXPIRE_TIME_FIVE_SECONDS = 5L;
    public static final Long EXPIRE_TIME_TEN_SECONDS = 10L;

    // redis key
    public static final String GOODS_SPU_CODE_PREFIX = "GOOGS:SPU:CODE";


    // redission key
    public static final String GOODS_SPU_CODE_LOCK_KEY = "goods:spu:code:generate:lock";
}
