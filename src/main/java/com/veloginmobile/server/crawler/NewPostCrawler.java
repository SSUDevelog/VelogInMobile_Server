package com.veloginmobile.server.crawler;

import com.veloginmobile.server.data.entity.Subscribe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;

public class NewPostCrawler {

    EntityManager em;

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

                    Elements posts = doc.select("#root > div.sc-efQSVx.kdrjec.sc-cTAqQK.gdjBUK > div.sc-hiwPVj.dezvna.sc-fXEqDS.hmUoNK > div:nth-child(4) > div.sc-evcjhq.dAPqfe > div > div:nth-child(1) > div.subinfo > span:nth-child(1)");
                    String writeTime = posts.text();


                    if (writeTime.equals("1분 전") && writeTime.equals("방금 전")) {
                        newPostDto.newPostAuthor.add(subscriber);
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
