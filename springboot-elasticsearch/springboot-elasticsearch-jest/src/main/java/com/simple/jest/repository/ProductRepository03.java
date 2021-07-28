package com.simple.jest.repository;

import com.simple.jest.dataobject.ESProductDO;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductRepository03 extends ElasticsearchRepository<ESProductDO, Integer> {
//    default Page<ESProductDO> search(Integer cid, String keyword, Pageable pageable) {
////        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
////        if (cid != null) {
////            nativeSearchQueryBuilder.withFilter(QueryBuilders.termQuery("cid", cid));
////        }
//    };
}
