package com.ym.order.bo;


import com.ym.common.bo.CartDiscountInfoBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
     * 订单总金额 单位：分
     */
    private Long totalPrice;

    /**
     * 优惠金额 单位：分
     */
    private Long discountPrice;

    /**
     * 最终订单金额 单位：分
     */
    private Long finalPrice;

    /**
     * 整单命中的优惠活动/使用的优惠券明细
     */
    private List<CartDiscountInfoBO> orderDiscountInfo;

    public void addOrderDiscountInfo(CartDiscountInfoBO discountInfo) {
        if (null == orderDiscountInfo) {
            orderDiscountInfo = new ArrayList<>();
        }
        orderDiscountInfo.add(discountInfo);
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class OrderSkuBO {

        private Long price; // 当前单价

        private Long categoryId;

        /**
         * 数量
         */
        private int quantity;
        // 构造、getter/setter

        private Long skuId;           // SKU ID

        private String skuName;       // 商品名称

        private Long spuId;           // SPU ID（用于多件多折分组）

        /**
         * 行小计(单价×数量)扣除已分摊优惠后的金额 单位：分
         */
        private long finalPrice;
        /**
         * 折扣金额 比如满减优惠 单位：分
         */
        private long discountPrice;

        /**
         * 该行分摊到的各项优惠明细
         */
        private List<CartDiscountInfoBO> discountInfoList;

        public void addItemDiscountInfo(CartDiscountInfoBO discountInfo) {
            if (null == discountInfoList) {
                discountInfoList = new ArrayList<>();
            }
            discountInfoList.add(discountInfo);
        }
    }
}


