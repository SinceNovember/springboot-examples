package com.simple.jest.repository;

import com.simple.jest.dataobject.ESProductDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductRepository extends ElasticsearchRepository<ESProductDO, Integer> {
}
