package com.veloginmobile.server.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscribeRequestModel {
    private Boolean validate;
    private String userName;
    private String profilePictureURL;
}
