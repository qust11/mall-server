package com.ym.product.converter;


import com.ym.product.dto.req.GoodsUpdateReq;
import com.ym.product.dto.resp.goods.spu.GoodsSpuDetailResp;
import com.ym.product.dto.resp.goods.spu.GoodsSpuListResp;
import com.ym.product.dto.resp.goods.spu.GoodsSpuResp;
import com.ym.product.entity.elastic.GoodsSpuDoc;
import com.ym.product.entity.goods.GoodsSpu;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author qushutao
 * @since 2026-06-25 22:50
 **/
@Mapper
public interface GoodsConverter {

    GoodsConverter INSTANCE = Mappers.getMapper(GoodsConverter.class);

    GoodsSpu toGoodsSpu(GoodsUpdateReq goodsUpdateReq);

    GoodsSpuListResp toGoodsResp(GoodsSpu goodsSpu);

    GoodsSpuDetailResp toGoodsDetailResp(GoodsSpu dbGoodsSpu);


    List<GoodsSpuListResp> batchToGoodsResp(List<GoodsSpu> records);

    GoodsSpuResp toGoodsSpuResp(GoodsSpu goodsSpu);

    List<GoodsSpuResp> batchToGoodsSpuResp(List<GoodsSpu> records);


    GoodsSpuResp docToGoodsSpuResp(GoodsSpuDoc goodsSpuDoc);

    List<GoodsSpuResp> batchDocToGoodsSpuResp(List<GoodsSpuDoc> records);
}
