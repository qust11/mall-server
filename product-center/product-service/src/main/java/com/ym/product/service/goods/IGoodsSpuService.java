package com.ym.product.service.goods;

import com.ym.product.entity.goods.GoodsSpu;
import com.baomidou.mybatisplus.extension.service.IService;

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

}
