package com.veloginmobile.server.crawler;

import com.veloginmobile.server.data.entity.Target;
import com.veloginmobile.server.data.repository.CrawlingRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class NewPostCrawler {

    CrawlingRepository crawlingRepository;

    @Autowired
    public NewPostCrawler (CrawlingRepository crawlingRepository) {
        this.crawlingRepository = crawlingRepository;
    }

    public void searchNewPost() throws IOException {
        List<Target> allSubscribers = crawlingRepository.findAll();

        for(Target subscriber : allSubscribers) {

            String velogUser = subscriber.getVelogUserName();
            NewPostDto newPostDto = new NewPostDto();

            String userProfileURL = "https://velog.io/@" + velogUser;
            Document doc = Jsoup.connect(userProfileURL).get();

            Elements posts = doc.select(".subinfo span");
            String writeTime = posts.first().text();

            if (writeTime.equals("1분 전") || writeTime.equals("2분전") || writeTime.equals("3분전")|| writeTime.equals("4분전")|| writeTime.equals("5분전")) {
                newPostDto.newPostAuthor.add(velogUser);
            }
        }
        // 알림 함수 호출 부분
    }
}
