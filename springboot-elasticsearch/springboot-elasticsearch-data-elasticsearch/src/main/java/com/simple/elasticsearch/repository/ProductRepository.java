package com.simple.elasticsearch.repository;

import com.simple.elasticsearch.dataobject.ESProductDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductRepository extends ElasticsearchRepository<ESProductDO, Integer> {
}
