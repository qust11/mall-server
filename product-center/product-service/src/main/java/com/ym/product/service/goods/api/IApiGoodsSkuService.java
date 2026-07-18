package com.ym.product.service.goods.api;

import com.ym.common.bo.CartSkuDetailBO;
import com.ym.product.dto.GoodsSkuLockDto;

import java.util.List;

/**
 * <p>
 * 商品SKU规格库存表 服务类
 * </p>
 *
 * @author qushutao
 * @since 2026-06-18
 */
public interface IApiGoodsSkuService  {

    void skuLockStock(GoodsSkuLockDto goodsSkuLockDto);

    List<CartSkuDetailBO> getSkuInfo(List<Long> skuIds);

}
