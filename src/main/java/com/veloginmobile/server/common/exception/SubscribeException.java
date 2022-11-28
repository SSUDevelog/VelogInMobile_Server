package com.veloginmobile.server.common.exception;

import org.springframework.http.HttpStatus;

import java.io.IOException;

public class SubscribeException extends VelogException {
    public SubscribeException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
