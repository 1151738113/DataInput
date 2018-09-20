package com.yzc.servers.impl;

import com.google.common.collect.Maps;
import com.yzc.dao.INotifyInputMapper;
import com.yzc.es.EService;
import com.yzc.es.EsClientConfig;
import com.yzc.model.Tsmnoticesendlogs;
import com.yzc.servers.ESDataInput;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by wei.wang on 2018/9/12 0012.
 */
@Service
public class ESDataInputImpl implements ESDataInput {

    @Autowired
    EsClientConfig esClientConfig;

    @Resource
    INotifyInputMapper iNotifyInput;

    @Value("${elasticsearch.index}")
    private String index;

    @Value("${elasticsearch.type}")
    private String type;

    @Value("${mysql.start}")
    private int start;

    //分页插入数据
    int size = 1000;

    @Override
    public void input() {

        long start_time = System.currentTimeMillis();

        TransportClient client = esClientConfig.getClient();
        //获取总数
        long titol = iNotifyInput.getCount("2018-09-11 14:24:36");

        //获取当前查询数量
        long start_now = titol - start;

        //获取插入次数
        float times = start_now / size;

        //获取最后的数据
        float last = start_now % size;
        //批量插入
        for (int i = 0; i <= times; i++) {
            //查询数据
            List<Tsmnoticesendlogs> list = iNotifyInput.getNotify(start, size, "'2018-09-11 14:24:36'");
            BulkRequestBuilder requestBuilder = client.prepareBulk();
            list.forEach(result -> setData(result,requestBuilder));
            BulkResponse response = requestBuilder.get();
            start += list.size();
            System.out.println("已导入 -" + start + "- 条数据");
        }
        long end_time = System.currentTimeMillis();
        System.out.println("导入时间--->"+(start_time-end_time)+" ms");

        //结束后再查一次数量更新数据（目的，防止数据导入时，数据存在更新）
        long titol_now = iNotifyInput.getCount("2018-09-11 14:24:36");
        if(titol_now == titol){
            return;
        }
        //获得最新的插入次数
        float i = (titol_now - titol)/size;
        //从上次数据的最后一条更新
        for(int s=0; s<=i ;s++){
            List<Tsmnoticesendlogs> list = iNotifyInput.getNotify((int)titol, size, "'2018-09-11 14:24:36'");
            BulkRequestBuilder requestBuilder = client.prepareBulk();
            list.forEach(result -> setData(result,requestBuilder));
            BulkResponse response = requestBuilder.get();
        }
    }

    private void setData(Tsmnoticesendlogs smnNoticeSendlogs, BulkRequestBuilder bulkProcessor) {
        Map<String, Object> source = Maps.newHashMap();
        try {
            source.putAll(geLogstMap(smnNoticeSendlogs));
            String id = smnNoticeSendlogs.getNoticesendlogsid();
            bulkProcessor.add(new IndexRequest(index, type, id).source(source));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> geLogstMap(Tsmnoticesendlogs smnoticesendlogs) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, Object> map = Maps.newHashMap();
            map.put("NoticeSendLogsId", smnoticesendlogs.getNoticesendlogsid());
            map.put("BusinessType", smnoticesendlogs.getBusinesstype());
            map.put("ModuleCode", smnoticesendlogs.getModulecode());
            map.put("ModuleName", smnoticesendlogs.getModulename());
            map.put("MemberLevel", smnoticesendlogs.getMemberlevel());
            map.put("SendType", smnoticesendlogs.getSendtype());
            map.put("Receiver", smnoticesendlogs.getReceiver());
            map.put("ReceiverName", smnoticesendlogs.getReceivername());
            map.put("MessageTitle", smnoticesendlogs.getMessagetitle());
            map.put("MessageContent", smnoticesendlogs.getMessagecontent());
            map.put("ReadUrl", smnoticesendlogs.getReadurl());
            map.put("SkipUrl", smnoticesendlogs.getSkipurl());
            map.put("CompanyId", smnoticesendlogs.getCompanyid());
            map.put("CompanyName", smnoticesendlogs.getCompanyname());
            map.put("ReceiveCompanyId", smnoticesendlogs.getReceivecompanyid());
            map.put("ReceiveCompanyName", smnoticesendlogs.getReceivecompanyname());
            if (smnoticesendlogs.getSendtime() != null) {
//                Date date = fmt.parse(String.valueOf(smnoticesendlogs.getSendtime()));
                map.put("SendTime", format(fmt.format(smnoticesendlogs.getSendtime())));
            } else {
                map.put("SendTime", smnoticesendlogs.getSendtime());
            }
            map.put("IsDel", smnoticesendlogs.getIsdel());
            map.put("IsRead", smnoticesendlogs.getIsread());
            return map;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    //处理日期格式
    private String format(String date) {
        String[] dates = date.split(" ");
        return dates[0] + "T" + dates[1] + "+08:00";
    }

}
