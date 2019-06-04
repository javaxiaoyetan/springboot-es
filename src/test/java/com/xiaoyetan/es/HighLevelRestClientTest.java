package com.xiaoyetan.es;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 高版本的
 * @Author zhangrong
 * @Date 2019/3/11 17:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HighLevelRestClientTest {
    Logger logger = LoggerFactory.getLogger(HighLevelRestClientTest.class);
    //java api :https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-supported-apis.html

    public final RestClientBuilder restClientBuilder = RestClient
            .builder(new HttpHost("47.96.190.178", 9200, "http"));

    RestHighLevelClient highLevelClient = new RestHighLevelClient(restClientBuilder);

    /**
     * 单文档添加索引 同步
     */
    @Test
    public void singleDocIndexAddWithSync(){
        try{
            //根据给写的index和type索引一个JSON资源，index和type由IndexRequest提供，IndexRequest也可提供id，若不提供时，id将自动生成；
            IndexRequest request = new IndexRequest(
                    "create",
                    "doc");

            //2. 组装要查找的文档源
//            String jsonString = "{" +
//                    "\"user\":\"kimchy\"," +
//                    "\"postDate\":\"2013-01-30\"," +
//                    "\"message\":\"trying out Elasticsearch\"" +
//                    "}";

            //3. 指定文档源 源可以是json，可以是map（最终也是会被转换成json）,可以是 XContentBuilder，可以直接key-value
            Map<String,Object> params =new HashMap<>();
            params.put("user","xiaoyetan");
            params.put("createDate",new Date());
            params.put("message","enter singleDocIndexAddWithOriginal");
            //这里要注意的就是如果不清楚版本最好不要指定version 不然会导致版本冲突
            //request.source(jsonString, XContentType.JSON);
            request.source(params);
            // 如果把操作类型指定成create，如果index已存在，那么也会导致版本冲突
            //.opType(DocWriteRequest.OpType.CREATE);
            //这里source方法支持多种传参 一般是map
            //request.source(Collections.emptyMap());
            //request.source("user", "kimchy", "postDate", new Date(),"message", "trying out Elasticsearch");

            //3.1 其他参数
            request.routing("routing"); //设置分片路由
//            request.parent("parent"); // 设置父文档id
//
//            request.timeout(TimeValue.timeValueSeconds(1)); // 等待主分片可用的超时时间, TimeValue形式
//            request.timeout("1s");                          // 等待主分片可用的超时时间, String形式
//
//            request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL); // 刷新策略
//            request.setRefreshPolicy("wait_for");                            // 刷新策略
//
//            request.version(2); // 版本
//
//            request.versionType(VersionType.EXTERNAL); // 版本类型
//
//            request.opType(DocWriteRequest.OpType.CREATE); // 操作类型
//            request.opType("create");                      // 操作类型
//
//            request.setPipeline("pipeline"); // 管道名称
            //4.调用 调用分同步调用和异步调用
            //同步调用 highLevelClient.index(request,RequestOptions.DEFAULT)
            IndexResponse indexResponse = highLevelClient.index(request,RequestOptions.DEFAULT);
            String index = indexResponse.getIndex();
            String type = indexResponse.getType();
            String id = indexResponse.getId();
            logger.info(String.format("返回结果:%s",index));
            long version = indexResponse.getVersion();
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                logger.info("新增");
            } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                logger.info("修改");
            }
            ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
            //{"failed":0,"failures":[],"fragment":false,"successful":1,"total":2}
            logger.info(String.format("返回结果:%s", JSON.toJSONString(shardInfo)));
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {

            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure :
                        shardInfo.getFailures()) {
                    String reason = failure.reason();
                }
            }


        }catch (IOException e){
            e.printStackTrace();
        }catch(ElasticsearchException e) {
            if (e.status() == RestStatus.CONFLICT) {
               System.out.println("版本冲突");
            }
            e.printStackTrace();
        }
    }

    /**
     * 单文档添加索引 异步接收结果
     */
    @Test
    public void singleDocIndexAddWithASync(){
        try{
            //1. 创建索引请求对象，指定索引、类型和文档id
            IndexRequest request = new IndexRequest(
                    "hah",
                    "doc",
                    "1");

            Map<String,Object> params =new HashMap<>();
            params.put("user","xiaoyetan");
            params.put("createDate",new Date());
            params.put("message","enter singleDocIndexAddWithOriginal");
            //这里要注意的就是如果不清楚版本最好不要指定version 不然会导致版本冲突
            //request.source(jsonString, XContentType.JSON);
            request.source(params);
            //3.1 其他参数
            request.routing("routing"); //设置分片路由
            //提供一个监听
            ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {
                @Override
                public void onResponse(IndexResponse indexResponse) {
                    // 成功
                    String index = indexResponse.getIndex();
                    String type = indexResponse.getType();
                    String id = indexResponse.getId();
                    logger.info(String.format("返回结果:%s",index));
                    long version = indexResponse.getVersion();
                    if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                        logger.info("新增");
                    } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                        logger.info("修改");
                    }
                    ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
                    logger.info("返回结果:"+JSON.toJSONString(shardInfo));
                }

                @Override
                public void onFailure(Exception e) {
                    // 失败
                    logger.info("失败了，大哥");
                }
            };
            //异步调用  需要传递一个监听器来处理异步调用返回的Response
            highLevelClient.indexAsync(request,RequestOptions.DEFAULT,listener);

        }catch(ElasticsearchException e) {
            if (e.status() == RestStatus.CONFLICT) {
                System.out.println("版本冲突");
            }
            e.printStackTrace();
        }

    }

    /**
     * 获取当个文档
     */
    public void singleDocGetWithOriginal(){
        GetRequest getRequest = new GetRequest(
                "posts",
                "doc",
                "1");
        getRequest.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE);
    }
}
