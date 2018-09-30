package com.yzc.search;

import com.google.common.collect.Lists;
import com.yzc.es.EsClientConfig;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by wei.wang on 2018/9/30 0030.
 * 拼音检索
 */
@Service
public class PinyinSearchImpl implements PinyinSearch{

    private String index = "pinyin_v2";
    private String type = "company";

    @Autowired
    EsClientConfig esClientConfig;

    @Override
    public List<String> search(String word) {
        List<String> list = Lists.newArrayList();
        TransportClient client = esClientConfig.getClient();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        QueryBuilder builder1 = QueryBuilders.matchPhraseQuery("name",word).boost(10f);
        QueryBuilder builder2 = QueryBuilders.matchQuery("name",word).minimumShouldMatch("100%").boost(1f);
        boolQueryBuilder.should(builder1);
        boolQueryBuilder.should(builder2);
        SearchRequestBuilder requestBuilder = client.prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setSize(10)
                .setQuery(boolQueryBuilder);
        SearchResponse response = requestBuilder.get();
        SearchHit[] hit = response.getHits().getHits();
        for(SearchHit searchHitFields : hit){
            Map<String,Object> source = searchHitFields.getSource();
            list.add(source.get("name").toString());
        }
        return list;
    }
}
