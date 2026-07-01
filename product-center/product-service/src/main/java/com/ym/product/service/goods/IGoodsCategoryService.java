package com.ym.product.service.goods;

import com.ym.product.dto.resp.category.PrimaryCategoryResp;
import com.ym.product.entity.goods.GoodsCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品分类 服务类
 * </p>
 *
 * @author qushutao
 * @since 2026-06-18
 */
public interface IGoodsCategoryService extends IService<GoodsCategory> {

    List<PrimaryCategoryResp> listPrimaryCategory();

}
