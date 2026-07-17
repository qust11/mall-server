package com.ym.product.converter;


import com.ym.product.dto.req.GoodsUpdateReq;
import com.ym.product.dto.resp.goods.spu.GoodsSpuDetailResp;
import com.ym.product.dto.resp.goods.spu.GoodsSpuListResp;
import com.ym.product.dto.resp.goods.spu.GoodsSpuUserDetailResp;
import com.ym.product.dto.resp.goods.spu.GoodsSpuUserResp;
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

    GoodsSpuUserResp toGoodsSpuResp(GoodsSpu goodsSpu);

    List<GoodsSpuUserResp> batchToGoodsSpuResp(List<GoodsSpu> records);


    GoodsSpuUserResp docToGoodsSpuResp(GoodsSpuDoc goodsSpuDoc);

    List<GoodsSpuUserResp> batchDocToGoodsSpuResp(List<GoodsSpuDoc> records);

    GoodsSpuUserDetailResp toGoodsSpuUserDetailResp(GoodsSpuDoc goodsSpuDoc);
}
