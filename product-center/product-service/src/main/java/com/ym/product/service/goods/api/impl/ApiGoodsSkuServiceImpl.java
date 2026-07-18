package com.ym.product.service.goods.api.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ym.common.bo.CartSkuDetailBO;
import com.ym.common.constant.ResultCodeEnum;
import com.ym.common.exception.BusinessException;
import com.ym.product.constant.GoodsRedisConstant;
import com.ym.product.converter.GoodsSkuConverter;
import com.ym.product.dto.GoodsSkuLockDto;
import com.ym.product.entity.goods.GoodsSku;
import com.ym.product.service.goods.api.IApiGoodsSkuService;
import com.ym.product.service.goods.core.IGoodsSkuService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 商品SKU规格库存表 服务实现类
 * </p>
 *
 * @author qushutao
 * @since 2026-06-18
 */
@Service
@RequiredArgsConstructor
public class ApiGoodsSkuServiceImpl implements IApiGoodsSkuService {

    private final RedissonClient redisson;
    private final IGoodsSkuService goodsSkuService;


    @Override
    public void skuLockStock(GoodsSkuLockDto goodsSkuLockDto) {
        RLock lock = redisson.getLock(GoodsRedisConstant.GOODS_SPU_CODE_LOCK_KEY + goodsSkuLockDto.getSkuId());
        try {
            boolean isLock = lock.tryLock(GoodsRedisConstant.EXPIRE_TIME_FIVE_SECONDS, GoodsRedisConstant.EXPIRE_TIME_TEN_SECONDS, TimeUnit.SECONDS);
            if (!isLock) {
                throw new BusinessException(ResultCodeEnum.ACTIVITY_CREATE_FAILED, "请稍后再试");
            }
            GoodsSku goodsSku = goodsSkuService.getById(goodsSkuLockDto.getSkuId());
            if (null == goodsSku) {
                throw new BusinessException(ResultCodeEnum.ACTIVITY_CREATE_FAILED, "商品SKU不存在");
            }
            Integer lockStock = goodsSkuLockDto.getLockStock();
            if (lockStock > goodsSku.getRemainStock()) {
                throw new BusinessException(ResultCodeEnum.ACTIVITY_CREATE_FAILED, "库存不足");
            }
            goodsSkuService.update(new LambdaUpdateWrapper<GoodsSku>().eq(GoodsSku::getId, goodsSku.getId()).setDecrBy(GoodsSku::getRemainStock, lockStock).setIncrBy(GoodsSku::getLockStock, lockStock));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ResultCodeEnum.ACTIVITY_CREATE_FAILED);
        }finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public List<CartSkuDetailBO> getSkuInfo(List<Long> skuIds) {
        List<GoodsSku> goodsSkus = goodsSkuService.listByIds(skuIds);
        return GoodsSkuConverter.INSTANCE.batchToCartSkuDetailBO(goodsSkus);
    }
}
