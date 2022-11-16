package com.veloginmobile.server.crawler;

import com.veloginmobile.server.data.entity.Subscribe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewPostCrawler {

    @PersistenceUnit
    EntityManagerFactory factory;
    EntityManager em;

    public NewPostCrawler() {
        this.factory = Persistence.createEntityManagerFactory("name");
        this.em = factory.createEntityManager();
    }

    public void searchNewPost() {
        while (Boolean.TRUE){
            Subscribe subscribe = new Subscribe();
            Subscribe findSubscribe = em.find(Subscribe.class, subscribe.getId());
            List<String> allSubscribers = findSubscribe.getSubscribers();

            for(String subscriber : allSubscribers) {

                NewPostDto newPostDto = new NewPostDto();

                try {
                    String userProfileURL = "https://velog.io/@" + subscriber;
                    Document doc = Jsoup.connect(userProfileURL).get();

                    Elements posts = doc.select(".subinfo span");
                    String writeTime = posts.first().text();
                    System.out.println(writeTime);

                    if (writeTime.equals("1분 전") || writeTime.equals("2분 전")) {
                        newPostDto.newPostAuthor.add(subscriber);
                        System.out.println(newPostDto);
                    }

                } catch (IOException e) {
                    return;
                }

            }
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
