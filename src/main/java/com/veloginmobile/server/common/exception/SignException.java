package com.veloginmobile.server.common.exception;

import org.springframework.http.HttpStatus;

public class SignException extends VelogException{
    public SignException(HttpStatus httpStatus, String message){
        super(httpStatus, message);
    }
}
