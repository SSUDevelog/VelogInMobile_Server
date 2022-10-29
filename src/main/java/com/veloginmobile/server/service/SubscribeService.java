package com.veloginmobile.server.service;

import com.veloginmobile.server.data.dto.subscribe.SubscribePostDto;
import com.veloginmobile.server.data.dto.subscribe.SubscriberPostResultDto;
import com.veloginmobile.server.data.entity.Subscribe;

import java.io.IOException;
import java.util.List;

public interface SubscribeService {

    List<SubscribePostDto> getSubscriberPosts(String subscriber) throws IOException;

    SubscriberPostResultDto getSubscribersPost(String userName) throws IOException;

    SubscriberPostResultDto getSubscribersPost(List<String> subscribers) throws IOException;

    void addSubscribe(String uid, String subscriber);

    List<String> getSubscribers(String userName);
}
