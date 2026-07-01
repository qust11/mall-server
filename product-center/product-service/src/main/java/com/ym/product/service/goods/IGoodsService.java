package com.ym.product.service.goods;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ym.common.dto.req.PageReq;
import com.ym.product.dto.req.GoodsReq;
import com.ym.product.dto.req.GoodsSkuReq;
import com.ym.product.dto.req.GoodsUpdateReq;
import com.ym.product.dto.resp.goods.sku.GoodsSkuListResp;
import com.ym.product.dto.resp.goods.spu.GoodsSpuDetailResp;
import com.ym.product.dto.resp.goods.spu.GoodsSpuListResp;
import com.ym.product.dto.resp.goods.spu.GoodsSpuResp;

/**
 * @author qushutao
 * @since 2026-06-23 10:02
 **/
public interface IGoodsService {
    IPage<GoodsSpuListResp> pageProduct(GoodsReq goodsReq);

    GoodsSpuListResp updateGoods(Long id , GoodsUpdateReq goodsUpdateReq);

    GoodsSpuListResp saveGoods(GoodsUpdateReq goodsUpdateReq);

    GoodsSpuDetailResp getGood(Long id);

    void deleteGood(Long id);

    IPage<GoodsSkuListResp> pageSkusBySpuId(Long spuId, PageReq pageReq);

    void updateSkusBySkuId(Long spuId, Long skuId, GoodsSkuReq goodsSkuReq);

    void saveSkusBySkuId(Long spuId, GoodsSkuReq goodsSkuReq);

    void deleteSku(Long skuId);

    IPage<GoodsSpuResp> pageHotGoods(PageReq pageReq);
}
