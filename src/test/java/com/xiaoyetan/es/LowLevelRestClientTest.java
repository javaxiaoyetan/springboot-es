package com.xiaoyetan.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * 低级别的REST客户端，通过http与集群交互，用户需自己编组请求JSON串，及解析响应JSON串。兼容所有ES版本
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LowLevelRestClientTest {

    public final RestClient restClient = RestClient
            .builder(new HttpHost("47.96.190.178", 9200, "http"))
            .build();

    /**
     * 同步请求查询
     * @throws Exception
     */
    @Test
    public void searchBySync() throws Exception{
        //需要自己拼接请求json串
        String queryString = "{" +
                "  \"size\": 20," +
                "  \"query\": {" +
                "   \"range\": {" +
                "     \"createTime\": {" +
                "       \"gte\": \"2018-06-01 00:00:00\"" +
                "     }" +
                "   }" +
                "  }" +
                "}";

        try {
            //外部请求 httpclient
            //访问的 api需要自己准确记忆
            Request request =new Request("GET","/some_important_index*/_search");
            request.setJsonEntity(queryString);
            Response response = restClient.performRequest(request);
            System.out.println(response.getStatusLine().getStatusCode());
            String responseBody = null;

            responseBody = EntityUtils.toString(response.getEntity());
            System.out.println("******************************************** ");

            //结果自己解析
            JSONObject jsonObject = JSON.parseObject(responseBody);


            System.out.println(jsonObject.get("hits"));
        }catch (ResponseException e){
            e.printStackTrace();
        }finally {
            restClient.close();
        }
    }

    /**
     * 异步
     * @throws Exception
     */
    @Test
    public void searchAsync() throws Exception{
        try {
            String queryString = "{" +
                    "  \"size\": 20," +
                    "  \"query\": {" +
                    "   \"range\": {" +
                    "     \"createTime\": {" +
                    "       \"gte\": \"2018-06-01 00:00:00\"" +
                    "     }" +
                    "   }" +
                    "  }" +
                    "}";

            Request request =new Request("GET","/code_flow_log*/_search");
            request.setJsonEntity(queryString);
            restClient.performRequestAsync(request, new ResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    try {
                        JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));
                        System.out.println(jsonObject.get("hits"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                }
            });
        }finally {
            restClient.close();
        }

    }

}
