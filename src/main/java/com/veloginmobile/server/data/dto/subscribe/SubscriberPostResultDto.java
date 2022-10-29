package com.veloginmobile.server.data.dto.subscribe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubscriberPostResultDto {
    List<SubscribePostDto> subscribePostDtoList = new ArrayList<>();
}
