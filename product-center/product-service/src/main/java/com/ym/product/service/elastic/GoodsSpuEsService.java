package com.ym.product.service.elastic;


import com.ym.product.entity.elastic.GoodsSpuDoc;
import com.ym.product.repository.GoodsSpuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

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
    public void save(GoodsSpuDoc goodsSpuDoc) {
        goodsSpuRepository.save(goodsSpuDoc);
    }

    public void updatePart(GoodsSpuDoc goodsSpuDoc) {
        operations.update(goodsSpuDoc);
    }

    public void removeById(Long id) {
        goodsSpuRepository.deleteById(id);
    }
}
