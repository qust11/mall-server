package com.ym.cart.service.impl;


import com.alibaba.fastjson2.JSON;
import com.ym.cart.constant.CartRedisConstant;
import com.ym.cart.service.ICartService;
import com.ym.common.bo.CartBO;
import com.ym.common.bo.CartSkuDetailBO;
import com.ym.common.constant.ResultCodeEnum;
import com.ym.common.exception.BusinessException;
import com.ym.common.result.Result;
import com.ym.common.util.RedisUtil;
import com.ym.common.util.UserHolderUtil;
import com.ym.product.api.GoodSkuClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public void addCart(CartBO cartBO) {
        Long userId = UserHolderUtil.get();
        String redisKey = CartRedisConstant.CART_KEY_PREFIX + userId;
        CartBO cartBOFromRedis = redisUtil.getHash(redisKey, cartBO.getSkuId().toString(), CartBO.class);
        if (null == cartBOFromRedis) {
            Long hashAllMemberCount = redisUtil.getHashAllMemberCount(redisKey);
            if (hashAllMemberCount >= CartRedisConstant.CART_SKU_MAX_NUM) {
                throw new BusinessException(ResultCodeEnum.CART_SKU_MAX_NUM);
            }
            cartBOFromRedis = cartBO;
        } else {
            cartBOFromRedis.setQuantity(cartBOFromRedis.getQuantity() + cartBO.getQuantity());
        }
        redisUtil.putHash(redisKey, cartBO.getSkuId().toString(), JSON.toJSONString(cartBOFromRedis), CartRedisConstant.CART_EXPIRE_SECONDS);
    }

    @Override
    public List<CartSkuDetailBO> getCartSkuDetail() {
        Long userId = UserHolderUtil.get();
        String redisKey = CartRedisConstant.CART_KEY_PREFIX + userId;
        List<CartBO> cartBOList = redisUtil.getHashAllMember(redisKey, CartBO.class);
        List<Long> skuIds = cartBOList.stream().map(CartBO::getSkuId).toList();
        Result<List<CartSkuDetailBO>> skuResult = goodSkuClient.getSkuInfo(skuIds);
        if (!ResultCodeEnum.SUCCESS.getCode().equals(skuResult.getCode())) {
            throw new BusinessException(ResultCodeEnum.CART_NOT_EXIST, skuResult.getMsg());
        }
        return skuResult.getData();
    }
}
