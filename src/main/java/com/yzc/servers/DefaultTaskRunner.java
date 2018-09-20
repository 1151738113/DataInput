package com.yzc.servers;


import com.yzc.es.EService;
import com.yzc.es.SpringUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;

import java.util.TimeZone;

/**
 * Created by gyz on 2017/11/29 0029.
 */
public class DefaultTaskRunner implements ApplicationRunner, Ordered {

    public int getOrder(){
        return 1;
    }

    public void run(ApplicationArguments args){
        // 查看所有的可用 bean
        // SpringUtil.PrintAllBeanNames();

        // 设置时区为+8:00
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));

        // 启动搜索引擎
        StartEService();
    }

    /**
     * 启动搜索引擎
     */
    private void StartEService(){
        try {
            EService es = SpringUtil.getBean(EService.class);
            es.StartEServer();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
