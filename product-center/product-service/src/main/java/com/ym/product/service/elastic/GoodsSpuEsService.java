package com.ym.product.service.elastic;


import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ym.common.constant.ResultCodeEnum;
import com.ym.common.dto.req.PageReq;
import com.ym.common.exception.BusinessException;
import com.ym.common.util.PageUtil;
import com.ym.product.bo.goods.GoodsNumBO;
import com.ym.product.entity.elastic.GoodsSpuDoc;
import com.ym.product.entity.goods.GoodsSpu;
import com.ym.product.repository.GoodsSpuRepository;
import com.ym.product.service.goods.core.IGoodsBrandService;
import com.ym.product.service.goods.core.IGoodsCategoryService;
import com.ym.product.service.goods.core.IGoodsSkuService;
import com.ym.product.service.goods.core.IGoodsSpuService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author qushutao
 * @since 2026-07-15 15:47
 **/
@Component
@RequiredArgsConstructor
public class GoodsSpuEsService {

    private final GoodsSpuRepository goodsSpuRepository;
    private final ElasticsearchOperations operations;
    private final ElasticsearchOperations elasticsearchOperations;
    private final IGoodsSpuService goodsSpuService;
    private final IGoodsSkuService goodsSkuService;
    private final IGoodsCategoryService goodsCategoryService;
    private final IGoodsBrandService goodsBrandService;

    public void save(GoodsSpuDoc goodsSpuDoc) {
        goodsSpuRepository.save(goodsSpuDoc);
    }

    public void updatePart(GoodsSpuDoc goodsSpuDoc) {
        operations.update(goodsSpuDoc);
    }

    public void removeById(Long id) {
        goodsSpuRepository.deleteById(id);
    }

    public IPage<GoodsSpuDoc> getAll(String keyword, Integer isHot, Long categoryId, PageReq pageReq) {
        // 1. 构建 Bool 查询
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
        if (StringUtils.isNotBlank(keyword)) {
            boolQueryBuilder.must(QueryBuilders.match()
                    .field("all")
                    .query(keyword)
                    .build()._toQuery());
        }

        if (null != categoryId) {
            boolQueryBuilder.must(QueryBuilders.term()
                    .field("categoryId")
                    .value(categoryId)
                    .build()._toQuery());
        }

        if (null != isHot){
            boolQueryBuilder.must(QueryBuilders.term()
                    .field("isHot")
                    .value(isHot)
                    .build()._toQuery());
        }
        Query query = NativeQuery.builder()
                .withQuery(boolQueryBuilder.build()._toQuery())
                .withPageable(PageRequest.of(pageReq.getCurrent() - 1, pageReq.getSize()))
                .build();

        // 5. 执行查询
        SearchHits<GoodsSpuDoc> search = elasticsearchOperations.search(query, GoodsSpuDoc.class);
        List<SearchHit<GoodsSpuDoc>> searchHits = search.getSearchHits();
        List<GoodsSpuDoc> goodsSpuDocs = searchHits.stream().map(SearchHit::getContent).toList();
        return PageUtil.getPagByParma(pageReq.getCurrent(), pageReq.getSize(),Long.valueOf(search.getTotalHits()).intValue(),goodsSpuDocs);
    }

    public GoodsSpuDoc getById(Long id) {
        Optional<GoodsSpuDoc> goodsSpuDocOptional = goodsSpuRepository.findById(id);
        GoodsSpuDoc goodsSpuDoc = goodsSpuDocOptional.orElse(null);
        if (goodsSpuDocOptional.isEmpty()){
            // 数据库中获取
            GoodsSpu goodsSpu = Optional.ofNullable(goodsSpuService.getById(id)).orElseThrow(() -> new BusinessException(ResultCodeEnum.GOODS_NOT_EXIST));
            goodsSpuDoc = saveToEs(goodsSpu);
        }
        return goodsSpuDoc;
    }

    private GoodsSpuDoc saveToEs(GoodsSpu goodsSpu) {
        GoodsSpuDoc goodsSpuDoc = buildGoodsSpuDoc(goodsSpu);
        return goodsSpuRepository.save(goodsSpuDoc);
    }

    public GoodsSpuDoc buildGoodsSpuDoc(GoodsSpu goodsSpu) {
        List<GoodsNumBO> goodsNumBOList = goodsSkuService.getGoodsNumBOListBySpuIds(Arrays.asList(goodsSpu.getId()));
        Map<Long, GoodsNumBO> skuIdToNumMap = goodsNumBOList.stream().collect(Collectors.toMap(GoodsNumBO::getSpuId, Function.identity(), (v1, v2) -> v1));

        GoodsSpuDoc entity = new GoodsSpuDoc();
        GoodsNumBO goodsNumBO = skuIdToNumMap.get(goodsSpu.getId());
        entity.setId(goodsSpu.getId());
        entity.setGoodsName(goodsSpu.getGoodsName());
        entity.setBrandName(goodsBrandService.getById(goodsSpu.getBrandId()).getBrandName());
        entity.setBrandId(goodsSpu.getBrandId());
        entity.setSubTitle(goodsSpu.getSubTitle());
        entity.setMainImg(goodsSpu.getMainImg());
        entity.setCategoryId(goodsSpu.getCategoryId());
        entity.setCategoryName(goodsCategoryService.getById(goodsSpu.getCategoryId()).getCategoryName());
        entity.setSkuCount(goodsNumBO.getSkuCount());
        entity.setStockCount(goodsNumBO.getStockCount());
        entity.setMaxPrice(goodsNumBO.getMaxPrice());
        entity.setMinPrice(goodsNumBO.getMinPrice());
        entity.setSkuId(goodsNumBO.getSkuId());
        entity.setIsHot(goodsSpu.getIsHot());
        entity.setIsNew(goodsSpu.getIsNew());
        entity.setSales(goodsSpu.getSales());
        entity.setSpuCode(goodsSpu.getSpuCode());

        return entity;
    }
}
