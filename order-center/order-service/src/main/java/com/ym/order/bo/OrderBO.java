package com.ym.order.bo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * @author qushutao
 * @since 2026-07-19 22:09
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderBO {

    private List<OrderSkuBO> itemList;

    private Long totalPrioce;


    public class OrderSkuBO {
        /**
         * skuId
         */
        private Long id;

        private long price; // 当前单价

        private int quantity;
        // 构造、getter/setter
    }
}


