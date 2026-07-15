package com.ym.product.service.goods.core.impl;

import com.ym.product.bo.goods.GoodsNumBO;
import com.ym.product.bo.goods.GoodsSpuCommonDto;
import com.ym.product.entity.goods.GoodsBrand;
import com.ym.product.entity.goods.GoodsSpu;
import com.ym.product.mapper.goods.GoodsSpuMapper;
import com.ym.product.service.goods.core.IGoodsBrandService;
import com.ym.product.service.goods.core.IGoodsSkuService;
import com.ym.product.service.goods.core.IGoodsSpuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * SPU绑定规格 服务实现类
 * </p>
 *
 * @author qushutao
 * @since 2026-06-18
 */
@Service
@RequiredArgsConstructor
public class GoodsSpuServiceImpl extends ServiceImpl<GoodsSpuMapper, GoodsSpu> implements IGoodsSpuService {

    private final IGoodsSkuService goodsSkuService;
    private final IGoodsBrandService goodsBrandService;
    @Override
    public String getMaxSpuCode() {
        String maxCode = baseMapper.getMaxSpuCode();
        if (StringUtils.isBlank(maxCode)){
            return "000001";
        }
        return maxCode;
    }

    @Override
    public void populateSkuInfo(List<? extends GoodsSpuCommonDto> records) {
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
}
