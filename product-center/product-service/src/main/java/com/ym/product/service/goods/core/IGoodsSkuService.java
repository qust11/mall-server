package com.ym.product.service.goods.core;

import com.ym.product.bo.goods.GoodsNumBO;
import com.ym.product.dto.GoodsSkuLockDto;
import com.ym.product.entity.goods.GoodsSku;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品SKU规格库存表 服务类
 * </p>
 *
 * @author qushutao
 * @since 2026-06-18
 */
public interface IGoodsSkuService extends IService<GoodsSku> {

    List<GoodsNumBO> getGoodsNumBOListBySpuIds(List<Long> spuIds);

    void skuLockStock(GoodsSkuLockDto goodsSkuLockDto);

}
