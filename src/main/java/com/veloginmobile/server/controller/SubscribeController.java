package com.veloginmobile.server.controller;

import com.veloginmobile.server.config.security.JwtTokenProvider;
import com.veloginmobile.server.data.dto.subscribe.SubscribeRequestModel;
import com.veloginmobile.server.data.dto.subscribe.SubscriberPostResultDto;
import com.veloginmobile.server.service.SubscribeService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("subscribe")
public class SubscribeController {

    private JwtTokenProvider jwtTokenProvider;
    private SubscribeService subscribeService;
    private SubscribeRequestModel subscribeRequestModel;

    @Autowired
    public SubscribeController(JwtTokenProvider jwtTokenProvider, SubscribeService subscribeService){
        this.jwtTokenProvider = jwtTokenProvider;
        this.subscribeService = subscribeService;
    }

    private int openURL(String profileURL) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(profileURL).openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return responseCode;
        } catch (IOException e) {
            return 0;
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")})
    @GetMapping(value = "/getsubscriber")
    public ResponseEntity<List<String>> getSubscriber(@RequestHeader("X-AUTH-TOKEN") String token) {
        String userName = jwtTokenProvider.getUsername(token);

        List<String> subscribers = subscribeService.getSubscribers(userName);

        return ResponseEntity.status(HttpStatus.OK).body(subscribers);//임시
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")})
    @GetMapping(value = "/addsubscriber/{name}")
    public ResponseEntity<String> addSubscriber(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable String name) {
        String userName = jwtTokenProvider.getUsername(token);

        subscribeService.addSubscribe(userName, name);

        return ResponseEntity.status(HttpStatus.OK).body("Temp");//임시
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")})
    @GetMapping(value = "/subscriberpost")
    public ResponseEntity<SubscriberPostResultDto> getSubscriberPost(@RequestHeader("X-AUTH-TOKEN") String token) throws IOException {
        String userName = jwtTokenProvider.getUsername(token);

        SubscriberPostResultDto subscriberPostResultDto = subscribeService.getSubscribersPost(userName);

        return ResponseEntity.status(HttpStatus.OK).body(subscriberPostResultDto);
    }

    //임시. 나중에 이건 지워줄 예정? 인증이 필요없는 메소드 같아서.
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")})
    @GetMapping("/inputname/{name}")
    @ResponseBody
    public Object subscribeInput(@PathVariable String name) throws IOException {

        this.subscribeRequestModel = new SubscribeRequestModel();
        String userProfileURL = "https://velog.io/@" + name;
        Document document = Jsoup.connect(userProfileURL).get();

        if (openURL(userProfileURL) == 404) {
            subscribeRequestModel.setValidate(Boolean.FALSE);
            subscribeRequestModel.setUserName("");
            return subscribeRequestModel;
        } else {
            subscribeRequestModel.setUserName(name);
            subscribeRequestModel.setValidate(Boolean.TRUE);
        }

        Elements profileImageURL = document.select("#root > div.sc-efQSVx.sc-cTAqQK.hKuDqm > div.sc-hiwPVj.cFguvd.sc-dkqQuH > div.sc-jlRLRk.itanDZ.sc-dwsnSq.cXXBgc > div.sc-dUbtfd.gBxoyd > a > img");
        subscribeRequestModel.setProfilePictureURL(profileImageURL.attr("src"));
        return subscribeRequestModel;
    }
}