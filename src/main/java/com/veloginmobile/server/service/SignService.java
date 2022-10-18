package com.veloginmobile.server.service;

import com.veloginmobile.server.data.dto.SignInResultDto;
import com.veloginmobile.server.data.dto.SignUpResultDto;

public interface SignService {

    SignUpResultDto signUp(String id, String password, String name, String role);

    SignInResultDto signIn(String id, String password) throws RuntimeException;

}
