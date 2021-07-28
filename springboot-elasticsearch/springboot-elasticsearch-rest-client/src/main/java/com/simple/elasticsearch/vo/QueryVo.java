package com.simple.elasticsearch.vo;

import java.util.Map;

/**
 * 查询条件
 */
public class QueryVo {
    /**
     * 索引名
     */
    private String idxName;
    /**
     * 需要反射的实体类型，用于对查询结果的封装
     */
    private String className;
    /**
     * 具体条件
     */
    private Map<String, Map<String,Object>> query;

    public String getIdxName() {
        return idxName;
    }

    public void setIdxName(String idxName) {
        this.idxName = idxName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, Map<String, Object>> getQuery() {
        return query;
    }

    public void setQuery(Map<String, Map<String, Object>> query) {
        this.query = query;
    }
}
