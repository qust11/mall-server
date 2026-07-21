package com.ym.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单价格计算引擎（重构版）
 * 计算顺序：秒杀/拼团价 → 多件多折 → 满减 → 优惠券(仅1张,取最大) → 积分抵扣 → 运费
 *
 * 主要改进：
 * 1. 满减实际优惠金额正确记录（不超过订单金额）
 * 2. 优惠券选择逻辑分离免邮券，避免互相干扰
 * 3. 计算步骤拆分为独立方法，提升可读性和可测性
 * 4. 增强空值安全与边界条件处理
 * 5. 多件多折明细更新使用Map优化性能
 * 6. 优惠券比较仅基于实际优惠金额，语义清晰
 */
public class OrderPriceCalculator {

    // ==================== 数据模型（保持不变） ====================

    /**
     * 订单项
     */
    public static class OrderItem {
        private Long skuId;
        private String skuName;
        private BigDecimal originalPrice;
        private Integer quantity;
        private Long spuId;
        private BigDecimal weight;
        private SeckillInfo seckillInfo;
        private GroupBuyInfo groupBuyInfo;

        public OrderItem(Long skuId, String skuName, BigDecimal originalPrice,
                         Integer quantity, Long spuId, BigDecimal weight) {
            this.skuId = skuId;
            this.skuName = skuName;
            this.originalPrice = originalPrice;
            this.quantity = quantity;
            this.spuId = spuId;
            this.weight = weight;
        }

        // Getters & Setters（保留原有）
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
        private BigDecimal seckillPrice;
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
        private BigDecimal groupPrice;
        private Integer minGroupSize;

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
        private Long spuId;
        private List<Threshold> thresholds;

        @Data
        @NoArgsConstructor
        public static class Threshold {
            private Integer minQuantity;
            private BigDecimal discountRate;

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
            return BigDecimal.ONE;
        }
    }

    /**
     * 满减规则
     */
    public static class FullReduction {
        private BigDecimal threshold;
        private BigDecimal reduction;
        private String scope; // "ALL" 或指定类目，扩展预留

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
        private BigDecimal threshold;
        private BigDecimal value;
        private BigDecimal maxDiscount;
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
         * 计算优惠金额（免邮券返回0，运费阶段处理）
         */
        public BigDecimal calculateDiscount(BigDecimal orderAmount) {
            if (!isValid() || orderAmount.compareTo(threshold) < 0) {
                return BigDecimal.ZERO;
            }
            switch (type) {
                case CASH:
                    return value.min(orderAmount);
                case PERCENT:
                    BigDecimal discount = orderAmount.multiply(BigDecimal.ONE.subtract(value));
                    return maxDiscount != null ? discount.min(maxDiscount) : discount;
                case FREE_SHIPPING:
                    return BigDecimal.ZERO;
                default:
                    return BigDecimal.ZERO;
            }
        }

        // Getters
        public Long getCouponId() { return couponId; }
        public String getCouponName() { return couponName; }
        public Type getType() { return type; }
        public BigDecimal getThreshold() { return threshold; }
        public BigDecimal getValue() { return value; }
        public BigDecimal getMaxDiscount() { return maxDiscount; }
    }

    /**
     * 积分抵扣规则
     */
    public static class PointRule {
        private BigDecimal exchangeRate;
        private Integer maxUsePoints;
        private BigDecimal maxDeductionRatio;

        public PointRule(BigDecimal exchangeRate, Integer maxUsePoints,
                         BigDecimal maxDeductionRatio) {
            this.exchangeRate = exchangeRate;
            this.maxUsePoints = maxUsePoints;
            this.maxDeductionRatio = maxDeductionRatio;
        }

