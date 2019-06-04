##### 
https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-index.html

###### JAVA low Rest Client 

springboot 2.0 +es6.6.1

讲一个最简单的例子
Like ‘%套装%’
女士套装
Like ‘%套装女%’
查不到了
关系型数据库无法解决
几百万条数据 存储 统计分析 近实时  
不用讲搜索引擎如何来的 

##### Lucene 和es
Elasticsearch是一个基于Apache Lucene(TM)的开源搜索引擎。无论在开源还是专有领域，Lucene可以被认为是迄今为止最先进、性能最好的、功能最全的搜索引擎库。
但是，Lucene只是一个库。想要使用它，你必须使用Java来作为开发语言并将其直接集成到你的应用中，更糟糕的是，Lucene非常复杂，你需要深入了解检索的相关知识来理解它是如何工作的。
Elasticsearch也使用Java开发并使用Lucene作为其核心来实现所有索引和搜索的功能，但是它的目的是通过简单的RESTful API来隐藏Lucene的复杂性，从而让全文搜索变得简单.
国内外案例
维基百科、Github——用ES实现站内实时搜索。维基百科用ES实现全文搜索、高亮关键字。Github使用ES搜索它们1000多亿行代码。

百度——实施日志监控平台。 搜索 
谷歌、阿里、腾讯等
中大型电商平台  商品搜索和行为数据

站内近实时搜索 
分布式存储
海量数据分析 -----es的聚合查询   联想mysql的groupby
不太适用的场景
事务要求、频繁更新
解决方案 : 乐观锁、 悲观锁


##### 二. 安装
前提得有java环境,版本: jdk1.8+

47.96.190.178
root/pwd
安装es
创建用户
groupadd elsearch
useradd elsearch -g elsearch
passwd elsearch
su elsearch
cd ~
pwd
确定是/home/elsearch 再继续操作
安装
https://www.elastic.co/cn/downloads/elasticsearch
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.6.1.tar.gz
tar -zxvf elasticsearch-6.6.1.tar.gz
mv elasticsearch-6.6.1 es6
启动
cd es6
./bin/elasticsearch -d(后台启动) 

启动会报一些错 百度搜 然后解决 
安装中文分词
打开浏览器  ip:9200 看到如下信息
{
  "name" : "TKHtb4o",
  "cluster_name" : "elasticsearch",
  "cluster_uuid" : "rm82y4t6RSSXHuHXvYgB9g",
  "version" : {
    "number" : "6.6.1",
    "build_flavor" : "default",
    "build_type" : "tar",
    "build_hash" : "1fd8f69",
    "build_date" : "2019-02-13T17:10:04.160291Z",
    "build_snapshot" : false,
    "lucene_version" : "7.6.0",
    "minimum_wire_compatibility_version" : "5.6.0",
    "minimum_index_compatibility_version" : "5.0.0"
  },
  "tagline" : "You Know, for Search"
}
 安装中文分词
Elasticsearch自带分词器会简单地拆分每个汉字，没有根据词库来分词，这样的后果就是搜索结果很可能不是你想要的。这里推荐使用elasticsearch-analysis-ik，支持自定义词库

##### 三. 了解几个核心概念
1. 在逻辑层面：
 Index (索引)
这里的 Index 是名词，一个 Index 就像是传统关系数据库的 Database。它是 Elasticsearch 用来存储数据的逻辑区域 
 Type (类型)
文档归属于一种 Type，就像是关系数据库中的一个 Table
Document (文档)
Elasticsearch 使用 JSON 文档来表示一个对象。就像是关系数据库中一个 Table 中的一行Row数据
Field (字段)
每个文档包含多个字段，类似关系数据库中一个 Table 的列

Mysql : database > table > row >column
Es   : index > type>document >field

2. 在物理层面：

> Node (节点)：node 是一个运行着的 Elasticsearch 实例，一个 node 就是一个单独的 server

>Cluster (集群)：cluster 是多个 node 的集合

>Shard (分片)：数据分片，一个 index 可能会存在于多个 shard


##### 四. 如何开箱即用
传统用法 curl  但是不好用啊
https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-api.html

https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/index.html


Java Low Level REST Client: 低级别的REST客户端，通过http与集群交互，用户需自己编组请求JSON串，及解析响应JSON串。兼容所有ES版本。
Java High Level REST Client: 高级别的REST客户端，基于低级别的REST客户端，增加了编组请求JSON串、解析响应JSON串等相关api。使用的版本需要保持和ES服务端的版本一致，否则会有版本问题。

推荐high-client 


##### 五. 其他
与业务集成 贴合业务模式
Elasticsearch-head 
ELK 
Elasearch 集群
分片



版本跨越 1.x-2.x-5.x ++
1.X 2.x 5.X 三个版本，之所以出现版本号的跳跃，是因为ElasticSearch属于ELK(ElasticSearch, Logstash, Kibana)之一，是ELK工作站的一员。原先在ES1和2的版本时，Kibana等版本号有自己的版本数字命名，十分混乱。所以为了统一，之后便统一从5开始命名

代码


深入了解原理  https://es.xiaoleilu.com/  <<elaticsearch 权威指南>>









