package com.veloginmobile.server.service;

import com.veloginmobile.server.common.exception.SignException;
import com.veloginmobile.server.data.dto.sign.*;

public interface SignService {

    SignUpResultDto signUp(SignUpDto signUpDto) throws SignException;

    SignInResultDto signIn(SignInDto signInDto) throws SignException;

    SignOutResultDto signOut(SignOutDto signOutDto) throws SignException;
}
