package com.veloginmobile.server.controller;

import com.veloginmobile.server.config.security.JwtTokenProvider;
import com.veloginmobile.server.data.dto.subscribe.SubscribeRequestDto;
import com.veloginmobile.server.data.dto.subscribe.SubscriberPostResultDto;
import com.veloginmobile.server.service.SubscribeService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final JwtTokenProvider jwtTokenProvider;
    private final SubscribeService subscribeService;
    private final SubscribeRequestDto subscribeRequestDto;//별도 서비스를 만들어야함.
    private final Logger LOGGER = LoggerFactory.getLogger(SignController.class);

    public SubscribeController(JwtTokenProvider jwtTokenProvider, SubscribeService subscribeService){
        this.jwtTokenProvider = jwtTokenProvider;
        this.subscribeService = subscribeService;
        this.subscribeRequestDto = new SubscribeRequestDto();
    }

    private int openURL(String profileURL) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(profileURL).openConnection();
        connection.setRequestMethod("HEAD");
        return connection.getResponseCode();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")})
    @GetMapping(value = "/getsubscriber")
    public ResponseEntity<List<String>> getSubscriber(@RequestHeader("X-AUTH-TOKEN") String token) {//제네릭 안의 제네릭 -> 별도응답 객체를 만들어야함!
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
    @GetMapping(value = "/subscriberpost")//responseentity가 없어도 되는지 테스트 되는지 확인해보기//403 402같은 코드를 던질경우 쓰기
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
    public Object subscribeInput(@PathVariable String name) {//별도서비스에서 익셉션을 비즈니스 로직에서 처리해야함. 서비스가 알아서 처리해야함. 글로벌 익셉션에서 커스텀 익셉션?

        String userProfileURL = "https://velog.io/@" + name;
        int responseCode = 0;
        Document document = null;

        try {
            document = Jsoup.connect(userProfileURL).get();
            responseCode = openURL(userProfileURL);
        }
        catch (IOException e) {
            LOGGER.error("에러가 발생했습니다. 다시 시도하세요.");
        }

        if (responseCode == 404) {
            subscribeRequestDto.setValidate(Boolean.FALSE);
            subscribeRequestDto.setUserName("");
            return subscribeRequestDto;
        }

        subscribeRequestDto.setUserName(name);
        subscribeRequestDto.setValidate(Boolean.TRUE);

        Elements profileImageURL = document.select("#root > div.sc-efQSVx.sc-cTAqQK.hKuDqm > div.sc-hiwPVj.cFguvd.sc-dkqQuH > div.sc-jlRLRk.itanDZ.sc-dwsnSq.cXXBgc > div.sc-dUbtfd.gBxoyd > a > img");
        subscribeRequestDto.setProfilePictureURL(profileImageURL.attr("src"));
        return subscribeRequestDto;
    }
}