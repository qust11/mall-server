package com.ym.product.service.elastic;


import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ym.common.dto.req.PageReq;
import com.ym.common.util.PageUtil;
import com.ym.product.entity.elastic.GoodsSpuDoc;
import com.ym.product.repository.GoodsSpuRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

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
}
