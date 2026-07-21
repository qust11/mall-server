package com.ym.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单价格计算引擎
 * 计算顺序：秒杀/拼团价 → 多件多折 → 满减 → 优惠券(仅1张,取最大) → 积分抵扣 → 运费
 */
public class OrderPriceCalculator2 {

    // ==================== 数据模型 ====================

    /**
     * 订单项
     */
    public static class OrderItem {
        private Long skuId;           // SKU ID
        private String skuName;       // 商品名称
        private BigDecimal originalPrice; // 原价
        private Integer quantity;     // 购买数量
        private Long spuId;           // SPU ID（用于多件多折分组）
        private BigDecimal weight;    // 重量(kg)，用于运费计算

        // 活动标记（互斥，同一时间只能参加一种）
        private SeckillInfo seckillInfo;   // 秒杀信息
        private GroupBuyInfo groupBuyInfo; // 拼团信息

        public OrderItem(Long skuId, String skuName, BigDecimal originalPrice,
                         Integer quantity, Long spuId, BigDecimal weight) {
            this.skuId = skuId;
            this.skuName = skuName;
            this.originalPrice = originalPrice;
            this.quantity = quantity;
            this.spuId = spuId;
            this.weight = weight;
        }

        // Getters & Setters
        public Long getSkuId() { return skuId; }
        public String getSkuName() { return skuName; }
        public BigDecimal getOriginalPrice() { return originalPrice; }
        public Integer getQuantity() { return quantity; }
        public Long getSpuId() { return spuId; }
        public BigDecimal getWeight() { return weight; }
        public SeckillInfo getSeckillInfo() { return seckillInfo; }
        public void setSeckillInfo(SeckillInfo seckillInfo) { this.seckillInfo = seckillInfo; }
        public GroupBuyInfo getGroupBuyInfo() { return groupBuyInfo; }
        public void setGroupBuyInfo(GroupBuyInfo groupBuyInfo) { this.groupBuyInfo = groupBuyInfo; }
    }

    /**
     * 秒杀信息
     */
    public static class SeckillInfo {
        private BigDecimal seckillPrice; // 秒杀价
        private Date startTime;
        private Date endTime;

        public SeckillInfo(BigDecimal seckillPrice, Date startTime, Date endTime) {
            this.seckillPrice = seckillPrice;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public BigDecimal getSeckillPrice() { return seckillPrice; }
        public boolean isValid() {
            Date now = new Date();
            return now.after(startTime) && now.before(endTime);
        }
    }

    /**
     * 拼团信息
     */
    public static class GroupBuyInfo {
        private BigDecimal groupPrice; // 拼团价
        private Integer minGroupSize;  // 最低成团人数

        public GroupBuyInfo(BigDecimal groupPrice, Integer minGroupSize) {
            this.groupPrice = groupPrice;
            this.minGroupSize = minGroupSize;
        }
        public BigDecimal getGroupPrice() { return groupPrice; }
    }

    /**
     * 多件多折规则
     */
    @Data
    @NoArgsConstructor
    public static class MultiPieceDiscount {
        private Long spuId;            // 适用SPU
        private List<Threshold> thresholds; // 阶梯规则，如满2件9折，满3件8折

        @Data
        @NoArgsConstructor
        public static class Threshold {
            private Integer minQuantity;      // 最低件数
            private BigDecimal discountRate;  // 折扣率(0.9=9折)

            public Threshold(Integer minQuantity, BigDecimal discountRate) {
                this.minQuantity = minQuantity;
                this.discountRate = discountRate;
            }
        }

        public MultiPieceDiscount(Long spuId, List<Threshold> thresholds) {
            this.spuId = spuId;
            this.thresholds = thresholds.stream()
                    .sorted(Comparator.comparing(Threshold::getMinQuantity).reversed())
                    .collect(Collectors.toList());
        }

        public BigDecimal getDiscountRate(int totalQuantity) {
            for (Threshold t : thresholds) {
                if (totalQuantity >= t.minQuantity) {
                    return t.discountRate;
                }
            }
            return BigDecimal.ONE; // 不满足任何阶梯，无折扣
        }

        public Long getSpuId() { return spuId; }
    }

    /**
     * 满减规则
     */
    public static class FullReduction {
        private BigDecimal threshold;   // 满多少
        private BigDecimal reduction;   // 减多少
        private String scope;           // "ALL"全局或指定类目

