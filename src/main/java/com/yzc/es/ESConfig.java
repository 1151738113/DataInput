package com.yzc.es;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by gyz on 2017/11/12 0012.
 * ES 配置
 */
@Configuration
@ConfigurationProperties(prefix = "esconfig")
@Component
@Setter
@Getter
public class ESConfig {
    /**
     * 主机集群
     */
    private String[] hosts;

    /**
     * 站外公告 index
     */
    private String ixnotice;

    private String clusterName;

    public String[] getHosts() {
        return hosts;
    }

    public void setHosts(String[] hosts) {
        this.hosts = hosts;
    }

    public String getIxnotice() {
        return ixnotice;
    }

    public void setIxnotice(String ixnotice) {
        this.ixnotice = ixnotice;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }
}
