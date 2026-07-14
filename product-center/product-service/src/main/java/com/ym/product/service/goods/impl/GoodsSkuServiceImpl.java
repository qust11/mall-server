package com.ym.product.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ym.common.constant.ResultCodeEnum;
import com.ym.common.exception.BusinessException;
import com.ym.product.bo.goods.GoodsNumBO;
import com.ym.product.constant.GoodsRedisConstant;
import com.ym.product.dto.GoodsSkuLockDto;
import com.ym.product.entity.goods.GoodsSku;
import com.ym.product.mapper.goods.GoodsSkuMapper;
import com.ym.product.service.goods.IGoodsSkuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class GoodsSkuServiceImpl extends ServiceImpl<GoodsSkuMapper, GoodsSku> implements IGoodsSkuService {

    private final RedissonClient redisson;
    @Override
    public List<GoodsNumBO> getGoodsNumBOListBySpuIds(List<Long> spuIds) {
        return baseMapper.getGoodsNumBOList(spuIds);
    }


    @Override
    public void skuLockStock(GoodsSkuLockDto goodsSkuLockDto) {
        RLock lock = redisson.getLock(GoodsRedisConstant.GOODS_SPU_CODE_LOCK_KEY + goodsSkuLockDto.getSkuId());
        try {
            boolean isLock = lock.tryLock(GoodsRedisConstant.EXPIRE_TIME_FIVE_SECONDS, GoodsRedisConstant.EXPIRE_TIME_TEN_SECONDS, TimeUnit.SECONDS);
            if (!isLock) {
                throw new BusinessException(ResultCodeEnum.ACTIVITY_CREATE_FAILED, "请稍后再试");
            }
            GoodsSku goodsSku = getById(goodsSkuLockDto.getSkuId());
            if (null == goodsSku) {
                throw new BusinessException(ResultCodeEnum.ACTIVITY_CREATE_FAILED, "商品SKU不存在");
            }
            Integer lockStock = goodsSkuLockDto.getLockStock();
            if (lockStock > goodsSku.getRemainStock()) {
                throw new BusinessException(ResultCodeEnum.ACTIVITY_CREATE_FAILED, "库存不足");
            }
            update(new LambdaUpdateWrapper<GoodsSku>().eq(GoodsSku::getId, goodsSku.getId()).setDecrBy(GoodsSku::getRemainStock, lockStock).setIncrBy(GoodsSku::getLockStock, lockStock));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ResultCodeEnum.ACTIVITY_CREATE_FAILED);
        }finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