        public BigDecimal calculateDeduction(int usePoints, BigDecimal orderAmount) {
            if (usePoints <= 0 || orderAmount.compareTo(BigDecimal.ZERO) <= 0) {
                return BigDecimal.ZERO;
            }
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
        private BigDecimal freeShippingThreshold;
        private BigDecimal baseFee;
        private BigDecimal weightFee;

        public ShippingRule(BigDecimal freeShippingThreshold,
                            BigDecimal baseFee, BigDecimal weightFee) {
            this.freeShippingThreshold = freeShippingThreshold;
            this.baseFee = baseFee;
            this.weightFee = weightFee;
        }

        public BigDecimal calculateShipping(BigDecimal orderAmount, BigDecimal totalWeight) {
            if (freeShippingThreshold != null &&
                    orderAmount.compareTo(freeShippingThreshold) >= 0) {
                return BigDecimal.ZERO;
            }
            BigDecimal weightCost = totalWeight.multiply(weightFee);
            return baseFee.add(weightCost).setScale(2, RoundingMode.HALF_UP);
        }
    }

    // ==================== 计算结果 ====================

    public static class PriceResult {
        private BigDecimal originalTotal;
        private BigDecimal seckillDiscount;
        private BigDecimal groupBuyDiscount;
        private BigDecimal multiPieceDiscount;
        private BigDecimal fullReduction;
        private BigDecimal couponDiscount;
        private BigDecimal pointDeduction;
        private BigDecimal shippingFee;
        private BigDecimal finalPayAmount;

        private Coupon usedCoupon;
        private List<ItemPriceDetail> itemDetails;

        public static class ItemPriceDetail {
            public Long skuId;
            public String skuName;
            public Integer quantity;
            public BigDecimal originalUnitPrice;
            public BigDecimal finalUnitPrice;
            public BigDecimal itemSubtotal;
            public String appliedActivity;
        }

        // Getters（保持不变）
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

        // setters for internal use
        void setOriginalTotal(BigDecimal originalTotal) { this.originalTotal = originalTotal; }
        void setSeckillDiscount(BigDecimal seckillDiscount) { this.seckillDiscount = seckillDiscount; }
        void setGroupBuyDiscount(BigDecimal groupBuyDiscount) { this.groupBuyDiscount = groupBuyDiscount; }
        void setMultiPieceDiscount(BigDecimal multiPieceDiscount) { this.multiPieceDiscount = multiPieceDiscount; }
        void setFullReduction(BigDecimal fullReduction) { this.fullReduction = fullReduction; }
        void setCouponDiscount(BigDecimal couponDiscount) { this.couponDiscount = couponDiscount; }
        void setPointDeduction(BigDecimal pointDeduction) { this.pointDeduction = pointDeduction; }
        void setShippingFee(BigDecimal shippingFee) { this.shippingFee = shippingFee; }
        void setFinalPayAmount(BigDecimal finalPayAmount) { this.finalPayAmount = finalPayAmount; }
        void setUsedCoupon(Coupon usedCoupon) { this.usedCoupon = usedCoupon; }
        void setItemDetails(List<ItemPriceDetail> itemDetails) { this.itemDetails = itemDetails; }

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

    private final List<MultiPieceDiscount> multiPieceDiscounts;
    private final List<FullReduction> fullReductions;
    private final ShippingRule shippingRule;
    private final PointRule pointRule;

    public OrderPriceCalculator(List<MultiPieceDiscount> multiPieceDiscounts,
                                List<FullReduction> fullReductions,
                                ShippingRule shippingRule,
                                PointRule pointRule) {
        this.multiPieceDiscounts = multiPieceDiscounts != null ? multiPieceDiscounts : Collections.emptyList();
        this.fullReductions = fullReductions != null ? fullReductions : Collections.emptyList();
        this.shippingRule = shippingRule;
        this.pointRule = pointRule;
    }

    /**
     * 核心计算入口
     *
     * @param items               订单商品列表（不可为空）
     * @param availableCoupons    可用优惠券列表（不含免邮券，免邮券通过 hasFreeShippingCoupon 单独传入）
     * @param usePoints           用户选择使用的积分
     * @param hasFreeShippingCoupon 是否使用免邮券（优先，与折扣券互斥）
     * @return 价格计算结果
     * @throws IllegalArgumentException 如果 items 为空或包含 null
     */
    public PriceResult calculate(List<OrderItem> items,
                                 List<Coupon> availableCoupons,
                                 int usePoints,
                                 boolean hasFreeShippingCoupon) {
        // 参数校验
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("订单商品列表不能为空");
        }
        for (OrderItem item : items) {
            if (item == null) throw new IllegalArgumentException("订单项不能为null");
        }

