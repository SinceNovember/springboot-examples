package com.simple.elasticsearch.utils;

import com.simple.elasticsearch.base.entity.ElasticEntity;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ElasticUtil {

    private static Logger log = LoggerFactory.getLogger(ElasticEntity.class);

    private ElasticUtil(){}

    public static Class<?> getClazz(String clazzName){
        try {
            return Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 设置查询Builder
     * @param queryBuilder
     * @param from  设置from选项，确定要开始搜索的结果索引。 默认为0。
     * @param size  设置大小选项，确定要返回的搜索匹配数。 默认为10。
     * @param timeout
     * @return
     */
    public static SearchSourceBuilder initSearchSourceBuilder(QueryBuilder queryBuilder, int from, int size, int timeout){

        //使用默认选项创建 SearchSourceBuilder 。
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //设置查询对象。可以使任何类型的 QueryBuilder
        sourceBuilder.query(queryBuilder);
        //设置from选项，确定要开始搜索的结果索引。 默认为0。
        sourceBuilder.from(from);
        //设置大小选项，确定要返回的搜索匹配数。 默认为10。
        sourceBuilder.size(size);
        sourceBuilder.timeout(new TimeValue(timeout, TimeUnit.SECONDS));
        return sourceBuilder;
    }

    public static SearchSourceBuilder initSearchSourceBuilder(QueryBuilder queryBuilder){
        return initSearchSourceBuilder(queryBuilder,0,10,60);
    }
}
