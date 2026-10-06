package com.ym.order.service.impl;


import com.alibaba.fastjson2.JSON;
import com.ym.common.bo.CartBO;
import com.ym.common.bo.CartDetailBO;
import com.ym.common.bo.CartSkuBO;
import com.ym.common.bo.CartSkuDetailBO;
import com.ym.common.constant.ResultCodeEnum;
import com.ym.common.exception.BusinessException;
import com.ym.common.result.Result;
import com.ym.common.util.RedisUtil;
import com.ym.common.util.UserHolderUtil;
import com.ym.order.bo.OrderBO;
import com.ym.order.calculate.PromotionEngine;
import com.ym.order.constant.OrderRedisConstant;
import com.ym.order.service.ICartService;
import com.ym.product.api.GoodSkuClient;
import com.ym.promotion.api.PromotionApi;
import com.ym.promotion.dto.PromotionDetailDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author qushutao
 * @since 2026-07-17 18:02
 **/
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    private final RedisUtil redisUtil;
    private final GoodSkuClient goodSkuClient;
    private final PromotionApi promotionApi;
    private final PromotionEngine promotionEngine;

    @Override
    public void addCart(CartBO cartBO) {
        Long userId = UserHolderUtil.get();
        String redisKey = OrderRedisConstant.CART_KEY_PREFIX + userId;
        CartBO cartBOFromRedis = redisUtil.getHash(redisKey, cartBO.getSkuId().toString(), CartBO.class);
        if (null == cartBOFromRedis) {
            Long hashAllMemberCount = redisUtil.getHashAllMemberCount(redisKey);
            if (hashAllMemberCount >= OrderRedisConstant.CART_SKU_MAX_NUM) {
                throw new BusinessException(ResultCodeEnum.CART_SKU_MAX_NUM);
            }
            cartBOFromRedis = cartBO;
        } else {
            cartBOFromRedis.setQuantity(cartBOFromRedis.getQuantity() + cartBO.getQuantity());
        }
        redisUtil.putHash(redisKey, cartBO.getSkuId().toString(), JSON.toJSONString(cartBOFromRedis), OrderRedisConstant.CART_EXPIRE_SECONDS);
    }

    @Override
    public CartDetailBO getCartSkuDetail() {
        Long userId = UserHolderUtil.get();
        String redisKey = OrderRedisConstant.CART_KEY_PREFIX + userId;
        List<CartBO> cartBOList = redisUtil.getHashAllMember(redisKey, CartBO.class);
        if (CollectionUtils.isEmpty(cartBOList)) {
            return emptyCartDetail();
        }

        List<Long> skuIds = cartBOList.stream().map(CartBO::getSkuId).toList();
        Result<List<CartSkuBO>> skuResult = goodSkuClient.getSkuInfo(skuIds);
        // 先校验响应再消费数据
        if (null == skuResult || !ResultCodeEnum.SUCCESS.getCode().equals(skuResult.getCode())) {
            throw new BusinessException(ResultCodeEnum.CART_NOT_EXIST, null == skuResult ? null : skuResult.getMsg());
        }
        if (CollectionUtils.isEmpty(skuResult.getData())) {
            // 没有商品信息 删除redis key
            redisUtil.del(redisKey);
            return emptyCartDetail();
        }

        // 整个购物车作为一个订单整体参与促销计算(满减/优惠券均按整单口径)
        OrderBO orderBO = buildOrderBO(skuResult.getData(), cartBOList);
        PromotionDetailDto promotionDetailDto = promotionApi.getUserAllPromotion(skuIds);
        promotionEngine.applyPromotions(orderBO, null == promotionDetailDto ? new PromotionDetailDto() : promotionDetailDto);

        return convertToCartDetailBO(orderBO, skuResult.getData());
    }

    private CartDetailBO emptyCartDetail() {
        CartDetailBO cartDetailBO = new CartDetailBO();
        cartDetailBO.setItems(new ArrayList<>());
        cartDetailBO.setTotalPrice(0L);
        cartDetailBO.setDiscountPrice(0L);
        cartDetailBO.setFinalPrice(0L);
        cartDetailBO.setOrderPromotions(new ArrayList<>());
        return cartDetailBO;
    }

    /**
     * 购物车所有商品合并为一个订单:每个 SKU 一行,行小计 = 单价 × 数量
     */
    private OrderBO buildOrderBO(List<CartSkuBO> skuList, List<CartBO> cartBOList) {
        Map<Long, CartBO> cartBOMap = cartBOList.stream().collect(Collectors.toMap(CartBO::getSkuId, Function.identity(), (a, b) -> a));
        List<OrderBO.OrderSkuBO> itemList = new ArrayList<>();
        long totalPrice = 0L;
        for (CartSkuBO cartSkuBO : skuList) {
            CartBO cartBO = cartBOMap.get(cartSkuBO.getSkuId());
            // 购物车中已无该商品则跳过
            if (null == cartBO || null == cartSkuBO.getPrice() || null == cartBO.getQuantity()) {
                continue;
            }
            OrderBO.OrderSkuBO orderSkuBO = new OrderBO.OrderSkuBO();
            orderSkuBO.setPrice(cartSkuBO.getPrice());
            orderSkuBO.setFinalPrice(cartSkuBO.getPrice() * cartBO.getQuantity());
            orderSkuBO.setDiscountPrice(0L);
            orderSkuBO.setSkuId(cartSkuBO.getSkuId());
            orderSkuBO.setSpuId(cartSkuBO.getSpuId());
            orderSkuBO.setSkuName(cartSkuBO.getSkuName());
            orderSkuBO.setCategoryId(cartSkuBO.getCategoryId());
            orderSkuBO.setQuantity(cartBO.getQuantity());
            itemList.add(orderSkuBO);
            totalPrice += orderSkuBO.getFinalPrice();
        }
        OrderBO orderBO = new OrderBO();
        orderBO.setItemList(itemList);
        orderBO.setTotalPrice(totalPrice);
        orderBO.setDiscountPrice(0L);
        orderBO.setFinalPrice(totalPrice);
        return orderBO;
    }

    private CartDetailBO convertToCartDetailBO(OrderBO orderBO, List<CartSkuBO> skuList) {
        Map<Long, CartSkuBO> skuMap = skuList.stream()
                .filter(sku -> null != sku.getSkuId())
                .collect(Collectors.toMap(CartSkuBO::getSkuId, Function.identity(), (a, b) -> a));

        List<CartSkuDetailBO> items = new ArrayList<>();
        long totalPrice = 0L;
        long finalPrice = 0L;
        long discountPrice = 0L;
        for (OrderBO.OrderSkuBO item : orderBO.getItemList()) {
            CartSkuBO cartSkuBO = skuMap.get(item.getSkuId());
            CartSkuDetailBO cartSkuDetailBO = new CartSkuDetailBO();
            cartSkuDetailBO.setSkuId(item.getSkuId());
            cartSkuDetailBO.setSpuId(item.getSpuId());
            if (null != cartSkuBO) {
                cartSkuDetailBO.setTotalStock(cartSkuBO.getTotalStock());
                cartSkuDetailBO.setRemainStock(cartSkuBO.getRemainStock());
                cartSkuDetailBO.setLockStock(cartSkuBO.getLockStock());
                cartSkuDetailBO.setSkuImg(cartSkuBO.getSkuImg());
                cartSkuDetailBO.setSpecIds(cartSkuBO.getSpecIds());
            }
            cartSkuDetailBO.setSkuName(item.getSkuName());
            cartSkuDetailBO.setQuantity(item.getQuantity());
            // 行原价 = 单价 × 数量
            long originPrice = item.getPrice() * item.getQuantity();
            cartSkuDetailBO.setTotalPrice(originPrice);
            cartSkuDetailBO.setFinalPrice(item.getFinalPrice());
            cartSkuDetailBO.setDiscountPrice(item.getDiscountPrice());
            cartSkuDetailBO.setDiscountInfo(item.getDiscountInfoList());
            items.add(cartSkuDetailBO);
            totalPrice += originPrice;
            finalPrice += item.getFinalPrice();
            discountPrice += item.getDiscountPrice();
        }

        CartDetailBO cartDetailBO = new CartDetailBO();
        cartDetailBO.setItems(items);
        cartDetailBO.setTotalPrice(totalPrice);
        cartDetailBO.setDiscountPrice(discountPrice);
        cartDetailBO.setFinalPrice(finalPrice);
        cartDetailBO.setOrderPromotions(Objects.requireNonNullElseGet(orderBO.getOrderDiscountInfo(), ArrayList::new));
        return cartDetailBO;
    }
}
