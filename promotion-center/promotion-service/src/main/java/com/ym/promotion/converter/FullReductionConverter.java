package com.ym.promotion.converter;


import com.ym.promotion.dto.req.FullReductionReq;
import com.ym.promotion.entity.FullReduction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qushutao
 * @since 2026-06-25 22:50
 **/
@Mapper
public interface FullReductionConverter {

    FullReductionConverter INSTANCE = Mappers.getMapper(FullReductionConverter.class);

    FullReduction toFullReduction(FullReductionReq fullReductionReq);


}
