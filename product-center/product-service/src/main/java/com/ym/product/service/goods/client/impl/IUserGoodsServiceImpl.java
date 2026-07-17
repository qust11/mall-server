package com.ym.product.service.goods.client.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ym.common.dto.req.PageReq;
import com.ym.common.enums.YesNoEnums;
import com.ym.common.util.PageUtil;
import com.ym.product.converter.GoodsConverter;
import com.ym.product.converter.GoodsSkuConverter;
import com.ym.product.dto.resp.goods.sku.GoodsSkuDetailResp;
import com.ym.product.dto.resp.goods.spu.GoodsSpuUserDetailResp;
import com.ym.product.dto.resp.goods.spu.GoodsSpuUserResp;
import com.ym.product.entity.elastic.GoodsSpuDoc;
import com.ym.product.entity.goods.GoodsSku;
import com.ym.product.service.elastic.GoodsSpuEsService;
import com.ym.product.service.goods.core.IGoodsSkuService;
import com.ym.product.service.goods.core.IGoodsSpuService;
import com.ym.product.service.goods.client.IUserGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qushutao
 * @since 2026-06-23 10:05
 **/
@Service
@RequiredArgsConstructor
public class IUserGoodsServiceImpl implements IUserGoodsService {

    private final IGoodsSpuService goodsSpuService;
    private final GoodsSpuEsService goodsSpuEsService;
    private final IGoodsSkuService goodsSkuService;

    @Override
    public IPage<GoodsSpuUserResp> pageHotGoods(PageReq pageReq) {
        IPage<GoodsSpuDoc> result = goodsSpuEsService.getAll(null, YesNoEnums.YES.getCode(), null, pageReq);
        List<GoodsSpuUserResp> resultList = GoodsConverter.INSTANCE.batchDocToGoodsSpuResp(result.getRecords());
        goodsSpuService.populateSkuInfo(resultList);
        return PageUtil.getPage(result, resultList);
    }

    @Override
    public GoodsSpuUserDetailResp getSpuAndSkuDetailBySpuId(Long id) {
        GoodsSpuDoc goodsSpuDoc = goodsSpuEsService.getById(id);
        GoodsSpuUserDetailResp goodsSpuUserResp = GoodsConverter.INSTANCE.toGoodsSpuUserDetailResp(goodsSpuDoc);

        // @TODO SKU 信息先从数据库中获取 后续改到es中
        List<GoodsSku> goodsSkus = goodsSkuService.listBySpuId(id);
        goodsSpuUserResp.setSkuList(GoodsSkuConverter.INSTANCE.batchToGoodsSkuDetailResp(goodsSkus));
        return goodsSpuUserResp;
    }
}