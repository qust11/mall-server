package com.ym.product.mapper.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ym.product.dto.resp.goods.spu.GoodsSpuListResp;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品SKU规格库存表 Mapper 接口
 * </p>
 *
 * @author qushutao
 * @since 2026-06-18
 */
@Mapper
public interface GoodsMapper {

    IPage<GoodsSpuListResp> pageAdminProduct(Page<Object> objectPage);
}
