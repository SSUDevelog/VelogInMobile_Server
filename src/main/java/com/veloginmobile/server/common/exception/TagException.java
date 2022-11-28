package com.veloginmobile.server.common.exception;

import org.springframework.http.HttpStatus;

public class TagException  extends VelogException {
    public TagException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
