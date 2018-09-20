package com.yzc;

import com.yzc.servers.DefaultTaskRunner;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.yzc.dao")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class NotifyApplication {
	@Bean
	public DefaultTaskRunner defaultTaskRunner(){
		return new DefaultTaskRunner();
	}
	public static void main(String[] args) {
		SpringApplication.run(NotifyApplication.class, args);
	}
}
