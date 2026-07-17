package com.ym.product.service.goods.client;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ym.common.dto.req.PageReq;
import com.ym.product.dto.resp.goods.spu.GoodsSpuUserDetailResp;
import com.ym.product.dto.resp.goods.spu.GoodsSpuUserResp;

/**
 * @author qushutao
 * @since 2026-06-23 10:02
 **/
public interface IUserGoodsService {


    IPage<GoodsSpuUserResp> pageHotGoods(PageReq pageReq);

    GoodsSpuUserDetailResp getSpuAndSkuDetailBySpuId(Long id);

}
