package com.ym.product.service.goods.client.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ym.common.dto.req.PageReq;
import com.ym.common.enums.YesNoEnums;
import com.ym.common.util.PageUtil;
import com.ym.product.converter.GoodsConverter;
import com.ym.product.dto.resp.goods.spu.GoodsSpuResp;
import com.ym.product.entity.goods.GoodsSpu;
import com.ym.product.service.goods.core.IGoodsBrandService;
import com.ym.product.service.goods.core.IGoodsSkuService;
import com.ym.product.service.goods.core.IGoodsSpuService;
import com.ym.product.service.goods.client.IClientGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qushutao
 * @since 2026-06-23 10:05
 **/
@Service
@RequiredArgsConstructor
public class IClientGoodsServiceImpl implements IClientGoodsService {

    private final IGoodsSkuService goodsSkuService;
    private final IGoodsSpuService goodsSpuService;
    private final IGoodsBrandService goodsBrandService;


    @Override
    public IPage<GoodsSpuResp> pageHotGoods(PageReq pageReq) {
        IPage<GoodsSpu> result = goodsSpuService.page(new Page<>(pageReq.getCurrent(), pageReq.getSize()), new LambdaQueryWrapper<GoodsSpu>()
                .eq(GoodsSpu::getIsHot, YesNoEnums.YES.getCode()));
        List<GoodsSpuResp> resultList = GoodsConverter.INSTANCE.batchToGoodsSpuResp(result.getRecords());
        goodsSpuService.populateSkuInfo(resultList);
        return PageUtil.getPage(result, resultList);
    }
}