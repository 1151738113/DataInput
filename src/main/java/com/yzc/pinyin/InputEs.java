package com.yzc.pinyin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yzc.es.EsClientConfig;
import com.yzc.servers.InputPinyin;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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

    private String idnex = "pinyin_v2";

    private String type = "company";

    private int size = 1000;

    public void inputData(){
//        List<String> list = Lists.newArrayList();

//        //读取文件
//        InputStream in = InputEs.class.getClassLoader().getResourceAsStream("output.txt");
//        BufferedReader br;
//        InputStreamReader reader;
//        int i =0;
//        try {
//            reader = new InputStreamReader(in, "UTF-8");
//            br = new BufferedReader(reader);
//            for (String line = br.readLine();
//                 line != null; line = br.readLine()) {
////                if(i>size){
////                    break;
////                }
//                list.add(line);
////                i++;
//            }
//        } catch (Exception exception) {
//            logger.error("读取配置文件失败");
//        }



        List<String> list = readFile("output.txt");
        List<String> list1 = readFile("company.txt");
        setData(list,1);
        setData(list1,2);
    }

    private void setData(List<String> list,int s){
        //获取搜索引擎
        TransportClient client = esClientConfig.getClient();
        BulkRequestBuilder requestBuilder = client.prepareBulk();
        for(String ss : list){
            if(s == 1){
                //插入Users表中的数据
                Insert(requestBuilder,ss);
            }else{
                //T_BiddingSubInfo表中的数据
                InsertCompany(requestBuilder,ss);}
        }
        requestBuilder.get();
    }

    private void test(BulkRequestBuilder builder, String str){
        String[] strs = str.split("\t");
        if(strs.length<3){
            return;
        }
        //如果字段长度小于5，则过滤
        if(strs[1].length()<5){
            return;
        }
        Pattern pattern = Pattern.compile("\\w|\\d");
        Matcher matcher = pattern.matcher(strs[1]);
        if(matcher.find()){
            return;
        }
        Map<String,Object> map = Maps.newHashMap();
        map.put("name_index",strs[1]);
        map.put("name_search",strs[1]);
        map.put("name_fullindex",strs[1]);
        map.put("name_fullsearch",strs[1]);
        builder.add(new IndexRequest(idnex,type).source(map));
    }


    private void Insert(BulkRequestBuilder builder, String str){
        String[] strs = str.split("\t");
        if(strs.length<3){
            return;
        }
        //如果字段长度小于5，则过滤
        if(strs[1].length()<5){
            return;
        }
        Pattern pattern = Pattern.compile("\\w|\\d");
        Matcher matcher = pattern.matcher(strs[1]);
        if(matcher.find()){
            return;
        }
        Map<String,Object> map = Maps.newHashMap();
        map.put("id",strs[0]);
        map.put("name",strs[1]);
        map.put("companyId",strs[2]);
        builder.add(new IndexRequest(idnex,type).source(map));
    }

    /**
     * 导入公司名称表
     * @param builder
     * @param str
     */
    private void InsertCompany(BulkRequestBuilder builder, String str){
        String[] strs = str.split("\t");
        if(strs.length<2){
            return;
        }
        if(strs[1].length()<5){
            return;
        }
        Pattern pattern = Pattern.compile("\\w|\\d");
        Matcher matcher = pattern.matcher(strs[1]);
        if(matcher.find()){
            return;
        }
        Map<String,Object> map = Maps.newHashMap();
        map.put("companyId",strs[0]);
        map.put("name",strs[1]);
        builder.add(new IndexRequest(idnex,type).source(map));
    }

    private List<String> readFile(String path)
    {
        List<String> list = Lists.newArrayList();
        //读取文件
        InputStream in = InputEs.class.getClassLoader().getResourceAsStream(path);
        BufferedReader br;
        InputStreamReader reader;
        int i =0;
        try {
            reader = new InputStreamReader(in, "UTF-8");
            br = new BufferedReader(reader);
            for (String line = br.readLine();
                 line != null; line = br.readLine()) {
                list.add(line);
            }
        } catch (Exception exception) {
            logger.error("读取配置文件失败");
        }
        return list;
    }




}
