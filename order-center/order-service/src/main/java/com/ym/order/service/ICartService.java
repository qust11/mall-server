package com.ym.order.service;


import com.ym.common.bo.CartBO;
import com.ym.common.bo.CartSkuDetailBO;

import java.util.List;

/**
 *
 * @author qushutao
 * @since 2026-07-17 18:02
 **/
public interface ICartService {

    void addCart(CartBO cartBO);

    List<CartSkuDetailBO> getCartSkuDetail();
}
