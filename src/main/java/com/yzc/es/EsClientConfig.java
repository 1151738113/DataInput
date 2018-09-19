package com.yzc.es;


import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * 链接ES
 * Created by Administrator on 2018/6/12 0012.
 */
@Configuration
@ComponentScan
public class EsClientConfig {

    //日志信息打印
   private  static  Logger logger = LoggerFactory.getLogger(EsClientConfig.class);

   @Value("${elasticsearch..name}")
    private String name;

   @Value("${elasticsearch.node}")
    private String hosts;

   @Value("${elasticsearch.post}")
    private int post;

    /**
     * 创建ES链接
     * @return es客户端
     */
    @Bean
    public TransportClient getClient(){
       Settings setting = Settings.builder()
               //通过设置sniff来自动嗅取集群节点
               .put("client.transport.sniff",true)
               .put("cluster.name",name).build();  //.put("client.transport.sniff",true)
       String[] ips = hosts.split(",");
       TransportClient client = null;
       try {
           List<InetSocketTransportAddress> addressList = new ArrayList<InetSocketTransportAddress>();
           for(String ip : ips){
               addressList.add(new InetSocketTransportAddress(InetAddress.getByName(ip), post));
           }
           client =new PreBuiltTransportClient(setting).addTransportAddresses(addressList.toArray(new InetSocketTransportAddress[addressList.size()]));
       } catch (Exception e) {
           logger.error("ES链接失败");
           e.printStackTrace();
       }

    return client;
   }



}
