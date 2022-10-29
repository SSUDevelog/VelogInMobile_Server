package com.veloginmobile.server.service;

import com.veloginmobile.server.config.security.JwtTokenProvider;
import com.veloginmobile.server.controller.SignController;
import com.veloginmobile.server.data.dto.subscribe.SubscribePostDto;
import com.veloginmobile.server.data.dto.subscribe.SubscriberPostResultDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class SubscribeserviceTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private SubscribeService subscribeService;

    @Test
    void getSubscriberPostsTest() throws IOException {
        List<SubscribePostDto> subscribePostDtos = subscribeService.getSubscriberPosts("hyemin916");

        System.out.println(subscribePostDtos);
    }

    @Test
    void sortSubscribersPostTest() throws IOException {
        List<String> groups = new ArrayList<>();
        groups.add("hyemin916");
        groups.add("ngngs");
        groups.add("hamham");

        SubscriberPostResultDto subscriberPostResultDto = subscribeService.getSubscribersPost(groups);
        List<SubscribePostDto> subscribePostDtos = subscriberPostResultDto.getSubscribePostDtoList();

        for(SubscribePostDto dto: subscribePostDtos){
            System.out.println(dto.getDate());
        }
    }
}
