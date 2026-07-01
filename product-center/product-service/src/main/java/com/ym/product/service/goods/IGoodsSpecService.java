package com.ym.product.service.goods;

import com.ym.product.dto.resp.goodspec.GoodsSpecResp;
import com.ym.product.entity.goods.GoodsSpec;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 规格维度 服务类
 * </p>
 *
 * @author qushutao
 * @since 2026-06-18
 */
public interface IGoodsSpecService extends IService<GoodsSpec> {

    List<GoodsSpecResp> listGoodsSpecs();


}
