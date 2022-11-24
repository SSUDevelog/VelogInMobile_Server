package com.veloginmobile.server.crawler;

import com.veloginmobile.server.data.entity.Crawling;
import com.veloginmobile.server.data.entity.Subscribe;
import com.veloginmobile.server.data.repository.CrawlingRepository;
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

    CrawlingRepository crawlingRepository;

    public void searchNewPost() throws IOException {
        while (Boolean.TRUE){
            List<Crawling> allSubscribers = crawlingRepository.findAll();

            for(Crawling subscriber : allSubscribers) {

                String velogUser = subscriber.getVelogUserName();
                NewPostDto newPostDto = new NewPostDto();

                String userProfileURL = "https://velog.io/@" + velogUser;
                Document doc = Jsoup.connect(userProfileURL).get();

                Elements posts = doc.select(".subinfo span");
                String writeTime = posts.first().text();
                System.out.println(writeTime);

                if (writeTime.equals("1분 전") || writeTime.equals("2분전")) {
                    newPostDto.newPostAuthor.add(velogUser);
                    System.out.println(newPostDto);
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
