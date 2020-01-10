package com.wchat.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan({"com.wchat"})
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.wchat.dao")
public class WchatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WchatApplication.class, args);
    }

}
