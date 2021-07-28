package com.simple.elasticsearch;

import com.simple.elasticsearch.dataobject.ESProductDO;
import com.simple.elasticsearch.repository.ProductRepository02;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ProductRepository02Test {

    @Autowired
    private ProductRepository02 productRepository;


    @Test // 根据名字获得一条记录
    public void testFindByName() {
        ESProductDO product = productRepository.findByName("我的测试啊");
        System.out.println(product);
    }

    @Test // 使用 name 模糊查询，分页返回结果
    public void testFindByNameLikePageable() {
        // 根据情况，是否要制造测试数据
        if (true) {
            testInsert();
        }

        // 创建排序条件
        Sort sort = Sort.by(Sort.Direction.DESC, "id"); // ID 倒序
        // 创建分页条件。
        Pageable pageable = PageRequest.of(0, 10, sort);
        // 执行分页操作
        Page<ESProductDO> page = productRepository.findByNameLike("芋道", pageable);
        // 打印
        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        System.out.println(page.getContent());
    }



    /**
     * 为了给分页制造一点数据
     */
    private void testInsert() {
        for (int i = 1; i <= 100; i++) {
            ESProductDO product = new ESProductDO();
            product.setId(i); // 一般 ES 的 ID 编号，使用 DB 数据对应的编号。这里，先写死
            product.setName("芋道源码：" + i);
            product.setSellPoint("愿半生编码，如一生老友");
            product.setDescription("我只是一个描述");
            product.setCid(1);
            product.setCategoryName("技术");
            productRepository.save(product);
        }
    }

}