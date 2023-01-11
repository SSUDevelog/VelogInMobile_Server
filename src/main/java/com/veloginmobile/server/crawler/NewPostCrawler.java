package com.veloginmobile.server.crawler;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.veloginmobile.server.data.dto.notification.NotificationDto;
import com.veloginmobile.server.data.entity.Target;
import com.veloginmobile.server.data.repository.CrawlingRepository;
import com.veloginmobile.server.service.NotificationService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Component
public class NewPostCrawler {

    CrawlingRepository crawlingRepository;
    NotificationService notificationService;

    @Autowired
    public NewPostCrawler (CrawlingRepository crawlingRepository, NotificationService notificationService) {
        this.crawlingRepository = crawlingRepository;
        this.notificationService = notificationService;
    }

    public void searchNewPost() throws IOException, FirebaseMessagingException {
        List<Target> allSubscribers = crawlingRepository.findAll();
        ArrayList<NotificationDto> notificationDtos = new ArrayList<>();

        for(Target subscriber : allSubscribers) {

            String velogUser = subscriber.getVelogUserName();

            String userProfileURL = "https://velog.io/@" + velogUser;
            Document doc = Jsoup.connect(userProfileURL).get();

            Element post = doc.select("#root > div > div > div > div > div").get(6).select("> div").get(0);
            String writeTime = post.select(".subinfo span").get(0).text();

            if (writeTime.equals("1분 전") || writeTime.equals("2분전") || writeTime.equals("3분전")|| writeTime.equals("4분전")|| writeTime.equals("5분전")) {
                NotificationDto notificationDto = new NotificationDto();
                notificationDto.setUserName(velogUser);
                notificationDto.setBody(post.select("p").text());
                notificationDto.setLink(post.select("> a").attr("href"));
                notificationDto.setTitle(post.select("a h2").text());

                notificationDtos.add(notificationDto);
            }
        }

        for (NotificationDto notificationDto : notificationDtos){
            notificationService.sendByGroupName(notificationDto.getUserName(), notificationDto);
        }
    }
}
