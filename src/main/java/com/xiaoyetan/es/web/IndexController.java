package com.xiaoyetan.es.web;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 索引
 */
@RestController
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 创建
     * @return
     */
    @GetMapping("/create")
    public  String createIndex(){
        //1. 创建索引请求对象，指定索引、类型和文档id
        //创建一个mall的数据库，创建一张表product,
        IndexRequest request = new IndexRequest(
                "mall",
                "product",
                "1");
        //往表中插入一条数据
        Map<String,Object> params =new HashMap<>();
        params.put("desc","Five Plus2019新款女夏装蕾丝连衣裙短袖V领荷叶边长裙");
        params.put("brand","Five Plus");
        params.put("specs","米白010 黑色090 V领 简约 XS S M L");
        //这里要注意的就是如果不清楚版本最好不要指定version 不然会导致版本冲突
        //request.source(jsonString, XContentType.JSON);
        request.source(params);
        try {
            IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
            return  indexResponse.toString();
        }catch (IOException ex){
            ex.printStackTrace();
        }finally {
            try {
                restHighLevelClient.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return "";
    }

   @GetMapping("query")
    public String query(){
       GetRequest getRequest =new GetRequest("mall",
               "product",
               "1");
       try {
           GetResponse getResponse = restHighLevelClient.get(getRequest,RequestOptions.DEFAULT);
           return getResponse.toString();
       }catch (IOException e){
           e.printStackTrace();
       }
      return  "";
   }

    /**
     * 通过关键字搜索
     * @return
     */
    @GetMapping("queryByKeyWords")
    public String queryByKeyWords(){
        GetRequest getRequest =new GetRequest("mall",
                "product",
                "1");
        getRequest.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE);

        try {
            GetResponse getResponse = restHighLevelClient.get(getRequest,RequestOptions.DEFAULT);
            return getResponse.toString();
        }catch (IOException e){
            e.printStackTrace();
        }
        return  "";
    }


}
