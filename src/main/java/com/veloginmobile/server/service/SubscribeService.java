package com.veloginmobile.server.service;

import com.veloginmobile.server.common.exception.SubscribeException;
import com.veloginmobile.server.data.dto.subscribe.SubscribePostDto;
import com.veloginmobile.server.data.dto.subscribe.SubscribeRequestDto;
import com.veloginmobile.server.data.dto.subscribe.SubscriberPostResultDto;
import com.veloginmobile.server.data.entity.Subscribe;

import java.io.IOException;
import java.util.List;

public interface SubscribeService {

    List<SubscribePostDto> getSubscriberPosts(String subscriber) throws IOException;

    SubscriberPostResultDto getSubscribersPost(List<String> subscribers) throws IOException, SubscribeException;

    SubscriberPostResultDto getSubscribersPost(String uid) throws IOException, SubscribeException;

    void addSubscribe(String uid, String subscriber) throws SubscribeException;

    List<String> getSubscribers(String userName) throws SubscribeException;

    Boolean isValidateUser(SubscribeRequestDto subscribeRequestDto, String velogUsername) throws IOException;

    SubscribeRequestDto getVelogUserProfile(Boolean isPresent, SubscribeRequestDto subscribeRequestDto) throws IOException;
}
