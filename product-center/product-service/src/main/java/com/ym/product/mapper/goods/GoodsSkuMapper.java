package com.ym.product.mapper.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ym.product.bo.goods.GoodsNumBO;
import com.ym.product.bo.goods.GoodsPriceBO;
import com.ym.product.entity.goods.GoodsSku;

import java.util.List;

/**
 * <p>
 * 商品SKU规格库存表 Mapper 接口
 * </p>
 *
 * @author qushutao
 * @since 2026-06-18
 */
public interface GoodsSkuMapper extends BaseMapper<GoodsSku> {

    List<GoodsNumBO> getGoodsNumBOList(List<Long> spuIds);

    GoodsPriceBO getSpuRangePrice(Long spuId);
}
