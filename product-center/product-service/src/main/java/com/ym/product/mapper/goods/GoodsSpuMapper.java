package com.ym.product.mapper.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ym.product.entity.goods.GoodsSpu;

/**
 * <p>
 * SPU绑定规格 Mapper 接口
 * </p>
 *
 * @author qushutao
 * @since 2026-06-18
 */
public interface GoodsSpuMapper extends BaseMapper<GoodsSpu> {

    String getMaxSpuCode();

}