        PriceResult result = new PriceResult();
        result.setItemDetails(new ArrayList<>());

        // 1. 计算原价总和
        BigDecimal originalTotal = items.stream()
                .map(item -> item.getOriginalPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        result.setOriginalTotal(originalTotal);

        // 2. 应用秒杀/拼团价，并构建明细及SPU分组
        ActivityResult activityResult = applyActivityPricing(items);
        result.setSeckillDiscount(activityResult.seckillDiscount);
        result.setGroupBuyDiscount(activityResult.groupBuyDiscount);
        result.setItemDetails(activityResult.itemDetails);
        BigDecimal afterActivity = activityResult.afterActivityPrice;

        // 3. 应用多件多折
        MultiPieceResult multiResult = applyMultiPieceDiscount(activityResult.spuGroup, result.getItemDetails(), afterActivity);
        result.setMultiPieceDiscount(multiResult.discount);
        BigDecimal afterMultiPiece = multiResult.afterPrice;

        // 4. 应用满减（实际优惠金额正确记录）
        FullReductionResult fullResult = applyFullReduction(afterMultiPiece);
        result.setFullReduction(fullResult.discount);
        BigDecimal afterFullReduction = fullResult.afterPrice;

        // 5. 选择最优优惠券（现金/百分比券），免邮券单独处理
        CouponSelectionResult couponResult = selectBestCoupon(availableCoupons, afterFullReduction, hasFreeShippingCoupon);
        result.setUsedCoupon(couponResult.coupon);
        result.setCouponDiscount(couponResult.discount);
        BigDecimal afterCoupon = couponResult.afterPrice;

        // 6. 积分抵扣
        PointDeductionResult pointResult = applyPointDeduction(usePoints, afterCoupon);
        result.setPointDeduction(pointResult.deduction);
        BigDecimal afterPoint = pointResult.afterPrice;

        // 7. 计算运费
        ShippingFeeResult shippingResult = calculateShipping(items, afterPoint, hasFreeShippingCoupon || (couponResult.coupon != null && couponResult.coupon.getType() == Coupon.Type.FREE_SHIPPING));
        result.setShippingFee(shippingResult.fee);
        BigDecimal finalAmount = afterPoint.add(shippingResult.fee).setScale(2, RoundingMode.HALF_UP);
        result.setFinalPayAmount(finalAmount);

        return result;
    }

    // ==================== 各步骤私有方法 ====================

    private static class ActivityResult {
        BigDecimal seckillDiscount;
        BigDecimal groupBuyDiscount;
        BigDecimal afterActivityPrice;
        List<PriceResult.ItemPriceDetail> itemDetails;
        Map<Long, List<OrderItem>> spuGroup;
    }

