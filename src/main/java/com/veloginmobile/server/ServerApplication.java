package com.veloginmobile.server;

import com.veloginmobile.server.crawler.NewPostCrawler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {

    //DB 재작업 후 크롤링 쓰레드 활성화 해야함
    public static void main(String[] args) {
        Thread serverThread = new Thread(()->{
            SpringApplication.run(ServerApplication.class, args);
        });

//        Thread CrawlerThread = new Thread(()->{
//            NewPostCrawler newPostCrawler = new NewPostCrawler();
//            newPostCrawler.searchNewPost();
//        });

        serverThread.start();
//        CrawlerThread.start();
    }

}
