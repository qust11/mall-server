package com.ym.product.service.goods.core;

import com.ym.product.dto.resp.brands.BrandsResp;
import com.ym.product.entity.goods.GoodsBrand;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 规格维度 服务类
 * </p>
 *
 * @author qushutao
 * @since 2026-06-26
 */
public interface IGoodsBrandService extends IService<GoodsBrand> {

    List<BrandsResp> pageBrands();
}
