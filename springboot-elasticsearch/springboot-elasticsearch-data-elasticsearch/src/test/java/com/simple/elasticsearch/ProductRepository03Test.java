package com.simple.elasticsearch;

import com.simple.elasticsearch.dataobject.ESProductDO;
import com.simple.elasticsearch.repository.ProductRepository03;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
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
import org.springframework.data.elasticsearch.core.SearchHitsImpl;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.data.querydsl.QSort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ProductRepository03Test {

    @Autowired
    private ProductRepository03 productRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    public void testSearch() {
        // ??????????????? 1 + ?????????????????????????????? id ??????
        Page<ESProductDO> page = productRepository.search(1, "??????",
                PageRequest.of(0, 5, Sort.Direction.ASC, "id"));
        System.out.println(page.getTotalPages());

        // ??????????????? 1 ??????????????? id ??????
        page = productRepository.search(1, null,
                PageRequest.of(0, 5, Sort.Direction.ASC, "id"));
        System.out.println(page.getTotalPages());
    }


    /**
     * ??????????????????
     */
    @Test
    public void testFindPageable() {
        NativeSearchQuery query = new NativeSearchQuery(new BoolQueryBuilder());
        query.setPageable(PageRequest.of(0, 10));
        // ??????1???
        SearchHits<ESProductDO> searchHits = elasticsearchRestTemplate.search(query, ESProductDO.class);

        // ??????2???
        // SearchHits<ESProductDO> searchHits = elasticsearchOperations.search(query, ESProductDO.class);
        List<ESProductDO> productList = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        System.out.println(productList.size());
        System.out.println(productList);
    }

    /**
     * ??????????????????
     */
    @Test
    public void testFindPageableScroll(){
        String scrollId = "1";
        NativeSearchQuery query = new NativeSearchQuery(new BoolQueryBuilder());
        query.setPageable(PageRequest.of(0, 10));
        SearchHits<ESProductDO> searchHits = null;
        if (StringUtils.isEmpty(scrollId)) {
            // ???????????????????????????????????? scroll ??????????????? 60s
            // ????????? scroll ????????????????????????????????? query??????????????????
            searchHits = elasticsearchRestTemplate.searchScrollStart(60000, query, ESProductDO.class, IndexCoordinates.of("product"));
            if (searchHits instanceof SearchHitsImpl) {
                scrollId = ((SearchHitsImpl) searchHits).getScrollId();
            }
        } else {
            // ????????????
            searchHits = elasticsearchRestTemplate.searchScrollContinue(scrollId, 60000, ESProductDO.class, IndexCoordinates.of("product"));
        }

        List<ESProductDO> articles = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
        if (articles.size() == 0) {
            // ????????????
            elasticsearchRestTemplate.searchScrollClear(Collections.singletonList(scrollId));
            scrollId = null;
        }

        System.out.println(articles.size());
        System.out.println(articles);

    }

    /**
     * ????????????
     */
    @Test
    public void testComplexSearch(){
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        // ???????????? cid
//        if (cid != null) {
            nativeSearchQueryBuilder.withFilter(QueryBuilders.termQuery("cid", 1));
//        }
        String keyword = "??????";
        // ??????
        if (StringUtils.hasText(keyword)) {
            FunctionScoreQueryBuilder.FilterFunctionBuilder[] functions = { // TODO ???????????????????????????
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(matchQuery("name", keyword),
                            ScoreFunctionBuilders.weightFactorFunction(10)),
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(matchQuery("sellPoint", keyword),
                            ScoreFunctionBuilders.weightFactorFunction(2)),
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(matchQuery("categoryName", keyword),
                            ScoreFunctionBuilders.weightFactorFunction(3)),
//                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(matchQuery("description", keyword),
//                            ScoreFunctionBuilders.weightFactorFunction(2)), // TODO ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
            };
            FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(functions)
                    .scoreMode(FunctionScoreQuery.ScoreMode.SUM)
                    .setMinScore(2F); // TODO ???????????????????????? score
            nativeSearchQueryBuilder.withQuery(functionScoreQueryBuilder);
        }
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name", "categoryName"));
        // ??????
        nativeSearchQueryBuilder.withPageable(pageable); // ??????
        // ??????
        if (StringUtils.hasText(keyword)) { // ????????????????????????
            nativeSearchQueryBuilder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
        } else if (pageable.getSort().isSorted()) { // ???????????????????????????
            pageable.getSort().get().forEach(sortField -> nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField.getProperty())
                    .order(sortField.getDirection().isAscending() ? SortOrder.ASC : SortOrder.DESC)));
        } else { // ????????????????????? ID ??????
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC));
        }

        nativeSearchQueryBuilder.withPageable(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize())); // ??????

        // ????????????
        SearchHits<ESProductDO> searchHits =  elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), ESProductDO.class);

        List<ESProductDO> productList = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        System.out.println(productList.size());
        System.out.println(productList);
    }

}