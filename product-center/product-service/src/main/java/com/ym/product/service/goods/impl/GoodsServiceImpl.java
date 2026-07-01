package com.ym.product.service.goods.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ym.common.dto.req.PageReq;
import com.ym.common.enums.YesNoEnums;
import com.ym.common.util.PageUtil;
import com.ym.common.util.RedisUtil;
import com.ym.product.bo.goods.GoodsNumBO;
import com.ym.product.bo.goods.GoodsSpuCommonDto;
import com.ym.product.constant.GoodsConstant;
import com.ym.product.constant.GoodsRedisConstant;
import com.ym.product.converter.GoodsConverter;
import com.ym.product.converter.GoodsSkuConverter;
import com.ym.product.dto.req.GoodsReq;
import com.ym.product.dto.req.GoodsSkuReq;
import com.ym.product.dto.req.GoodsUpdateReq;
import com.ym.product.dto.resp.goods.sku.GoodsSkuListResp;
import com.ym.product.dto.resp.goods.spu.GoodsSpuDetailResp;
import com.ym.product.dto.resp.goods.spu.GoodsSpuListResp;
import com.ym.product.dto.resp.goods.spu.GoodsSpuResp;
import com.ym.product.entity.goods.GoodsBrand;
import com.ym.product.entity.goods.GoodsSku;
import com.ym.product.entity.goods.GoodsSpu;
import com.ym.product.mapper.goods.GoodsMapper;
import com.ym.product.service.goods.IGoodsBrandService;
import com.ym.product.service.goods.IGoodsService;
import com.ym.product.service.goods.IGoodsSkuService;
import com.ym.product.service.goods.IGoodsSpuService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author qushutao
 * @since 2026-06-23 10:05
 **/
