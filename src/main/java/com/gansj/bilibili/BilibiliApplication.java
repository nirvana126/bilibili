package com.gansj.bilibili;

import com.gansj.bilibili.core.WebsocketService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
@MapperScan("com.gansj.bilibili.mapper")
public class BilibiliApplication {

    public static void main(String[] args) {

        ApplicationContext app =SpringApplication.run(BilibiliApplication.class, args);
//        WebsocketService.setApplicationContext(app);
    }

}
