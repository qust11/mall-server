package com.ym.order.calculate.promo;


import com.ym.order.bo.OrderBO;
import org.springframework.stereotype.Component;

/**
 *
 * @author qushutao
 * @since 2026-07-19 22:24
 **/
@Component
public class Seckillpromotion implements Promotion {
    @Override
    public void apply(OrderBO order) {

    }

    @Override
    public int getSort() {
        return 0;
    }
}
