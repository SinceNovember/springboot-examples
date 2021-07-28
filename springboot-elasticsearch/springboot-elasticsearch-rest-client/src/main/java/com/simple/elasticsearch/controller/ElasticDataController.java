package com.simple.elasticsearch.controller;

import com.simple.elasticsearch.base.entity.ElasticEntity;
import com.simple.elasticsearch.base.service.BaseElasticService;
import com.simple.elasticsearch.utils.ElasticUtil;
import com.simple.elasticsearch.vo.ElasticDataVo;
import com.simple.elasticsearch.vo.QueryVo;
import com.simple.elasticsearch.vo.ResponseResult;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.simple.elasticsearch.vo.ResponseResult.ResponseCode;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequestMapping("/elastic/data")
@RestController
public class ElasticDataController {

    private static Logger log = LoggerFactory.getLogger(ElasticIndexController.class);

    @Resource
    private BaseElasticService baseElasticService;

    /**
     * 添加数据
     * @param elasticDataVo
     * @return
     */
    @PostMapping(value = "/add")
    public ResponseResult add(@RequestBody ElasticDataVo elasticDataVo) {
        ResponseResult response  = new ResponseResult();
        try {
            if (StringUtils.isBlank(elasticDataVo.getIdxName())) {
                response.setCode(ResponseCode.PARAM_ERROR_CODE.getCode());
                response.setMsg("索引为空，不允许提交");
                response.setStatus(false);
                log.info("索引为空");
                return response;
            }

            ElasticEntity elasticEntity = new ElasticEntity(elasticDataVo.getElasticEntity().getId(), elasticDataVo.getElasticEntity().getData());
            baseElasticService.insertOrUpdateOne(elasticDataVo.getIdxName(), elasticEntity);
        } catch (Exception e) {
            response.setCode(ResponseCode.ERROR.getCode());
            response.setMsg(ResponseCode.ERROR.getMsg());
            response.setStatus(false);
            log.error("插入数据异常，metadataVo={},异常信息={}", elasticDataVo.toString(),e.getMessage());
        }
        return response;
    }

    /**
     * 删除数据
     * @param elasticDataVo
     * @return
     */
    @PostMapping(value = "/delete")
    public ResponseResult delete(@RequestBody ElasticDataVo elasticDataVo){
        ResponseResult response = new ResponseResult();
        try {
            if(!StringUtils.isNotEmpty(elasticDataVo.getIdxName())){
                response.setCode(ResponseCode.PARAM_ERROR_CODE.getCode());
                response.setMsg("索引为空，不允许提交");
                response.setStatus(false);
                log.warn("索引为空");
                return response;
            }
            baseElasticService.deleteOne(elasticDataVo.getIdxName(),elasticDataVo.getElasticEntity());
        } catch (Exception e) {
            log.error("删除数据失败");
        }
        return response;

    }

    /**
     * 查润数据
     * @param queryVo
     * @return
     * format:
     * {
     *     "idxName" : "customer",
     *     "query" : {
     *         "match":{
     *             "name":"你好       "
     *         }
     *     }
     * }
     *
     */
    @GetMapping(value = "/get")
    public ResponseResult get(@RequestBody QueryVo queryVo) {
        ResponseResult response = new ResponseResult();

        if (StringUtils.isEmpty(queryVo.getIdxName())) {
            response.setCode(ResponseCode.PARAM_ERROR_CODE.getCode());
            response.setMsg("索引为空，不允许提交");
            response.setStatus(false);
            log.warn("索引为空");
            return response;
        }
        try {
            Class<?> clazz = HashMap.class;
            if (queryVo.getClassName() != null) {
                clazz = ElasticUtil.getClazz(queryVo.getClassName());
            }

            Map<String, Object> params = new HashMap<>();
            if (queryVo.getQuery().get("match") != null) {
                params = queryVo.getQuery().get("match");
            } else {
                params.put("match_all", "all");
            }
            Set<String> keys = params.keySet();
            MatchQueryBuilder queryBuilders=null;
            for(String ke:keys){
                queryBuilders = QueryBuilders.matchQuery(ke, params.get(ke));
            }
            if(null != queryBuilders){
                SearchSourceBuilder searchSourceBuilder = ElasticUtil.initSearchSourceBuilder(queryBuilders);
                List<?> data = baseElasticService.search(queryVo.getIdxName(), searchSourceBuilder, clazz);
                response.setData(data);
            }
        } catch (Exception e) {
            response.setCode(ResponseCode.ERROR.getCode());
            response.setMsg("服务忙，请稍后再试");
            response.setStatus(false);
            log.error("查询数据异常，metadataVo={},异常信息={}", queryVo.toString(),e.getMessage());
        }
        return response;
    }



}
