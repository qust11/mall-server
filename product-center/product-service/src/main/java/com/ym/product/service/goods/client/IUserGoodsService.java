package com.ym.product.service.goods.client;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ym.common.dto.req.PageReq;
import com.ym.product.dto.resp.goods.spu.GoodsSpuResp;

/**
 * @author qushutao
 * @since 2026-06-23 10:02
 **/
public interface IUserGoodsService {


    IPage<GoodsSpuResp> pageHotGoods(PageReq pageReq);

}
