package com.veloginmobile.server.service;

import com.veloginmobile.server.data.dto.sign.SignInDto;
import com.veloginmobile.server.data.dto.sign.SignInResultDto;
import com.veloginmobile.server.data.dto.sign.SignUpDto;
import com.veloginmobile.server.data.dto.sign.SignUpResultDto;

public interface SignService {

    SignUpResultDto signUp(SignUpDto signUpDto);

    SignInResultDto signIn(SignInDto signInDto) throws RuntimeException;

}
