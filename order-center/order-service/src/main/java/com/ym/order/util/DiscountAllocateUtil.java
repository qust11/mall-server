package com.ym.order.util;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 优惠金额按商品行小计比例分摊工具:
 * 各行向下取整,尾差按行金额从大到小补偿,且单行分摊不超过该行自身金额,
 * 保证各行分摊之和与实际优惠总额一致(极端场景所有行都无吸收空间时允许少量尾差丢弃)
 *
 * @author qushutao
 * @since 2026-10-06 15:45
 **/
public final class DiscountAllocateUtil {

    private DiscountAllocateUtil() {
    }

    /**
     * @param totalDiscount 待分摊的优惠总额 单位:分
     * @param weights       各商品行的分摊权重(行小计) 单位:分
     * @return 各行分摊到的优惠金额,顺序与 weights 一致
     */
    public static List<Long> allocate(long totalDiscount, List<Long> weights) {
        int size = weights.size();
        List<Long> shares = new ArrayList<>(size);
        long weightSum = 0L;
        for (Long weight : weights) {
            weightSum += nullToZero(weight);
        }
        for (int i = 0; i < size; i++) {
            if (totalDiscount <= 0 || weightSum <= 0) {
                shares.add(0L);
                continue;
            }
            long weight = nullToZero(weights.get(i));
            // 向下取整,且不超过该行自身金额
            shares.add(Math.min(totalDiscount * weight / weightSum, weight));
        }
        if (totalDiscount <= 0 || weightSum <= 0) {
            return shares;
        }

        long remainder = totalDiscount - sum(shares);
        if (remainder > 0) {
            // 尾差补偿:行金额大的优先吸收,单行仍不超过自身金额
            List<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                indexes.add(i);
            }
            indexes.sort(Comparator.comparingLong((Integer i) -> nullToZero(weights.get(i))).reversed());
            for (Integer index : indexes) {
                if (remainder <= 0) {
                    break;
                }
                long room = nullToZero(weights.get(index)) - shares.get(index);
                long extra = Math.min(remainder, room);
                shares.set(index, shares.get(index) + extra);
                remainder -= extra;
            }
        }
        return shares;
    }

    public static long sum(List<Long> values) {
        long sum = 0L;
        for (Long value : values) {
            sum += nullToZero(value);
        }
        return sum;
    }

    private static long nullToZero(Long value) {
        return null == value ? 0L : value;
    }
}
