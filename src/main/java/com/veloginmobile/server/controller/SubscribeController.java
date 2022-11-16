package com.veloginmobile.server.controller;

import com.veloginmobile.server.config.security.JwtTokenProvider;
import com.veloginmobile.server.data.dto.subscribe.SubscribeRequestDto;
import com.veloginmobile.server.data.dto.subscribe.SubscriberPostResultDto;
import com.veloginmobile.server.service.SubscribeService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("subscribe")
public class SubscribeController {

    private final JwtTokenProvider jwtTokenProvider;
    private final SubscribeService subscribeService;
    private final Logger LOGGER = LoggerFactory.getLogger(SignController.class);

    public SubscribeController(JwtTokenProvider jwtTokenProvider, SubscribeService subscribeService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.subscribeService = subscribeService;
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
    public ResponseEntity<SubscribeRequestDto> subscribeInput(@PathVariable String name) throws IOException {
        SubscribeRequestDto subscribeRequestDto = new SubscribeRequestDto(name);
        Boolean isPresent = subscribeService.isValidateUser(subscribeRequestDto, name);
        subscribeService.getVelogUserProfile(isPresent, subscribeRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(subscribeRequestDto);
    }

}