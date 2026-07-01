package com.ym.product.service.goods.impl;

import com.ym.product.bo.goods.GoodsNumBO;
import com.ym.product.bo.goods.GoodsPriceBO;
import com.ym.product.entity.goods.GoodsSku;
import com.ym.product.mapper.goods.GoodsSkuMapper;
import com.ym.product.service.goods.IGoodsSkuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品SKU规格库存表 服务实现类
 * </p>
 *
 * @author qushutao
 * @since 2026-06-18
 */
@Service
public class GoodsSkuServiceImpl extends ServiceImpl<GoodsSkuMapper, GoodsSku> implements IGoodsSkuService {

    @Override
    public List<GoodsNumBO> getGoodsNumBOListBySpuIds(List<Long> spuIds) {
        return baseMapper.getGoodsNumBOList(spuIds);
    }

    @Override
    public GoodsPriceBO getSpuRangePrice(Long spuId) {
        return baseMapper.getSpuRangePrice(spuId);
    }
}
