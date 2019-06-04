package com.xiaoyetan.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EsApplication {

    @Bean
    RestHighLevelClient getRestClient(){
        return new RestHighLevelClient(RestClient.builder(new HttpHost("47.96.190.178", 9200, "http")));
    }

    public static void main(String[] args) {
        SpringApplication.run(EsApplication.class, args);
    }

}
