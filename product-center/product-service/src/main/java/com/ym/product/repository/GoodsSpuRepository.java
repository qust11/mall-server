package com.ym.product.repository;


import com.ym.product.entity.elastic.GoodsSpuDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 *
 * @author qushutao
 * @since 2026-07-15 15:46
 **/
public interface GoodsSpuRepository extends ElasticsearchRepository<GoodsSpuDoc, Long> {

    Page<GoodsSpuDoc> findByAllMatchesAndIsHotEquals(String keyword, Integer isHot, Pageable pageable);
}