    private ActivityResult applyActivityPricing(List<OrderItem> items) {
        ActivityResult result = new ActivityResult();
        result.itemDetails = new ArrayList<>();
        result.spuGroup = new HashMap<>();
        BigDecimal afterPrice = BigDecimal.ZERO;
        BigDecimal seckillTotal = BigDecimal.ZERO;
        BigDecimal groupBuyTotal = BigDecimal.ZERO;

        for (OrderItem item : items) {
            PriceResult.ItemPriceDetail detail = new PriceResult.ItemPriceDetail();
            detail.skuId = item.getSkuId();
            detail.skuName = item.getSkuName();
            detail.quantity = item.getQuantity();
            detail.originalUnitPrice = item.getOriginalPrice();

            BigDecimal unitPrice = item.getOriginalPrice();
            String activityDesc = "无活动";

            if (item.getSeckillInfo() != null && item.getSeckillInfo().isValid()) {
                BigDecimal seckillPrice = item.getSeckillInfo().getSeckillPrice();
                seckillTotal = seckillTotal.add(
                        item.getOriginalPrice().subtract(seckillPrice)
                                .multiply(BigDecimal.valueOf(item.getQuantity()))
                );
                unitPrice = seckillPrice;
                activityDesc = "秒杀价";
            } else if (item.getGroupBuyInfo() != null) {
                BigDecimal groupPrice = item.getGroupBuyInfo().getGroupPrice();
                groupBuyTotal = groupBuyTotal.add(
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
            result.itemDetails.add(detail);

            afterPrice = afterPrice.add(detail.itemSubtotal);

            // 按SPU分组
            result.spuGroup.computeIfAbsent(item.getSpuId(), k -> new ArrayList<>()).add(item);
        }

        result.seckillDiscount = seckillTotal.setScale(2, RoundingMode.HALF_UP);
        result.groupBuyDiscount = groupBuyTotal.setScale(2, RoundingMode.HALF_UP);
        result.afterActivityPrice = afterPrice.setScale(2, RoundingMode.HALF_UP);
        return result;
    }

    private static class MultiPieceResult {
        BigDecimal discount;
        BigDecimal afterPrice;
    }

    private MultiPieceResult applyMultiPieceDiscount(Map<Long, List<OrderItem>> spuGroup,
                                                     List<PriceResult.ItemPriceDetail> details,
                                                     BigDecimal currentPrice) {
        if (spuGroup.isEmpty() || multiPieceDiscounts.isEmpty()) {
            return new MultiPieceResult() {{ discount = BigDecimal.ZERO; afterPrice = currentPrice; }};
        }

        // 构建 skuId -> detail 映射，便于快速更新
        Map<Long, PriceResult.ItemPriceDetail> detailMap = details.stream()
                .collect(Collectors.toMap(d -> d.skuId, d -> d));

        BigDecimal totalDiscount = BigDecimal.ZERO;

        for (MultiPieceDiscount discountRule : multiPieceDiscounts) {
            List<OrderItem> groupItems = spuGroup.get(discountRule.getSpuId());
            if (groupItems == null || groupItems.isEmpty()) continue;

            int totalQty = groupItems.stream().mapToInt(OrderItem::getQuantity).sum();
            BigDecimal rate = discountRule.getDiscountRate(totalQty);
            if (rate.compareTo(BigDecimal.ONE) >= 0) continue;

            for (OrderItem item : groupItems) {
                // 获取当前单价（可能已秒杀/拼团）
                BigDecimal currentUnitPrice = item.getSeckillInfo() != null && item.getSeckillInfo().isValid()
                        ? item.getSeckillInfo().getSeckillPrice()
                        : (item.getGroupBuyInfo() != null ? item.getGroupBuyInfo().getGroupPrice() : item.getOriginalPrice());

                BigDecimal itemOriginalSubtotal = currentUnitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                BigDecimal itemDiscountedSubtotal = itemOriginalSubtotal.multiply(rate);
                BigDecimal itemDiscount = itemOriginalSubtotal.subtract(itemDiscountedSubtotal);

                totalDiscount = totalDiscount.add(itemDiscount);

                // 更新明细
                PriceResult.ItemPriceDetail detail = detailMap.get(item.getSkuId());
                if (detail != null) {
                    detail.appliedActivity += " + 多件多折(" + rate.multiply(BigDecimal.TEN) + "折)";
                    detail.itemSubtotal = itemDiscountedSubtotal.setScale(2, RoundingMode.HALF_UP);
                }
            }
        }

        BigDecimal discount = totalDiscount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal afterPrice = currentPrice.subtract(discount).max(BigDecimal.ZERO);
        MultiPieceResult res = new MultiPieceResult();
        res.discount = discount;
        res.afterPrice = afterPrice;
        return res;
    }

    private static class FullReductionResult {
        BigDecimal discount;
        BigDecimal afterPrice;
    }

    private FullReductionResult applyFullReduction(BigDecimal currentPrice) {
        BigDecimal totalDiscount = BigDecimal.ZERO;
        // 仅处理全局满减，按门槛降序
        List<FullReduction> sorted = fullReductions.stream()
                .filter(r -> "ALL".equals(r.getScope()))
                .sorted(Comparator.comparing(FullReduction::getThreshold).reversed())
                .collect(Collectors.toList());

        for (FullReduction reduction : sorted) {
            if (currentPrice.compareTo(reduction.getThreshold()) >= 0) {
                // 实际优惠 = min(减额, 当前金额)
                BigDecimal actual = reduction.getReduction().min(currentPrice);
                totalDiscount = actual;
                currentPrice = currentPrice.subtract(actual);
                break;
            }
        }

        FullReductionResult res = new FullReductionResult();
        res.discount = totalDiscount;
        res.afterPrice = currentPrice.max(BigDecimal.ZERO);
        return res;
    }

    private static class CouponSelectionResult {
        Coupon coupon;
        BigDecimal discount;
        BigDecimal afterPrice;
    }

    private CouponSelectionResult selectBestCoupon(List<Coupon> availableCoupons,
                                                   BigDecimal currentPrice,
                                                   boolean hasFreeShippingCoupon) {
        // 如果使用了免邮券，则不选择其他折扣券，直接返回
        if (hasFreeShippingCoupon) {
            // 构造一个虚拟免邮券用于展示（可选）
            Coupon freeCoupon = null;
            // 可以尝试从availableCoupons中找到免邮券，但更合理的是外部传入标识，此处仅返回null
            return new CouponSelectionResult() {{
                coupon = null; // 免邮券不参与折扣金额
                discount = BigDecimal.ZERO;
                afterPrice = currentPrice;
            }};
        }

        Coupon bestCoupon = null;
        BigDecimal bestDiscount = BigDecimal.ZERO;

        if (availableCoupons != null && !availableCoupons.isEmpty()) {
            for (Coupon coupon : availableCoupons) {
                if (!coupon.isValid()) continue;
                // 只处理现金券和百分比券，免邮券在此阶段忽略（已经外部处理）
                if (coupon.getType() == Coupon.Type.FREE_SHIPPING) continue;

                BigDecimal discount = coupon.calculateDiscount(currentPrice);
                if (discount.compareTo(BigDecimal.ZERO) > 0) {
                    if (bestCoupon == null || discount.compareTo(bestDiscount) > 0) {
                        bestCoupon = coupon;
                        bestDiscount = discount;
                    }
                    // 如果折扣相同，可考虑其他规则（如面值），但实际业务中很少，暂不处理
                }
            }
        }

        BigDecimal afterPrice = currentPrice.subtract(bestDiscount).max(BigDecimal.ZERO);
        CouponSelectionResult res = new CouponSelectionResult();
        res.coupon = bestCoupon;
        res.discount = bestDiscount;
        res.afterPrice = afterPrice;
        return res;
    }

    private static class PointDeductionResult {
        BigDecimal deduction;
        BigDecimal afterPrice;
    }

    private PointDeductionResult applyPointDeduction(int usePoints, BigDecimal currentPrice) {
        if (pointRule == null || usePoints <= 0) {
            return new PointDeductionResult() {{ deduction = BigDecimal.ZERO; afterPrice = currentPrice; }};
        }
        BigDecimal deduction = pointRule.calculateDeduction(usePoints, currentPrice);
        BigDecimal afterPrice = currentPrice.subtract(deduction).max(BigDecimal.ZERO);
        PointDeductionResult res = new PointDeductionResult();
        res.deduction = deduction;
        res.afterPrice = afterPrice;
        return res;
    }

    private static class ShippingFeeResult {
        BigDecimal fee;
    }

    private ShippingFeeResult calculateShipping(List<OrderItem> items, BigDecimal currentPrice, boolean freeShipping) {
        if (freeShipping || shippingRule == null) {
            return new ShippingFeeResult() {{ fee = BigDecimal.ZERO; }};
        }
        BigDecimal totalWeight = items.stream()
                .map(i -> {
                    BigDecimal w = i.getWeight();
                    return w != null ? w.multiply(BigDecimal.valueOf(i.getQuantity())) : BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal fee = shippingRule.calculateShipping(currentPrice, totalWeight);
        return new ShippingFeeResult() {{ fee = fee; }};
    }

    // ==================== 演示 ====================

    public static void main(String[] args) {
        // 构建测试数据（与原始示例相同）
        OrderItem item1 = new OrderItem(
                1001L, "iPhone 15 128GB",
                new BigDecimal("5999"), 1,
                100L, new BigDecimal("0.3")
        );

        OrderItem item2 = new OrderItem(
                1002L, "iPhone 15 硅胶壳",
                new BigDecimal("99"), 2,
                101L, new BigDecimal("0.05")
        );

        OrderItem item3 = new OrderItem(
                1003L, "AirPods Pro 2",
                new BigDecimal("1999"), 1,
                102L, new BigDecimal("0.1")
        );
        item3.setSeckillInfo(new SeckillInfo(
                new BigDecimal("1499"),
                new Date(System.currentTimeMillis() - 3600000),
                new Date(System.currentTimeMillis() + 3600000)
        ));

        List<OrderItem> items = Arrays.asList(item1, item2, item3);

        List<MultiPieceDiscount.Threshold> thresholds = Arrays.asList(
                new MultiPieceDiscount.Threshold(2, new BigDecimal("0.8")),
                new MultiPieceDiscount.Threshold(3, new BigDecimal("0.7"))
        );
        MultiPieceDiscount multiPieceDiscount = new MultiPieceDiscount(101L, thresholds);

        List<FullReduction> fullReductions = Arrays.asList(
                new FullReduction(new BigDecimal("5000"), new BigDecimal("200"), "ALL"),
                new FullReduction(new BigDecimal("10000"), new BigDecimal("500"), "ALL")
        );

        ShippingRule shippingRule = new ShippingRule(
                new BigDecimal("99"), new BigDecimal("10"), new BigDecimal("2")
        );

        PointRule pointRule = new PointRule(
                new BigDecimal("100"), 10000, new BigDecimal("0.3")
        );

        List<Coupon> coupons = Arrays.asList(
                new Coupon(1L, "满3000减100", Coupon.Type.CASH,
                        new BigDecimal("3000"), new BigDecimal("100"), null,
                        new Date(System.currentTimeMillis() - 86400000),
                        new Date(System.currentTimeMillis() + 86400000)),
                new Coupon(2L, "满5000减300", Coupon.Type.CASH,
                        new BigDecimal("5000"), new BigDecimal("300"), null,
                        new Date(System.currentTimeMillis() - 86400000),
                        new Date(System.currentTimeMillis() + 86400000)),
                new Coupon(3L, "9折券", Coupon.Type.PERCENT,
                        new BigDecimal("0"), new BigDecimal("0.9"), new BigDecimal("200"),
                        new Date(System.currentTimeMillis() - 86400000),
                        new Date(System.currentTimeMillis() + 86400000))
        );

        OrderPriceCalculator calculator = new OrderPriceCalculator(
                Arrays.asList(multiPieceDiscount),
                fullReductions,
                shippingRule,
                pointRule
        );

        PriceResult result = calculator.calculate(items, coupons, 5000, false);
        System.out.println(result);

        // 手工验证（已修正满减记录）
        System.out.println("\n========== 手动验证（修正后） ==========");
        System.out.println("商品原价: 5999 + 99*2 + 1999 = 8196元");
        System.out.println("秒杀后: 5999 + 198 + 1499 = 7696元");
        System.out.println("多件多折: 手机壳2件8折，优惠 99*2*0.2 = 39.6元 => 7656.4元");
        System.out.println("满减: 满5000减200，实际减200 => 7456.4元");
        System.out.println("优惠券: 最优满5000减300 => 7156.4元");
        System.out.println("积分: 5000/100=50元 => 7106.4元");
        System.out.println("运费: 满99免邮 => 0");
        System.out.println("最终: 7106.4元（与输出一致）");
    }
}