package com.ym.order.bo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author qushutao
 * @since 2026-07-19 22:09
 * 秒杀不参与加购下单 只能单独下单
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderBO {

    private List<OrderSkuBO> itemList;

    /**
     * 订单总金额
     */
    private Long totalPrice;

    /**
     * 优惠金额
     */
    private Long discountPrice;

    /**
     * 最终订单金额
     */
    private Long finalPrice;


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public class OrderSkuBO {

        private Long price; // 当前单价

        private Long categoryId;

        private int quantity;
        // 构造、getter/setter

        private Long skuId;           // SKU ID
        private String skuName;       // 商品名称
        private Long spuId;           // SPU ID（用于多件多折分组）
        private BigDecimal weight;    // 重量(kg)，用于运费计算

        private Long finalPrice;
        /**
         * 折扣金额 比如满减优惠
         */
        private Long discountPrice;
    }
}


