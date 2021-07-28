package com.simple.elasticsearch.vo;

import com.simple.elasticsearch.base.entity.ElasticEntity;

public class ElasticDataVo {
    /**
     * 索引名
     */
    private String idxName;
    /**
     * 数据存储对象
     */
    private ElasticEntity elasticEntity;

    public ElasticDataVo(String idxName, ElasticEntity elasticEntity) {
        this.idxName = idxName;
        this.elasticEntity = elasticEntity;
    }

    public String getIdxName() {
        return idxName;
    }

    public void setIdxName(String idxName) {
        this.idxName = idxName;
    }

    public ElasticEntity getElasticEntity() {
        return elasticEntity;
    }

    public void setElasticEntity(ElasticEntity elasticEntity) {
        this.elasticEntity = elasticEntity;
    }
}
