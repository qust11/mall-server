package com.ym.promotion.converter;


import com.ym.promotion.dto.FullReductionDto;
import com.ym.promotion.dto.req.FullReductionReq;
import com.ym.promotion.dto.resp.FullReductionResp;
import com.ym.promotion.entity.FullReduction;
import com.ym.promotion.entity.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author qushutao
 * @since 2026-06-25 22:50
 **/
@Mapper
public interface FullReductionConverter {

    FullReductionConverter INSTANCE = Mappers.getMapper(FullReductionConverter.class);

    FullReduction toFullReduction(FullReductionReq fullReductionReq);

    @Mappings({
            @Mapping(source = "fullReduction.id", target = "id"),
    })
    FullReductionResp toFullReductionResp(Promotion promotion, FullReduction fullReduction);

    FullReductionDto toFullReductionDto(FullReduction fullReduction);

    List<FullReductionDto> batchToFullReductionDto(List<FullReduction> fullReductions);

}
