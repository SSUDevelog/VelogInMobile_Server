package com.veloginmobile.server.common.exception;

import org.springframework.http.HttpStatus;

public class VelogException  extends Exception {

    private HttpStatus httpStatus;
    public VelogException(HttpStatus httpStatus, String message){
        super(message);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatusCode() {
        return httpStatus.value();
    }

    public String getHttpStatusType() {
        return httpStatus.getReasonPhrase();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
