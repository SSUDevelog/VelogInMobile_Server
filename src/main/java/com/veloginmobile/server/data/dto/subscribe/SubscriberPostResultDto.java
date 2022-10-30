package com.veloginmobile.server.data.dto.subscribe;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class SubscriberPostResultDto {
    List<SubscribePostDto> subscribePostDtoList = new ArrayList<>();
}