@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements IGoodsService {
    private final GoodsMapper goodsMapper;

    private final IGoodsSkuService goodsSkuService;
    private final IGoodsSpuService goodsSpuService;
    private final IGoodsBrandService goodsBrandService;
    private final RedisUtil redisUtil;
    private final RedissonClient redisson;

    @Override
    public IPage<GoodsSpuListResp> pageProduct(GoodsReq goodsReq) {
        IPage<GoodsSpuListResp> result = goodsMapper.pageAdminProduct(new Page<>(goodsReq.getCurrent(), goodsReq.getSize()));
        List<GoodsSpuListResp> records = result.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return result;
        }
        populateSkuInfo(records);
        return result;
    }

    private void populateSkuInfo(List<? extends GoodsSpuCommonDto> records) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        List<Long> spuIds = records.stream().map(GoodsSpuCommonDto::getId).collect(Collectors.toList());
        List<GoodsNumBO> goodsNumBOList = goodsSkuService.getGoodsNumBOListBySpuIds(spuIds);
        Map<Long, GoodsNumBO> skuIdToNumMap = goodsNumBOList.stream().collect(Collectors.toMap(GoodsNumBO::getSpuId, Function.identity(), (v1, v2) -> v1));

        List<Long> goodBrandIds = records.stream().map(GoodsSpuCommonDto::getBrandId).collect(Collectors.toList());
        List<GoodsBrand> goodsBrandList = goodsBrandService.listByIds(goodBrandIds);
        Map<Long, GoodsBrand> brandIdToGoodsBrandMap = goodsBrandList.stream().collect(Collectors.toMap(GoodsBrand::getId, Function.identity(), (v1, v2) -> v1));
        records.forEach(goodsResp -> {
            GoodsNumBO goodsNumBO = skuIdToNumMap.get(goodsResp.getId());
            if (goodsNumBO != null) {
                goodsResp.setStockCount(goodsNumBO.getStockCount());
                goodsResp.setSkuCount(goodsNumBO.getSkuCount());
                goodsResp.setMaxPrice(goodsNumBO.getMaxPrice());
                goodsResp.setMinPrice(goodsNumBO.getMinPrice());
                goodsResp.setSkuId(goodsNumBO.getSkuId());
            }
            GoodsBrand goodsBrand = brandIdToGoodsBrandMap.get(goodsResp.getBrandId());
            if (goodsBrand != null) {
                goodsResp.setBrandName(goodsBrand.getBrandName());
            }
        });
    }

    @Override
    public GoodsSpuListResp updateGoods(Long id, GoodsUpdateReq goodsUpdateReq) {
        GoodsSpu dbGoodsSpu = goodsSpuService.getById(id);
        if (null == dbGoodsSpu) {
            return null;
        }
        GoodsSpu goodsSpu = GoodsConverter.INSTANCE.toGoodsSpu(goodsUpdateReq);
        goodsSpu.setId(id);
        goodsSpuService.updateById(goodsSpu);
        GoodsSpuListResp goodsSpuListResp = GoodsConverter.INSTANCE.toGoodsResp(goodsSpu);
        populateSkuInfo(Collections.singletonList(goodsSpuListResp));
        return goodsSpuListResp;
    }

    @Override
    public GoodsSpuListResp saveGoods(GoodsUpdateReq goodsUpdateReq) {
        RLock lock = redisson.getLock(GoodsRedisConstant.GOODS_SPU_CODE_LOCK_KEY);
        try {
            boolean isLock = lock.tryLock(GoodsRedisConstant.EXPIRE_TIME_FIVE_SECONDS, GoodsRedisConstant.EXPIRE_TIME_TEN_SECONDS, TimeUnit.SECONDS);
            if (!isLock) {
                throw new RuntimeException("当前新增商品人数较多，请稍后重试");
            }
            GoodsSpu goodsSpu = GoodsConverter.INSTANCE.toGoodsSpu(goodsUpdateReq);
            goodsSpu.setSpuCode(getSpuCode());
            goodsSpuService.save(goodsSpu);

            GoodsSpuListResp goodsSpuListResp = GoodsConverter.INSTANCE.toGoodsResp(goodsSpu);
            populateSkuInfo(Collections.singletonList(goodsSpuListResp));
            return goodsSpuListResp;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("当前新增商品人数较多，请稍后重试");
        } finally {
            // 4. 释放锁（仅当前线程持有锁时释放）
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private String getSpuCode() {
        Object o = redisUtil.get(GoodsRedisConstant.GOODS_SPU_CODE_PREFIX);
        Long code;
        if (null == o) {
            String maxSpuCode = goodsSpuService.getMaxSpuCode();
            code = 1L;
            if (StringUtils.isNotBlank(maxSpuCode)) {
                code = Long.parseLong(maxSpuCode.replace(GoodsConstant.GOODS_SPU_CODE_PREFIX, "")) + 1;
            }
            redisUtil.set(GoodsRedisConstant.GOODS_SPU_CODE_PREFIX, code + 1, 30 * 24 * 60 * 60L, TimeUnit.SECONDS);
        } else {
            code = Long.valueOf(o.toString());
            redisUtil.increment(GoodsConstant.GOODS_SPU_CODE_PREFIX);
        }
        return GoodsConstant.GOODS_SPU_CODE_PREFIX + code;
    }

    @Override
    public GoodsSpuDetailResp getGood(Long id) {
        GoodsSpu dbGoodsSpu = goodsSpuService.getById(id);
        return GoodsConverter.INSTANCE.toGoodsDetailResp(dbGoodsSpu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGood(Long id) {
        goodsSkuService.remove(new LambdaQueryWrapper<GoodsSku>().eq(GoodsSku::getSpuId, id));
        goodsSpuService.removeById(id);
    }

    @Override
    public IPage<GoodsSkuListResp> pageSkusBySpuId(Long spuId, PageReq pageReq) {
        IPage<GoodsSku> page = goodsSkuService.page(new Page<>(pageReq.getCurrent(), pageReq.getSize()), new LambdaQueryWrapper<GoodsSku>().eq(GoodsSku::getSpuId, spuId));
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return new Page<>(pageReq.getCurrent(), pageReq.getSize(), page.getTotal());
        }
        List<GoodsSkuListResp> goodsSpuList = GoodsSkuConverter.INSTANCE.batchToGoodsResp(page.getRecords());
        return PageUtil.getPage(page, goodsSpuList);
    }

    @Override
    public void updateSkusBySkuId(Long spuId, Long skuId, GoodsSkuReq goodsSkuReq) {
        GoodsSku dbGoodsSku = goodsSkuService.getOne(new LambdaQueryWrapper<GoodsSku>().eq(GoodsSku::getSpuId, spuId).eq(GoodsSku::getId, skuId));
        if (null == dbGoodsSku) {
            return;
        }
        GoodsSku goodsSku = GoodsSkuConverter.INSTANCE.toGoodsSku(goodsSkuReq);
        goodsSku.setId(skuId);
        goodsSkuService.updateById(goodsSku);
    }

    @Override
    public void saveSkusBySkuId(Long spuId, GoodsSkuReq goodsSkuReq) {
        GoodsSku goodsSku = GoodsSkuConverter.INSTANCE.toGoodsSku(goodsSkuReq);
        goodsSku.setSpuId(spuId);
        goodsSkuService.save(goodsSku);
    }

    @Override
    public void deleteSku(Long skuId) {
        goodsSkuService.removeById(skuId);
    }

    @Override
    public IPage<GoodsSpuResp> pageHotGoods(PageReq pageReq) {
        IPage<GoodsSpu> result = goodsSpuService.page(new Page<>(pageReq.getCurrent(), pageReq.getSize()), new LambdaQueryWrapper<GoodsSpu>()
                .eq(GoodsSpu::getIsHot, YesNoEnums.YES.getCode()));
        List<GoodsSpuResp> resultList = GoodsConverter.INSTANCE.batchToGoodsSpuResp(result.getRecords());
        populateSkuInfo(resultList);
        return PageUtil.getPage(result, resultList);
    }
}