package com.yzc.pinyin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yzc.es.EService;
import com.yzc.es.EsClientConfig;
import com.yzc.servers.InputPinyin;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wei.wang on 2018/9/20 0020.
 * 将企业名称导入elasticsearch，可通过拼音查询获得查询结果
 */
@Service
public class InputEs implements InputPinyin {
    private static final Logger logger = LoggerFactory.getLogger(InputEs.class);

    @Autowired
    EsClientConfig esClientConfig;

    private String idnex = "company_v2";

    private String type = "company";

    private int size = 1000;

    public void inputData(){
        List<String> list = Lists.newArrayList();
        //获取搜索引擎
        TransportClient client = esClientConfig.getClient();
        //读取文件
        InputStream in = InputEs.class.getClassLoader().getResourceAsStream("data.txt");
        BufferedReader br;
        InputStreamReader reader;
        int i =0;
        try {
            reader = new InputStreamReader(in, "UTF-8");
            br = new BufferedReader(reader);
            for (String line = br.readLine();
                 line != null; line = br.readLine()) {
//                if(i>size){
//                    break;
//                }
                list.add(line);
//                i++;
            }
        } catch (Exception exception) {
            logger.error("读取配置文件失败");
        }

        BulkRequestBuilder requestBuilder = client.prepareBulk();
        for(String ss : list){
            Insert(requestBuilder,ss);
        }
        requestBuilder.get();
    }

    private void Insert(BulkRequestBuilder builder, String str){
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()){
            return;
        }
        Map<String,Object> map = Maps.newHashMap();
        map.put("name",str);
        map.put("name_pinyin",str);
        builder.add(new IndexRequest(idnex,type).source(map));
    }

}
