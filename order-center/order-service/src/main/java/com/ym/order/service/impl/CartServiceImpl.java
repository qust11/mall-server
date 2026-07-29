package com.ym.order.service.impl;


import com.alibaba.fastjson2.JSON;
import com.ym.common.bo.CartBO;
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
    public List<CartSkuDetailBO> getCartSkuDetail() {
        Long userId = UserHolderUtil.get();
        String redisKey = OrderRedisConstant.CART_KEY_PREFIX + userId;
        List<CartBO> cartBOList = redisUtil.getHashAllMember(redisKey, CartBO.class);
        List<Long> skuIds = cartBOList.stream().map(CartBO::getSkuId).toList();
        Result<List<CartSkuBO>> skuResult = goodSkuClient.getSkuInfo(skuIds);
        // 将CartSkuBO转换为List<OrderBO>
        if (CollectionUtils.isEmpty(skuResult.getData())) {
            return List.of();
        }

        List<OrderBO> orderList = skuResult.getData().stream().map(c->convertToOrderBO(c,cartBOList)).toList();
        PromotionDetailDto promotionDetailDto = promotionApi.getUserAllPromotion(skuIds);
        orderList.forEach(c-> promotionEngine.applyPromotions(c,promotionDetailDto));

        if (!ResultCodeEnum.SUCCESS.getCode().equals(skuResult.getCode())) {
            throw new BusinessException(ResultCodeEnum.CART_NOT_EXIST, skuResult.getMsg());
        }

        List<CartSkuDetailBO> result = convertToCartSkuDetailBO(orderList,skuResult.getData());

        return result;
    }

    private List<CartSkuDetailBO> convertToCartSkuDetailBO(List<OrderBO> orderList, List<CartSkuBO> skuList) {

        Map<Long,CartSkuBO> skuMap = skuList.stream().collect(Collectors.toMap(CartSkuBO::getSkuId, Function.identity()));
        List<CartSkuDetailBO> result = new ArrayList<>();
        for (OrderBO orderBO : orderList) {
            CartSkuDetailBO cartSkuDetailBO = new CartSkuDetailBO();

            List<OrderBO.OrderSkuBO> itemList = orderBO.getItemList();
            if (CollectionUtils.isEmpty(itemList)) {
                continue;
            }
            OrderBO.OrderSkuBO orderSkuBO = itemList.get(0);
            CartSkuBO cartSkuBO = skuMap.get(orderSkuBO.getSkuId());
            cartSkuDetailBO.setSkuId(orderSkuBO.getSkuId());
            cartSkuDetailBO.setSpuId(orderSkuBO.getSpuId());
            cartSkuDetailBO.setTotalStock(cartSkuBO.getTotalStock());
            cartSkuDetailBO.setRemainStock(cartSkuBO.getRemainStock());
            cartSkuDetailBO.setLockStock(cartSkuBO.getLockStock());
            cartSkuDetailBO.setSkuImg(cartSkuBO.getSkuImg());

            cartSkuDetailBO.setSkuName(orderSkuBO.getSkuName());
            cartSkuDetailBO.setQuantity(orderSkuBO.getQuantity());
            cartSkuDetailBO.setTotalPrice(orderBO.getTotalPrice());
            cartSkuDetailBO.setFinalPrice(orderBO.getFinalPrice());
            cartSkuDetailBO.setDiscountPrice(orderBO.getDiscountPrice());
            result.add(cartSkuDetailBO);
        }
        return result;

    }

    private OrderBO convertToOrderBO(CartSkuBO cartSkuBO,List<CartBO> cartBOList) {
        Map<Long,CartBO> cartBOMap = cartBOList.stream().collect(Collectors.toMap(CartBO::getSkuId, Function.identity()));
        OrderBO orderBO = new OrderBO();
        List<OrderBO.OrderSkuBO> itemList = new ArrayList<>();

        OrderBO.OrderSkuBO orderSkuBO = new OrderBO.OrderSkuBO();
        orderSkuBO.setFinalPrice(cartSkuBO.getPrice() * cartBOMap.get(cartSkuBO.getSkuId()).getQuantity());
        orderSkuBO.setPrice(orderSkuBO.getFinalPrice());
        orderSkuBO.setDiscountPrice(0L);
        orderSkuBO.setSkuId(cartSkuBO.getSkuId());
        orderSkuBO.setSpuId(cartSkuBO.getSpuId());
        orderSkuBO.setSkuName(cartSkuBO.getSkuName());
        orderSkuBO.setCategoryId(cartSkuBO.getCategoryId());
        orderSkuBO.setQuantity(cartBOMap.get(cartSkuBO.getSkuId()).getQuantity());
        itemList.add(orderSkuBO);
        orderBO.setItemList(itemList);
        orderBO.setTotalPrice(orderSkuBO.getFinalPrice());
        orderBO.setDiscountPrice(0L);
        orderBO.setFinalPrice(orderSkuBO.getFinalPrice());
        return orderBO;
    }
}
