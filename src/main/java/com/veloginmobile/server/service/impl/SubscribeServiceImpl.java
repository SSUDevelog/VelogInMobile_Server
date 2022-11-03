package com.veloginmobile.server.service.impl;

import com.veloginmobile.server.data.dto.subscribe.SubscribePostDto;
import com.veloginmobile.server.data.dto.subscribe.SubscribeRequestDto;
import com.veloginmobile.server.data.dto.subscribe.SubscriberPostResultDto;
import com.veloginmobile.server.data.entity.Subscribe;
import com.veloginmobile.server.data.entity.User;
import com.veloginmobile.server.data.repository.SubscribeRepository;
import com.veloginmobile.server.data.repository.UserRepository;
import com.veloginmobile.server.service.SubscribeService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SubscribeServiceImpl implements SubscribeService {

    SubscribeRepository subscribeRepository;

    UserRepository userRepository;

    @Autowired
    public SubscribeServiceImpl(SubscribeRepository subscribeRepository, UserRepository userRepository) {
        this.subscribeRepository = subscribeRepository;
        this.userRepository = userRepository;
    }


    public SubscriberPostResultDto getSubscribersPost(String uid) throws IOException {//밖에서 유저 객체를 받아야함.
        User user = userRepository.getByUid(uid);//밖으로 빼야함.
        Subscribe subscribe = subscribeRepository.findByUser(user);
        List<String> subscribers = subscribe.getSubscribers();

        return getSubscribersPost(subscribers);
    }

    public SubscriberPostResultDto getSubscribersPost(List<String> subscribers) throws IOException  {

        SubscriberPostResultDto subscriberPostResultDto = new SubscriberPostResultDto();

        for(String sub : subscribers){
            List<SubscribePostDto> subscribePostDtos = getSubscriberPosts(sub);
            subscriberPostResultDto.getSubscribePostDtoList().addAll(subscribePostDtos);
        }
        Collections.sort(subscriberPostResultDto.getSubscribePostDtoList(), SubscribePostDto.compareByDate);

        return subscriberPostResultDto;
    }

    //반환형 나중에 성공여부 DTO로 바꾸기//나중에 실제 존재 여부 체크하기
    public void addSubscribe(String uid, String subscriber){

        User user = userRepository.getByUid(uid);
        Subscribe subscribe = subscribeRepository.findByUser(user);
        if(subscribe == null) {
            subscribe = makeSubscribe(user);
        }

        subscribe.getSubscribers().add(subscriber);
        subscribeRepository.save(subscribe);
    }

    public List<String> getSubscribers(String userName) {

        User user = userRepository.getByUid(userName);
        Subscribe subscribe = subscribeRepository.findByUser(user);

        return subscribe.getSubscribers();
    }

    public List<SubscribePostDto> getSubscriberPosts(String subscriber) throws IOException {

        String userProfileURL = "https://velog.io/@" + subscriber;
        Document doc = Jsoup.connect(userProfileURL).get();

        List<SubscribePostDto> subscribePostDtos = new ArrayList<>();

        Elements posts = doc.select("#root > div.sc-efQSVx.sc-cTAqQK.hKuDqm > div.sc-hiwPVj.cFguvd.sc-dkqQuH > div > div.sc-gGPzkF.idFviV > div.sc-kmQMED > div.sc-gslxeA.leuZzQ");

        for(Element post: posts) {
            SubscribePostDto subscribePostDto = new SubscribePostDto();

            subscribePostDto.setName(subscriber);
            subscribePostDto.setTitle(post.select("a h2").text());
            subscribePostDto.setSummary(post.select("p").text());
            subscribePostDto.setDate(post.select(".subinfo span").get(0).text());
            subscribePostDto.setComment(Integer.parseInt(post.select(".subinfo span").get(1).text().replace("개의 댓글", "")));
            subscribePostDto.setLike(Integer.parseInt(post.select(".subinfo span").get(2).text()));
            subscribePostDto.setImg(post.select("a div img").attr("src"));

            Elements tags = post.select(".tags-wrapper .sc-TBWPX.fOVlQW");
            for(Element tag : tags){
                subscribePostDto.getTag().add(tag.text());
            }

            subscribePostDtos.add(subscribePostDto);
        }

        return subscribePostDtos;
    }

    public Boolean isValidateUser(SubscribeRequestDto subscribeRequestDto, String velogUsername) throws IOException {

        int responseCode = openURL(subscribeRequestDto.getProfileURL());

        if (responseCode == 404) return Boolean.FALSE;
        return Boolean.TRUE;
    }

    public SubscribeRequestDto getVelogUserProfile(Boolean isPresent, SubscribeRequestDto subscribeRequestDto) throws IOException {
        if (isPresent == Boolean.FALSE) {
            subscribeRequestDto.setValidate(Boolean.FALSE);
            return subscribeRequestDto;
        }
        subscribeRequestDto.setValidate(Boolean.TRUE);
        getVelogUserProfilePicture(subscribeRequestDto);
        return subscribeRequestDto;
    }

    private void getVelogUserProfilePicture(SubscribeRequestDto subscribeRequestDto) throws IOException {
        Document document = Jsoup.connect(subscribeRequestDto.getProfileURL()).get();
        Elements profileImageURL = document.select("#root > div.sc-efQSVx.sc-cTAqQK.hKuDqm > div.sc-hiwPVj.cFguvd.sc-dkqQuH > div.sc-jlRLRk.itanDZ.sc-dwsnSq.cXXBgc > div.sc-dUbtfd.gBxoyd > a > img");
        subscribeRequestDto.setProfilePictureURL(profileImageURL.attr("src"));
    }


    private Subscribe makeSubscribe(User user){

        Subscribe subscribe = new Subscribe();

        subscribe.setUser(user);
        return subscribeRepository.save(subscribe);
    }

    private int openURL(String profileURL) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(profileURL).openConnection();
        connection.setRequestMethod("HEAD");
        return connection.getResponseCode();
    }
}