        public FullReduction(BigDecimal threshold, BigDecimal reduction, String scope) {
            this.threshold = threshold;
            this.reduction = reduction;
            this.scope = scope;
        }
        public BigDecimal getThreshold() { return threshold; }
        public BigDecimal getReduction() { return reduction; }
        public String getScope() { return scope; }
    }

    /**
     * 优惠券
     */
    public static class Coupon {
        public enum Type { CASH, PERCENT, FREE_SHIPPING }

        private Long couponId;
        private String couponName;
        private Type type;
        private BigDecimal threshold;    // 使用门槛
        private BigDecimal value;        // 面值/折扣率
        private BigDecimal maxDiscount;  // 最大优惠金额（百分比券用）
        private Date validStart;
        private Date validEnd;

        public Coupon(Long couponId, String couponName, Type type,
                      BigDecimal threshold, BigDecimal value,
                      BigDecimal maxDiscount, Date validStart, Date validEnd) {
            this.couponId = couponId;
            this.couponName = couponName;
            this.type = type;
            this.threshold = threshold;
            this.value = value;
            this.maxDiscount = maxDiscount;
            this.validStart = validStart;
            this.validEnd = validEnd;
        }

        public boolean isValid() {
            Date now = new Date();
            return now.after(validStart) && now.before(validEnd);
        }

        /**
         * 计算优惠金额
         */
        public BigDecimal calculateDiscount(BigDecimal orderAmount) {
            if (!isValid() || orderAmount.compareTo(threshold) < 0) {
                return BigDecimal.ZERO;
            }

            switch (type) {
                case CASH:
                    return value.min(orderAmount); // 现金券不超过订单金额
                case PERCENT:
                    BigDecimal discount = orderAmount.multiply(BigDecimal.ONE.subtract(value));
                    return maxDiscount != null ? discount.min(maxDiscount) : discount;
                case FREE_SHIPPING:
                    return BigDecimal.ZERO; // 免邮券在运费阶段处理
                default:
                    return BigDecimal.ZERO;
            }
        }

        // 用于选择最大优惠券：优先比较实际优惠金额，若相同则比较面值
        public int compareDiscountValue(Coupon other, BigDecimal orderAmount) {
            BigDecimal myDiscount = this.calculateDiscount(orderAmount);
            BigDecimal otherDiscount = other.calculateDiscount(orderAmount);
            int cmp = myDiscount.compareTo(otherDiscount);
            return cmp != 0 ? cmp : this.value.compareTo(other.value);
        }

        public Long getCouponId() { return couponId; }
        public Type getType() { return type; }
        public String getCouponName() { return couponName; }
    }

    /**
     * 积分抵扣规则
     */
    public static class PointRule {
        private BigDecimal exchangeRate;   // 多少积分抵1元
        private Integer maxUsePoints;      // 单次最多使用积分
        private BigDecimal maxDeductionRatio; // 最多抵扣订单金额的百分比

        public PointRule(BigDecimal exchangeRate, Integer maxUsePoints,
                         BigDecimal maxDeductionRatio) {
            this.exchangeRate = exchangeRate;
            this.maxUsePoints = maxUsePoints;
            this.maxDeductionRatio = maxDeductionRatio;
        }

        /**
         * 计算积分可抵扣金额
         */
        public BigDecimal calculateDeduction(int usePoints, BigDecimal orderAmount) {
            if (usePoints <= 0) return BigDecimal.ZERO;

            int actualPoints = Math.min(usePoints, maxUsePoints);
            BigDecimal moneyByPoints = BigDecimal.valueOf(actualPoints)
                    .divide(exchangeRate, 4, RoundingMode.HALF_UP);

            BigDecimal maxDeduction = orderAmount.multiply(maxDeductionRatio);
            return moneyByPoints.min(maxDeduction).setScale(2, RoundingMode.HALF_UP);
        }
    }

    /**
     * 运费规则
     */
    public static class ShippingRule {
        private BigDecimal freeShippingThreshold; // 满多少免邮
        private BigDecimal baseFee;               // 基础运费
        private BigDecimal weightFee;             // 每公斤续重费用

        public ShippingRule(BigDecimal freeShippingThreshold,
                            BigDecimal baseFee, BigDecimal weightFee) {
            this.freeShippingThreshold = freeShippingThreshold;
            this.baseFee = baseFee;
            this.weightFee = weightFee;
        }

