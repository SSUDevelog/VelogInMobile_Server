package com.velogm.server_velogm.service;

import com.velogm.server_velogm.data.dto.SignInResultDto;
import com.velogm.server_velogm.data.dto.SignUpResultDto;

public interface SignService {

    SignUpResultDto signUp(String id, String password, String name, String role);

    SignInResultDto signIn(String id, String password) throws RuntimeException;

}
