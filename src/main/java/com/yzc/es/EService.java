package com.yzc.es;

import org.apache.http.HttpHost;
import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders.exponentialDecayFunction;
import static org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders.randomFunction;

/**
 * Created by Administrator on 2017/11/12 0012.
 * doc: https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/index.html
 * 搜索服务
 */
@Component
public class EService {
    private static RestHighLevelClient _restClient;
    private static TransportClient _transportClient;
    
    private Logger logging = LoggerFactory.getLogger(EService.class);

    @Resource
    ESConfig esconfig;

    @Override
    protected void finalize() throws Throwable {
        if(_transportClient != null) _transportClient.close();
        super.finalize();
    }

    /**
     * 启动ES
     * @throws Exception
     */
    public void StartEServer() throws Exception {
        // System.out.println(esconfig == null);

        String[] hosts = esconfig.getHosts();

        if(hosts == null || hosts.length == 0){
//        	logging.error("ES配置错误");
        	throw new Exception("ES配置错误");
        }

        Settings settings = Settings.builder()
                .put("cluster.name", esconfig.getClusterName())
                .put("client.transport.sniff", true)
                .build();

        _transportClient = new PreBuiltTransportClient(settings);
            
//        System.out.println("ELASTICSEARCH HOSTS:" + hosts[0]);
//        logging.info("ELASTICSEARCH HOSTS:" + hosts[0]);

        HttpHost[] httpHosts = new HttpHost[hosts.length];

        for(int i=0;i<hosts.length;i++){
            httpHosts[i] = new HttpHost(hosts[i], 9200, "http");
            _transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hosts[i]), 9300));
        }

        RestClient lowLevelRestClient = RestClient.builder(httpHosts).build();
        _restClient = new RestHighLevelClient(lowLevelRestClient);

        printClusterInfo();
    }

    public static TransportClient getTransportClient(){
        return _transportClient;
    }

    /**
     * 获取可用的RestClient
     * @return RestClient
     */
    public static RestHighLevelClient getRestClient(){
        if(_restClient == null){
        	throw new NullPointerException("搜索引擎配置错误或没有初始化");
        }
        return _restClient;
    }

    /**
     * 打印日志信息
     */
    public void printClusterInfo(){

        try {
            MainResponse response = _restClient.info();
//            System.out.println("******************** ES CLUSTER INFO ********************");
//            System.out.println("Cluster Name: " + response.getClusterName());
//            System.out.println("Node Name: " + response.getNodeName());
//            System.out.println("Version: " + response.getVersion());
//            System.out.println("*********************************************************");
            logging.info("******************** ES CLUSTER INFO ********************\n"
            		+"Cluster Name: " + response.getClusterName()+"\n"
            		+"Node Name: " + response.getNodeName()+"\n"
            		+"Version: " + response.getVersion()+"\n"
            		+"*********************************************************");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // throw new NotImplementedException();
    }

    /**
     * 测试，可参考
     */
    public void test(){
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/5.6/java-rest-high-search.html#java-rest-high-request-highlighting
        SearchRequest request = new SearchRequest("logstash-2017.11");
        request.types("debug");

        SearchSourceBuilder builder = new SearchSourceBuilder();

        // 简单查询  https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-term-level-queries.html
        {
            // term
            QueryBuilder qb = termQuery("type", "1");
            // terms
            QueryBuilder qb2 = termsQuery("tags", "a", "b");
            // range
            QueryBuilder qb3 = rangeQuery("price").gte("10").lt("20");
            // exists
            QueryBuilder qb4 = rangeQuery("title");
        }
        // 复杂查询 https://www.cnblogs.com/zhangchenliang/p/4195406.html  https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-compound-queries.html#java-query-dsl-constant-score-query
        {
            // 忽略相关度查询
            QueryBuilder qb5 = constantScoreQuery(termQuery("title", "zhangsan")).boost(2.0f);
            // bool 查询
            QueryBuilder qb6 = boolQuery()
                    .must(matchQuery("title", "zhangsan"))
                    .must(termQuery("type", "1"))
                    .mustNot(termQuery("type2", "2"))
                    .should(termQuery("type3", "3"))
                    .must(existsQuery("account"));
            FunctionScoreQueryBuilder.FilterFunctionBuilder[] functions = {
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                            matchQuery("name", "kimchy"),
                            randomFunction("ABCDEF")),
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                            exponentialDecayFunction("age", 0L, 1L))
            };
            QueryBuilder qb7 = QueryBuilders.functionScoreQuery(functions);
            // builder.query(qb7);
        }

        builder.query(QueryBuilders.matchQuery("doc.GroupName", "SendMsg2WeChat").minimumShouldMatch("100%"));
        builder.from(0);
        builder.size(5);
        builder.sort(new FieldSortBuilder("doc.Created").order(SortOrder.DESC));
        builder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        //builder.fetchSource(new String[]{}, new String[]{"doc"});
        request.source(builder);

        try {
            SearchResponse response = _restClient.search(request);
            TimeValue took = response.getTook();
            Boolean terminatedEarly = response.isTerminatedEarly();
            boolean timeOut = response.isTimedOut();
            int totalShards = response.getTotalShards();

            SearchHits hits = response.getHits();
            long totalHits = hits.getTotalHits();
//            System.out.println("Total Hits:" + totalHits);
//            System.out.println(request.toString());
//            logging.info("Total Hits:" + totalHits);
//            logging.info(request.toString());
            for(SearchHit hit : hits.getHits()){
                String id = hit.getId();

                String sourceAsString = hit.getSourceAsString();
//                System.out.println(sourceAsString);
//                logging.info(sourceAsString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
