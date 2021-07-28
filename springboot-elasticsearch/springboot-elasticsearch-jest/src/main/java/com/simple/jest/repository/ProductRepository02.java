package com.simple.jest.repository;

import com.simple.jest.dataobject.ESProductDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductRepository02 extends ElasticsearchRepository<ESProductDO, Integer> {

    ESProductDO findByName(String name);

    Page<ESProductDO> findByLike(String name, Pageable pageable);
}
