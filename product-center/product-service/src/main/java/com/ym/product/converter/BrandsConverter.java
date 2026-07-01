package com.ym.product.converter;


import com.ym.product.dto.resp.brands.BrandsResp;
import com.ym.product.entity.goods.GoodsBrand;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author qushutao
 * @since 2026-06-25 22:50
 **/
@Mapper
public interface BrandsConverter {

    BrandsConverter INSTANCE = Mappers.getMapper(BrandsConverter.class);

    BrandsResp toBrandsResp(GoodsBrand brand);

    List<BrandsResp> toBrandsRespList(List<GoodsBrand> brands);


}
