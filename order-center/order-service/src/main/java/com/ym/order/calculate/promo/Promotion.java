package com.ym.order.calculate.promo;


import com.ym.order.bo.OrderBO;

/**
 *
 * @author qushutao
 * @since 2026-07-19 22:09
 **/
public interface Promotion {

    void apply(OrderBO order);

    int getSort();
}
