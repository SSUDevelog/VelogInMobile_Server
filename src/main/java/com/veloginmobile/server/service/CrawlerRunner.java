package com.veloginmobile.server.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.veloginmobile.server.crawler.NewPostCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@Component
public class CrawlerRunner implements Runnable {

    @Autowired
    NewPostCrawler newPostCrawler;

    @Override
    @Scheduled(fixedDelay = 1000 * 60 * 3)
    public void run() {
        try {
            newPostCrawler.searchNewPost();
        } catch (IOException e) {

        } catch (FirebaseMessagingException e) {

        }
    }
}
