package com.veloginmobile.server.service.impl;

import com.veloginmobile.server.common.exception.SubscribeException;
import com.veloginmobile.server.data.dto.subscribe.SubscribePostDto;
import com.veloginmobile.server.data.dto.subscribe.SubscribeRequestDto;
import com.veloginmobile.server.data.dto.subscribe.SubscriberPostResultDto;
import com.veloginmobile.server.data.entity.Subscribe;
import com.veloginmobile.server.data.entity.Target;
import com.veloginmobile.server.data.entity.User;
import com.veloginmobile.server.data.repository.SubscribeRepository;
import com.veloginmobile.server.data.repository.TargetRepository;
import com.veloginmobile.server.data.repository.UserRepository;
import com.veloginmobile.server.service.SubscribeService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    TargetRepository targetRepository;

    @Autowired
    public SubscribeServiceImpl(SubscribeRepository subscribeRepository, UserRepository userRepository, TargetRepository targetRepository) {
        this.subscribeRepository = subscribeRepository;
        this.userRepository = userRepository;
        this.targetRepository = targetRepository;
    }


    public SubscriberPostResultDto getSubscribersPost(String uid) throws IOException, SubscribeException {
        User user = userRepository.getByUid(uid);//밖으로 빼야함.
        List<String> subscribers = getSubscribers(user);

        return getSubscribersPost(subscribers);
    }

    public SubscriberPostResultDto getSubscribersPost(List<String> subscribers) throws IOException, SubscribeException {

        SubscriberPostResultDto subscriberPostResultDto = new SubscriberPostResultDto();

        for (String sub : subscribers) {
            List<SubscribePostDto> subscribePostDtos = getSubscriberPosts(sub);
            if(subscribePostDtos != null) {
                subscriberPostResultDto.getSubscribePostDtoList().addAll(subscribePostDtos);
            }
        }
        Collections.sort(subscriberPostResultDto.getSubscribePostDtoList(), SubscribePostDto.compareByDate);
        if(subscriberPostResultDto.getSubscribePostDtoList() == null){
            throw new SubscribeException(HttpStatus.ACCEPTED, "불러올 포스트가 없습니다.");
        }

        return subscriberPostResultDto;
    }

    //반환형 나중에 성공여부 DTO로 바꾸기
    public void addSubscribe(String uid, String subscriber) throws SubscribeException {

        User user = userRepository.getByUid(uid);

        if(getSubscribers(user).contains(subscriber)){
            throw new SubscribeException(HttpStatus.BAD_REQUEST, "이미 추가한 구독대상입니다.");
        }

        makeSubscribe(user, subscriber);
    }

    public List<String> getSubscribers(String userName){

        User user = userRepository.getByUid(userName);

        return getSubscribers(user);
    }

    public List<SubscribePostDto> getSubscriberPosts(String subscriber) throws IOException {

        String userProfileURL = "https://velog.io/@" + subscriber;
        Document doc = Jsoup.connect(userProfileURL).get();

        List<SubscribePostDto> subscribePostDtos = new ArrayList<>();

        Elements posts = doc.select("#root > div > div > div > div > div").get(6).select("> div");

        for (Element post : posts) {

            SubscribePostDto subscribePostDto = new SubscribePostDto();

            subscribePostDto.setName(subscriber);
            subscribePostDto.setTitle(post.select("a h2").text());
            subscribePostDto.setSummary(post.select("p").text());
            subscribePostDto.setDate(post.select(".subinfo span").get(0).text());
            subscribePostDto.setComment(Integer.parseInt(post.select(".subinfo span").get(1).text().replace("개의 댓글", "")));
            subscribePostDto.setLike(Integer.parseInt(post.select(".subinfo span").get(2).text()));
            subscribePostDto.setImg(post.select("a div img").attr("src"));
            subscribePostDto.setUrl(post.select("> a").attr("href"));

            Elements tags = post.select(".tags-wrapper a");
            for (Element tag : tags) {
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

    private List<String> getSubscribers(User user){
        List<Subscribe> subscribes = user.getSubscribes();
        List<String> subscribers = new ArrayList<>();

        for(Subscribe subscribe: subscribes){
            subscribers.add(subscribe.getTarget().getVelogUserName());
        }

        return subscribers;
    }

    private void getVelogUserProfilePicture(SubscribeRequestDto subscribeRequestDto) throws IOException {
        Document document = Jsoup.connect(subscribeRequestDto.getProfileURL()).get();
        Elements profileImageURL = document.select("#root > div.sc-efQSVx.sc-cTAqQK.hKuDqm > div.sc-hiwPVj.cFguvd.sc-dkqQuH > div.sc-jlRLRk.itanDZ.sc-dwsnSq.cXXBgc > div.sc-dUbtfd.gBxoyd > a > img");
        subscribeRequestDto.setProfilePictureURL(profileImageURL.attr("src"));
    }

    private void makeSubscribe(User user, String subscriber){
        Target target = targetRepository.getByVelogUserName(subscriber);
        if(target == null){
            target = new Target();
            target.setVelogUserName(subscriber);
            targetRepository.save(target);
        }

        makeSubscribe(user, target);
    }

    private void makeSubscribe(User user, Target target) {

        Subscribe subscribe = new Subscribe();

        subscribe.setUser(user);
        subscribe.setTarget(target);
        subscribeRepository.save(subscribe);
    }

    private int openURL(String profileURL) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(profileURL).openConnection();
        connection.setRequestMethod("HEAD");
        return connection.getResponseCode();
    }
}