        public BigDecimal calculateShipping(BigDecimal orderAmount, BigDecimal totalWeight) {
            // 免邮券或满额免邮
            if (freeShippingThreshold != null &&
                    orderAmount.compareTo(freeShippingThreshold) >= 0) {
                return BigDecimal.ZERO;
            }

            BigDecimal weightCost = totalWeight.multiply(weightFee);
            return baseFee.add(weightCost).setScale(2, RoundingMode.HALF_UP);
        }
    }

    // ==================== 计算结果 ====================

    /**
     * 价格计算结果
     */
    public static class PriceResult {
        private BigDecimal originalTotal;      // 商品原价总和
        private BigDecimal seckillDiscount;    // 秒杀优惠
        private BigDecimal groupBuyDiscount;   // 拼团优惠
        private BigDecimal multiPieceDiscount; // 多件多折优惠
        private BigDecimal fullReduction;      // 满减优惠
        private BigDecimal couponDiscount;     // 优惠券优惠
        private BigDecimal pointDeduction;     // 积分抵扣
        private BigDecimal shippingFee;        // 运费
        private BigDecimal finalPayAmount;     // 最终支付金额

        // 使用的优惠券
        private Coupon usedCoupon;
        // 各商品明细
        private List<ItemPriceDetail> itemDetails;

        public static class ItemPriceDetail {
            public Long skuId;
            public String skuName;
            public Integer quantity;
            public BigDecimal originalUnitPrice;
            public BigDecimal finalUnitPrice;
            public BigDecimal itemSubtotal;
            public String appliedActivity; // 应用的活动描述
        }

        // Getters
        public BigDecimal getOriginalTotal() { return originalTotal; }
        public BigDecimal getSeckillDiscount() { return seckillDiscount; }
        public BigDecimal getGroupBuyDiscount() { return groupBuyDiscount; }
        public BigDecimal getMultiPieceDiscount() { return multiPieceDiscount; }
        public BigDecimal getFullReduction() { return fullReduction; }
        public BigDecimal getCouponDiscount() { return couponDiscount; }
        public BigDecimal getPointDeduction() { return pointDeduction; }
        public BigDecimal getShippingFee() { return shippingFee; }
        public BigDecimal getFinalPayAmount() { return finalPayAmount; }
        public Coupon getUsedCoupon() { return usedCoupon; }
        public List<ItemPriceDetail> getItemDetails() { return itemDetails; }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("========== 订单价格明细 ==========\n");
            sb.append(String.format("商品原价总计:    ¥%s\n", originalTotal));
            if (seckillDiscount.compareTo(BigDecimal.ZERO) > 0)
                sb.append(String.format("秒杀优惠:       -¥%s\n", seckillDiscount));
            if (groupBuyDiscount.compareTo(BigDecimal.ZERO) > 0)
                sb.append(String.format("拼团优惠:       -¥%s\n", groupBuyDiscount));
            if (multiPieceDiscount.compareTo(BigDecimal.ZERO) > 0)
                sb.append(String.format("多件多折优惠:    -¥%s\n", multiPieceDiscount));
            if (fullReduction.compareTo(BigDecimal.ZERO) > 0)
                sb.append(String.format("满减优惠:       -¥%s\n", fullReduction));
            if (couponDiscount.compareTo(BigDecimal.ZERO) > 0)
                sb.append(String.format("优惠券优惠:     -¥%s (%s)\n",
                        couponDiscount, usedCoupon != null ? usedCoupon.getCouponName() : ""));
            if (pointDeduction.compareTo(BigDecimal.ZERO) > 0)
                sb.append(String.format("积分抵扣:       -¥%s\n", pointDeduction));
            sb.append("--------------------------------\n");
            sb.append(String.format("运费:           +¥%s\n", shippingFee));
            sb.append("================================\n");
            sb.append(String.format("最终支付金额:    ¥%s\n", finalPayAmount));
            return sb.toString();
        }
    }

    // ==================== 计算引擎 ====================

    private List<MultiPieceDiscount> multiPieceDiscounts;
    private List<FullReduction> fullReductions;
    private ShippingRule shippingRule;
    private PointRule pointRule;

    public OrderPriceCalculator2(List<MultiPieceDiscount> multiPieceDiscounts,
                                 List<FullReduction> fullReductions,
                                 ShippingRule shippingRule,
                                 PointRule pointRule) {
        this.multiPieceDiscounts = multiPieceDiscounts != null ? multiPieceDiscounts : new ArrayList<>();
        this.fullReductions = fullReductions != null ? fullReductions : new ArrayList<>();
        this.shippingRule = shippingRule;
        this.pointRule = pointRule;
    }

