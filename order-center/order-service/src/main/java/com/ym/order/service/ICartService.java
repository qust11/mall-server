package com.ym.order.service;


import com.ym.common.bo.CartBO;
import com.ym.common.bo.CartDetailBO;

/**
 *
 * @author qushutao
 * @since 2026-07-17 18:02
 **/
public interface ICartService {

    void addCart(CartBO cartBO);

    CartDetailBO getCartSkuDetail();
}
