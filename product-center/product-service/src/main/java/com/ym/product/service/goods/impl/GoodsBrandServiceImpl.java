package com.ym.product.service.goods.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ym.product.converter.BrandsConverter;
import com.ym.product.dto.resp.brands.BrandsResp;
import com.ym.product.entity.goods.GoodsBrand;
import com.ym.product.mapper.goods.GoodsBrandMapper;
import com.ym.product.service.goods.IGoodsBrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 规格维度 服务实现类
 * </p>
 *
 * @author qushutao
 * @since 2026-06-26
 */
@Service
public class GoodsBrandServiceImpl extends ServiceImpl<GoodsBrandMapper, GoodsBrand> implements IGoodsBrandService {

    @Override
    public List<BrandsResp> pageBrands() {
        List<GoodsBrand> brands = list();
        List<BrandsResp> result = BrandsConverter.INSTANCE.toBrandsRespList(brands);
        return result;
    }
}