    /**
     * 核心计算入口
     *
     * @param items 订单商品列表
     * @param availableCoupons 可用优惠券列表（会自动选择优惠最大的）
     * @param usePoints 用户选择使用的积分
     * @param hasFreeShippingCoupon 是否使用了免邮券
     */
    public PriceResult calculate(List<OrderItem> items,
                                 List<Coupon> availableCoupons,
                                 int usePoints,
                                 boolean hasFreeShippingCoupon) {

        PriceResult result = new PriceResult();
        result.itemDetails = new ArrayList<>();

        // ========== Step 1: 计算商品原价总和 ==========
        BigDecimal originalTotal = BigDecimal.ZERO;
        for (OrderItem item : items) {
            originalTotal = originalTotal.add(
                    item.getOriginalPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }
        result.originalTotal = originalTotal.setScale(2, RoundingMode.HALF_UP);

        // ========== Step 2: 应用秒杀/拼团价（确定商品基准价） ==========
        // 注意：秒杀和拼团互斥，优先级：秒杀 > 拼团
        BigDecimal afterActivityPrice = BigDecimal.ZERO;
        BigDecimal seckillTotalDiscount = BigDecimal.ZERO;
        BigDecimal groupBuyTotalDiscount = BigDecimal.ZERO;

        Map<Long, List<OrderItem>> spuGroup = new HashMap<>(); // 用于多件多折分组

        for (OrderItem item : items) {
            PriceResult.ItemPriceDetail detail = new PriceResult.ItemPriceDetail();
            detail.skuId = item.getSkuId();
            detail.skuName = item.getSkuName();
            detail.quantity = item.getQuantity();
            detail.originalUnitPrice = item.getOriginalPrice();

            BigDecimal unitPrice = item.getOriginalPrice();
            String activityDesc = "无活动";

            // 秒杀优先级最高
            if (item.getSeckillInfo() != null && item.getSeckillInfo().isValid()) {
                BigDecimal seckillPrice = item.getSeckillInfo().getSeckillPrice();
                seckillTotalDiscount = seckillTotalDiscount.add(
                        item.getOriginalPrice().subtract(seckillPrice)
                                .multiply(BigDecimal.valueOf(item.getQuantity()))
                );
                unitPrice = seckillPrice;
                activityDesc = "秒杀价";
            }
            // 其次拼团
            else if (item.getGroupBuyInfo() != null) {
                BigDecimal groupPrice = item.getGroupBuyInfo().getGroupPrice();
                groupBuyTotalDiscount = groupBuyTotalDiscount.add(
                        item.getOriginalPrice().subtract(groupPrice)
                                .multiply(BigDecimal.valueOf(item.getQuantity()))
                );
                unitPrice = groupPrice;
                activityDesc = "拼团价";
            }

            detail.finalUnitPrice = unitPrice;
            detail.itemSubtotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);
            detail.appliedActivity = activityDesc;
            afterActivityPrice = afterActivityPrice.add(detail.itemSubtotal);

            result.itemDetails.add(detail);

            // 按SPU分组统计数量（多件多折）
            spuGroup.computeIfAbsent(item.getSpuId(), k -> new ArrayList<>()).add(item);
        }

        result.seckillDiscount = seckillTotalDiscount.setScale(2, RoundingMode.HALF_UP);
        result.groupBuyDiscount = groupBuyTotalDiscount.setScale(2, RoundingMode.HALF_UP);

        // ========== Step 3: 应用多件多折 ==========
        BigDecimal multiPieceTotalDiscount = BigDecimal.ZERO;

        for (MultiPieceDiscount discount : multiPieceDiscounts) {
            List<OrderItem> groupItems = spuGroup.get(discount.getSpuId());
            if (groupItems == null || groupItems.isEmpty()) continue;

            int totalQty = groupItems.stream().mapToInt(OrderItem::getQuantity).sum();
            BigDecimal rate = discount.getDiscountRate(totalQty);

            if (rate.compareTo(BigDecimal.ONE) < 0) {
                for (OrderItem item : groupItems) {
                    // 多件多折基于当前单价（已应用秒杀/拼团价后）
                    BigDecimal currentUnitPrice = item.getSeckillInfo() != null && item.getSeckillInfo().isValid()
                            ? item.getSeckillInfo().getSeckillPrice()
                            : (item.getGroupBuyInfo() != null ? item.getGroupBuyInfo().getGroupPrice() : item.getOriginalPrice());

                    BigDecimal itemOriginalSubtotal = currentUnitPrice
                            .multiply(BigDecimal.valueOf(item.getQuantity()));
                    BigDecimal itemDiscountedSubtotal = itemOriginalSubtotal.multiply(rate);
                    BigDecimal itemDiscount = itemOriginalSubtotal.subtract(itemDiscountedSubtotal);

                    multiPieceTotalDiscount = multiPieceTotalDiscount.add(itemDiscount);

                    // 更新明细
                    result.itemDetails.stream()
                            .filter(d -> d.skuId.equals(item.getSkuId()))
                            .findFirst()
                            .ifPresent(d -> {
                                d.appliedActivity += " + 多件多折(" + rate.multiply(BigDecimal.valueOf(10)) + "折)";
                                d.itemSubtotal = itemDiscountedSubtotal.setScale(2, RoundingMode.HALF_UP);
                            });
                }
            }
        }

        result.multiPieceDiscount = multiPieceTotalDiscount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal afterMultiPiece = afterActivityPrice.subtract(result.multiPieceDiscount);

        // ========== Step 4: 应用满减 ==========
        BigDecimal fullReductionTotal = BigDecimal.ZERO;
        // 按scope分组计算，这里简化处理全局满减
        List<FullReduction> sortedReductions = fullReductions.stream()
                .filter(r -> "ALL".equals(r.getScope()))
                .sorted(Comparator.comparing(FullReduction::getThreshold).reversed())
                .collect(Collectors.toList());

        for (FullReduction reduction : sortedReductions) {
            if (afterMultiPiece.compareTo(reduction.getThreshold()) >= 0) {
                fullReductionTotal = reduction.getReduction();
                break; // 只应用最高档
            }
        }

        result.fullReduction = fullReductionTotal;
        BigDecimal afterFullReduction = afterMultiPiece.subtract(fullReductionTotal);
        // 满减后不能为负
        afterFullReduction = afterFullReduction.max(BigDecimal.ZERO);

        // ========== Step 5: 选择并应用最优优惠券（仅1张） ==========
        Coupon bestCoupon = null;
        BigDecimal bestCouponDiscount = BigDecimal.ZERO;

        if (availableCoupons != null && !availableCoupons.isEmpty()) {
            for (Coupon coupon : availableCoupons) {
                if (!coupon.isValid()) continue;

                BigDecimal discount = coupon.calculateDiscount(afterFullReduction);
                if (discount.compareTo(BigDecimal.ZERO) > 0) {
                    if (bestCoupon == null ||
                            coupon.compareDiscountValue(bestCoupon, afterFullReduction) > 0) {
                        bestCoupon = coupon;
                        bestCouponDiscount = discount;
                    }
                }
            }
        }

        result.usedCoupon = bestCoupon;
        result.couponDiscount = bestCouponDiscount;
        BigDecimal afterCoupon = afterFullReduction.subtract(bestCouponDiscount);
        afterCoupon = afterCoupon.max(BigDecimal.ZERO);

        // ========== Step 6: 积分抵扣 ==========
        BigDecimal pointDeduction = BigDecimal.ZERO;
        if (pointRule != null && usePoints > 0) {
            pointDeduction = pointRule.calculateDeduction(usePoints, afterCoupon);
        }
        result.pointDeduction = pointDeduction;
        BigDecimal afterPoint = afterCoupon.subtract(pointDeduction);
        afterPoint = afterPoint.max(BigDecimal.ZERO);

        // ========== Step 7: 计算运费 ==========
        BigDecimal totalWeight = items.stream()
                .map(i -> i.getWeight().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shippingFee = BigDecimal.ZERO;
        if (shippingRule != null) {
            // 免邮券优先
            if (hasFreeShippingCoupon ||
                    (bestCoupon != null && bestCoupon.getType() == Coupon.Type.FREE_SHIPPING)) {
                shippingFee = BigDecimal.ZERO;
            } else {
                shippingFee = shippingRule.calculateShipping(afterPoint, totalWeight);
            }
        }
        result.shippingFee = shippingFee;

        // ========== Step 8: 最终金额 ==========
        result.finalPayAmount = afterPoint.add(shippingFee).setScale(2, RoundingMode.HALF_UP);

        return result;
    }

    // ==================== 演示 ====================

    public static void main(String[] args) {
        // 构建测试数据

        // 商品1：iPhone 15，原价5999
        OrderItem item1 = new OrderItem(
                1001L, "iPhone 15 128GB",
                new BigDecimal("5999"), 1,
                100L, new BigDecimal("0.3")
        );

        // 商品2：iPhone 15 手机壳，原价99，买2件
        OrderItem item2 = new OrderItem(
                1002L, "iPhone 15 硅胶壳",
                new BigDecimal("99"), 2,
                101L, new BigDecimal("0.05")
        );

        // 商品3：正在秒杀的AirPods，原价1999，秒杀价1499
        OrderItem item3 = new OrderItem(
                1003L, "AirPods Pro 2",
                new BigDecimal("1999"), 1,
                102L, new BigDecimal("0.1")
        );
        item3.setSeckillInfo(new SeckillInfo(
                new BigDecimal("1499"),
                new Date(System.currentTimeMillis() - 3600000), // 1小时前开始
                new Date(System.currentTimeMillis() + 3600000)  // 1小时后结束
        ));

        List<OrderItem> items = Arrays.asList(item1, item2, item3);

        // 多件多折规则：手机壳SPU(101)满2件打8折
        List<MultiPieceDiscount.Threshold> thresholds = Arrays.asList(
                new MultiPieceDiscount.Threshold(2, new BigDecimal("0.8")),
                new MultiPieceDiscount.Threshold(3, new BigDecimal("0.7"))
        );
        MultiPieceDiscount multiPieceDiscount = new MultiPieceDiscount(101L, thresholds);

        // 满减规则：满5000减200，满10000减500
        List<FullReduction> fullReductions = Arrays.asList(
                new FullReduction(new BigDecimal("5000"), new BigDecimal("200"), "ALL"),
                new FullReduction(new BigDecimal("10000"), new BigDecimal("500"), "ALL")
        );

        // 运费规则：满99免邮，基础运费10元，续重2元/kg
        ShippingRule shippingRule = new ShippingRule(
                new BigDecimal("99"), new BigDecimal("10"), new BigDecimal("2")
        );

        // 积分规则：100积分=1元，最多用10000积分，最多抵扣订单30%
        PointRule pointRule = new PointRule(
                new BigDecimal("100"), 10000, new BigDecimal("0.3")
        );

        // 优惠券列表
        List<Coupon> coupons = Arrays.asList(
                // 满3000减100现金券
                new Coupon(1L, "满3000减100", Coupon.Type.CASH,
                        new BigDecimal("3000"), new BigDecimal("100"), null,
                        new Date(System.currentTimeMillis() - 86400000),
                        new Date(System.currentTimeMillis() + 86400000)),
                // 满5000减300现金券（更优）
                new Coupon(2L, "满5000减300", Coupon.Type.CASH,
                        new BigDecimal("5000"), new BigDecimal("300"), null,
                        new Date(System.currentTimeMillis() - 86400000),
                        new Date(System.currentTimeMillis() + 86400000)),
                // 9折券，最高减200
                new Coupon(3L, "9折券", Coupon.Type.PERCENT,
                        new BigDecimal("0"), new BigDecimal("0.9"), new BigDecimal("200"),
                        new Date(System.currentTimeMillis() - 86400000),
                        new Date(System.currentTimeMillis() + 86400000))
        );

        // 创建计算器
        OrderPriceCalculator2 calculator = new OrderPriceCalculator2(
                Arrays.asList(multiPieceDiscount),
                fullReductions,
                shippingRule,
                pointRule
        );

        // 执行计算：使用5000积分
        PriceResult result = calculator.calculate(items, coupons, 5000, false);

        // 输出结果
        System.out.println(result);

        // 验证计算过程
        System.out.println("\n========== 手动验证 ==========");
        System.out.println("商品原价: 5999*1 + 99*2 + 1999*1 = " +
                new BigDecimal("5999").add(new BigDecimal("99").multiply(new BigDecimal("2")))
                        .add(new BigDecimal("1999")) + "元");
        System.out.println("秒杀后: AirPods按1499算 = 5999 + 198 + 1499 = 7696元");
        System.out.println("多件多折: 手机壳2件8折 = 99*2*0.2 = 39.6元优惠");
        System.out.println("满减: 7696-39.6=7656.4 >= 5000, 减200元");
        System.out.println("优惠券: 7656.4-200=7456.4 >= 5000, 最优是满5000减300");
        System.out.println("积分: 5000/100=50元, 但上限30%*7156.4=2146.92, 实际50元");
        System.out.println("运费: 满99免邮，当前金额7106.4>99，免邮");
        System.out.println("最终: 7456.4 - 300 - 50 = 7106.4元");
    }
}