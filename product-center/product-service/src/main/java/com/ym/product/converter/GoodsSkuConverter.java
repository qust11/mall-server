package com.ym.product.converter;


import com.ym.product.dto.req.GoodsSkuReq;
import com.ym.product.dto.resp.goods.sku.GoodsSkuDetailResp;
import com.ym.product.dto.resp.goods.sku.GoodsSkuListResp;
import com.ym.product.entity.goods.GoodsSku;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author qushutao
 * @since 2026-06-25 22:50
 **/
@Mapper
public interface GoodsSkuConverter {

    GoodsSkuConverter INSTANCE = Mappers.getMapper(GoodsSkuConverter.class);

    GoodsSkuListResp toGoodsSkuListRest(GoodsSku goodsSku);

    List<GoodsSkuListResp> batchToGoodsResp(List<GoodsSku> goodsSpuList);

    GoodsSku toGoodsSku(GoodsSkuReq goodsSkuReq);

    @Mapping(target = "discountPrice", source = "price")
    GoodsSkuDetailResp toGoodsSkuDetailResp(GoodsSku goodsSku);

    List<GoodsSkuDetailResp> batchToGoodsSkuDetailResp(List<GoodsSku> goodsSkuList);
}
