package com.yzc.es;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/11/12 0012.
 * Spring 工具类
 */
@Component
public class SpringUtil implements ApplicationContextAware {
//	private Logger logging = LoggerFactory.getLogger(SpringUtil.class);
	
    private static ApplicationContext applicationContext = null;

    // 非@import显式注入，@Component是必须的，且该类必须与main同包或子包
    // 若非同包或子包，则需手动import 注入，有没有@Component都一样
    // 可复制到Test同包测试
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringUtil.applicationContext == null){
            SpringUtil.applicationContext  = applicationContext;
        }
//        System.out.println("---------------com.ilex.jiutou.util.Test.Main.SubPackage.SpringUtil---------------");
//        logging.info("---------------com.ilex.jiutou.util.Test.Main.SubPackage.SpringUtil---------------");
    }

    /**
     *  获取applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     * @param name 获取Bean
     * @return
     */
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);

    }

    /**
     * 通过class获取Bean.
     * @param clazz 类类型
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }
}