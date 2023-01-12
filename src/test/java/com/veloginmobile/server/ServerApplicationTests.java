package com.veloginmobile.server;

import com.veloginmobile.server.service.CrawlingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServerApplicationTests {

    @Autowired
    CrawlingService crawlingService;
    @Test
    void contextLoads() {
        crawlingService.crawling("https://velog.io/@morethanmin", "//*[@id=\"root\"]/div[2]/div[3]/div[4]/div[3]/div/div");
    }

}
