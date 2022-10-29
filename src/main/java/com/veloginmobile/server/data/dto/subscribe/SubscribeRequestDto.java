package com.veloginmobile.server.data.dto.subscribe;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscribeRequestDto {
    private Boolean validate;
    private String userName;
    private String profilePictureURL;
}
