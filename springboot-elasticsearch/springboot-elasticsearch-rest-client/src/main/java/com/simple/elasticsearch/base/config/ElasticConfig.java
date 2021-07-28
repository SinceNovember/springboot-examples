package com.simple.elasticsearch.base.config;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpPost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfig {

    @Value("${es.host}")
    public String host;

    @Value("${es.port}")
    public int port;

    @Value("${es.scheme}")
    public String scheme;

    @Bean
    public RestClientBuilder restClientBuilder() {
        return RestClient.builder(makeHttpHost());
    }

    @Bean
    public RestClient elasticsearchRestClient() {
        return RestClient.builder(makeHttpHost()).build();
    }

    private HttpHost makeHttpHost() {
        return new HttpHost(host, port, scheme);
    }

    @Bean
    public RestHighLevelClient restHighLevelClient(RestClientBuilder restClientBuilder) {
        return new RestHighLevelClient(restClientBuilder);
    }
}
