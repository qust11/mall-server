package com.ym.product.service.goods.core;

import com.ym.product.bo.goods.GoodsSpuCommonDto;
import com.ym.product.entity.goods.GoodsSpu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * SPU绑定规格 服务类
 * </p>
 *
 * @author qushutao
 * @since 2026-06-18
 */
public interface IGoodsSpuService extends IService<GoodsSpu> {

    String getMaxSpuCode();

    void populateSkuInfo(List<? extends GoodsSpuCommonDto> records);

}
